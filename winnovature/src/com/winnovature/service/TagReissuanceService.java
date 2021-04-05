package com.winnovature.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.winnovature.dao.TagReissuanceDAO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDetailsDTO;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.PropertyReader;
import com.winnovature.validation.TagErrorCode;

/**
 * @author Sudhir Khobragade
 *
 * 18-Feb-2021
 */

public class TagReissuanceService {
	static Logger log = Logger.getLogger(TagReissuanceService.class.getName());
	
	/*
	public static VehicleDetailsDTO getVehicleDetails(String vehicleNumber, Connection conn) {
		VehicleDetailsDTO vehicleDetailsDTO = new VehicleDetailsDTO();
		vehicleDetailsDTO = TagReissuanceDAO.getReissuanceTagDetails(vehicleNumber, conn);
		if(vehicleDetailsDTO==null) {
			responseDTO.setErrorCode(TagErrorCode.TAGBU0042.name());
			responseDTO.setMessage(TagErrorCode.TAGBU0042.getErrorMessage());
			responseDTO.setStatus(ResponseDTO.failure);
		}
		List<BarCodeDataDTO> data = TagReissuanceDAO.getBarcodeData(vehicleDetailsDTO.getTagClassId(), conn);
		vehicleDetailsDTO.setData(data);
		return vehicleDetailsDTO;
		
	}
	*/
	
	public static ResponseDTO reIssueTag(VehicleDetailsDTO vehicleDetailsDTO, Connection conn, String header) {
		String lowBalanceResponse = addTagInlowBalanceCall(vehicleDetailsDTO.getTagId());
		ResponseDTO responseDTO = new ResponseDTO();
		//lowBalanceResponse = "success";
		if(lowBalanceResponse==null) {
			responseDTO.setErrorCode(TagErrorCode.TAGBU0034.name());
			responseDTO.setMessage(TagErrorCode.TAGBU0034.getErrorMessage());
			responseDTO.setStatus(ResponseDTO.failure);
			return responseDTO;
		}
		//String vehicleNumber = vehicleDetailsDTO.getVehicleNumber();
		log.info(("Before Getting Barcode Details  :: " + vehicleDetailsDTO.toString()));
		if(lowBalanceResponse.equalsIgnoreCase("success")) {
			vehicleDetailsDTO = TagReissuanceDAO.getBarcodeTagDetails(vehicleDetailsDTO, conn);
		}
		
		//vehicleDetailsDTO.setVehicleNumber(vehicleNumber);
		log.info(("After Getting Barcode Details  :: " + vehicleDetailsDTO.toString()));
		String reqMngTagEntriesResp = addTagInNPCIMapper(vehicleDetailsDTO); 
		if(reqMngTagEntriesResp==null) {
			responseDTO.setErrorCode(TagErrorCode.TAGBU0034.name());
			responseDTO.setMessage(TagErrorCode.TAGBU0034.getErrorMessage());
			responseDTO.setStatus(ResponseDTO.failure);
			return responseDTO;
		}
		if(reqMngTagEntriesResp.equalsIgnoreCase("success")) {
			TagReissuanceDAO.updateReissuanceInventoty(vehicleDetailsDTO, conn);
			responseDTO.setErrorCode(TagErrorCode.TAGBU0044.name());
			responseDTO.setMessage(TagErrorCode.TAGBU0044.getErrorMessage());
			responseDTO.setStatus(ResponseDTO.success);
			return responseDTO;
		}
		responseDTO.setErrorCode(TagErrorCode.TAGBU0045.name());
		responseDTO.setMessage(TagErrorCode.TAGBU0045.getErrorMessage());
		responseDTO.setStatus(ResponseDTO.failure);
		return responseDTO;
	}
	
	private static String addTagInlowBalanceCall(String tagId) {
		String response = null;
		String ttype = "ReqMngException";
		String opid = "ADD";
		String seqNum = "1";
		String excCode = "03";
		String requestParams = "ttype=" + ttype + "&opid=" + opid + "&tagId=" + tagId + "&seqNum=" + seqNum
				+ "&excCode=" + excCode;
		String line = null;
		String callResponse = null;
		StringBuffer strBuffer = new StringBuffer();
		try {
			String manageTagURL = PropertyReader.getPropertyValue("manageTagURL");
			log.info(("manageTagURL  :: " + manageTagURL));
			URL url = new URL(manageTagURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			log.info("addInlowBalanceCall() :: Output Stream Open");
			wr.write(requestParams);
			wr.flush();
			log.info(("addInlowBalanceCall() :: Response Code : " + con.getResponseCode()));
			if (con.getResponseCode() == 200) {
				log.info(
						("TagReissuanceService.addInlowBalanceCall() :: Response Code " + con.getResponseCode() + " is HTTP OK...."));
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = br.readLine()) != null) {
					//log.info(("TagReissuanceService.addInlowBalanceCall() :: Line : " + line));
					strBuffer.append(line);
				}
				MemoryComponent.closeBufferedReader(br);
			}
			con.disconnect();
			callResponse = strBuffer.toString();
			wr.write(callResponse);
			MemoryComponent.closeOutputStreamWriter(wr);
			log.info(("TagReissuanceService.addInlowBalanceCall() :: Response : " + strBuffer));
			if (callResponse != null) {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(callResponse));
				Document doc = db.parse(is);
				NodeList respnode = doc.getElementsByTagName("Resp");
				Element respelement = (Element) respnode.item(0);
				NamedNodeMap respattributes = respelement.getAttributes();
				for (int i = 0; i < respattributes.getLength(); ++i) {
					Attr attr = (Attr) respattributes.item(i);
					String attrName = attr.getNodeName();
					if (attrName.equalsIgnoreCase("respCode")) {
						String respCode = attr.getNodeValue();
						log.info(("respCode : " + respCode));
						break;
					}
				}
				NodeList exception = doc.getElementsByTagName("Tag");
				Element element = (Element) exception.item(0);
				NamedNodeMap attributes = element.getAttributes();
				String errCode = null;
				String result = null;
				for (int j = 0; j < attributes.getLength(); ++j) {
					Attr attr2 = (Attr) attributes.item(j);
					String attrName2 = attr2.getNodeName();
					if (attrName2.equals("errCode")) {
						errCode = attr2.getNodeValue();
					}
					if (attrName2.equals("result")) {
						result = attr2.getNodeValue();
					}
				}
				if ((errCode.equals("000") || errCode.equals("240") || errCode.equals("155"))
						&& result.equalsIgnoreCase("SUCCESS")) {
					log.info("addInlowBalanceCall()  :: Result Success...");
					response = "success";
				} else {
					String msg = "-RESULT-" + result + "-ERRORCODE-" + errCode;
					log.info(("Response Result :: " + msg));
					response = msg;
				}
				log.info(("errCode : " + errCode));
				log.info(("result :" + result));
				log.info(("Root element of the response is :: " + doc.getDocumentElement().getNodeName()));
			} else {
				log.info("addInlowBalanceCall() :: Response is null ");
				response = "Fail-Response is null";
			}
		} catch (Exception e) {
			log.error("addInlowBalanceCall() :: Error Occurred : ", (Throwable) e);
			e.printStackTrace();
		}
		return response;
	}
	
	//unused
	public String addTagInNPCIMapper_(String tagId) {
		String response = null;
		String ttype = "ReqMngException";
		String opid = "ADD";
		String seqNum = "1";
		String excCode = "03";
		
		String requestParams = "ttype=" + ttype + "&opid=" + opid + "&tagId=" + tagId + "&seqNum=" + seqNum
				+ "&excCode=" + excCode;
		String line = null;
		String callResponse = null;
		StringBuffer strBuffer = new StringBuffer();
		try {
			String manageTagURL = PropertyReader.getPropertyValue("manageTagURL");
			log.info(("manageTagURL  :: " + manageTagURL));
			URL url = new URL(manageTagURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			log.info("addInlowBalanceCall() :: Output Stream Open");
			wr.write(requestParams);
			wr.flush();
			log.info(("addInlowBalanceCall() :: Response Code : " + con.getResponseCode()));
			if (con.getResponseCode() == 200) {
				log.info(
						("TagReissuanceService.addInlowBalanceCall() :: Response Code " + con.getResponseCode() + " is HTTP OK...."));
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = br.readLine()) != null) {
					//log.info(("TagReissuanceService.addInlowBalanceCall() :: Line : " + line));
					strBuffer.append(line);
				}
				MemoryComponent.closeBufferedReader(br);
			}
			con.disconnect();
			callResponse = strBuffer.toString();
			wr.write(callResponse);
			MemoryComponent.closeOutputStreamWriter(wr);
			log.info(("TagReissuanceService.addInlowBalanceCall() :: Response : " + strBuffer));
			if (callResponse != null) {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(callResponse));
				Document doc = db.parse(is);
				NodeList respnode = doc.getElementsByTagName("Resp");
				Element respelement = (Element) respnode.item(0);
				NamedNodeMap respattributes = respelement.getAttributes();
				for (int i = 0; i < respattributes.getLength(); ++i) {
					Attr attr = (Attr) respattributes.item(i);
					String attrName = attr.getNodeName();
					if (attrName.equalsIgnoreCase("respCode")) {
						String respCode = attr.getNodeValue();
						log.info(("respCode : " + respCode));
						break;
					}
				}
				NodeList exception = doc.getElementsByTagName("Tag");
				Element element = (Element) exception.item(0);
				NamedNodeMap attributes = element.getAttributes();
				String errCode = null;
				String result = null;
				for (int j = 0; j < attributes.getLength(); ++j) {
					Attr attr2 = (Attr) attributes.item(j);
					String attrName2 = attr2.getNodeName();
					if (attrName2.equals("errCode")) {
						errCode = attr2.getNodeValue();
					}
					if (attrName2.equals("result")) {
						result = attr2.getNodeValue();
					}
				}
				if ((errCode.equals("000") || errCode.equals("240") || errCode.equals("155"))
						&& result.equalsIgnoreCase("SUCCESS")) {
					log.info("addInlowBalanceCall()  :: Result Success...");
					response = "success";
				} else {
					String msg = "-RESULT-" + result + "-ERRORCODE-" + errCode;
					log.info(("Response Result :: " + msg));
					response = msg;
				}
				log.info(("errCode : " + errCode));
				log.info(("result :" + result));
				log.info(("Root element of the response is :: " + doc.getDocumentElement().getNodeName()));
			} else {
				log.info("addInlowBalanceCall() :: Response is null ");
				response = "Fail-Response is null";
			}
		} catch (Exception e) {
			log.error("addInlowBalanceCall() :: Error Occurred : ", (Throwable) e);
			e.printStackTrace();
		}
		return response;
	}
	
	public static String addTagInNPCIMapper(VehicleDetailsDTO vehicleDetailsDTO) {
		String line = null;
		StringBuffer registrationResp = new StringBuffer();
		//String response = null;
		String regResp = null;
		String sequenceNumber = null, txnId = null, response=null;
		String ttype = "ReqMngTagEntries";
		String opid = "ADD";
		String excCode = "00";
		String issueDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String manageTagURL = PropertyReader.getPropertyValue("manageTagURL");
		
		String postData = "ttype=" + ttype + "&opid=" + opid + "&tagId=" + vehicleDetailsDTO.getTagId() + "&tid=" + vehicleDetailsDTO.getTid() + "&issueDate="
				+ issueDate + "&excCode=" + excCode + "&vehicleClass=" + vehicleDetailsDTO.getVehicleClassId() + "&regNum=" + vehicleDetailsDTO.getVehicleNumber()
				+ "&comVehicle=" + vehicleDetailsDTO.getIsCommercial();
		
		log.info("manageTagURL :"+manageTagURL + " postData :: "+postData);
		
		try {
		URL url = new URL(manageTagURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.connect();
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		log.info("registrationApiCall() :: Output Stream Open");
		wr.write(postData);
		wr.flush();
		log.info(("NETCREPORT  :::java  ::  registrationApiCall() :: Response Code : " + con.getResponseCode()));
		if (con.getResponseCode() == 200) {
			log.info(("registrationApiCall() :: Response Code " + con.getResponseCode()
					+ " is HTTP OK...."));
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((line = br.readLine()) != null) {
				//log.info(("registrationApiCall() :: Line : " + line));
				registrationResp.append(line);
			}
			MemoryComponent.closeBufferedReader(br);
		}
		con.disconnect();
		regResp = registrationResp.toString();
		wr.write(regResp);
		MemoryComponent.closeOutputStreamWriter(wr);
		log.info(("registrationApiCall() :: Response : " + regResp));
		if (regResp != null) {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(regResp));
			Document doc = db.parse(is);
			NodeList respnode = doc.getElementsByTagName("Resp");
			Element respelement = (Element) respnode.item(0);
			NamedNodeMap respattributes = respelement.getAttributes();
			for (int i = 0; i < respattributes.getLength(); ++i) {
				Attr attr = (Attr) respattributes.item(i);
				String attrName = attr.getNodeName();
				if (attrName.equalsIgnoreCase("respCode")) {
					String respCode = attr.getNodeValue();
					log.info(("respCode : " + respCode));
					break;
				}
			}
			NodeList exception = doc.getElementsByTagName("Tag");
			Element element = (Element) exception.item(0);
			NamedNodeMap attributes = element.getAttributes();
			String errCode = null;
			String result = null;
			for (int j = 0; j < attributes.getLength(); ++j) {
				Attr attr2 = (Attr) attributes.item(j);
				String attrName2 = attr2.getNodeName();
				if (attrName2.equals("errCode")) {
					errCode = attr2.getNodeValue();
				}
				if (attrName2.equals("result")) {
					result = attr2.getNodeValue();
				}
				if (attrName2.equals("seqNum")) {
					sequenceNumber = attr2.getNodeValue();
				}
			}
			if ((errCode.equals("000") || errCode.equals("240")) && result.equalsIgnoreCase("SUCCESS")) {
				log.info("Result Success...");
				NodeList txn = doc.getElementsByTagName("Txn");
				Element txnelement = (Element) txn.item(0);
				NamedNodeMap txnattributes = txnelement.getAttributes();
				for (int k = 0; k < txnattributes.getLength(); ++k) {
					Attr attr3 = (Attr) txnattributes.item(k);
					String attrName3 = attr3.getNodeName();
					if (attrName3.equals("id")) {
						txnId = attr3.getNodeValue();
						log.info(("txnID : " + txnId));
					}
				}
				TagReissuanceDAO.insertChallan(vehicleDetailsDTO, txnId, sequenceNumber);
				log.info("Tag Re-issued successfully.");
				response = "success";
			} else {
				String msg = "-RESULT-" + result + "-ERRORCODE-" + errCode;
				log.info(("Response Result :: " + msg));
				response = msg;
			}
			log.info(("errCode : " + errCode));
			log.info(("result :" + result));
			log.info(("Root element of the response is :: "+ doc.getDocumentElement().getNodeName()));
		} else {
			log.info("Tag could not be registered. regResp is null.");
			response = "Fail-Response is null.";
		}
		}catch (Exception e) {
			log.info("Exception in tag-reissuance"+e.getMessage());
		}
		return response;
	}
}
