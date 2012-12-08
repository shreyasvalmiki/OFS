package ofs.ds;
/*
 * Modified to requirement from www.java2s.com
 * 9.33.1.Implementation of a bit map of any size, together with static methods to manipulate int, byte and byte[] values as bit maps
 **/
public class Bitmap {
	private int words;
	private int size;
	private int[] map;
	//Constructor
	public Bitmap(int size){
		this.size = size;
		words = size/Constants.WORD_SIZE;
		if(size % Constants.WORD_SIZE != 0){
			words++;
		}
		map = new int[words];
	}
	
	//gets the number of words
	public int getWords(){
		return words;
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
	
	//
	public void setMapAtPos(int wordIndex, int val){
		map[wordIndex] = val;
	}
	public int[] getMap(){
		return map;
	}
	//Prints the bitmap
	public void print(){
		for(int i = 0; i<words; ++i)
		{
			System.out.print(Integer.toBinaryString(map[i]));
			
		}
	}
	
	public int getFirstFreeBit(){
		for(int i = 2;i < size;++i){
			if(!isSetAtPos(i))
			{
				return i;
			}
		}
		return -1;
	}
	
	public int setFirstEmptyBit(){
		for(int i = 2;i < size;++i){
			if(!isSetAtPos(i))
			{
				setAtPos(i);
				return i;
			}
		}
		return -1;
	}
	
	public boolean isFree(int numOfBits){
		int i = 0;
		
		while(i < size){
			if(isSetAtPos(i)){
				--numOfBits;
			}
			else
			{
				return false;
			}
			if(numOfBits == 0){
				return true;
			}
			++i;
		}
		return false;
	}
}
