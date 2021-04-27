package com.winnovature.dispute.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.winnovature.utils.PropertyReader;

@WebServlet("/dispute/downloadegcs")
public class DownloadEGCSFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DownloadEGCSFile.class.getName());
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = true;//CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
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
				rows.add("Tag_ID,Function_Code,Txn_Time,Txn_Id,Issuer_ID,Acquirer_ID,Txn_Amount,Reason_Code,Full_Partial_Indicator,Toll_Plaza_Id,TID,MMT,Internal Tracking Number");
				rows.add("\n");
				rows.add("34161FA8202F424203ED4388,450,"+getDayMinus1Date()+",'21116182176400,506010,500002,1000,3001,F,100101,E404041334B1D0112265CB77,Customer has not avail the NETC Service,D1617610096113");
				log.info("DISPUTE---->Bulk EGCS file downloadCSV()");

			} finally {
				Iterator<String> iter = rows.iterator();
				while (iter.hasNext()) {
					String outputString = (String) iter.next();
					response.getOutputStream().print(outputString);
				}
			}

			log.info("*****************Response to POST dispute/downloadegcs API()****************");

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			DatabaseManager.commitConnection(conn);
		}

	}

	public static String getFileName() {
		String bankParticipantId = PropertyReader.getPropertyValue("bankParticipantId");
		String date = DateUtility.getJulianDateOfDate("yyDDD"); //new SimpleDateFormat("yyDDD").format(new Date());
		String fileName = bankParticipantId + date + "00" + ".csv";
		return fileName;
	}

	public static String getDayMinus1Date() {
		/*Calendar calendar = Calendar.getInstance();
	    Date today = calendar.getTime();
	    System.out.println("Today's Date: " + today);
	    calendar.add(Calendar.DAY_OF_YEAR, -1);
	    Date datedays = calendar.getTime();*/
	    String dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
		return dateFormat;
	}
	public static void main(String[] args) {
		System.out.println(getFileName());
	}
}
