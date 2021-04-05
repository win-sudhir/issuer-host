package com.winnovature.dto;

import java.io.Serializable;
import java.util.List;

public class Recon861DTO {

	private String requestType;
	private String reconDate;
	private String reconCycle;
	private String fileName;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getReconDate() {
		return reconDate;
	}

	public void setReconDate(String reconDate) {
		this.reconDate = reconDate;
	}

	public String getReconCycle() {
		return reconCycle;
	}

	public void setReconCycle(String reconCycle) {
		this.reconCycle = reconCycle;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private List<TransactionInfoDTO> transactionInfoDTO;

	public List<TransactionInfoDTO> getTransactionInfoDTO() {
		return transactionInfoDTO;
	}

	public void setTransactionInfoDTO(List<TransactionInfoDTO> transactionInfoDTO) {
		this.transactionInfoDTO = transactionInfoDTO;
	}

	public static class TransactionInfoDTO implements Serializable {

		private static final long serialVersionUID = 1L;
		private String transactionSequenceNumber;
		private String transactionId;
		private String messageId;
		private String note;
		private String referenceId;
		private String referenceURL;
		private String transactionDateAndTime;
		private String transactionType;
		private String originalTransactionId;
		private String tagId;
		private String TID;
		private String AVC;
		private String WIM;
		private String merchantId;
		private String merchantType;
		private String subMerchantType;
		private String laneId;
		private String laneDirection;
		private String laneReaderId;
		private String parkingFloor;
		private String parkingZone;
		private String parkingSlot;
		private String parkingReaderId;
		private String readerReadDateAndTime;
		private String signatureData;
		private String signatureAuthentication;
		private String ePCVerified;
		private String procRestrictionRes;
		private String vehicleAuth;
		private String publicKeyCVV;
		private String readerTransactionCounter;
		private String readerTransactionStatus;
		private String payerAddress;
		private String issuerId;
		private String payerCode;
		private String payerName;
		private String payerType;
		private String transactionAmount;
		private String currencyCode;
		private String payeeAddress;
		private String acquirerId;
		private String payeeCode;
		private String payeeName;
		private String payeeType;
		private String responseCode;
		private String transactionStatus;
		private String approvalNumber;
		private String payeeErrorCode;
		private String settledAmount;
		private String settledCurrency;
		private String accountType;
		private String availableBalance;
		private String ledgerBalance;
		private String accountNumber;
		private String customerName;
		private String initiatedBy;
		private String initiatedTime;
		private String lastUpdatedBy;
		private String lastUpdatedTime;
		private String vehicleRegistrationNumber;
		private String vehicleClass;
		private String vehicleType;
		private String tagStatus;
		private String tagIssueDate;

		public String getTransactionSequenceNumber() {
			return transactionSequenceNumber;
		}

		public void setTransactionSequenceNumber(String transactionSequenceNumber) {
			this.transactionSequenceNumber = transactionSequenceNumber;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getMessageId() {
			return messageId;
		}

		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

		public String getReferenceId() {
			return referenceId;
		}

		public void setReferenceId(String referenceId) {
			this.referenceId = referenceId;
		}

		public String getReferenceURL() {
			return referenceURL;
		}

		public void setReferenceURL(String referenceURL) {
			this.referenceURL = referenceURL;
		}

		public String getTransactionDateAndTime() {
			return transactionDateAndTime;
		}

		public void setTransactionDateAndTime(String transactionDateAndTime) {
			this.transactionDateAndTime = transactionDateAndTime;
		}

		public String getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}

		public String getOriginalTransactionId() {
			return originalTransactionId;
		}

		public void setOriginalTransactionId(String originalTransactionId) {
			this.originalTransactionId = originalTransactionId;
		}

		public String getTagId() {
			return tagId;
		}

		public void setTagId(String tagId) {
			this.tagId = tagId;
		}

		public String getTID() {
			return TID;
		}

		public void setTID(String tID) {
			TID = tID;
		}

		public String getAVC() {
			return AVC;
		}

		public void setAVC(String aVC) {
			AVC = aVC;
		}

		public String getWIM() {
			return WIM;
		}

		public void setWIM(String wIM) {
			WIM = wIM;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getMerchantType() {
			return merchantType;
		}

		public void setMerchantType(String merchantType) {
			this.merchantType = merchantType;
		}

		public String getSubMerchantType() {
			return subMerchantType;
		}

		public void setSubMerchantType(String subMerchantType) {
			this.subMerchantType = subMerchantType;
		}

		public String getLaneId() {
			return laneId;
		}

		public void setLaneId(String laneId) {
			this.laneId = laneId;
		}

		public String getLaneDirection() {
			return laneDirection;
		}

		public void setLaneDirection(String laneDirection) {
			this.laneDirection = laneDirection;
		}

		public String getLaneReaderId() {
			return laneReaderId;
		}

		public void setLaneReaderId(String laneReaderId) {
			this.laneReaderId = laneReaderId;
		}

		public String getParkingFloor() {
			return parkingFloor;
		}

		public void setParkingFloor(String parkingFloor) {
			this.parkingFloor = parkingFloor;
		}

		public String getParkingZone() {
			return parkingZone;
		}

		public void setParkingZone(String parkingZone) {
			this.parkingZone = parkingZone;
		}

		public String getParkingSlot() {
			return parkingSlot;
		}

		public void setParkingSlot(String parkingSlot) {
			this.parkingSlot = parkingSlot;
		}

		public String getParkingReaderId() {
			return parkingReaderId;
		}

		public void setParkingReaderId(String parkingReaderId) {
			this.parkingReaderId = parkingReaderId;
		}

		public String getReaderReadDateAndTime() {
			return readerReadDateAndTime;
		}

		public void setReaderReadDateAndTime(String readerReadDateAndTime) {
			this.readerReadDateAndTime = readerReadDateAndTime;
		}

		public String getSignatureData() {
			return signatureData;
		}

		public void setSignatureData(String signatureData) {
			this.signatureData = signatureData;
		}

		public String getSignatureAuthentication() {
			return signatureAuthentication;
		}

		public void setSignatureAuthentication(String signatureAuthentication) {
			this.signatureAuthentication = signatureAuthentication;
		}

		public String getePCVerified() {
			return ePCVerified;
		}

		public void setePCVerified(String ePCVerified) {
			this.ePCVerified = ePCVerified;
		}

		public String getProcRestrictionRes() {
			return procRestrictionRes;
		}

		public void setProcRestrictionRes(String procRestrictionRes) {
			this.procRestrictionRes = procRestrictionRes;
		}

		public String getVehicleAuth() {
			return vehicleAuth;
		}

		public void setVehicleAuth(String vehicleAuth) {
			this.vehicleAuth = vehicleAuth;
		}

		public String getPublicKeyCVV() {
			return publicKeyCVV;
		}

		public void setPublicKeyCVV(String publicKeyCVV) {
			this.publicKeyCVV = publicKeyCVV;
		}

		public String getReaderTransactionCounter() {
			return readerTransactionCounter;
		}

		public void setReaderTransactionCounter(String readerTransactionCounter) {
			this.readerTransactionCounter = readerTransactionCounter;
		}

		public String getReaderTransactionStatus() {
			return readerTransactionStatus;
		}

		public void setReaderTransactionStatus(String readerTransactionStatus) {
			this.readerTransactionStatus = readerTransactionStatus;
		}

		public String getPayerAddress() {
			return payerAddress;
		}

		public void setPayerAddress(String payerAddress) {
			this.payerAddress = payerAddress;
		}

		public String getIssuerId() {
			return issuerId;
		}

		public void setIssuerId(String issuerId) {
			this.issuerId = issuerId;
		}

		public String getPayerCode() {
			return payerCode;
		}

		public void setPayerCode(String payerCode) {
			this.payerCode = payerCode;
		}

		public String getPayerName() {
			return payerName;
		}

		public void setPayerName(String payerName) {
			this.payerName = payerName;
		}

		public String getPayerType() {
			return payerType;
		}

		public void setPayerType(String payerType) {
			this.payerType = payerType;
		}

		public String getTransactionAmount() {
			return transactionAmount;
		}

		public void setTransactionAmount(String transactionAmount) {
			this.transactionAmount = transactionAmount;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public String getPayeeAddress() {
			return payeeAddress;
		}

		public void setPayeeAddress(String payeeAddress) {
			this.payeeAddress = payeeAddress;
		}

		public String getAcquirerId() {
			return acquirerId;
		}

		public void setAcquirerId(String acquirerId) {
			this.acquirerId = acquirerId;
		}

		public String getPayeeCode() {
			return payeeCode;
		}

		public void setPayeeCode(String payeeCode) {
			this.payeeCode = payeeCode;
		}

		public String getPayeeName() {
			return payeeName;
		}

		public void setPayeeName(String payeeName) {
			this.payeeName = payeeName;
		}

		public String getPayeeType() {
			return payeeType;
		}

		public void setPayeeType(String payeeType) {
			this.payeeType = payeeType;
		}

		public String getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(String responseCode) {
			this.responseCode = responseCode;
		}

		public String getTransactionStatus() {
			return transactionStatus;
		}

		public void setTransactionStatus(String transactionStatus) {
			this.transactionStatus = transactionStatus;
		}

		public String getApprovalNumber() {
			return approvalNumber;
		}

		public void setApprovalNumber(String approvalNumber) {
			this.approvalNumber = approvalNumber;
		}

		public String getPayeeErrorCode() {
			return payeeErrorCode;
		}

		public void setPayeeErrorCode(String payeeErrorCode) {
			this.payeeErrorCode = payeeErrorCode;
		}

		public String getSettledAmount() {
			return settledAmount;
		}

		public void setSettledAmount(String settledAmount) {
			this.settledAmount = settledAmount;
		}

		public String getSettledCurrency() {
			return settledCurrency;
		}

		public void setSettledCurrency(String settledCurrency) {
			this.settledCurrency = settledCurrency;
		}

		public String getAccountType() {
			return accountType;
		}

		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}

		public String getAvailableBalance() {
			return availableBalance;
		}

		public void setAvailableBalance(String availableBalance) {
			this.availableBalance = availableBalance;
		}

		public String getLedgerBalance() {
			return ledgerBalance;
		}

		public void setLedgerBalance(String ledgerBalance) {
			this.ledgerBalance = ledgerBalance;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getInitiatedBy() {
			return initiatedBy;
		}

		public void setInitiatedBy(String initiatedBy) {
			this.initiatedBy = initiatedBy;
		}

		public String getInitiatedTime() {
			return initiatedTime;
		}

		public void setInitiatedTime(String initiatedTime) {
			this.initiatedTime = initiatedTime;
		}

		public String getLastUpdatedBy() {
			return lastUpdatedBy;
		}

		public void setLastUpdatedBy(String lastUpdatedBy) {
			this.lastUpdatedBy = lastUpdatedBy;
		}

		public String getLastUpdatedTime() {
			return lastUpdatedTime;
		}

		public void setLastUpdatedTime(String lastUpdatedTime) {
			this.lastUpdatedTime = lastUpdatedTime;
		}

		public String getVehicleRegistrationNumber() {
			return vehicleRegistrationNumber;
		}

		public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
			this.vehicleRegistrationNumber = vehicleRegistrationNumber;
		}

		public String getVehicleClass() {
			return vehicleClass;
		}

		public void setVehicleClass(String vehicleClass) {
			this.vehicleClass = vehicleClass;
		}

		public String getVehicleType() {
			return vehicleType;
		}

		public void setVehicleType(String vehicleType) {
			this.vehicleType = vehicleType;
		}

		public String getTagStatus() {
			return tagStatus;
		}

		public void setTagStatus(String tagStatus) {
			this.tagStatus = tagStatus;
		}

		public String getTagIssueDate() {
			return tagIssueDate;
		}

		public void setTagIssueDate(String tagIssueDate) {
			this.tagIssueDate = tagIssueDate;
		}

	}

}
