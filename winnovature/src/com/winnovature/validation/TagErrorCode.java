/**
 * 
 */
package com.winnovature.validation;


public enum TagErrorCode {
	TAGBU000("Success"),
	TAGBU001("No Data Found for the Entered Tag Id/TID/Vehicle Number and Exception Code "),
	TAGBU002("Transaction is in good faith."),
	TAGBU003("Dispute raised successfully, your dispute reference number is: "),
	
	TAGBU0020("File is empty."),
	TAGBU0021("Request parameters are missing(null or blank)."),
	TAGBU0022("File has been already processed."),
	TAGBU0023("At least one parameter is required."),
	TAGBU0024("Exception code is required."),
	TAGBU0025("Tag is already in exception."),
	TAGBU0026("Response not received from NPCI."),
	TAGBU0027("TxnRespTagResult not received from NPCI."),
	TAGBU0028("Unable to update tag exception details."),
	TAGBU0029("Status not found."),
	TAGBU0030("Only ADD and REMOVE operations are allowed."),
	TAGBU0031("Tag added in exception successfully."),
	TAGBU0032("Tag removed from exception successfully."),
	TAGBU0033("Response from NPCI is failure."),
	TAGBU0034("Response from NPCI is null or empty."),
	TAGBU0035("Error in parsing XML."),
	TAGBU0036("Technical error, Please check later."),
	TAGBU0037("The details you are fetching is not found in NPCI."),
	TAGBU0038("RespMngException result is FAILURE."),
	TAGBU0039("Tag result is FAILURE and ERRORCODE is "),
	TAGBU0040("RespDetails result is FAILURE."),
	TAGBU0041("Request details not found."),
	
	//tag reissuance
	TAGBU0042("Request type not found."),
	TAGBU0043("Vehicle number not found."),
	TAGBU0044("Tag reissued successfully."),
	TAGBU0045("Tag can not be reissued, Please try later.");
	private String errorMessage;
	
	TagErrorCode(String errorMessage) {
		this.errorMessage=errorMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
}
