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
	public static HashMap<Integer,String> ftMap = new HashMap<Integer,String>();
	public static Scanner in = new Scanner(System.in);

	public static void main(String[] args){
		Shell sh = new Shell();
		FileSystem fs = new FileSystem();

		String fileOption = "0";

		boolean isFileSysSet = false;


		boolean isExit = false;
		//String fileName = new String();
		do{

			while(!isFileSysSet){
				System.out.println("Enter the file system file you want to use (example: file.dat):");
				sh.fileName = in.next();
				//				if (!sh.fileName.substring(sh.fileName.length()-4).equals(".dat"))
				//				{
				//					System.out.println("Invalid file extension");
				//					isFileSysSet = false;
				//				}
				//				else
				//				{
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
			//			}

			System.out.println("Please select a file operation:");
			System.out.println("1. Create directory");
			System.out.println("2. List");
			System.out.println("3. Navigate to other directory");
			System.out.println("4. Create a file");
			System.out.println("5. View file");
			System.out.println("6. Remove directory");
			System.out.println("7. Remove file");
			System.out.println("9. Choose a different filesystem");
			System.out.println("0. Exit");

			fileOption = in.next();

			if(fileOption.equals("1")){
				sh.createDir();
			}
			else if(fileOption.equals("2")){
				sh.list();
			}
			else if(fileOption.equals("3")){

				sh.navigate();
			}
			else if(fileOption.equals("4")){
				sh.createRegFile();
			}


		}while(!isExit);
		in.close();
	}

	public Shell(){
		errMap.put(Constants.ERR_INSUFF_MEM, "Error! Insufficient memory.");
		errMap.put(Constants.ERR_FILE_TOO_LARGE, "Error! File too large.");
		errMap.put(Constants.ERR_FILE_NAME_EXISTS, "Error! Filename exists.");
		errMap.put(Constants.ERR_INVALID_PATH, "Error! Not a valid path.");
		errMap.put(Constants.ERR_FILE_SIZE_NOT_INT, "Error! File size is not an integer");

		ftMap.put(Constants.FT_DIR, "DIR");
		ftMap.put(Constants.FT_REG_FILE, "File");
		ftMap.put(Constants.FT_UNKNOWN, "Unknown");
	}
	private void createDir(){
		String name = "New Dir";
		boolean done = false;
		System.out.print("Enter name:");
		name = in.next();
		int err = -1;
		DirectoryOper dirOp = new DirectoryOper(raFile);
		while(!done){
			System.out.println("before dir create");
			err = dirOp.create(currInode, name);
			//test
			System.out.println("after dir create");
			if(err == Constants.NO_ERROR){
				System.out.println(name+" has been created.");
				done = true;
			}else{
				displayError(err);
				System.out.println("Enter name again:");
				name = in.next();
			}
		}
	}

	private void createRegFile(){
		String name = "New Dir";
		String size;
		int sizeInt = 0;
		boolean done = false;
		boolean sizeIsInt = true;
		System.out.print("Enter name of file:");
		name = in.next();
		System.out.println("Enter size of file");
		size = in.next();
		int err = Constants.NO_ERROR;
		RegFileOper regFile = new RegFileOper(raFile);
		while(!done){	
			try{
				sizeInt = Integer.parseInt(size);
				sizeIsInt = true;
			}catch(Exception ex){
				sizeIsInt = false;
				err = Constants.ERR_FILE_SIZE_NOT_INT;
			}
			if(!sizeIsInt){
				err = regFile.create(currInode, name, sizeInt);
			}
			if(err == Constants.NO_ERROR || !sizeIsInt){
				System.out.println(name + " has been created.");
				done = true;
			}else{
				displayError(err);

				System.out.println("Enter name again:");
				name = in.next();
				System.out.println("Enter size in integer");
				size = in.next();
			}
		}
	}

	private void list(){
		ArrayList<DirectoryEntry> dirArr = new ArrayList<DirectoryEntry>();
		dirArr = FSUtils.getNavigateList(raFile, currInode.getBlock(0));
		Inode inode = new Inode();
		for(DirectoryEntry entry: dirArr){
			inode = FSUtils.getInode(raFile, entry.getInode());
			System.out.println("Created on: "+GeneralUtils.getDateFromLong(inode.getCreatedTime())+ "\t" + entry.getName() + "\t" + ftMap.get(entry.getFileType()));
		}
	}

	private void navigate(){
		String path = ".";
		DirectoryOper dir = new DirectoryOper(raFile);
		int err = 0;
		Inode inode = new Inode();
		System.out.print("Enter path (eg: /abc/dec/fcd -- from root. xyz/teu -- from current dir):");
		path = in.next();
		inode = dir.navigateTo(path, currInode);
		while(inode == null){
			displayError(Constants.ERR_INVALID_PATH);
			System.out.print("Enter path:");
			path = in.next();
			inode = dir.navigateTo(path, currInode);
		}

		currInode = inode;
		System.out.println("You are currently in "+path);
	}

	private void displayError(int err){
		System.out.println(errMap.get(err));
	}
}
