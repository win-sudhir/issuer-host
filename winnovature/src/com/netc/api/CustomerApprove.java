
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
import com.winnovature.service.CustomerService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

@WebServlet("/customer/approve")
public class CustomerApprove extends HttpServlet {
	static Logger log = Logger.getLogger(CustomerApprove.class.getName());

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

			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("REQUEST :: " + jsonRequest);
			String customerId = jsonRequest.getString("customerId");
			responseDTO = CustomerService.approveCustomer(conn, customerId, request.getHeader("userId"));
			finalResponse = gson.toJson(responseDTO);
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to /customer/approve API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

}
