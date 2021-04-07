package com.winnovature.recon.api;

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
import com.winnovature.dao.CheckSession;
import com.winnovature.dao.ReconDAO;
import com.winnovature.dto.Recon861DTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.ReconErrorCode;
import com.winnovature.validation.ReconValidation;
import com.winnovature.validation.SessionValidation;


@WebServlet("/recon/recon861")
public class Reconciliation861 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	static Logger log = Logger.getLogger(Reconciliation861.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		String userId = null;
		String authToken = null;
		String finalResponse = null;
		Gson gson = new GsonBuilder().create();
		ResponseDTO responseDTO = new ResponseDTO();
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			//responseDTO.setStatus("1");
			//responseDTO = SessionValidation.validateSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			//finalResponse = gson.toJson(responseDTO);
			//if(responseDTO.getStatus().equals(ResponseDTO.success)){
				
				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("jsonRequest "+jsonRequest);
				// jsonResponse = new JSONObject();

				Recon861DTO recon861DTO = new Gson().fromJson(jsonRequest.toString(), Recon861DTO.class);
				//log.info("reconDTO "+reconDTO.toString());
				if (recon861DTO.getRequestType().equalsIgnoreCase("postBulktxns")) {

					responseDTO = ReconValidation.reconValidation(userId, authToken, recon861DTO, conn);
					if (responseDTO.getStatus().equals(ResponseDTO.success)) {
						responseDTO = new ReconDAO().processReconciliation(recon861DTO, conn);
					}
					finalResponse = gson.toJson(responseDTO);
					// log.info("finalResponse : "+finalResponse);
					// jsonResponse = new JSONObject(finalResponse);
				} 
					
					
				else if (recon861DTO.getRequestType().equalsIgnoreCase("dateList")) {
					responseDTO = new ReconDAO().getDateList(conn);
					finalResponse = gson.toJson(responseDTO);
				}
				
				else if (recon861DTO.getRequestType().equalsIgnoreCase("skippedCycleList")) {
					responseDTO = new ReconDAO().getSkippedDateList(conn);
					finalResponse = gson.toJson(responseDTO);
				}
				
				else if (recon861DTO.getRequestType().equalsIgnoreCase("revokedCycleList")) {
					responseDTO = new ReconDAO().getRevokedDateList(conn);
					finalResponse = gson.toJson(responseDTO);
				}

				else if (recon861DTO.getRequestType().equalsIgnoreCase("deleteSkippedDate")) {
					responseDTO = new ReconDAO().deleteSkippedCycle(recon861DTO, conn);
					finalResponse = gson.toJson(responseDTO);
				}
				//
				else if (recon861DTO.getRequestType().equalsIgnoreCase("insertCycle")) {
					responseDTO = new ReconDAO().addSkippedCycle(recon861DTO, conn);
					finalResponse = gson.toJson(responseDTO);
					
				} else {
					log.info("Invalid Request Type");
					//ResponseDTO resp = new ResponseDTO();
					responseDTO.setErrorCode(ReconErrorCode.RECONBU0016.name());
					responseDTO.setMessage(ReconErrorCode.RECONBU0016.getErrorMessage());
					responseDTO.setStatus(ResponseDTO.failure);
					finalResponse = gson.toJson(responseDTO);
				}

			//} 
			
			
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to ReconManagement API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
