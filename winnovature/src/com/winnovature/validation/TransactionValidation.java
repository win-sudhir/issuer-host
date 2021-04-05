package com.winnovature.validation;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.winnovature.dao.CustomerDAO;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.service.TransactionService;

public class TransactionValidation {
	static Logger log = Logger.getLogger(TransactionValidation.class.getName());
	
	public static ResponseDTO validateTransaction(Connection conn, TransactionDTO transactionDTO, String userId) {
		//transactionDTO.setSourceChannel(TransactionDTO.SOURCECHANNEL);
		//transactionDTO.setSourceChannelIP(TransactionDTO.SOURCECHANNELIP);
		ResponseDTO responseDTO = validateTransactionParams(transactionDTO, userId);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		//responseDTO = validateLimit(transactionDTO, userId);
		if(validateLimit(transactionDTO, userId,conn)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TransactionErrorCode.WINTXNBU003.getErrorMessage());
			responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU003.name());
			return responseDTO;
		}
		return responseDTO;
	}
	
	private static ResponseDTO validateTransactionParams(TransactionDTO transactionDTO, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (transactionDTO.getWalletId() == null || transactionDTO.getPayMode() == null ||
			transactionDTO.getTxnAmount() == null || transactionDTO.getTxnType() == null ||
			transactionDTO.getRemarks() == null || transactionDTO.getPartnerId() == null ||
			transactionDTO.getPartnerRefId() == null || userId == null ||
			transactionDTO.getWalletId().isEmpty() || transactionDTO.getPayMode().isEmpty() ||
			transactionDTO.getTxnAmount().isEmpty() || transactionDTO.getTxnType().isEmpty() ||
			transactionDTO.getRemarks().isEmpty() || transactionDTO.getPartnerId().isEmpty() ||
			transactionDTO.getPartnerRefId().isEmpty() || userId.isEmpty()) {
			
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TransactionErrorCode.WINTXNBU001.getErrorMessage());
			responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU001.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		
		return responseDTO;
	}
	
	private static boolean validateLimit(TransactionDTO transactionDTO, String userId, Connection conn) {
		CustomerDTO customerDTO = new CustomerDAO().geCustomersWalletInfo(conn, transactionDTO.getWalletId());
		if(transactionDTO.getTxnType().equalsIgnoreCase("CREDIT")) {
			//CustomerDTO customerDTO = new CustomerDAO_().geCustomersWalletInfo(conn, transactionDTO.getWalletId());
			double finalbal = customerDTO.getCurrentBalance()+Double.valueOf(transactionDTO.getTxnAmount());
			if((finalbal>customerDTO.getMaxBalance())) {
				return true;
			}
		}
		if(transactionDTO.getTxnType().equalsIgnoreCase("DEBIT")) {
			//CustomerDTO customerDTO = new CustomerDAO_().geCustomersWalletInfo(conn, transactionDTO.getWalletId());
			double finalbal = customerDTO.getCurrentBalance()-Double.valueOf(transactionDTO.getTxnAmount());
			if(finalbal<0) {
				return true;
			}
		}
		//double txnAmount = Double.valueOf(transactionDTO.getSecurityDeposit())+Double.valueOf(transactionDTO.getTxnAmount());
//		String smsContent = "Your wallet has been credited by "+transactionDTO.getTxnAmount()+ "Rs./-";
//		TransactionService.sendSMS(customerDTO.getContactNumber(), smsContent);
		
		return false;
	}
}
