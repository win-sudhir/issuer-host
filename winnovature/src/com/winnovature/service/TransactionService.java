package com.winnovature.service;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.winnovature.dao.CustomerDAO;
import com.winnovature.dao.WalletTransactionDAO;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.utils.PropertyReader;
import com.winnovature.utils.Server2ServerCall;
import com.winnovature.validation.TransactionErrorCode;

public class TransactionService {
	static Logger log = Logger.getLogger(TransactionService.class.getName());
	/*
	public static ResponseDTO addCustomer(Connection conn, CustomerDTO customerDTO, AddressDTO addressDTO, AccountDTO accountDTO, KycDTO kycDTO, VehicleDTO vehicleDTO, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		String customerId = new IDGenerator().getCustomerId();
		String walletId = new IDGenerator().getWalletId();
		log.info("CustomerId : "+customerId);
		log.info("WalletId : "+walletId);
		customerDTO.setUserId(customerId);
		customerDTO.setWalletId(walletId);
		addressDTO.setUserId(customerId);
		accountDTO.setUserId(customerId);
		kycDTO.setUserId(customerId);
		kycDTO.setAddressProofDocPath(kycDTO.getAddressProofDoc());
		kycDTO.setIdProofDocPath(kycDTO.getIdProofDoc());
		CustomerDAO_.addCustomer(conn, customerDTO, userId);
		AddressDAO.addAddress(conn, addressDTO);
		AccountDAO.addAccount(conn, accountDTO, userId);
		KycDAO.addKYC(conn, kycDTO, userId);
		VehicleDAO.addVehicle(conn, vehicleDTO, customerId);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setErrorCode("WINCUBU0001");
		responseDTO.setMessage("Customer added successfully.");
		return responseDTO;
	}*/

	
	public static ResponseDTO approveCustomer(Connection conn, String customerId, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		new CustomerDAO().approveCustomer(conn, userId, customerId);
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage("Customer approved successfully.");
		responseDTO.setErrorCode("WINCUBU0004");
		return responseDTO;
	}
	
	public static CustomerDTO getCustomerWalletInfo(Connection conn, String customerId) {
		CustomerDTO customerDTO = new CustomerDAO().geCustomersWalletInfo(conn, customerId);
		return customerDTO;
	}
	
	public static ResponseDTO doTransaction(Connection conn, TransactionDTO transactionDTO, String userId) {
		transactionDTO.setSourceChannel(TransactionDTO.SOURCECHANNEL);
		transactionDTO.setSourceChannelIP(TransactionDTO.SOURCECHANNELIP);
		
		ResponseDTO responseDTO = WalletTransactionDAO.walletTransaction(transactionDTO, userId, conn);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TransactionErrorCode.WINTXNBU002.getErrorMessage());
			responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU002.name());
			return responseDTO;
		}
		responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU000.name());
		//sending SMS
		CustomerDTO customerDTO = new CustomerDAO().geCustomersWalletInfo(conn, transactionDTO.getWalletId());
		String smsContent = "Your wallet has been credited by "+transactionDTO.getTxnAmount()+ "Rs./-";
		TransactionService.sendSMS(customerDTO.getContactNumber(), smsContent);
		return responseDTO;
	}
	
	public static ResponseDTO doTagTransaction(Connection conn, TransactionDTO transactionDTO, String userId) {
		transactionDTO.setSourceChannel(TransactionDTO.SOURCECHANNEL);
		transactionDTO.setSourceChannelIP(TransactionDTO.SOURCECHANNELIP);
		transactionDTO = getCustomerInfoByVehicle(transactionDTO, conn);
		ResponseDTO responseDTO = WalletTransactionDAO.tagAllocattionTransaction(transactionDTO, userId, conn);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TransactionErrorCode.WINTXNBU002.getErrorMessage());
			responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU002.name());
			return responseDTO;
		}
		responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU000.name());
		String vehicleNumber = transactionDTO.getUserId();
		CustomerDTO customerDTO = new CustomerDAO().geCustomersWalletByVehicleNumber(conn, vehicleNumber);
		double txnAmount = Double.valueOf(transactionDTO.getSecurityDeposit())+Double.valueOf(transactionDTO.getTxnAmount());
		String smsContent = "Your wallet has been debited by "+txnAmount+ "Rs./- for tag allocation.";
		sendSMS(customerDTO.getContactNumber(), smsContent);
		return responseDTO;
	}

	private static TransactionDTO getCustomerInfoByVehicle(TransactionDTO transactionDTO, Connection conn) {
		String vehicleNumber = transactionDTO.getUserId(); 
		CustomerDTO customerDTO = new CustomerDAO().geCustomersWalletByVehicleNumber(conn, vehicleNumber);
		transactionDTO.setWalletId(customerDTO.getWalletId());
		transactionDTO.setTxnType("DEBIT");
		transactionDTO.setPayMode("TAGREGISTRATION");
		transactionDTO.setRemarks("Security and issuance amount is"+transactionDTO.getSecurityDeposit()+ " "+transactionDTO.getTxnAmount());
		transactionDTO.setPartnerId("ADMIN");
		transactionDTO.setPartnerRefId(System.currentTimeMillis()+"");
		return transactionDTO;
	}
	public static void sendSMS(String contactNumber, String smsContent) {
		String url = PropertyReader.getPropertyValue("smsURL");
		smsContent = smsContent.replaceAll(" ", "%20");
		url = url + contactNumber + smsContent;
		Server2ServerCall.secureServerCall(url);
	}
	public static void main(String[] args) {
		String sms = "Hi sudhir? How are you?";
		sms = sms.replaceAll(" ", "%20");
		System.out.println(sms);
	}
}
