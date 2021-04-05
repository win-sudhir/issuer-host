/**
 * 
 */
package com.winnovature.xml.dto;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlElement;


public class Txn {

	private String id;
	private String note;
	private String orgTxnId;
	private String refId;
	private String refUrl;
	private String ts;
	private String type;
	
	private Resp resp;
	
	public String getId() {
		return id;
	}
	
	@XmlAttribute(name="id")
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNote() {
		return note;
	}
	
	@XmlAttribute(name="note")
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getOrgTxnId() {
		return orgTxnId;
	}
	
	@XmlAttribute(name="orgTxnId")
	public void setOrgTxnId(String orgTxnId) {
		this.orgTxnId = orgTxnId;
	}
	public String getRefId() {
		return refId;
	}
	
	@XmlAttribute(name="refId")
	public void setRefId(String refId) {
		this.refId = refId;
	}
	
	public String getRefUrl() {
		return refUrl;
	}
	
	@XmlAttribute(name="refUrl")
	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}
	
	public String getTs() {
		return ts;
	}
	
	@XmlAttribute(name="ts")
	public void setTs(String ts) {
		this.ts = ts;
	}
	
	public String getType() {
		return type;
	}
	
	@XmlAttribute(name="type")
	public void setType(String type) {
		this.type = type;
	}

	public Resp getResp() {
		return resp;
	}

	@XmlElement(name="Resp")
	public void setResp(Resp resp) {
		this.resp = resp;
	}
	
	
}
