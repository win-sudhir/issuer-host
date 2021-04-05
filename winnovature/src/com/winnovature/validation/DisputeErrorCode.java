/**
 * 
 */
package com.winnovature.validation;


public enum DisputeErrorCode {
	DISPUTEBU000("Success"),
	DISPUTEBU001("Raised amount is greater than transaction amount."),
	DISPUTEBU002("Transaction is in good faith."),
	DISPUTEBU003("Dispute raised successfully, your dispute reference number is: "),
	DISPUTEBU004("Parameters are missing or null/empty"),
	DISPUTEBU005("Dispute has been already raised on this trasactionId."),
	DISPUTEBU006("Transaction is in good faith."),
	DISPUTEBU007("Dispute can not be raised, Please try again."),
	DISPUTEBU008("Transaction Id not found in request pay."),
	DISPUTEBU009("Dispute can not be approved, Please try again."),
	DISPUTEBU0010("Dispute approved successfully."),
	
	DISPUTEBU0011("Dispute can not be rejected, Please try again."),
	DISPUTEBU0012("Dispute rejected successfully."),
	
	DISPUTEBU0013("Dispute can not be updated, Please try again."),
	DISPUTEBU0014("Dispute updated successfully."),
	
	DISPUTEBU0015("Transaction id not found to close, Please try again."),
	DISPUTEBU0016("Dispute closed successfully."),
	DISPUTEBU0017("Dispute can not close."),
	//akki below
	DISPUTEBU0018("Invalid transaction amount for transactionId:"),
	DISPUTEBU0019("Invalid settlement amount for transactionId:"),
	DISPUTEBU0020("Invalid disputed amount for transactionId:"),
	DISPUTEBU0021("File is empty."),
	DISPUTEBU0022("Invalid transaction date_time for transactionId:"),
	//DISPUTEBU0023("request parameter missing."),
	DISPUTEBU0024("Failed to upload all dispute file."),
	DISPUTEBU0025("Invalild request type."),
	DISPUTEBU0026("Dispute file uploaded successfully."),
	DISPUTEBU0027("Dispute can not be raised on 0 amount."),
	DISPUTEBU0028("Records not found."),
	DISPUTEBU0030("");
	
	private String errorMessage;
	
	DisputeErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
