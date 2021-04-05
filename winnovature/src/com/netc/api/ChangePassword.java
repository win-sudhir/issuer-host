package com.netc.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.PasswordDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.PasswordManager;
import com.winnovature.utils.ResponseHandler;;

@WebServlet("/user/changepassword")
public class ChangePassword extends HttpServlet {
	static Logger log = Logger.getLogger(ChangePassword.class.getName());
	private static final long serialVersionUID = 1L;
       
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		boolean checkOldpass = false;
		JSONObject jreq = new JSONObject();
		JSONObject jresp = new JSONObject();
		PrintWriter out = response.getWriter();
		
		StringBuffer sbuffer = new StringBuffer();
		String line = null;
		String oldPassword;
		String newPassword;
		String confirmPassword;
		Connection conn = null;
						
		try
		{
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
			{
				sbuffer.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			jreq = new JSONObject(sbuffer.toString());
			jresp = new JSONObject();
			
			conn = DatabaseManager.getAutoCommitConnection();
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);
			
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			
			String userId = request.getHeader("userId").toString();
			//String auth_token = request.getHeader("Authorization").toString();//request.getParameter("auth_token");
						
			//if( userId != null &&  auth_token != null && LoginDao.isValidSession(userId, auth_token))
			//{
			oldPassword = jreq.getString("oldPassword");
			newPassword = jreq.getString("newPassword");
			confirmPassword = jreq.getString("confirmPassword");
			 
		
			
			if (oldPassword != null && newPassword != null && confirmPassword != null)
			{
				if (!oldPassword.equalsIgnoreCase(newPassword))
				{
					if (newPassword.equals(confirmPassword)) 
					{
						if(newPassword.length()>=8)
						{
							
							ResponseHandler respFormat = new PasswordManager().getPasswordStats(newPassword);
							
							log.info("Password Format : resp : "+respFormat.getRespCode()+" , respmessage : "+respFormat.getRespMessage());
							
							if(respFormat.getRespCode() == 0)
							{
							
							PasswordDAO obj = new PasswordDAO();
							checkOldpass = obj.validateOldPassword(oldPassword,userId);
							
							if(checkOldpass)
							{
							
								boolean checkHistory = obj.validateNewPasswordHistory(newPassword, userId);
								if(!checkHistory)
								{
								
								boolean changedPass = obj.updatePassword(userId,oldPassword,newPassword);
								if(changedPass)
								{
									jresp.put("message","Password Changed Successfully. !! ");
									jresp.put("status", "1");
									log.info("Password Changed Successfully. !!  userID  :: "+userId);
								}
								
								else
								{
									jresp.put("message","Password not changed please try again ..!! ");
									jresp.put("status", "0");
									log.info("Password not changed getting error while updating password");
								}
								
								}
								else
								{
									jresp.put("message", "Please Choose Another Password, New password must be differ than from Last 5 Password History!");
									jresp.put("status", "0");
									log.info("Password Matched in USERHISTORY :: ");
								}
								
							}
							
							else
							{
								jresp.put("message","Old password mismatched please Try again !! ");
								jresp.put("status", "0");
							}
							
							
							}
							else
							{
								jresp.put("message",respFormat.getRespMessage());
								jresp.put("status", "0");
							}
							
						}
						
						else{
							jresp.put("message","New password sholud be minimum 8 character please Try again !! ");
							jresp.put("status", "0");
						}	
					}
					
					else
					{
						jresp.put("message","New and Confirm password mismached... Try again.... !! ");
						jresp.put("status", "0");
					}
				}
				
				else{
					jresp.put("message","old and new password is the same please enter the different password !! ");
					jresp.put("status", "0");
				}
			}
			else
			{
				jresp.put("message","Password cannot be null please try again... ");
				jresp.put("status", "0");
				
			}
			
			//}
			
			/*else{
				log.info("Invalid Session : "+auth_token+"  , flag = 0 ");
				jresp.put("flag","0");
			}*/
			
			
		}
		catch(Exception e)
		{
			jresp.put("message",e.getMessage());
			jresp.put("status", "0");
			log.info("ChangePassword.java   ::::   Exception  ::: " + e.getMessage());
			log.error("Getting Exception   :::    ",e);
		}
		finally
		{
			out.write(jresp.toString());	
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
		
	}

}
