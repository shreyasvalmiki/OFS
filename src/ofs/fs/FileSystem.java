package ofs.fs;

import java.io.*;
import java.util.*;
import ofs.ds.*;

public class FileSystem {
	
	private static String sep;
	private String fileName;
	private int fileSize;
	private int blockSize;
	private int blockCount;
	private int inodeCount;
	private RandomAccessFile raFile;
	private Superblock sBlock;
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
						System.out.println("Enter file size in order of 2 (eg. 1024):");
						fs.fileSize = in.nextInt();
						System.out.println("Enter file size in order of 2 (eg. 128):");
						fs.blockSize = in.nextInt();
						fs.blockCount = fs.inodeCount = fs.fileSize/fs.blockSize;
						fs.create(fs.fileName,fs.fileSize,fs.blockSize);
					}
				}
				else
				{
					System.out.println("File system exists");
					fileNameOk = true;
				}
			}
		}while(!fileNameOk);
		in.close();
	}
	
	public void create(String fName, int fSize, int blkSize){
		fileName = fName;
		fileSize = fSize;
		blockSize = blkSize;
		File file = new File("src"+sep+"Assets"+sep+fileName);
		try {
			raFile = new RandomAccessFile(file, "rw");
			raFile.setLength(fileSize);
			setFileSystemObject();
			//raFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public boolean isCreated(String fsName){
		fileName = fsName;
		File file = new File("src"+sep+"Assets"+sep+fileName);
		return file.exists();
	}
	public void delete(String fileName){
		
	}
	public void setFileSystemObject(){
//		try {
//			raFile.seek(blockSize);
//			
//			raFile.writeInt(678889);
//			raFile.seek(blockSize);
//			System.out.println(raFile.readInt());
//			raFile.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		setInitSuperblock();
		blockBitmap = new Bitmap(blockCount);
		inodeBitmap = new Bitmap(inodeCount);
	}
	
	public void updateFileSystem(){
		try{
			updateSuperblock();
			raFile.seek(blockSize);
			updateBitmap(blockBitmap);
			raFile.seek(blockSize * 2);
			updateBitmap(inodeBitmap);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	private void setInitSuperblock() {
		sBlock = new Superblock();
		sBlock.setBlockSize(blockSize);
		sBlock.setInodeCount(1);
		sBlock.setWTime(new Date());
		sBlock.setInodeSize(Constants.INODE_SIZE);
		sBlock.setFreeInodesCount(1);
		sBlock.setBlocksCount(blockCount);
		sBlock.setFirstDataBlock(0);
		sBlock.setFirstInode(0);
	}
	
	public void updateSuperblock(){
		try{
			raFile.writeInt(sBlock.getBlockSize());
			raFile.writeInt(sBlock.getInodeCount());
			raFile.writeLong(sBlock.getWTime());
			raFile.writeInt(sBlock.getInodeSize());
			raFile.writeInt(sBlock.getFreeInodesCount());
			raFile.writeInt(sBlock.getBlocksCount());
			raFile.writeInt(sBlock.getFirstDataBlock());
			raFile.writeInt(sBlock.getFirstInode());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateBitmap(Bitmap bitmap){
		int words = bitmap.getWords();
		int[] map = bitmap.getMap();
		try {
			for(int i = 0; i<words; i++){
				raFile.writeInt(map[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
