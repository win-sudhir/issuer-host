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
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.contants.IDGenerator;
import com.winnovature.dao.CheckSession;
import com.winnovature.dao.PurchaseOrderDao;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/purcharseorder/add")
public class PurchaseOrderAdd extends HttpServlet {
	static Logger log = Logger.getLogger(PurchaseOrderAdd.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean checkadd = false;

		JSONObject jresp = new JSONObject();

		PrintWriter out = response.getWriter();
		// SalesAgentDao salesagentDao = new SalesAgentDao();
		PurchaseOrderDao poDao = new PurchaseOrderDao();

		StringBuffer sbuffer = new StringBuffer();
		String line = null;

		String podate, suppid, sgst, cgst, ordervalue, totalordervalue;// for agent

		String tagclassid, orderqty, unitprice;// for order

		JSONObject purchaseOrder = new JSONObject();

		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			// DataManager dm =new DataManager();
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sbuffer.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			purchaseOrder = new JSONObject(sbuffer.toString());

			String userId = request.getHeader("userId").toString();
			// String auth_token = request.getHeader("Authorization").toString();
			log.info("PO JSON REQUEST :: " + purchaseOrder);

			// if( userId != null && auth_token != null && LoginDao.isValidSession(userId,
			// auth_token))
			// {
			podate = purchaseOrder.getString("poDate");
			suppid = purchaseOrder.getString("supplierId");
			sgst = purchaseOrder.getString("sGst");
			cgst = purchaseOrder.getString("cGst");
			ordervalue = purchaseOrder.getString("orderValue");
			totalordervalue = purchaseOrder.getString("totalOrderValue");

			log.info("podate suppid" + suppid + podate);

			JSONArray po = purchaseOrder.getJSONArray("order");

			for (int i = 0; i < po.length(); i++) {
				JSONObject oder = po.getJSONObject(i);
				tagclassid = oder.getString("tagClassId");
				orderqty = oder.getString("orderQty");
				unitprice = oder.getString("unitPrice");
				log.info(">>>>tagclassid " + tagclassid + "  orderqty " + orderqty + "   unitprice  " + unitprice);
			}

			String po_id = null;
			/*
			 * String number = Double.toString(Math.random()); po_id = number.substring(2,
			 * 8);//(1,6);
			 */

			po_id = IDGenerator.randomNumeric(6);

			log.info("Poid  : " + po_id);
			checkadd = poDao.addPurchaseOrder(podate, suppid, sgst, cgst, ordervalue, totalordervalue, po_id, po,
					userId, conn);// true;
			// tagallocationDao.allocateTag(TID, VehicleNumber);
			if (checkadd) {
				jresp.put("message", "New data created Po Id : " + po_id);
				jresp.put("status", "1");
			} else {

				jresp.put("message", "Can't add PO " + po_id);
				jresp.put("status", "0");
				log.info("Po can not be created.................");
			}

			out.write(jresp.toString());
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
