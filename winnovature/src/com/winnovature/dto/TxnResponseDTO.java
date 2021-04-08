package com.winnovature.dto;

public class TxnResponseDTO {

	private String tagId;
	private String tId;
	private String vehicleNo;
	private String txnId;
	private String respCode;
	private String respMessage;
	private String txnTime;
	private String amount;
	private String vehicleClass;
	private String tollPlazaId;
	private String tollPlazaName;
	private String cbsId;
	private String cbsResponseTime;
	private String isCommercial;
	private String tollTxnId;
	private String txnType;

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String gettId() {
		return tId;
	}

	public void settId(String tId) {
		this.tId = tId;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
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

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

	public String getTollPlazaId() {
		return tollPlazaId;
	}

	public void setTollPlazaId(String tollPlazaId) {
		this.tollPlazaId = tollPlazaId;
	}

	public String getTollPlazaName() {
		return tollPlazaName;
	}

	public void setTollPlazaName(String tollPlazaName) {
		this.tollPlazaName = tollPlazaName;
	}

	public String getCbsId() {
		return cbsId;
	}

	public void setCbsId(String cbsId) {
		this.cbsId = cbsId;
	}

	public String getCbsResponseTime() {
		return cbsResponseTime;
	}

	public void setCbsResponseTime(String cbsResponseTime) {
		this.cbsResponseTime = cbsResponseTime;
	}

	public String getIsCommercial() {
		return isCommercial;
	}

	public void setIsCommercial(String isCommercial) {
		this.isCommercial = isCommercial;
	}

	public String getTollTxnId() {
		return tollTxnId;
	}

	public void setTollTxnId(String tollTxnId) {
		this.tollTxnId = tollTxnId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	@Override
	public String toString() {
		return "TxnResponseDTO [tagId=" + tagId + ", tId=" + tId + ", vehicleNo=" + vehicleNo + ", txnId=" + txnId
				+ ", respCode=" + respCode + ", respMessage=" + respMessage + ", txnTime=" + txnTime + ", amount="
				+ amount + ", vehicleClass=" + vehicleClass + ", tollPlazaId=" + tollPlazaId + ", tollPlazaName="
				+ tollPlazaName + ", cbsId=" + cbsId + ", cbsResponseTime=" + cbsResponseTime + ", isCommercial="
				+ isCommercial + ", tollTxnId=" + tollTxnId + ", txnType=" + txnType + "]";
	}

}
