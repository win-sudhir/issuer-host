package com.winnovature.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.constants.WINConstants;
import com.winnovature.dao.CustomerDAO;
import com.winnovature.dao.UnRegisterTagDAO;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagAllocationDTO;
import com.winnovature.dto.TransactionDTO;

public class UnRegisterTagService {
	static Logger log = Logger.getLogger(UnRegisterTagService.class.getName());
	
	public static TagAllocationDTO isUnRegisterTag(TagAllocationDTO tagAllocationDTO, Connection conn) {
		return UnRegisterTagDAO.isUnRegisterTag(tagAllocationDTO.getTID(), tagAllocationDTO.getVehicleNumber(), conn);
		//return tagAllocationDTO;
	}

	public static String removeFromBlackList(String tagId, String userId, String authToken, Connection conn) {
		String url =WINConstants.localBLURL;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("opid","REMOVE");
		jsonObject.put("tagId",tagId);		
		jsonObject.put("excCode","01");
		jsonObject.put("insertionFlag","A");
		JSONObject jsonResp = null;
		try {
			String response = removeBlackListTag(jsonObject.toString(), userId, authToken, url);
			jsonResp = new JSONObject(response);
			log.info("RESPONSE : "+jsonResp.toString());
			if(jsonResp!=null) {
				if(jsonResp.getString("status").equals("1")) {
					return "SUCCESS";
				}else {
					return "FAILURE";
				}
			}
		}
		catch (Exception e) {
			log.info("Exception in removeFromBlackList"+e.getMessage());
		}
		return "FAILURE";
	}

	public static String unRegisterTagTransaction(TagAllocationDTO tagAllocationDTO, String userId, String authToken, Connection conn) {
		log.info("---UNREG TRANSACTION START---"+tagAllocationDTO.getVehicleNumber());
		CustomerDTO customerDTO = new CustomerDAO().geCustomersWalletByVehicleNumber(conn, tagAllocationDTO.getVehicleNumber());
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setWalletId(customerDTO.getWalletId());
		log.info("--- UNREG WALLETID ---"+customerDTO.getWalletId());
		log.info("--- UNREG TXN AMOUNT ---"+tagAllocationDTO.getUnRegTxnAmount());
		transactionDTO.setSourceChannel(TransactionDTO.SOURCECHANNEL);
		transactionDTO.setSourceChannelIP(TransactionDTO.SOURCECHANNELIP);
		transactionDTO.setTxnType("DEBIT");
		//transactionDTO = TransactionService.getCustomerInfoByVehicle(transactionDTO, conn);
		transactionDTO.setTxnAmount(tagAllocationDTO.getUnRegTxnAmount());
		transactionDTO.setSecurityDeposit("0");
		transactionDTO.setRemarks("Unregister tag transaction.");
		transactionDTO.setPayMode("UNREGTAG");
		ResponseDTO responseDTO = UnRegisterTagDAO.unRegisterTagTransaction(transactionDTO, userId, conn);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			responseDTO.setStatus(ResponseDTO.failure);
			//responseDTO.setMessage(TransactionErrorCode.WINTXNBU002.getErrorMessage());
			//responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU002.name());
			return responseDTO.getStatus();//failure
		}
		//responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU000.name());
		//responseDTO.setStatus("1");
		return "1";//success
	}
	
	
	public static String removeBlackListTag(String reqData, String userId, String authToken, String URL) {
		try {
			String data = reqData;
			String line = null;
			log.info("finalURL >>>>>> >>>> " + URL);
			URL url = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
			con.setRequestProperty("Authorization",authToken);
			con.setRequestProperty("userId",userId);
			con.addRequestProperty("Content-Length", data.getBytes().length + "");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			log.info("Data length :: " + data.getBytes().length);
			wr.write(data);
			wr.flush();
			StringBuffer response = new StringBuffer();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				log.info("HTTP OK....");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while ((line = br.readLine()) != null) {
					log.info(line);
					response.append(line);
				}
				br.close();
			} else {
				log.info("Response code :: " + con.getResponseCode() + " Response Message :: "
						+ con.getResponseMessage());
				//response = response.append(con.getResponseCode());
				response = response.append("FAIL");
			}

			con.disconnect();
			response.toString();

			log.info("Response from :: "+URL+ " :: " + response.toString());
			
			return response.toString();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return null;
	}

}
