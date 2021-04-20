package com.winnovature.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.UnRegisterTagDAO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagAllocationDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.dto.WalletResponse;
import com.winnovature.validation.TransactionErrorCode;

public class UnRegisterTagService {
	static Logger log = Logger.getLogger(UnRegisterTagService.class.getName());
	
	public static TagAllocationDTO isUnRegisterTag(String tid, String vehicleNumber, Connection conn) {
		TagAllocationDTO tagAllocationDTO = UnRegisterTagDAO.isUnRegisterTag(tid, vehicleNumber, conn);
		return tagAllocationDTO;
	}

	public static String removeFromBlackList(String tagId, String userId, String authToken, Connection conn) {
		String url ="http://localhost:8080/winnovature/tag/excemanagement";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("opid","REMOVE");
		jsonObject.put("tagId",tagId);		
		jsonObject.put("excCode","01");
		jsonObject.put("insertionFlag","M");
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
		log.info("---UNREG TRANSACTION START---");
		TransactionDTO transactionDTO = new TransactionDTO();	
		transactionDTO.setSourceChannel(TransactionDTO.SOURCECHANNEL);
			transactionDTO.setSourceChannelIP(TransactionDTO.SOURCECHANNELIP);
			transactionDTO = TransactionService.getCustomerInfoByVehicle(transactionDTO, conn);
			transactionDTO.setTxnAmount(tagAllocationDTO.getUnRegTxnAmount());
			transactionDTO.setRemarks("Unregister tag transaction.");
			transactionDTO.setPayMode("UNREGTAG");
			ResponseDTO responseDTO = UnRegisterTagDAO.unRegisterTagTransaction(transactionDTO, userId, conn);
			if(responseDTO.getStatus().equals(ResponseDTO.failure)){
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(TransactionErrorCode.WINTXNBU002.getErrorMessage());
				responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU002.name());
				return responseDTO.getStatus();//failure
			}
			UnRegisterTagDAO.updateUnRegisterTagStatus(tagAllocationDTO, conn);
			responseDTO.setErrorCode(TransactionErrorCode.WINTXNBU000.name());
			return "1";//success
	}
	
	private static WalletResponse removeBlackListTagTest(String data,String userId, String authToken, String sURL) throws Exception
	{
		// TODO Auto-generated method stub	
		log.info("removeBlackListTag.java ::: Posting URL : " + sURL);
		
		WalletResponse walletResponse = new WalletResponse();
		URL obj = new URL(sURL);
		
		
		//Use for HTTP Connection
 		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
	
		con.setRequestMethod("POST");
		con.addRequestProperty("Content-Type", "text/plain;charset=UTF-8");
		con.setDoOutput(true);
		con.setRequestProperty("Authorization",authToken);
		con.setRequestProperty("userId",userId);
		
		con.connect();
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();

		// set response code
		int responseCode = con.getResponseCode();
		walletResponse.setHttpCode(responseCode);
		
		StringBuffer response = new StringBuffer();
		
		
		log.info("removeBlackListTag() ::: Response Code : " + responseCode);
		BufferedReader in = null;
		String inputLine = null;
		if(responseCode==200)
		{
			log.info("removeBlackListTag ::: responseCode : "+responseCode);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		}
		else if(responseCode==202)
		{
			log.info("Server2ServerCall.java :::  removeBlackListTag :: responseCode : "+responseCode);
			in = null;
			response.append("NO DATA");
		}
		else
		{
			in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		
		if(in != null)
		{
			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			
			in.close();
			
			//print result
			
			log.info("Server2ServerCall.java ::: removeBlackListTag() :: Response Received From switch call :: "+response.toString());
			walletResponse.setResponse(response.toString());
		}

		return walletResponse;
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
