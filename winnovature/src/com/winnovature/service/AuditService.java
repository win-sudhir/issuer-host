package com.winnovature.service;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.winnovature.dao.AuditDAO;
import com.winnovature.dto.ResponseDTO;

public class AuditService {
	static Logger log = Logger.getLogger(AuditService.class.getName());
	
	public static ResponseDTO getAuditTrailOnLoad(Connection conn)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		log.info("getAuditTrailOnLoad --> ");
		responseDTO.setData(AuditDAO.getAuditLogOnLoad(conn));
		responseDTO.setStatus(ResponseDTO.success);	
		return responseDTO;
	}
	public static ResponseDTO getAuditTrailByDate(Connection conn, String fromDate, String toDate)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		log.info("getAuditTrailByDate --> ");
		responseDTO.setData(AuditDAO.getAuditLogByDate(conn, fromDate, toDate));
		responseDTO.setStatus(ResponseDTO.success);	
		return responseDTO;
	}
}
