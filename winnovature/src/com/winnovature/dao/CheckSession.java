/**
 * 
 */
package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.PropertyReader;

public class CheckSession {
	static Logger log = Logger.getLogger(CheckSession.class.getName());

	public static boolean isValidSession(String userId, String sessionId, Connection conn) {
		// Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			log.info("Session Handling,  userId : "+userId +" & sessionId : "+sessionId);
			if (userId == null || userId.isEmpty() || sessionId == null || sessionId.isEmpty()) {
				return false;
			}

			// conn = DatabaseConnectionManager.getConnection();
			// user_id, auth_token, rodt
			String sql = "SELECT * FROM tbl_session_master where user_id = ? and auth_token = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, sessionId);
			rs = ps.executeQuery();

			if (rs.next()) {
				if (userId.equalsIgnoreCase("STFleetOwner")) {
					return true;
				} else {
					if (loginTimeCheck(userId, sessionId, conn)) {
						return true;
					} else {
						return false;
					}
				}
			} else {
				System.out.println("CheckSession.java  ::: Token in request  :: " + sessionId + " user Id :: " + userId
						+ " Not exists in session master");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseConnectionManager.closeConnection(conn);

		}
		return false;
	}

	public static boolean loginTimeCheck(String userId, String sessionId, Connection conn) {
		// final long TEN_MINUTES = 5 * 60 * 1000;

		boolean flag = false;

		try {
			String sessionTimeOut = PropertyReader.getPropertyValue("sessionTimeOut");// getUrlString("sessionTimeOut");
			final long TEN_MINUTES = Long.parseLong(sessionTimeOut) * 60 * 1000;

			String loginTime = null;
			Long login = 0l;

			loginTime = getUserLoginTime(userId, conn); // "2018-09-21 19:50:33";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(loginTime);

			login = System.currentTimeMillis() - date.getTime(); // current
																	// different

			System.out.println("loginTime : " + loginTime + " , date : " + date + " , Date.getTime :: " + date.getTime()
					+ " , login : " + login + "  , System.currentTimeMillis() : " + System.currentTimeMillis()
					+ "  , TimeUnit : " + TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()));
			System.out.println("Get Request Time in milliseconds : " + login);
			System.out.println("Session Time out In milliseconds : " + TEN_MINUTES);

			if (login > TEN_MINUTES) {
				System.out.println("Get Request is older than " + sessionTimeOut + " minutes then session out.");
				flag = false;
			} else {
				System.out.println("Get Request inside the " + sessionTimeOut + " minutes so session to be continue..");
				// update login time
				updateLoginTime(userId, sessionId, conn);

				flag = true;
			}
		}

		catch (Exception e) {
			System.out.println("Exception in CheckSession.loginTimeCheck() :: " + e.getMessage());
			e.printStackTrace();
		}

		return flag;
	}

	public static String getUserLoginTime(String userId, Connection conn) throws Exception {
		// Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String time = null;
		try {
			// conn = DatabaseConnectionManager.getConnection();
			String sql = "SELECT * FROM tbl_session_master where user_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				time = rs.getString("rodt");
				return time;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseConnectionManager.closeConnection(conn);

		}
		return time;
	}

	public static void updateLoginTime(String userId, String sessionId, Connection conn) {
		// Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			String sql = "UPDATE tbl_session_master set rodt=? where user_id=? and auth_token=?";
			// conn = DatabaseConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, current_date);
			ps.setString(2, userId);
			ps.setString(3, sessionId);
			ps.executeUpdate();
			System.out.println("SESSION TIME HAS BEEN UPDATED SUCCESSFULLY...");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseConnectionManager.closeConnection(conn);

		}
	}
}
