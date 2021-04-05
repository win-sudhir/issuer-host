package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.PropertyReader;


public class LoginDao 
{
	static Logger log = Logger.getLogger(LoginDao.class.getName());
	public boolean isValidUserLogin(String userid,String pass) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//String password = FleetService.getMD5Password(pass);
		try{
			conn = DatabaseManager.getConnection();
			String sql ="SELECT * FROM tbl_user_master where user_id=? and password=? and status_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userid);
			ps.setString(2, pass);//password	
			ps.setString(3, "1");	
			rs = ps.executeQuery();
			if(rs.next()){
				int flag = rs.getInt("status_id");
				if(flag==1){
					return true;
				}	
			}else
			{
				return false; 
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
		return false;
	} 
	
	public static boolean isValidSession(String userId,String sessionId) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = DatabaseManager.getConnection();
			//user_id, auth_token, rodt
			String sql ="SELECT * FROM tbl_session_master where user_id = ? and auth_token = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, sessionId);	
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				if(userId.equalsIgnoreCase("STFleetOwner"))
				{
					return true;
				}
			else
			{
				if(loginTimeCheck(userId,sessionId))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			}	
			else
			{
				log.info("LoginDao.java  ::: Token in request  :: "+sessionId+" user Id :: "+userId+" Not exists in session master" );
				return false; 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
		return false;
	} 
	
	public static String geSessionToken(String userId) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = DatabaseManager.getConnection();
			//user_id, auth_token, rodt
			String sql ="SELECT * FROM tbl_session_master where user_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			//ps.setString(2, sessionId);	
			rs = ps.executeQuery();
			if(rs.next())
			{
					return rs.getString("auth_token");
			}	
			else
			{
				return null; 
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
		return null;
	} 
	
	public boolean insertSessionId(Connection conn, String userid,String sessionId)
	  {
	    //Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    PreparedStatement ps2 = null;
	    try
	    {
	      //conn = DatabaseManager.getConnection();;
	      String q1 = "delete from tbl_session_master where user_id=?";
	      ps = conn.prepareStatement(q1);
	      ps.setString(1, userid);
	      ps.executeUpdate();
	      
	      log.info("LoginDao.java :: while Login :: insertSessionId() :: delete from tbl_session_master where user_id = "+userid);
	      
	      String q2 = "insert into tbl_session_master values(?,?,?)";
	      ps2 = conn.prepareStatement(q2);
	      ps2.setString(1, userid);
	      ps2.setString(2, sessionId);
	      ps2.setString(3,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	      ps2.executeUpdate();
	      log.info("LoginDao.java :: while Login :: insertSessionId() :: insert into tbl_session_master userId : "+userid+"  , sessionId : "+sessionId);
	      
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      return false;
	    }
	    finally
	    {
	    	DatabaseManager.closeResultSet(rs);
	    	DatabaseManager.closePreparedStatement(ps2);
	    	DatabaseManager.closePreparedStatement(ps);
	    	//DatabaseManager.closeConnection(conn);
	    
	     }
	    return true;
	  }
	
	
	public boolean deleteSessionIdLogout(String userid,String sessionId)
	  {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    try
	    {
	      conn = DatabaseManager.getConnection();;
	      String q1 = "delete from tbl_session_master where user_id=?";
	      ps = conn.prepareStatement(q1);
	      ps.setString(1, userid);
	      ps.execute();
	      
	      log.info("LoginDao.java :: while Logout :: deleteSessionIdLogout() :: delete from tbl_session_master where user_id = "+userid);
	     
	      return true;
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      return false;
	    }
	    finally
	    {
	    	DatabaseManager.closePreparedStatement(ps);
	    	DatabaseManager.closeConnection(conn);
	    
	     }
	  }
	
	
	public static String getUserLoginTime(String userId) throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String time = null;
		try{
			conn = DatabaseManager.getConnection();
			String sql ="SELECT * FROM tbl_session_master where user_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if(rs.next())
			{
				time = rs.getString("rodt");
				return time;
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
		return time;
	} 
	
	public static boolean loginTimeCheck(String userId, String sessionId)  
	{
		// final long TEN_MINUTES = 5 * 60 * 1000;  
		
		boolean flag = false;
		
		try 
		{
			String sessionTimeOut = PropertyReader.getPropertyValue("sessionTimeOut");
			final long TEN_MINUTES = Long.parseLong(sessionTimeOut) * 60 * 1000;
			
			String loginTime = null;
			Long login = 0l;
			
				
				loginTime = getUserLoginTime(userId); //"2018-09-21 19:50:33";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(loginTime);
				
				login = System.currentTimeMillis() - date.getTime();  // current different 
				
				log.info("loginTime : "+loginTime+" , date : "+date+" , Date.getTime :: "+date.getTime()+" , login : "+login+"  , System.currentTimeMillis() : "+System.currentTimeMillis()+"  , TimeUnit : "+TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()));
				log.info("Get Request Time in milliseconds : "+login);
				log.info("Session Time out In milliseconds : "+TEN_MINUTES);
			
			if (login > TEN_MINUTES) 
			{
			    log.info("Get Request is older than "+sessionTimeOut+" minutes then session out.");
			    flag = false;
			}
			else
			{
				log.info("Get Request inside the "+sessionTimeOut+" minutes so session to be continue..");
				//update login time
				LoginDao loginDao = new LoginDao();
				loginDao.updateLoginTime(userId, sessionId);
				
				flag = true;
			}
		} 
		
		catch (Exception e)
		{	
			log.info("Exception in LoginDao.loginTimeCheck() :: "+e.getMessage());
			e.printStackTrace();
		} 
		
		return flag;
    }
	
	public void updateLoginTime(String userId,String sessionId) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try{
			String sql ="UPDATE tbl_session_master set rodt=? where user_id=? and auth_token=?";
			conn = DatabaseManager.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, current_date);
			ps.setString(2, userId);	
			ps.setString(3, sessionId);	
			ps.executeUpdate();
			log.info("SESSION TIME HAS BEEN UPDATED SUCCESSFULLY...");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
	} 
	
	public void updateUserLoginIp(String userid,String ip) 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//String password = FleetService.getMD5Password(pass);
		String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try{
			String sql ="UPDATE tbl_user_master set last_login_date=?, last_login_ip=? where user_id=?";
			conn = DatabaseManager.getConnection();
			//conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.setString(1, current_date);
			ps.setString(2, ip);	
			ps.setString(3, userid);	
			ps.executeUpdate();
			//conn.setAutoCommit(false);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
	} 
	
	
	
	
	public static boolean isValidRequest(String userId, String hash)  {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = DatabaseManager.getConnection();
			// user_id, auth_token, rodt
			sql = "SELECT * FROM tbl_dupe_txn_data where user_id = ?";
			//sql = "SELECT s.*,c.*  FROM tbl_session_master s, tbl_customer_master c where c.contact_number=? and s.auth_token=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				log.info("DB HASH >>>>> " + rs.getString("hash_data"));
				sql = "update tbl_dupe_txn_data set hash_data = '"+hash+"' where user_id='"+userId+"'";
				ps1 = conn.prepareStatement(sql);
				ps1.executeUpdate();
				
				if(rs.getString("hash_data").equals(hash))
				{
					return false;
				}
				else
				{
					return true;
				}
				
			} 
			else 
			{
				sql = "insert into tbl_dupe_txn_data values('"+userId+"','"+hash+"','"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"')";
				ps1 = conn.prepareStatement(sql);
				ps1.executeUpdate();
				return true;
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps1);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
			
		}
		return false;
	}
	
}
