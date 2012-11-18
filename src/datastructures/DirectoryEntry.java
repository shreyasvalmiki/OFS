package datastructures;

public class DirectoryEntry {
	//4 bytes
	private int inode;
	//4 bytes
	private int recordLength;
	//1 byte
	private byte nameLength;
	//1 byte
	private byte fileType;
	//Variable size
	private String name;
	
	//Constructor
	public DirectoryEntry(){
		name = new String();
	}
	
	//inode Property
	public int getInode(){
		return this.inode;
	}
	public void setInode(int val){
		this.inode = val;
	}
	
	//recordLength Property
	public int getRecordLength(){
		return this.recordLength;
	}
	public void setRecordLength(int val){
		this.recordLength = val;
	}
	
	//nameLength Property
	public byte getNameLength(){
		return this.nameLength;
	}
	public void setNameLength(byte val){
		this.nameLength = val;
	}
	
	//fileType Property
	public byte getFileType(){
		return this.fileType;
	}
	public void setFileType(byte val){
		this.fileType = val;
	}
	
	//name Property
	public String getName(){
		return this.name;
	}
	public void setName(String val){
		this.name = val;
	}
}
