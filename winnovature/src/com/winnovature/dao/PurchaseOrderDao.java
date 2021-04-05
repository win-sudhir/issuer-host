package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

//import com.netc.utils.DBConnection;

public class PurchaseOrderDao {
	static Logger log = Logger.getLogger(PurchaseOrderDao.class.getName());

	public boolean addPurchaseOrder(String podate, String suppid, String sgst, String cgst, String ordervalue,
			String totalordervalue, String po_id, JSONArray po, String userId)
	{
		boolean isadded = false;
		String tagclassid, orderqty, unitprice;// for order
		Connection con = null;
		PreparedStatement preparedStmt = null;
		PreparedStatement preparedStmt1 = null;
		/*
		 * String po_id = null; String number = Double.toString(Math.random());
		 * po_id = number.substring(2, 8);//(1,6); log.info("Poid : "+po_id);
		 */
		Date cuurent_date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(cuurent_date);

		String sql = "insert into po_master(po_id, po_date,po_status, supp_id, created_by, created_on, approved_by, approved_on, order_value, cgst, sgst, total_order_value)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		String sql1 = "insert into po_item_master(po_id, tag_class_id, order_qty, unit_price)" + " values (?, ?, ?, ?)";

		try {
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();
			preparedStmt = con.prepareStatement(sql);// for customer vehicle
														// master
			preparedStmt.setString(1, po_id);
			preparedStmt.setString(2, podate);
			preparedStmt.setString(3, "0");
			preparedStmt.setString(4, suppid);
			preparedStmt.setString(5, userId);// changes made on 8/7/2019
			preparedStmt.setString(6, date);

			preparedStmt.setString(7, "");
			preparedStmt.setString(8, date);
			preparedStmt.setString(9, ordervalue);
			preparedStmt.setString(10, cgst);
			preparedStmt.setString(11, sgst);
			preparedStmt.setString(12, totalordervalue);
			preparedStmt.executeUpdate();
			log.info("PurchaseOrderDao.java  :::   PO added in cust vehicle master.........");

			preparedStmt1 = con.prepareStatement(sql1);

			for (int i = 0; i < po.length(); i++) {
				JSONObject order = po.getJSONObject(i);
				tagclassid = order.getString("tagClassId");
				orderqty = order.getString("orderQty");
				unitprice = order.getString("unitPrice");
				log.info(">>>>tagclassid " + tagclassid + "  orderqty " + orderqty + "   unitprice  " + unitprice);
				preparedStmt1.setString(1, po_id);
				preparedStmt1.setString(2, tagclassid);
				preparedStmt1.setString(3, orderqty);
				preparedStmt1.setString(4, unitprice);
				preparedStmt1.executeUpdate();
			}

			// preparedStmt1.executeUpdate();
			log.info("Successfully added purchase order.........");
			isadded = true;
		} catch (Exception e) {
			try {
				con.rollback();
				log.info("PurchaseOrderDao.java  :::  Something wrong while insert in purchase order..." + e);
				log.error("Getting Exception   :::    ", e);
			} catch (Exception ex) {
				log.info("Insert operation rolled back..." + ex);
			}
		} finally {
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}
		return isadded;
	}

}
