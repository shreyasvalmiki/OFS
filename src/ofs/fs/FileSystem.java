package ofs.fs;

import java.io.*;
import java.util.Scanner;

//import ofs.ds.*;

public class FileSystem {
	
	private static String sep;
	private String fileName;
	private int fileSize;
	private int blockSize;
	
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
						System.out.println("Enter file size in order of 2 (eg. 256):");
						fs.fileSize = in.nextInt();
						System.out.println("Enter file size in order of 2 (eg. 128):");
						fs.blockSize = in.nextInt();
						fs.create();
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
	
	public void create(){
		File file = new File("src"+sep+"Assets"+sep+fileName);
		try {
			RandomAccessFile randAccFile = new RandomAccessFile(file, "rw");
			randAccFile.setLength(fileSize);
			randAccFile.close();
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
	public void setFileSystem(){
		
	}
	
}
