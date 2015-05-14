package com.testbank.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.testbank.enums.Seperator;
import com.testbank.enums.SpecialFileName;
import com.testbank.enums.TimeFormatDefiniation;
import com.testbank.factory.JsonObjectMapperFactory;
import com.testbank.model.CheckPointContianer;
import com.testbank.model.CheckpointItem;
import com.testbank.utils.FileOperationUtils;
import com.testbank.utils.HttpServletRequestUtils;
import com.testbank.utils.UniqueCodeGenerator;


@Controller
public class MainController {
	private static final Logger logger = Logger.getLogger(MainController.class);
	
	@RequestMapping(value="/receive/**/**")
	@ResponseBody
	public String receive(HttpServletRequest request, HttpServletResponse response){
		String res="";
		String url=request.getPathInfo();
		if(url.contains("/collectionid=") && url.contains("/requesttype=") && url.contains("/cardno=") && url.contains("/phoneno=")){
			try {
				String collectionid=StringUtils.substringBetween(url, "/collectionid=", "/requesttype=").toLowerCase();
				String requesttype=StringUtils.substringBetween(url, "/requesttype=", "/cardno=");
				String cardno=StringUtils.substringBetween(url, "/cardno=", "/phoneno=");
				String phoneno=StringUtils.substringAfter(url, "/phoneno=").replace("/", "");
				File root=new File(SpecialFileName.ROOT.getValue());
				if(root.exists() && root.isDirectory()){
					String sep=Seperator.BASESEPERATOR.getValue();
					String infosep=Seperator.CHECKINFOSEPERATOR.getValue();
					for(File child : root.listFiles()){
						if(child.isFile()){
							String filename=child.getName();
							if(filename.split(sep)[2].equalsIgnoreCase(requesttype) && filename.contains(sep+collectionid+sep) && filename.contains(sep+cardno+sep+phoneno+sep)){
								String rescode=filename.split(sep)[3];
								ObjectMapper mapper = JsonObjectMapperFactory.getObjectMapper();
								res=collectionid+sep+requesttype+sep+rescode+sep+cardno+sep+phoneno+sep;
								if(!FileOperationUtils.isFileEmpty(child)){
									CheckPointContianer cps = new CheckPointContianer();
									cps = mapper.readValue(child, CheckPointContianer.class);
									for(Entry<String,CheckpointItem> entry:cps.getCheckPoint().entrySet()){
										CheckpointItem item=entry.getValue();
										res+=item.getVerifiedfield()+infosep+item.getType()+infosep+item.getExpectedtext()+sep;
									}
								}
								return res.substring(0, res.length()-1);
							}
						}
						
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.getClass()+e.getMessage());
			}
		}
		return res;
	}

	@RequestMapping(value="/log", method=RequestMethod.POST)
	@ResponseBody
	public synchronized boolean log(HttpServletRequest request, HttpServletResponse response){
		boolean res=false;
		String collectionid=request.getParameter("collectionid");
		String requesttype=request.getParameter("requesttype");
		String cardno=request.getParameter("cardno");
		String phoneno=request.getParameter("phoneno");
		String verifiedinfo=request.getParameter("verifiedinfo");
		if(collectionid!=null && requesttype!=null && cardno!=null && phoneno!=null && verifiedinfo!=null){
			File dir=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.LOG.getValue());
			String sep=Seperator.BASESEPERATOR.getValue();
			String filename=collectionid+sep+requesttype+sep+cardno+sep+phoneno+sep;
			for(String info : verifiedinfo.split(Seperator.SEMICOLON.getValue())){
				try {
					info=info.replace("-", "").replace(":","");
					String id=UniqueCodeGenerator.generateShortUuid(6);
					new File(dir,id+sep+filename+info).createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return false;
				}
			}
			res=true;
		}
		return res;
	}
	
	@RequestMapping(value="/config", method=RequestMethod.POST)
	@ResponseBody
	public synchronized String config(HttpServletRequest request, HttpServletResponse response){
		try {
			String body=HttpServletRequestUtils.getHttpServletRequestBody(request);
			String collectionid=request.getParameter("collectionid")==null ? HttpServletRequestUtils.getValueFromRequestInput(body,"collectionid") : request.getParameter("collectionid");
			String requesttype=request.getParameter("requesttype")==null ? HttpServletRequestUtils.getValueFromRequestInput(body,"requesttype") : request.getParameter("requesttype");
			String rescode=request.getParameter("responsecode")==null ? HttpServletRequestUtils.getValueFromRequestInput(body,"responsecode") : request.getParameter("responsecode");
			String cardno=request.getParameter("cardno")==null ? HttpServletRequestUtils.getValueFromRequestInput(body,"cardno") : request.getParameter("cardno");
			String phoneno=request.getParameter("phoneno")==null ? HttpServletRequestUtils.getValueFromRequestInput(body,"phoneno") : request.getParameter("phoneno");
			String verifiedinfo=request.getParameter("verifiedinfo")==null ? HttpServletRequestUtils.getValueFromRequestInput(body,"verifiedinfo") : request.getParameter("verifiedinfo");
			if(collectionid!=null && requesttype!=null && rescode!=null && cardno!=null){// && phoneno!=null && verifiedinfo!=null){
				String id=UUID.randomUUID().toString();
				File f=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue());
				String sep=Seperator.BASESEPERATOR.getValue();
				String duplicatedtext=sep+collectionid+sep+requesttype+sep;
				for(String filename : f.list()){
					if(filename.contains(duplicatedtext)){
						return "Fail.Only 1 configuration allowed for the same business type with the same bank。";
					}
				}
				SimpleDateFormat format = new SimpleDateFormat(TimeFormatDefiniation.FORMAT.getValue());			
				String time=format.format(new Date());
				phoneno=(phoneno!=null && !phoneno.isEmpty())?phoneno:"";
				String newfilename=id+duplicatedtext+rescode+sep+cardno+sep+phoneno+sep+time;
				f=new File(f,newfilename);
				f.createNewFile();
//					//create description file
//					File desp=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.CONFIGDESCRIPTION.getValue());
//					desp=new File(desp,id);
//					desp.createNewFile();
//					String s=Seperator.SEPERATOR.getValue();
//					FileUtils.writeStringToFile(desp, collectionid+s+requesttype+s+rescode);
				if(verifiedinfo!=null && !verifiedinfo.isEmpty()){
					for(String info : verifiedinfo.split(sep)){
						CheckpointItem item =new CheckpointItem();
						String[] arr = info.split(Seperator.CHECKINFOSEPERATOR.getValue());
						item.setId(UUID.randomUUID().toString());
						if(arr[0].indexOf('\\') > -1 ||
								arr[0].indexOf('/') > -1 ||
								arr[0].indexOf(':') > -1 ||
								arr[0].indexOf('*') > -1 ||
								arr[0].indexOf('?') > -1 ||
								arr[0].indexOf('"') > -1 ||
								arr[0].indexOf('<') > -1 ||
								arr[0].indexOf('>') > -1 ||
								arr[0].indexOf('|') > -1 ||
								arr[0].indexOf('@') > -1)
							return "Parameter error.Cannot include character \\/:*?\"<>|@'";
						item.setVerifiedfield(arr[0]);
						item.setType(arr[1]);
						item.setExpectedtext(arr[2]); 
						item.setTime(time);
						CheckPointContianer c=new CheckPointContianer();
						c.getCheckPoint().put(item.getId(), item);
						ObjectMapper mapper = JsonObjectMapperFactory.getObjectMapper();
						mapper.writeValue(f, c);
					}
				}
				return "Success. 1 configuration created!";
			}
			return "Fail. Parameter error. collectionid,requesttype,responsecode,cardno are required!";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "Exception. "+e.getMessage()+e.getStackTrace().toString();
		}
	}

	@RequestMapping(value="/sendRtpInfo", method=RequestMethod.POST)
	@ResponseBody
	public synchronized void sendRtpInfo(HttpServletRequest request, HttpServletResponse response){
		String collectionid=request.getParameter("collectionid");
		String requesttype=request.getParameter("requesttype");
		String cardno=request.getParameter("cardno");
		String phoneno=request.getParameter("phoneno");
		String detail=request.getParameter("detail");
		if(collectionid!=null && requesttype!=null && cardno!=null && phoneno!=null){
			try{
				File dir=FileOperationUtils.createFolder(SpecialFileName.ROOT.getValue()+"/"+SpecialFileName.RTPINFO.getValue());
				String sep=Seperator.BASESEPERATOR.getValue();
				String id=UniqueCodeGenerator.generateShortUuid(6);
				String filename=id+sep+collectionid+sep+requesttype+sep+cardno+sep+phoneno+sep;
				SimpleDateFormat format = new SimpleDateFormat(TimeFormatDefiniation.FORMAT.getValue());			
				String time=format.format(new Date());
				File f=new File(dir,filename+time);
				f.createNewFile();
				FileUtils.writeStringToFile(f, detail);
			}catch(Exception ex){
			}
		}
	}

}
