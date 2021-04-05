package com.winnovature.validation;

public enum AgentErrorCode {
	WINNABU000("Agent added successfully."),
	WINNABU001("Request parameters are missing."),
	WINNABU002("Agent could not added, Please try later."),
	WINNABU003("Agent could not delete, Please try again."),
	WINNABU004("Agent could not reject, Please try again."),
	WINNABU005("Agent could not approve, Please try again."),
	
	WINNABU006("Agent deleted successfully."),
	WINNABU007("Agent rejected successfully."),
	WINNABU008("Agent approved successfully."),


	WINNABU009("Agent could not updated, Please try again."),
	WINNABU0010("Agent updated successfully."),
	
	//maker
	WINNABU0011("Agent creation request initiated, Please wait for approval."),
	WINNABU0012("Agent updation request initiated, Please wait for approval."),
	WINNABU0013("Agent approval request initiated, Please wait for approval."),
	WINNABU0014("Agent rejection request initiated, Please wait for approval."),
	WINNABU0015("Agent deletion request initiated, Please wait for approval."),
	
	WINNABU0019("EmailId is already present."),
	WINNABU0020("Mobile number is already present."),
	WINNABU0029("Invalild request type."),
	WINNABU0030("Failed.");
	
	private String errorMessage;
	
	AgentErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
