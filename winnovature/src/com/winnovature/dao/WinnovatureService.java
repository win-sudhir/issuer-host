package com.winnovature.dao;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.PasswordManager;

public class WinnovatureService {
	static Logger log = Logger.getLogger(WinnovatureService.class.getName());

	public byte[] loadBinaryFile(String name) {
		DataInputStream dis = null;
		byte[] theBytes = null;
		try {

			dis = new DataInputStream(new FileInputStream(name));
			theBytes = new byte[dis.available()];
			dis.read(theBytes, 0, dis.available());
			dis.close();
			return theBytes;
		} catch (IOException ex) {
		}
		return theBytes;
	}

	public String getCustomerId() {
		String id;
		String number = Double.toString(Math.random());
		id = "C0" + number.substring(2, 6);
		log.info("Cust_ID : " + id);
		return id;

	}

	public static void insertUser(String userid, String role_id, String createdBy, String password, String emailId) {

		Connection con = null;
		PreparedStatement preparedStmt = null;

		String sqluser = "insert into user_master (user_id, password, role_id, email_id,last_login_date, last_login_ip, is_active, is_deleted, created_by, created_on, approved_by, approved_on)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String current_date = null;
		current_date = dateFormat.format(date);
		try {
			con = DatabaseManager.getConnection();
			preparedStmt = con.prepareStatement(sqluser);
			log.info("User ID : " + userid);
			preparedStmt.setString(1, userid);
			preparedStmt.setString(2, password);
			preparedStmt.setString(3, role_id);
			preparedStmt.setString(4, emailId);
			preparedStmt.setString(5, current_date);
			preparedStmt.setString(6, "0.0.0.0");
			preparedStmt.setString(7, "1");
			preparedStmt.setString(8, "0");
			preparedStmt.setString(9, createdBy);
			preparedStmt.setString(10, current_date);
			preparedStmt.setString(11, "");
			preparedStmt.setString(12, current_date);
			preparedStmt.executeUpdate();

			PasswordManager.insertUserHistory(userid, password);

			log.info("NETCService.java   :::   User inserted successfully in user master.........");

		} catch (Exception e) {
			try {
				con.rollback();
				log.info("Something wrong while insert user ..." + e);
				log.error("Getting Exception   :::    ", e);
			} catch (Exception ex) {
				log.info("User Insert opration rolled back..." + ex);
			}
		} finally {
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(con);

		}
	}

	
	public static void updateUserMaster(String userId, String emailId) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseManager.getConnection();/// new
															/// DBConnection().getConnection();
			if (con != null) {
				con.setAutoCommit(false);
				String query = "update user_master set email_id = ? where user_id = ? ";
				ps = con.prepareStatement(query);
				ps.setString(1, emailId);
				ps.setString(2, userId);
				ps.executeUpdate();
				con.commit();

				log.info("while updating record ::: EmailId Update in user master where user_id = " + userId
						+ "  , Email Id :: " + emailId);
			} else {
				log.info("NETCService.java updateUserMaster() ::: connection is null !!");
			}

		}

		catch (Exception e) {
			log.error("NetcService.java   ::: Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

	}
	public static void updateCustomerStatus(String customerId) {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseManager.getConnection();/// new
															/// DBConnection().getConnection();
			if (con != null) {
				con.setAutoCommit(false);
				String query = "update customer_master set is_approved = ? where cust_id = ? ";
				ps = con.prepareStatement(query);
				ps.setString(1, "0");
				ps.setString(2, customerId);
				ps.executeUpdate();
				con.commit();

				
			} else {
				log.info("NETCService.java updateCustomerStatus() ::: connection is null !!");
			}

		}

		catch (Exception e) {
			log.error("NetcService.java   ::: Getting Error   :::    ", e);
		}

		finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

	}

}
