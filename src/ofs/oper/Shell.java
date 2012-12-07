package ofs.oper;

import java.io.RandomAccessFile;
import java.util.*;
import ofs.ds.*;
import ofs.fs.*;
import ofs.utils.*;

public class Shell {

	public RandomAccessFile raFile;
	public Inode currInode = new Inode();
	public int fileSize;
	public int blockSize;
	public String fileName = new String();
	public Superblock sBlock = new Superblock();
	public static HashMap<Integer,String> errMap = new HashMap<Integer,String>();
	public static Scanner in = new Scanner(System.in);

	public static void main(String[] args){
		Shell sh = new Shell();
		FileSystem fs = new FileSystem();
		
		int fileOption = 0;
		
		boolean isFileSysSet = false;

		boolean isExit = false;
		//String fileName = new String();
		do{
			
			while(!isFileSysSet){
				System.out.println("Enter the file system file you want to use (example: file.dat):");
				sh.fileName = in.next();
				if (!sh.fileName.substring(sh.fileName.length()-4).equals(".dat"))
				{
					System.out.println("Invalid file extension");
					isFileSysSet = false;
				}
				else
				{
					if(!fs.isCreated(sh.fileName))
					{
						System.out.println(sh.fileName + " is not avalable. Do you want to create? [Y/N]");
						String dec = in.next();
						if(dec.equalsIgnoreCase("Y")){
							isFileSysSet = true;
							System.out.println("Enter file size in order of 2 (eg. 1024000):");
							sh.fileSize = in.nextInt();
							System.out.println("Enter block size in multiples of 512 bytes (eg. enter 1 for 1*512, 2 for 2*512):");
							sh.blockSize = Constants.SECTOR_SIZE*in.nextInt();

							sh.raFile = fs.create(sh.fileName,sh.fileSize,sh.blockSize);
						}
					}
					else
					{
						System.out.println("File system exists");
						isFileSysSet = true;
						sh.raFile = fs.get(sh.fileName);
						fs.displaySuperBlock();	
					}
					
					sh.sBlock = FSUtils.getSuperblock(sh.raFile);
					sh.currInode = FSUtils.getRootInode(sh.raFile);
				}
			}
			
			System.out.println("Please select a file operation:");
			System.out.println("1. Create directory");
			System.out.println("2. List");
			System.out.println("3. Navigate to other directory");
			System.out.println("3. Create a file");
			System.out.println("4. View file");
			System.out.println("4. Remove directory");
			System.out.println("5. Remove file");
			System.out.println("9. Choose a different filesystem");
			
			fileOption = in.nextInt();
			
			if(fileOption == 1){
				
				
			}
			
			
		}while(!isExit);
		in.close();
	}
	
	public Shell(){
		errMap.put(Constants.ERR_INSUFF_MEM, "Error! Insufficient memory.");
		errMap.put(Constants.ERR_FILE_TOO_LARGE, "Error! File too large.");
		errMap.put(Constants.ERR_FILE_NAME_EXISTS, "Error! Filename exists.");
		errMap.put(Constants.ERR_INVALID_PATH, "Error! Not a valid path.");
	}
	private void createDir(){
		String name = "New Dir";
		boolean done = false;
		System.out.print("Enter name:");
		name = in.next();
		DirectoryOper dirOp = new DirectoryOper(raFile);
		while(!done){
			
		}
	}
}
