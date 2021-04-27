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
import com.winnovature.utils.MemoryComponent;

@WebServlet("/dispute/alldisputefile")
public class DownloadAllDisputeFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DownloadAllDisputeFile.class.getName());
	
	String dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());//27-Apr-18
	//String dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//26-Apr-18
	String dateFormat3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
	//String dateFormat3 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//22-04-2018 12:27:44,22-04-2018 11:56:58,23-Apr-18
	
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String uploadAllDisputeResponse = null;
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			if (!checkSession) {
				resp.setStatus(403);
				return;
			}
		uploadAllDisputeResponse = "{\"status\":\"1\",\"message\":\"All Dispute file uploaded successfully.\"}";
		}
		catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		}
		finally {
			log.info("*****************Response to GET dispute/alldisputefile API()****************");
			out.write(uploadAllDisputeResponse);
			log.info(uploadAllDisputeResponse);
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
				rows.add("Report Date,Dispute Raise Date,Dispute Raised Settlement Date,Case Number,FunctionCode,Function Code and Description,Transaction Sequence Number,Tag ID,TID,Transaction Date and Time,Reader Read Date and Time,Transaction Settlement Date,Transaction Amount,Settlement Amount,Settlement Currency Code,Note,Transaction ID,Transaction Type,Merchant ID,Lane_Id,Merchant type,Sub Merchant Type,Transaction Status,TAG Status,AVC,WIM,Originator Point,Acquirer ID,Transaction Originator Institution PID,Acquirer Name and Country,IIN,Transaction Destination  Institution PID,Issuer Name and Country,Vehicle Registration Number,Vehicle Class,Vehicle Type,Financial/Non-Financial Indicator,Dispute Reason code,Dispute Reason code description,Disputed Amount,Full / Partial Indicator,Member Message text,Document Indicator,Document Attached Date,Deadline date,Days to act,Direction of Dispute");
				rows.add("\n");
				rows.add(""+dateFormat1+","+getDayMinus1Date()+","+dateFormat1+",ICIC18117A0757,763,763-Debit Adjustment,525eaa82-32d2-4483-9a65-45da530baf2d,34161FA8202F424203ED3048,E404041334B1D0112295CB21,"+dateFormat3+","+dateFormat3+","+dateFormat1+",10,10,356,,'21116182176400,DEBIT,2001,LANE03,Toll,National,ACCEPTED,A,VC4,,File,720301,ICIC2290001,ICICI BANK LTD ACQUIRER- IN,652150,EQTB0880001,EQTB BANK- IN,MH02GH4645,VC4,F,F,1001,Toll fare calculation error,225,P,Debit Adjustment Raised because of Toll Fare calculation error. Transactionid -21116182176400,Y,,,0,Inward");
				log.info("DISPUTE---->All_DisputeCSV()");

			} finally {
				Iterator<String> iter = rows.iterator();
				while (iter.hasNext()) {
					String outputString = (String) iter.next();
					response.getOutputStream().print(outputString);
				}
			}

			log.info("*****************Response to POST dispute/alldisputefile API()****************");

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			DatabaseManager.commitConnection(conn);
		}

	}

	public static String getFileName() {
		//String dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
		//System.out.println(dateFormat1);
		return "All_Dispute.csv";
	}
	public static String getDayMinus1Date() {
		Calendar calendar = Calendar.getInstance();
	    Date today = calendar.getTime();
	    System.out.println("Today's Date: " + today);
	    calendar.add(Calendar.DAY_OF_YEAR, -1);
	    Date datedays = calendar.getTime();
	    String dateFormat = new SimpleDateFormat("dd-MMM-yyyy").format(datedays);
		return dateFormat;
	}
	
	
	public static void main(String[] args) {
		getFileName();
	    //System.out.println("Date before 1 days: " + dateFormat1);
	}

	
}
