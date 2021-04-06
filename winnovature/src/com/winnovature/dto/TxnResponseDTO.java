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

	@Override
	public String toString() {
		return "TxnResponseDTO [tagId=" + tagId + ", tId=" + tId + ", vehicleNo=" + vehicleNo + ", txnId=" + txnId
				+ ", respCode=" + respCode + ", respMessage=" + respMessage + ", txnTime=" + txnTime + ", amount="
				+ amount + "]";
	}

}
