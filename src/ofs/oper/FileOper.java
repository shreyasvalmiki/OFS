package ofs.oper;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;

import ofs.ds.*;
import ofs.utils.*;

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
	protected int updateNewFileDetails(int fileMode, int fileSize){
		int inodePos;
		int blockPos;
		int blockCount;
		Date date = new Date();
		long dateLong = GeneralUtils.getLongFromDate(date);
		sBlock = FSUtils.getSuperblock(raFile);
		
		blockCount = (int) Math.ceil(fileSize/sBlock.getBlockSize());
		
		if(sBlock.getFreeBlocksCount() < blockCount || sBlock.getFreeInodesCount() < 1){
			return Constants.ERR_INSUFF_MEM;
		}
		if(blockCount > Constants.DIRECT_LINKS_CNT){
			return Constants.ERR_FILE_TOO_LARGE;
		}
		blockBitmap = FSUtils.getBitmap(raFile, sBlock.getBlocksCount(), true);
		inodeBitmap = FSUtils.getBitmap(raFile, sBlock.getInodeCount(), false);
		
		inodePos = inodeBitmap.setFirstEmptyBit();
		
		curInodeLoc = inodePos * Constants.INODE_SIZE + sBlock.getFirstInode();
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
				blockPos = (blockPos + sBlock.getFirstDataBlock())*sBlock.getBlockSize();
				curInode.setBlock(i, blockPos);
			}
		}
		sBlock.setFreeBlocksCount(sBlock.getFreeBlocksCount() - blockCount);
		sBlock.setFreeInodesCount(sBlock.getFreeInodesCount() - 1);
		
		FSUtils.updateSuperblock(raFile, sBlock);
		FSUtils.updateInode(raFile, curInode, inodePos);
		
		return Constants.NO_ERROR;
	}
	
	protected int addFile(Inode parInode, String fileName, int fileType, int fileMode, int size){
		int err = Constants.NO_ERROR;
		this.parInode = parInode;
		long prevPos;
		long pos;
		long tailPos;
		int recordLength;
		long posDiff;
		long tailDiff;
		Bitmap blockBitmap;
		DirectoryEntry prevDirEntry = new DirectoryEntry();
		DirectoryEntry dirEntry = new DirectoryEntry();
		DirectoryEntry tailEntry = new DirectoryEntry();
		err = updateNewFileDetails(fileMode,size);
		if(err != Constants.NO_ERROR)
		{
			return err;
		}
		blockBitmap = FSUtils.getBitmap(raFile, sBlock.getBlocksCount(), true);
		
		recordLength = (Constants.DIR_ENTRY_SIZE_SANS_NAME + fileName.length());
		recordLength = recordLength % 4 == 0 ? recordLength : recordLength + (4 - recordLength%4);
		
		prevPos = FSUtils.getLastDirEntry(raFile, parInode.getBlock(0));
		parInodeLoc =(int) prevPos;
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
		tailPos = pos+prevDirEntry.getRecordLength();
		if(posDiff - sBlock.getBlockSize() > 0){
			tailPos = blockBitmap.setFirstEmptyBit();
			if(pos == -1){
				return Constants.ERR_INSUFF_MEM;
			}
			else{
				blockBitmap.unsetAtPos((int)(pos/sBlock.getBlockSize() - sBlock.getFirstDataBlock()));
				tailPos = (sBlock.getFirstDataBlock() + pos) * sBlock.getBlockSize(); 
			}
		}	
		
		prevDirEntry.setRecordLength((int)(pos - prevPos));
		
		dirEntry.setInode(curInodeLoc);
		dirEntry.setFileType((byte)fileType);
		dirEntry.setName(fileName);
		dirEntry.setNameLength((byte)fileName.length());
		dirEntry.setRecordLength((int)(tailPos - pos));
		
		tailDiff = (int)Math.floor(tailPos/sBlock.getBlockSize());
		tailDiff = sBlock.getBlockSize() - (tailPos - tailDiff);
		
		tailEntry.setInode(0);
		tailEntry.setFileType((byte)0);
		tailEntry.setName("");
		tailEntry.setNameLength((byte)0);
		dirEntry.setRecordLength((int)tailDiff);
		
		FSUtils.updateDirEntry(raFile, prevDirEntry, prevPos);
		FSUtils.updateDirEntry(raFile, dirEntry, pos);
		FSUtils.updateDirEntry(raFile, tailEntry, tailPos);
		
		return err;
		
	}
	
}
