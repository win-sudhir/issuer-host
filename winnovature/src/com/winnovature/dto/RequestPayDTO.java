/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;


public class RequestPayDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String txnId; 
	private String acquirerId; 
	private String txnType; 
	private String originalTxnId; 
	private String tagId; 
	private String tid; 
	private String tollPlazaId; 
	private String tollPlazaName; 
	private String txnAmount; 
	private String txnTime; 
	private String vehicleNo;
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getAcquirerId() {
		return acquirerId;
	}
	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getOriginalTxnId() {
		return originalTxnId;
	}
	public void setOriginalTxnId(String originalTxnId) {
		this.originalTxnId = originalTxnId;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTollPlazaId() {
		return tollPlazaId;
	}
	public void setTollPlazaId(String tollPlazaId) {
		this.tollPlazaId = tollPlazaId;
	}
	public String getTollPlazaName() {
		return tollPlazaName;
	}
	public void setTollPlazaName(String tollPlazaName) {
		this.tollPlazaName = tollPlazaName;
	}
	public String getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	
}
