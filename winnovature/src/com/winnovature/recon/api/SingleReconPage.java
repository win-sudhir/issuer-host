package com.winnovature.recon.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.winnovature.dao.CheckSession;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtility;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/recon/singlereconpage")
public class SingleReconPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(SingleReconPage.class.getName());
	
	String dateFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
	String dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	String dateFormat3 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String reconsummary = null;
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			if (!checkSession) {
				resp.setStatus(403);
				return;
			}
		reconsummary = "{\"status\":\"1\",\"message\":\"File uploaded successfully.\"\r\n"
				+ "\"reconSummary\":[{\"fileName\":"+getFileName()+",\"reconDate\":"+dateFormat3+",\"reconCycle\":\"1\",\"totalFileRecord\":\"1\",\"totalMatchRecord\":\"1\",\"totalMismatchRecord\":\"0\",\"alreadyRecon\":\"0\",\"deemedAccepted\":\"0\",\"remark\":\"Reconcilled cycle 1 of "+dateFormat3+"\",\"status\":\"RECON SUCCESSFUL\"}]\r\n"
				+ "}";
		}
		catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		}
		finally {
			log.info("*****************Response to GET recon/singlereconpage API()****************");
			out.write(reconsummary);
			log.info(reconsummary);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			response.setContentType("text/csv");
			String fileName = getFileName();
			ArrayList<String> rows = new ArrayList<String>();
			
			/*
			 * 30-03-2021 17:06:60 2021-03-30 17:06:53 30-03-2021 11:46:54 2021-03-30
			 * 17:06:55 2021-03-30
			 */
			try {
				response.setHeader("Content-disposition", "attachment; " + "filename=" + fileName);
				rows.add(
						"\"Transaction Sequence Number\",\"Transaction ID\",\"Message ID\",\"Note\",\"Reference ID\",\"Reference URL\",\"Transaction Date and Time\",\"Transaction Type\",\"Original Transaction ID\",\"Tag ID\",\"TID\",\"AVC\",\"Wim\",\"Merchant Id\",\"Merchant Type\",\"Sub Merchant Type\",\"Lane Id\",\"Lane Direction\",\"Lane Reader ID\",\"Parking Floor\",\"Parking Zone\",\"Parking Slot\",\"Parking Reader ID\",\"Reader Read Date and Time\",\"Signature Data\",\"Signature Authentication\",\"EPC Verified\",\"Proc Restriction Res\",\"Vehicle Auth\",\"Public Key CVV\",\"Reader Transaction Counter\",\"Reader Transaction Status\",\"Payer Address\",\"Issuer ID\",\"Payer Code\",\"Payer name\",\"Payer Type\",\"Transaction Amount\",\"Currency Code\",\"Payee Address\",\"Acquirer ID\",\"Payee Code\",\"Payee name\",\"Payee Type\",\"Response Code\",\"Transaction Status\",\"Approval Number\",\"Payee Error Code\",\"Settled Amount\",\"Settled Currency\",\"Account Type\",\"Available Balance\",\"Ledger Balance\",\"Account Number\",\"Customer Name\",\"Initiated By\",\"Initiated Time\",\"Last Updated By\",\"Last Updated Time\",\"Vehicle Registration number\",\"Vehicle Class\",\"Vehicle Type\",\"Tag status\",\"Tag Issue Date\"");
				rows.add("\n");
				rows.add("d8601ae0-bb08-4bc4-95c7-17210290d9fa,21116182176400,EQTIDS14,,,," + dateFormat1
						+ ",DEBIT,,34161FA8202F424203ED4388,E404041334B1D0112265CB77,VC4,,100101,Toll,State,123,N,12,,,,,"
						+ dateFormat2
						+ ",,VALID,NETC TAG,,UNKNO,,1234,SUCCESS,34161FA8202F424203ED4388@506010.iin.npci,607799,,,PERSON,10.00,INR,500002@iin.npci,506010,,,MERCHANT,00,01,1234,000,10.00,INR,,0.00,0.00,,,EQTX,"
						+ dateFormat1 + ",EQTI," + dateFormat2 + ",MH08BA0808,VC4,F,A," + dateFormat3 + "");
				log.info("RECON---->downloadCSV()");

			} finally {
				Iterator<String> iter = rows.iterator();
				while (iter.hasNext()) {
					String outputString = (String) iter.next();
					response.getOutputStream().print(outputString);
				}
			}

			log.info("*****************Response to POST recon/singlereconpage API()****************");

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			DatabaseManager.commitConnection(conn);
		}

	}

	public static String getFileName() {
		String bankParticipantId = "861EQTB183000121";
		String date = DateUtility.getJulianDateOfDate("DDD");
		String fileName = bankParticipantId + date + ".csv";
		return fileName;
	}

	public static void main(String[] args) {
		System.out.println(getFileName());
	}
}
