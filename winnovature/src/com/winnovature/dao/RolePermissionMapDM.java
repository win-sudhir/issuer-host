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

public class RolePermissionMapDM {
	static Logger log = Logger.getLogger(RolePermissionMapDM.class.getName());

	public JSONArray getRolePemission() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();
			query = "Select group_concat(distinct rpm.roleid) as role_id, group_concat(rpm.permissionid) as permissions,rm.role_description from role_permissions_map rpm INNER JOIN permissions_master pm ON rpm.permissionid=pm.permissionid "
					+ "INNER JOIN role_master rm ON rpm.roleid=rm.role_id where rpm.is_deleted=? and rm.is_deleted=? and pm.is_deleted=? group by rm.role_id";
			ps = con.prepareStatement(query);

			ps.setString(1, "0");
			ps.setString(2, "0");
			ps.setString(3, "0");
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("roleId", rs.getString("role_id"));
				jo.put("role", rs.getString("role_description"));
				jo.put("Permissions", getPemissionData(rs.getString("permissions")));
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
		/*
		 * JSONObject mainObj = new JSONObject(); mainObj.put("Menus", report);
		 * mainObj.put("RoleId", roleId); mainObj.put("UserId", userId);
		 */
		return report;

	}

	public JSONArray getPemissionData(String PermissionData) {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		// System.out.print("Submenu : "+MenuId);
		try {
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();
			query = "SELECT * FROM permissions_master where permissionid IN (" + PermissionData + ") and is_deleted=?";
			ps = con.prepareStatement(query);
			ps.setString(1, "0");
			// ps.setString(2,toDate);
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();

				jo.put("modulename", rs.getString("modulename"));
				jo.put("permission", rs.getString("permission"));
				jo.put("permissionId", rs.getString("permissionid"));
				// jo.put("submenuurl", rs.getString("sub_menu_url"));
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

}
