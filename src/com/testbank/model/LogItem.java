package com.testbank.model;

public class LogItem {
	private String id;
	private String collectionid;
	private String requesttype;
	private String cardno;
	private String phoneno;
	private String verifiedfield;
	private String type;
	private String expectedtext;
	private String time;
	private String result;
	private String actualvalue;
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
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getActualvalue() {
		return actualvalue;
	}
	public void setActualvalue(String actualvalue) {
		this.actualvalue = actualvalue;
	}
	public String getCollectionid() {
		return collectionid;
	}
	public void setCollectionid(String collectionid) {
		this.collectionid = collectionid;
	}
	public String getRequesttype() {
		return requesttype;
	}
	public void setRequesttype(String requesttype) {
		this.requesttype = requesttype;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
}
