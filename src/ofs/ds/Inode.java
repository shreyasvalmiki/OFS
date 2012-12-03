package ofs.ds;

public class Inode {
	//4 bytes
	private int mode;
	//4 bytes
	private int size;
	//8 bytes
	private long accessedTime;
	//8 bytes
	private long createdTime;
	//8 bytes
	private long modifiedTime;
	//8 bytes
	private long deletedTime;
	//1 byte
	private byte linksCount;
	//4 bytes
	private int blocksCount;
	//4 bytes
	private int implFlag;
	//60 bytes
	private int[] block;
	
	//Constructor
	public Inode(){
		block = new int[15];
	}
	
	//mode Property
	public int getMode(){
		return this.mode;
	}
	public void setMode(int val){
		this.mode = val;
	}
	
	//size Property
	public int getSize(){
		return this.size;
	}
	public void setSize(int val){
		this.size = val;
	}
	
	//accessedTime Property
	public long getAccessedTime(){
		return this.accessedTime;
	}
	public void setAccessedTime(long val){
		this.accessedTime = val;
	}
	
	//createdTime Property
	public long getCreatedTime(){
		return this.createdTime;
	}
	public void setCreatedTime(long val){
		this.createdTime = val;
	}
	
	//modifiedTime Property
	public long getModifiedTime(){
		return this.modifiedTime;
	}
	public void setModifiedTime(long val){
		this.modifiedTime = val;
	}
	
	//deletedTime Property
	public long getDeletedTime(){
		return this.deletedTime;
	}
	public void setDeletedTime(long val){
		this.deletedTime = val;
	}
	
	//linksCount Property
	public byte getLinksCount(){
		return this.linksCount;
	}
	public void setLinksCount(byte val){
		this.linksCount = val;
	}
	
	//blocksCount Property
	public int getBlocksCount(){
		return this.blocksCount;
	}
	public void setBlocksCount(int val){
		this.blocksCount = val;
	}
	
	//implFlag Property
	public int getImplFlag(){
		return this.implFlag;
	}
	public void setImplFlag(int val){
		this.implFlag = val;
	}
	
	//gets a block from block array
	public int getBlock(int index){
		return this.block[index];
	}
	//sets a block to the block array at the index
	public void setBlock(int index,int val){
		this.block[index] = val;
	}
	
	
	//Prints an Inode
	public void print(){
		System.out.println("Mode:\t\t" + mode);
		System.out.println("Size:\t\t" + size);
		System.out.println("Accessed Time:\t\t" + accessedTime);
		System.out.println("Created Time:\t\t" + createdTime);
		System.out.println("Modified Time:\t\t" + modifiedTime);
		System.out.println("Deleted Time:\t\t" + deletedTime);
		System.out.println("Links Count:\t\t" + linksCount);
		System.out.println("Blocks Count:\t\t" + blocksCount);
		System.out.println("Implementation Flag:\t\t" + implFlag);
		System.out.println("Blocks:");
		for(int i=0; i<15; i++){
			System.out.println("\t\tBlock " + i + ": " + block);
		}
	}
}
