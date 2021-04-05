/**
 * 
 */
package com.winnovature.xml.dto;

import javax.xml.bind.annotation.XmlAttribute;


public class Head {

	private String msgId;
	private String orgId;
	private String ts;
	private String ver;
	
	public String getMsgId() {
		return msgId;
	}
	
	@XmlAttribute(name="msgId")
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public String getOrgId() {
		return orgId;
	}
	
	@XmlAttribute(name="orgId")
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getTs() {
		return ts;
	}
	
	@XmlAttribute(name="ts")
	public void setTs(String ts) {
		this.ts = ts;
	}
	
	public String getVer() {
		return ver;
	}
	
	@XmlAttribute(name="ver")
	public void setVer(String ver) {
		this.ver = ver;
	}
}
