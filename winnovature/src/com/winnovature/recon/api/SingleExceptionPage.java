package com.winnovature.recon.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.CheckSession;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

@WebServlet("/tag/demoexception")
public class SingleExceptionPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(SingleExceptionPage.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String getDetails = null;
		Connection conn = null;
		String npciUpdatedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			StringBuffer stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			JSONObject jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("REQUEST :: " + jsonRequest);
			if(jsonRequest.getString("requestType").equalsIgnoreCase("toAdd")) {
				getDetails = "{\r\n"
						+ "	\"tagId\":\"34161FA8202F424203ED4388\",\r\n"
						+ "	\"tid\":\"E404041334B1D0112265CB77\",\r\n"
						+ "	\"vehicleNumber\":\"MH02GH4645\",\r\n"
						+ "	\"exceptionCode\":\"03\"\r\n"
						+ "}";
			}else if(jsonRequest.getString("requestType").equalsIgnoreCase("toRemove")) {
				getDetails = "{\r\n"
						+ "	\"id\":\"15\",\r\n"
						+ "	\"tagId\":\"34161FA8202F424203ED4388\",\r\n"
						+ "	\"tid\":\"E404041334B1D0112265CB77\",\r\n"
						+ "	\"vehicleNumber\":\"MH02GH4645\",\r\n"
						+ "	\"exceptionCode\":\"03\",\r\n"
						+ "	\"messageId\":\"msg_03\",\r\n"
						+ "	\"npciUpdatedDate\":"+npciUpdatedDateTime+"\r\n"
						+ "}";
			}
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to GET tag/demoexception API()****************");
			out.write(getDetails);
			log.info(getDetails);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String resp = null;
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			StringBuffer stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			JSONObject jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("REQUEST :: " + jsonRequest);
			if(jsonRequest.getString("requestType").equalsIgnoreCase("ADD")) {
				resp = "{\"status\":\"1\",\"message\":\"Tad added in NPCI excetpion list successfully.\"}";
			}else if(jsonRequest.getString("requestType").equalsIgnoreCase("REMOVE")) {
				resp = "{\"status\":\"1\",\"message\":\"Tad removed from NPCI excetpion list successfully.\"}";
			}

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to POST tag/demoexception API()****************");
			out.write(resp);
			log.info(resp);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}
}
