package com.winnovature.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.winnovature.dto.AccountDTO;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.AgentDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.DatabaseManager;

public class AgentValidation {
	static Logger log = Logger.getLogger(AgentValidation.class.getName());
	public static ResponseDTO validateAgentRequest(AgentDTO agentDTO, AddressDTO addressDTO, AccountDTO accountDTO, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		
		if (agentDTO.getAgentName() == null || agentDTO.getAgentName().isEmpty() 
				|| agentDTO.getContactNumber() == null || agentDTO.getContactNumber().isEmpty()
				|| agentDTO.getEmailId() == null || agentDTO.getEmailId().isEmpty()
				|| agentDTO.getContactPersonName() == null || agentDTO.getContactPersonName().isEmpty()
				
				|| addressDTO.getResiAddress1() == null || addressDTO.getResiAddress1().isEmpty()
				|| addressDTO.getResiPin() == null || addressDTO.getResiPin().isEmpty()
				|| addressDTO.getResiCity() == null || addressDTO.getResiCity().isEmpty()
				|| addressDTO.getResiState() == null || addressDTO.getResiState().isEmpty()
				
								
				|| accountDTO.getAccountNumber() == null || accountDTO.getAccountNumber().isEmpty()
				|| accountDTO.getIfscCode() == null || accountDTO.getIfscCode().isEmpty()
				|| accountDTO.getBankName() == null || accountDTO.getBankName().isEmpty()
				|| accountDTO.getAccountType() == null || accountDTO.getAccountType().isEmpty()
				) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU001.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU001.name());
			return responseDTO;
		}
		if (validateEmailId(agentDTO.getEmailId(), conn) ) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU0019.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU0019.name());
			return responseDTO;
		}
		if (validateMobileNumber(agentDTO.getContactNumber(), conn) ) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU0020.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU0020.name());
			return responseDTO;
		}
		
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	private static boolean validateMobileNumber(String mobileNumber, Connection conn){
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT contact_number FROM agent_info where contact_number = ? and status in('NEW','APPROVE','ACTIVE')";
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
	       log.error("Exception in while checking agent records"+e.getMessage());
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
		String sql = "SELECT email_id FROM agent_info where email_id=? and status in('NEW','APPROVE','ACTIVE')";
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
	       log.error("Exception in while checking agent records"+e.getMessage());
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
