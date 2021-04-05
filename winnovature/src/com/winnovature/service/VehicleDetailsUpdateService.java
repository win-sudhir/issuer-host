/**
 * 
 */
package com.winnovature.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.winnovature.dao.UpdateNPCIVehicleDetailsDAO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDetailsDTO;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.PropertyReader;
import com.winnovature.validation.UpdateVehicleValidation;
import com.winnovature.validation.VehicleErrorCode;

/**
 * @author Sudhir Khobragade
 *
 * 18-Feb-2021
 */

public class VehicleDetailsUpdateService {
	static Logger log = Logger.getLogger(VehicleDetailsUpdateService.class.getName());
	
	public static ResponseDTO getVehicleDetals() {
		ResponseDTO responseDTO = new ResponseDTO();
		List<VehicleDetailsDTO> data = UpdateNPCIVehicleDetailsDAO.getVehicleDetails();
		responseDTO.setData(data);
		return responseDTO;
	}
	
	public static ResponseDTO updateVehicleDetals(VehicleDetailsDTO vehicleDetailsDTO, Connection conn, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		
		log.info("In updateVehicleDetals : "+vehicleDetailsDTO.toString());
		boolean flag = new UpdateNPCIVehicleDetailsDAO().isVehicleNumberPresent(vehicleDetailsDTO.getVehicleNumber());
		log.info("Step 1");
		if(flag) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(VehicleErrorCode.WINNVEHBU005.getErrorMessage());
			responseDTO.setErrorCode(VehicleErrorCode.WINNVEHBU005.name());
			return responseDTO;
		}
		log.info("Step 2");
		responseDTO = UpdateVehicleValidation.velidateVehicleInfo(vehicleDetailsDTO, conn, userId);
		log.info("Step 3");
		if(responseDTO.getStatus().equals(ResponseDTO.failure)) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(VehicleErrorCode.WINNVEHBU001.getErrorMessage());
			responseDTO.setErrorCode(VehicleErrorCode.WINNVEHBU001.name());
			return responseDTO;
		}
		log.info("Step 4");
		String xmlData = vehicleUpdateS2SCall(vehicleDetailsDTO);
		log.info("Step 5");
		JSONObject resp = validateNPCIResponse(xmlData);
		log.info("Step 6");
		if(resp==null) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(VehicleErrorCode.WINNVEHBU004.getErrorMessage());
			responseDTO.setErrorCode(VehicleErrorCode.WINNVEHBU004.name());
			return responseDTO;
		}
		log.info("Step 7");
		if(resp.getString("status").equalsIgnoreCase("FAILURE")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(VehicleErrorCode.WINNVEHBU001.getErrorMessage()+resp.getString("errorCode"));
			responseDTO.setErrorCode(VehicleErrorCode.WINNVEHBU001.name());
			return responseDTO;
		}
		log.info("Step 8");
		if(resp.getString("errorCode").equals("000") && resp.getString("status").equalsIgnoreCase("SUCCESS")){//responseDTO.getStatus().equals(ResponseDTO.failure)) {
			responseDTO = UpdateNPCIVehicleDetailsDAO.updateVehicleDetals(vehicleDetailsDTO, conn, userId);
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(VehicleErrorCode.WINNVEHBU000.getErrorMessage());
			responseDTO.setErrorCode(VehicleErrorCode.WINNVEHBU000.name());
			return responseDTO;
		}
		log.info("Step 9");
		return responseDTO;
	}
		
	private static String vehicleUpdateS2SCall(VehicleDetailsDTO vehicleDetailsDTO)
	{
		String updateVehDetailsURL = PropertyReader.getPropertyValue("issuerSwitchUrl");
		String XMLData = null;
		String opid = "UPDATE";
		String ttype = "ReqMngTagEntries";
		
		String postData = "ttype="+ttype+"&opid=" + opid + "&tagId=" + vehicleDetailsDTO.getTagId() + "&vehicleClass=" + vehicleDetailsDTO.getVehicleClassId() + "&regNum=" + vehicleDetailsDTO.getVehicleNumber() + "&comVehicle="+ vehicleDetailsDTO.getIsCommercial();
		
		log.info("UpdateVehicleDetails.java :: URL "+updateVehDetailsURL+" , postData : "+postData);
		
		try
		{
			URL url = new URL(updateVehDetailsURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			// con.addRequestProperty("Content-Type",
			// "text/plain;charset=UTF-8");
			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			log.info("UpdateVehicleDetails.java ::: Output Stream Open");
			wr.write(postData);
			MemoryComponent.closeOutputStreamWriter(wr);

			StringBuffer outputResp = new StringBuffer();
			String lineResp = null;

			log.info("UpdateVehicleDetails.java ::: Response Code : " + con.getResponseCode());
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				log.info("UpdateVehicleDetails.java ::: Response Code " + con.getResponseCode() + " is HTTP OK....");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while ((lineResp = br.readLine()) != null) {
					log.info("UpdateVehicleDetails.java ::: Line : " + lineResp);
					outputResp.append(lineResp);
				}
				XMLData = outputResp.toString();
				MemoryComponent.closeBufferedReader(br);
			}

			else {
				log.info("UpdateVehicleDetails.java ::: updateVehDetailsURL : " + updateVehDetailsURL + " response code :: " + con.getResponseCode());
				XMLData = outputResp.toString();
			}
			return XMLData;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return XMLData;
	}			 
			
	private static JSONObject validateNPCIResponse(String XMLData)
	{
		JSONObject resp = null;//new JSONObject();
		if (XMLData != null)
		{
			resp = new JSONObject();
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(XMLData));
	
				Document doc = db.parse(is);
				// for Resp node
				NodeList respnode = doc.getElementsByTagName("Resp");
				Element respelement = (Element) respnode.item(0);
				NamedNodeMap respattributes = respelement.getAttributes();
				for (int i = 0; i < respattributes.getLength(); i++) 
				{
					Attr attr = (Attr) respattributes.item(i);
					String attrName = attr.getNodeName();
					// String attrValue = attr.getNodeValue();
					if (attrName.equalsIgnoreCase("respCode")) {
						String respCode = attr.getNodeValue();
						log.info("UpdateVehicleDetails() :: respCode : " + respCode);
						break;
					}
				}
				// for tag node
				NodeList exception = doc.getElementsByTagName("Tag");
				Element element = (Element) exception.item(0);
				NamedNodeMap attributes = element.getAttributes();
				String errCode = null, result = null;
				// boolean flag = false;
				for (int i = 0; i < attributes.getLength(); i++) 
				{
					Attr attr = (Attr) attributes.item(i);
					String attrName = attr.getNodeName();
					// String attrValue = attr.getNodeValue();
					if (attrName.equals("errCode")) {
						errCode = attr.getNodeValue();
					}
					if (attrName.equals("result")) {
						result = attr.getNodeValue();
					}
				
				}
				if (errCode.equals("000") && result.equalsIgnoreCase("SUCCESS")) 
				{
					log.info("Result Success...");
					/*NodeList txn = doc.getElementsByTagName("Txn");
					Element txnelement = (Element) txn.item(0);
					NamedNodeMap txnattributes = txnelement.getAttributes();
					for (int i = 0; i < txnattributes.getLength(); i++) 
					{
						Attr attr = (Attr) txnattributes.item(i);
						String attrName = attr.getNodeName();
						break;
					}*/
					log.info("Vehicle Details Updated In NPCI - RESULT "+result+" - ERRORCODE - "+errCode);
					resp.put("status", "SUCCESS");
				} 
				else 
				{
					log.info("Vehicle Details could not be Update - RESULT "+result+" - ERRORCODE - "+errCode);	
					resp.put("status", "SUCCESS");
				}
				resp.put("errorCode", errCode);
				resp.put("result", result);
				return resp;
			} 
			
			catch (Exception e) 
			{
				log.info(e.getMessage());
				e.printStackTrace();
			}
		}
		return resp;
	}
}
