package com.winnovature.validation;

public enum TransactionErrorCode {
	WINTXNBU000("Success"),
	WINTXNBU001("Request parameters are missing."),
	WINTXNBU002("WalletId not found."),
	WINTXNBU003("Can not exceed limit."),
	WINTXNBU004("In-sufficient balance");
	private String errorMessage;
	TransactionErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
}
