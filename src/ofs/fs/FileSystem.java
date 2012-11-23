package ofs.fs;

import java.io.*;
import java.util.Scanner;

import ofs.ds.*;

public class FileSystem {
	
	String sep;
	public FileSystem(){
		String sep = File.separator;
	}
	public static void main(){
		FileSystem fs = new FileSystem();
		Scanner in = new Scanner(System.in);
		String fileName = new String();
		System.out.println("Enter the file system you want to use (example: file.dat):");
		fileName = in.next();
		if (!fileName.substring(-4,-1).equals(".dat"))
		{
			System.out.println("Invalid file extension");
		}
		else
		{
			if(!fs.isCreated(fileName))
			{
				System.out.println(fileName + " is not avalable. Do you want to create? [Y/N]");
				if(in.next().toUpperCase() == "Y"){
					System.out.println("Enter file size in order of 2 (eg. 256):");
					int fileSize = in.nextInt();
					System.out.println("Enter file size in order of 2 (eg. 128):");
					int blockSize = in.nextInt();
					fs.create(fileName, fileSize, blockSize);
				}
			}
		}
		in.close();
	}
	
	public void create(String fileName, int fileSize, int blockSize){
		File file = new File(".."+sep+"Access"+sep+fileName);
		try {
			RandomAccessFile randAccFile = new RandomAccessFile(file, "rw");
			randAccFile.setLength(fileSize);
			randAccFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public boolean isCreated(String fileName){
		File file = new File(".."+sep+"Access"+sep+fileName);
		return file.exists();
	}
	public void delete(String fileName){
		
	}
	public void set(){
		
	}
	public static FileSystem get(String fileName){
		FileSystem fs = new FileSystem();
		
		return fs;
	}
	
}
