package ofs.ds;
/*
 * Modified to requirement from www.java2s.com
 * 9.33.1.Implementation of a bit map of any size, together with static methods to manipulate int, byte and byte[] values as bit maps
 **/
public class Bitmap {
	private int words;
	private int[] map;
	//Constructor
	public Bitmap(int size){
		words = size/Constants.WORD_SIZE;
		if(size % Constants.WORD_SIZE != 0){
			words++;
		}
		map = new int[words];
	}

	//sets a bit map position
	public void setAtPos(int position){
		int wordIndex;
		int mask;
		int word;
		wordIndex = position >> Constants.WORD_SHIFT;
		mask = Constants.HEX_EMPTY_WORD >>> (position & Constants.HEX_WORD_SIZE);
		word = map[wordIndex];
		map[wordIndex] = word | mask;
	}
	
	//unsets at bit map position
	public void unsetAtPos(int position){
		int wordIndex;
		int mask;
		int word;
		wordIndex = position >> Constants.WORD_SHIFT;
		mask = Constants.HEX_EMPTY_WORD >>> (position & Constants.HEX_WORD_SIZE);
		mask = ~mask;
		word = map[wordIndex];
		map[wordIndex] = word & mask;
	}
	
	//checks if bit is set at the position
	public boolean isSetAtPos(int position){
		int wordIndex;
		int mask;
		int word;
		wordIndex = position >> Constants.WORD_SHIFT;
		mask = Constants.HEX_EMPTY_WORD >>> (position & Constants.HEX_WORD_SIZE);
		word = map[wordIndex];
		
		return (mask & word) == 1;
	}
}
