package com.testbank.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.testbank.model.ConfigItem;
import com.testbank.model.Json;
import com.testbank.model.JsonList;
import com.testbank.service.ConfigService;
import com.testbank.utils.JsonUtils;


@Controller
public class ConfigController {

	@Autowired
	ConfigService configService;
	
	@RequestMapping(value="/getCollectionIds", method=RequestMethod.POST )
	@ResponseBody
	public Json getCollectionIds(){
		HashMap<String,String> banks = configService.getCollectionIds();
		banks.put(" ", "");
		return JsonUtils.returnJsonFromHashMap(banks);
	}
	
	@RequestMapping(value="/getSupportedRequestTypes", method=RequestMethod.POST )
	@ResponseBody
	public Json getSupportedRequestTypes(@RequestParam String bankKey){
		HashMap<String,String> types = configService.getSupportedRequestTypes(bankKey);
		return JsonUtils.returnJsonFromHashMap(types);
	}
	
	@RequestMapping(value="/getSupportedResponseCodes", method=RequestMethod.POST )
	@ResponseBody
	public Json getSupportedResponseCodes(@RequestParam String bankKey,@RequestParam String requesttypeKey){
		HashMap<String,String> rescodes = configService.getSupportedResponseCodes(bankKey, requesttypeKey);
		return JsonUtils.returnJsonFromHashMap(rescodes);
	}
	
	@RequestMapping(value="/addConfig", method=RequestMethod.POST)
	@ResponseBody
	public Json addConfig(@RequestBody ConfigItem[] item,
			@RequestParam String rawcollectionid,
			@RequestParam String rawrequesttype,
			@RequestParam String rawresponsecode) throws Exception {
		return configService.addConfig(item[0],rawcollectionid,rawrequesttype,rawresponsecode);
	}
	
	@RequestMapping(value="/updateConfig", method=RequestMethod.POST)
	@ResponseBody
	public Json updateConfig(@RequestBody ConfigItem[] item,
			@RequestParam String rawcollectionid,
			@RequestParam String rawrequesttype,
			@RequestParam String rawresponsecode) throws Exception {
		return configService.updateConfig(item[0],rawcollectionid,rawrequesttype,rawresponsecode);
	}
	
	@RequestMapping(value="/deleteConfig" )
	@ResponseBody
	public Json deleteConfig(@RequestBody ConfigItem[] item) throws Exception {
		return configService.deleteConfig(item[0].getId());
	}
	
	@RequestMapping(value="/getAllConfig" )
	@ResponseBody
	public JsonList getAllConfig() {
		return configService.getAllConfig();
	}
	
	@RequestMapping(value="/getConfigDescription", method=RequestMethod.POST)
	@ResponseBody
	public Json getConfigDescription(@RequestParam String id) throws Exception {
		return configService.getConfigDescription(id);
	}
}
