package datastructures;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SuperBlock {
	//4 bytes
	private int inodeCount;
	//4 bytes
	private int blocksCount;
	//4 bytes
	private int rBlocksCount;
	//4 bytes
	private int freeBlocksCount;
	//4 bytes
	private int freeInodesCount;
	//4 bytes
	private int firstDataBlock;
	//4 bytes
	private int logBlockSize;
	//4 bytes
	private int logFragSize;
	//4 bytes
	private int blocksPerGroup;
	//4 bytes
	private int fragsPerGroup;
	//4 bytes
	private int inodesPerGroup;
	//8 bytes
	private long wTime;
	//4 bytes
	private int firstInode;
	//4 bytes
	private int inodeSize;
	//4 bytes
	private int blockGroupNum;
	
	
	//inodeCount Property
	public int getInodeCount(){
		return this.inodeCount;
	}
	public void setInodeCount(int val){
		this.inodeCount = val;
	}
	
	//blocksCount Property
	public int getBlocksCount(){
		return this.blocksCount;
	}
	public void setBlocksCount(int val){
		this.blocksCount = val;
	}
	
	//rBlocksCount Property
	public int getRBlocksCount(){
		return this.rBlocksCount;
	}
	public void setRBlocksCount(int val){
		this.rBlocksCount = val;
	}
	
	//freeBlocksCount Property
	public int getFreeBlocksCount(){
		return this.freeBlocksCount;
	}
	public void setFreeBlocksCount(int val){
		this.freeBlocksCount = val;
	}
	
	//freeInodesCount Property
	public int getFreeInodesCount(){
		return this.freeInodesCount;
	}
	public void setFreeInodesCount(int val){
		this.freeInodesCount = val;
	}
	
	//firstDataBlock Property
	public int getFirstDataBlock(){
		return this.firstDataBlock;
	}
	public void setFirstDataBlock(int var){
		this.firstDataBlock = var;
	}
	
	//logBlockSize Property
	public int getLogBlockSize(){
		return this.logBlockSize;
	}
	public void setLogBlockSize(int val){
		this.logBlockSize = val;
	}
	
	//logFragSize Property
	public int getLogFragSize(){
		return this.logFragSize;
	}
	public void setLogFragSize(int val){
		this.logFragSize = val;
	}
	
	//blocksPerGroup Property
	public int getBlocksPerGroup(){
		return this.blocksPerGroup;
	}
	public void setBlocksPerGroup(int val){
		this.blocksPerGroup = val;
	}
	
	//fragsPerGroup Property
	public int getFragsPerGroup(){
		return this.fragsPerGroup;
	}
	public void setFragsPerGroup(int val){
		this.fragsPerGroup = val;
	}
	
	//inodesPerGroup Property
	public int getInodesPerGroup(){
		return this.inodesPerGroup;
	}
	public void setInodesPerGroup(int val){
		this.inodesPerGroup = val;
	}
	
	//wTime Property
	public long getWTime(){
		return this.wTime;
	}
	public void setWTime(Date val){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmss");
		String date = new String();
		date = format.format(val);
		this.wTime = Long.parseLong(date);
	}
	
	//firstInode Property
	public int getFirstInode(){
		return this.firstInode;
	}
	public void setFirstInode(int val){
		this.firstInode = val;
	}
	
	//inodeSize Property
	public int getInodeSize(){
		return this.inodeSize;
	}
	public void setInodeSize(int val){
		this.inodeSize = val;
	}
	
	//blockGroupNum Property
	public int getBlockGroupNum(){
		return this.blockGroupNum;
	}
	public void setBlockGroupNum(int val){
		this.blockGroupNum = val;
	}
	
	//Prints all the superblock values
	public void print(){
		System.out.println("Total Number of Inodes:\t\t" + this.inodeCount);
		System.out.println("Total Number of Blocks:\t\t" + this.blocksCount);
		System.out.println("Number of Free Blocks:\t\t" + this.freeBlocksCount);
		System.out.println("Number of Free Inodes:\t\t" + this.freeInodesCount);
		System.out.println("First Data Block:\t\t" + this.firstDataBlock);
		System.out.println("Number of Blocks Per Group:\t\t" + this.blocksPerGroup);
		System.out.println("Number of Inodes Per Group:\t\t" + this.inodesPerGroup);
		System.out.println("Last Written Date and Time:\t\t" + this.wTime);
		System.out.println("First Inode:\t\t" + this.firstInode);
		System.out.println("Inode Size:\t\t" + this.inodeSize);
		System.out.println("Block Group of Superblock:\t\t" + this.blockGroupNum);
	}
	
}
