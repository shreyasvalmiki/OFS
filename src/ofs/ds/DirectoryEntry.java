package ofs.ds;
/**
 * Gets an sets elements in the directory entry
 * @author shreyasvalmiki
 *
 */
public class DirectoryEntry {
	/**
	 * 4 bytes
	 */
	private int inode;
	/**
	 * 4 bytes
	 */
	private int recordLength;
	/**
	 * 1 byte
	 */
	private byte nameLength;
	/**
	 * 1 byte
	 */
	private byte fileType;
	/**
	 * 2 * Size of name
	 */
	private String name;
	
	/**
	 * Constructor
	 */
	public DirectoryEntry(){
		name = new String();
	}
	
	/**
	 * inode Property
	 * @return
	 */
	public int getInode(){
		return this.inode;
	}
	public void setInode(int val){
		this.inode = val;
	}
	
	/**
	 * recordLength Property
	 * @return
	 */
	public int getRecordLength(){
		return this.recordLength;
	}
	public void setRecordLength(int val){
		this.recordLength = val;
	}
	
	/**
	 * nameLength Property
	 * @return
	 */
	public byte getNameLength(){
		return this.nameLength;
	}
	public void setNameLength(byte val){
		this.nameLength = val;
	}
	
	/**
	 * fileType Property
	 * @return
	 */
	public byte getFileType(){
		return this.fileType;
	}
	public void setFileType(byte val){
		this.fileType = val;
	}
	
	/**
	 * name Property
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	public void setName(String val){
		this.name = val;
	}
	
	/**
	 * Prints the directory entry
	 */
	public void print(){
		System.out.println("Inode:\t\t" + inode);
		System.out.println("Record Length:\t\t" + recordLength);
		System.out.println("File Type:\t\t" + fileType);
		System.out.println("Name:\t\t" + name);
	}
}
