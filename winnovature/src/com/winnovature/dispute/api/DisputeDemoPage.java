package com.winnovature.dispute.api;

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

@WebServlet("/dispute/demodispute")
public class DisputeDemoPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DisputeDemoPage.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String getDetails = null;
		Connection conn = null;
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
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
			if(jsonRequest.getString("requestType").equalsIgnoreCase("toRaise")) {
				getDetails = "{\"status\":\"1\",\"data\":[{\"tagId\":\"34161FA8202F424203ED4388\",\"txnTime\":"+date+",\"txnAmount\":\"10\",\"tid\":\"E404041334B1D0112265CB77\",\"txnType\":\"DEBIT\",\"txnId\":\"21116182176400\",\"originalTxnId\":\"20210426183921000901\",\"vehicleNumber\":\"MH08BA0808\"}]}";
			}else if(jsonRequest.getString("requestType").equalsIgnoreCase("toApprove")) {
				getDetails = "{\r\n"
						+ "	\"id\":\"87\",\"tollPlazaId\":\"100101\",\"createdBy\":\"demouser\",\"createdOn\":"+date+",\"reasonCode\":\"3014\",\"functionCode\":\"450\",\"tagId\":\"34161FA8202F424203ED4388\",\"txnTime\":"+date+",\"issuerId\":\"EQTB\",\"acquirerId\":\"500002@iin.npci\",\"txnAmount\":\"10.00\",\"fullPartialIndicator\":\"F\",\"tid\":\"E404041334B1D0112265CB77\",\"mmt\":\"demo dispute\",\"txnType\":\"CREDIT\",\"originalTxnId\":\"20210426183921000901\",\"comment\":\"demo test\",\"referenceId\":\"D2190380230295\"\r\n"
						+ "}";
			}
			else if(jsonRequest.getString("requestType").equalsIgnoreCase("status")) {
				getDetails = "{\"status\":\"1\",\"data\":[{\"id\":\"102\",\"tollPlazaId\":\"100101\",\"status\":\"CLOSED\",\"actualTxnAmount\":\"10.00\",\"createdBy\":\"demouser\",\"createdOn\":"+date+",\"reasonCode\":\"3014\",\"functionCode\":\"405\",\"tagId\":\"34161FA8202F424203ED4388\",\"txnTime\":"+date+",\"issuerId\":\"101010\",\"acquirerId\":\"500002@iin.npci\",\"txnAmount\":\"10.00\",\"fullPartialIndicator\":\"F\",\"tid\":\"E404041334B1D0112265CB77\",\"mmt\":\"Chargeback raised partial through file\",\"txnType\":\"DEBIT\",\"originalTxnId\":\"'20210426183921000901\",\"comment\":\"DISPUTEADJ FILE TXN NOT FOUND\",\"referenceId\":\"D2190380230295\",\"approvalStatus\":\"APPROVED\"}]}";
			}
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to GET dispute/demodispute API()****************");
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
			if(jsonRequest.getString("requestType").equalsIgnoreCase("raiseDispute")) {
				resp = "{\"status\":\"1\",\"message\":\"Dispute raised successfully, your reference number is: D2190380230295\"}";
			}else if(jsonRequest.getString("requestType").equalsIgnoreCase("approveDispute")) {
				resp = "{\"status\":\"1\",\"message\":\"Dispute approved successfully.\"}";
			}

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to POST dispute/demodispute API()****************");
			out.write(resp);
			log.info(resp);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}
}
