package com.winnovature.dispute.api;

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
import com.winnovature.dto.ComplaintDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.ComplaintService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.DisputeErrorCode;
import com.winnovature.validation.SessionValidation;


@WebServlet("/complaint/managecomplaint")
public class Complaint extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(Complaint.class.getName());  
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
			
			ComplaintService complaintService = new ComplaintService();
			if (responseDTO.getStatus().equals(ResponseDTO.success)) {

				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("jsonRequest " + jsonRequest);
				ComplaintDTO complaintDTO = new Gson().fromJson(jsonRequest.toString(), ComplaintDTO.class);
				String requestType = jsonRequest.getString("requestType");
				log.info("requestType " + requestType);
				//customers only
				if (("getComplaintList").equalsIgnoreCase(requestType)) {
					responseDTO=complaintService.getCompliantList(request.getHeader("userId"), conn);
					//finalResponse = gson.toJson(responseDTO);
				}
				else if(("addComplaint").equalsIgnoreCase(requestType)) {
					responseDTO=complaintService.addCompliant(complaintDTO, request.getHeader("userId"), conn);
					//finalResponse = gson.toJson(responseDTO);
				} 
				else if(("closeComplaint").equalsIgnoreCase(requestType)) {
					responseDTO=complaintService.updateCompliantStatus(complaintDTO, request.getHeader("userId"), conn);
					//finalResponse = gson.toJson(responseDTO);
				} 
				else if(("getComplaintReport").equalsIgnoreCase(requestType)) {
					responseDTO=complaintService.getCompliantReport(jsonRequest.getString("fromDate"), jsonRequest.getString("toDate"), request.getHeader("userId"), conn);
					//finalResponse = gson.toJson(responseDTO);
				} 
				
				
				else {
					log.info("Invalid Request Type");
					// ResponseDTO resp = new ResponseDTO();
					responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0025.name());
					responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0025.getErrorMessage());
					responseDTO.setStatus(ResponseDTO.failure);
					//finalResponse = gson.toJson(responseDTO);
				}

			}
			finalResponse = gson.toJson(responseDTO);
			log.info("*****************Response to complaint/managecomplaint API()****************");

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

}
