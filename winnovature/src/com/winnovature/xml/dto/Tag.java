package com.winnovature.xml.dto;

import javax.xml.bind.annotation.XmlAttribute;


public class Tag {

	private String errCode;
	private String op;
	private String seqNum;
	private String tagId;
	private String result;
	
	
	
	public String getErrCode() {
		return errCode;
	}

	@XmlAttribute(name="errCode")
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getResult() {
		return result;
	}

	@XmlAttribute(name="result")
	public void setResult(String result) {
		this.result = result;
	}

	public String getOp() {
		return op;
	}
	
	@XmlAttribute(name="op")
	public void setOp(String op) {
		this.op = op;
	}
	
	public String getSeqNum() {
		return seqNum;
	}
	
	@XmlAttribute(name="seqNum")
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	
	public String getTagId() {
		return tagId;
	}
	
	@XmlAttribute(name="tagId")
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
	
}
