package com.winnovature.dto;

public class AccountDTO {
	private String userId;
	private String bankName;
	private String accountNumber;
	private String accountType;
	private String ifscCode;
	private String branchAddress;
	private String status;
	private String createdOn;
	public AccountDTO() {}
	public AccountDTO(String userId, String bankName, String accountNumber, String accountType, String ifscCode,
			String branchAddress, String status, String createdOn) {
		super();
		this.userId = userId;
		this.bankName = bankName;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.ifscCode = ifscCode;
		this.branchAddress = branchAddress;
		this.status = status;
		this.createdOn = createdOn;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBranchAddress() {
		return branchAddress;
	}
	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
}
