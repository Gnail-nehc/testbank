package com.testbank.utils;

import java.io.File;
import java.util.HashMap;

import jxl.Sheet;
import jxl.Workbook;

public class XlsReader {
	private String xlspath;
	
	public XlsReader(String path){
		xlspath = path;
	}
	
	public HashMap<String,String> getCollectionIds(){
		HashMap<String,String> banks = new HashMap<String,String>();
		File inputWorkbook = new File(xlspath);
		if(inputWorkbook.exists() && inputWorkbook.isFile()){
			try {
				Workbook w = Workbook.getWorkbook(inputWorkbook);
		    	Sheet sheet = w.getSheet(0);
		        for (int i = 1; i < sheet.getRows(); i++) {
		        	String key=sheet.getCell(0, i).getContents();
		        	String val=sheet.getCell(1, i).getContents();
		        	if(!key.isEmpty() && !val.isEmpty()){
		        		banks.put(key, val);
		        	}
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    } 
//			finally{
//		    	w.close();
//		    }
		}
		return banks;
	}
	
	public HashMap<String,String> getSupportedRequestTypes(String bankKey){
		HashMap<String,String> requesttypes = new HashMap<String,String>();
		File inputWorkbook = new File(xlspath);
		if(inputWorkbook.exists() && inputWorkbook.isFile()){
			Workbook w = null;
			try {
		    	w = Workbook.getWorkbook(inputWorkbook);
		    	Sheet sheet = w.getSheet(0);
		    	int row=0;
		        for (int i = 1; i < sheet.getRows(); i++) {
		        	String key=sheet.getCell(0, i).getContents();
		        	if(bankKey.equals(key)){
		        		row=i;
		        		break;  
		        	}
		        }
		        if(row!=0){
		        	for(int j = row; j < sheet.getRows(); j++){
		        		String compare=sheet.getCell(0, j).getContents();
		        		if(!compare.isEmpty() && !bankKey.equals(compare)){
		        			break;
		        		}
		        		String key=sheet.getCell(2, j).getContents();
		        		String val=sheet.getCell(3, j).getContents();
		        		if(!key.isEmpty() && !val.isEmpty()){
		        			requesttypes.put(key, val);
		        		}
		        	}
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    } 
//			finally{
//		    	w.close();
//		    }
		}
		return requesttypes ;
	}

	public HashMap<String,String> getSupportedResponseCodes(String bankKey,String requesttypeKey){
		HashMap<String,String> responsecodes = new HashMap<String,String>();
		File inputWorkbook = new File(xlspath);
		if(inputWorkbook.exists() && inputWorkbook.isFile()){
			Workbook w = null;
			try {
		    	w = Workbook.getWorkbook(inputWorkbook);
		    	Sheet sheet = w.getSheet(0);
		    	int row=0;
		        for (int i = 1; i < sheet.getRows(); i++) {
		        	String key=sheet.getCell(0, i).getContents();
		        	if(bankKey.equals(key)){
		        		row=i;
		        		break;
		        	}
		        }
		        if(row!=0){
		        	int row2=0;
					for (int j = row; j < sheet.getRows(); j++) {
						String key=sheet.getCell(2, j).getContents();
						if(requesttypeKey.equals(key)){
							row2=j;
							break;
						}
					}
					if(row2!=0){
						for(int k = row2; k < sheet.getRows(); k++){
			        		String compare=sheet.getCell(2, k).getContents();
			        		if(!compare.isEmpty() && !requesttypeKey.equals(compare)){
			        			break;
			        		}
			        		String key=sheet.getCell(4, k).getContents();
			        		String val=sheet.getCell(5, k).getContents();
			        		if(!key.isEmpty() && !val.isEmpty()){
			        			responsecodes.put(key, val);
			        		}
			        	}
					}
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    } 
//			finally{
//		    	w.close();
//		    }
		}
		return responsecodes ;
	}
	
	public HashMap<String,String> getVerifiedFieldsByCollectionId(String collectionid){
		HashMap<String,String> fields = new HashMap<String,String>();
		File inputWorkbook = new File(xlspath);
		if(inputWorkbook.exists() && inputWorkbook.isFile()){
			Workbook w = null;
			try {
		    	w = Workbook.getWorkbook(inputWorkbook);
		    	Sheet sheet = w.getSheet(1);
		    	int row=0;
		        for (int i = 1; i < sheet.getRows(); i++) {
		        	String key=sheet.getCell(0, i).getContents();
		        	if(collectionid.equals(key)){
		        		row=i;
		        		break;  
		        	}
		        }
		        if(row!=0){
		        	for(int j = row; j < sheet.getRows(); j++){
		        		String compare=sheet.getCell(0, j).getContents();
		        		if(!compare.isEmpty() && !collectionid.equals(compare)){
		        			break;
		        		}
		        		String key=sheet.getCell(2, j).getContents();
		        		String val=sheet.getCell(3, j).getContents();
		        		if(!key.isEmpty() && !val.isEmpty()){
		        			fields.put(key, val);
		        		}
		        	}
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    } 
		}
		return fields ;
	}
}
