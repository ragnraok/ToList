package com.ragnarok.tolist.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class FileIOUtil {

	/**
     * get the FileOutputStream
     * @param filePath the filePath must contain head "/"
     * @return
     */
    public static FileOutputStream getFileOutputStream(String filePath) {
		FileOutputStream fouts = null;
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filePath.trim());
		
		if (file.exists() && file.isFile()) {
			try {
				fouts = new FileOutputStream(file);
				Log.d("Ragnarok", "get the fouts path = " + file.getAbsolutePath());
				return fouts;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {	
			try {
				File fileDirs = new File(file.getParent());
				fileDirs.mkdirs();
				//Log.d(LOG_TAG, "make the fileDirs " + fileDirs.getPath());
				//file.createNewFile();	
				//Log.d("Ragnarok", "create a new file name " + file.getName());
				//Log.d("Ragnarok", "file path " + file.getAbsolutePath());
				synchronized (file) {
					file.createNewFile();
					fouts = new FileOutputStream(file);
					return fouts;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
    
    /**
     * get the FileInputStream
     * @param filePath, it must contains the head "/"
     * @return
     * @throws FileNotFoundException if the file is not exist
     */
    public static FileInputStream getFileInputStream(String filePath) throws FileNotFoundException {
    	FileInputStream fins = null;
    	File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + filePath);
    	fins = new FileInputStream(file);
    	return fins;
    }
    
    /**
     * delete all file contains in this path
     * @param path, it must be the absolute path
     * @return
     */
    public static boolean deleteAllUnderPath(String path) {
		boolean bool = false; 	
	     File f = new File(path);
	     if(f.exists() && f.isDirectory()){
	       if(f.listFiles().length==0)
	       	return true;
	       else{
	         File[] flist = f.listFiles();
	         for(int i = 0; i < flist.length; i++){
	           if(flist[i].isDirectory()){
	           	deleteAllUnderPath(flist[i].getAbsolutePath());
	           }
	           flist[i].delete();
	         }
	       }
	       bool = true;
	    }
	    return bool;
	}
}
