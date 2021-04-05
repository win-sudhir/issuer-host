/**
 * 
 */
package com.winnovature.validation;



public enum ResponseErrorCode {
	WINNBU000("Success"),
	WINNBU001("Request headers are missing."),
	WINNBU002("Invalid Session Id."),
	
	WINNBU003("Invalid transaction date_time for transactionId:"),
	WINNBU004("Invalid transaction amount for transactionId:"),
	
	WINNBU005("File has been already processed."),
	WINNBU006("Invalid file name."),
	
	WINNBU007("request parameter missing."),
	WINNBU008("Recon cycle has been already processed for the date."),
	
	WINNBU009("Kindly check last recon date and cycle, last recon date was "),
	
	WINNBU0010("Failed to insert transactions."),
	WINNBU0011("Failed to reconciliation transactions."),
	WINNBU0012("Failed to insert recon date and cycle."),
	
	
	WINNBU0013("File is empty."),
	WINNBU0014("Failed to delete skipped date and cycle."),
	WINNBU0015("Failed to insert skipped date and cycle."),
	
	WINNBU0016("Invalild request type."),
	//RECONBU0017("Fuel recon file downloaded successfully.");
	WINNBU0017("This date and cycle has already revoked, Please try another"),
	WINNBU0018("Recon file uploaded successfully."),
	WINNBU0019("Failed to upload recon file.");
	
	private String errorMessage;
	
	ResponseErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
