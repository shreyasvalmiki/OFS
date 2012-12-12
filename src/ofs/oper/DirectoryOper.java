package ofs.oper;
import java.io.RandomAccessFile;
import ofs.ds.*;
import ofs.utils.*;
import java.util.*;

public class DirectoryOper extends FileOper{
	
	public DirectoryOper(RandomAccessFile file){
		super(file);
	}
	
	public int create(Inode parInode, int parInodeLoc, String fileName){
		this.parInodeLoc = parInodeLoc;
		int err = Constants.NO_ERROR;
		err = addFile(parInode, fileName, Constants.FT_DIR, Constants.DIR, 1);
		if(err != Constants.NO_ERROR)
		{
			return err;
		}
		
		FSUtils.updateInitDirEntry(raFile, curInodeLoc, parInodeLoc, false, curInode.getBlock(0));
		
		return err;
	}
	
	public Inode navigateTo(String path, Inode parInode){
		Inode inode = new Inode();
		ArrayList<DirectoryEntry> dirList = new ArrayList<DirectoryEntry>();
		int inodeNum = -1;
		if(path.startsWith("/")){
			inode = FSUtils.getRootInode(raFile);			
		}
		else
		{
			inode = parInode;
			path = "/"+path;
		}		
		for (String s: Arrays.asList(path.split("/"))){
			if(!s.equals("")){
				dirList = FSUtils.getNavigateList(raFile, inode.getBlock(0));
				inodeNum = getInodeWithName(s,dirList);
				if(inodeNum == -1){
					return null;
				}				
				inode = FSUtils.getInode(raFile, inodeNum);
				//dirList = FSUtils.getNavigateList(raFile, inode.getBlock(0));
			}
		}
		
		return inode;
	}
	
	
}
