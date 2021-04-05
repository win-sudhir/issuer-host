package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.PasswordManager;
import com.winnovature.utils.PropertyReader;

public class PasswordDAO {
	static Logger log = Logger.getLogger(PasswordDAO.class.getName());

	public boolean resetPassword(String emailId, String password, String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		int i = 0;

		try {
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();

			if (con != null) {

				PasswordManager.insertUserHistory(userId, password);

				String pass = PropertyReader.sha256(password.getBytes());

				String query = "update user_master set password= ?,is_first_login=? where email_id = ? and user_id = ?";
				ps = con.prepareStatement(query);
				// ps.setString(1, password); // normal text
				ps.setString(1, pass);
				ps.setString(2, "1");
				ps.setString(3, emailId);
				ps.setString(4, userId);
				i = ps.executeUpdate();
				log.info("No of Record Updated... " + i);
				if (i > 0) {
					return true;
				}

			}

		} catch (Exception e) {

			log.error("Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return false;
	}

	public boolean validateOldPassword(String oldPassword, String userId) {
		boolean status = false;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		log.info("PasswordResetChangeDao.java  ::::  Check Old password");

		String sql = "SELECT user_id,password FROM user_master where password = ? and user_id = ?";
		log.info("validateOldPassword Query ::" + sql);
		try {
			conn = DatabaseManager.getConnection();// new
																// DBConnection().getConnection();
			if (conn != null) {

				String pass = PropertyReader.sha256(oldPassword.getBytes());

				ps = conn.prepareStatement(sql);
				ps.setString(1, pass); // validate in sha256
				ps.setString(2, userId);
				rs = ps.executeQuery();
				if (rs.next()) {
					status = true;
					log.info("PasswordResetChangeDao.java  ::: Found old password in tbl_user_master.....");
				}
			} else {
				log.info("PasswordResetChangeDao.java    :::::: validateOldPassword()  Connection is null...!!!");
			}
		} catch (Exception e) {
			log.info("PasswordResetChangeDao.java ::: Error while checking old password tbl_user_master records"
					+ e.getMessage());
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return status;
	}

	public boolean updatePassword(String userId, String oldPassword, String newPassword) {
		boolean status = false;
		Connection con = null;
		PreparedStatement ps = null;
		int i = 0;

		try {
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();
			if (con != null) {

				String pass = PropertyReader.sha256(newPassword.getBytes());

				String oldpass = PropertyReader.sha256(oldPassword.getBytes());

				String query = "update user_master set password = ? where user_id = ? and password = ? ";
				ps = con.prepareStatement(query);
				ps.setString(1, pass);
				ps.setString(2, userId);
				ps.setString(3, oldpass);
				i = ps.executeUpdate();
				if (i > 0) {

					Map hm = new HashMap();
					hm.put("USERID", userId);
					hm.put("NEWPASSWORD", newPassword);
					hm.put("STATUS", "SUCCESS");

					/*
					 * AuditTrailDao auditTrailDao = new AuditTrailDao();
					 * auditTrailDao.addAuditData(userId, userId,
					 * "USERCHANGEPASSWORD","CHANGESUCCESS", hm, current_date);
					 */
					log.info("PasswordResetChangeDao.java    ::::  Update Password in user_master");

					PasswordManager.insertUserHistory(userId, newPassword);

					status = true;
					log.info("PasswordResetChangeDao.java    ::::  Update Password in user_master");
					log.info("PasswordResetChangeDao.java    ::::  updatePassword() :: in userId :: " + userId
							+ "  ,  password  :: " + newPassword);

				}

			} else {
				log.info("PasswordResetChangeDao.java    :::::: updatePassword()  Connection is null...!!!");
			}

		}

		catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return status;
	}

	public static boolean validateEmailId(String email_id, String userId) {
		boolean status = false;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		String sql = "SELECT email_id FROM user_master where email_id = ? and user_id = ?";
		log.info("validateEmailId Query ::" + sql);
		try {
			conn = DatabaseManager.getConnection();// new
																// DBConnection().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, email_id);
			ps.setString(2, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				status = true;
				log.info("userID and email ID Found in user_master.....");
			} else {
				log.info("userID and email ID not Found in user_master.....");
			}
		} catch (Exception e) {
			log.info("Error while checking user_master records" + e.getMessage());
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return status;
	}

	public boolean validateNewPasswordHistory(String newPassword, String userId) {
		boolean status = false;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		log.info("PasswordResetChangeDao.java  ::::  Check new password in history");

		String sql = "SELECT user_id,password FROM tbl_user_history where password = ? and user_id = ? limit 5";
		log.info("validateNewPasswordHistory Query ::" + sql);
		try {
			conn = DatabaseManager.getConnection();// new
																// DBConnection().getConnection();
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, newPassword);
				ps.setString(2, userId);
				rs = ps.executeQuery();
				if (rs.next()) {
					status = true;
					log.info("PasswordResetChangeDao.java :: Found new password in tbl_user_history.");
				}

			}

			else {
				status = false;
				log.info("PasswordResetChangeDao.java :: validateNewPasswordHistory() connection is null !!!");
			}
		} catch (Exception e) {
			log.info("PasswordResetChangeDao.java ::: Error while checking new password in tbl_user_history records"
					+ e.getMessage());
			log.error("Getting Exception in validateOldPassword() :: ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return status;
	}

}
