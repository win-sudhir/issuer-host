package com.winnovature.service;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.winnovature.dao.ReconDAO;
import com.winnovature.dto.ResponseDTO;

public class ReconService {
	static Logger log = Logger.getLogger(ReconService.class.getName());
	
	public static ResponseDTO getReconSummary(Connection conn)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		log.info("getReconSummary --> ");
		responseDTO.setReconSummary(ReconDAO.getNPCIReconSummaryOnLoad(conn));
		responseDTO.setStatus(ResponseDTO.success);	
		return responseDTO;
	}
	public static ResponseDTO getNPCIReconSummaryByDate(Connection conn, String fromDate, String toDate)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		log.info("getReconSummary --> ");
		responseDTO.setReconSummary(ReconDAO.getNPCIReconSummaryByDate(conn, fromDate, toDate));
		responseDTO.setStatus(ResponseDTO.success);	
		return responseDTO;
	}
}
