/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;



public class TagInfoDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String BLACKLISTCODE="01";
	public static final String EXEMPTIONCODE="02";
	public static final String LOWBALANCECODE="03";
	public static final String MANUALINSERTIONFLAG="M";
	public static final String ADDOPERATION="ADD";
	public static final String REMOVEOPERATION="REMOVE";
	
	
	private String requestTime;
	private String requestBy;
	private String requestType;
	private String filter;
	private String filterValue;
	
	private String id;
	private String tagId;
	private String tid;
	private String tagClassId;
	private String vehicleNumber;
	private String exceptionCode;
	private String exceptionType;
	private String insertionFlag;
	private String messageId;
	private String npciUpdatedDate;
	
	private String vehicleClass;
	private String tagStatus;
	private String issueDate;
	private String commVehicle;
	private String bankId;
	
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getRequestBy() {
		return requestBy;
	}
	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getFilterValue() {
		return filterValue;
	}
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getInsertionFlag() {
		return insertionFlag;
	}
	public void setInsertionFlag(String insertionFlag) {
		this.insertionFlag = insertionFlag;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getNpciUpdatedDate() {
		return npciUpdatedDate;
	}
	public void setNpciUpdatedDate(String npciUpdatedDate) {
		this.npciUpdatedDate = npciUpdatedDate;
	}
	public String getVehicleClass() {
		return vehicleClass;
	}
	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}
	public String getTagStatus() {
		return tagStatus;
	}
	public void setTagStatus(String tagStatus) {
		this.tagStatus = tagStatus;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getCommVehicle() {
		return commVehicle;
	}
	public void setCommVehicle(String commVehicle) {
		this.commVehicle = commVehicle;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getTagClassId() {
		return tagClassId;
	}
	public void setTagClassId(String tagClassId) {
		this.tagClassId = tagClassId;
	}
	
	
}
