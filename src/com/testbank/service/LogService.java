package com.testbank.service;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.testbank.enums.Seperator;
import com.testbank.enums.SpecialFileName;
import com.testbank.model.Json;
import com.testbank.model.JsonList;
import com.testbank.model.LogItem;
import com.testbank.utils.FileOperationUtils;


@Service("logService")
public class LogService {
	private static final Logger logger = Logger.getLogger(LogService.class);
	
	public Json deleteLog(String id){
		Json j = new Json();
		File dir=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.LOG.getValue());
		for(File f : dir.listFiles()){
			if(f.getName().startsWith(id)){
				f.delete();
				break;
			}
		}
		j.setSuccess(true);
		return j;
	}
	
	public JsonList getTestLogs(String collectionid,String date){
		JsonList list= new JsonList();
		ArrayList<LogItem> rows = new ArrayList<LogItem>();
		File dir=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.LOG.getValue());
		boolean fullsearch=(!StringUtils.isNullOrEmpty(collectionid) && !StringUtils.isNullOrEmpty(date))?true:false;
		boolean condition=false;
		for(String log : dir.list()){
			LogItem item = new LogItem();
			String[] row=log.split(Seperator.BASESEPERATOR.getValue());
			String actcollectionid=row[1];
			String acttime=row[8];
			if(fullsearch){
				condition=actcollectionid.equalsIgnoreCase(collectionid) && acttime.startsWith(date+' ');
			}else{
				if(StringUtils.isNullOrEmpty(collectionid) && StringUtils.isNullOrEmpty(date))
					condition=true;
				else if(StringUtils.isNullOrEmpty(collectionid))
					condition=acttime.startsWith(date+' ');
				else if(StringUtils.isNullOrEmpty(date))
					condition=actcollectionid.equalsIgnoreCase(collectionid);
			}
			if(condition){
				item.setId(row[0]);
				item.setCollectionid(actcollectionid);
				item.setRequesttype(row[2]);
				item.setCardno(row[3]);
				item.setPhoneno(row[4]);
				item.setVerifiedfield(row[5]);
				item.setType(row[6]);
				item.setExpectedtext(row[7]);
				item.setTime(acttime);
				item.setResult(row[9]);
				item.setActualvalue(row[10]);
				rows.add(item);
			}
		}
		list.setRows(rows);
		list.setTotal(rows.size());
		return list;
	}
	
//	public JsonList searchLogs(String collectionid, String date){
//		if(collectionid.isEmpty() && date.isEmpty()){
//			return getTestLogs();
//		}
//		JsonList list=new JsonList();
//		ArrayList<LogItem> rows = new ArrayList<LogItem>();
//		File dir=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.LOG.getValue());
//		for(String info : dir.list()){
//			boolean condition=(!collectionid.isEmpty() && !date.isEmpty()) ? true : false;
//			String sep=Seperator.BASESEPERATOR.getValue();
//			if(condition){
//				condition=info.split(sep)[1].equals(collectionid) && info.split(sep)[8].startsWith(date);
//			}else{
//				if(collectionid.isEmpty())
//					condition=info.split(sep)[8].startsWith(date);
//				else
//					condition=info.split(sep)[1].equals(collectionid);
//			}
//			if(condition){
//				LogItem item = new LogItem();
//				String[] row=info.split(Seperator.BASESEPERATOR.getValue());
//				item.setId(row[0]);
//				item.setCollectionid(row[1]);
//				item.setRequesttype(row[2]);
//				item.setCardno(row[3]);
//				item.setPhoneno(row[4]);
//				item.setVerifiedfield(row[5]);
//				item.setType(row[6]);
//				item.setExpectedtext(row[7]);
//				item.setTime(row[8]);
//				item.setResult(row[9]);
//				item.setActualvalue(row[10]);
//				rows.add(item);
//			}
//		}
//		list.setRows(rows);
//		list.setTotal(rows.size());
//		return list;
//	}
	
}
