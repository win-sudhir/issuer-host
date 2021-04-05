/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;


public class ChallanDTO  implements Serializable {
	private static final long serialVersionUID = 1L;
	private String BankName;
	private String ChallanId;
	private String ChassisNumber;
	private String CreatedDate;
	private String EngineNumber;
	/*private String TID;
	private String TagId;
	private String VehicleNumber;*/
	public String getBankName() {
		return BankName;
	}
	public void setBankName(String bankName) {
		BankName = bankName;
	}
	public String getChallanId() {
		return ChallanId;
	}
	public void setChallanId(String challanId) {
		ChallanId = challanId;
	}
	public String getChassisNumber() {
		return ChassisNumber;
	}
	public void setChassisNumber(String chassisNumber) {
		ChassisNumber = chassisNumber;
	}
	public String getCreatedDate() {
		return CreatedDate;
	}
	public void setCreatedDate(String createdDate) {
		CreatedDate = createdDate;
	}
	public String getEngineNumber() {
		return EngineNumber;
	}
	public void setEngineNumber(String engineNumber) {
		EngineNumber = engineNumber;
	}
	
}
