package com.winnovature.dto;

public class WalletDTO {
	public static String LTDKYC="LTDKYC";
	public static String FULLKYC="FULLKYC";
	public static double CURRENTBALANCE=0;
	public static double MINBALANCE=100;
	public static double SECURITY=0;
	public static double MINSECURITY=0;
	public static double MAXBALANCE=100000;
	public static double LDTMAXBALANCE=10000;
	
	private String walletId;
	private double currentBalance;
	private double maxBalance;
	private double minBalance;
	private double secDeposit;
	private double minSecDeposit;
	private String kycType;
	private String status;
	private String valildityUpto;
	private String createdBy;
	private String createdOn;
	private String modifiedBy;
	private String modifiedOn;
	
	public WalletDTO(String walletId, double currentBalance, double maxBalance, double minBalance, double secDeposit,
			double minSecDeposit, String kycType, String status, String valildityUpto, String createdBy,
			String createdOn, String modifiedBy, String modifiedOn) {
		super();
		this.walletId = walletId;
		this.currentBalance = currentBalance;
		this.maxBalance = maxBalance;
		this.minBalance = minBalance;
		this.secDeposit = secDeposit;
		this.minSecDeposit = minSecDeposit;
		this.kycType = kycType;
		this.status = status;
		this.valildityUpto = valildityUpto;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = modifiedOn;
	}
	
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getValildityUpto() {
		return valildityUpto;
	}
	public void setValildityUpto(String valildityUpto) {
		this.valildityUpto = valildityUpto;
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
	
	
}
