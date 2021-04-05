package com.winnovature.dispute.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dispute.dao.DisputeCloseDAO;
import com.winnovature.dispute.dao.DisputeServiceUtil;
import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.DisputeErrorCode;
import com.winnovature.validation.SessionValidation;

@WebServlet("/dispute/closedispute")
public class DisputeClose extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DisputeClose.class.getClass());
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		
		String finalResponse = null;
		Gson gson = new GsonBuilder().create();
		ResponseDTO responseDTO = new ResponseDTO();
		Connection conn = null;
		try {
			
			conn = DatabaseManager.getAutoCommitConnection();
			responseDTO = SessionValidation.validateSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			finalResponse = gson.toJson(responseDTO);
			if (responseDTO.getStatus().equals(ResponseDTO.success)) {
				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("jsonRequest " + jsonRequest);
				DisputeMasterDTO disputeMasterDTO = new Gson().fromJson(jsonRequest.toString(), DisputeMasterDTO.class);
				String requestType = jsonRequest.getString("requestType");
				
				if(("closeDispute").equalsIgnoreCase(requestType)) {
					responseDTO = new DisputeCloseDAO().closeDispute(disputeMasterDTO, conn);
					finalResponse = gson.toJson(responseDTO);
				}
				
				else if(("getTransactionToClose").equalsIgnoreCase(requestType)) {
					List<DisputeMasterDTO> data = DisputeServiceUtil.getDisputeSearchService().getTransactionToCloseByDate(disputeMasterDTO.getFromDate(), disputeMasterDTO.getToDate(), conn);
					responseDTO.setData(data);
					finalResponse = gson.toJson(responseDTO);
				}
				
				else {
					log.info("Invalid Request Type");
					// ResponseDTO resp = new ResponseDTO();
					responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0025.name());
					responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0025.getErrorMessage());
					responseDTO.setStatus(ResponseDTO.failure);
					finalResponse = gson.toJson(responseDTO);
				}

				
			}
			log.info("*****************Response to dispute/closedispute API()****************");
			out.write(finalResponse);
			log.info(finalResponse);

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}
}
