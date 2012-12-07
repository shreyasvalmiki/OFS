package ofs.oper;
import java.io.RandomAccessFile;
import ofs.ds.*;

public class RegFileOper extends FileOper{
	public RegFileOper(RandomAccessFile file){
		super(file);
	}
	
	public int create(Inode parInode, String fileName, int fileSize){
		int err = Constants.NO_ERROR;
		err = addFile(parInode, fileName, Constants.FT_REG_FILE, Constants.REG_FILE, fileSize);
		if(err != Constants.NO_ERROR)
		{
			return err;
		}
		return err;
	}
	

}
