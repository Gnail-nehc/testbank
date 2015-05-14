package com.testbank.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.testbank.model.Json;
import com.testbank.model.JsonList;
import com.testbank.model.LogItem;
import com.testbank.model.RtpInfo;
import com.testbank.service.RtpInfoService;


@Controller
public class RtpInfoController {
	private static final Logger logger = Logger.getLogger(RtpInfoController.class);
	@Autowired
	RtpInfoService rtpInfoService;
	
	@RequestMapping(value="/deleteRtpInfo" )
	@ResponseBody
	public Json deleteRtpInfo(@RequestBody RtpInfo[] item) throws Exception {
		return rtpInfoService.deleteRtpInfo(item[0].getId());
	}
	
	@RequestMapping(value="/getRtpInfos" )
	@ResponseBody
	public JsonList getRtpInfos(HttpServletRequest request, HttpServletResponse response) {
		String collectionid=request.getParameter("collectionid");
		String cardno=request.getParameter("cardno");
		String phoneno=request.getParameter("phoneno");
		String date=request.getParameter("date");
		
		return rtpInfoService.getRtpInfos(collectionid,cardno,phoneno,date,600);
	}
	
	@RequestMapping(value="/getRtpInfoDetail" )
	@ResponseBody
	public String getRtpInfoDetail(@RequestParam String id) {
		return rtpInfoService.getRtpInfoDetail(id);
	}
	
}
