package com.winnovature.dto;

public class BranchAccountDTO {

	private String branchId;
	private String bankName;
	private String accountNumber;
	private String accountType;
	private String ifscCode;
	private String branchAddress;
	private String panCardDoc;
	private String agreementDoc;

	public BranchAccountDTO() {
	}

	public BranchAccountDTO(String branchId, String bankName, String accountNumber, String accountType, String ifscCode,
			String branchAddress, String panCardDoc, String agreementDoc) {
		super();
		this.branchId = branchId;
		this.bankName = bankName;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.ifscCode = ifscCode;
		this.branchAddress = branchAddress;
		this.panCardDoc = panCardDoc;
		this.agreementDoc = agreementDoc;

	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
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

	public String getPanCardDoc() {
		return panCardDoc;
	}

	public void setPanCardDoc(String panCardDoc) {
		this.panCardDoc = panCardDoc;
	}

	public String getAgreementDoc() {
		return agreementDoc;
	}

	public void setAgreementDoc(String agreementDoc) {
		this.agreementDoc = agreementDoc;
	}

	@Override
	public String toString() {
		return "BranchAccountDTO [branchId=" + branchId + ", bankName=" + bankName + ", accountNumber=" + accountNumber
				+ ", accountType=" + accountType + ", ifscCode=" + ifscCode + ", branchAddress=" + branchAddress
				+ ", panCardDoc=" + panCardDoc + ", agreementDoc=" + agreementDoc + "]";
	}

}
