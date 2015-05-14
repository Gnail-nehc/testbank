package com.testbank.enums;

public enum TimeFormatDefiniation {
	FORMAT("yyyyMMdd HHmmss");
	
	private String value;
	
	private TimeFormatDefiniation(String _value){
		this.setValue(_value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
