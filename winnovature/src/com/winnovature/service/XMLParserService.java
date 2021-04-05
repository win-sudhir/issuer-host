/**
 * 
 */
package com.winnovature.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagInfoDTO;
import com.winnovature.utils.XMLUtil;
import com.winnovature.validation.TagErrorCode;
import com.winnovature.xml.dto.Detail;
import com.winnovature.xml.dto.Resp;
import com.winnovature.xml.dto.RespDetails;
import com.winnovature.xml.dto.RespMngException;
import com.winnovature.xml.dto.Tag;
import com.winnovature.xml.dto.Vehicle;




public class XMLParserService {

	static Logger log = Logger.getLogger(XMLParserService.class.getClass());
	
	//Method not used
	//RespMngException parse the xml and response
	/*
	public ResponseDTO parseRespMngException(TagInfoDTO tagInfoDTO, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		
		if (tagInfoDTO.getTagId() == null || tagInfoDTO.getTagId().isEmpty() || tagInfoDTO.getExceptionCode() == null
				|| tagInfoDTO.getExceptionCode().isEmpty()) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0021.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0021.name());
			return responseDTO;
		}
		
		String respXML = new NPCICallService().callNPCI(tagInfoDTO);
		
		if(respXML==null || respXML.isEmpty()){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0034.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0034.name());
			return responseDTO;
		}
		//parsing exception using JAXB
		RespMngException respMngException = (RespMngException) XMLUtil.xmlUnmarshal(respXML, RespMngException.class);
		Tag tag = respMngException.getTxn().getResp().getTag();
		Resp resp = respMngException.getTxn().getResp();
		
		if(resp.getResult().equalsIgnoreCase("FAILURE")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0038.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0038.name());
			return responseDTO;
		}
		if(tag.getResult().equalsIgnoreCase("FAILURE")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0039.getErrorMessage()+tag.getErrCode());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0039.name());
			return responseDTO;
		}
		if(tag.getErrCode().equals("000")){
			JSONObject result = new TagDetailsDAO().tagAddRemoveException(tagInfoDTO, conn);
			if (result.getInt("status") != -1 && result.getInt("status") != 2) {
				responseDTO.setStatus(ResponseDTO.success);
				responseDTO.setMessage(result.getString("resp_msg"));
				responseDTO.setErrorCode(TagErrorCode.TAGBU0032.name());
				return responseDTO;
			}
		}
		return responseDTO;
	
	}
	*/
	
	public ResponseDTO parseRespMngException(TagInfoDTO tagInfoDTO){
		ResponseDTO responseDTO = new ResponseDTO();
		
		String respXML = new NPCICallService().callNPCI(tagInfoDTO);
		
		if(respXML==null || respXML.isEmpty()){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0034.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0034.name());
			return responseDTO;
		}
		//parsing exception using JAXB
		RespMngException respMngException = (RespMngException) XMLUtil.xmlUnmarshal(respXML, RespMngException.class);
		Tag tag = respMngException.getTxn().getResp().getTag();
		Resp resp = respMngException.getTxn().getResp();
		
		if(resp.getResult().equalsIgnoreCase("FAILURE")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0038.getErrorMessage()+" ERRORCODE : "+tag.getErrCode());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0038.name());
			return responseDTO;
		}
		if(tag.getResult().equalsIgnoreCase("FAILURE")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0039.getErrorMessage()+tag.getErrCode());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0039.name());
			return responseDTO;
		}
		
		if(tag.getErrCode().equals("000")){
			responseDTO.setStatus(ResponseDTO.success);
			return responseDTO;
		}
		return responseDTO;
	}
	
	public ResponseDTO parseRespDetails(TagInfoDTO tagInfoDTO){
		ResponseDTO responseDTO = new ResponseDTO();
		
		String respXML = new NPCICallService().callNPCIForSearch(tagInfoDTO);
		if(respXML==null || respXML.isEmpty()){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0034.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0034.name());
			return responseDTO;
		}
		//parsing exception using JAXB
		RespDetails respDetails = (RespDetails) XMLUtil.xmlUnmarshal(respXML, RespDetails.class);
		
		Resp resp = respDetails.getTxn().getResp();
		Vehicle vehicle = respDetails.getTxn().getResp().getVehicle();
		
		if(resp.getResult().equalsIgnoreCase("FAILURE")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0040.getErrorMessage()+" ERRORCODE : "+vehicle.getErrCode());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0040.name());
			return responseDTO;
		}
		
		if(!vehicle.getErrCode().equals("000")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0040.getErrorMessage()+" ERRORCODE : "+vehicle.getErrCode());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0040.name());
			return responseDTO;
		}
		
		if(vehicle.getErrCode().equals("000"))
		{
			List<Detail> data = respDetails.getTxn().getResp().getVehicle().getVehicleDetails().getDetailList();
			List<TagInfoDTO> lst = new ArrayList<TagInfoDTO>();
			TagInfoDTO info = new TagInfoDTO();
			/*info.setTagId("TAGID");
			info.setTid("TID");
			info.setVehicleNumber("VEHICLENUMBER");
			info.setVehicleClass("VEHICLECLASS");
			info.setTagStatus("TAGSTATUS");
			info.setIssueDate("ISSUEDATE");
			info.setExceptionCode("EXCEPTIONCODE");
			info.setCommVehicle("COMVEHICLE");
			info.setBankId("BANKID");
			lst.add(info);*/
			Map<String, String> map = new HashMap<String, String>();
			for(Detail detail : data){
				log.info("NAME : "+detail.getName() + " \tVALUE : "+detail.getValue());
				map.put(detail.getName(), detail.getValue());
			} 
			//System.out.println(map.size());
			//System.out.println(map.get("TAGID"));
			//System.out.println(map.get("BANKID"));
			info = new TagInfoDTO();
			info.setTagId(map.get("TAGID"));
			info.setTid(map.get("TID"));
			info.setVehicleNumber(map.get("REGNUMBER"));
			info.setVehicleClass(map.get("VEHICLECLASS"));
			info.setTagStatus(map.get("TAGSTATUS"));
			info.setIssueDate(map.get("ISSUEDATE"));
			info.setExceptionCode(map.get("EXCCODE"));
			info.setCommVehicle(map.get("COMVEHICLE"));
			info.setBankId(map.get("BANKID"));
			lst.add(info);
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setErrorCode(TagErrorCode.TAGBU000.name());
			responseDTO.setMessage(TagErrorCode.TAGBU000.getErrorMessage());
			responseDTO.setData(lst);
		}
		//following gives only key value par so above changes converts in list
		/*if(vehicle.getErrCode().equals("000"))
		{
			List<Detail> data = respDetails.getTxn().getResp().getVehicle().getVehicleDetails().getDetailList();
			//List<TagInfoDTO> info = new ArrayList<TagInfoDTO>();
			for(Detail detail : data){
				log.info("NAME : "+detail.getName() + " VALUE : "+detail.getValue());
				//TagInfoDTO in = new TagInfoDTO();
			}
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setErrorCode(TagErrorCode.TAGBU000.name());
			responseDTO.setMessage(TagErrorCode.TAGBU000.getErrorMessage());
			responseDTO.setData(data);
		}*/
		
		return responseDTO;
	}
	
}
