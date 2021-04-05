package com.winnovature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

//import com.netc.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class RoleMenuDAO {
	static Logger log = Logger.getLogger(RoleMenuDAO.class.getName());

	public String createRole(String roleName, String groupId) {
		JSONObject roleIdStatus = null;
		CallableStatement cs = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "CALL pro_InsertRole(?,?,?,?)";
				cs = conn.prepareCall(query);
				cs.setString(1, roleName);
				cs.setString(2, groupId);
				cs.registerOutParameter(3, Types.BIGINT);
				cs.registerOutParameter(4, Types.VARCHAR);

				cs.execute();
				conn.commit();

				int roleId = cs.getInt(3);
				String isDeleted = cs.getString(4);

				roleIdStatus = new JSONObject();

				roleIdStatus.put("RoleId", roleId);
				roleIdStatus.put("IsDeleted", isDeleted);

				log.info("RoleMenuDAO.java ::: createRole() :: Status : " + roleId + " , Is Deleted : " + isDeleted);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			// DatabaseConnectionManager.closeResultSet(rs);
			// DatabaseConnectionManager.closePreparedStatement(ps);
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return roleIdStatus.toString();
	}

	public int updateOrDeleteRole(String roleName, String groupId, String roleId, String opertion) {
		int status = -1;
		CallableStatement cs = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "CALL pro_updateOrDeleteRole(?,?,?,?)";
				cs = conn.prepareCall(query);
				cs.setString(1, roleName);
				cs.setString(2, groupId);
				cs.setString(3, roleId);
				cs.setString(4, opertion);

				status = cs.executeUpdate();
				conn.commit();

				log.info("RoleMenuDAO.java ::: createRole() :: Status : " + status);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
			status = -1;
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return status;
	}

	public JSONArray createRoleMenuMapping(String roleId, List<String> menuList) {
		CallableStatement cs = null;
		Connection conn = null;
		String status = null;
		JSONObject mapStatus = null;
		JSONArray mapStatusArr = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				mapStatusArr = new JSONArray();
				for (int i = 0; i < menuList.size(); i++) {
					conn.setAutoCommit(false);
					String query = "CALL pro_roleMenuMapping(?,?,?,?)";
					cs = conn.prepareCall(query);
					cs.setString(1, roleId);
					cs.setString(2, menuList.get(i).split("~")[0]);
					cs.setString(3, menuList.get(i).split("~")[1]);
					cs.registerOutParameter(4, Types.VARCHAR);

					cs.execute();
					// conn.commit();

					status = cs.getString(4);

					mapStatus = new JSONObject();

					log.info("RoleMenuDAO.java ::: createRoleMenuMapping() :: Status : " + status);
					if (status.equalsIgnoreCase("NA")) {
						mapStatus.put("Message", "Mapping Already Exists.");
						mapStatus.put("Status", true);
						mapStatus.put("menuId", menuList.get(i).split("~")[0]);
						mapStatus.put("submenuId", menuList.get(i).split("~")[1]);
					} else if (status.equalsIgnoreCase("N")) {
						mapStatus.put("Message", "Mapping added Successfully.");
						mapStatus.put("Status", true);
						mapStatus.put("menuId", menuList.get(i).split("~")[0]);
						mapStatus.put("submenuId", menuList.get(i).split("~")[1]);
					} else if (status.equalsIgnoreCase("Y")) {
						mapStatus.put("Message", "Existing Role-Menu-Submenu Map Enabled.");
						mapStatus.put("Status", true);
						mapStatus.put("menuId", menuList.get(i).split("~")[0]);
						mapStatus.put("submenuId", menuList.get(i).split("~")[1]);
					}
					mapStatusArr.put(mapStatus);
				}
				conn.commit();
			}
		} catch (Exception e) {
			mapStatusArr = null;
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				log.error("Getting Exception   :::    ", e1);
			}
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return mapStatusArr;
	}

	public JSONArray getListData(String listType) {
		JSONObject getData = null;
		JSONArray getReport = null;
		CallableStatement cs = null;
		Connection conn = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				String query = "CALL pro_getDataFromTable(?)";
				cs = conn.prepareCall(query);
				cs.setString(1, listType);
				rs = cs.executeQuery();

				getReport = new JSONArray();
				while (rs != null && rs.next()) {
					getData = new JSONObject();

					rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						// log.info(rsmd.getColumnLabel(i)+" ::::
						// "+rsmd.getColumnTypeName(i));

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
				log.info("RoleMenuDAO.java ::: getListData() :: List Data : " + getReport);

			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);
			// rsmd = null;

		}

		return getReport;
	}

	public int deleteRoleMapping(String roleId, String menuId, String submenuId) {
		int status = -1;
		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "Update role_menu_mapping set is_deleted = ? where role_id = ? and menu_id = ? and submenu_id = ?";
				ps = conn.prepareStatement(query);

				ps.setString(1, "1");
				ps.setString(2, roleId);
				ps.setString(3, menuId);
				ps.setString(4, submenuId);

				status = ps.executeUpdate();
				conn.commit();

				log.info("RoleMenuDAO.java ::: createRole() :: Status : " + status);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
			status = -1;
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}

		return status;
	}

	public String createSubMenu(String menuId, String subMenuShortName, String subMenuURL, String isActive) {
		JSONObject roleIdStatus = null;
		CallableStatement cs = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "CALL pro_insertSubMenu(?,?,?,?,?,?)";
				cs = conn.prepareCall(query);
				cs.setString(1, menuId);
				cs.setString(2, subMenuShortName);
				cs.setString(3, subMenuURL);
				cs.setString(4, isActive);
				cs.registerOutParameter(5, Types.INTEGER);
				cs.registerOutParameter(6, Types.VARCHAR);

				cs.execute();
				conn.commit();

				int subMenuId = cs.getInt(5);
				String isActivated = cs.getString(6);

				roleIdStatus = new JSONObject();

				roleIdStatus.put("SubMenuId", subMenuId);
				roleIdStatus.put("isActivated", isActivated);

				log.info("RoleMenuDAO.java ::: createSubMenu() :: SubMenu Id : " + subMenuId + " , Is Activated : "
						+ isActivated);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return roleIdStatus.toString();
	}

	public int updateOrDeleteSubMenu(String menuId, String subMenuId, String subMenuShortName, String subMenuURL,
			String isActive, String opertion) {
		int status = -1;
		CallableStatement cs = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "CALL pro_updateOrDeleteSubMenu(?,?,?,?,?,?)";
				cs = conn.prepareCall(query);
				cs.setString(1, menuId);
				cs.setString(2, subMenuId);
				cs.setString(3, subMenuShortName);
				cs.setString(4, subMenuURL);
				cs.setString(5, isActive);
				cs.setString(6, opertion);

				status = cs.executeUpdate();
				conn.commit();

				log.info("RoleMenuDAO.java ::: updateOrDeleteSubMenu() :: Status : " + status);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
			status = -1;
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return status;
	}

	public String createTagClass(String tagClassId, String tagColorId, String isActive) {
		String status = null;
		CallableStatement cs = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "CALL pro_insertTagClass(?,?,?,?)";
				cs = conn.prepareCall(query);
				cs.setString(1, tagClassId);
				cs.setString(2, tagColorId);
				cs.setString(3, isActive);
				cs.registerOutParameter(4, Types.VARCHAR);

				cs.execute();
				conn.commit();

				status = cs.getString(4);

				log.info("RoleMenuDAO.java ::: createTagClass() :: Status : " + status);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return status;

	}

	public int updateOrDeleteTagClass(String userId, String tagClassId, String tagColorid, String isActive,
			String opertion) {
		int status = -1;
		CallableStatement cs = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "CALL pro_updateOrDeleteTagClass(?,?,?,?,?)";
				cs = conn.prepareCall(query);
				cs.setString(1, userId);
				cs.setString(2, tagClassId);
				cs.setString(3, tagColorid);
				cs.setString(4, isActive);
				cs.setString(5, opertion);

				status = cs.executeUpdate();
				conn.commit();

				log.info("RoleMenuDAO.java ::: updateOrDeleteTagClass() :: Status : " + status);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
			status = -1;
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return status;
	}

	public String createVehicleClass(String classCode, String desc, String maxThreshold, String minBalance,
			String remarks) {
		String status = null;
		CallableStatement cs = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "CALL pro_insertVehicleClass(?,?,?,?,?,?)";
				cs = conn.prepareCall(query);
				cs.setString(1, classCode);
				cs.setString(2, desc);
				cs.setString(3, maxThreshold);
				cs.setString(4, minBalance);
				cs.setString(5, remarks);
				cs.registerOutParameter(6, Types.VARCHAR);

				cs.execute();
				conn.commit();

				status = cs.getString(6);

				log.info("RoleMenuDAO.java ::: createVehicleClass() :: Status : " + status);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return status;
	}

	public JSONArray getRoleMenuMap(HashMap<Integer, String> roleMap) {
		CallableStatement cs = null;
		JSONObject getMenuData = null;
		JSONObject getSubMenuData = null;
		JSONArray menuArr = null;
		JSONArray subMenuArr = null;
		JSONObject respData = null;
		JSONArray resp = null;
		Connection conn = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				resp = new JSONArray();

				for (HashMap.Entry<Integer, String> entry : roleMap.entrySet()) {
					String roleId = entry.getKey().toString();
					String roleName = entry.getValue();
					log.info("Fetch Menus for Role Id :: " + roleId);

					String query = "CALL pro_getDataFromIds(?,?,?)";
					cs = conn.prepareCall(query);
					cs.setString(1, "rolemenu");
					cs.setString(2, roleId);
					cs.setString(3, null);

					ResultSet rs = cs.executeQuery();
					ResultSetMetaData rsmd = null;

					menuArr = new JSONArray();

					while (rs != null && rs.next()) {
						getMenuData = new JSONObject();

						rsmd = rs.getMetaData();
						int colCount = rsmd.getColumnCount();

						for (int j = 1; j <= colCount; j++) {
							getMenuData.put(rsmd.getColumnLabel(j), rs.getString(j));
							// getMenuData.remove("submenuId");
						}
						// log.info("Menu Object : "+getMenuData);

						String query1 = "CALL pro_getDataFromIds(?,?,?)";
						cs = conn.prepareCall(query1);
						cs.setString(1, "submenu");
						cs.setString(2, roleId);
						cs.setString(3, rs.getString("menuId"));

						ResultSet rs1 = cs.executeQuery();
						ResultSetMetaData rsmd1 = null;

						subMenuArr = new JSONArray();

						while (rs1 != null && rs1.next()) {
							getSubMenuData = new JSONObject();

							rsmd1 = rs1.getMetaData();
							int colCount1 = rsmd1.getColumnCount();
							for (int p = 1; p <= colCount1; p++) {
								getSubMenuData.put(rsmd1.getColumnLabel(p), rs1.getString(p));
							}
							subMenuArr.put(getSubMenuData);

							// log.info("SubMenu Object : "+getSubMenuData);
						}

						getMenuData.put("submenus", subMenuArr);
						menuArr.put(getMenuData);

					}

					// log.info("Menu Array : "+menuArr);

					respData = new JSONObject();

					respData.put("Menus", menuArr);
					respData.put("roleId", roleId);
					respData.put("role", roleName);
					resp.put(respData);
				}

				// log.info("RoleMenuDAO.java ::: getRoleMenuMap() :: Final
				// Array : " + resp);
			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return resp;
	}

	public JSONArray approveOrRejectPo(String poId) {
		JSONObject respObj = null;
		JSONArray respArr = null;
		CallableStatement cs = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DatabaseManager.getConnection();// new
													// DBConnection().getConnection();
			if (conn != null) {
				conn.setAutoCommit(false);
				String query = "CALL pro_ApproveOrRejectPo(?,?)";
				cs = conn.prepareCall(query);
				cs.setString(1, "approve");
				cs.setString(2, poId);
				// cs.registerOutParameter(3,Types.VARCHAR);

				boolean status = cs.execute();
				conn.commit();

				// String tid = cs.getString(3);
				if (status) {
					rs = cs.getResultSet();
					if (rs != null) {
						respArr = new JSONArray();
						while (rs.next()) {
							respObj = new JSONObject();
							log.info("ResultSet :: " + rs.getString("TId"));

							respObj.put("Message", "Tag Approval Successful.");
							respObj.put("Status", true);
							respObj.put("TId", rs.getString("TId"));
							respArr.put(respObj);
						}
					} else {
						respObj = new JSONObject();
						respObj.put("Message", "PO Status Updated.No Data found for Tag Approval.");
						respObj.put("Status", true);
					}
				} else {
					respObj = new JSONObject();
					respObj.put("Message", "PO Status Updation Failed.");
					respObj.put("Status", false);
				}
				// log.info("RoleMenuDAO.java ::: approveOrRejectPo() :: TId : "
				// + tid);
			}
		} catch (Exception e) {
			respObj = new JSONObject();
			respObj.put("Message", e.getMessage());
			respObj.put("Status", false);
		} finally {

			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}

		return respArr;
	}

	public String dispatchPo(Connection conn, String poId, String courierCompName, String dispAddress, String dispDate,
			String docketNo, String tagDelAddress, String remarks) {
		String resp = null;
		CallableStatement cs = null;
		// Connection conn = null;

		try {
			// conn = DatabaseManager.getConnection();// new
			// DBConnection().getConnection();
			String query = "CALL pro_dispatchPo(?,?,?,?,?,?,?,?)";
			cs = conn.prepareCall(query);
			cs.setString(1, poId);
			cs.setString(2, courierCompName);
			cs.setString(3, dispAddress);
			cs.setString(4, dispDate);
			cs.setString(5, docketNo);
			cs.setString(6, tagDelAddress);
			cs.setString(7, remarks);
			cs.registerOutParameter(8, Types.VARCHAR);

			cs.execute();
			// conn.commit();
			resp = cs.getString(8);
			log.info("RoleMenuDAO.java ::: dispatchPo() :: Resp : " + resp);
			// }
		} catch (Exception e) {
			// try {
			// conn.rollback();
			// } catch (SQLException e1) {
			// TODO Auto-generated catch block
			log.error("Getting Exception   :::    ", e);
			// }
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			// DatabaseManager.closeConnection(conn);

		}

		return resp;
	}
}
