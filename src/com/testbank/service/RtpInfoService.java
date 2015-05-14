package com.testbank.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.testbank.enums.Seperator;
import com.testbank.enums.SpecialFileName;
import com.testbank.model.Json;
import com.testbank.model.JsonList;
import com.testbank.model.RtpInfo;
import com.testbank.utils.FileOperationUtils;


@Service("rtpInfoService")
public class RtpInfoService {
private static final Logger logger = Logger.getLogger(RtpInfoService.class);
	
	public Json deleteRtpInfo(String id){
		Json j = new Json();
		File dir=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.RTPINFO.getValue());
		for(File f : dir.listFiles()){
			if(f.getName().startsWith(id)){
				f.delete();
				break;
			}
		}
		j.setSuccess(true);
		return j;
	}
	
	public JsonList getRtpInfos(String collectionid,String cardno,String phoneno,String date,int limit){
		JsonList list= new JsonList();
		ArrayList<RtpInfo> rows = new ArrayList<RtpInfo>();
		File dir=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.RTPINFO.getValue());
		boolean fullsearch=!StringUtils.isNullOrEmpty(collectionid) &&
				!StringUtils.isNullOrEmpty(cardno) && 
				!StringUtils.isNullOrEmpty(phoneno) &&
				!StringUtils.isNullOrEmpty(date);
		boolean condition=true;
		int count=0;
		for(String log : dir.list()){
			RtpInfo item = new RtpInfo();
			String[] row=log.split(Seperator.BASESEPERATOR.getValue());
			String actcollectionid=row[1];
			String actcardno=row[3];
			String actphoneno=row[4];
			String acttime=row[5];
			if(fullsearch){
				condition=actcollectionid.equalsIgnoreCase(collectionid) &&
						actcardno.equalsIgnoreCase(cardno) && 
						actphoneno.equalsIgnoreCase(phoneno) &&
						acttime.startsWith(date+' ');
			}else{
				condition=StringUtils.isNullOrEmpty(collectionid) ? true : actcollectionid.equalsIgnoreCase(collectionid);
				condition=condition && (StringUtils.isNullOrEmpty(cardno) ?  true : actcardno.equalsIgnoreCase(cardno));
				condition=condition && (StringUtils.isNullOrEmpty(phoneno) ?  true : actphoneno.equalsIgnoreCase(phoneno));
				condition=condition && (StringUtils.isNullOrEmpty(date) ?  true : acttime.startsWith(date+' '));
			}
			if(condition){
				if(count>=limit)
					break;
				item.setId(row[0]);
				item.setCollectionid(actcollectionid);
				item.setRequesttype(row[2]);
				item.setCardno(row[3]);
				item.setPhoneno(row[4]);
				item.setTime(acttime);
				rows.add(item);
				count++;
			}
		}
		list.setRows(rows);
		list.setTotal(rows.size());
		return list;
	}
	
	public String getRtpInfoDetail(String id){
		String str="";
		try {
			File dir=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.RTPINFO.getValue());
			for(File f : dir.listFiles()){
				if(f.getName().startsWith(id)){
					str=FileUtils.readFileToString(f);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
}
