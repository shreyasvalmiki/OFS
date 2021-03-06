package ofs.oper;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;

import ofs.ds.*;
import ofs.utils.*;
/**
 * Handles basic file operations. Superclass to DirectoryOper and RegFileOper.
 * @author shreyasvalmiki
 *
 */
public class FileOper {

	protected Superblock sBlock = new Superblock();
	protected Bitmap inodeBitmap;
	protected Bitmap blockBitmap;
	protected RandomAccessFile raFile;
	protected Inode curInode = new Inode();
	protected Inode parInode = new Inode();
	protected int curInodeLoc = 0;
	protected ArrayList<DirectoryEntry> dirEntryList = new ArrayList<DirectoryEntry>();
	protected int parInodeLoc = 0;
	public FileOper(RandomAccessFile raFile){
		this.raFile = raFile;
		dirEntryList.clear();
	}
	
	/**
	 * Updates Inode, block bitmap and inode bitmap for the new file (of all types, including directories)
	 * @param fileMode
	 * @param fileSize
	 * @return
	 */
	protected int updateNewFileDetails(int fileMode, int fileSize){
		int inodePos;
		int blockPos;
		int blockCount;
		Date date = new Date();
		long dateLong = GeneralUtils.getLongFromDate(date);
		sBlock = FSUtils.getSuperblock(raFile);
		
		blockCount = (int)Math.floor(fileSize/sBlock.getBlockSize());
		if(fileSize%sBlock.getBlockSize() != 0){
			++blockCount;
		}
		
		if(sBlock.getFreeBlocksCount() < blockCount || sBlock.getFreeInodesCount() < 1){
			return Constants.ERR_INSUFF_MEM;
		}
		if(blockCount > Constants.DIRECT_LINKS_CNT){
			return Constants.ERR_FILE_TOO_LARGE;
		}
		blockBitmap = FSUtils.getBitmap(raFile, sBlock.getBlocksCount(), true);
		inodeBitmap = FSUtils.getBitmap(raFile, sBlock.getInodeCount(), false);
		
		inodePos = inodeBitmap.setFirstEmptyBit();

		curInodeLoc = (inodePos-1) * Constants.INODE_SIZE + sBlock.getFirstInode();
		curInode.setMode(fileMode);
		curInode.setSize(fileSize);
		curInode.setAccessedTime(dateLong);
		curInode.setCreatedTime(dateLong);
		curInode.setModifiedTime(dateLong);
		curInode.setDeletedTime(0);
		curInode.setBlocksCount(blockCount);

		for(int i=0;i<blockCount;++i){
			blockPos = blockBitmap.setFirstEmptyBit();
			if(blockPos == -1){
				return Constants.ERR_INSUFF_MEM;
			}
			else
			{
				blockPos = (blockPos + sBlock.getFirstDataBlock() - 1)*sBlock.getBlockSize();
				curInode.setBlock(i, blockPos);
			}
		}
		sBlock.setFreeBlocksCount(sBlock.getFreeBlocksCount() - blockCount);
		sBlock.setFreeInodesCount(sBlock.getFreeInodesCount() - 1);



		return Constants.NO_ERROR;
	}
	
	/**
	 * Adds the directory entry of the file
	 * @param parInode
	 * @param fileName
	 * @param fileType
	 * @param fileMode
	 * @param size
	 * @return
	 */
	protected int addFile(Inode parInode, String fileName, int fileType, int fileMode, int size){
		int err = Constants.NO_ERROR;
		this.parInode = parInode;
		long prevPos;
		long pos;
		long tailPos;
		int recordLength;
		long posDiff;
		long tailDiff;
		DirectoryEntry prevDirEntry = new DirectoryEntry();
		DirectoryEntry dirEntry = new DirectoryEntry();
		DirectoryEntry tailEntry = new DirectoryEntry();

		if(FSUtils.isFileNameExists(raFile, fileName, parInode.getBlock(0))){
			return Constants.ERR_FILE_NAME_EXISTS;
		}

		err = updateNewFileDetails(fileMode,size);
		if(err != Constants.NO_ERROR)
		{
			return err;
		}

		recordLength = (Constants.DIR_ENTRY_SIZE_SANS_NAME + fileName.length()*Constants.SIZE_OF_CHAR);
		recordLength = recordLength % 4 == 0 ? recordLength : recordLength + (4 - recordLength%4);

		prevPos = FSUtils.getTailDirEntry(raFile, parInode.getBlock(0));
		prevDirEntry = FSUtils.getDirEntry(raFile, prevPos);
		posDiff = prevPos+prevDirEntry.getRecordLength() - parInode.getBlock(0);
		pos = prevPos+prevDirEntry.getRecordLength();
		if(posDiff - sBlock.getBlockSize() > 0){
			pos = blockBitmap.setFirstEmptyBit();
			if(pos == -1){
				return Constants.ERR_INSUFF_MEM;
			}
			else{
				pos = (sBlock.getFirstDataBlock() + pos) * sBlock.getBlockSize(); 
			}
		}
		posDiff = pos+prevDirEntry.getRecordLength() - parInode.getBlock(0);
		tailPos = pos+recordLength;
		if(posDiff - sBlock.getBlockSize() > 0){
			tailPos = blockBitmap.setFirstEmptyBit();
			if(pos == -1){
				return Constants.ERR_INSUFF_MEM;
			}
			else{
				tailPos = (sBlock.getFirstDataBlock() + pos) * sBlock.getBlockSize(); 
			}
		}

		prevDirEntry.setRecordLength((int)(pos - prevPos));

		dirEntry.setInode(curInodeLoc);
		dirEntry.setFileType((byte)fileType);
		dirEntry.setName(fileName);
		dirEntry.setNameLength((byte)fileName.length());
		dirEntry.setRecordLength((int)(tailPos - pos));

		tailDiff = tailPos%sBlock.getBlockSize();
		tailDiff = sBlock.getBlockSize() - tailDiff;

		tailEntry.setInode(0);
		tailEntry.setFileType((byte)0);
		tailEntry.setName("");
		tailEntry.setNameLength((byte)0);
		tailEntry.setRecordLength((int)tailDiff);

		FSUtils.updateDirEntry(raFile, prevDirEntry, prevPos);
		FSUtils.updateDirEntry(raFile, dirEntry, pos);
		FSUtils.updateDirEntry(raFile, tailEntry, tailPos);

		FSUtils.updateBitmap(raFile, blockBitmap, (Constants.BLOCK_BITMAP_BLK_POS-1)*sBlock.getBlockSize());

		FSUtils.updateSuperblock(raFile, sBlock);
		FSUtils.updateInode(raFile, curInode, curInodeLoc);

		FSUtils.updateBitmap(raFile, inodeBitmap, (Constants.INODE_BITMAP_BLK_POS-1)*sBlock.getBlockSize());

		return err;
	}

	/**
	 * Removes file (also directory) from the filesystem.
	 * Removes all elements from a directory recursively
	 * @param parInodePos
	 * @param fileName
	 * @return
	 */
	protected int deleteFile(long parInodePos, String fileName){
		Inode parInode = FSUtils.getInode(raFile, parInodePos);
		ArrayList<DirectoryEntry>dirEntryList = new ArrayList<>();
		ArrayList<DirectoryEntry>parDirEntryList = FSUtils.getDirEntryList(raFile, parInode.getBlock(0));
		long inodePos = getInodeWithName(fileName, parDirEntryList);
		if(inodePos == -1 || fileName.equals("") || fileName.equals(".") || fileName.equals("..")){
			return Constants.ERR_FILE_DOES_NOT_EXIST;
		}

		Inode inode = FSUtils.getInode(raFile, inodePos);
		if(inode.getMode() == Constants.DIR){
			dirEntryList = FSUtils.getDirEntryList(raFile, inode.getBlock(0));
			for(DirectoryEntry entry: dirEntryList){
				deleteFile(inodePos,entry.getName());
			}
		}
		updateNeighboringEntries(parInodePos,fileName);
		FSUtils.deleteComponents(raFile, inodePos);
		
		return Constants.NO_ERROR;
	}
	/**
	 * To delete a file, the adjacent directory entries are modified to severe the link.
	 * Disadvantage: the memory is lost forever -- there is no defragmentation routines, yet.
	 * @param inodePos
	 * @param fileName
	 */
	protected void updateNeighboringEntries(long inodePos, String fileName){
		Inode inode = FSUtils.getInode(raFile, inodePos);
		ArrayList<DirectoryEntry>entryList = new ArrayList<>();
		entryList = FSUtils.getThisAndPrevEntry(raFile, fileName, inode.getBlock(0));
		DirectoryEntry prevEntry = entryList.get(0);
		DirectoryEntry thisEntry = entryList.get(1);
		
		if(prevEntry.getInode() == 0||prevEntry.getName().equals("")){
			thisEntry.setName("");
			FSUtils.updateDirEntry(raFile, thisEntry, FSUtils.getDirEntryPos(raFile, fileName, inode.getBlock(0)));
		}
		else{
			prevEntry.setRecordLength(prevEntry.getRecordLength()+thisEntry.getRecordLength());
			FSUtils.updateDirEntry(raFile, prevEntry, FSUtils.getDirEntryPos(raFile, prevEntry.getName(), inode.getBlock(0)));
		}
	}

	/**
	 * Gets the inode position with the name of the file.
	 * @param name
	 * @param dirList
	 * @return
	 */
	public int getInodeWithName(String name, ArrayList<DirectoryEntry> dirList){
		for(DirectoryEntry entry: dirList){
			if(entry.getName().equals(name) && entry.getFileType() == Constants.FT_DIR){
				return entry.getInode();
			}
		}
		return -1;
	}
}
