package com.winnovature.dto;

public class WalletResponse {
	
	private int httpCode;
	private String response;
	
	public int getHttpCode() {
		return httpCode;
	}	
	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String respXML) {
		this.response = respXML;
	}
	
	

}
