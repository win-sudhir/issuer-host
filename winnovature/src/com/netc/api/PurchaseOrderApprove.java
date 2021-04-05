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
import com.winnovature.utils.MemoryComponent;

@WebServlet("/purchaseorder/approve")
public class PurchaseOrderApprove extends HttpServlet {
	static Logger log = Logger.getLogger(PurchaseOrderApprove.class.getName());
	private static final long serialVersionUID = 1L;

	public PurchaseOrderApprove() {
		super();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean checkApprove = false;
		JSONObject jreq = new JSONObject();
		JSONObject jresp = new JSONObject();

		PrintWriter out = response.getWriter();
		DAOManager dataManager = new DAOManager();

		StringBuffer sbuffer = new StringBuffer();
		String line = null;

		String poId = null;

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
			log.info(" authToken " + request.getHeader("Authorization").toString());

			String userId = request.getHeader("userId").toString();
			// String auth_token = request.getHeader("Authorization").toString();
			// if (userId != null && auth_token != null && LoginDao.isValidSession(userId,
			// auth_token)) {
			poId = jreq.getString("poId");

			checkApprove = dataManager.poAuthorize(poId, userId);
			if (checkApprove) {

				jresp.put("message", "PO Authorized");
				jresp.put("status", "1");
			} else {
				jresp.put("message", "Sorry,PO Can't be Authorized...!!!");
				jresp.put("status", "0");
			}
			/*
			 * } else { jresp.put("flag", "0"); }
			 */
			out.write(jresp.toString());
			log.info(jresp.toString());

		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}