package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.dto.PurchaseOrderDTO;
import com.winnovature.dto.ResponseDTO;
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

	public static PurchaseOrderDTO getOnePurchaseOrder(Connection conn, String poId) {
		PurchaseOrderDTO purchaseOrderDTO = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "SELECT * FROM po_master WHERE po_id=? AND po_status=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, poId);
			ps.setString(2, "0");
			rs = ps.executeQuery();
			if (rs.next()) {
				purchaseOrderDTO = new PurchaseOrderDTO();
				purchaseOrderDTO.setPoId(poId);
				purchaseOrderDTO.setPoDate(rs.getString("po_date"));
				purchaseOrderDTO.setSupplierId(rs.getString("supp_id"));
				purchaseOrderDTO.setcGst(rs.getString("cgst"));
				purchaseOrderDTO.setsGst(rs.getString("sgst"));
				purchaseOrderDTO.setOrderValue(rs.getString("order_value"));
				purchaseOrderDTO.setTotalOrderValue(rs.getString("total_order_value"));
			}
			return purchaseOrderDTO;
		}

		catch (Exception e) {
			log.error("getOnePurchaseOrder()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return purchaseOrderDTO;
	}

	
	public static List<PurchaseOrderDTO> getOrderList(Connection conn, String poId) {
		List<PurchaseOrderDTO> lst = new ArrayList<PurchaseOrderDTO>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "SELECT * FROM po_item_master WHERE po_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, poId);
			
			rs = ps.executeQuery();
			PurchaseOrderDTO purchaseOrderDTO = null;
			while (rs.next()) {
				purchaseOrderDTO = new PurchaseOrderDTO();
				purchaseOrderDTO.setId(rs.getString("id"));
				purchaseOrderDTO.setPoId(poId);
				purchaseOrderDTO.setTagClassId(rs.getString("tag_class_id"));
				purchaseOrderDTO.setOrderQty(rs.getString("order_qty"));
				purchaseOrderDTO.setUnitPrice(rs.getString("unit_price"));
				lst.add(purchaseOrderDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getOrderList()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;
	}

	public static ResponseDTO updatePurchaseOrder(Connection conn, PurchaseOrderDTO purchaseOrder, JSONArray orderList,
			String userId) {
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String sql = "UPDATE po_master SET po_date=? , supp_id=?, order_value=?, cgst=?, sgst=?, total_order_value=?, last_updated_on=?, last_updated_by=? WHERE po_id=?";
		String sql1 = "UPDATE po_item_master SET tag_class_id=?, order_qty=?, unit_price=? WHERE po_id=? AND id=?";
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, purchaseOrder.getPoDate());
			ps.setString(2, purchaseOrder.getSupplierId());
			ps.setString(3, purchaseOrder.getOrderValue());
			ps.setString(4, purchaseOrder.getcGst());
			ps.setString(5, purchaseOrder.getsGst());
			ps.setString(6, purchaseOrder.getTotalOrderValue());
			ps.setString(7, date);
			ps.setString(8, userId);
			ps.setString(9, purchaseOrder.getPoId());
			int po = ps.executeUpdate();
			log.info("PurchaseOrderDao.java  updatePurchaseOrder()");

			//String tagClassId = null, orderQty = null, unitPrice = null;
			for (int i = 0; i < orderList.length(); i++) {
				//log.info("i = "+i);
				JSONObject order = orderList.getJSONObject(i);
				String tagClassId = order.getString("tagClassId");
				String orderQty = order.getString("orderQty");
				String unitPrice = order.getString("unitPrice");
				String id = order.getString("id");
				log.info("ID "+id+" >>>>tagclassid " + tagClassId + "  orderqty " + orderQty + "   unitprice  " + unitPrice);
				ps1 = conn.prepareStatement(sql1);
				ps1.setString(1, tagClassId);
				ps1.setString(2, orderQty);
				ps1.setString(3, unitPrice);
				ps1.setString(4, purchaseOrder.getPoId());
				ps1.setString(5, id);
				ps1.executeUpdate();
			}
			responseDTO.setStatus(ResponseDTO.failure);
			if (po > 0) {
				responseDTO.setStatus(ResponseDTO.success);
				return responseDTO;
			}
		} catch (Exception e) {
			log.error("updatePurchaseOrder()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closePreparedStatement(ps1);
		}
		return responseDTO;
	}

}
