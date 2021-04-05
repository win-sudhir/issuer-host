package com.winnovature.dispute.api;
/**
 * This api is used to raise the dispute 
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dispute.dao.DisputeRaiseDAO;
import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.DisputeErrorCode;
import com.winnovature.validation.DisputeValidation;
import com.winnovature.validation.SessionValidation;

@WebServlet("/dispute/raisedispute")
public class DisputeRaise extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DisputeRaise.class.getClass());
	
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
				if("raiseDispute".equalsIgnoreCase(requestType)){
					responseDTO = new DisputeValidation().validateRaiseDisputeRequest(disputeMasterDTO);
					if(responseDTO.getStatus().equals(ResponseDTO.success)){
						responseDTO = new DisputeRaiseDAO().addDisputeInfo(disputeMasterDTO, conn, request.getHeader("userId"));
					}
					finalResponse = gson.toJson(responseDTO);
				}
				else if(("approveDisputeTransaction").equalsIgnoreCase(requestType)) {
					responseDTO = new DisputeRaiseDAO().approveDispute(disputeMasterDTO, conn, request.getHeader("userId"));
					finalResponse = gson.toJson(responseDTO);
				}
				
				else if(("rejectDisputeTransaction").equalsIgnoreCase(requestType)) {
					responseDTO = new DisputeRaiseDAO().rejectDispute(disputeMasterDTO, conn, request.getHeader("userId"));
					finalResponse = gson.toJson(responseDTO);
				}
				
				else if(("updateDisputeTransaction").equalsIgnoreCase(requestType)) {
					responseDTO = new DisputeRaiseDAO().updateDisputeTransaction(disputeMasterDTO, conn, request.getHeader("userId"));
					finalResponse = gson.toJson(responseDTO);
				}
				
				
				
				else{
					log.info("Invalid Request Type");
					responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0025.name());
					responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0025.getErrorMessage());
					responseDTO.setStatus(ResponseDTO.failure);
					finalResponse = gson.toJson(responseDTO);
				}
				//TODO write api to add dispute
			}
			log.info("*****************Response to DisputeRaiseAction API()****************");
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
