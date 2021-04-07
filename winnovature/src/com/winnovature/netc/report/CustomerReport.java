package com.winnovature.netc.report;

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
import com.winnovature.dao.NETCReportsDAO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.SessionValidation;

@WebServlet("/report/customer")
public class CustomerReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(CustomerReport.class.getName());

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
			String userId = request.getHeader("userId");
			//finalResponse = gson.toJson(responseDTO);
			responseDTO.setStatus("1");
			if (responseDTO.getStatus().equals(ResponseDTO.success)) {
				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("REQUEST :: " + jsonRequest);
				
				String fromDate = jsonRequest.getString("fromDate");
				//fromDate = fromDate+" 00:00:00";
				log.info("fromDate" + fromDate);
				String toDate = jsonRequest.getString("toDate");
				//toDate = toDate+" 23:59:59";
				log.info("toDate" + toDate);
				/*
				String ReportType = jsonRequest.getString("ReportType");
				log.info("ReportType" + ReportType);
				// String Type = js.getString("type");
				String loginuserId = "admin";// request.getHeader("userId").toString();
				log.info("loginuserId" + loginuserId);
				String txntype = jsonRequest.getString("txntype");
				log.info("txntype" + txntype);
				String type = jsonRequest.getString("type");
				log.info("type" + type);
				String operation = jsonRequest.getString("operation");
				log.info("operation" + operation);
				*/
				finalResponse = new NETCReportsDAO().getCUSTOMERReport(fromDate, toDate, userId, conn);
			}
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to /report/customer API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}
}