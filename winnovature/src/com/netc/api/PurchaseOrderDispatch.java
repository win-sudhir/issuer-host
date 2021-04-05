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
import com.winnovature.dao.RoleMenuDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/purchaseorder/dispatch")
public class PurchaseOrderDispatch extends HttpServlet {
	static Logger log = Logger.getLogger(PurchaseOrderDispatch.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		JSONObject jsonObject = null;
		StringBuffer jb = new StringBuffer();
		String line = null;
		String sResp = null;
		JSONObject jsonResp = new JSONObject();

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
				jb.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);

			jsonObject = new JSONObject(jb.toString());

			log.info(" auth token " + request.getHeader("Authorization").toString());

			String poId = jsonObject.getString("poId");
			String courierCompName = jsonObject.getString("courierCompanyName");
			String dispAddress = jsonObject.getString("dispatchAddress");
			String dispDate = jsonObject.getString("dispatchDatetime");
			String docketNo = jsonObject.getString("docketNo");
			String tagDelAddress = jsonObject.getString("tagDeliveryAddress");
			String remarks = jsonObject.getString("remarks");

			if (jsonObject.has("poId") && poId != null && courierCompName != null && dispAddress != null
					&& dispDate != null && docketNo != null && tagDelAddress != null && remarks != null) {
				sResp = new RoleMenuDAO().dispatchPo(conn, poId, courierCompName, dispAddress, dispDate, docketNo,
						tagDelAddress, remarks);

				log.info("PurchaseOrderDispatch.java ::: Final Response :: " + sResp);

				if (sResp != null && sResp.equalsIgnoreCase("DSU")) {
					jsonResp.put("message", "Purchase order dispatch successful.");
					jsonResp.put("status", "1");
				} else if (sResp != null && sResp.equalsIgnoreCase("SNF")) {
					jsonResp.put("message", "Supplier Id Not found for PO.");
					jsonResp.put("status", "0");
				} else if (sResp != null && sResp.equalsIgnoreCase("DPF")) {
					jsonResp.put("message", "Duplicate PO Id found for Dispatch.");
					jsonResp.put("status", "0");
				} else {
					jsonResp.put("message", "Error Occurred while Dispatching PO.");
					jsonResp.put("status", "0");
				}

			} else {
				jsonResp.put("message", "One or More Input Parameter is Missing/Null/Blank.");
				jsonResp.put("status", "0");
			}

		} catch (Exception e) {
			jsonResp.put("message", e.getMessage());
			jsonResp.put("status", "0");
			e.printStackTrace();
		} finally {
			out.println(jsonResp.toString());
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

}
