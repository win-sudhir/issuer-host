/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;


public class TagAllocationDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/*private String Message;
	private boolean Status;*/
	private String id;
	private String txnId;
	private String TID;
	private String vehicleNumber;
	private String minThreshold;
	
	private String amtIssuence;
	private String amtRecharge;
	private String amtRSA;
	private String amtSecurity;
	private String amtInsurance;
	
	private boolean issuanceCharge;
	
	private String tagClassId;
	// 
	private String tagId; 
	private String vehicleClass; 
	private String comVehicle;
	
	private String errorCode;   
	private String result;  
	private String sequenceNo;  
	private String transactionId;
	private boolean isUnRegister; 
	private String unRegTxnAmount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTID() {
		return TID;
	}
	public void setTID(String tID) {
		TID = tID;
	}
	
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	
	public String getMinThreshold() {
		return minThreshold;
	}
	public void setMinThreshold(String minThreshold) {
		this.minThreshold = minThreshold;
	}
	public String getAmtIssuence() {
		return amtIssuence;
	}
	public void setAmtIssuence(String amtIssuence) {
		this.amtIssuence = amtIssuence;
	}
	public String getAmtRecharge() {
		return amtRecharge;
	}
	public void setAmtRecharge(String amtRecharge) {
		this.amtRecharge = amtRecharge;
	}
	public String getAmtRSA() {
		return amtRSA;
	}
	public void setAmtRSA(String amtRSA) {
		this.amtRSA = amtRSA;
	}
	public String getAmtSecurity() {
		return amtSecurity;
	}
	public void setAmtSecurity(String amtSecurity) {
		this.amtSecurity = amtSecurity;
	}
	public String getAmtInsurance() {
		return amtInsurance;
	}
	public void setAmtInsurance(String amtInsurance) {
		this.amtInsurance = amtInsurance;
	}
	public boolean isIssuanceCharge() {
		return issuanceCharge;
	}
	public void setIssuanceCharge(boolean issuanceCharge) {
		this.issuanceCharge = issuanceCharge;
	}
	public String getTagClassId() {
		return tagClassId;
	}
	public void setTagClassId(String tagClassId) {
		this.tagClassId = tagClassId;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getVehicleClass() {
		return vehicleClass;
	}
	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}
	public String getComVehicle() {
		return comVehicle;
	}
	public void setComVehicle(String comVehicle) {
		this.comVehicle = comVehicle;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public boolean isUnRegister() {
		return isUnRegister;
	}
	public void setUnRegister(boolean isUnRegister) {
		this.isUnRegister = isUnRegister;
	}
	public String getUnRegTxnAmount() {
		return unRegTxnAmount;
	}
	public void setUnRegTxnAmount(String unRegTxnAmount) {
		this.unRegTxnAmount = unRegTxnAmount;
	}
	
}
