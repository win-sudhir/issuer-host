/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;


public class DisputeMasterDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String CHBFUNCTIONCODE="450";
	public static final String RESONCODETYPE="1";//not used in db
	//for more than 35 days old transaction function code will be 680 and if not then it will be 450
	public static final String GOODFAITH="680";
	public static final String GOODFAITHDAYS="35";
	public static final String CLOSESTATUS="CLOSE";
	public static final String OPENSTATUS="OPEN";
	public static final String MANUALPROCESSSTATUS="M";
	public static final String AUTOMATICPROCESSSTATUS="A";
	public static final String NEW="0";
	public static final String APPROVE="1";
	public static final String REJECT="2";
	public static final String CREDIT="CREDIT";
	public static final String DEBIT="DEBIT";
	public static final String DEBITPAYMODE="DEBITDISPUTEADJ";
	public static final String CREDITPAYMODE="CREDITDISPUTEADJ";
	
	public static final String NEWLY="NEW";
	public static final String APPROVED="APPROVED";
	public static final String REJECTED="REJECTED";
	public static final String PARTNERID="DISPUTE";
	public static final String USERID="WINN";
	public static final String UDF1="CloseDispute";
	public static final String ACQLIBILITY="051";
	
	private String requestTime;
	private String requestBy;
	private String requestType;
	private String filter;
	private String filterValue;
	private String fromDate;
	private String toDate;
	
	
	private String id;
	private String acqTxnId;
	private String tollTxnId;
	private String tollPlazaId;
	private String disputeType;
	//private float amount;
	private String adjustmentType;
	private String disputeDesc;//description
	private String evidenceDoc1;
	private String evidenceDoc2;
	private String evidenceDoc3;
	private String evidenceDoc4;
	private String evidenceDoc5;
	private String evidenceDoc6;
	private String status;
	private String dateTime;
	private String processStatus;
	private String isApproved;
	private String actualTxnAmount; 
	private String remark;
	
	private String createdBy;
	private String createdOn;
	private String lastUpdatedBy;
	private String lastUpdatedOn;
	
	private String reasonCode;
	private String functionCode;
	private String reasonCodeType;
	
	private String tagId; 
	private String txnTime; 
	private String issuerId; 
	private String acquirerId; 
	private String txnAmount; 
	private String fullPartialIndicator; 
	private String tid; 
	private String mmt;
	private String txnType; 
	private String rodt;
	private String txnId;
	private String originalTxnId;
	private String vehicleNumber;
	private String tollPlazaName;
	private String readerTimeStamp;
	private String comment;
	private String duplicateTxnId;
	
	private String referenceId;
	private String plazaReaderId;
	private String tollPlazaType;
	private String approvalStatus;
	
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
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	/*public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}*/
	public String getAcqTxnId() {
		return acqTxnId;
	}
	public void setAcqTxnId(String acqTxnId) {
		this.acqTxnId = acqTxnId;
	}
	public String getTollTxnId() {
		return tollTxnId;
	}
	public void setTollTxnId(String tollTxnId) {
		this.tollTxnId = tollTxnId;
	}
	public String getTollPlazaId() {
		return tollPlazaId;
	}
	public void setTollPlazaId(String tollPlazaId) {
		this.tollPlazaId = tollPlazaId;
	}
	public String getDisputeType() {
		return disputeType;
	}
	public void setDisputeType(String disputeType) {
		this.disputeType = disputeType;
	}
	/*public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}*/
	public String getAdjustmentType() {
		return adjustmentType;
	}
	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	public String getDisputeDesc() {
		return disputeDesc;
	}
	public void setDisputeDesc(String disputeDesc) {
		this.disputeDesc = disputeDesc;
	}
	public String getEvidenceDoc1() {
		return evidenceDoc1;
	}
	public void setEvidenceDoc1(String evidenceDoc1) {
		this.evidenceDoc1 = evidenceDoc1;
	}
	public String getEvidenceDoc2() {
		return evidenceDoc2;
	}
	public void setEvidenceDoc2(String evidenceDoc2) {
		this.evidenceDoc2 = evidenceDoc2;
	}
	public String getEvidenceDoc3() {
		return evidenceDoc3;
	}
	public void setEvidenceDoc3(String evidenceDoc3) {
		this.evidenceDoc3 = evidenceDoc3;
	}
	public String getEvidenceDoc4() {
		return evidenceDoc4;
	}
	public void setEvidenceDoc4(String evidenceDoc4) {
		this.evidenceDoc4 = evidenceDoc4;
	}
	public String getEvidenceDoc5() {
		return evidenceDoc5;
	}
	public void setEvidenceDoc5(String evidenceDoc5) {
		this.evidenceDoc5 = evidenceDoc5;
	}
	public String getEvidenceDoc6() {
		return evidenceDoc6;
	}
	public void setEvidenceDoc6(String evidenceDoc6) {
		this.evidenceDoc6 = evidenceDoc6;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}
	public String getActualTxnAmount() {
		return actualTxnAmount;
	}
	public void setActualTxnAmount(String actualTxnAmount) {
		this.actualTxnAmount = actualTxnAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getReasonCodeType() {
		return reasonCodeType;
	}
	public void setReasonCodeType(String reasonCodeType) {
		this.reasonCodeType = reasonCodeType;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public String getIssuerId() {
		return issuerId;
	}
	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}
	public String getAcquirerId() {
		return acquirerId;
	}
	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}
	public String getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getFullPartialIndicator() {
		return fullPartialIndicator;
	}
	public void setFullPartialIndicator(String fullPartialIndicator) {
		this.fullPartialIndicator = fullPartialIndicator;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getMmt() {
		return mmt;
	}
	public void setMmt(String mmt) {
		this.mmt = mmt;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getRodt() {
		return rodt;
	}
	public void setRodt(String rodt) {
		this.rodt = rodt;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getOriginalTxnId() {
		return originalTxnId;
	}
	public void setOriginalTxnId(String originalTxnId) {
		this.originalTxnId = originalTxnId;
	} 
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getTollPlazaName() {
		return tollPlazaName;
	}
	public void setTollPlazaName(String tollPlazaName) {
		this.tollPlazaName = tollPlazaName;
	}
	public String getReaderTimeStamp() {
		return readerTimeStamp;
	}
	public void setReaderTimeStamp(String readerTimeStamp) {
		this.readerTimeStamp = readerTimeStamp;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDuplicateTxnId() {
		return duplicateTxnId;
	}
	
	public void setDuplicateTxnId(String duplicateTxnId) {
		this.duplicateTxnId = duplicateTxnId;
	}
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getPlazaReaderId() {
		return plazaReaderId;
	}
	public void setPlazaReaderId(String plazaReaderId) {
		this.plazaReaderId = plazaReaderId;
	}
	public String getTollPlazaType() {
		return tollPlazaType;
	}
	public void setTollPlazaType(String tollPlazaType) {
		this.tollPlazaType = tollPlazaType;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
}
