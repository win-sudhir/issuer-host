package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;

import org.apache.log4j.Logger;
//import org.apache.poi.hssf.util.HSSFColor.DARK_TEAL;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class GetPOSListDao {
	static Logger log = Logger.getLogger(GetPOSListDao.class.getName());

	public String getPoSList(String branchId) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();
			query = "SELECT agent_id,agent_name,contact_number from sales_agent_master where is_active=1 and is_deleted=0 and branch_id=?";
			log.info("Query :: " + query);
			ps = con.prepareStatement(query);
			ps.setString(1, branchId);

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("agentId", rs.getString("agent_id"));
				jo.put("agentName", rs.getString("agent_name"));
				jo.put("contactNumber", rs.getString("contact_number"));
				report.put(jo);
			}

		} catch (Exception e) {
			log.error("GetPOSListDao.java Getting Error   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("POSList", report);
		return mainObj.toString();
	}

	public String getKYCList() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject jo = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();
			query = "SELECT * from tbl_kyc_list";
			log.info("getKYCList Query :: " + query);
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("add_ProofId", rs.getString("add_proof_id"));
				jo.put("add_ProofIdName", rs.getString("add_proof_id_name"));
				report.put(jo);
			}

		} catch (Exception e) {
			log.error("GetPOSListDao.java Getting Error   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("kycList", report);
		return mainObj.toString();
	}
}
