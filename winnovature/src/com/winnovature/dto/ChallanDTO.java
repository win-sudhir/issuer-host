/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;


public class ChallanDTO  implements Serializable {
	private static final long serialVersionUID = 1L;
	private String bankName;
	private String challanId;
	private String chassisNumber;
	private String createdDate;
	private String engineNumber;
	private String vehicleNumber;
	private String tid;
	private String tagId;
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getChallanId() {
		return challanId;
	}
	public void setChallanId(String challanId) {
		this.challanId = challanId;
	}
	public String getChassisNumber() {
		return chassisNumber;
	}
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getEngineNumber() {
		return engineNumber;
	}
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
}