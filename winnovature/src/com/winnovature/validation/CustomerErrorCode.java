package com.winnovature.validation;

public enum CustomerErrorCode {
	WINNBU000("Customer added successfully."),
	WINNBU001("Request parameters are missing."),
//	WINNBU002("Request headers are missing."),
//	WINNBU003("Request headers are missing."),
//	WINNBU004("Request headers are missing."),
//	WINNBU005("Request headers are missing."),
		
	WINNBU0019("EmailId is already present in customer."),
	WINNBU0020("Mobile number is already present in customer."),
	WINNBU0021("Address proof number is already present in customer."),
	WINNBU0022("Pan number is already present in customer."),
	WINNBU0023("Vehicle number is already present."),
	WINNBU0030("Failed.");
	
	private String errorMessage;
	
	CustomerErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
