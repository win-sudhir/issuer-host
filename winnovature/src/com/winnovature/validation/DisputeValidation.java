/**
 * 
 */
package com.winnovature.validation;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.winnovature.dispute.dao.DisputeRaiseDAO;
import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.DisputeUploadFileDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.DateUtility;




public class DisputeValidation {
	static Logger log = Logger.getLogger(DisputeRaiseDAO.class.getClass());
	public ResponseDTO validateRaiseDisputeRequest(DisputeMasterDTO disputeMasterDTO){
		ResponseDTO responseDTO = new ResponseDTO();
		if(disputeMasterDTO.getOriginalTxnId()==null || disputeMasterDTO.getOriginalTxnId().isEmpty()){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if(disputeMasterDTO.getReasonCode()==null || disputeMasterDTO.getReasonCode().isEmpty()){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if(disputeMasterDTO.getTxnAmount()==null || disputeMasterDTO.getTxnAmount().isEmpty()){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if(disputeMasterDTO.getComment()==null || disputeMasterDTO.getComment().isEmpty()){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if(disputeMasterDTO.getFullPartialIndicator()==null || disputeMasterDTO.getFullPartialIndicator().isEmpty()){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if(disputeMasterDTO.getTxnAmount().equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0027.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0027.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	public static ResponseDTO validateDisputeUploadFile( DisputeUploadFileDTO disputeUploadFileDTO){
		
		
		ResponseDTO responseDTO = new ResponseDTO(); 
		
		
		  responseDTO = validateParams(disputeUploadFileDTO);
		
		  if(responseDTO.getStatus().equals(ResponseDTO.failure)){ return responseDTO;
		 }
		 
		
		//validate file is empty
		responseDTO = validateIsFileEmpty(disputeUploadFileDTO);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		
		
		responseDTO = validateTransactionDateTime(disputeUploadFileDTO);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		responseDTO = validateTransactionAmount(disputeUploadFileDTO);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		
		
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
		
	}
	
	
	private static ResponseDTO validateIsFileEmpty(DisputeUploadFileDTO disputeUploadFileDTO){
		ResponseDTO responseDTO = new ResponseDTO();
		log.info("File having number of records : "+disputeUploadFileDTO.getTransactionInfoDTO().size());
		if(disputeUploadFileDTO.getTransactionInfoDTO().size()<=0){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0021.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0021.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	private static ResponseDTO validateTransactionDateTime(DisputeUploadFileDTO disputeUploadFileDTO){
		ResponseDTO responseDTO = new ResponseDTO(); 
		String date = null;
		String txnId = null;
		for(int n=0; n<disputeUploadFileDTO.getTransactionInfoDTO().size(); n++){
			System.out.println("date time : "+disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionDateAndTime());
			date = disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionDateAndTime();
			if(date.startsWith("'"))
				date = disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionDateAndTime().substring(1);
			log.info("date "+date);
			
			if(!DateUtility.validateDate(disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionDateAndTime(), "dd-MM-yyyy HH:mm:ss")){//"yymmddhhmmss")){
				
				txnId = disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionId();
				
				if(txnId.startsWith("'"))
					txnId = disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionId().substring(1);
				
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0022.getErrorMessage()+txnId);
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0022.name());
				return responseDTO;
			}
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	
	
private static ResponseDTO validateTransactionAmount(DisputeUploadFileDTO disputeUploadFileDTO){
	ResponseDTO responseDTO = new ResponseDTO();
	for(int n=0; n<disputeUploadFileDTO.getTransactionInfoDTO().size(); n++){
		if(!Pattern.matches("[0-9]+", disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionAmount())){
			String rrn = disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionId().substring(1);
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0018.getErrorMessage()+rrn);
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0018.name());
			return responseDTO;
		}
		
		if(!Pattern.matches("[0-9]+", disputeUploadFileDTO.getTransactionInfoDTO().get(n).getSettlementAmount())){
			String rrn = disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionId().substring(1);
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0019.getErrorMessage()+rrn);
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0019.name());
			return responseDTO;
		}
		if(!Pattern.matches("[0-9]+", disputeUploadFileDTO.getTransactionInfoDTO().get(n).getDisputedAmount())){
			String rrn = disputeUploadFileDTO.getTransactionInfoDTO().get(n).getTransactionId().substring(1);
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0020.getErrorMessage()+rrn);
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0020.name());
			return responseDTO;
		}
		
		
		
	}
	responseDTO.setStatus(ResponseDTO.success);
	return responseDTO;
}

	

private static ResponseDTO validateParams(DisputeUploadFileDTO disputeUploadFileDTO){
	
	ResponseDTO responseDTO = new ResponseDTO(); 
	if(disputeUploadFileDTO.getDisputeDate()==null || disputeUploadFileDTO.getDisputeDate()==""||
			disputeUploadFileDTO.getRequestType()== null||disputeUploadFileDTO.getRequestType()=="" ||
			
			disputeUploadFileDTO.getRequestBy()==null||disputeUploadFileDTO.getRequestBy()==""){
		responseDTO.setStatus(ResponseDTO.failure);
		responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
		responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
		return responseDTO;
	}
	
	responseDTO.setStatus(ResponseDTO.success);
	return responseDTO;
}
	
	
}	
	
	
	
	
	
	
	
	
	
