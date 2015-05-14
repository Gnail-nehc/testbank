package com.testbank.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbank.enums.Seperator;
import com.testbank.enums.SpecialFileName;
import com.testbank.enums.TimeFormatDefiniation;
import com.testbank.factory.JsonObjectMapperFactory;
import com.testbank.model.CheckPointContianer;
import com.testbank.model.CheckpointItem;
import com.testbank.model.Json;
import com.testbank.model.JsonList;
import com.testbank.utils.FileOperationUtils;
import com.testbank.utils.XlsReader;


@Service("checkpointService")
public class CheckpointService {
	private static final Logger logger = Logger.getLogger(CheckpointService.class);
	@Autowired
	XlsReader xlsReader;
	
	public HashMap<String,String> getVerifiedFieldsByCollectionId(String collectionid){
		return xlsReader.getVerifiedFieldsByCollectionId(collectionid);
	}
	
	public JsonList getAllCheckpoints(String configid) {
		JsonList j = new JsonList();
		List<CheckpointItem> row=new ArrayList<CheckpointItem>();
		try {
			File f = getConfigFileById(configid);
			if(f!=null){
				if(!FileOperationUtils.isFileEmpty(f)){
					ObjectMapper mapper = JsonObjectMapperFactory.getObjectMapper();
					CheckPointContianer cps = new CheckPointContianer();
					cps = mapper.readValue(f, CheckPointContianer.class);
					for(Entry<String,CheckpointItem> entry:cps.getCheckPoint().entrySet()){
						row.add(entry.getValue());
					}
				}
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		j.setRows(row);
		j.setTotal(row.size());
		return j;
	}
	
	public Json addCheckpoint(String configid,CheckpointItem item){
		Json j=new Json();
		try {
			File f = getConfigFileById(configid);
			if(f!=null){
				ObjectMapper mapper = JsonObjectMapperFactory.getObjectMapper();
				CheckPointContianer c=new CheckPointContianer();
				if(!FileOperationUtils.isFileEmpty(f)){
					c= mapper.readValue(f, CheckPointContianer.class);
				}
				SimpleDateFormat format = new SimpleDateFormat(TimeFormatDefiniation.FORMAT.getValue());			
				item.setTime(format.format(new Date()));
				c.getCheckPoint().put(item.getId(), item);
				mapper.writeValue(f, c);
				j.setSuccess(true);
			}else{
				j.setMsg("not found config file");
				j.setSuccess(false);
			}
		} catch (Exception e) {
			j.setMsg(e.getClass()+":"+e.getMessage());
			j.setSuccess(false);
		}
		return j;
	}
	
	public Json deleteCheckpoint(String configid,String id) throws Exception{
		Json j = new Json();
		File f = getConfigFileById(configid);
		if(f!=null){
			ObjectMapper mapper = JsonObjectMapperFactory.getObjectMapper();
			CheckPointContianer cps = mapper.readValue(f, CheckPointContianer.class);
			cps.getCheckPoint().remove(id);
			mapper.writeValue(f, cps);
			j.setSuccess(true);
		}else{
			j.setMsg("not found config file");
			j.setSuccess(false);
		}
		return j;
	}
	
	private File getConfigFileById(String configid){
		File f=new File(SpecialFileName.ROOT.getValue());
		if(f.exists() && f.isDirectory()){
			for(File child : f.listFiles()){
				if(child.getName().startsWith(configid+Seperator.BASESEPERATOR.getValue())){
					return child;
				}
			}
		}
		return null;
	}
}
