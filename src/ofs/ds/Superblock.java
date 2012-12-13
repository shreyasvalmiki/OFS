package ofs.ds;

import ofs.utils.GeneralUtils;

/**
 * This maintains the properties of the entire file system
 * 
 * Reference: http://www.nongnu.org/ext2-doc/
 * @author shreyasvalmiki
 *
 */
public class Superblock {
	/**
	 * 4 bytes
	 */
	private int blockSize;
	/**
	 * 4 bytes
	 */
	private int inodeCount;
	/**
	 * 4 bytes
	 */
	private int blocksCount;
	/**
	 * 4 bytes
	 */
	private int rBlocksCount;
	/**
	 * 4 bytes
	 */
	private int freeBlocksCount;
	/**
	 * 4 bytes
	 */
	private int freeInodesCount;
	/**
	 * 4 bytes
	 */
	private int firstDataBlock;
	/**
	 * 4 bytes
	 */
	private int logBlockSize;
	/**
	 * 4 bytes
	 */
	private int logFragSize;
	/**
	 * 4 bytes
	 */
	private int blocksPerGroup;
	/**
	 * 4 bytes
	 */
	private int fragsPerGroup;
	/**
	 * 4 bytes
	 */
	private int inodesPerGroup;
	/**
	 * 8 bytes
	 */
	private long wTime;
	/**
	 * 4 bytes
	 */
	private int firstInode;
	/**
	 * 4 bytes
	 */
	private int inodeSize;
	/**
	 * 4 bytes
	 */
	private int blockGroupNum;
	
	/**
	 * blockSize Property
	 * @return
	 */
	public int getBlockSize(){
		return this.blockSize;
	}
	public void setBlockSize(int val){
		this.blockSize = val;
	}
	
	/**
	 * inodeCount Property
	 * @return
	 */
	public int getInodeCount(){
		return this.inodeCount;
	}
	public void setInodeCount(int val){
		this.inodeCount = val;
	}
	
	/**
	 * blocksCount Property
	 * @return
	 */
	public int getBlocksCount(){
		return this.blocksCount;
	}
	public void setBlocksCount(int val){
		this.blocksCount = val;
	}
	
	/**
	 * rBlocksCount Property
	 * @return
	 */
	public int getRBlocksCount(){
		return this.rBlocksCount;
	}
	public void setRBlocksCount(int val){
		this.rBlocksCount = val;
	}
	
	/**
	 * freeBlocksCount Property
	 * @return
	 */
	public int getFreeBlocksCount(){
		return this.freeBlocksCount;
	}
	public void setFreeBlocksCount(int val){
		this.freeBlocksCount = val;
	}
	
	/**
	 * freeInodesCount Property
	 * @return
	 */
	public int getFreeInodesCount(){
		return this.freeInodesCount;
	}
	public void setFreeInodesCount(int val){
		this.freeInodesCount = val;
	}
	
	/**
	 * firstDataBlock Property
	 * @return
	 */
	public int getFirstDataBlock(){
		return this.firstDataBlock;
	}
	public void setFirstDataBlock(int var){
		this.firstDataBlock = var;
	}
	
	/**
	 * logBlockSize Property
	 * @return
	 */
	public int getLogBlockSize(){
		return this.logBlockSize;
	}
	public void setLogBlockSize(int val){
		this.logBlockSize = val;
	}
	
	/**
	 * logFragSize Property
	 * @return
	 */
	public int getLogFragSize(){
		return this.logFragSize;
	}
	public void setLogFragSize(int val){
		this.logFragSize = val;
	}
	
	/**
	 * blocksPerGroup Property
	 * @return
	 */
	public int getBlocksPerGroup(){
		return this.blocksPerGroup;
	}
	public void setBlocksPerGroup(int val){
		this.blocksPerGroup = val;
	}
	
	/**
	 * fragsPerGroup Property
	 * @return
	 */
	public int getFragsPerGroup(){
		return this.fragsPerGroup;
	}
	public void setFragsPerGroup(int val){
		this.fragsPerGroup = val;
	}
	
	/**
	 * inodesPerGroup Property
	 * @return
	 */
	public int getInodesPerGroup(){
		return this.inodesPerGroup;
	}
	public void setInodesPerGroup(int val){
		this.inodesPerGroup = val;
	}
	
	/**
	 * wTime Property
	 * @return
	 */
	public long getWTime(){
		return this.wTime;
	}
	public void setWTime(long val){
		this.wTime = val;
	}
	
	/**
	 * firstInode Property
	 * @return
	 */
	public int getFirstInode(){
		return this.firstInode;
	}
	public void setFirstInode(int val){
		this.firstInode = val;
	}
	
	/**
	 * inodeSize Property
	 * @return
	 */
	public int getInodeSize(){
		return this.inodeSize;
	}
	public void setInodeSize(int val){
		this.inodeSize = val;
	}
	
	/**
	 * blockGroupNum Property
	 * @return
	 */
	public int getBlockGroupNum(){
		return this.blockGroupNum;
	}
	public void setBlockGroupNum(int val){
		this.blockGroupNum = val;
	}
	
	/**
	 * Prints all the superblock values
	 */
	public void print(){
		System.out.println("Size of each block:\t\t" + this.blockSize);
		System.out.println("Total Number of Inodes:\t\t" + this.inodeCount);
		System.out.println("Total Number of Blocks:\t\t" + this.blocksCount);
		System.out.println("Number of Free Blocks:\t\t" + this.freeBlocksCount);
		System.out.println("Number of Free Inodes:\t\t" + this.freeInodesCount);
		System.out.println("First Data Block:\t\t" + this.firstDataBlock);
		System.out.println("Last Written Date and Time:\t" + GeneralUtils.getDateFromLong(this.wTime));
		System.out.println("First Inode:\t\t\t" + this.firstInode);
		System.out.println("Inode Size:\t\t\t" + this.inodeSize);
	}
}
