/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;
import java.util.List;

public class DisputeUploadFileDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String requestTime;
	private String requestBy;
	private String requestType;
	private String disputeDate;
	private String fileName;

	private List<DisputeTransactionInfoDTO> transactionInfoDTO;

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

	public String getDisputeDate() {
		return disputeDate;
	}

	public void setDisputeDate(String disputeDate) {
		this.disputeDate = disputeDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<DisputeTransactionInfoDTO> getTransactionInfoDTO() {
		return transactionInfoDTO;
	}

	public void setTransactionInfoDTO(List<DisputeTransactionInfoDTO> transactionInfoDTO) {
		this.transactionInfoDTO = transactionInfoDTO;
	}

	public static class DisputeTransactionInfoDTO implements Serializable {

		private static final long serialVersionUID = 1L;

		private String reportDate;
		private String disputeRaiseDate;
		private String disputeRaisedSettlementDate;
		private String caseNumber;
		private String functionCode;
		private String functionCodeAndDescription;
		private String transactionSequenceNumber;
		private String tagId;
		private String tid;
		private String transactionDateAndTime;
		private String readerReadDateAndTime;
		private String transactionSettlementDate;
		private String transactionAmount;
		private String settlementAmount;
		private String settlementCurrencyCode;
		private String note;
		private String transactionId;
		private String transactionType;
		private String merchantId;
		private String laneId;
		private String merchantType;
		private String subMerchantType;
		private String transactionStatus;
		private String tagStatus;
		private String avc;
		private String wim;
		private String originatorPoint;
		private String accquirerId;
		private String transactionOriginatorInstitutionPid;
		private String acquirerNameAndCountry;
		private String iin;
		private String transactionDestinationInstitutionPid;
		private String issuerNameAndCountry;
		private String vehicleRegistrationNumber;
		private String vehicleClass;
		private String vehicleType;
		private String financialNonFinancialIndicator;
		private String disputeReasonCode;
		private String disputeReasonCodeDescription;
		private String disputedAmount;

		private String fullPartialIndicator;
		private String memberMessageText;
		private String documentIndicator;
		private String documentAttachedDate;
		private String deadlineDate;
		private String daysToAct;
		private String directionOfDispute;

		public String getReportDate() {
			return reportDate;
		}

		public void setReportDate(String reportDate) {
			this.reportDate = reportDate;
		}

		public String getDisputeRaiseDate() {
			return disputeRaiseDate;
		}

		public void setDisputeRaiseDate(String disputeRaiseDate) {
			this.disputeRaiseDate = disputeRaiseDate;
		}

		public String getDisputeRaisedSettlementDate() {
			return disputeRaisedSettlementDate;
		}

		public void setDisputeRaisedSettlementDate(String disputeRaisedSettlementDate) {
			this.disputeRaisedSettlementDate = disputeRaisedSettlementDate;
		}

		public String getCaseNumber() {
			return caseNumber;
		}

		public void setCaseNumber(String caseNumber) {
			this.caseNumber = caseNumber;
		}

		public String getFunctionCode() {
			return functionCode;
		}

		public void setFunctionCode(String functionCode) {
			this.functionCode = functionCode;
		}

		public String getFunctionCodeAndDescription() {
			return functionCodeAndDescription;
		}

		public void setFunctionCodeAndDescription(String functionCodeAndDescription) {
			this.functionCodeAndDescription = functionCodeAndDescription;
		}

		public String getTransactionSequenceNumber() {
			return transactionSequenceNumber;
		}

		public void setTransactionSequenceNumber(String transactionSequenceNumber) {
			this.transactionSequenceNumber = transactionSequenceNumber;
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

		public String getTransactionDateAndTime() {
			return transactionDateAndTime;
		}

		public void setTransactionDateAndTime(String transactionDateAndTime) {
			this.transactionDateAndTime = transactionDateAndTime;
		}

		public String getReaderReadDateAndTime() {
			return readerReadDateAndTime;
		}

		public void setReaderReadDateAndTime(String readerReadDateAndTime) {
			this.readerReadDateAndTime = readerReadDateAndTime;
		}

		public String getTransactionSettlementDate() {
			return transactionSettlementDate;
		}

		public void setTransactionSettlementDate(String transactionSettlementDate) {
			this.transactionSettlementDate = transactionSettlementDate;
		}

		public String getTransactionAmount() {
			return transactionAmount;
		}

		public void setTransactionAmount(String transactionAmount) {
			this.transactionAmount = transactionAmount;
		}

		public String getSettlementAmount() {
			return settlementAmount;
		}

		public void setSettlementAmount(String settlementAmount) {
			this.settlementAmount = settlementAmount;
		}

		public String getSettlementCurrencyCode() {
			return settlementCurrencyCode;
		}

		public void setSettlementCurrencyCode(String settlementCurrencyCode) {
			this.settlementCurrencyCode = settlementCurrencyCode;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getLaneId() {
			return laneId;
		}

		public void setLaneId(String laneId) {
			this.laneId = laneId;
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

		public String getTransactionStatus() {
			return transactionStatus;
		}

		public void setTransactionStatus(String transactionStatus) {
			this.transactionStatus = transactionStatus;
		}

		public String getTagStatus() {
			return tagStatus;
		}

		public void setTagStatus(String tagStatus) {
			this.tagStatus = tagStatus;
		}

		public String getAvc() {
			return avc;
		}

		public void setAvc(String avc) {
			this.avc = avc;
		}

		public String getWim() {
			return wim;
		}

		public void setWim(String wim) {
			this.wim = wim;
		}

		public String getOriginatorPoint() {
			return originatorPoint;
		}

		public void setOriginatorPoint(String originatorPoint) {
			this.originatorPoint = originatorPoint;
		}

		public String getAccquirerId() {
			return accquirerId;
		}

		public void setAccquirerId(String accquirerId) {
			this.accquirerId = accquirerId;
		}

		public String getTransactionOriginatorInstitutionPid() {
			return transactionOriginatorInstitutionPid;
		}

		public void setTransactionOriginatorInstitutionPid(String transactionOriginatorInstitutionPid) {
			this.transactionOriginatorInstitutionPid = transactionOriginatorInstitutionPid;
		}

		public String getAcquirerNameAndCountry() {
			return acquirerNameAndCountry;
		}

		public void setAcquirerNameAndCountry(String acquirerNameAndCountry) {
			this.acquirerNameAndCountry = acquirerNameAndCountry;
		}

		public String getIin() {
			return iin;
		}

		public void setIin(String iin) {
			this.iin = iin;
		}

		public String getTransactionDestinationInstitutionPid() {
			return transactionDestinationInstitutionPid;
		}

		public void setTransactionDestinationInstitutionPid(String transactionDestinationInstitutionPid) {
			this.transactionDestinationInstitutionPid = transactionDestinationInstitutionPid;
		}

		public String getIssuerNameAndCountry() {
			return issuerNameAndCountry;
		}

		public void setIssuerNameAndCountry(String issuerNameAndCountry) {
			this.issuerNameAndCountry = issuerNameAndCountry;
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

		public String getFinancialNonFinancialIndicator() {
			return financialNonFinancialIndicator;
		}

		public void setFinancialNonFinancialIndicator(String financialNonFinancialIndicator) {
			this.financialNonFinancialIndicator = financialNonFinancialIndicator;
		}

		public String getDisputeReasonCode() {
			return disputeReasonCode;
		}

		public void setDisputeReasonCode(String disputeReasonCode) {
			this.disputeReasonCode = disputeReasonCode;
		}

		public String getDisputeReasonCodeDescription() {
			return disputeReasonCodeDescription;
		}

		public void setDisputeReasonCodeDescription(String disputeReasonCodeDescription) {
			this.disputeReasonCodeDescription = disputeReasonCodeDescription;
		}

		public String getDisputedAmount() {
			return disputedAmount;
		}

		public void setDisputedAmount(String disputedAmount) {
			this.disputedAmount = disputedAmount;
		}

		public String getFullPartialIndicator() {
			return fullPartialIndicator;
		}

		public void setFullPartialIndicator(String fullPartialIndicator) {
			this.fullPartialIndicator = fullPartialIndicator;
		}

		public String getMemberMessageText() {
			return memberMessageText;
		}

		public void setMemberMessageText(String memberMessageText) {
			this.memberMessageText = memberMessageText;
		}

		public String getDocumentIndicator() {
			return documentIndicator;
		}

		public void setDocumentIndicator(String documentIndicator) {
			this.documentIndicator = documentIndicator;
		}

		public String getDocumentAttachedDate() {
			return documentAttachedDate;
		}

		public void setDocumentAttachedDate(String documentAttachedDate) {
			this.documentAttachedDate = documentAttachedDate;
		}

		public String getDeadlineDate() {
			return deadlineDate;
		}

		public void setDeadlineDate(String deadlineDate) {
			this.deadlineDate = deadlineDate;
		}

		public String getDaysToAct() {
			return daysToAct;
		}

		public void setDaysToAct(String daysToAct) {
			this.daysToAct = daysToAct;
		}

		public String getDirectionOfDispute() {
			return directionOfDispute;
		}

		public void setDirectionOfDispute(String directionOfDispute) {
			this.directionOfDispute = directionOfDispute;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("DisputeTransactionInfoDTO [reportDate=");
			builder.append(reportDate);
			builder.append(", disputeRaiseDate=");
			builder.append(disputeRaiseDate);
			builder.append(", disputeRaisedSettlementDate=");
			builder.append(disputeRaisedSettlementDate);
			builder.append(", caseNumber=");
			builder.append(caseNumber);
			builder.append(", functionCode=");
			builder.append(functionCode);
			builder.append(", functionCodeAndDescription=");
			builder.append(functionCodeAndDescription);
			builder.append(", transactionSequenceNumber=");
			builder.append(transactionSequenceNumber);
			builder.append(", tagId=");
			builder.append(tagId);
			builder.append(", tid=");
			builder.append(tid);
			builder.append(", transactionDateAndTime=");
			builder.append(transactionDateAndTime);
			builder.append(", readerReadDateAndTime=");
			builder.append(readerReadDateAndTime);
			builder.append(", transactionSettlementDate=");
			builder.append(transactionSettlementDate);
			builder.append(", transactionAmount=");
			builder.append(transactionAmount);
			builder.append(", settlementAmount=");
			builder.append(settlementAmount);
			builder.append(", settlementCurrencyCode=");
			builder.append(settlementCurrencyCode);
			builder.append(", note=");
			builder.append(note);
			builder.append(", transactionId=");
			builder.append(transactionId);
			builder.append(", transactionType=");
			builder.append(transactionType);
			builder.append(", merchantId=");
			builder.append(merchantId);
			builder.append(", laneId=");
			builder.append(laneId);
			builder.append(", merchantType=");
			builder.append(merchantType);
			builder.append(", subMerchantType=");
			builder.append(subMerchantType);
			builder.append(", transactionStatus=");
			builder.append(transactionStatus);
			builder.append(", tagStatus=");
			builder.append(tagStatus);
			builder.append(", avc=");
			builder.append(avc);
			builder.append(", wim=");
			builder.append(wim);
			builder.append(", originatorPoint=");
			builder.append(originatorPoint);
			builder.append(", accquirerId=");
			builder.append(accquirerId);
			builder.append(", transactionOriginatorInstitutionPid=");
			builder.append(transactionOriginatorInstitutionPid);
			builder.append(", acquirerNameAndCountry=");
			builder.append(acquirerNameAndCountry);
			builder.append(", iin=");
			builder.append(iin);
			builder.append(", transactionDestinationInstitutionPid=");
			builder.append(transactionDestinationInstitutionPid);
			builder.append(", issuerNameAndCountry=");
			builder.append(issuerNameAndCountry);
			builder.append(", vehicleRegistrationNumber=");
			builder.append(vehicleRegistrationNumber);
			builder.append(", vehicleClass=");
			builder.append(vehicleClass);
			builder.append(", vehicleType=");
			builder.append(vehicleType);
			builder.append(", financialNonFinancialIndicator=");
			builder.append(financialNonFinancialIndicator);
			builder.append(", disputeReasonCode=");
			builder.append(disputeReasonCode);
			builder.append(", disputeReasonCodeDescription=");
			builder.append(disputeReasonCodeDescription);
			builder.append(", disputedAmount=");
			builder.append(disputedAmount);
			builder.append(", fullPartialIndicator=");
			builder.append(fullPartialIndicator);
			builder.append(", memberMessageText=");
			builder.append(memberMessageText);
			builder.append(", documentIndicator=");
			builder.append(documentIndicator);
			builder.append(", documentAttachedDate=");
			builder.append(documentAttachedDate);
			builder.append(", deadlineDate=");
			builder.append(deadlineDate);
			builder.append(", daysToAct=");
			builder.append(daysToAct);
			builder.append(", directionOfDispute=");
			builder.append(directionOfDispute);
			builder.append("]");
			return builder.toString();
		}

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DisputeUploadFileDTO [requestTime=");
		builder.append(requestTime);
		builder.append(", requestBy=");
		builder.append(requestBy);
		builder.append(", requestType=");
		builder.append(requestType);
		builder.append(", disputeDate=");
		builder.append(disputeDate);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", transactionInfoDTO=");
		builder.append(transactionInfoDTO);
		builder.append("]");
		return builder.toString();
	}

}
