package com.winnovature.validation;

public enum VehicleErrorCode {
	WINNVEHBU000("Vehicle updated successfully."),
	WINNVEHBU001("Request parameters are missing."),
	WINNVEHBU002("Both vehicle numbers and commercial values are equals."),
	WINNVEHBU003("Vehicle could not update, Please try again."),
	WINNVEHBU004("NCPI has not responding."),
	WINNVEHBU005("Vehicle number already present, Please try another."),
	WINNVEHBU006("NCPI got error code, "),
	/*WINNVEHBU005("Vehicle could not approve, Please try again."),
	
	WINNVEHBU006("Vehicle deleted successfully."),
	WINNVEHBU007("Vehicle rejected successfully."),*/
	WINNVEHBU008("Vehicle approved successfully."),

	WINNVEHBU0030("Failed.");
	
	private String errorMessage;
	
	VehicleErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
