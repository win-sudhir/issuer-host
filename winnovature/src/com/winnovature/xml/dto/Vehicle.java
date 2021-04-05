/**
 * 
 */
package com.winnovature.xml.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class Vehicle {
	private String errCode;
	
	public String getErrCode() {
		return errCode;
	}

	@XmlAttribute(name="errCode")
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	private VehicleDetails vehicleDetails;
	
	
	public VehicleDetails getVehicleDetails() {
		return vehicleDetails;
	}
	@XmlElement(name="VehicleDetails")
	public void setVehicleDetails(VehicleDetails vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}
	
}
