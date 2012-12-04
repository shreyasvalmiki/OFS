package ofs.ds;

public class Constants {
	//public static final int SUPERBLOCK_SIZE = 68;
	public static final int BLOCK_BITMAP_BLK_POS = 2;
	public static final int INODE_BITMAP_BLK_POS = 3;
	public static final int BYTE_SIZE = 8;
	public static final int SECTOR_SIZE = 512;
	public static final int SUPERBLOCK_SIZE = 40;
	public static final int INODE_SIZE = 109;
	public static final int WORD_SIZE = 32;
	public static final int WORD_SHIFT = 5;
	public static final int HEX_WORD_SIZE = 0x1F;
	public static final int HEX_EMPTY_WORD = 0x80000000;
	
	public static final int SIZE_OF_INT = 4;
	public static final int SIZE_OF_LONG = 8;
	public static final int SIZE_OF_BYTE = 1;
	
	
	//Inode mode constants
	public static final int DIR = 1;
	public static final int REG_FILE = 2;
	public static final int SYM_LINK = 3;
	
	public static final int BLOCKS_PER_INODE = 15;
	
	
	//Directory entry constants
	public static final int INIT_DIR_ENTRY_SIZE = 12;
	
	public static final int FT_UNKNOWN = 0;
	public static final int FT_REG_FILE = 1;
	public static final int FT_DIR = 2;
}
