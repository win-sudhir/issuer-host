/**
 * 
 */
package com.winnovature.validation;

import java.sql.Connection;

import com.winnovature.dao.CheckSession;
import com.winnovature.dto.ResponseDTO;




public class SessionValidation {
	public static ResponseDTO validateSession(String userId, String authorization, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO(); 
		responseDTO = isValidSession(userId, authorization, conn);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	private static ResponseDTO isValidSession(String userId, String authorization, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO(); 
		if(userId==null || authorization==null || userId=="" || authorization==""){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ResponseErrorCode.WINNBU001.getErrorMessage());
			responseDTO.setErrorCode(ResponseErrorCode.WINNBU001.name());
			responseDTO.setFlag(ResponseDTO.failure);
			return responseDTO;
		}
		
		if(!CheckSession.isValidSession(userId, authorization, conn)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ResponseErrorCode.WINNBU002.getErrorMessage());
			responseDTO.setErrorCode(ResponseErrorCode.WINNBU002.name());
			responseDTO.setFlag(ResponseDTO.failure);
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	public static void main(String[] args) {
		String userid = null;
		if(userid==null){
			System.out.println("null value");
		}
	}
}
