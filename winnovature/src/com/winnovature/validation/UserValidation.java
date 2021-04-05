package com.winnovature.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.UserDTO;
import com.winnovature.utils.DatabaseManager;

public class UserValidation {
	static Logger log = Logger.getLogger(UserValidation.class.getName());
	public static ResponseDTO validateUserRequest(UserDTO userDTO, 
			AddressDTO addressDTO, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		
		if (userDTO.getUserName() == null || userDTO.getUserName().isEmpty() 
				|| userDTO.getContactNumber() == null || userDTO.getContactNumber().isEmpty()
				|| userDTO.getEmailId() == null || userDTO.getEmailId().isEmpty()
				|| userDTO.getRoleId() == null || userDTO.getRoleId().isEmpty()
				
				|| addressDTO.getResiAddress1() == null || addressDTO.getResiAddress1().isEmpty()
				|| addressDTO.getResiPin() == null || addressDTO.getResiPin().isEmpty()
				|| addressDTO.getResiCity() == null || addressDTO.getResiCity().isEmpty()
				|| addressDTO.getResiState() == null || addressDTO.getResiState().isEmpty()
				) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(UserErrorCode.WINNUBU001.getErrorMessage());
			responseDTO.setErrorCode(UserErrorCode.WINNUBU001.name());
			return responseDTO;
		}
		if (validateEmailId(userDTO.getEmailId(), conn) ) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(UserErrorCode.WINNUBU0019.getErrorMessage());
			responseDTO.setErrorCode(UserErrorCode.WINNUBU0019.name());
			return responseDTO;
		}
		if (validateMobileNumber(userDTO.getContactNumber(), conn) ) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(UserErrorCode.WINNUBU0020.getErrorMessage());
			responseDTO.setErrorCode(UserErrorCode.WINNUBU0020.name());
			return responseDTO;
		}
		
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	private static boolean validateMobileNumber(String mobileNumber, Connection conn){
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT contact_number FROM user_info where contact_number = ? and status in('NEW','APPROVE','ACTIVE')";
		log.info("validateMobileNumber Query ::"+sql);
		try
		{
			//conn = DatabaseManager.getConnection();
			ps =  conn.prepareStatement(sql);
			ps.setString(1, mobileNumber);
		    rs = ps.executeQuery();
		    if(rs.next())
		    {
		    	return true;
		    }
		} 
		catch (Exception e)
		{
	       log.error("Exception in while checking user records"+e.getMessage());
	       log.error(e);
	       e.printStackTrace();
	       //return false;
	    }
		finally
		{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}
	private static boolean validateEmailId(String emailId, Connection conn){
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT email_id FROM user_info where email_id=? and status in('NEW','APPROVE','ACTIVE')";
		log.info("validateEmailId Query ::"+sql);
		try
		{
			ps =  conn.prepareStatement(sql);
			ps.setString(1, emailId);
			//ps.setString(2, "0");
		    rs = ps.executeQuery();
		    if(rs.next())
		    {
		    	return true;
		    }
		} 
		catch (Exception e)
		{
	       log.error("Exception in while checking user records"+e.getMessage());
	       log.error(e);
	       e.printStackTrace();
	       //return false;
	    }
		finally
		{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}
	
}
