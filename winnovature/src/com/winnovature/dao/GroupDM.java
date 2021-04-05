package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

//import com.netc.utils.DBConnection;

public class GroupDM {
	static Logger log = Logger.getLogger(GroupDM.class.getName());

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
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return jo.toString();
	}

	public boolean checkGroup(String groupDesc) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();
			String sql = "SELECT * FROM user_group_master where group_desc=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, groupDesc);
			rs = ps.executeQuery();
			if (rs.next()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return false;
	}

	public String grpDelete(String grpId, String grpDesc) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();
			String sql = "CALL deleteGroup(?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, grpId);
			ps.setString(2, grpDesc);
			rs = ps.executeQuery();
			if (rs.next()) {
				// return true;
				jo.put("groupId", rs.getString("grpId"));
				jo.put("Status", true);
				jo.put("Message", "Group deleted Successfully");
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return jo.toString();

	}

	public JSONArray getGroups() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();
			query = "SELECT * FROM user_group_master where is_deleted=?";
			ps = con.prepareStatement(query);
			ps.setString(1, "0");
			// ps.setString(2,toDate);
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				if (rs.getString("is_deleted").equals("1")) {
					jo.put("isDeleted", true);
				} else {
					jo.put("isDeleted", false);
				}

				jo.put("groupDescription", rs.getString("group_desc"));
				jo.put("groupId", rs.getString("group_id"));
				report.put(jo);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		return report;
	}

	public String groupEdit(String groupDesc, String groupId) {

		Connection conn = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		JSONObject jo = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();
			String sql = "UPDATE user_group_master SET group_desc=? where group_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, groupDesc);
			ps.setString(2, groupId);

			ps.executeUpdate();

			jo.put("Status", true);
			jo.put("Message", "Group Updated Successfully");
			jo.put("groupId", groupId);
			jo.put("group_desc", groupDesc);

		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			// DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return jo.toString();

	}

	public boolean isGroupDeleted(String groupDesc) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();
			String sql = "SELECT * FROM user_group_master where group_desc=? and is_deleted=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, groupDesc);
			ps.setString(2, "1");

			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return false;
	}

	public String updateGroup(String groupDesc) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();
			String sql = "CALL updateGroup(?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, groupDesc);
			rs = ps.executeQuery();
			if (rs.next()) {
				// return true;
				jo.put("groupId", rs.getString("group_id"));
				jo.put("group_desc", rs.getString("group_desc"));
				jo.put("Status", true);
				jo.put("Message", "Existing Group Re-Enabled sucessfully");
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return jo.toString();
	}

}
