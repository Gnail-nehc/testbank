package com.testbank.enums;

public enum Seperator {
	BASESEPERATOR("@"),SEPERATOR("\n"),CHECKINFOSEPERATOR("~"),COMMA(","),SEMICOLON(";");
	
	private String value;
	
	private Seperator(String _value){
		this.setValue(_value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
