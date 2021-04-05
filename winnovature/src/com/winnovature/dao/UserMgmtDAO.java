package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class UserMgmtDAO {
	static Logger log = Logger.getLogger(UserMgmtDAO.class.getName());

	public boolean getLogin(Connection conn, String userid, String pass, String ipAddress) throws SQLException {
		// Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PreparedStatement pst = null;

		try {
			// conn = DatabaseManager.getConnection();//new DBConnection().getConnection();
			String sql = "SELECT * FROM user_master where user_id = ? and password = ? and is_active = ? and is_deleted = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, pass);
			ps.setString(3, "1");
			ps.setString(4, "0");
			rs = ps.executeQuery();
			if (rs.next()) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String lastLoginTime = dateFormat.format(date);

				String updateQuery = "update user_master set last_login_date = ?, last_login_ip = ? where user_id = ? and password = ? ";
				pst = conn.prepareStatement(updateQuery);
				pst.setString(1, lastLoginTime);
				pst.setString(2, ipAddress);
				pst.setString(3, userid);
				pst.setString(4, pass);
				pst.executeUpdate();

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pst);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(conn);

		}
		return false;
	}

	public JSONArray permissions() {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT permissionid,modulename,permission FROM permissions_master";
			ps = con.prepareStatement(query);
			// ps.setString(1,fromDate);
			// ps.setString(2,toDate);
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("permissionid", rs.getString("permissionid"));
				jo.put("modulename", rs.getString("modulename"));
				jo.put("permission", rs.getString("permission"));

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
		 * JSONObject mainObj = new JSONObject(); mainObj.put("reports", report);
		 */
		return report;
	}

	public String addMenu(String menuDesc, String menuLink, String menuShortName, String isActive) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();// new DBConnection().getConnection();
			String sql = "CALL addMenu(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuDesc);
			ps.setString(2, menuLink);
			ps.setString(3, menuShortName);
			ps.setString(4, isActive);
			rs = ps.executeQuery();
			if (rs.next()) {
				// return true;

				jo.put("isActive", isActive);
				jo.put("isDeleted", false);
				jo.put("Message", "Menu Created Successfully");
				jo.put("Status", true);
				jo.put("menuId", rs.getString("last_id"));
				jo.put("menu_desc", menuDesc);
				jo.put("menu_link", menuLink);
				jo.put("menu_shortname", menuShortName);

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

	public boolean checkMenu(String menuDesc, String menuLink, String menuShortName, String isActive) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();// new DBConnection().getConnection();
			String sql = "SELECT * FROM menu_master where menu_description=? and menu_link=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuDesc);
			ps.setString(2, menuLink);
			// ps.setString(3, menuShortName);
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

	public JSONArray getMenu() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * FROM menu_master where is_deleted=?";
			ps = con.prepareStatement(query);
			ps.setString(1, "0");
			// ps.setString(2,toDate);
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

				jo.put("menuDescription", rs.getString("menu_description"));
				jo.put("menuId", rs.getString("menu_id"));
				jo.put("menuLink", rs.getString("menu_link"));
				jo.put("menushortName", rs.getString("short_name"));
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

	public String menuDelete(String menu) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();// new DBConnection().getConnection();
			String sql = "CALL deleteMenu(?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menu);

			rs = ps.executeQuery();
			if (rs.next()) {
				// return true;
				jo.put("menuId", rs.getString("menuId"));
				jo.put("Status", true);
				jo.put("Message", "Menu deleted Successfully");
			} else {
				jo.put("Status", false);
				jo.put("Message", "Menu deleted Failed");
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

	public String menuEdit(String menuDesc, String menuLink, String shortName, String isactive, String menuId) {

		Connection conn = null;
		PreparedStatement ps = null;
		// ResultSet rs = null;
		JSONObject jo = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();// new DBConnection().getConnection();
			String sql = "UPDATE menu_master SET menu_description=?,menu_link=?,short_name=?,is_active=? where menu_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuDesc);
			ps.setString(2, menuLink);
			ps.setString(3, shortName);
			ps.setString(4, isactive);
			ps.setString(5, menuId);
			ps.executeUpdate();
			if (isactive.equals("1")) {
				jo.put("IsActive", true);
			} else {
				jo.put("IsActive", false);
			}
			jo.put("IsDeleted", null);
			jo.put("Status", true);
			jo.put("Message", "Menu Updated Successfully");

		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			// DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return jo.toString();

	}

	public JSONObject getModule() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT * FROM modules where is_deleted = ?";
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

				jo.put("moduleName", rs.getString("module_name"));
				jo.put("permittedOperations", rs.getString("permitted_operations"));
				jo.put("Id", rs.getString("id"));
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
		JSONObject mainObj = new JSONObject();
		mainObj.put("Modules", report);
		return mainObj;
	}

	public JSONArray getRoleGroup() {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT rm.role_id,rm.group_id,ugm.group_desc,rm.role_description,rm.is_deleted from role_master rm inner join user_group_master ugm on rm.group_id=ugm.group_id where rm.is_deleted=?";
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

				jo.put("roleId", rs.getString("role_id"));
				jo.put("groupId", rs.getString("group_id"));
				jo.put("groupName", rs.getString("group_desc"));
				jo.put("roleDescription", rs.getString("role_description"));

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
		 * JSONObject mainObj = new JSONObject(); mainObj.put("Modules", report);
		 */
		return report;
	}
	/*
	 * public static void main(String[] args){ DataManager dm = new DataManager();
	 * log.info(dm.getRoleGroup()); }
	 */

	public boolean isMenuDeleted(String menuDesc, String menuLink, String menuShortName, String isActive) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();// new DBConnection().getConnection();
			String sql = "SELECT * FROM menu_master where menu_description=? and menu_link=? and short_name = ? and is_deleted=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuDesc);
			ps.setString(2, menuLink);
			ps.setString(3, menuShortName);
			ps.setString(4, "1");

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

	public String updateMenu(String menuDesc, String menuLink, String menuShortName, String isActive) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();// new DBConnection().getConnection();
			String sql = "CALL updateMenu(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuDesc);
			ps.setString(2, menuLink);
			ps.setString(3, menuShortName);
			ps.setString(4, isActive);
			rs = ps.executeQuery();
			if (rs.next()) {
				// return true;
				jo.put("menuId", rs.getString("menu_id"));
				jo.put("menu_desc", rs.getString("menu_description"));
				jo.put("menu_link", rs.getString("menu_link"));
				jo.put("menu_shortname", rs.getString("short_name"));
				jo.put("Status", true);
				jo.put("Message", "Existing Menu Re-Enabled sucessfully");
				if (rs.getString("is_active").equals("0")) {
					jo.put("IsActive", true);
				} else {
					jo.put("IsActive", false);
				}
				if (rs.getString("is_deleted").equals("0")) {
					jo.put("IsDeleted", true);
				} else {
					jo.put("IsDeleted", false);
				}
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

	public JSONObject getLoginDetails(Connection con, String userId, String password) {

		PreparedStatement ps = null;
		//Connection con = null;
		ResultSet rs = null;
		// JSONArray report = null;
		String query = null;
		JSONObject jo = new JSONObject();
		// password.toString().toUpperCase();

		try {

			//con = DatabaseManager.getConnection();// new DBConnection().getConnection();
			query = "SELECT um.user_id,um.role_id,rm.role_description FROM user_master um INNER JOIN role_master rm on um.role_id = rm.role_id where um.user_id=? and um.password=?";
			ps = con.prepareStatement(query);
			ps.setString(1, userId);
			ps.setString(2, password.toString().toUpperCase());
			rs = ps.executeQuery();
			// report = new JSONArray();
			if (rs.next()) {

				jo.put("role", rs.getString("role_description"));
				jo.put("roleId", rs.getString("role_id"));
				jo.put("userId", rs.getString("user_id"));

				// report.put(jo);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closeConnection(con);

		}
		/*
		 * JSONObject mainObj = new JSONObject(); mainObj.put("reports", report);
		 */
		return jo;
	}

	public String isFirstLoginFlag(Connection conn, String userId) {
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;

		try {
			//conn = DatabaseManager.getConnection();// new DBConnection().getConnection();
			sql = "SELECT is_first_login FROM user_master where user_id=? and is_deleted=? and is_active=?";
			ps = conn.prepareStatement(sql);
			log.info("isFirstLoginFlag() query ::" + sql);
			ps.setString(1, userId);
			ps.setString(2, "0");
			ps.setString(3, "1");
			rs = ps.executeQuery();
			if (rs.next()) {
				String isFirst = rs.getString("is_first_login");
				log.info("isFirst Flag :: " + isFirst);
				return isFirst;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closeConnection(conn);

		}
		return null;
	}

}
