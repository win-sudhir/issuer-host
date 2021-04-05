package com.winnovature.dto;

public class AddressDTO {
	private String userId;
	private String resiAddress1;
	private String resiAddress2;
	private String resiPin;
	private String resiCity;
	private String resiState;
	private String businessAdd1;
	private String businessAdd2;
	private String businessPin;
	private String businessCity;
	private String businessState;
	public AddressDTO() {}
	public AddressDTO(String userId, String resiAddress1, String resiAddress2, String resiPin, String resiCity,
			String resiState, String businessAdd1, String businessAdd2, String businessPin, String businessCity,
			String businessState) {
		super();
		this.userId = userId;
		this.resiAddress1 = resiAddress1;
		this.resiAddress2 = resiAddress2;
		this.resiPin = resiPin;
		this.resiCity = resiCity;
		this.resiState = resiState;
		this.businessAdd1 = businessAdd1;
		this.businessAdd2 = businessAdd2;
		this.businessPin = businessPin;
		this.businessCity = businessCity;
		this.businessState = businessState;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getResiAddress1() {
		return resiAddress1;
	}
	public void setResiAddress1(String resiAddress1) {
		this.resiAddress1 = resiAddress1;
	}
	public String getResiAddress2() {
		return resiAddress2;
	}
	public void setResiAddress2(String resiAddress2) {
		this.resiAddress2 = resiAddress2;
	}
	public String getResiPin() {
		return resiPin;
	}
	public void setResiPin(String resiPin) {
		this.resiPin = resiPin;
	}
	public String getResiCity() {
		return resiCity;
	}
	public void setResiCity(String resiCity) {
		this.resiCity = resiCity;
	}
	public String getResiState() {
		return resiState;
	}
	public void setResiState(String resiState) {
		this.resiState = resiState;
	}
	public String getBusinessAdd1() {
		return businessAdd1;
	}
	public void setBusinessAdd1(String businessAdd1) {
		this.businessAdd1 = businessAdd1;
	}
	public String getBusinessAdd2() {
		return businessAdd2;
	}
	public void setBusinessAdd2(String businessAdd2) {
		this.businessAdd2 = businessAdd2;
	}
	public String getBusinessPin() {
		return businessPin;
	}
	public void setBusinessPin(String businessPin) {
		this.businessPin = businessPin;
	}
	public String getBusinessCity() {
		return businessCity;
	}
	public void setBusinessCity(String businessCity) {
		this.businessCity = businessCity;
	}
	public String getBusinessState() {
		return businessState;
	}
	public void setBusinessState(String businessState) {
		this.businessState = businessState;
	}
	
}
