package ofs.oper;
import java.io.RandomAccessFile;
import ofs.ds.*;
import ofs.utils.FSUtils;
/**
 * Handles operations for a regular file
 * @author shreyasvalmiki
 *
 */
public class RegFileOper extends FileOper{
	public RegFileOper(RandomAccessFile file){
		super(file);
	}
	
	/**
	 * Creates a regular file
	 * @param parInode
	 * @param fileName
	 * @param fileSize
	 * @return
	 */
	public int create(Inode parInode, String fileName, String content){
		int err = Constants.NO_ERROR;
		Inode inode = new Inode();
		err = addFile(parInode, fileName, Constants.FT_REG_FILE, Constants.REG_FILE, content.length()*Constants.SIZE_OF_CHAR);
		if(err != Constants.NO_ERROR)
		{
			return err;
		}
		inode = FSUtils.getInode(raFile, FSUtils.getInodePosFromName(raFile, fileName, parInode.getBlock(0)));
		
		FSUtils.insertTextData(raFile, content, inode.getBlock(0));
		
		return err;
	}
	
	public String showFileData(Inode parInode, String fileName){
		Inode inode = FSUtils.getInode(raFile, FSUtils.getInodePosFromName(raFile, fileName, parInode.getBlock(0)));
		return FSUtils.getTextData(raFile, inode.getSize(), inode.getBlock(0));
	}
}
