package com.winnovature.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.winnovature.dto.AccountDTO;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.KycDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDTO;
import com.winnovature.dto.VehicleDTO.Vehicles;
import com.winnovature.utils.DatabaseManager;

public class CustomerValidation {
	static Logger log = Logger.getLogger(CustomerValidation.class.getName());
	public static ResponseDTO validateCustomerRequest(CustomerDTO customerDTO, 
			AddressDTO addressDTO, AccountDTO accountDTO, KycDTO kycDTO, VehicleDTO vehicleDTO, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		
		if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().isEmpty() 
				|| customerDTO.getContactNumber() == null || customerDTO.getContactNumber().isEmpty()
				|| customerDTO.getEmailId() == null || customerDTO.getEmailId().isEmpty()
				|| customerDTO.getGender() == null || customerDTO.getGender().isEmpty()
				|| customerDTO.getOccupation() == null || customerDTO.getOccupation().isEmpty()
				|| addressDTO.getResiAddress1() == null || addressDTO.getResiAddress1().isEmpty()
				|| addressDTO.getResiPin() == null || addressDTO.getResiPin().isEmpty()
				|| addressDTO.getResiCity() == null || addressDTO.getResiCity().isEmpty()
				|| addressDTO.getResiState() == null || addressDTO.getResiState().isEmpty()
				|| kycDTO.getAddressProofDoc() == null || kycDTO.getAddressProofDoc().isEmpty()
				|| kycDTO.getAddressProofId()== null || kycDTO.getAddressProofId().isEmpty()
				|| kycDTO.getAddressProofNo() == null || kycDTO.getAddressProofNo().isEmpty()
				|| kycDTO.getIdProofDoc() == null || kycDTO.getIdProofDoc().isEmpty()
				|| kycDTO.getIdProofNo() == null || kycDTO.getIdProofNo().isEmpty() 
				|| accountDTO.getAccountNumber() == null || accountDTO.getAccountNumber().isEmpty()
				|| accountDTO.getIfscCode() == null || accountDTO.getIfscCode().isEmpty()
				|| accountDTO.getBankName() == null || accountDTO.getBankName().isEmpty()
				|| accountDTO.getAccountType() == null || accountDTO.getAccountType().isEmpty()
				) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(CustomerErrorCode.WINNBU001.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNBU001.name());
			return responseDTO;
		}
		if (validateEmailId(customerDTO.getEmailId(), conn) ) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(CustomerErrorCode.WINNBU0019.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNBU0019.name());
			return responseDTO;
		}
		if (validateMobileNumber(customerDTO.getContactNumber(), conn) ) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(CustomerErrorCode.WINNBU0020.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNBU0020.name());
			return responseDTO;
		}
		if (validateAddreProofNumber(kycDTO.getAddressProofNo(), conn) ) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(CustomerErrorCode.WINNBU0021.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNBU0021.name());
			return responseDTO;
		}
		if (validatePanNumber(kycDTO.getIdProofNo(), conn) ) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(CustomerErrorCode.WINNBU0022.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNBU0022.name());
			return responseDTO;
		}
		//if(vehicleDTO.getVehicles().size()>0) {
		for (Vehicles vehicle : vehicleDTO.getVehicles()) 
		{
			if (validateVehicleNumber(vehicle.getVehicleNumber(), conn)) 
			{
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(CustomerErrorCode.WINNBU0023.getErrorMessage());
				responseDTO.setErrorCode(CustomerErrorCode.WINNBU0023.name());
				return responseDTO;
			}
		}
		//}
		
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	private static boolean validateMobileNumber(String mobileNumber, Connection conn){
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT contact_number FROM customer_info where contact_number = ? and status in('NEW','APPROVE','ACTIVE')";
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
	       log.error("Exception in while checking customer_info records"+e.getMessage());
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
		String sql = "SELECT email_id FROM customer_info where email_id=? and status in('NEW','APPROVE','ACTIVE')";
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
	       log.error("Exception in while checking customer_info records"+e.getMessage());
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
	private static boolean validateAddreProofNumber(String addProofNumber, Connection conn){
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT address_proof_no FROM customer_kyc_info where address_proof_no=?";
		log.info("validateEmailId Query ::"+sql);
		try
		{
			ps =  conn.prepareStatement(sql);
			ps.setString(1, addProofNumber);
		    rs = ps.executeQuery();
		    if(rs.next())
		    {
		    	return true;
		    }
		} 
		catch (Exception e)
		{
	       log.error("Exception in while checking customer_kyc_info records"+e.getMessage());
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
	private static boolean validatePanNumber(String panNumber, Connection conn){
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT id_proof_no FROM customer_kyc_info where id_proof_no=?";
		log.info("validateEmailId Query ::"+sql);
		try
		{
			ps =  conn.prepareStatement(sql);
			ps.setString(1, panNumber);
			rs = ps.executeQuery();
		    if(rs.next())
		    {
		    	return true;
		    }
		} 
		catch (Exception e)
		{
	       log.error("Exception in while checking customer_kyc_info records"+e.getMessage());
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
	
	private static boolean validateVehicleNumber(String vehicleNumber, Connection conn){
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT vehicle_number FROM customer_vehicle_info where vehicle_number=? and status='ACTIVE' ";
		log.info("validateVehicleNumber Query ::"+sql);
		try
		{
			ps =  conn.prepareStatement(sql);
			ps.setString(1, vehicleNumber);
			rs = ps.executeQuery();
		    if(rs.next())
		    {
		    	return true;
		    }
		} 
		catch (Exception e)
		{
	       log.error("Exception in while checking customer_vehicle_info records"+e.getMessage());
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
