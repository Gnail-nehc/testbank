package com.testbank.model;

import java.util.HashMap;
import java.util.Map;

public class CheckPointContianer {
	Map<String,CheckpointItem> checkPoint= new HashMap<String,CheckpointItem>();

	public Map<String, CheckpointItem> getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(Map<String, CheckpointItem> checkPoint) {
		this.checkPoint = checkPoint;
	}
	
}
