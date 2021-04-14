package com.netc.api;

import java.io.BufferedReader;
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

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.DAOManager;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.EmailTemplate;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.SendMailService;

@WebServlet("/supplier/approve")
public class SupplierApprove extends HttpServlet {
	static Logger log = Logger.getLogger(SupplierApprove.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean checkApprove = false;
		JSONObject jreq = new JSONObject();
		JSONObject jresp = new JSONObject();

		PrintWriter out = response.getWriter();
		DAOManager daoManager = new DAOManager();

		StringBuffer sbuffer = new StringBuffer();
		String line = null;
		String supplier_id = null;
		Connection conn = null;
		
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sbuffer.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			jreq = new JSONObject(sbuffer.toString());
			jresp = new JSONObject();

			supplier_id = jreq.getString("supplierId");
			String userId = request.getHeader("userId").toString();
			checkApprove = daoManager.approveSupplier(supplier_id, userId);
			if (checkApprove) {
				SendMailService obj = new SendMailService();
				JSONObject jsonObj = obj.getEmailIdPassword(supplier_id);
				String emailId = jsonObj.getString("email_id").toString();
				String password = jsonObj.getString("password").toString();

				String bodyText = EmailTemplate.getEmailBody(supplier_id, password);

				int statusEmail = obj.sendMail(emailId, "Approve Successfully ", "", bodyText, "");

				if (statusEmail == 1) {
					jresp.put("message", "Supplier Approved successfully.");
					jresp.put("status", "1");
					log.info("Mail Send Sucessfully :::: " + emailId);

				} else {
					jresp.put("message", "Supplier Approved successfully but getting error while sending email !!");
					jresp.put("status", "1");
					log.info("SupplierApprove.java   Getting Error while sending Mail :::: " + emailId);
				}

			} else {
				jresp.put("status", "0");
				jresp.put("message", "Sorry, Can't Approve Supplier...!!!");
			}

			/*
			 * }else{ jresp.put("flag","0"); }
			 */
			out.write(jresp.toString());
			log.info(jresp.toString());
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
