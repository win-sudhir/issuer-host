package com.winnovature.dto;

public class TransactionDTO {
	public static String SOURCECHANNEL="WINNOVATURE";
	public static String SOURCECHANNELIP="127.0.0.1";
	
	private String userId;
	private String walletId;
	private String txnType;
	private String txnAmount;
	private String securityDeposit;
	private String remarks;
	private String payMode;
	private String partnerId; 
	private String partnerRefId;
	private String UDF1; 
	private String UDF2;
	private String UDF3; 
	private String UDF4;
	private String UDF5; 
	private String UDF6;
	private String UDF7; 
	private String UDF8;
	private String createdBy; 
	private String sourceChannel;
	private String sourceChannelIP; 
	private String respCode;
	private String respMessage; 
	private String currentBalance;
	
	public TransactionDTO() {}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getSecurityDeposit() {
		return securityDeposit;
	}
	public void setSecurityDeposit(String securityDeposit) {
		this.securityDeposit = securityDeposit;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPartnerRefId() {
		return partnerRefId;
	}
	public void setPartnerRefId(String partnerRefId) {
		this.partnerRefId = partnerRefId;
	}
	public String getUDF1() {
		return UDF1;
	}
	public void setUDF1(String uDF1) {
		UDF1 = uDF1;
	}
	public String getUDF2() {
		return UDF2;
	}
	public void setUDF2(String uDF2) {
		UDF2 = uDF2;
	}
	public String getUDF3() {
		return UDF3;
	}
	public void setUDF3(String uDF3) {
		UDF3 = uDF3;
	}
	public String getUDF4() {
		return UDF4;
	}
	public void setUDF4(String uDF4) {
		UDF4 = uDF4;
	}
	public String getUDF5() {
		return UDF5;
	}
	public void setUDF5(String uDF5) {
		UDF5 = uDF5;
	}
	public String getUDF6() {
		return UDF6;
	}
	public void setUDF6(String uDF6) {
		UDF6 = uDF6;
	}
	public String getUDF7() {
		return UDF7;
	}
	public void setUDF7(String uDF7) {
		UDF7 = uDF7;
	}
	public String getUDF8() {
		return UDF8;
	}
	public void setUDF8(String uDF8) {
		UDF8 = uDF8;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getSourceChannel() {
		return sourceChannel;
	}
	public void setSourceChannel(String sourceChannel) {
		this.sourceChannel = sourceChannel;
	}
	public String getSourceChannelIP() {
		return sourceChannelIP;
	}
	public void setSourceChannelIP(String sourceChannelIP) {
		this.sourceChannelIP = sourceChannelIP;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	
}
