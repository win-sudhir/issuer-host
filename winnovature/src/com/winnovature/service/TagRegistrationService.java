package com.winnovature.service;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.ManageTagResponse;
import com.winnovature.dao.TagAllocationService;
import com.winnovature.dao.TagRegistrationDAO;
import com.winnovature.dto.ChallanDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagAllocationDTO;
import com.winnovature.dto.TransactionDTO;

public class TagRegistrationService {
	static Logger log = Logger.getLogger(TagRegistrationService.class.getName());

	public static ResponseDTO isUnregisterTag(TagAllocationDTO tagAllocationDTO, String userId, String authToken,
			Connection conn) {
		ResponseDTO responseDTO = new ResponseDTO();
		TagAllocationDTO tagAllocationDTO1 = new TagAllocationDTO();
		tagAllocationDTO1.setAmtIssuence(tagAllocationDTO.getAmtIssuence());
		tagAllocationDTO1.setAmtSecurity(tagAllocationDTO.getAmtSecurity());
		tagAllocationDTO1.setVehicleNumber(tagAllocationDTO.getVehicleNumber());
		tagAllocationDTO.setMinThreshold("0");
		boolean isAllocated = TagRegistrationDAO.isAllocated(tagAllocationDTO.getTID(),
				tagAllocationDTO.getVehicleNumber(), tagAllocationDTO.getMinThreshold(), conn);
		log.info("isAllocated value == " + isAllocated);
		if (isAllocated) {
			responseDTO.setMessage("TID not found in the inventory.");
			responseDTO.setStatus("0");
			return responseDTO;
		}
		
		tagAllocationDTO = UnRegisterTagService.isUnRegisterTag(tagAllocationDTO, conn);
		tagAllocationDTO1.setUnRegTxnAmount(tagAllocationDTO.getUnRegTxnAmount());
		if (tagAllocationDTO.isUnRegister()) {
			String result = UnRegisterTagService.removeFromBlackList(tagAllocationDTO.getTagId(), userId, authToken,
					conn);
			if (result.equalsIgnoreCase("FAILURE")) {
				log.info("Fail to remove renregistered tag from blacklist tag.");
				responseDTO.setMessage("Fail to remove renregistered tag from blacklist.");
				responseDTO.setStatus("0");
				return responseDTO;
			}
			UnRegisterTagService.unRegisterTagTransaction(tagAllocationDTO1, userId, authToken, conn);
		}
		log.info("ISSUEANCE : "+tagAllocationDTO1.getAmtIssuence()+" SECURITY : "+tagAllocationDTO1.getAmtSecurity());
		if (TagRegistrationDAO.checkBalance(tagAllocationDTO1.getVehicleNumber(), tagAllocationDTO1.getAmtIssuence(),
				tagAllocationDTO1.getAmtSecurity(), conn)) {
			log.info("Balance not sufficient.");
			responseDTO.setMessage("In-sufficient balance.");
			responseDTO.setStatus("0");
			return responseDTO;
		}
		responseDTO.setStatus("1");
		return responseDTO;
	}

	public static ResponseDTO processTagRegistration(TagAllocationDTO tagAllocationDTO, String userId,
			Connection conn) {
		ResponseDTO responseDTO = new ResponseDTO();
		String XMLData;
		try {
			XMLData = new ManageTagResponse().mngTagApiCall(tagAllocationDTO.getTID(),
					tagAllocationDTO.getVehicleNumber(), conn);
			JSONObject parsedXML = new TagAllocationService().parseTagAllocationProcess(XMLData);
			if (parsedXML == null) {
				responseDTO.setMessage("Unable to process tag allocation, NPCI getting null response.");
				responseDTO.setStatus("0");
				return responseDTO;
			}
			String errCode = parsedXML.getString("errCode");
			String result = parsedXML.getString("result");
			String seqNO = parsedXML.getString("seqNO");
			String txnID = parsedXML.getString("txnID");
			if ((errCode.equals("000") || errCode.equals("240")) && result.equalsIgnoreCase("SUCCESS")) {
				log.info("Result Success...");
				tagAllocationDTO.setMinThreshold("0");
				tagAllocationDTO.setAmtRecharge("0");
				tagAllocationDTO.setAmtInsurance("0");
				tagAllocationDTO.setAmtRSA("0");
				boolean isChallanGenerated = new ManageTagResponse().allocateTagInsertChallan(tagAllocationDTO.getTID(),
						tagAllocationDTO.getVehicleNumber(), tagAllocationDTO.getMinThreshold(), txnID, seqNO,
						tagAllocationDTO.getAmtIssuence(), tagAllocationDTO.getAmtRecharge(),
						tagAllocationDTO.getAmtRSA(), tagAllocationDTO.getAmtSecurity(),
						tagAllocationDTO.getAmtInsurance(), conn);
				if (isChallanGenerated) {
					// tag allocation txn-----
					TransactionDTO transactionDTO = new TransactionDTO();
					transactionDTO.setTxnAmount(tagAllocationDTO.getAmtIssuence());
					transactionDTO.setSecurityDeposit(tagAllocationDTO.getAmtSecurity());
					transactionDTO.setUserId(tagAllocationDTO.getVehicleNumber());
					// Connection conn = DatabaseManager.getConnection();
					TransactionService.doTagTransaction(conn, transactionDTO, userId);

					ChallanDTO challan = TagRegistrationDAO.getChallanData(tagAllocationDTO, conn);
					log.info("CHALLAN return challanData :: " + challan.toString());
					log.info("TID registered and allocated.................");

					responseDTO.setMessage("Tag registered successfully in NPCI.");
					responseDTO.setStatus("1");
					responseDTO.setChallan(challan);
					return responseDTO;
				}

				else {
					log.info("CHALLAN is not generated :: isgenerated : " + isChallanGenerated);
					responseDTO.setMessage("CHALLAN is not generated.");
					responseDTO.setStatus("0");
					return responseDTO;
				}
			} else {
				responseDTO.setMessage("Tag could not be registered-RESULT" + result + "-ERRORCODE-" + errCode);
				responseDTO.setStatus("0");
				return responseDTO;
			}

		} catch (Exception e) {
			log.info("Exception in tag allocation process" + e.getMessage());
			e.printStackTrace();
		}
		responseDTO.setMessage("Unable to process tag allocation.");
		responseDTO.setStatus("0");
		return responseDTO;
	}

}
