package com.winnovature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.constants.WINConstants;
import com.winnovature.utils.DatabaseManager;

public class DAOManager {
	static Logger log = Logger.getLogger(DAOManager.class.getName());

	public boolean approveSupplier(String supplier_id, String userId) {
		boolean approve = false;

		Connection con = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String approvedOn = dateFormat.format(date);

		String sql = "update supplier_master set is_approved = ?, approved_by = ?, approved_on = ?,act_deact_status=?,act_date=?,last_updated_by=? where supplier_id='"
				+ supplier_id + "'";
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			preparedStmt = con.prepareStatement(sql);
			preparedStmt.setInt(1, 1);
			preparedStmt.setString(2, userId);
			preparedStmt.setString(3, approvedOn);
			preparedStmt.setString(4, "1");
			preparedStmt.setString(5, approvedOn);
			preparedStmt.setString(6, userId);
			preparedStmt.executeUpdate();
			approve = true;
			log.info("Successfully approved.........");
		} catch (Exception e) {
			log.info("Something went wrong while approving ..." + e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}

		return approve;
	}

	public boolean dispatchApproval(String supplier_id) {
		boolean approve = false;

		Connection con = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;

		String sql = "update supplier_master set is_approved = ? where supplier_id='" + supplier_id + "'";
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			preparedStmt = con.prepareStatement(sql);
			preparedStmt.setInt(1, 1);
			preparedStmt.executeUpdate();
			approve = true;
			log.info("Successfully approved.........");
		} catch (Exception e) {
			log.info("Something went wrong while approving ..." + e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}

		return approve;
	}

	public boolean deleteSupplier(String supplier_id, Connection conn) {
		boolean isdelete = false;

		PreparedStatement preparedStmt = null;

		try {

			String sql = "update supplier_master set is_deleted = ? where supplier_id='" + supplier_id + "'";
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setInt(1, 1);
			preparedStmt.executeUpdate();
			isdelete = true;
			log.info("DAOManager.java  ::: deleteSupplier() Successfully deleted.........");
		} catch (Exception e) {
			log.info("Something went wrong while deleting..." + e);
		} finally {
			DatabaseManager.closePreparedStatement(preparedStmt);
		}

		return isdelete;
	}

	public JSONArray getSuppAccounts(String user_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * FROM account_master where user_id = ? and is_deleted=0";
			ps = con.prepareStatement(query);
			ps.setString(1, user_id);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				if (rs.getString("is_active").equals("1")) {
					jo.put("isActive", true);
				} else {
					jo.put("isActive", false);
				}
				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}

				jo.put("accountNo", rs.getString("acc_no"));
				jo.put("accountType", rs.getString("account_type"));
				jo.put("bankName", rs.getString("bank_name"));
				jo.put("branchAddress", rs.getString("branch_address"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("ifscCode", rs.getString("ifsc_code"));
				jo.put("userId", rs.getString("user_id"));

				report.put(jo);

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report;
	}

	public JSONObject getSuppAddress(String user_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		String query = null;
		JSONObject jo = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * FROM address_master where user_id = ? and is_deleted=0";
			ps = con.prepareStatement(query);
			ps.setString(1, user_id);

			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}

				jo.put("Id", rs.getString("id"));
				jo.put("businessAddr1", rs.getString("business_add1"));
				jo.put("businessAddr2", rs.getString("business_add2"));
				jo.put("businessCity", rs.getString("business_city"));
				jo.put("businessPin", rs.getString("business_pin"));
				jo.put("businessState", rs.getString("business_state"));
				jo.put("resiAddr1", rs.getString("resi_add1"));
				jo.put("resiAddr2", rs.getString("resi_address2"));
				jo.put("resiCity", rs.getString("resi_city"));
				jo.put("resiPin", rs.getString("resi_pin"));
				jo.put("resiState", rs.getString("resi_state"));
				jo.put("userId", rs.getString("user_id"));

			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo;
	}

	public JSONObject getSupplier(String supplier_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from supplier_master where supplier_id = ? and is_deleted=0";
			ps = con.prepareStatement(query);
			ps.setString(1, supplier_id);

			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}
				if (rs.getString("is_approved").equals("1")) {
					jo.put("isApproved", true);
				} else {
					jo.put("isApproved", false);
				}

				jo.put("GSTN", rs.getString("gstn"));
				jo.put("approvedOn", rs.getString("approved_on"));
				jo.put("approvedby", rs.getString("approved_by"));
				jo.put("contactNumber1", rs.getString("contact_number1"));
				jo.put("contactNumber2", rs.getString("contact_number2"));
				jo.put("contactPerson", rs.getString("contact_person"));
				jo.put("createdBy", rs.getString("created_by"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("deliveryPeriod", rs.getString("delivery_period"));
				jo.put("emailId", rs.getString("email_id"));
				jo.put("is_npci_certified", rs.getString("is_npci_certified"));
				jo.put("maxOrderQty", rs.getString("max_order_qty"));
				jo.put("minOrderQty", rs.getString("min_order_qty"));
				jo.put("npci_certification_expiry", rs.getString("npci_certification_expiry"));
				jo.put("status", rs.getString("status"));
				jo.put("supplierId", rs.getString("supplier_id"));
				jo.put("supplierName", rs.getString("supplier_name"));
				jo.put("webSite", rs.getString("web_site"));
				jo.put("actDeactStatus", rs.getString("act_deact_status"));
				jo.put("deleteStatus", rs.getString("delete_status"));

				if (rs.getString("is_npci_certified").equals("1")) {
					jo.put("is_npci_certified", true);
				}

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo;
	}

	public String getAllSuppliers(Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject suppliers = null;
		JSONArray supplier = null;
		String query = null;
		try {

			query = "SELECT * from supplier_master where is_deleted=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, "0");

			rs = ps.executeQuery();
			supplier = new JSONArray();
			while (rs.next()) {
				suppliers = new JSONObject();
				suppliers.put("isDeleted", rs.getString("is_deleted"));
				suppliers.put("isApproved", rs.getString("is_approved"));

				suppliers.put("GSTN", rs.getString("gstn"));
				// jo.put("approvedOn", rs.getString("approved_on"));
				// jo.put("approvedby", rs.getString("approved_by"));
				suppliers.put("contactNumber1", rs.getString("contact_number1"));
				suppliers.put("contactNumber2", rs.getString("contact_number2"));
				suppliers.put("contactPerson", rs.getString("contact_person"));
				suppliers.put("createdBy", rs.getString("created_by"));
				suppliers.put("createdOn", rs.getString("created_on"));
				suppliers.put("deliveryPeriod", rs.getString("delivery_period"));
				suppliers.put("emailId", rs.getString("email_id"));
				suppliers.put("isNpciCertified", rs.getString("is_npci_certified"));
				suppliers.put("maxOrderQty", rs.getString("max_order_qty"));
				suppliers.put("minOrderQty", rs.getString("min_order_qty"));
				suppliers.put("npciCertificationExpiry", rs.getString("npci_certification_expiry"));
				suppliers.put("status", rs.getString("status"));
				suppliers.put("supplierId", rs.getString("supplier_id"));
				suppliers.put("supplierName", rs.getString("supplier_name"));
				suppliers.put("webSite", rs.getString("web_site"));
				// jo.put("actDeactStatus", rs.getString("act_deact_status"));
				// jo.put("deleteStatus", rs.getString("delete_status"));
				// jo.put("is_npci_certified", rs.getString("is_npci_certified"));
				supplier.put(suppliers);
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return supplier.toString();
	}

	public String getAllSupplierList(String userid, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray report = null;
		String query = null;
		try {
			log.info("DAOManager.java   :: getAllSupplierList()  ::: ");

			query = "select supplier_id,supplier_name from supplier_master where is_deleted=? order by created_on desc";
			ps = conn.prepareStatement(query);
			ps.setString(1, "0");

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();

				jo.put("supplierId", rs.getString("supplier_id"));
				jo.put("supplierName", rs.getString("supplier_name"));

				report = report.put(jo);
			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return report.toString();
	}

	public String getSingleSupplierData(String supplierId, String userid, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;
		String query = null;
		try {

			// changes for maker and checker //19-06-2019
			if (userid.equalsIgnoreCase("admin") || userid.startsWith("STCH")) {
				query = "select supplier_id from supplier_master where supplier_id=? and is_deleted=? and is_approved<2 order by created_on desc limit 20";
				ps = conn.prepareStatement(query);
				ps.setString(1, supplierId);
				ps.setString(2, "0");
			} else if (userid.startsWith("ST")) {
				query = "select supplier_id from supplier_master where supplier_id=? and is_deleted=? and created_by=? order by created_on desc";
				ps = conn.prepareStatement(query);
				ps.setString(1, supplierId);
				ps.setString(2, "0");
				ps.setString(3, userid);
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				jo.put("Accounts", getSuppAccounts(rs.getString("supplier_id")));

				jo.put("Address", getSuppAddress(rs.getString("supplier_id")));

				jo.put("Supplier", getSupplier(rs.getString("supplier_id")));

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}

		return jo.toString();
	}

	public String getSupplierData(String userid) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray report = null;
		String query = null;
		try {
			log.info("DAOManager.java   :: getSupplierData()  ::: ");
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			// changes for maker and checker //19-06-2019
			if (userid.equalsIgnoreCase("admin") || userid.startsWith("STCH")) {
				query = "select supplier_id from supplier_master where is_deleted=? and is_approved<2 order by created_on desc limit 20";
				ps = con.prepareStatement(query);
				ps.setString(1, "0");
			} else if (userid.startsWith("ST")) {
				query = "select supplier_id from supplier_master where is_deleted=? and created_by=? order by created_on desc";
				ps = con.prepareStatement(query);
				ps.setString(1, "0");
				ps.setString(2, userid);
			}

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				report = report.put(jo);
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		return report.toString();
	}

	public JSONObject getSalesAgent(String agent_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from sales_agent_master where agent_id = ? and is_deleted=0";
			ps = con.prepareStatement(query);
			ps.setString(1, agent_id);

			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				if (rs.getString("is_deleted").equals("0")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}
				if (rs.getString("is_approved").equals("0")) {
					jo.put("isApproved", false);
				} else {
					jo.put("isApproved", true);
				}
				if (rs.getString("is_active").equals("0")) {
					jo.put("isActive", true);
				} else {
					jo.put("isActive", false);
				}

				jo.put("agentId", rs.getString("agent_id"));
				jo.put("agentName", rs.getString("agent_name"));
				jo.put("approvedOn", rs.getString("approved_on"));
				jo.put("approvedby", rs.getString("approved_by"));
				jo.put("branchId", rs.getString("branch_id"));
				jo.put("contactNumber", rs.getString("contact_number"));
				jo.put("createdBy", rs.getString("created_by"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("emailId", rs.getString("email_id"));

			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Error   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo;
	}

	public String getTagClass() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "select id,tag_class_id,tag_color_id,is_active from tag_class_master";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				if (rs.getString("is_active").equals("1")) {
					jo.put("isactive", true);
				} else {
					jo.put("isactive", false);
				}

				jo.put("id", rs.getString("id"));
				jo.put("tagclassid", rs.getString("tag_class_id"));
				jo.put("tagcolorid", rs.getString("tag_color_id"));
				report.put(jo);

			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("TagClassMasterData", report);
		return mainObj.toString();
	}

	public String getPoList(String userId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray polist = null;
		String query = null;
		try {

			if (userId.equalsIgnoreCase("admin")) {
				// query = "select
				// po_id,supp_id,created_on,cgst,sgst,total_order_value,po_status from po_master
				// where po_status <=6 ORDER BY created_on desc ";
				query = "select po.po_id,pm.order_qty, po.supp_id,po.created_on,po.cgst,po.sgst,po.total_order_value,po.po_status from po_master po, po_item_master pm where po.po_id=pm.po_id and po.po_status <=6 ORDER BY po.created_on desc";
				ps = conn.prepareStatement(query);
				// ps.setInt(1,0);
			} else {
				// query = "select
				// po_id,supp_id,created_on,cgst,sgst,total_order_value,po_status from po_master
				// where po_status <=6 and supp_id=? ORDER BY created_on desc ";
				query = "select po.po_id,pm.order_qty, po.supp_id,po.created_on,po.cgst,po.sgst,po.total_order_value,po.po_status from po_master po, po_item_master pm where po.po_id=pm.po_id and po.supp_id=? and po.po_status <=6 ORDER BY po.created_on desc";
				ps = conn.prepareStatement(query);
				// ps.setInt(1,1);
				ps.setString(1, userId);
			}

			rs = ps.executeQuery();
			polist = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();

				jo.put("cGst", rs.getString("cgst"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("poId", rs.getString("po_id"));
				jo.put("sGst", rs.getString("sgst"));
				jo.put("suppId", rs.getString("supp_id"));
				jo.put("totalOrderValue", rs.getString("total_order_value"));
				jo.put("poStatus", rs.getString("po_status"));
				jo.put("orderQuantity", rs.getString("order_qty"));
				polist.put(jo);

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("poList", polist);
		return mainObj.toString();
	}

	public String getSinglePoList(String poId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray report = null;
		String query = null;
		try {

			// query = "select po_id,supp_id,created_on,cgst,sgst,total_order_value from
			// po_master where po_status = ?";
			query = "select po.po_id,pm.order_qty, po.supp_id,po.created_on,po.cgst,po.sgst,po.total_order_value,po.po_status "
					+ "from po_master po, po_item_master pm "
					+ "where po.po_id=pm.po_id and po.po_status = ? and po.po_id = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, 6);
			ps.setString(2, poId);

			rs = ps.executeQuery();
			report = new JSONArray();
			if (rs.next()) {
				jo = new JSONObject();

				jo.put("cGst", rs.getString("cgst"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("poId", rs.getString("po_id"));
				jo.put("sGst", rs.getString("sgst"));
				jo.put("suppId", rs.getString("supp_id"));
				jo.put("totalOrderValue", rs.getString("total_order_value"));
				jo.put("orderQuantity", rs.getString("order_qty"));
				report.put(jo);

			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("POList", report);
		return mainObj.toString();
	}

	public String tagAllocation() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM branch_master where is_deleted=?";
			ps = con.prepareStatement(query);
			ps.setString(1, "0");
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				if (rs.getString("is_active").equals("1")) {
					jo.put("isActive", true);
				} else {
					jo.put("isActive", false);
				}
				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}
				if (rs.getString("is_approved").equals("1")) {
					jo.put("isApproved", true);
				} else {
					jo.put("isApproved", false);
				}

				jo.put("approvedOn", rs.getString("approved_on"));

				jo.put("approvedby", rs.getString("approved_by"));
				jo.put("branchId", rs.getString("branch_id"));
				jo.put("branchName", rs.getString("branch_name"));
				jo.put("contactId", rs.getString("contact_number"));
				jo.put("createdBy", rs.getString("created_by"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("emailId", rs.getString("email_id"));
				report.put(jo);

			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("", report);
		return mainObj.toString();
	}

	public String addGroup(String groupDesc) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();
			String sql = "CALL addGroups(?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, groupDesc);
			rs = ps.executeQuery();
			if (rs.next()) {
				// return true;
				jo.put("Message", "Group Created Successfully");
				jo.put("Status", true);
				jo.put("groupId", rs.getString("last_id"));
				jo.put("group_desc", groupDesc);
			}
		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			// DatabaseConnectionManager.closeCallableStatement(cs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return jo.toString();
	}

	public JSONArray getStAddress() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();
			query = "SELECT address_master.user_id FROM address_master JOIN staff_master ON (staff_master.staff_id = address_master.user_id)";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				if (rs.getString("is_active").equals("1")) {
					jo.put("isActive", true);
				} else {
					jo.put("isActive", false);
				}
				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}

				/*
				 * jo.put("menuDescription", rs.getString("user_id")); jo.put("menuId",
				 * rs.getString("menu_id")); jo.put("menuLink", rs.getString("menu_link"));
				 * jo.put("menushortName", rs.getString("short_name")); report.put(jo);
				 */

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report;
	}

	public JSONObject getStaffAddress(String user_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM address_master where user_id = ? and is_deleted=?";
			ps = con.prepareStatement(query);
			ps.setString(1, user_id);
			ps.setString(2, "0");
			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				if (rs.getString("is_deleted").equals("0")) {
					jo.put("isDeleted", false);
				} else {
					jo.put("isDeleted", true);
				}

				jo.put("Id", rs.getString("id"));
				jo.put("businessAddr1", rs.getString("business_add1"));
				jo.put("businessAddr2", rs.getString("business_add2"));
				jo.put("businessCity", rs.getString("business_city"));
				jo.put("businessPin", rs.getString("business_pin"));
				jo.put("businessState", rs.getString("business_state"));
				jo.put("resiAddr1", rs.getString("resi_add1"));
				jo.put("resiAddr2", rs.getString("resi_address2"));
				jo.put("resiCity", rs.getString("resi_city"));
				jo.put("resiPin", rs.getString("resi_pin"));
				jo.put("resiState", rs.getString("resi_state"));
				jo.put("userId", rs.getString("user_id"));

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo;
	}

	public JSONObject getStaff(String staff_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from staff_master where staff_id = ? and is_deleted=?";
			ps = con.prepareStatement(query);
			ps.setString(1, staff_id);
			ps.setString(2, "0");
			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				if (rs.getString("is_deleted").equals("0")) {
					jo.put("isDeleted", false);
				} else {
					jo.put("isDeleted", true);
				}
				if (rs.getString("is_approved").equals("1")) {
					jo.put("isApproved", true);
				} else {
					jo.put("isApproved", false);
				}
				if (rs.getString("is_active").equals("1")) {
					jo.put("isActive", true);
				} else {
					jo.put("isActive", false);
				}

				jo.put("approvedOn", rs.getString("approved_on"));
				jo.put("approvedby", rs.getString("approved_by"));
				// jo.put("branchId", rs.getString("branch_id"));
				jo.put("roleId", rs.getString("role_id")); // add 23-08

				jo.put("contactNumber", rs.getString("contact_number"));
				jo.put("createdBy", rs.getString("created_by"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("emailId", rs.getString("email_id"));
				jo.put("staffId", rs.getString("staff_id"));
				jo.put("staffName", rs.getString("staff_name"));
				jo.put("actDeactStatus", rs.getString("act_deact_status"));
				jo.put("deleteStatus", rs.getString("delete_status"));

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Error   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo;
	}

	public String getStaffData(String userid) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			// changes for maker and checker //19-06-2019
			if (userid.equalsIgnoreCase("admin") || userid.startsWith("STCH")) {
				query = "select staff_id from staff_master where is_deleted = ? and is_approved<2 order by created_on desc";
				ps = con.prepareStatement(query);
				ps.setString(1, "0");
			} else if (userid.startsWith("ST")) {
				query = "select staff_id from staff_master where is_deleted = ? and created_by=?  and is_approved<2 order by created_on desc";
				ps = con.prepareStatement(query);
				ps.setString(1, "0");
				ps.setString(2, userid);
			}

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("Address", getStaffAddress(rs.getString("staff_id")));
				jo.put("Staff", getStaff(rs.getString("staff_id")));
				report = report.put(jo);
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report.toString();
	}

	public String getSingleStaffData(String staffId, String userid) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			// changes for maker and checker //19-06-2019
			if (userid.equalsIgnoreCase("admin") || userid.startsWith("STCH")) {
				query = "select staff_id from staff_master where staff_id = ?  and is_approved<2 order by created_on desc";
				ps = con.prepareStatement(query);
				ps.setString(1, staffId);
			} else if (userid.startsWith("ST")) {
				query = "select staff_id from staff_master where staff_id =  ?  and is_approved<2 order by created_on desc";
				ps = con.prepareStatement(query);
				ps.setString(1, staffId);
				// ps.setString(2,userid);
			}
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("Address", getStaffAddress(rs.getString("staff_id")));
				jo.put("Staff", getStaff(rs.getString("staff_id")));
				report = report.put(jo);
			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report.toString();
	}

	public JSONArray orderList(String po_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from po_item_master";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();

				jo.put("orderqty", rs.getInt("order_qty"));
				jo.put("poid", rs.getString("po_id"));
				jo.put("tagclassid", rs.getString("tag_class_id"));
				jo.put("unitprice", rs.getString("unit_price"));

				report.put(jo);
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report;
	}

	public JSONArray singleOrderList(String po_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from po_item_master where po_id = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, po_id);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();

				jo.put("orderqty", rs.getInt("order_qty"));
				jo.put("poid", rs.getString("po_id"));
				jo.put("tagclassid", rs.getString("tag_class_id"));
				jo.put("unitprice", rs.getString("unit_price"));

				report.put(jo);
				// log.info(report);

			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report;
	}

	public String orderFullfillmentList() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "select po_id from po_item_master";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("orderfullfillmentlist", orderList(rs.getString("po_id")));

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo.toString();
	}

	public String singleOrderFullfillmentList(String poId) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		String query = null;
		try {
			con = DatabaseManager.getConnection();
			// new DBConnection().getConnection();
			query = "select po_id from po_item_master where po_id=?";
			ps = con.prepareStatement(query);
			ps.setString(1, poId);

			rs = ps.executeQuery();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("orderfullfillmentlist", singleOrderList(rs.getString("po_id")));
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo.toString();
	}

	public JSONArray singleOrderListView(String poId) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from viewpomaster where po_id = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, poId);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("business_add1", rs.getString("business_add1"));
				jo.put("podate", rs.getString("po_date"));
				jo.put("poid", rs.getString("po_id"));
				jo.put("suppid", rs.getString("supp_id"));
				jo.put("suppliername", rs.getString("supplier_name"));
				jo.put("totalordervalue", rs.getString("total_order_value"));

				report.put(jo);
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report;
	}

	public JSONArray orderListView(String po_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from viewpomaster";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("business_add1", rs.getString("business_add1"));
				jo.put("podate", rs.getString("po_date"));
				jo.put("poid", rs.getString("po_id"));
				jo.put("suppid", rs.getString("supp_id"));
				jo.put("suppliername", rs.getString("supplier_name"));
				jo.put("totalordervalue", rs.getString("total_order_value"));

				report.put(jo);

				// log.info(report);
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report;
	}
	/*
	 * public String orderFullfillmentView(){ PreparedStatement ps = null;
	 * Connection con =null; ResultSet rs = null; JSONObject jo = null;
	 * 
	 * // JSONArray report = null; String query = null; try { con =
	 * DatabaseManager.getConnection();//new DBConnection().getConnection();
	 * 
	 * query ="select po_id from viewpomaster"; log.info(" query  "+query); ps =
	 * con.prepareStatement(query);
	 * 
	 * rs = ps.executeQuery(); // report = new JSONArray(); while (rs.next()) { jo =
	 * new JSONObject();
	 * 
	 * jo.put("orderfullfillmentview", orderListView(rs.getString("po_id")));
	 * log.info("please work1" + jo.put("orderfullfillmentview",
	 * orderListView(rs.getString("po_id"))));
	 * 
	 * // report.put(jo);
	 * 
	 * }
	 * 
	 * } catch (Exception e) {
	 * 
	 * log.error("DAOManager.java getting Error   :::    ",e); }
	 * 
	 * finally{ DatabaseManager.closeResultSet(rs);
	 * DatabaseManager.closePreparedStatement(ps);
	 * DatabaseManager.closeConnection(con);
	 * 
	 * }
	 * 
	 * return jo.toString(); }
	 */

	/*
	 * public String singleOrderFullfillmentView(String po_id){ log.info("hello");
	 * PreparedStatement ps = null; Connection con =null; ResultSet rs = null;
	 * JSONObject jo = null; String query = null; try { con =
	 * DatabaseManager.getConnection();//new DBConnection().getConnection(); query
	 * ="select * from viewpomaster where po_id=?"; ps =
	 * con.prepareStatement(query); ps.setString(1,po_id);
	 * 
	 * rs = ps.executeQuery(); while (rs.next()) { jo = new JSONObject();
	 * jo.put("orderfullfillmentview", singleOrderListView(rs.getString("po_id")));
	 * }
	 * 
	 * } catch (Exception e) {
	 * 
	 * log.error("DAOManager.java Getting Exception   :::    ",e); }
	 * 
	 * 
	 * finally{ DatabaseManager.closeResultSet(rs);
	 * DatabaseManager.closePreparedStatement(ps);
	 * DatabaseManager.closeConnection(con);
	 * 
	 * }
	 * 
	 * return jo.toString(); }
	 */

	public boolean poUnAuthorize(String po_id) {
		boolean approve = false;

		Connection con = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;

		String sql = "update po_master set po_status = ? where po_id='" + po_id + "'";
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			preparedStmt = con.prepareStatement(sql);

			log.info("sql::::::" + sql);
			preparedStmt.setString(1, "4");
			preparedStmt.executeUpdate();
			approve = true;
			log.info("Successfully UnAuthorized.........");
		} catch (Exception e) {
			log.info("Something went wrong while UnAuthorizing ..." + e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}
		return approve;
	}

	public boolean poAuthorize(String po_id, String userId, Connection conn) {
		boolean approve = false;

		PreparedStatement preparedStmt = null;
		ResultSet rs = null;

		String sql = "update po_master set po_status = ?,approved_on=now(), approved_by=? where po_id=?";
		try {
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setInt(1, 1);
			preparedStmt.setString(2, userId);
			preparedStmt.setString(3, po_id);
			preparedStmt.executeUpdate();
			approve = true;
			log.info("Successfully Authorized.........");
		} catch (Exception e) {
			log.info("Something went wrong while Authorizing ..." + e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt);
		}
		return approve;
	}

	public boolean POReject(String po_id, String userId) {
		boolean approve = false;

		Connection con = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;

		String sql = "update po_master set po_status = ? where po_id='" + po_id + "'";
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			preparedStmt = con.prepareStatement(sql);
			preparedStmt.setInt(1, 4);
			preparedStmt.executeUpdate();
			approve = true;
			log.info("Successfully Rejected.........");
		} catch (Exception e) {
			log.info("Something went wrong while Rejecting ..." + e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}

		return approve;
	}

	public boolean editSalesAgent(String agentId, String branchId, String agentName, String emailId,
			String contactNumber, String isActive, String resiAddr1, String resiAddr2, String resiPin, String resiCity,
			String resiState, String businessAddr1, String businessAddr2, String businessPin, String businessCity,
			String businessState, String accountNo, String bankName, String branchAddress, String ifscCode,
			String accountType, String isModifiedby) {
		boolean isadded = false;

		Connection con = null;
		PreparedStatement preparedStmt = null;
		PreparedStatement preparedStmt1 = null;
		PreparedStatement preparedStmt2 = null;

		String sql = "update sales_agent_master set agent_id=?, branch_id=?, agent_name=?, email_id=?, contact_number=?, is_active=?, created_by=?, created_on=?, approved_by=?, approved_on=? ,isModifiedby=? where agent_id='"
				+ agentId + "'";

		String sql1 = "update address_master  set user_id=?, resi_add1=?, resi_address2=?, resi_pin=?, resi_city=?, resi_state=?, business_add1=?, business_add2=?, business_pin=?, business_city=?, business_state=? where user_id='"
				+ agentId + "'";

		String sql2 = "update account_master  set user_id=?, acc_no=?, bank_name=?, branch_address=?, ifsc_code=?, account_type=?, created_on=?, is_active=? where user_id='"
				+ agentId + "'";

		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			con.setAutoCommit(false);
			preparedStmt = con.prepareStatement(sql);// for sales agent master
			preparedStmt.setString(1, agentId);
			preparedStmt.setString(2, branchId);
			preparedStmt.setString(3, agentName);
			preparedStmt.setString(4, emailId);
			preparedStmt.setString(5, contactNumber);
			preparedStmt.setString(6, isActive);
			preparedStmt.setString(7, "sud");
			preparedStmt.setString(8, "2018-03-21");
			preparedStmt.setString(9, "xyz");
			preparedStmt.setString(10, "2018-03-21");
			preparedStmt.setString(11, isModifiedby);
			preparedStmt.executeUpdate();

			preparedStmt1 = con.prepareStatement(sql1);// for address master
			preparedStmt1.setString(1, agentId);
			preparedStmt1.setString(2, resiAddr1);
			preparedStmt1.setString(3, resiAddr2);
			preparedStmt1.setString(4, resiPin);
			preparedStmt1.setString(5, resiCity);
			preparedStmt1.setString(6, resiState);
			preparedStmt1.setString(7, businessAddr1);
			preparedStmt1.setString(8, businessAddr2);
			preparedStmt1.setString(9, businessPin);
			preparedStmt1.setString(10, businessCity);
			preparedStmt1.setString(11, businessState);

			preparedStmt1.executeUpdate();

			preparedStmt2 = con.prepareStatement(sql2);// for account master
			preparedStmt2.setString(1, agentId);
			preparedStmt2.setString(2, accountNo);
			preparedStmt2.setString(3, bankName);
			preparedStmt2.setString(4, branchAddress);
			preparedStmt2.setString(5, ifscCode);
			preparedStmt2.setString(6, accountType);
			preparedStmt2.setString(7, "2018-03-21");
			preparedStmt2.setString(8, "0");

			preparedStmt2.executeUpdate();

			con.commit();
			isadded = true;
			log.info("Successfully sales agent added.........");
		} catch (Exception e) {
			log.info("Something wrong while insert..." + e);
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			// DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt2);
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}
		return isadded;
	}

	public static String getLeftPaddedString(String sInputData, String padd, int iLength)

	{
		if (sInputData.length() > iLength) {
			sInputData = sInputData.substring(0, iLength);
		} else {
			while (sInputData.length() < iLength) {
				sInputData = padd + sInputData;
			}
		}
		return sInputData;
	}

	public String addSupplier(String supplierId, String supplierName, String emailId, String contactPerson,
			String contactNumber1, String contactNumber2, String deliveryPeriod, String maxOrderQty, String minOrderQty,
			String is_npci_certified, String npci_certification_expiry, String GSTN, String webSite, String status,
			String resiAddr1, String resiAddr2, String resiPin, String resiCity, String resiState, String businessAddr1,
			String businessAddr2, String businessPin, String businessCity, String businessState, String accountNo,
			String bankName, String branchAddress, String ifscCode, String accountType, String createdBy,
			String password, Connection conn) {
		String isadded = "0";

		PreparedStatement preparedStmt = null;
		PreparedStatement preparedStmt1 = null;
		PreparedStatement preparedStmt2 = null;

		ResultSet rs = null;
		String sql = "insert into supplier_master (supplier_id, supplier_name, email_id, contact_person, contact_number1, contact_number2, web_site, delivery_period, max_order_qty,min_order_qty,is_npci_certified,npci_certification_expiry,gstn,created_by,created_on,approved_by,approved_on,status,is_deleted,is_approved)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String sql1 = "insert into address_info  (user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state, business_add1, business_add2, business_pin, business_city, business_state)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String sql2 = "insert into account_info  (user_id, account_number, bank_name, branch_address, ifsc_code, account_type, created_on, status)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?)";

		try {

			preparedStmt = conn.prepareStatement(sql);// for sales agent master

			preparedStmt.setString(1, supplierId);

			preparedStmt.setString(2, supplierName);
			preparedStmt.setString(3, emailId);
			preparedStmt.setString(4, contactPerson);
			preparedStmt.setString(5, contactNumber1);
			preparedStmt.setString(6, contactNumber2);
			preparedStmt.setString(7, webSite);
			preparedStmt.setString(8, deliveryPeriod);
			preparedStmt.setString(9, maxOrderQty);
			preparedStmt.setString(10, minOrderQty);
			preparedStmt.setString(11, is_npci_certified);
			preparedStmt.setString(12, npci_certification_expiry);
			preparedStmt.setString(13, GSTN);
			preparedStmt.setString(14, createdBy);
			preparedStmt.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
			preparedStmt.setString(16, "");
			preparedStmt.setTimestamp(17, new Timestamp(System.currentTimeMillis()));

			preparedStmt.setString(18, status);
			preparedStmt.setString(19, "0");
			preparedStmt.setString(20, "0");

			preparedStmt.executeUpdate();

			preparedStmt1 = conn.prepareStatement(sql1); // for address master
			preparedStmt1.setString(1, supplierId);
			preparedStmt1.setString(2, resiAddr1);
			preparedStmt1.setString(3, resiAddr2);
			preparedStmt1.setString(4, resiPin);
			preparedStmt1.setString(5, resiCity);
			preparedStmt1.setString(6, resiState);
			preparedStmt1.setString(7, businessAddr1);
			preparedStmt1.setString(8, businessAddr2);
			preparedStmt1.setString(9, businessPin);
			preparedStmt1.setString(10, businessCity);
			preparedStmt1.setString(11, businessState);
			// preparedStmt1.setString(12, "0");
			preparedStmt1.executeUpdate();

			preparedStmt2 = conn.prepareStatement(sql2);// for account master
			preparedStmt2.setString(1, supplierId);
			preparedStmt2.setString(2, accountNo);
			preparedStmt2.setString(3, bankName);
			preparedStmt2.setString(4, branchAddress);
			preparedStmt2.setString(5, ifscCode);
			preparedStmt2.setString(6, accountType);
			preparedStmt2.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStmt2.setString(8, WINConstants.NEW);
			// preparedStmt2.setString(9, "0");

			preparedStmt2.executeUpdate();

			String role_id = "158";
			WinnovatureService.insertUser(supplierId, role_id, createdBy, password, emailId);
			log.info("supplier user inserted successfully.........");

			isadded = "1";
		} catch (Exception e) {
			isadded = "-1";

			log.error("DAOManager.java :: Something wrong while insert... " + e.getMessage());
			log.error("DAOManager.java Getting Exception   :::    ", e);

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt2);
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closePreparedStatement(preparedStmt);
		}
		return isadded;
	}

	public static boolean validateSupplierID(String email_id, Connection conn) {
		boolean check = false;
		ResultSet rs = null;
		PreparedStatement st = null;
		String sql = "SELECT email_id FROM supplier_master where email_id=? and is_deleted=?";

		try {

			st = conn.prepareStatement(sql);
			st.setString(1, email_id);
			st.setString(2, "0");
			rs = st.executeQuery();
			if (rs.next()) {
				check = true;
				log.error("Supplier email_id already present ");
			}

		} catch (Exception e) {
			log.error("Error while checking Supplier  :: ", e);
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeStatement(st);
		}
		return check;
	}

	public String getUnAllocatedVechicleList(String customerId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray vehicleList = null;
		String query = null;
		try {

			query = "select * from customer_vehicle_info where user_id = ? and status = ? and vehicle_number NOT IN (select vehicle_number FROM vehicle_tag_linking where customer_id = ? and (vehicle_number is not null and vehicle_number !=''))";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			ps.setString(2, "ACTIVE");
			ps.setString(3, customerId);

			rs = ps.executeQuery();
			vehicleList = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();

				// isCommercial = rs.getString("is_commercial");
				jo.put("vehicleNumber", rs.getString("vehicle_number"));
				jo.put("tagClassId", rs.getString("tag_class_id"));
				jo.put("isCommercial", rs.getString("is_commercial"));

				vehicleList.put(jo);

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("vehicleList", vehicleList);

		log.info("DAOManager.java  ::: getUnallocatedVechicle()   :: mainObj  :: " + mainObj);

		return mainObj.toString();
	}

	public String getBarcode(String userId, String vehicleNumber, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String tagclassId = null;
		String isCommercial = null;
		JSONObject jo = null;

		JSONArray barcodeData = null;
		String query = null;
		try {

			tagclassId = getTagclassID(vehicleNumber, conn);
			isCommercial = getIsCommercial(vehicleNumber, conn);
			log.info("tagclassId : " + tagclassId);
			log.info("isCommercial : " + isCommercial);

			if (userId.equalsIgnoreCase("admin") || userId.startsWith("ST")) {
				query = "select vtl.tid,vtl.barcode_data from vehicle_tag_linking vtl inner join inventory_master im on vtl.tid = im.tid where im.status = ? and vtl.tag_class_id= ? and ((vtl.vehicle_number is null and vtl.customer_id is null) or ( vtl.vehicle_number ='' and vtl.customer_id ='')) and ((im.branch_id = '0' or im.branch_id = '') and (im.agent_id = '0' or im.agent_id = ''))";
				ps = conn.prepareStatement(query);
				ps.setString(1, "3");
				ps.setString(2, tagclassId);
			} else if (userId.startsWith("A")) {
				query = "select vtl.tid,vtl.barcode_data from vehicle_tag_linking vtl inner join inventory_master im on vtl.tid = im.tid "
						+ "where im.status = ? and vtl.tag_class_id= ? and im.agent_id = ? and ((vtl.vehicle_number is null and vtl.customer_id is null) or ( vtl.vehicle_number ='' and vtl.customer_id =''))";
				ps = conn.prepareStatement(query);
				ps.setString(1, "3");
				ps.setString(2, tagclassId);
				ps.setString(3, userId);
			}

			log.info("QUERY : " + query);
			rs = ps.executeQuery();
			barcodeData = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();

				jo.put("barCode", rs.getString("barcode_data"));
				jo.put("tid", rs.getString("tid"));

				barcodeData.put(jo);

			}
			log.info(barcodeData);

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		JSONObject mainObj = new JSONObject();
		mainObj.put("barCodeData", barcodeData);
		mainObj.put("isCommercial", isCommercial);

		return mainObj.toString();
	}

	private String getIsCommercial(String vno, Connection conn) {
		String isCommercial = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {

			query = "select is_commercial from customer_vehicle_info where vehicle_number  = ? and status=? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, vno);
			ps.setString(2, "ACTIVE");

			rs = ps.executeQuery();

			while (rs.next()) {
				isCommercial = rs.getString("is_commercial");
			}
			log.info("DAOManager.java getIsCommercial() return isCommercial :: " + isCommercial
					+ "  against the VehicleNo  :::  " + vno + " from customer_vehicle_info ");

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return isCommercial;
	}

	public String getTagclassID(String vno, Connection conn) {
		String tagClassID = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {

			query = "select tag_class_id from customer_vehicle_info where vehicle_number  = ? and status=? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, vno);
			ps.setString(2, "ACTIVE");
			rs = ps.executeQuery();

			if (rs.next()) {
				tagClassID = rs.getString("tag_class_id");

			}
			log.info("DAOManager.java getTagclassID() return tag_class_id :: " + tagClassID
					+ "  against the VehicleNo  :::  " + vno + " from customer_vehicle_info ");

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return tagClassID;
	}

	public String tagAllocateToAgentStartBarcode(String tagClassId, String id, String idValue, String count,
			String userid, String startBarcode) {
		String status = "-1";

		Connection con = null;
		PreparedStatement preparedStmt = null;
		PreparedStatement preparedStmt1 = null;
		PreparedStatement preparedStmt2 = null;
		PreparedStatement preparedStmt3 = null;
		PreparedStatement preparedStmt4 = null;
		String colid = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String qcolid = null;

		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();

			if (userid.equalsIgnoreCase("admin") || userid.startsWith("ST"))// admin
			{
				// qcolid = "select id from inventory_master where tid = (select tid from
				// vehicle_tag_linking where barcode_data = ? and tag_class_id = ? )";

				qcolid = "select im.id from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and im.tid =(select tid from vehicle_tag_linking where vehicle_number is null and customer_id is null and barcode_data = ? and tag_class_id = ? )";
				preparedStmt4 = con.prepareStatement(qcolid);
				preparedStmt4.setString(1, tagClassId);
				preparedStmt4.setString(2, "3");
				preparedStmt4.setString(3, "0");
				preparedStmt4.setString(4, "0");
				preparedStmt4.setString(5, startBarcode);
				preparedStmt4.setString(6, tagClassId);
			} else // branch
			{
				qcolid = "select im.id from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and im.tid =(select tid from vehicle_tag_linking where vehicle_number is null and customer_id is null and barcode_data= ? and tag_class_id = ? )";
				preparedStmt4 = con.prepareStatement(qcolid);
				preparedStmt4.setString(1, tagClassId);
				preparedStmt4.setString(2, "3");
				preparedStmt4.setString(3, "0");
				preparedStmt4.setString(4, userid);
				preparedStmt4.setString(5, startBarcode);
				preparedStmt4.setString(6, tagClassId);
			}

			rs2 = preparedStmt4.executeQuery();

			if (rs2 != null && rs2.next()) {
				colid = rs2.getString("id");
				log.info("Start from barcode and tid (Inventory master ) ColId Value : " + colid);

				String sql = "select im.tid from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and vtl.vehicle_number is null and vtl.customer_id is null and im.id >= ? limit "
						+ count;
				// String sql1 = "update inventory_master set "+id+" = '"+idValue+"' where tid =
				// ?";
				String sql1 = "update inventory_master set " + id + " = '" + idValue
						+ "' ,pos_date=?,branch_date=? where tid = ?";
				// branch to agent
				String q1 = "select im.tid from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and vtl.vehicle_number is null and vtl.customer_id is null and im.id >= ? limit "
						+ count;
				// String q2 = "update inventory_master set "+id+" = '"+idValue+"' where tid =
				// ?";
				String q2 = "update inventory_master set " + id + " = '" + idValue
						+ "' ,pos_date=?,branch_date=? where tid = ?";
				log.info("UPDATE QUERY 1 :: " + sql1);
				log.info("UPDATE QUERY 2 :: " + q2);

				con.setAutoCommit(false);
				if (userid.equalsIgnoreCase("admin") || userid.startsWith("ST")) {
					preparedStmt = con.prepareStatement(sql);
					preparedStmt.setString(1, tagClassId);
					preparedStmt.setString(2, "3");
					preparedStmt.setString(3, "0");
					preparedStmt.setString(4, "0");
					preparedStmt.setString(5, colid);
					rs = preparedStmt.executeQuery();
					// log.info("Result Set ::: "+rs.getFetchSize());
					String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					preparedStmt1 = con.prepareStatement(sql1);
					while (rs != null && rs.next())/////////////////////////////////////////////////
					{
						// log.info("Update ::: "+sql1+" for TID = "+rs.getString("tid"));
						preparedStmt1.setString(1, rs.getString("tid"));
						preparedStmt1.setString(2, date);
						preparedStmt1.setString(3, date);
						preparedStmt1.addBatch();
						log.info("DATE UPDATED...");
					}
					preparedStmt1.executeBatch();

					log.info("DAOManager.java :: inside the admin and ST.........");
				}

				else // branch
				{
					log.info("Branch : " + userid + " ,  Tag allocated to agent id : " + idValue + "  , No of Tag :: "
							+ count);

					preparedStmt2 = con.prepareStatement(q1);
					preparedStmt2.setString(1, tagClassId);
					preparedStmt2.setString(2, "3");
					preparedStmt2.setString(3, "0");
					preparedStmt2.setString(4, userid); // login as a branch id
					preparedStmt2.setString(5, colid);
					rs = preparedStmt2.executeQuery();
					String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					preparedStmt3 = con.prepareStatement(q2);
					while (rs != null && rs.next())/////////////////////////////////////////////////////////
					{
						// log.info("Update ::: "+sql1+" for TID = "+rs.getString("tid"));
						preparedStmt3.setString(1, rs.getString("tid"));
						preparedStmt3.setString(2, date);
						preparedStmt3.setString(3, date);
						preparedStmt3.addBatch();
						log.info("DATE UPDATED...");
					}
					preparedStmt3.executeBatch();

					log.info("DAOManager.java :: inside Branch and agent .........");
				}
				con.commit();
				status = "1";

			}

			else {
				status = "2";
			}

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				log.error("DAOManager.java Getting Exception   :::    ", e1);
			}
			log.error("Something went wrong while UnAuthorizing ...", e);
			// status = "-2";
			status = e.getMessage();
		}

		finally {
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt4);
			DatabaseManager.closePreparedStatement(preparedStmt3);
			DatabaseManager.closePreparedStatement(preparedStmt2);
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}
		return status;
	}

	// bkp function before the changes related to start bar code data ...
	public String tagAllocateToAgent(String tagClassId, String id, String idValue, String count, String userid) {
		String status = "-1";

		Connection con = null;
		PreparedStatement preparedStmt = null;
		PreparedStatement preparedStmt1 = null;
		PreparedStatement preparedStmt2 = null;
		PreparedStatement preparedStmt3 = null;
		ResultSet rs = null;

		// admin
		String sql = "select im.tid from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and vtl.vehicle_number is null and vtl.customer_id is null limit "
				+ count;
		String sql1 = "update inventory_master set " + id + " = '" + idValue + "' where tid = ?";
		// branch to agent
		String q1 = "select im.tid from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and vtl.vehicle_number is null and vtl.customer_id is null limit "
				+ count;
		String q2 = "update inventory_master set " + id + " = '" + idValue + "' where tid = ?";

		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			con.setAutoCommit(false);

			if (userid.equalsIgnoreCase("admin") || userid.startsWith("ST")) {
				preparedStmt = con.prepareStatement(sql);
				preparedStmt.setString(1, tagClassId);
				preparedStmt.setString(2, "3");
				preparedStmt.setString(3, "0");
				preparedStmt.setString(4, "0");
				rs = preparedStmt.executeQuery();
				// log.info("Result Set ::: "+rs.getFetchSize());

				preparedStmt1 = con.prepareStatement(sql1);
				while (rs != null && rs.next()) {
					// log.info("Update ::: "+sql1+" for TID = "+rs.getString("tid"));
					preparedStmt1.setString(1, rs.getString("tid"));
					preparedStmt1.addBatch();
				}
				preparedStmt1.executeBatch();

			}

			else // branch
			{
				log.info("Branch : " + userid + " ,  Tag allocated to agent id : " + idValue + "  , No of Tag :: "
						+ count);

				preparedStmt2 = con.prepareStatement(q1);
				preparedStmt2.setString(1, tagClassId);
				preparedStmt2.setString(2, "3");
				preparedStmt2.setString(3, "0");
				preparedStmt2.setString(4, userid); // login as a branch id
				rs = preparedStmt2.executeQuery();

				preparedStmt3 = con.prepareStatement(q2);
				while (rs != null && rs.next()) {
					// log.info("Update ::: "+sql1+" for TID = "+rs.getString("tid"));
					preparedStmt3.setString(1, rs.getString("tid"));
					preparedStmt3.addBatch();
				}
				preparedStmt3.executeBatch();

			}
			con.commit();
			status = "1";

			log.info("Successfully Allocated.........");
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				log.error("DAOManager.java Getting Exception   :::    ", e1);
			}
			log.error("Something went wrong while UnAuthorizing ...", e);
			status = e.getMessage();
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt3);
			DatabaseManager.closePreparedStatement(preparedStmt2);
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}
		return status;
	}

	public void insertOrder(String idata, JSONObject resp) {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;

		JSONArray jsonArray;
		try {
			String q3 = null;
			String q4 = null;
			String poid = null;

			conn = DatabaseManager.getConnection();// new DBConnection().getConnection();

			conn.setAutoCommit(false);
			q3 = "Insert into inventory_master (po_id,tid,tag_class_id,tag_unique_id,branch_id,agent_id,status,date_time)values(?,?,?,?,?,?,?,?)";
			q4 = "update po_master set po_status = ? where po_id = ?";

			ps = conn.prepareStatement(q3);

			jsonArray = new JSONArray(idata);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject item = jsonArray.getJSONObject(i);
				String branchId = item.getString("branchid");
				String agentId = item.getString("agentid");
				String datetime = item.getString("datetime");
				poid = item.getString("poid");
				String status = item.getString("status");
				String tagclassid = item.getString("tagclassid");
				String taguniqueid = item.getString("taguniqueid");

				try {
					ps.setString(1, poid);
					ps.setString(2, taguniqueid);
					ps.setString(3, tagclassid);
					ps.setString(4, "0");
					ps.setString(5, branchId);
					ps.setString(6, agentId);
					if (status.equals("")) {
						ps.setString(7, "0");
					} else {
						ps.setString(7, status);
					}
					ps.setString(8, datetime);

					ps.addBatch();

				} catch (SQLException e) {
					log.error("DAOManager.java Getting Exception   :::    ", e);
				}
			}
			ps.executeBatch();

			ps1 = conn.prepareStatement(q4);
			ps1.setInt(1, 1);
			ps1.setString(2, poid);

			ps1.executeUpdate();

			conn.commit();
		} catch (ParseException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error("DAOManager.java Getting Exception   :::    ", e1);
			}
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} catch (SQLException e1) {
			log.error("DAOManager.java Getting Exception   :::    ", e1);
		}

		finally {
			// DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps1);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}

	}

	public JSONArray getCustomerAccount(String user_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * FROM account_master where user_id = ? and is_deleted=0";
			ps = con.prepareStatement(query);
			ps.setString(1, user_id);
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				if (rs.getString("is_active").equals("1")) {
					jo.put("isActive", true);
				} else {
					jo.put("isActive", false);
				}
				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}

				jo.put("accountNo", rs.getString("acc_no"));
				jo.put("accountType", rs.getString("account_type"));
				jo.put("bankName", rs.getString("bank_name"));
				jo.put("branchAddress", rs.getString("branch_address"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("ifscCode", rs.getString("ifsc_code"));
				jo.put("userId", rs.getString("user_id"));
				jo.put("isWallet", rs.getString("iswallet"));
				report.put(jo);

			}

		}

		catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		return report;
	}

	public JSONObject getCustomerAddress(String user_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * FROM address_master where user_id = ? and is_deleted=0 ";
			ps = con.prepareStatement(query);
			ps.setString(1, user_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}

				jo.put("Id", rs.getString("id"));
				jo.put("businessAddr1", rs.getString("business_add1"));
				jo.put("businessAddr2", rs.getString("business_add2"));
				jo.put("businessCity", rs.getString("business_city"));
				jo.put("businessPin", rs.getString("business_pin"));
				jo.put("businessState", rs.getString("business_state"));
				jo.put("resiAddr1", rs.getString("resi_add1"));
				jo.put("resiAddr2", rs.getString("resi_address2"));
				jo.put("resiCity", rs.getString("resi_city"));
				jo.put("resiPin", rs.getString("resi_pin"));
				jo.put("resiState", rs.getString("resi_state"));
				jo.put("userId", rs.getString("user_id"));

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo;
	}

	public JSONObject getCustomer(String cust_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;
		String query = null;
		// String isCommercial = null;

		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from customer_master where cust_id = ? and is_deleted = 0";
			ps = con.prepareStatement(query);
			ps.setString(1, cust_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				/*
				 * isCommercial = getCustomerIsCommercial(cust_id); // For HDFC
				 * 
				 * if(isCommercial != null && isCommercial.equalsIgnoreCase("1")) {
				 * jo.put("isCommercial", "true"); } else { jo.put("isCommercial", "false"); }
				 */

				if (rs.getString("is_corporate").equals("1")) {
					jo.put("isCommercial", "true");
				}

				else {
					jo.put("isCommercial", "false");
				}

				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}
				if (rs.getString("is_approved").equals("1")) {
					jo.put("isApproved", true);
				}

				else {
					jo.put("isApproved", false);
				}

				if (rs.getString("is_active").equals("1")) {
					jo.put("isActive", true);
				}

				else {
					jo.put("isActive", false);
				}

				jo.put("custId", rs.getString("cust_id"));
				jo.put("staffName", rs.getString("staff_name"));
				jo.put("approvedOn", rs.getString("approved_on"));
				jo.put("approvedby", rs.getString("approved_by"));
				jo.put("branchId", rs.getString("branch_id"));
				jo.put("contactNo", rs.getString("contact_number"));
				jo.put("createdBy", rs.getString("created_by"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("emailId", rs.getString("email_id"));
				jo.put("emailId", rs.getString("email_id"));
				jo.put("dob", rs.getString("dob"));
				jo.put("deleteStatus", rs.getString("delete_status"));
				jo.put("customer_type", rs.getString("customer_type"));
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo;
	}

	public JSONObject getCustomerKYC(String cust_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from customer_kyc_master where cust_id = ? and is_deleted=0";
			ps = con.prepareStatement(query);
			ps.setString(1, cust_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();
				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", false);
				} else {
					jo.put("isDeleted", true);
				}

				jo.put("addresProofDocPath", rs.getString("address_proof_doc_path")); // aadhar

				jo.put("addressProofId", rs.getString("address_proof_id"));
				jo.put("addressProofNo", base64Decoded(rs.getString("address_proof_no")));
				jo.put("custId", rs.getString("cust_id"));

				jo.put("idProof", rs.getString("id_proof"));
				jo.put("idProofDocPath", rs.getString("id_proof_doc_path")); // pan
				jo.put("idProofNo", base64Decoded(rs.getString("id_proof_no")));

			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		return jo;
	}

	private static String base64Decoded(String encodedString) {
		String decodedString = null;
		if (encodedString != null || encodedString != "") {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
			decodedString = new String(decodedBytes);

		}
		return decodedString;
	}

	public JSONArray getCustomerVehicle(String user_id, String cust_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			if (user_id.startsWith("C")) {
				log.info("DAOManager.java :: getCustomerVehicle() if part.. ");
				query = "SELECT * FROM customer_vehicle_master where cust_id = ? and is_deleted= ?  and vehicle_number NOT IN (select vehicle_number FROM vehicle_tag_linking  where vehicle_number is not null)";
			}

			else {
				log.info("DAOManager.java :: getCustomerVehicle() else part..  ");
				query = "SELECT * FROM customer_vehicle_master where cust_id = ? and is_deleted= ? and vehicle_number NOT IN (select vehicle_number FROM vehicle_tag_linking  where vehicle_number is not null)";
			}

			ps = con.prepareStatement(query);
			ps.setString(1, cust_id);
			ps.setString(2, "0");
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();

				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}
				jo.put("Documents", getVehicleDoc(rs.getString("vehicle_number")));
				jo.put("chassisNumber", rs.getString("chassis_number"));
				jo.put("createdOn", rs.getString("created_on"));
				jo.put("custId", rs.getString("cust_id"));
				jo.put("engineNumber", rs.getString("engine_number"));
				if (rs.getString("is_commercial").equals("1")) {
					jo.put("isCommercial", true);
				} else {
					jo.put("isCommercial", false);
				}

				jo.put("vehicleClassId", rs.getString("vehicle_class_id"));
				jo.put("vehicleId", rs.getString("vehicle_id"));
				jo.put("vehicleNumber", rs.getString("vehicle_number"));

				report.put(jo);

			}

		} catch (Exception e) {
			log.error("DAOManager.java :: getCustomerVehicle() :: Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		return report;
	}

	public JSONObject getVehicleDoc(String cust_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		JSONObject jo = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * from vehicle_document_master where vehicle_number = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, cust_id);

			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", false);
				} else {
					jo.put("isDeleted", true);
				}

				jo.put("otherDoc1", rs.getString("other_doc1"));
				jo.put("otherDoc2", rs.getString("other_doc2"));
				jo.put("otherDoc3", rs.getString("other_doc3"));
				jo.put("pathBackPic", rs.getString("path_back_pic"));

				jo.put("pathFrontPic", rs.getString("path_front_pic"));
				jo.put("pathInsurance", rs.getString("path_insurance"));
				jo.put("pathRCBook", rs.getString("path_rc_book"));
				jo.put("vehicleNumber", rs.getString("vehicle_number"));

			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {

			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jo;
	}

	public String getSingleCustomerData(String userId) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			// changes for filter
			if (userId.equalsIgnoreCase("admin") || userId.startsWith("STCH")) {
				query = "select * from customer_master where is_deleted = 0 and is_approved<2 order by created_on desc limit 20";
				ps = con.prepareStatement(query);
			} else if (userId.startsWith("C") || userId.startsWith("F")) // F for Fleet Owner as a customer
			{
				query = "select cust_id from customer_master where cust_id = ? and is_deleted = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, userId);
				ps.setString(2, "0");
			} else if (userId.startsWith("ST")) // || userId.startsWith("ST"))
			{
				query = "select * from customer_master where is_deleted = 0 and created_by=? order by created_on desc limit 20";
				ps = con.prepareStatement(query);
				ps.setString(1, userId);
			} else {
				query = "select cust_id from customer_master where created_by = ? and is_deleted = 0 order by created_on desc limit 20";
				ps = con.prepareStatement(query);
				ps.setString(1, userId);
			}

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();

				jo.put("Accounts", getCustomerAccount(rs.getString("cust_id")));

				jo.put("Address", getCustomerAddress(rs.getString("cust_id")));

				jo.put("Customer", getCustomer(rs.getString("cust_id")));

				jo.put("CustomerKYC", getCustomerKYC(rs.getString("cust_id")));

				jo.put("Vehicles", getCustomerVehicle(userId, rs.getString("cust_id")));

				report = report.put(jo);
			}

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return report.toString();
	}

	public String orderFullfillmentGrid() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;

		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "select po_id from po_master order by rodt desc";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				jo = new JSONObject();

				jo.put("orderfullfillment", orderGrid(rs.getString("po_id")));

			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		return jo.toString();
	}

	public JSONArray orderGrid(String po_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		JSONObject jo = null;

		try {
			con = DatabaseManager.getConnection(); // new DBConnection().getConnection();
			query = "SELECT approved_on,po_id,po_status,supp_id,total_order_value from po_master where po_status!=? order by rodt desc";
			ps = con.prepareStatement(query);
			ps.setInt(1, 6);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();

				jo.put("approvedon", rs.getString("approved_on"));
				jo.put("poid", rs.getString("po_id"));
				jo.put("postatus", rs.getString("po_status"));
				jo.put("suppid", rs.getString("supp_id"));
				jo.put("totalordervalue", rs.getString("total_order_value"));

				report.put(jo);
			}

		} catch (Exception e) {

			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		return report;
	}

	public boolean editSupplier(String supplierId, String supplierName, String emailId, String contactPerson,
			String contactNumber1, String contactNumber2, String deliveryPeriod, String maxOrderQty, String minOrderQty,
			String is_npci_certified, String npci_certification_expiry, String GSTN, String createdBy, String webSite,
			String status, String resiAddr1, String resiAddr2, String resiPin, String resiCity, String resiState,
			String businessAddr1, String businessAddr2, String businessPin, String businessCity, String businessState,
			String accountNo, String bankName, String branchAddress, String ifscCode, String accountType,
			String isModifiedby) {
		boolean isadded = false;

		Connection con = null;
		PreparedStatement preparedStmt = null;
		PreparedStatement preparedStmt1 = null;
		PreparedStatement preparedStmt2 = null;

		// String sql = "update supplier_master set supplier_id=?, supplier_name=?,
		// email_id=?, contact_person=?, contact_number1=?, contact_number2=?,
		// web_site=?, delivery_period=?,
		// max_order_qty=?,min_order_qty=?,is_npci_certified=?,npci_certification_expiry=?,gstn=?,created_by=?,created_on=?,approved_by=?,approved_on=?,status=?
		// where supplier_id='"+supplierId+"'";
		String sql = "update supplier_master set supplier_id=?, supplier_name=?, email_id=?, contact_person=?, contact_number1=?, contact_number2=?, web_site=?, delivery_period=?, max_order_qty=?,min_order_qty=?,is_npci_certified=?,npci_certification_expiry=?,gstn=?,rodt=?,isModifiedby=?,status=? where supplier_id='"
				+ supplierId + "'";

		String sql1 = "update address_master  set user_id=?, resi_add1=?, resi_address2=?, resi_pin=?, resi_city=?, resi_state=?, business_add1=?, business_add2=?, business_pin=?, business_city=?, business_state=? where user_id='"
				+ supplierId + "'";

		String sql2 = "update account_master  set user_id=?, acc_no=?, bank_name=?, branch_address=?, ifsc_code=?, account_type=?, created_on=?, is_active=? where user_id='"
				+ supplierId + "'";
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			preparedStmt = con.prepareStatement(sql);// for supplier master

			preparedStmt.setString(1, supplierId);

			preparedStmt.setString(2, supplierName);
			preparedStmt.setString(3, emailId);
			preparedStmt.setString(4, contactPerson);
			preparedStmt.setString(5, contactNumber1);
			preparedStmt.setString(6, contactNumber2);
			preparedStmt.setString(7, webSite);
			preparedStmt.setString(8, deliveryPeriod);
			preparedStmt.setString(9, maxOrderQty);
			preparedStmt.setString(10, minOrderQty);
			preparedStmt.setString(11, is_npci_certified);
			preparedStmt.setString(12, npci_certification_expiry);
			preparedStmt.setString(13, GSTN);
			preparedStmt.setString(14, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			preparedStmt.setString(15, isModifiedby);
			preparedStmt.setString(16, status);
			/*
			 * preparedStmt.setString(14, createdBy); preparedStmt.setTimestamp(15, new
			 * Timestamp(System.currentTimeMillis())); preparedStmt.setString(16,
			 * createdBy); preparedStmt.setTimestamp(17, new
			 * Timestamp(System.currentTimeMillis()));
			 * 
			 * preparedStmt.setString(18, status);
			 */

			preparedStmt.executeUpdate();

			preparedStmt1 = con.prepareStatement(sql1);// for address master
			preparedStmt1.setString(1, supplierId);
			preparedStmt1.setString(2, resiAddr1);
			preparedStmt1.setString(3, resiAddr2);
			preparedStmt1.setString(4, resiPin);
			preparedStmt1.setString(5, resiCity);
			preparedStmt1.setString(6, resiState);
			preparedStmt1.setString(7, businessAddr1);
			preparedStmt1.setString(8, businessAddr2);
			preparedStmt1.setString(9, businessPin);
			preparedStmt1.setString(10, businessCity);
			preparedStmt1.setString(11, businessState);
			// preparedStmt1.setString(12, agentId);
			preparedStmt1.executeUpdate();

			preparedStmt2 = con.prepareStatement(sql2);// for account master
			preparedStmt2.setString(1, supplierId);
			preparedStmt2.setString(2, accountNo);
			preparedStmt2.setString(3, bankName);
			preparedStmt2.setString(4, branchAddress);
			preparedStmt2.setString(5, ifscCode);
			preparedStmt2.setString(6, accountType);
			preparedStmt2.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStmt2.setString(8, "0");
			preparedStmt2.executeUpdate();

			isadded = true;
			log.info("Successfully edited supplier.........");
		} catch (Exception e) {
			log.info("Something wrong while inserting..." + e);
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(preparedStmt2);
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}
		return isadded;
	}

	public String getDashboard(String userId, String roleId, String todaysDate, String yDate, Connection conn) {

		CallableStatement cs = null;
		JSONObject jo = new JSONObject();
		try {

			String sql = "{CALL pr_dashboard(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			cs = conn.prepareCall(sql);
			cs.setString(1, userId);
			cs.setString(2, roleId);
			cs.setString(3, todaysDate);
			cs.setString(4, yDate);
			cs.registerOutParameter(5, java.sql.Types.VARCHAR);
			cs.registerOutParameter(6, java.sql.Types.VARCHAR);
			cs.registerOutParameter(7, java.sql.Types.VARCHAR);
			cs.registerOutParameter(8, java.sql.Types.VARCHAR);
			cs.registerOutParameter(9, java.sql.Types.VARCHAR);
			cs.registerOutParameter(10, java.sql.Types.VARCHAR);
			cs.registerOutParameter(11, java.sql.Types.VARCHAR);
			cs.registerOutParameter(12, java.sql.Types.VARCHAR);
			cs.registerOutParameter(13, java.sql.Types.VARCHAR);
			cs.registerOutParameter(14, java.sql.Types.VARCHAR);
			cs.registerOutParameter(15, java.sql.Types.VARCHAR);
			cs.registerOutParameter(16, java.sql.Types.VARCHAR);
			cs.registerOutParameter(17, java.sql.Types.VARCHAR);
			cs.registerOutParameter(18, java.sql.Types.VARCHAR);
			cs.registerOutParameter(19, java.sql.Types.VARCHAR);
			cs.registerOutParameter(20, java.sql.Types.VARCHAR);

			cs.execute();

			jo.put("activeTags", cs.getString(5));
			jo.put("lowbalance", cs.getString(6));
			jo.put("blacklist", cs.getString(7));
			jo.put("pendingPO", cs.getString(8));
			jo.put("txnCount", cs.getString(9));
			jo.put("txnAmt", cs.getDouble(10));
			jo.put("ytxnCount", cs.getString(11));
			jo.put("yunstxnCount", cs.getString(12));
			jo.put("ytxnAmt", cs.getDouble(13));
			jo.put("customers", cs.getString(14));
			jo.put("vehicles", cs.getString(15));
			jo.put("vehiclesPending", cs.getString(16));
			jo.put("processedTags", cs.getString(17));
			jo.put("inQue", cs.getString(18));
			jo.put("pendingTags", cs.getString(19));
			jo.put("invalidTags", cs.getString(20));

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
		}
		return jo.toString();

	}

	public String getDashboard(String userId, Connection conn) {
		CallableStatement cs = null;
		JSONObject jo = new JSONObject();
		try {
			String sql = "{CALL pr_cust_dashboard(?,?,?,?,?,?,?,?,?,?)}";
			cs = conn.prepareCall(sql);
			cs.setString(1, userId);
			cs.registerOutParameter(2, java.sql.Types.VARCHAR);
			cs.registerOutParameter(3, java.sql.Types.VARCHAR);
			cs.registerOutParameter(4, java.sql.Types.VARCHAR);
			cs.registerOutParameter(5, java.sql.Types.VARCHAR);
			cs.registerOutParameter(6, java.sql.Types.VARCHAR);
			cs.registerOutParameter(7, java.sql.Types.VARCHAR);
			cs.registerOutParameter(8, java.sql.Types.VARCHAR);
			cs.registerOutParameter(9, java.sql.Types.VARCHAR);
			cs.registerOutParameter(10, java.sql.Types.VARCHAR);

			cs.execute();

			jo.put("todaysTxnCount", cs.getString(2));
			jo.put("todaysTxnAmount", cs.getString(3));
			jo.put("yesterdaysTxnCount", cs.getString(4));
			jo.put("yesterdaysTxnAmount", cs.getString(5));
			jo.put("customerName", cs.getString(6));
			jo.put("walletId", cs.getString(7));
			jo.put("totalVehicles", cs.getString(8));
			jo.put("currentBalance", cs.getString(9));
			jo.put("walletValidity", cs.getString(10));

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
		}
		return jo.toString();

	}

	public String getBranchDashboard(String userId, Connection conn) {
		CallableStatement cs = null;
		JSONObject jo = new JSONObject();
		try {
			String sql = "{CALL pr_branch_dashboard(?,?,?,?,?)}";
			cs = conn.prepareCall(sql);
			cs.setString(1, userId);
			cs.registerOutParameter(2, java.sql.Types.INTEGER);
			cs.registerOutParameter(3, java.sql.Types.INTEGER);
			cs.registerOutParameter(4, java.sql.Types.INTEGER);
			cs.registerOutParameter(5, java.sql.Types.INTEGER);

			cs.execute();

			jo.put("activeVehicle", cs.getInt(2));
			jo.put("totalCustomers", cs.getInt(3));
			jo.put("availableTags", cs.getInt(4));
			jo.put("allocatedTags", cs.getInt(5));

		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
		}
		return jo.toString();

	}

	public JSONArray getRoleIdListForStaff() {
		JSONObject getData = null;
		JSONArray getReport = null;
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			conn = DatabaseManager.getConnection();// new DBConnection().getConnection();
			if (conn != null) {
				String query = "select rm.group_id as groupId,group_desc as groupName,role_description as roleDescription,case when rm.is_deleted = 1 then 'true' else 'false' end as isDeleted,role_id as roleId"
						+ " from role_master rm inner join user_group_master gm on rm.group_id = gm.group_id "
						+ "where rm.is_deleted = 0 and gm.is_deleted = 0 and gm.group_id > 11 order by role_id DESC ";

				log.info("query ::: " + query);
				ps = conn.prepareStatement(query);

				rs = ps.executeQuery();

				getReport = new JSONArray();
				while (rs != null && rs.next()) {
					getData = new JSONObject();

					rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						if (rsmd.getColumnTypeName(i).equals("VARCHAR")
								|| rsmd.getColumnTypeName(i).equals("TIMESTAMP")) {
							getData.put(rsmd.getColumnLabel(i), rs.getString(i));
						} else if (rsmd.getColumnTypeName(i).equals("BOOLEAN")) {
							getData.put(rsmd.getColumnLabel(i), rs.getBoolean(i));
						} else if (rsmd.getColumnTypeName(i).equals("DOUBLE")
								|| rsmd.getColumnTypeName(i).equals("FLOAT")) {
							getData.put(rsmd.getColumnLabel(i), rs.getDouble(i));
						} else {
							getData.put(rsmd.getColumnLabel(i), rs.getInt(i));
						}
					}
					getReport.put(getData);
				}

				// log.info("DAOManager.java ::: getRoleIdListForStaff() :: DropDownList Of role
				// Id ");
				// log.info("List Data : "+getReport);
			}
		} catch (Exception e) {
			log.error("DAOManager.java Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return getReport;
	}

}
