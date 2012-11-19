package ofs.ds;

public class Inode {
	//4 bytes
	private int mode;
	//4 bytes
	private int size;
	//4 bytes
	private int accessedTime;
	//4 bytes
	private int createdTime;
	//4 bytes
	private int modifiedTime;
	//4 bytes
	private int deletedTime;
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
	public int getAccessedTime(){
		return this.accessedTime;
	}
	public void setAccessedTime(int val){
		this.accessedTime = val;
	}
	
	//createdTime Property
	public int getCreatedTime(){
		return this.createdTime;
	}
	public void setCreatedTime(int val){
		this.createdTime = val;
	}
	
	//modifiedTime Property
	public int getModifiedTime(){
		return this.modifiedTime;
	}
	public void setModifiedTime(int val){
		this.modifiedTime = val;
	}
	
	//deletedTime Property
	public int getDeletedTime(){
		return this.deletedTime;
	}
	public void setDeletedTime(int val){
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
	public int addBlock(int index){
		return this.block[index];
	}
	//sets a block to the block array at the index
	public void setBlock(int index,int val){
		this.block[index] = val;
	}
}
