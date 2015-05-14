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
import com.testbank.service.LogService;


@Controller
public class LogController {
	private static final Logger logger = Logger.getLogger(LogController.class);
	@Autowired
	LogService logService;
	
	@RequestMapping(value="/deleteLog" )
	@ResponseBody
	public Json deleteLog(@RequestBody LogItem[] item) throws Exception {
		return logService.deleteLog(item[0].getId());
	}
	
	@RequestMapping(value="/getTestLogs" )
	@ResponseBody
	public JsonList getTestLogs(HttpServletRequest request, HttpServletResponse response) {
		String collectionid=request.getParameter("collectionid");
		String date=request.getParameter("date");
		return logService.getTestLogs(collectionid,date);
	}
	
//	@RequestMapping(value="/searchLogs" )
//	@ResponseBody
//	public JsonList searchLogs(@RequestParam String collectionid, @RequestParam String date) {
//		return logService.searchLogs(collectionid,date);
//	}
	
}
