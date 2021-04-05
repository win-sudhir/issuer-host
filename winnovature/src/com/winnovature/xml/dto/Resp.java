/**
 * 
 */
package com.winnovature.xml.dto;

//import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class Resp {

	private String respCode;
	private String result;
	private String sucessReqCnt;
	private String totReqCnt;
	private String ts;
	
	private List<Tag> tagLst;
	
	//added by sud
	private Tag tag;
	private Vehicle vehicle;
	
	public String getRespCode() {
		return respCode;
	}

	@XmlAttribute(name="respCode")
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getResult() {
		return result;
	}

	@XmlAttribute(name="result")
	public void setResult(String result) {
		this.result = result;
	}

	public String getSucessReqCnt() {
		return sucessReqCnt;
	}

	@XmlAttribute(name="sucessReqCnt")
	public void setSucessReqCnt(String sucessReqCnt) {
		this.sucessReqCnt = sucessReqCnt;
	}

	public String getTotReqCnt() {
		return totReqCnt;
	}

	@XmlAttribute(name="totReqCnt")
	public void setTotReqCnt(String totReqCnt) {
		this.totReqCnt = totReqCnt;
	}

	public String getTs() {
		return ts;
	}

	@XmlAttribute(name="ts")
	public void setTs(String ts) {
		this.ts = ts;
	}

	public List<Tag> getTagLst() {
		return tagLst;
	}
/*
	@XmlElement(name="Tag")
	public void setTagLst(List<Tag> tagLst) {
		this.tagLst = tagLst;
	}

	public void setTag(Tag tag) {
		
		if(tagLst==null) {
			tagLst = new ArrayList<>();
		}
		
		tagLst.add(tag);
	}
*/	
	public Tag getTag() {
		return tag;
	}

	@XmlElement(name="Tag")
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	@XmlElement(name="Vehicle")
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}
