package com.winnovature.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class SMSService 
{
	static Logger log = Logger.getLogger(SMSService.class.getName());
	
	public String addDataForSMS(String refId, String smsContent, String contactNo, String insertOn) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		String status = "FAIL";
		try {
			conn=DatabaseManager.getConnection();
			
			if(conn != null)
			{
				String query="INSERT INTO netc_sms_info(ref_id,mobile_no,sms_body,bank_id,inserted_on,sender_id) VALUES(?,?,?,?,?,?)";
				ps = conn.prepareStatement(query);
				
				ps.setString(1, refId);
				ps.setString(2, contactNo);
				ps.setString(3, smsContent);
				ps.setString(4, "SIMPLI");
				ps.setString(5, insertOn);
				ps.setString(6, "SIMPLI");
				
				ps.execute();
				status = "SUCCESS";
				log.info("SMSService.java :: addDataForSMS() :: insert into netc_sms_info :: status "+status);
			}
			else
			{
				log.info("SMSService.java :: addDataForSMS() :: connection is null ");
			}
		} 
		
		catch (Exception e) 
		{
			log.info("SMSService.java :: addDataForSMS() :: connection is null "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			//DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
		}
		return status;
	}
	
	public String getContactNumber(String userId) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		try 
		{
			conn = DatabaseManager.getConnection();
			sql = "SELECT contact_number,user_id from tbl_wallet_master where user_id=? and status_id<2";
				
			log.info("getContactNumber QUERY ::"+sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				log.info("CONTACT NUMBER :: "+rs.getString("contact_number"));
				return rs.getString("contact_number");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
		
		}
		return null;
	}
	
	public JSONObject getContactNumberWalletId(String userId) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		try 
		{
			conn = DatabaseManager.getConnection();
			sql = "SELECT contact_number,user_id,wallet_id from tbl_wallet_master where wallet_id=? and status_id<2";
				
			log.info("getContactNumberWalletId QUERY ::"+sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				log.info("CONTACT NUMBER :: "+rs.getString("contact_number"));
				log.info("WALLET ID :: "+rs.getString("wallet_id"));
				JSONObject resp = new JSONObject();
				resp.put("contactNo", rs.getString("contact_number"));
				resp.put("walletId", rs.getString("wallet_id"));
				resp.put("custId", rs.getString("user_id"));
				return resp;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
		
		}
		return null;
	}
	
	public JSONObject getContactNumberVAccountId(String userId) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		try 
		{
			conn = DatabaseManager.getConnection();
			sql = "SELECT contact_number,fleet_owner_id,virtual_acc_no from tbl_fleet_owner_account_master where virtual_acc_no=?";
				
			log.info("getContactNumberVAccountId QUERY ::"+sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				log.info("CONTACT NUMBER :: "+rs.getString("contact_number"));
				log.info("VIRTUAL ACCOUNT NUMBER :: "+rs.getString("virtual_acc_no"));
				JSONObject resp = new JSONObject();
				resp.put("contactNo", rs.getString("contact_number"));
				resp.put("walletId", rs.getString("virtual_acc_no"));
				resp.put("custId", rs.getString("fleet_owner_id"));
				return resp;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
		return null;
	}
	
	public String addDataForEmail(String ref_id, String emailContent, String email_id, String insertOn,String subject) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		String status = "FAIL";
		
		try {
			conn=DatabaseManager.getConnection();
			
			if(conn != null)
			{
				
			//	ref_id, mobile_no, sms_body, bank_id, inserted_on, sender_id, sent_status
				String query="INSERT INTO tbl_email_info(ref_id,email_id,subject,email_body,bank_id,inserted_on,sender_id) VALUES(?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(query);
				
				ps.setString(1, ref_id);
				ps.setString(2, email_id);
				ps.setString(3, subject);
				ps.setString(4, emailContent);
				ps.setString(5, "HDFC");
				ps.setString(6, insertOn);
				ps.setString(7, "HDFC");
							
				ps.execute();
				status = "SUCCESS";
				log.info("SMSService.java :: addDataForEmail() :: insert into netc_sms_info :: status "+status);
			}
			else
			{
				log.info("SMSService.java :: addDataForEmail() :: connection is null ");
			}
		} 
		
		catch (Exception e) 
		{
			log.info("SMSService.java :: addDataForEmail() :: connection is null "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			//DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
		}
		return status;
	}
	
	public JSONObject getContactNumberWalletIdForTag(String tid) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		try 
		{
			conn = DatabaseManager.getConnection();
			sql = "SELECT w.contact_number,w.user_id,w.wallet_id from tbl_wallet_master w, vehicle_tag_linking v where v.customer_id=w.user_id and w.status_id<2 and v.tid=?";
				
			log.info("getContactNumberWalletIdForTag QUERY ::"+sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, tid);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				log.info("CONTACT NUMBER :: "+rs.getString("contact_number"));
				log.info("WALLET ID :: "+rs.getString("wallet_id"));
				JSONObject resp = new JSONObject();
				resp.put("contactNo", rs.getString("contact_number"));
				resp.put("walletId", rs.getString("wallet_id"));
				resp.put("custId", rs.getString("user_id"));
				return resp;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
		return null;
	}
}
