package com.netc.api;

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
import com.winnovature.dto.TagAllocationDTO;
import com.winnovature.service.TagRegistrationService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

@WebServlet("/tag/registration")
public class TagRegistration extends HttpServlet {
	static Logger log = Logger.getLogger(TagRegistration.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		ResponseDTO responseDTO = new ResponseDTO();
		Gson gson = new GsonBuilder().create();
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			finalResponse = gson.toJson(responseDTO);
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());

			log.info("TagAllocation.java ::: JSON REQUEST :: " + jsonRequest);
			
			TagAllocationDTO tagAllocationDTO = new Gson().fromJson(stringBuffer.toString(), TagAllocationDTO.class);
			
			String userId = request.getHeader("userId").toString();
			String authToken = request.getHeader("Authorization");
			
			responseDTO = TagRegistrationService.isUnregisterTag(tagAllocationDTO, userId, authToken, conn);

			if(responseDTO.getStatus().equals(ResponseDTO.failure)){
				return;
			}
			responseDTO = TagRegistrationService.isAllocated(tagAllocationDTO, userId, conn);
			if(responseDTO.getStatus().equals(ResponseDTO.failure)){
				return;
			}
			responseDTO = TagRegistrationService.processTagRegistration(tagAllocationDTO, userId, conn);
			if(responseDTO.getStatus().equals(ResponseDTO.failure)){
				return;
			}
		} catch (Exception e) {
			log.error("Exception in TagAllcation ::  "+e.getMessage());
		} finally {
			finalResponse = gson.toJson(responseDTO);
			log.info("*****************Response to tag/registration API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
