package com.testbank.controller;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.testbank.model.CheckpointItem;
import com.testbank.model.Json;
import com.testbank.model.JsonList;
import com.testbank.service.CheckpointService;
import com.testbank.utils.JsonUtils;


@Controller
public class CheckpointController {
	private static final Logger logger = Logger.getLogger(CheckpointController.class);
	@Autowired
	CheckpointService checkpointService;
	
	@RequestMapping(value="/getVerifiedFieldsByCollectionId", method=RequestMethod.POST )
	@ResponseBody
	public Json getVerifiedFieldsByCollectionId(@RequestParam String collectionid){
		HashMap<String,String> fields = checkpointService.getVerifiedFieldsByCollectionId(collectionid);
		return JsonUtils.returnJsonFromHashMap(fields);
	}
	
	@RequestMapping(value="/addCheckpoint" )
	@ResponseBody
	public Json addCheckpoint(@RequestParam String configid,@RequestBody CheckpointItem[] item) throws Exception {
		return checkpointService.addCheckpoint(configid,item[0]);
	}
	
	@RequestMapping(value="/deleteCheckpoint" )
	@ResponseBody
	public Json deleteCheckpoint(@RequestParam String configid,@RequestBody CheckpointItem[] item) throws Exception {
		return checkpointService.deleteCheckpoint(configid,item[0].getId());
	}
	
	@RequestMapping(value="/getAllCheckpoints" )
	@ResponseBody
	public JsonList getAllCheckpoints(@RequestParam String configid) {
		return checkpointService.getAllCheckpoints(configid);
	}
	
}
