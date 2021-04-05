package com.winnovature.utils;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class SendMailService {
	static Logger log = Logger.getLogger(SendMailService.class.getName());

	public static void main(String[] args) {
		new SendMailService().sendMailTest("sonu@winnovature.com", "Test", "sonunchaudhari93@gmail.com", "HIi", "sonunchaudhari93@gmail.com");
	}

	public int sendMailTest(String to, String subject, String from, String mailBody, String cc) {
		int status = 0;
		try {
			from = "sonunchaudhari93@gmail.com";
			cc = "sonunchaudhari93@gmail.com";

			String host = "smtp.gmail.com";
			final String user1 = "sonunchaudhari93@gmail.com";
			final String pass = "Sonu@231193";
			//String port = "587";
			String port = "465";
			String startTls = "true";
			String sslEnable = "true";
			String smtpAuth = "true";
			
			
			// Get the session object
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);
			properties.setProperty("mail.smtp.user", user1);
			properties.setProperty("mail.smtp.password", pass);
			properties.put("mail.smtp.auth", smtpAuth);
			properties.put("mail.smtp.starttls.enable", startTls);
			properties.put("mail.smtp.ssl.enable", sslEnable);
			properties.put("mail.smtp.port", port);
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			// Session session = Session.getDefaultInstance(properties);
			Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user1, pass);// Specify the Username and the PassWord
				}
			});

			log.info("SendMailService.java ::: sendMail() :: Mail Params are : {" + to + "," + from + "," + subject
					+ "," + mailBody + "}");
			String[] recipientList = to.split(",");

			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));
			for (int i = 0; i < recipientList.length; i++) {
				log.info("SendMailService.java ::: sendMail() :: Mail Recipients TO are : "
						+ recipientList[i].toString());
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientList[i].toString()));
			}

			if (cc != null && !cc.equalsIgnoreCase("")) {
				String[] ccList = cc.split(",");

				for (int i = 0; i < ccList.length; i++) {
					log.info("SendMailService.java ::: sendMail() :: Mail Recipients CC are : " + ccList[i].toString());
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccList[i].toString()));
				}
			}

			message.setSubject(subject);
			message.setContent(mailBody, "text/html");

			// Send message
			Transport.send(message);
			status = 1;

		} catch (Exception mex) {
			status = -2;
			log.info("SendMailService.java ::: sendMail() :: catch block :: Error in mail sending and Status --> "
					+ mex);
		}
		return status;

	}
	
	
	public int sendMail(String to, String subject, String from, String mailBody, String cc) {
		int status = 0;
		try {
			from = PropertyReader.getPropertyValue("mailFrom");
			cc = PropertyReader.getPropertyValue("mailCC");

			String host = PropertyReader.getPropertyValue("smtpHost");
			final String user1 = PropertyReader.getPropertyValue("smtpUser");
			final String pass = PropertyReader.getPropertyValue("smtpPass");
			String port = PropertyReader.getPropertyValue("smtpPort");
			String startTls = PropertyReader.getPropertyValue("smtpStartTLS");
			String sslEnable = PropertyReader.getPropertyValue("smtpSSLEnable");
			String smtpAuth = PropertyReader.getPropertyValue("smtpAuth");

			// Get the session object
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);
			properties.setProperty("mail.smtp.user", user1);
			properties.setProperty("mail.smtp.password", pass);
			properties.put("mail.smtp.auth", smtpAuth);
			properties.put("mail.smtp.starttls.enable", startTls);
			properties.put("mail.smtp.ssl.enable", sslEnable);
			properties.put("mail.smtp.port", port);
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			// Session session = Session.getDefaultInstance(properties);
			Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user1, pass);// Specify the Username and the PassWord
				}
			});

			log.info("SendMailService.java ::: sendMail() :: Mail Params are : {" + to + "," + from + "," + subject
					+ "," + mailBody + "}");
			String[] recipientList = to.split(",");

			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));
			for (int i = 0; i < recipientList.length; i++) {
				log.info("SendMailService.java ::: sendMail() :: Mail Recipients TO are : "
						+ recipientList[i].toString());
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientList[i].toString()));
			}

			if (cc != null && !cc.equalsIgnoreCase("")) {
				String[] ccList = cc.split(",");

				for (int i = 0; i < ccList.length; i++) {
					log.info("SendMailService.java ::: sendMail() :: Mail Recipients CC are : " + ccList[i].toString());
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccList[i].toString()));
				}
			}

			message.setSubject(subject);
			message.setContent(mailBody, "text/html");

			// Send message
			Transport.send(message);
			status = 1;

		} catch (Exception mex) {
			status = -2;
			log.info("SendMailService.java ::: sendMail() :: catch block :: Error in mail sending and Status --> "
					+ mex);
		}
		return status;

	}

	public JSONObject getEmailIdPassword(String userId) {

		JSONObject jsonObj = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// String passmd5 = null;
		String passSha256 = null;
		log.info("sendMailService.java  ::  getEmailIdPassword()");
		try {

			con = DatabaseManager.getConnection();
			if (con != null) {
				String q = "select password,email_id from user_master where user_id = ?";
				ps = con.prepareStatement(q);
				ps.setString(1, userId);
				rs = ps.executeQuery();
				if (rs.next()) {
					jsonObj = new JSONObject();

					jsonObj.put("email_id", rs.getString("email_id"));
					jsonObj.put("password", rs.getString("password"));
					// sha256
					passSha256 = updateSha256Password(userId, rs.getString("password"));
					log.info("passSha256 Ststus  ::: " + passSha256);
				}

			}

		}

		catch (Exception e) {
			log.error("sendMailService.java  :: Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return jsonObj;
	}

	public JSONObject getEmailId(String userId) {

		JSONObject jsonObj = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// String passmd5 = null;
		// String passSha256 = null;
		log.info("sendMailService.java  ::  getEmailId()");
		try {

			con = DatabaseManager.getConnection();
			if (con != null) {
				String q = "select email_id from tbl_user_master where user_id = ?";
				ps = con.prepareStatement(q);
				ps.setString(1, userId);
				rs = ps.executeQuery();
				if (rs.next()) {
					jsonObj = new JSONObject();

					jsonObj.put("email_id", rs.getString("email_id"));
					// jsonObj.put("password", rs.getString("password"));

					/*
					 * passmd5 = updateMD5Password(userId, rs.getString("password"));
					 * log.info("passmd5 Ststus  ::: "+passmd5);
					 */

					// sha256
					// passSha256 = updateSha256Password(userId, rs.getString("password"));
					// log.info("passSha256 Ststus ::: "+passSha256);
				}

			}

		}

		catch (Exception e) {
			log.error("sendMailService.java  :: Getting Exception   :::    ", e);
		}

		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);
		}

		return jsonObj;
	}

	public String updateMD5Password(String userId, String password) {
		String status = "N";
		Connection con = null;
		PreparedStatement ps = null;
		int i = 0;

		try {
			con = DatabaseManager.getConnection();
			if (con != null) {

				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] thedigest = md.digest(password.getBytes());
				System.out.println(PropertyReader.byteToHex(thedigest));
				String pass = PropertyReader.byteToHex(thedigest);
				log.info("Encryption Password : " + pass);

				String query = "update user_master set password = ? where user_id = ? and password = ? ";
				ps = con.prepareStatement(query);

				ps.setString(1, pass);
				ps.setString(2, userId);
				ps.setString(3, password);

				i = ps.executeUpdate();
				if (i > 0) {
					status = "Y";
					log.info("sendMailService.java     ::::  Update Password MD5  in user_master");
				}

			} else {
				log.info("sendMailService.java     :::::: updatePassword() MD5 Connection is null...!!!");
			}

		} catch (Exception e) {

			log.error("sendMailService.java  :: Getting Exception   :::    ", e);
		}

		finally {
			// DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}

		return status;
	}

	public String updateSha256Password(String userId, String password) {
		String status = "N";
		Connection con = null;
		PreparedStatement ps = null;
		int i = 0;

		try {
			con = DatabaseManager.getConnection();
			if (con != null) {

				String pass = PropertyReader.sha256(password.getBytes());
				log.info("sha256 :: Encryption Password : " + pass);

				String query = "update user_master set password = ? where user_id = ? and password = ? ";
				ps = con.prepareStatement(query);

				ps.setString(1, pass);
				ps.setString(2, userId);
				ps.setString(3, password);

				i = ps.executeUpdate();
				if (i > 0) {
					status = "Y";
					log.info("sendMailService.java     ::::  Update Password MD5  in user_master");
				}

			} else {
				log.info("sendMailService.java     :::::: updatePassword() MD5 Connection is null...!!!");
			}

		} catch (Exception e) {

			log.error("sendMailService.java  :: Getting Exception   :::    ", e);
		}

		finally {
			// DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);
		}

		return status;
	}

}
