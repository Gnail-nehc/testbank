package com.testbank.model;

public class CheckpointItem {
	private String id;
	private String verifiedfield;
	private String type;
	private String expectedtext;
	private String time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVerifiedfield() {
		return verifiedfield;
	}
	public void setVerifiedfield(String verifiedfield) {
		this.verifiedfield = verifiedfield;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExpectedtext() {
		return expectedtext;
	}
	public void setExpectedtext(String expectedtext) {
		this.expectedtext = expectedtext;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
