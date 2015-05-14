package com.testbank.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.testbank.model.Json;


public class JsonUtils {
	public static Json returnJsonFromHashMap(HashMap<String,String> map){
		Json j=new Json();
		ArrayList<HashMap> arr=new ArrayList<HashMap>();
		for(Entry<String,String> entry : map.entrySet()){
			HashMap<String,String> m = new HashMap<String,String>();
			m.put("id", entry.getKey());
			m.put("text", entry.getValue());
			arr.add(m);
		}
		j.setObj(arr);
		j.setSuccess(true);
		return j;
	}
}
