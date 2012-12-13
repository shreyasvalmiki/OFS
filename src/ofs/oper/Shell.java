package ofs.oper;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import ofs.ds.*;
import ofs.fs.*;
import ofs.utils.*;
/**
 * Shell for basic file operations
 * Though it's not user friendly, it does the job
 * @author shreyasvalmiki
 *
 */
public class Shell {

	public RandomAccessFile raFile;
	public Inode currInode = new Inode();
	public DirectoryEntry currEntry = new DirectoryEntry();
	public int currInodeLoc = 0;
	public int fileSize;
	public int blockSize;
	public String fileName = new String();
	public Superblock sBlock = new Superblock();
	public HashMap<Integer,String> errMap = new HashMap<Integer,String>();
	public HashMap<Integer,String> ftMap = new HashMap<Integer,String>();
	public static Scanner in = new Scanner(System.in);

	public static void main(String[] args){
		Shell sh = new Shell();

		FileSystem fs = new FileSystem();

		String fileOption = "0";

		boolean isFileSysSet = false;

		boolean isExit = false;

		do{

			while(!isFileSysSet){
				System.out.println("Enter the file system file you want to use:");
				sh.fileName = in.next();
				if(!fs.isCreated(sh.fileName))
				{
					System.out.println(sh.fileName + " is not avalable. Do you want to create? [Y/N]");
					String dec = in.next();
					if(dec.equalsIgnoreCase("Y")){
						try {
							System.out.println("Enter file size in MBs:");
							sh.fileSize = Constants.ONE_MEG*in.nextInt();
							System.out.println("Enter block size in 512 bytes (eg. enter 1 for 1*512 bytes, 2 for 2*512 bytes, etc):");
							sh.blockSize = Constants.SECTOR_SIZE*in.nextInt();
							sh.raFile = fs.create(sh.fileName,sh.fileSize,sh.blockSize);
							isFileSysSet = true;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Please enter correct values");
							in.next();
						}
					}
				}
				else
				{
					System.out.println("File system exists");
					isFileSysSet = true;
					sh.raFile = fs.get(sh.fileName);
					fs.displaySuperBlock();	
				}
				if(isFileSysSet){
					sh.sBlock = FSUtils.getSuperblock(sh.raFile);
					sh.currInode = FSUtils.getRootInode(sh.raFile);
					sh.setCurrDirEntry();
				}
			}
			System.out.println();
			System.out.println("Please select a file operation:");
			System.out.println("1. Create directory");
			System.out.println("2. List");
			System.out.println("3. Navigate to other directory");
			System.out.println("4. Create a text file");
			System.out.println("5. Delete");
			System.out.println("6. Show file contents");
			System.out.println("7. Show file system properties");
			System.out.println("9. Choose a different filesystem");
			System.out.println("0. Exit");

			fileOption = in.next();
			if(fileOption.equals("0")){
				try {
					sh.raFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.exit(1);
				}
				System.exit(1);

			}
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
			else if(fileOption.equals("5")){
				sh.delete();
			}
			else if(fileOption.equals("6")){
				sh.printFileContents();
			}
			else if(fileOption.equals("7")){
				sh.sBlock = FSUtils.getSuperblock(sh.raFile);
				sh.sBlock.print();
			}
			else if(fileOption.equals("9")){
				isFileSysSet = false;
			}
			else{
				System.out.println("Please choose a valid option");
			}
			System.out.println();
			System.out.print("Press 'c' to continue");
			in.next();
		}while(!isExit);
		in.close();
	}
	/**
	 * Constructor
	 */
	public Shell(){
		errMap.put(Constants.ERR_INSUFF_MEM, "Error! Insufficient memory.");
		errMap.put(Constants.ERR_FILE_TOO_LARGE, "Error! File too large.");
		errMap.put(Constants.ERR_FILE_NAME_EXISTS, "Error! Filename exists.");
		errMap.put(Constants.ERR_INVALID_PATH, "Error! Not a valid path.");
		errMap.put(Constants.ERR_FILE_SIZE_NOT_INT, "Error! File size is not an integer.");
		errMap.put(Constants.ERR_FILE_DOES_NOT_EXIST, "Error! File does not exist.");
		errMap.put(Constants.ERR_NOT_CORRECT_VAL, "Error! Enter correct values.");

		ftMap.put(Constants.FT_DIR, "DIR");
		ftMap.put(Constants.FT_REG_FILE, "File");
		ftMap.put(Constants.FT_UNKNOWN, "Unknown");
	}
	/**
	 * Creates a directory
	 */
	private void createDir(){
		String name = "New Dir";
		boolean done = false;
		System.out.print("Enter name:");
		in.nextLine();
		name = in.nextLine();
		int err = -1;
		DirectoryOper dirOp = new DirectoryOper(raFile);
		while(!done){
			err = dirOp.create(currInode, currInodeLoc, name);
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

	/**
	 * Creates a regular file
	 */
	private void createRegFile(){
		String name = "New Dir";
		String content;
		System.out.print("Enter name of text file:");
		in.nextLine();
		name = in.nextLine();
		System.out.println("Enter content");
		content = in.nextLine();
		int err = Constants.NO_ERROR;
		RegFileOper regFile = new RegFileOper(raFile);
		err = regFile.create(currInode, name, content);
		if(err != Constants.NO_ERROR){
			displayError(err);
		}
	}

	/**
	 * Lists all the files and directories present in the current directory
	 */
	private void list(){
		ArrayList<DirectoryEntry> dirArr = new ArrayList<DirectoryEntry>();
		dirArr = FSUtils.getDirEntryList(raFile, currInode.getBlock(0));
		Inode inode = new Inode();
		System.out.println("\tName\t\tCreated On\t\t\t\tType");
		System.out.println("-------------------------------------------------------------------------------------");
		for(DirectoryEntry entry: dirArr){
			inode = FSUtils.getInode(raFile, entry.getInode());
			System.out.println();
			if(entry.getName().length()>7){
				System.out.println("\t"+entry.getName() + "\t" + GeneralUtils.getDateFromLong(inode.getCreatedTime())+ "\t\t" + this.ftMap.get((int)entry.getFileType()));
			}
			else{
				System.out.println("\t"+entry.getName() + "\t\t" + GeneralUtils.getDateFromLong(inode.getCreatedTime())+ "\t\t" + this.ftMap.get((int)entry.getFileType()));
			}
		}
	}

	/**
	 * Navigates to a desired directory
	 */
	private void navigate(){
		String path = ".";
		DirectoryOper dir = new DirectoryOper(raFile);
		Inode inode = new Inode();
		System.out.print("Enter path (eg: /abc/dec/fcd -- from root. xyz/teu -- from current dir):");
		in.nextLine();
		path = in.nextLine();
		inode = dir.navigateTo(path, currInode);
		while(inode == null){
			displayError(Constants.ERR_INVALID_PATH);
			System.out.print("Enter path:");
			path = in.next();
			inode = dir.navigateTo(path, currInode);
		}

		currInode = inode;
		setCurrDirEntry();
		System.out.println("You are currently in "+path);
	}

	/**
	 * Deletes a directory or a regular file
	 */
	private void delete(){
		System.out.println("Enter the name of the file or directory to delete: ");
		in.nextLine();
		String fileName = in.nextLine();
		FileOper fOper = new FileOper(raFile);
		int err = fOper.deleteFile(currInodeLoc, fileName);
		if(err == Constants.NO_ERROR){
			System.out.println(fileName + " is removed from the current directory");
		}
		else{
			displayError(err);
		}
	}
	
	private void printFileContents(){
		System.out.println("Enter the name of the file: ");
		in.nextLine();
		String fileName = in.nextLine();
		if(!FSUtils.isFileNameExists(raFile, fileName, currInode.getBlock(0))){
			displayError(Constants.ERR_FILE_DOES_NOT_EXIST);
		}
		else{
			Inode inode = FSUtils.getInode(raFile, FSUtils.getInodePosFromName(raFile, fileName, currInode.getBlock(0)));
			System.out.println(FSUtils.getTextData(raFile, inode.getSize(), inode.getBlock(0)));
		}
	}
	/**
	 * Displays the error message based on the error number
	 * @param err
	 */
	private void displayError(int err){
		System.out.println(errMap.get(err));
	}

	/**
	 * Sets the current directory
	 */
	private void setCurrDirEntry(){
		currEntry = FSUtils.getDirEntry(raFile, currInode.getBlock(0));
		currInodeLoc = currEntry.getInode();
	}
}
