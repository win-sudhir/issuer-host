/**
 * 
 */
package com.winnovature.validation;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.winnovature.dao.ReconDAO;
import com.winnovature.dto.Recon861DTO;
import com.winnovature.dto.ResponseDTO;
//import com.winnovature.utils.DateUtility;
import com.winnovature.utils.PropertyReader;



public class ReconValidation {
	static Logger log = Logger.getLogger(ReconValidation.class.getName());
	
	public static ResponseDTO reconValidation(String userId, String authorization, Recon861DTO Recon861DTO, Connection conn){
		
		ResponseDTO responseDTO = new ResponseDTO(); 
		/*responseDTO = SessionValidation.validateSession(userId, authorization);//isValidSession(userId, authorization);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}*/
		responseDTO = validateParams(Recon861DTO.getReconDate(), Recon861DTO.getReconCycle(), Recon861DTO.getFileName());
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		
		//validate file is empty
		responseDTO = validateIsFileEmpty(Recon861DTO);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		
		responseDTO = validateIsPreviousCycleProcessed(Recon861DTO.getReconDate(), Recon861DTO.getReconCycle(), conn);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		
		responseDTO = validateIsCycleProcessed(Recon861DTO.getReconDate(), Recon861DTO.getReconCycle(), conn);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		
		responseDTO = validateFileName(Recon861DTO.getFileName(), Recon861DTO.getReconDate(), Recon861DTO.getReconCycle());
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		
		responseDTO = validateIsFileProcessed(Recon861DTO.getFileName(), conn);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		
		responseDTO = validateTransactionDateTime(Recon861DTO);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		/*
		responseDTO = validateTransactionAmount(Recon861DTO);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			return responseDTO;
		}
		*/
		
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	
	private static ResponseDTO validateTransactionDateTime(Recon861DTO recon861DTO){
		ResponseDTO responseDTO = new ResponseDTO(); 
		String date = null;
		String txnId = null;
		for(int n=0; n<recon861DTO.getTransactionInfoDTO().size(); n++){
			//System.out.println("date time : "+Recon861DTO.getTransactionInfoDTO().get(n).getTransactionDateAndTime());
			//if(recon861DTO.getTransactionInfoDTO().get(n).getTransactionDateAndTime().startsWith("'"))
				//date = recon861DTO.getTransactionInfoDTO().get(n).getTransactionDateAndTime().substring(1);
			//if(!DateUtility.validateDate(date, "yyMMddHHmmss")){//"yymmddhhmmss")){
				
				txnId = recon861DTO.getTransactionInfoDTO().get(n).getTransactionId();
				if(txnId.startsWith("'"))
					txnId = recon861DTO.getTransactionInfoDTO().get(n).getTransactionId().substring(1);
				
				//rrn = Recon861DTO.getTransactionInfoDTO().get(n).getRrn().substring(1);
				//responseDTO.setStatus(ResponseDTO.failure);
				//responseDTO.setMessage(ReconErrorCode.RECONBU003.getErrorMessage()+rrn);
				//responseDTO.setErrorCode(ReconErrorCode.RECONBU003.name());
				//return responseDTO;
			//}
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	//private static ResponseDTO validateTransactionAmount(String transationAmount, String transactionId){
	/*private static ResponseDTO validateTransactionAmount(Recon861DTO Recon861DTO){
		ResponseDTO responseDTO = new ResponseDTO();
		for(int n=0; n<Recon861DTO.getTransactionInfoDTO().size(); n++){
			if(!Pattern.matches("[0-9]+", Recon861DTO.getTransactionInfoDTO().get(n).getTransactionAmount())){
				//String rrn = Recon861DTO.getTransactionInfoDTO().get(n).getRrn().substring(1);
				String rrn = Recon861DTO.getTransactionInfoDTO().get(n).getRrn();
				if(rrn.startsWith("'"))
					rrn = Recon861DTO.getTransactionInfoDTO().get(n).getRrn().substring(1);
				
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(ReconErrorCode.RECONBU004.getErrorMessage()+rrn);
				responseDTO.setErrorCode(ReconErrorCode.RECONBU004.name());
				return responseDTO;
			}
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}*/
	
	private static ResponseDTO validateIsFileProcessed(String fileName, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO(); 
		if(ReconDAO.checkFileProcessed(fileName, conn)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ReconErrorCode.RECONBU005.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU005.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	private static ResponseDTO validateFileName(String fileName, String reconDate, String reconCycle)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		responseDTO.setStatus(ResponseDTO.failure);
		try
		{
			log.info("841DATA file name :: "+fileName);
			
			SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat targetFormat = new SimpleDateFormat("yyDDD");
			Date cDate = originalFormat.parse(reconDate);
			String fname1 = PropertyReader.getPropertyValue("newDataFile1")+targetFormat.format(cDate);
			String fname2 = PropertyReader.getPropertyValue("newDataFile2")+targetFormat.format(cDate);
			log.info("CYCLE1 FILENAME SHOULD BE :: "+fname1);
			log.info("CYCLE2 FILENAME SHOULD BE :: "+fname2);
			if(fileName.startsWith(fname1) && reconCycle.equals("1"))
			{
				log.info("The file is for cycle 1 and date: "+reconDate+" and name starts with "+fname1);
				responseDTO.setStatus(ResponseDTO.success);
				return responseDTO;
			}
			if(fileName.startsWith(fname2) && reconCycle.equals("2"))
			{
				log.info("The file is for cycle 2 and date: "+reconDate+" and name starts with "+fname2);
				responseDTO.setStatus(ResponseDTO.success);
				return responseDTO;
			}
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ReconErrorCode.RECONBU006.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU006.name());
			return responseDTO;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}
	//parameter missing 007 TODO
	
	private static ResponseDTO validateParams(String reconDate, String reconCycle, String fileName){
		ResponseDTO responseDTO = new ResponseDTO(); 
		if(reconDate==null || reconCycle==null || fileName==null
				|| reconDate=="" || reconCycle=="" || fileName==""){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ReconErrorCode.RECONBU007.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU007.name());
			return responseDTO;
		}
		/*if(reconDate!=null || reconCycle!=null || fileName!=null){
			responseDTO.setStatus(ResponseDTO.success);
		}else{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ReconErrorCode.RECONBU007.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU007.name());
			return responseDTO;
		}*/
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	private static ResponseDTO validateIsPreviousCycleProcessed(String reconDate, String reconCycle, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO(); 
		if(!ReconDAO.validatePreviousCycle(reconDate, Integer.valueOf(reconCycle), conn)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ReconErrorCode.RECONBU009.getErrorMessage()+new ReconDAO().latestDate(conn));
			responseDTO.setErrorCode(ReconErrorCode.RECONBU009.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	private static ResponseDTO validateIsCycleProcessed(String reconDate, String reconCycle, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO(); 
		if(ReconDAO.isCycleProcessed(reconDate, reconCycle, conn)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ReconErrorCode.RECONBU008.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU008.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	private static ResponseDTO validateIsFileEmpty(Recon861DTO recon861DTO){
		ResponseDTO responseDTO = new ResponseDTO();
		log.info("File having number of records : "+recon861DTO.getTransactionInfoDTO().size());
		if(recon861DTO.getTransactionInfoDTO().size()<=0){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(ReconErrorCode.RECONBU0013.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU0013.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	
	
	public static void main(String[] args) {
		if(Pattern.matches("[0-9]+", "50")){
			System.out.println("valid");
		}
		else
		{
			System.out.println("invalid");
		}
	}
}
