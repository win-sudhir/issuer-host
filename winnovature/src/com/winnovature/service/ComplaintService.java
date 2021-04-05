package com.winnovature.service;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.dispute.dao.ComplaintDAO;
import com.winnovature.dto.ComplaintDTO;
import com.winnovature.dto.ComplaintDTO.ComplaintList;
import com.winnovature.dto.ResponseDTO;

public class ComplaintService {
	static Logger log = Logger.getLogger(ComplaintService.class.getName());
	ComplaintDAO complaintDAO = new ComplaintDAO();
	ResponseDTO responseDTO = new ResponseDTO();
	public ResponseDTO getCompliantList(String userId, Connection conn) {
		List<ComplaintList> data = ComplaintDAO.getCompliantList(userId, conn);
		responseDTO.setData(data);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	public ResponseDTO addCompliant(ComplaintDTO complaintDTO, String userId, Connection conn) {
		String response = complaintDAO.addComplaint(complaintDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage("Compliant can not added.");
			responseDTO.setErrorCode("COMPLBU002");
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage("Compliant added successfully.");
		responseDTO.setErrorCode("COMPLBU001");
		return responseDTO;
	}
	
	public ResponseDTO updateCompliantStatus(ComplaintDTO complaintDTO, String userId, Connection conn) {
		String response = complaintDAO.updateComplaintStatus(complaintDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage("Compliant can not close.");
			responseDTO.setErrorCode("COMPLBU003");
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage("Compliant closed successfully.");
		responseDTO.setErrorCode("COMPLBU004");
		return responseDTO;
	}
	
	public ResponseDTO getCompliantReport(String fromDate, String toDate, String userId, Connection conn) {
		fromDate = fromDate+" 00:00:00";
		toDate = toDate+" 23:59:59";
		List<ComplaintDTO> data = ComplaintDAO.getComplaintReport(userId, fromDate, toDate, conn);
		responseDTO.setData(data);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
}
