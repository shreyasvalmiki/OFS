package ofs.ds;
/*
 * Modified to requirement from www.java2s.com
 * 9.33.1.Implementation of a bit map of any size, together with static methods to manipulate int, byte and byte[] values as bit maps
 **/
public class Bitmap {
	private final int WORD_SIZE = 32;
	private final int WORD_SHIFT = 5;
	private final int HEX_WORD_SIZE = 0x1F;
	private final int HEX_EMPTY_WORD = 0x80000000;
	private int words;
	private int[] map;
	private int capacity;
	//Constructor
	public Bitmap(int size){
		capacity = size;
		words = size/WORD_SIZE;
		if(size % WORD_SIZE != 0){
			words++;
		}
		map = new int[words];
	}
	
	//sets a bit map position
	public void setAtPos(int position){
		int wordIndex;
		int mask;
		int word;
		wordIndex = position >> WORD_SHIFT;
		mask = HEX_EMPTY_WORD >>> (position & HEX_WORD_SIZE);
		word = map[wordIndex];
		map[wordIndex] = word | mask;
	}
	
	//unsets at bit map position
	public void unsetAtPos(int position){
		int wordIndex;
		int mask;
		int word;
		wordIndex = position >> WORD_SHIFT;
		mask = HEX_EMPTY_WORD >>> (position & HEX_WORD_SIZE);
		mask = ~mask;
		word = map[wordIndex];
		map[wordIndex] = word & mask;
	}
	
}
