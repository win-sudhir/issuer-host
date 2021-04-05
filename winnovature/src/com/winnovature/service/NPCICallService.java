/**
 * 
 */
package com.winnovature.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.TagDetailsDAO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagInfoDTO;
import com.winnovature.utils.PropertyReader;
import com.winnovature.validation.TagErrorCode;



public class NPCICallService {

	static Logger log = Logger.getLogger(NPCICallService.class.getClass());
	public static String httpServerCall(String URL, String data) {
		String line = null;
		BufferedReader br = null;
		StringBuffer respJson = null;

		log.info("NPCICallService.java ::: httpServerCall() :: Posting URL : " + URL + " , Request Data : " + data);
		try {
			URL url = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			
			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			
			wr.write(data);
			wr.flush();

			respJson = new StringBuffer();

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				log.info("NPCICallService.java ::: httpServerCall :: HTTP OK");
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while ((line = br.readLine()) != null) {
					log.info(line);
					respJson.append(line);
				}

				br.close();

				return respJson.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("NPCICallService.java ::: httpServerCall :: Error Occurred while Processing Request : "
					+ e.getMessage());
		}

		return null;
	}
	
	public static ResponseDTO parseResponse(String respXML, TagInfoDTO tagInfoDTO, Connection conn)  {
		ResponseDTO responseDTO = new ResponseDTO();
		try {

			Map<String, String> parsedXml = new NPCIXMLParser().parseData(respXML);
			String npciRespResult = parsedXml.get("TxnRespTagResult");
			if (npciRespResult == null) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(TagErrorCode.TAGBU0027.getErrorMessage());
				responseDTO.setErrorCode(TagErrorCode.TAGBU0027.name());
				return responseDTO;
			}
			if (npciRespResult.equalsIgnoreCase("FAILURE")) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(TagErrorCode.TAGBU0033.getErrorMessage());
				responseDTO.setErrorCode(TagErrorCode.TAGBU0033.name());
				return responseDTO;
			}
			JSONObject resp = null;
			if (npciRespResult.equalsIgnoreCase("SUCCESS")) {
				resp = new TagDetailsDAO().tagAddRemoveException(tagInfoDTO, conn);
			}
			if (resp == null)// && !resp.has("resp_msg"))
			{
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(TagErrorCode.TAGBU0028.getErrorMessage());
				responseDTO.setErrorCode(TagErrorCode.TAGBU0028.name());
				return responseDTO;
			}
			if (resp.getInt("status") != -1 && resp.getInt("status") != 2) {
				responseDTO.setStatus(ResponseDTO.success);
				responseDTO.setMessage(resp.getString("resp_msg"));
				//responseDTO.setErrorCode(TagErrorCode.TAGBU0029.name());
				return responseDTO;
			}
			/*log.info("resp.getString('operation') "+resp.getString("operation"));
			if (!resp.getString("operation").equalsIgnoreCase("ADD")
					|| !resp.getString("operation").equalsIgnoreCase("REMOVE")) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(TagErrorCode.TAGBU0030.getErrorMessage());
				responseDTO.setErrorCode(TagErrorCode.TAGBU0030.name());
				return responseDTO;
			}*/
			
			responseDTO.setStatus(ResponseDTO.success);
			
			return responseDTO;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseDTO;
	}
	//to ADD/REMOVE the tag in exception
	public String callNPCI(TagInfoDTO tagInfoDTO){
		String issuerSwitchUrl = PropertyReader.getPropertyValue("issuerSwitchUrl");
		String reqParams = "opid=" + tagInfoDTO.getRequestType() + "&tagId=" + tagInfoDTO.getTagId() + "&seqNum=1" + "&excCode=" + tagInfoDTO.getExceptionCode()
				+ "&ttype=ReqMngException";
		
		String xmlResponse = httpServerCall(issuerSwitchUrl, reqParams);
		log.info("xmlResponse : "+xmlResponse);
		return xmlResponse;
		
	}
	//For request details 
	public String callNPCIForSearch(TagInfoDTO tagInfoDTO){
		String issuerSwitchUrl = PropertyReader.getPropertyValue("issuerSwitchUrl");
				
		String reqParams = "TID=" + tagInfoDTO.getTid() + "&tagId=" + tagInfoDTO.getTagId() + "&vehNo=" + tagInfoDTO.getVehicleNumber() + "&ttype=tagDetails";
		
		String xmlResponse = httpServerCall(issuerSwitchUrl, reqParams);
		log.info("xmlResponse : "+xmlResponse);
		return xmlResponse;
		
	}
}
