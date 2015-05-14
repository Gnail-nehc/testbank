package com.testbank.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbank.enums.Seperator;
import com.testbank.enums.SpecialFileName;
import com.testbank.enums.TimeFormatDefiniation;
import com.testbank.model.ConfigItem;
import com.testbank.model.Json;
import com.testbank.model.JsonList;
import com.testbank.utils.FileOperationUtils;
import com.testbank.utils.XlsReader;


@Service("configService")
public class ConfigService {
	private static final Logger logger = Logger.getLogger(ConfigService.class);
	@Autowired
	XlsReader xlsReader;
	
	public HashMap<String,String> getCollectionIds(){
		return xlsReader.getCollectionIds();
	}
	
	public HashMap<String,String> getSupportedRequestTypes(String bankKey){
		return xlsReader.getSupportedRequestTypes(bankKey);
	}
	
	public HashMap<String,String> getSupportedResponseCodes(String bankKey,String requesttypeKey){
		if(bankKey.isEmpty() || requesttypeKey.isEmpty()){
			HashMap<String,String> map=new HashMap<String,String>();
			map.put("", "");
			return map;
		}
		return xlsReader.getSupportedResponseCodes(bankKey, requesttypeKey);
	} 
	
	public JsonList getAllConfig() {
		JsonList j = new JsonList();
		List<ConfigItem> row=new ArrayList<ConfigItem>();
		File f=new File(SpecialFileName.ROOT.getValue());
		if(f.exists() && f.isDirectory()){
			for(String filename : f.list()){
				if(filename.contains(Seperator.BASESEPERATOR.getValue())){
					ConfigItem item=new ConfigItem();
					String[] fields = filename.split(Seperator.BASESEPERATOR.getValue());
					item.setId(fields[0]);
					item.setCollectionid(fields[1]);
					item.setRequesttype(fields[2]);
					item.setResponsecode(fields[3]);
					item.setCardno(fields[4]);
					item.setPhoneno(fields[5]);
					item.setTime(fields[6]);
					item.setComment(fields.length>7 ? fields[7] : "");
					row.add(item);
				}
			}
		}
		j.setRows(row);
		j.setTotal(row.size());
		return j;
	}
	
	public Json addConfig(ConfigItem item,String rawcollectionid,String rawrequesttype,String rawresponsecode){
		Json j=new Json();
		try {
			String id=item.getId();
			File f=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue());
			String sep=Seperator.BASESEPERATOR.getValue();
			for(String filename : f.list()){
				if(filename.contains(sep)){
					String cardno=filename.split(sep)[4];
					if(item.getCardno()!=null && item.getCardno().equals(cardno)){
						j.setMsg("机构卡号不能雷同！");
						j.setSuccess(false);
						return j;
					}
				}
			}
			SimpleDateFormat format = new SimpleDateFormat(TimeFormatDefiniation.FORMAT.getValue());			
			String time=format.format(new Date());
			String newfilename=id+sep+item.getCollectionid()+sep+item.getRequesttype()+sep+item.getResponsecode()+sep+item.getCardno()+sep+item.getPhoneno()+sep+time+sep+item.getComment();
			new File(f,newfilename).createNewFile();
			//create description file
			File desp=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.CONFIGDESCRIPTION.getValue());
			desp=new File(desp,id);
			desp.createNewFile();
			String s=Seperator.SEPERATOR.getValue();
			FileUtils.writeStringToFile(desp, rawcollectionid+s+rawrequesttype+s+rawresponsecode);
			j.setSuccess(true);
		} catch (IOException e) {
			j.setMsg(e.getClass()+":"+e.getMessage());
			j.setSuccess(false);
		}
		return j;
	}
	
	public Json updateConfig(ConfigItem item,String rawcollectionid,String rawrequesttype,String rawresponsecode){
		Json j=new Json();
		try {
			String id=item.getId();
			File f=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue());
			String sep=Seperator.BASESEPERATOR.getValue();
			for(String filename : f.list()){
				if(filename.contains(sep) && !filename.startsWith(item.getId())){
					String cardno=filename.split(sep)[4];
					if(item.getCardno()!=null && item.getCardno().equals(cardno)){
						j.setMsg("机构卡号不能雷同！");
						j.setSuccess(false);
						return j;
					}
				}
			}
			for(File file : f.listFiles()){
				if(file.getName().startsWith(id)){
					SimpleDateFormat format = new SimpleDateFormat(TimeFormatDefiniation.FORMAT.getValue());			
					String time=format.format(new Date());
					String newfilename=id+sep+item.getCollectionid()+sep+item.getRequesttype()+sep+item.getResponsecode()+sep+item.getCardno()+sep+item.getPhoneno()+sep+time+sep+item.getComment();
					file.renameTo(new File(f,newfilename));
					//update description file
					File desp=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.CONFIGDESCRIPTION.getValue());
					desp=new File(desp,id);
					if(!desp.exists())
						desp.createNewFile();
					String s=Seperator.SEPERATOR.getValue();
					FileUtils.writeStringToFile(desp, rawcollectionid+s+rawrequesttype+s+rawresponsecode);
					break;
				}
			}
			j.setSuccess(true);
		} catch (IOException e) {
			j.setMsg(e.getClass()+":"+e.getMessage());
			j.setSuccess(false);
		}
		return j;
	}
	
	public Json deleteConfig(String id) {
		Json j = new Json();
		File f=new File(SpecialFileName.ROOT.getValue());
		for(File child : f.listFiles()){
			if(child.getName().startsWith(id)){
				child.delete();
				new File(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.CONFIGDESCRIPTION.getValue()+"/"+id).delete();
				break;
			}
		}
		f=new File(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.CONFIGDESCRIPTION.getValue()+"/"+id);
		if(f.exists()){
			f.delete();
		}
		j.setSuccess(true);
		return j;
	}
	
	public Json getConfigDescription(String id){
		Json j = new Json();
		String description="";
		try {
			File f=new File(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.CONFIGDESCRIPTION.getValue()+"/"+id);
			if(f.exists()){
				description=FileUtils.readFileToString(f);
				description=description.replaceFirst(Seperator.SEPERATOR.getValue(), "~业务种类：");
				description=description.replaceFirst(Seperator.SEPERATOR.getValue(), "\n返回码：");
				description="机构渠道："+description.replace("~", "\n");
			}
		} catch (IOException e) {
			j.setSuccess(false);
		}
		j.setSuccess(true);
		j.setObj(description);
		return j;
	}
	
}
