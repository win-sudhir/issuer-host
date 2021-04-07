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
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagInfoDTO;
import com.winnovature.service.XMLParserService;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.ReconErrorCode;
import com.winnovature.validation.SessionValidation;
import com.winnovature.validation.TagValidation;




@WebServlet("/tag/npcirequestdetails")
public class RequestDetailsAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(RequestDetailsAction.class.getClass());
	
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
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			//responseDTO = SessionValidation.validateSession(request.getHeader("userId"),request.getHeader("Authorization"), conn);

			//finalResponse = gson.toJson(responseDTO);
			
			//if(responseDTO.getStatus().equals(ResponseDTO.success)) {

				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("jsonRequest " + jsonRequest);

				
				TagInfoDTO tagInfoDTO = new Gson().fromJson(jsonRequest.toString(), TagInfoDTO.class);
				String requestType = jsonRequest.getString("requestType");

				if (("SEARCH").equalsIgnoreCase(requestType)) {
					responseDTO = new TagValidation().validateSearchRequest(tagInfoDTO);
					if (responseDTO.getStatus().equals((ResponseDTO.success))) {
						/*List<TagInfoDTO> data = new RequestDetailService().getRequestDetails(tagInfoDTO);
						responseDTO.setData(data);*/
						//OLD WAY BELOW METHOD
						//responseDTO = new RequestDetailService().getRequestDetail(tagInfoDTO);
						//NEW WAY
						responseDTO = new XMLParserService().parseRespDetails(tagInfoDTO);
					}
					finalResponse = gson.toJson(responseDTO);
				} else {
					log.info("Invalid Request Type");
					responseDTO.setErrorCode(ReconErrorCode.RECONBU0016.name());
					responseDTO.setMessage(ReconErrorCode.RECONBU0016.getErrorMessage());
					responseDTO.setStatus(ResponseDTO.failure);
					finalResponse = gson.toJson(responseDTO);
				}

			//}

			log.info("*****************Response to /tag/npcirequestdetails API()****************");
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