package com.winnovature.dto;

public class TagSinglePageDTO {

	private String tagId;
	private String tId;
	private String barCode;
	private String tagClassId;
	private String signedData;
	private String userMemory;
	private String supplierId;
	private String createdBy;

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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getTagClassId() {
		return tagClassId;
	}

	public void setTagClassId(String tagClassId) {
		this.tagClassId = tagClassId;
	}

	public String getSignedData() {
		return signedData;
	}

	public void setSignedData(String signedData) {
		this.signedData = signedData;
	}

	public String getUserMemory() {
		return userMemory;
	}

	public void setUserMemory(String userMemory) {
		this.userMemory = userMemory;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "TagSinglePageDTO [tagId=" + tagId + ", tId=" + tId + ", barCode=" + barCode + ", tagClassId="
				+ tagClassId + ", signedData=" + signedData + ", userMemory=" + userMemory + ", supplierId="
				+ supplierId + ", createdBy=" + createdBy + "]";
	}

}
