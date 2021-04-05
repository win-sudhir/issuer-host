/**
 * 
 */
package com.winnovature.validation;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.winnovature.dao.TagDetailsDAO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagInfoDTO;



public class TagValidation {
	static Logger log = Logger.getLogger(TagValidation.class.getClass());
	
	public ResponseDTO validateSearchRequest(TagInfoDTO tagInfoDTO){
		ResponseDTO responseDTO = new ResponseDTO();
		if(tagInfoDTO==null){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0021.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0021.name());
			return responseDTO;
		}
		if((tagInfoDTO.getTagId()==null && tagInfoDTO.getTid()==null && tagInfoDTO.getVehicleNumber()==null) || 
				(tagInfoDTO.getTagId().isEmpty() && tagInfoDTO.getTid().isEmpty() && tagInfoDTO.getVehicleNumber().isEmpty()) ){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0023.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0023.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	public ResponseDTO validateAddSearchRequest(TagInfoDTO tagInfoDTO, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		if(tagInfoDTO==null){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0021.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0021.name());
			return responseDTO;
		}
		if((tagInfoDTO.getTagId()==null && tagInfoDTO.getTid()==null && tagInfoDTO.getVehicleNumber()==null) || 
				(tagInfoDTO.getTagId().isEmpty() && tagInfoDTO.getTid().isEmpty() && tagInfoDTO.getVehicleNumber().isEmpty()) ){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0023.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0023.name());
			return responseDTO;
		}
		if((tagInfoDTO.getExceptionCode()==null || tagInfoDTO.getExceptionCode().isEmpty()) ){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0024.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0024.name());
			return responseDTO;
		}
		if(TagDetailsDAO.checkTagInException(tagInfoDTO, conn)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0025.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0025.name());
			return responseDTO;
		}
		
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
}
