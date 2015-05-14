package com.testbank.utils;

import java.io.File;
import java.io.FileReader;

public class FileOperationUtils {
	public static File createFolder(String path){
		File file=new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	
	public static boolean isFileEmpty(File f){
		try {
			FileReader fr = new FileReader(f);
			char[] arr=new char[32];
			boolean res=fr.read(arr)>0 ? false: true;
			fr.close();
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
