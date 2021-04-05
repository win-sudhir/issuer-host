package com.winnovature.validation;

public enum UserErrorCode {
	WINNUBU000("User added successfully."),
	WINNUBU001("Request parameters are missing."),
	WINNUBU002("User could not added, Please try later."),
	WINNUBU003("User could not delete, Please try again."),
	WINNUBU004("User could not reject, Please try again."),
	WINNUBU005("User could not approve, Please try again."),
	
	WINNUBU006("User deleted successfully."),
	WINNUBU007("User rejected successfully."),
	WINNUBU008("User approved successfully."),
//	WINNBU002("Request headers are missing."),
//	WINNBU003("Request headers are missing."),
//	WINNBU004("Request headers are missing."),
//	WINNBU005("Request headers are missing."),

	WINNUBU009("User could not updated, Please try again."),
	WINNUBU0010("User updated successfully."),
	
	WINNUBU0019("EmailId is already present."),
	WINNUBU0020("Mobile number is already present."),
	WINNUBU0029("Invalild request type."),
	WINNUBU0030("Failed.");
	
	private String errorMessage;
	
	UserErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
