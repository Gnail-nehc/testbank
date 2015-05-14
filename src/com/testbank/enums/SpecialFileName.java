package com.testbank.enums;

public enum SpecialFileName {
	ROOT("testbank"),CONFIGDESCRIPTION("desp"),LOG("log"),RTPINFO("rtpinfo");
	
	private String value;
	
	private SpecialFileName(String _value){
		this.setValue(_value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
