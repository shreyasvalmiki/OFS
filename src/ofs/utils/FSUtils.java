package ofs.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import ofs.ds.*;
import ofs.ds.Bitmap;

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

	public static void updateInitDirEntry(RandomAccessFile raFile,int blockSize,int curInodeIdx, int parInodeIdx,boolean isRoot, long pos){
		try{			
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
				raFile.writeInt(Constants.INIT_DIR_ENTRY_SIZE);
				raFile.writeByte(2);
				raFile.writeByte(Constants.FT_DIR);
				raFile.writeChars("..");

				pos += Constants.INIT_DIR_ENTRY_SIZE;
			}

			raFile.seek(pos);

			raFile.writeInt(0);
			raFile.writeInt(blockSize-(Constants.INIT_DIR_ENTRY_SIZE*2));
			raFile.writeByte(0);
			raFile.writeByte(Constants.FT_DIR);
			raFile.writeChars("");

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static Superblock getSuperBlock(RandomAccessFile raFile){
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
}
