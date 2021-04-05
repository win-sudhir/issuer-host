/**
 * 
 */
package com.winnovature.validation;


public enum ReconErrorCode {
	RECONBU000("Success"),
	RECONBU001("Request headers are missing."),
	RECONBU002("Invalid Session Id."),
	
	RECONBU003("Invalid transaction date_time for transactionId:"),
	RECONBU004("Invalid transaction amount for transactionId:"),
	
	RECONBU005("File has been already processed."),
	RECONBU006("Invalid file name."),
	
	RECONBU007("request parameter missing."),
	RECONBU008("Recon cycle has been already processed for the date."),
	
	RECONBU009("Kindly check last recon date and cycle, last recon date was "),
	
	RECONBU0010("Failed to insert transactions."),
	RECONBU0011("Failed to reconciliation transactions."),
	RECONBU0012("Failed to insert recon date and cycle."),
	
	
	RECONBU0013("File is empty."),
	RECONBU0014("Failed to delete skipped date and cycle."),
	RECONBU0015("Failed to insert skipped date and cycle."),
	
	RECONBU0016("Invalild request type."),
	//RECONBU0017("Fuel recon file downloaded successfully.");
	RECONBU0017("This date and cycle has already revoked, Please try another"),
	RECONBU0018("Recon file uploaded successfully."),
	RECONBU0019("Failed to upload recon file.");
	
	private String errorMessage;
	
	ReconErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
