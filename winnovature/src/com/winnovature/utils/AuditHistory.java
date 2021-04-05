
package com.winnovature.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AuditHistory {
	static Logger log = Logger.getLogger(AuditHistory.class.getName());

	public List<String> getAuditXMLHistoryCustomer(String cust_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String query = null;
		ArrayList list = new ArrayList();

		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM customer_master where cust_id=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, cust_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("cust_id"));
				list.add(rs.getString("branch_id"));
				list.add(rs.getString("staff_name"));
				list.add(rs.getString("email_id"));
				list.add(rs.getString("contact_number"));
				list.add(rs.getString("dob"));
				list.add(rs.getString("is_active"));
				list.add(rs.getString("is_corporate"));
				list.add(rs.getString("created_by"));
				list.add(rs.getString("created_on"));
				list.add(rs.getString("approved_by"));
				list.add(rs.getString("approved_on"));
				list.add(rs.getString("is_deleted"));
				list.add(rs.getString("is_approved"));
				log.info("userData :: " + cust_id);
			}
		} catch (Exception e) {
			log.error("AuditHistory.java Getting Error while getAuditXML()  :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		System.out.println("list " + list);
		return list;
	}

	public List<String> getAuditXMLHistoryStaff(String staff_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String query = null;
		ArrayList list = new ArrayList();

		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM staff_master where staff_id=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, staff_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("role_id"));
				list.add(rs.getString("staff_id"));
				list.add(rs.getString("contact_number"));
				list.add(rs.getString("email_id"));
				list.add(rs.getString("is_active"));
				list.add(rs.getString("staff_name"));
				list.add(rs.getString("created_by"));
				list.add(rs.getString("created_on"));
				list.add(rs.getString("approved_by"));
				list.add(rs.getString("approved_on"));
				list.add(rs.getString("is_approved"));
				list.add(rs.getString("is_deleted"));
				list.add(rs.getString("rodt"));
				log.info("userData :: " + staff_id);
			}
		} catch (Exception var16) {
			log.error("AuditHistory.java Getting Error while getAuditXML()  :::    ", var16);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		System.out.println("list " + list);
		return list;
	}

	public List<String> getAuditXMLHistoryStaffAddres(String user_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String query = null;
		ArrayList<String> list = new ArrayList<String>();

		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM address_master where user_id=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, user_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("user_id"));
				list.add(rs.getString("resi_add1"));
				list.add(rs.getString("resi_pin"));
				list.add(rs.getString("resi_city"));
				list.add(rs.getString("resi_state"));
				list.add(rs.getString("business_add1"));
				list.add(rs.getString("business_add2"));
				list.add(rs.getString("business_pin"));
				list.add(rs.getString("business_city"));
				list.add(rs.getString("business_state"));
				list.add(rs.getString("is_deleted"));
				log.info("userData :: " + user_id);
			}
		} catch (Exception e) {
			log.error("AuditHistory.java Getting Error while getAuditXML()  :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		System.out.println("list " + list);
		return list;
	}

	public List<String> getAuditXMLHistorySalesAgent(String agent_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String query = null;
		ArrayList<String> list = new ArrayList<String>();

		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM sales_agent_master where agent_id=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, agent_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("agent_id"));
				list.add(rs.getString("branch_id"));
				list.add(rs.getString("agent_name"));
				list.add(rs.getString("email_id"));
				list.add(rs.getString("contact_number"));
				list.add(rs.getString("is_active"));
				list.add(rs.getString("created_by"));
				list.add(rs.getString("created_on"));
				list.add(rs.getString("approved_by"));
				list.add(rs.getString("approved_on"));
				list.add(rs.getString("is_approved"));
				list.add(rs.getString("is_deleted"));
				list.add(rs.getString("rodt"));
				log.info("userData :: " + agent_id);
			}
		} catch (Exception e) {
			log.error("AuditHistory.java Getting Error while getAuditXML()  :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		System.out.println("list " + list);
		return list;
	}

	public List<String> getAuditXMLHistorybranch(String branch_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String query = null;
		ArrayList<String> list = new ArrayList<String>();

		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM branch_master where branch_id=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, branch_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("branch_id"));
				list.add(rs.getString("branch_name"));
				list.add(rs.getString("email_id"));
				list.add(rs.getString("contact_number"));
				list.add(rs.getString("is_active"));
				list.add(rs.getString("created_by"));
				list.add(rs.getString("created_on"));
				list.add(rs.getString("approved_by"));
				list.add(rs.getString("approved_on"));
				list.add(rs.getString("is_approved"));
				list.add(rs.getString("is_deleted"));
				list.add(rs.getString("rodt"));
				log.info("userData :: " + branch_id);
			}
		} catch (Exception e) {
			log.error("AuditHistory.java Getting Error while getAuditXML()  :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		System.out.println("list " + list);
		return list;
	}

	public List<String> getAuditXMLHistorySupplier(String supplier_id) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String query = null;
		ArrayList list = new ArrayList();

		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM supplier_master where supplier_id=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, supplier_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("supplier_id"));
				list.add(rs.getString("supplier_name"));
				list.add(rs.getString("email_id"));
				list.add(rs.getString("max_order_qty"));
				list.add(rs.getString("min_order_qty"));
				list.add(rs.getString("contact_person"));
				list.add(rs.getString("contact_number1"));
				list.add(rs.getString("contact_number2"));
				list.add(rs.getString("web_site"));
				list.add(rs.getString("delivery_period"));
				list.add(rs.getString("is_npci_certified"));
				list.add(rs.getString("npci_certification_expiry"));
				list.add(rs.getString("npci_certification_expiry"));
				list.add(rs.getString("gstn"));
				list.add(rs.getString("created_by"));
				list.add(rs.getString("created_on"));
				list.add(rs.getString("approved_by"));
				list.add(rs.getString("approved_on"));
				list.add(rs.getString("approved_on"));
				list.add(rs.getString("status"));
				list.add(rs.getString("is_deleted"));
				list.add(rs.getString("rodt"));
				log.info("userData :: " + supplier_id);
			}
		} catch (Exception e) {
			log.error("AuditHistory.java Getting Error while getAuditXML()  :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		System.out.println("list " + list);
		return list;
	}
}
