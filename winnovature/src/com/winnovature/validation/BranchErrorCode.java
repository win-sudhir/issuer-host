package com.winnovature.validation;

public enum BranchErrorCode {
	WINNABU000("Branch added successfully."), 
	WINNABU001("Request parameters are missing."),
	WINNABU002("Branch could not added, Please try later."), 
	WINNABU003("Branch could not delete, Please try again."),
	WINNABU004("Branch could not reject, Please try again."), 
	WINNABU005("Branch could not approve, Please try again."),

	WINNABU006("Branch deleted successfully."), 
	WINNABU007("Branch rejected successfully."),
	WINNABU008("Branch approved successfully."),

	WINNABU009("Branch could not updated, Please try again."), 
	WINNABU0010("Branch updated successfully."),

	// maker
	WINNABU0011("Branch creation request initiated, Please wait for approval."),
	WINNABU0012("Branch updation request initiated, Please wait for approval."),
	WINNABU0013("Branch approval request initiated, Please wait for approval."),
	WINNABU0014("Branch rejection request initiated, Please wait for approval."),
	WINNABU0015("Branch deletion request initiated, Please wait for approval."),

	WINNABU0019("EmailId is already present."), 
	WINNABU0020("Mobile number is already present."),
	WINNABU0029("Invalild request type."), 
	WINNABU0030("Failed.");

	private String errorMessage;

	BranchErrorCode(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
