package com.winnovature.dto;

public class CustomerDTO {
	public static String CORPORATE = "Y";
	public static String NOTCORPORATE = "N";
	public static String CORPORATECUSTOMER = "CORPORATE";
	public static String RETAILCUSTOMER = "RATAIL";
	
	private String userId;
	private String walletId;
	private String parentId;
	private String customerName;
	private String emailId;
	private String contactNumber;
	private String dob;
	private String isCorporate;
	private String customerType;
	private String gender;
	private String occupation;
	private String status;
	private String isWallet;
	private String createdBy;
	private String createdOn;
	private String approvedBy;
	private String approvedOn;
	private String modifiedBy;
	private String modifiedOn;
	
	private double currentBalance;
	private double maxBalance;
	private double minBalance;
	private double secDeposit;
	private double minSecDeposit;
	private String kycType;
	private String valildityUpto;
	
	public CustomerDTO() {}
	
	public CustomerDTO(String userId, String walletId, String customerName, String emailId, String contactNumber,
			String dob, String isCorporate, String gender, String occupation, String status, 
			String createdBy, String createdOn) {
		super();
		this.userId = userId;
		this.walletId = walletId;
		this.customerName = customerName;
		this.emailId = emailId;
		this.contactNumber = contactNumber;
		this.dob = dob;
		this.isCorporate = isCorporate;
		this.gender = gender;
		this.occupation = occupation;
		this.status = status;
		;
		this.createdOn = createdOn;
	}

	/*
	 * public CustomerDTO(String userId, String walletId, String parentId, String
	 * customerName, String emailId, String contactNumber, String dob, String
	 * isCorporate, String customerType, String gender, String occupation, String
	 * status, String isWallet, String createdBy, String createdOn, String
	 * approvedBy, String approvedOn, String modifiedBy, String modifiedOn) {
	 * super(); this.userId = userId; this.walletId = walletId; this.parentId =
	 * parentId; this.customerName = customerName; this.emailId = emailId;
	 * this.contactNumber = contactNumber; this.dob = dob; this.isCorporate =
	 * isCorporate; this.customerType = customerType; this.gender = gender;
	 * this.occupation = occupation; this.status = status; this.isWallet = isWallet;
	 * this.createdBy = createdBy; this.createdOn = createdOn; this.approvedBy =
	 * approvedBy; this.approvedOn = approvedOn; this.modifiedBy = modifiedBy;
	 * this.modifiedOn = modifiedOn; }
	 */
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getApprovedOn() {
		return approvedOn;
	}
	public void setApprovedOn(String approvedOn) {
		this.approvedOn = approvedOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getIsWallet() {
		return isWallet;
	}
	public void setIsWallet(String isWallet) {
		this.isWallet = isWallet;
	}
	public String getIsCorporate() {
		return isCorporate;
	}
	public void setIsCorporate(String isCorporate) {
		this.isCorporate = isCorporate;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getMaxBalance() {
		return maxBalance;
	}

	public void setMaxBalance(double maxBalance) {
		this.maxBalance = maxBalance;
	}

	public double getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(double minBalance) {
		this.minBalance = minBalance;
	}

	public double getSecDeposit() {
		return secDeposit;
	}

	public void setSecDeposit(double secDeposit) {
		this.secDeposit = secDeposit;
	}

	public double getMinSecDeposit() {
		return minSecDeposit;
	}

	public void setMinSecDeposit(double minSecDeposit) {
		this.minSecDeposit = minSecDeposit;
	}

	public String getKycType() {
		return kycType;
	}

	public void setKycType(String kycType) {
		this.kycType = kycType;
	}

	public String getValildityUpto() {
		return valildityUpto;
	}

	public void setValildityUpto(String valildityUpto) {
		this.valildityUpto = valildityUpto;
	}
	
}
