package ofs.fs;

import java.io.*;
import java.util.*;
import ofs.ds.*;
import ofs.utils.*;

public class FileSystem {
	
	private static String sep;
	private String fileName;
	private int fileSize;
	private int blockSize;
	private int blockCount;
	private int inodeCount;
	private int firstDataBlock;
	private RandomAccessFile raFile;
	private Superblock sBlock;
	private Inode initInode;
	private Bitmap blockBitmap;
	private Bitmap inodeBitmap;
	
	public FileSystem(){
		sep = File.separator;
		System.out.println(sep);
	}
	public static void main(String[] args){
		boolean fileNameOk = false;
		FileSystem fs = new FileSystem();
		Scanner in = new Scanner(System.in);
		//String fileName = new String();
		
		do{
			System.out.println("Enter the file system file you want to use (example: file.dat):");
			fs.fileName = in.next();
			if (!fs.fileName.substring(fs.fileName.length()-4).equals(".dat"))
			{
				System.out.println("Invalid file extension");
				fileNameOk = false;
			}
			else
			{
				if(!fs.isCreated(fs.fileName))
				{
					System.out.println(fs.fileName + " is not avalable. Do you want to create? [Y/N]");
					String dec = in.next();
					if(dec.equalsIgnoreCase("Y")){
						fileNameOk = true;
						System.out.println("Enter file size in order of 2 (eg. 1024000):");
						fs.fileSize = in.nextInt();
						System.out.println("Enter block size in multiples of 512 bytes (eg. enter 1 for 1*512, 2 for 2*512):");
						fs.blockSize = Constants.SECTOR_SIZE*in.nextInt();
						
						fs.create(fs.fileName,fs.fileSize,fs.blockSize);
					}
				}
				else
				{
					System.out.println("File system exists");
					fileNameOk = true;
					fs.get(fs.fileName);
					fs.displaySuperBlock();
				}
			}
		}while(!fileNameOk);
		in.close();
	}
	
	public void create(String fsName, int fSize, int blkSize){
		fileName = fsName;
		fileSize = fSize;
		blockSize = blkSize;
		
		float numInodeBlocks = (inodeCount * Constants.INODE_SIZE)/blockSize;
		firstDataBlock = (int)Math.floor(3 + numInodeBlocks);
		if(numInodeBlocks != Math.floor(numInodeBlocks)){
			++firstDataBlock;
		}
		blockCount = (int)fileSize/blockSize - firstDataBlock;
		int tempInodeCount = (int)((blockCount)/7.8);
		inodeCount = tempInodeCount <= blockSize*Constants.BYTE_SIZE? tempInodeCount: blockSize*Constants.BYTE_SIZE;
		
		File file = new File("src"+sep+"Assets"+sep+fileName);
		try {
			raFile = new RandomAccessFile(file, "rw");
			raFile.setLength(fileSize);
			setFileSystemObjects();
			updateFileSystem();
			displaySuperBlock();
			raFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public RandomAccessFile get(String fsName){
		fileName = fsName;
		File file = new File("src"+sep+"Assets"+sep+fileName);
		
		try{
			raFile = new RandomAccessFile(file, "rw");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return raFile;
	}
	public boolean isCreated(String fsName){
		fileName = fsName;
		File file = new File("src"+sep+"Assets"+sep+fileName);
		return file.exists();
	}
	public void delete(String fileName){
		
	}
	public void setFileSystemObjects(){
//		try {
//			raFile.seek(blockSize);
//			
//			raFile.writeInt(678889);
//			raFile.writeInt(38838);
//			raFile.seek(blockSize);
//			System.out.println(raFile.readInt());
//			System.out.println(raFile.readInt());
//			raFile.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		setInitSuperblock();
		blockBitmap = new Bitmap(blockCount);
		inodeBitmap = new Bitmap(inodeCount);
		setInitRootInode();
	}
	
	public void updateFileSystem(){
		try{
			FSUtils.updateSuperblock(raFile, sBlock);
			FSUtils.updateBitmap(raFile,blockBitmap, blockSize);
			FSUtils.updateBitmap(raFile,inodeBitmap, blockSize * 2);
			FSUtils.updateInode(raFile, initInode, blockSize * 3);
			FSUtils.updateInitDirEntry(raFile, blockSize, blockSize*3, 0, true, firstDataBlock*blockSize);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	private void setInitSuperblock() {
		
		Date now = new Date();
		sBlock = new Superblock();
		sBlock.setBlockSize(blockSize);
		sBlock.setInodeCount(inodeCount);
		sBlock.setWTime(GeneralUtils.getLongFromDate(now));
		sBlock.setInodeSize(Constants.INODE_SIZE);
		sBlock.setFreeInodesCount(1);
		sBlock.setBlocksCount(blockCount);
		sBlock.setFirstDataBlock(firstDataBlock);
		sBlock.setFirstInode(0);
	}
	private void setInitRootInode(){
		Date now = new Date();
		initInode = new Inode();
		initInode.setMode(Constants.DIR);
		initInode.setSize(0);
		initInode.setAccessedTime(GeneralUtils.getLongFromDate(now));
		initInode.setCreatedTime(GeneralUtils.getLongFromDate(now));
		initInode.setModifiedTime(GeneralUtils.getLongFromDate(now));
		initInode.setDeletedTime(GeneralUtils.getLongFromDate(now));
		initInode.setLinksCount((byte)(0));
		initInode.setBlocksCount(1);
		initInode.setBlock(0, firstDataBlock);
		for(int i = 1; i < Constants.BLOCKS_PER_INODE - 1; ++i){
			initInode.setBlock(i, -1);
		}
	}
	
	public void displaySuperBlock(){
		Superblock sb = new Superblock();
		sb = FSUtils.getSuperBlock(raFile);
		sb.print();
	}
	
}
