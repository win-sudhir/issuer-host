package com.winnovature.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class BalanceSheet 
{
	static Logger log = Logger.getLogger(BalanceSheet.class.getName());
	
	private void generateBalanceSheet() 
	{
		Connection con = null;
		CallableStatement cs = null;
		try 
		{
			//con = new DBConnection().getConnection();
			con = getConnection();
			con.setAutoCommit(false);
			cs = con.prepareCall("{CALL pr_balance_sheet(?,?)}");  
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			//log.info("Start date :: "+date+" 00:00:00");
			//log.info("End date :: "+date+" 23:55:59");
			cs.setString(1, date+" 00:00:00");//2019-05-10 23:23:23
			cs.setString(2, date+" 23:55:59");
			cs.execute();
			con.commit();
			//log.info("Executing BalanceSheet.generateBalanceSheet() :: ");
		}
		catch (Exception e) 
		{
			//log.error("Exception in BalanceSheet.generateBalanceSheet() :: " + e.getMessage());
			//log.error(e);
			e.printStackTrace();
		} 
		finally 
		{
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(con);
		}
	}
	public static void main(String[] args) 
	{
		log.info("<<<<<<<<<<<<<<<CRONJOB EXECUTING>>>>>>>>>>>>>>>>>>>>");
		new BalanceSheet().generateBalanceSheet();
		log.info("<<<<<<<<<<<<<<<CRONJOB WORK FINISHED>>>>>>>>>>>>>>>>>>>>");
	}
	
	public Connection getConnection()
	{
		Connection conn = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String dbUser="root";
			String dbIp="192.168.1.99:3306";
			String dbPass="root";
			String dbName="onemovefleet";		

			
			//  Used for MYSQL Server 

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// Class.forName("com.mysql.jdbc.Driver");
			String sURL = "jdbc:mysql://"+dbIp+"/"+dbName;
			String sUserName = dbUser;
			String sPwd = dbPass;
			conn = DriverManager.getConnection(sURL, sUserName, sPwd);

		}
		catch (Exception e) {
			//log.error("HDFC :: DBConnection.java :: getConnection() ::", e);
		}

		return conn;

	}
	
}
