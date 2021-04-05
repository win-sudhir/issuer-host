package com.winnovature.dto;

/**
 * 
 * @author Sudhir Khobragade
 *
 */

public class VehicleDetailsDTO {
	private String vehicleNumber;
	private String oldVehicleNumber;
	private String customerId;
	private String isCommercial;
	private String tagId;
	private String oldTagId;
	private String tid;
	private String barcodeData;
	private String tagClassId;
	private String vehicleClassId;
	private String vehicleId;
	private Object data;

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getOldVehicleNumber() {
		return oldVehicleNumber;
	}

	public void setOldVehicleNumber(String oldVehicleNumber) {
		this.oldVehicleNumber = oldVehicleNumber;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getIsCommercial() {
		return isCommercial;
	}

	public void setIsCommercial(String isCommercial) {
		this.isCommercial = isCommercial;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getOldTagId() {
		return oldTagId;
	}

	public void setOldTagId(String oldTagId) {
		this.oldTagId = oldTagId;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getBarcodeData() {
		return barcodeData;
	}

	public void setBarcodeData(String barcodeData) {
		this.barcodeData = barcodeData;
	}

	public String getTagClassId() {
		return tagClassId;
	}

	public void setTagClassId(String tagClassId) {
		this.tagClassId = tagClassId;
	}

	public String getVehicleClassId() {
		return vehicleClassId;
	}

	public void setVehicleClassId(String vehicleClassId) {
		this.vehicleClassId = vehicleClassId;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "VehicleDetailsDTO [vehicleNumber=" + vehicleNumber + ", oldVehicleNumber=" + oldVehicleNumber
				+ ", customerId=" + customerId + ", isCommercial=" + isCommercial + ", tagId=" + tagId + ", oldTagId="
				+ oldTagId + ", tid=" + tid + ", barcodeData=" + barcodeData + ", tagClassId=" + tagClassId
				+ ", vehicleClassId=" + vehicleClassId + ", vehicleId=" + vehicleId + ", data=" + data + "]";
	}

}
