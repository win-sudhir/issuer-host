package com.winnovature.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class PasswordManager 
{
	static Logger log = Logger.getLogger(PasswordManager.class.getName());
	
	ResponseHandler resp;

	
	public ResponseHandler getPasswordStats(String password) 
	{
		resp = new ResponseHandler();
		int numOfSpecial = 0;
		int numOfLetters = 0;
		int numOfUpperLetters = 0;
		int numOfLowerLetters = 0;
		int numOfDigits = 0;
		char c;
		int cint;
		try {
			for (int n = 0; n < password.length(); n++) {
				c = password.charAt(n);
				cint = (int) c;
				if (cint > 32 && cint < 48 || (cint > 57 && cint < 65) || (cint > 90 && cint < 97) || cint > 122) {
					numOfSpecial++;
					break;
				}
			}

			byte[] bytes = password.getBytes();
			for (byte tempByte : bytes) {
				char tempChar = (char) tempByte;
				if (Character.isDigit(tempChar)) {
					numOfDigits++;
				}

				if (Character.isLetter(tempChar)) {
					numOfLetters++;
				}

				if (Character.isUpperCase(tempChar)) {
					numOfUpperLetters++;
				}

				if (Character.isLowerCase(tempChar))
				{
					numOfLowerLetters++;
				}
			}
			System.out.println("***Statistics***");
			System.out.println("Number of special characters from (!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~) = " + numOfSpecial);
			System.out.println("Number of Letters = " + numOfLetters);
			System.out.println("Number of UpperLetters from (A to Z) = " + numOfUpperLetters);
			System.out.println("Number of LowerLetters from (a to z) = " + numOfLowerLetters);
			System.out.println("Number of Digits from (0 to 9) = " + numOfDigits);
			System.out.println("***Statistics***");

			if (password.length() < 8 && password.length() > 15) 
			{
				resp.setRespCode(01);
				resp.setRespMessage("New Password must have minimum 8 and maximum 15 characters please try again.");
			} 
			
			else 
			{

				if (numOfUpperLetters >= 1 && numOfLowerLetters >= 1 && numOfSpecial >= 1 && numOfDigits >= 1) 
				{
					resp.setRespCode(0);
					resp.setRespMessage("success");
				} 
				else 
				{
					resp.setRespCode(2);
					resp.setRespMessage("Password must have atleast 1 speacial character,1 numeric, 1 upper and 1 lower case character!!");
				}
			}

		} 
		catch (Exception e) 
		{
			System.out.println("Exception in printPasswordStats() :: " + e.getMessage());
			e.printStackTrace();
		}
		return resp;
	}
	
	

	public ResponseHandler checkWithPasswordHistory(String[] password, String newPassword) 
	{
		resp = new ResponseHandler();

		if (null == password || password.length < 1) {
			resp.setRespCode(1);
			resp.setRespMessage("Password should not be null or should have at least one password.");
			return resp;
		}
		for (String element : password)
		{
			if (element.equals(newPassword)) 
			{
				resp.setRespCode(2);
				resp.setRespMessage("Password matched in history.");
				break;
			} 
		}
		if (resp.getRespCode()==2) {
			resp.setRespCode(3);
			resp.setRespMessage("Password matched in history.");
		} 
		else {
			resp.setRespCode(0);
			resp.setRespMessage("success");
		} 
		
		
		/*for (int i = 0; i < password.length; i++) {
			if (password[i].equals(newPassword)) {
				resp.setRespCode(2);
				resp.setRespMessage("Password matched in history.");
				break;
			} else {
				resp.setRespCode(0);
				resp.setRespMessage("success.");
				return resp;
			}
		}*/
		return resp;
	}

	// password reset/change days (last resetDate and compare with current date
	// and retun number of days in resp handler
	// no of days to ba calculated till the current date
	public ResponseHandler countNumberOfDays(String lastPasswrodResetDate, String datePattern) 
	{
		resp = new ResponseHandler();

		if (null == lastPasswrodResetDate || lastPasswrodResetDate.length() < 1) {
			resp.setRespCode(1);
			resp.setRespMessage("lastPasswrodResetDate should not be null or empty.");
			return resp;
		}

		try {
			SimpleDateFormat myFormat = new SimpleDateFormat(datePattern);
			Date date1 = myFormat.parse(lastPasswrodResetDate);
			Date date2 = new Date();
			long diff = date2.getTime() - date1.getTime();
			int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
			resp.setRespCode(days);
			resp.setRespMessage("success");
		} catch (ParseException e) {
			resp.setRespCode(2);
			resp.setRespMessage(e.getMessage());
			e.printStackTrace();
		}
		return resp;
	}

	public ResponseHandler differenceBetween2Dates(String sinceDate, String fromDate) 
	{
		resp = new ResponseHandler();

		if (null == sinceDate || sinceDate.length() < 1 || null == fromDate || fromDate.length() < 1) {
			resp.setRespCode(01);
			resp.setRespMessage("First date and second date can not be null or empty.");
			return resp;
		}

		try 
		{
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = myFormat.parse(sinceDate);
			Date date2 = myFormat.parse(fromDate);
			long diff = date2.getTime() - date1.getTime();
			int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
			resp.setRespCode(days);
			resp.setRespMessage("success");
		} 
		
		catch (ParseException e) 
		{
			resp.setRespCode(02);
			resp.setRespMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return resp;
	}
	
	
	
	public boolean validateNewPasswordHistory(String newPassword, String userId) 
	{
		boolean status=false;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		log.info("PasswordManager.java  ::::  Check new password in history");
		
		String sql = "SELECT user_id,password FROM tbl_user_history where password = ? and user_id = ? limit 5";
		log.info("validateNewPasswordHistory Query ::"+sql);
		try
		{
			conn = DatabaseManager.getConnection();
			if(conn!=null)
			{
			ps =  conn.prepareStatement(sql);
			ps.setString(1, newPassword);
			ps.setString(2, userId);
		    rs = ps.executeQuery();
		    if(rs.next())
		    {
		    	status=true;
			    log.info("PasswordManager.java :: Found new password in tbl_user_history.");
		    }
		    
		    
			}
			
			else
			{
				status=false;
				log.info("PasswordManager.java :: validateNewPasswordHistory() false!!!");
			}
		} 
		catch (Exception e)
		{
	       log.info("PasswordManager.java ::: Error while checking new password in tbl_user_history records"+e.getMessage());
	       log.error("Getting Exception in validateOldPassword() :: ",e);
	    }
		finally
		{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
		return status;
	}
	
	
	
	public static void insertUserHistory(String userid, String password)
	{
		Connection con = null;
		CallableStatement cs = null;
		String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		try {
			con = DatabaseManager.getConnection();
			con.setAutoCommit(false);
			cs = con.prepareCall("{CALL pr_manage_user_history(?,?)}");
			cs.setString(1, userid);
			cs.setString(2, password);
			cs.execute();
			con.commit();
			
			Map hm = new HashMap();
			hm.put("USERID", userid);
			hm.put("NEWPASSWORD", password);
			hm.put("STATUS", "SUCCESS");
			
			/* AuditTrailDao auditTrailDao = new AuditTrailDao();
			   auditTrailDao.addAuditData(userid, password, "USERHISTORYCHANGEPASSWORD","CHANGESUCCESS", hm, current_date);*/
			
			log.info("PasswordManager :: User history is added successfully");
		} 
		catch (Exception e) 
		{
			log.error("PasswordManager :: Exception while Insert user history : " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} 
		finally 
		{
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(con);
		
		}
	}
	
	
	
	public static String getPasswordSaltString() 
	{
		final String specialChar = "!@#$%&*?";
		final int N = specialChar.length();
		Random rd = new Random();
		int iLength = 1;
		StringBuilder sb = new StringBuilder(iLength);
		for (int i = 0; i < 1; i++) {
		    sb.append(specialChar.charAt(rd.nextInt(N)));
		}
		//System.out.println(sb);
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 7) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltPassword = salt.toString();
		return saltPassword+sb;
	}
	
	public static void main(String args[])
	{

		String sinceDate = "2019-03-13 13:28:11";
		String fromDate = "2019-03-13 13:28:11";
		if (null == sinceDate || sinceDate.length() < 1 || null == fromDate || fromDate.length() < 1) 
		{
			System.out.println("Days: Null ");
		}

		try 
		{
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = myFormat.parse(sinceDate);
			Date date2 = myFormat.parse(fromDate);
			long diff = date2.getTime() - date1.getTime();
			int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
			
		} 
		
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		
		
	
	}

	public static String getLastLoginTime(String userId, String password)
	{
		String LastLoinDateTime = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		log.info("PasswordManager.java  ::::  getLastLoginTime() :: ");
		
		String sql = "SELECT last_login_date FROM user_master where user_id = ? and password = ? ";
		log.info("getLastLoginTime Query ::"+sql);
		try
		{
			conn = DatabaseManager.getConnection();
			if(conn!=null)
			{
			ps =  conn.prepareStatement(sql);
			
			ps.setString(1, userId);
			ps.setString(2,password);
		    rs = ps.executeQuery();
		    if(rs.next())
		    {
		    	LastLoinDateTime = rs.getString("last_login_date");
			    log.info("PasswordManager.java :: LastLoinDateTime : "+LastLoinDateTime);
		    }
			}
			else
			{
				LastLoinDateTime=null;
				log.info("PasswordManager.java :: LastLoinDateTime() Esle Part false!!!");
			}
		} 
		catch (Exception e)
		{
			LastLoinDateTime = null;
	       log.info("PasswordManager.java ::: Error while checking new password in LastLoinDateTime records"+e.getMessage());
	       log.error("Getting Exception in LastLoinDateTime() :: ",e);
	    }
		finally
		{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
		
		}
		return LastLoinDateTime;
	}
	
	
	
	
	
	public static String getLastChangePasswordDate(String userId, String password)
	{
		String LastPasswordChangeDateTime = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		log.info("PasswordManager.java  ::::  getLastLoginTime() :: ");
		
		String sql = "select created_on from tbl_user_history where user_id = ? order by id desc limit 1";
		log.info("LastPasswordChangeDateTime Query ::"+sql);
		try
		{
			conn = DatabaseManager.getConnection();
			if(conn!=null)
			{
			ps =  conn.prepareStatement(sql);
			
			ps.setString(1, userId);
			// ps.setString(2,password);
		    rs = ps.executeQuery();
		    if(rs.next())
		    {
		    	LastPasswordChangeDateTime = rs.getString("created_on");
			    log.info("PasswordManager.java :: LastPasswordChangeDateTime : "+LastPasswordChangeDateTime);
		    }

			}
			/*else
			{
				LastPasswordChangeDateTime = null;
				log.info("PasswordManager.java :: LastLoinDateTime() Esle Part false!!!");
			}*/
		} 
		catch (Exception e)
		{
			LastPasswordChangeDateTime = null;
	       log.info("PasswordManager.java ::: Error while checking new password in LastPasswordChangeDateTime records"+e.getMessage());
	       log.error("Getting Exception in LastLoinDateTime() :: ",e);
	    }
		finally
		{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
	
		}
		return LastPasswordChangeDateTime;
	}
	
}
