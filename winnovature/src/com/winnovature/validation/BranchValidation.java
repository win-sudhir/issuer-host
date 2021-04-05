package com.winnovature.validation;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.winnovature.dto.BranchAccountDTO;
import com.winnovature.dto.BranchDTO;
import com.winnovature.dto.ResponseDTO;

public class BranchValidation {
	static Logger log = Logger.getLogger(BranchValidation.class.getName());

	public static ResponseDTO validateBranchRequest(BranchDTO branchDTO, BranchAccountDTO branchAccountDTO,
			Connection conn) {
		ResponseDTO responseDTO = new ResponseDTO();

		log.info("branchDTO ::"+branchDTO);
		log.info("branchAccountDTO ::"+branchAccountDTO);
		if (branchDTO.getBranchName() == null || branchDTO.getBranchName().isEmpty() 
			|| branchDTO.getEmailId() == null || branchDTO.getEmailId().isEmpty() 
			|| branchDTO.getContactNumber() == null || branchDTO.getContactNumber().isEmpty() 
			|| branchDTO.getCity() == null 	|| branchDTO.getCity().isEmpty() 
			|| branchDTO.getState() == null || branchDTO.getState().isEmpty()
			|| branchDTO.getPin() == null || branchDTO.getPin().isEmpty()
			|| branchAccountDTO.getAccountNumber() == null || branchAccountDTO.getAccountNumber().isEmpty()
			|| branchAccountDTO.getIfscCode() == null || branchAccountDTO.getIfscCode().isEmpty()
			|| branchAccountDTO.getBankName() == null || branchAccountDTO.getBankName().isEmpty()
			|| branchAccountDTO.getAccountType() == null || branchAccountDTO.getAccountType().isEmpty()
			|| branchAccountDTO.getBranchAddress() == null || branchAccountDTO.getBranchAddress().isEmpty()
			) {

			log.info("---------------------------------------------------------------------------");
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU001.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU001.name());
			return responseDTO;
		}

		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
}
