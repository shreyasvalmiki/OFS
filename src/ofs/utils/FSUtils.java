package ofs.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import ofs.ds.*;

/**
 * @author shreyasvalmiki
 * Has the utilities necessary for getting from, or updating the filesystem random access file
 */
public class FSUtils {
	public static void updateSuperblock(RandomAccessFile raFile,Superblock sBlock){
		try{
			raFile.seek(1);
			raFile.writeInt(sBlock.getBlockSize());
			raFile.writeInt(sBlock.getInodeCount());
			raFile.writeLong(sBlock.getWTime());
			raFile.writeInt(sBlock.getInodeSize());
			raFile.writeInt(sBlock.getFreeBlocksCount());
			raFile.writeInt(sBlock.getFreeInodesCount());
			raFile.writeInt(sBlock.getBlocksCount());
			raFile.writeInt(sBlock.getFirstDataBlock());
			raFile.writeInt(sBlock.getFirstInode());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Writes the bitmap to the filesystem
	 * @param raFile
	 * @param bitmap
	 * @param pos
	 */
	public static void updateBitmap(RandomAccessFile raFile,Bitmap bitmap, long pos){	
		int words = bitmap.getWords();
		int[] map = bitmap.getMap();
		try {
			raFile.seek(pos);
			for(int i = 0; i<words; i++){
				raFile.writeInt(map[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Writes inode to the filesystem
	 * @param raFile
	 * @param inode
	 * @param pos
	 */
	public static void updateInode(RandomAccessFile raFile, Inode inode, long pos){
		try{
			raFile.seek(pos);

			raFile.writeInt(inode.getMode());
			raFile.writeInt(inode.getSize());
			raFile.writeLong(inode.getAccessedTime());
			raFile.writeLong(inode.getCreatedTime());
			raFile.writeLong(inode.getModifiedTime());
			raFile.writeLong(inode.getDeletedTime());
			raFile.writeByte(inode.getLinksCount());
			raFile.writeInt(inode.getBlocksCount());

			for(int i = 0; i < Constants.BLOCKS_PER_INODE; ++i){
				raFile.writeInt(inode.getBlock(i));
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the the initial directory entries for any new directory to the filesystem 
	 * @param raFile
	 * @param curInodeIdx
	 * @param parInodeIdx
	 * @param isRoot
	 * @param pos
	 */
	public static void updateInitDirEntry(RandomAccessFile raFile,int curInodeIdx, int parInodeIdx,boolean isRoot, long pos){
		try{
			Superblock sBlock = new Superblock();
			sBlock = getSuperblock(raFile);
			raFile.seek(pos);

			raFile.writeInt(curInodeIdx);
			raFile.writeInt(Constants.INIT_DIR_ENTRY_SIZE);
			raFile.writeByte(1);
			raFile.writeByte(Constants.FT_DIR);
			raFile.writeChars(".");
			pos += Constants.INIT_DIR_ENTRY_SIZE;
			if(!isRoot){
				raFile.seek(pos);

				raFile.writeInt(parInodeIdx);
				raFile.writeInt(Constants.INIT_PAR_DIR_ENTRY_SIZE);
				raFile.writeByte(2);
				raFile.writeByte(Constants.FT_DIR);
				raFile.writeChars("..");

				pos += Constants.INIT_PAR_DIR_ENTRY_SIZE;
			}

			raFile.seek(pos);

			raFile.writeInt(0);
			raFile.writeInt(sBlock.getBlockSize()-(Constants.INIT_DIR_ENTRY_SIZE*2));
			raFile.writeByte(0);
			raFile.writeByte(Constants.FT_DIR);
			raFile.writeChars("");

		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the superblock from the filesystem
	 * @param raFile
	 * @return
	 */
	public static Superblock getSuperblock(RandomAccessFile raFile){
		Superblock sBlock = new Superblock();
		try {
			raFile.seek(1);
			sBlock.setBlockSize(raFile.readInt());
			sBlock.setInodeCount(raFile.readInt());
			sBlock.setWTime(raFile.readLong());
			sBlock.setInodeSize(raFile.readInt());
			sBlock.setFreeBlocksCount(raFile.readInt());
			sBlock.setFreeInodesCount(raFile.readInt());
			sBlock.setBlocksCount(raFile.readInt());
			sBlock.setFirstDataBlock(raFile.readInt());
			sBlock.setFirstInode(raFile.readInt());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sBlock;
	}
	
	/**
	 * Gets an inode from the filesystem
	 * @param raFile
	 * @param pos
	 * @return
	 */
	public static Inode getInode(RandomAccessFile raFile, long pos){
		Inode inode = new Inode();
		try{
			raFile.seek(pos);
			inode.setMode(raFile.readInt());
			inode.setSize(raFile.readInt());
			inode.setAccessedTime(raFile.readLong());
			inode.setCreatedTime(raFile.readLong());
			inode.setModifiedTime(raFile.readLong());
			inode.setDeletedTime(raFile.readLong());
			inode.setLinksCount(raFile.readByte());
			inode.setBlocksCount(raFile.readInt());
			for(int i=0;i<15;i++){
				inode.setBlock(i, raFile.readInt());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inode;
	}
	
	/**
	 * Gets a bitmap from the filesystem
	 * @param raFile
	 * @param size
	 * @param isBlockBitmap
	 * @return
	 */
	public static Bitmap getBitmap(RandomAccessFile raFile,int size, boolean isBlockBitmap){
		Bitmap bitmap = new Bitmap(size);
		Superblock sBlock = getSuperblock(raFile);
		try{
			if(isBlockBitmap){
				raFile.seek((Constants.BLOCK_BITMAP_BLK_POS-1) * sBlock.getBlockSize());
			}
			else{
				raFile.seek((Constants.INODE_BITMAP_BLK_POS-1) * sBlock.getBlockSize());
			}
			for(int i = 0; i<bitmap.getWords();++i){
				bitmap.setMapAtPos(i, raFile.readInt());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * Gets a directory entry from the filesystem
	 * @param raFile
	 * @param pos
	 * @return
	 */
	public static DirectoryEntry getDirEntry(RandomAccessFile raFile, long pos){
		DirectoryEntry dirEntry = new DirectoryEntry();
		String name = "";
		try{
			raFile.seek(pos);
			
			dirEntry.setInode(raFile.readInt());
			dirEntry.setRecordLength(raFile.readInt());
			dirEntry.setNameLength(raFile.readByte());
			dirEntry.setFileType(raFile.readByte());
			//name = raFile.readUTF();
			for(int i = 0; i<dirEntry.getNameLength(); i++){
				name += String.valueOf(raFile.readChar());
			}
			dirEntry.setName(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dirEntry;
	}
	
	/**
	 * Gets the trailing directory entry for a directory
	 * @param raFile
	 * @param pos
	 * @return
	 */
	public static long getTailDirEntry(RandomAccessFile raFile,long pos){
		DirectoryEntry entry = new DirectoryEntry();
		DirectoryEntry nextEntry = new DirectoryEntry();
		while(true){
			entry = getDirEntry(raFile,pos);
			nextEntry = getDirEntry(raFile,pos+entry.getRecordLength());
			if(nextEntry.getInode() == 0){
				return pos;
			}
			pos += entry.getRecordLength();
		}
	}
	
	/**
	 * Gets the previous directory entry along with the current directory entry
	 * @param raFile
	 * @param fileName
	 * @param pos
	 * @return
	 */
	public static ArrayList<DirectoryEntry> getThisAndPrevEntry(RandomAccessFile raFile, String fileName,long pos){
		ArrayList<DirectoryEntry> dirEntryList = new ArrayList<>();
		DirectoryEntry prevEntry = new DirectoryEntry();
		DirectoryEntry thisEntry = new DirectoryEntry();
		DirectoryEntry nextEntry = new DirectoryEntry();
		boolean isDone = false;
		while(!isDone){
			prevEntry = thisEntry;
			thisEntry = getDirEntry(raFile,pos);
			nextEntry = getDirEntry(raFile,pos+thisEntry.getRecordLength());
			if(thisEntry.getName().equals(fileName)){
				dirEntryList.add(prevEntry);
				dirEntryList.add(thisEntry);
				return dirEntryList;
			}
			else if(nextEntry.getInode() == 0){
				return null;
			}
			pos += thisEntry.getRecordLength();
		}
		
		return dirEntryList;
	}
	
	/**
	 * Checks if the filename exists in the particular directory
	 * @param raFile
	 * @param fileName
	 * @param pos
	 * @return
	 */
	public static boolean isFileNameExists(RandomAccessFile raFile, String fileName, long pos){
		DirectoryEntry entry = new DirectoryEntry();
		int inode = -1;
		while(inode!=0){
			entry = getDirEntry(raFile,pos);
			inode = entry.getInode();
			if(entry.getName().equals(fileName)){
				return true;
			}
			pos += entry.getRecordLength();
		}
		return false;
	}
	
	/**
	 * Writes a directory entry in the filesystem
	 * @param raFile
	 * @param dirEntry
	 * @param pos
	 */
	public static void updateDirEntry(RandomAccessFile raFile,DirectoryEntry dirEntry, long pos){
		try{
			raFile.seek(pos);
			raFile.writeInt(dirEntry.getInode());
			raFile.writeInt(dirEntry.getRecordLength());
			raFile.writeByte(dirEntry.getNameLength());
			raFile.writeByte(dirEntry.getFileType());
			
			raFile.writeChars(dirEntry.getName());
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns all the directory entries for navigation. Returns current and parent
	 * directory entries
	 * @param raFile
	 * @param pos
	 * @return
	 */
	public static ArrayList<DirectoryEntry> getNavigateList(RandomAccessFile raFile, long pos){
		ArrayList<DirectoryEntry> entryList = new ArrayList<DirectoryEntry>();
		DirectoryEntry entry = new DirectoryEntry();
		int inode = -1;
		while(inode!=0){
			entry = getDirEntry(raFile, pos);
			inode = entry.getInode();
			entryList.add(entry);
			pos += entry.getRecordLength();
		}
		return entryList;
	}
	
	/**
	 * Returns only named directory entries in a directory entry
	 * @param raFile
	 * @param pos
	 * @return
	 */
	public static ArrayList<DirectoryEntry> getDirEntryList(RandomAccessFile raFile, long pos){
		ArrayList<DirectoryEntry> entryList = new ArrayList<DirectoryEntry>();
		DirectoryEntry entry = new DirectoryEntry();
		int inode = -1;
		while(inode!=0){
			entry = getDirEntry(raFile,pos);
			inode = entry.getInode();
			if(!entry.getName().equals("") && !entry.getName().equals(".") && !entry.getName().equals("..")){
				entryList.add(entry);
			}
			pos += entry.getRecordLength();
		}
		return entryList;
	}
	
	/**
	 * Gets the directory entry position based on the parent inode position
	 * @param raFile
	 * @param fileName
	 * @param pos
	 * @return
	 */
	public static long getDirEntryPos(RandomAccessFile raFile,String fileName, long pos){
		ArrayList<DirectoryEntry> entryList = new ArrayList<DirectoryEntry>();
		entryList = getNavigateList(raFile, pos);
		for(DirectoryEntry entry: entryList){
			if(entry.getName().equals(fileName)){
				return pos;
			}
			pos += entry.getRecordLength();
		}
		return 0;
	}
	
	/**
	 * Gets the root inode
	 * @param raFile
	 * @return
	 */
	public static Inode getRootInode(RandomAccessFile raFile){
		Superblock sBlock = getSuperblock(raFile);
		Inode inode = getInode(raFile, sBlock.getFirstInode());
		return inode;
	}
	
	/**
	 * Deletes all the components of a file from the filesystem
	 * Unsets the inode bitmap and the block bitmap
	 * @param raFile
	 * @param pos
	 */
	public static void deleteComponents(RandomAccessFile raFile, long pos){
		Inode inode = getInode(raFile, pos);
		Superblock sBlock = getSuperblock(raFile);
		Bitmap inodeBitmap = getBitmap(raFile,sBlock.getInodeCount(),false);
		Bitmap blockBitmap = getBitmap(raFile,sBlock.getBlocksCount(),true);
		
		inodeBitmap.unsetAtPos((int) ((pos-sBlock.getFirstInode())/sBlock.getBlockSize()));
		
		for(int i=0;i<Constants.DIRECT_LINKS_CNT;++i){
			if(inode.getBlock(i)!=0 && inode.getBlock(i)!=-1){
				blockBitmap.unsetAtPos((int)(inode.getBlock(i)/sBlock.getBlockSize()) - sBlock.getFirstDataBlock());
			}
		}
		
		updateBitmap(raFile, inodeBitmap, (Constants.INODE_BITMAP_BLK_POS - 1)*sBlock.getBlockSize());
		updateBitmap(raFile, blockBitmap, (Constants.BLOCK_BITMAP_BLK_POS - 1)*sBlock.getBlockSize());
	}
}
