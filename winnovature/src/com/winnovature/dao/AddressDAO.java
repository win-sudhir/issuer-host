package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.winnovature.dto.AddressDTO;
import com.winnovature.utils.DatabaseManager;

public class AddressDAO {
	static Logger log = Logger.getLogger(AddressDAO.class.getName());
	public static void addAddress(Connection conn, AddressDTO addressDTO) 
	{
		PreparedStatement ps = null;

		try {
						
			String query = "INSERT INTO address_info (user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state, business_add1, business_add2, business_pin, business_city, business_state) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, addressDTO.getUserId());
			ps.setString(2, addressDTO.getResiAddress1());
			ps.setString(3, addressDTO.getResiAddress2());
			ps.setString(4, addressDTO.getResiPin());
			ps.setString(5, addressDTO.getResiCity());
			ps.setString(6, addressDTO.getResiState());
			ps.setString(7, addressDTO.getBusinessAdd1());
			ps.setString(8, addressDTO.getBusinessAdd2());
			ps.setString(9, addressDTO.getBusinessPin());
			ps.setString(10, addressDTO.getBusinessCity());//always 0 corporate
			ps.setString(11, addressDTO.getBusinessState());
			
			ps.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	public AddressDTO getAddressById(Connection conn, String customerId) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		AddressDTO addressDTO = new AddressDTO();
		try {
			String query = "SELECT * FROM address_info WHERE user_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			//CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				//user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state, 
				//business_add1, business_add2, business_pin, business_city, business_state
				addressDTO.setUserId(rs.getString("user_id"));
				addressDTO.setResiAddress1(rs.getString("resi_address1"));
				addressDTO.setResiAddress2(rs.getString("resi_address2"));
				addressDTO.setResiPin(rs.getString("resi_pin"));
				addressDTO.setResiCity(rs.getString("resi_city"));
				addressDTO.setResiState(rs.getString("resi_state"));
				
				addressDTO.setBusinessAdd1(rs.getString("business_add1"));
				addressDTO.setBusinessAdd2(rs.getString("business_add2"));
				addressDTO.setBusinessPin(rs.getString("business_pin"));
				addressDTO.setBusinessCity(rs.getString("business_city"));
				addressDTO.setBusinessState(rs.getString("business_state"));
			}
			return addressDTO;
		}

		catch (Exception e) {
			log.error("getSingleAddress()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return addressDTO;
	}
	
	public AddressDTO getAddressByIdForChecker(Connection conn, String customerId) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		AddressDTO addressDTO = new AddressDTO();
		try {
			String query = "SELECT * FROM address_info_edited WHERE user_id=? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			//CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				//user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state, 
				//business_add1, business_add2, business_pin, business_city, business_state
				addressDTO.setUserId(rs.getString("user_id"));
				addressDTO.setResiAddress1(rs.getString("resi_address1"));
				addressDTO.setResiAddress2(rs.getString("resi_address2"));
				addressDTO.setResiPin(rs.getString("resi_pin"));
				addressDTO.setResiCity(rs.getString("resi_city"));
				addressDTO.setResiState(rs.getString("resi_state"));
				
				addressDTO.setBusinessAdd1(rs.getString("business_add1"));
				addressDTO.setBusinessAdd2(rs.getString("business_add2"));
				addressDTO.setBusinessPin(rs.getString("business_pin"));
				addressDTO.setBusinessCity(rs.getString("business_city"));
				addressDTO.setBusinessState(rs.getString("business_state"));
			}
			return addressDTO;
		}

		catch (Exception e) {
			log.error("getSingleAddress()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return addressDTO;
	}
	
	public static void updateAddress(Connection conn, AddressDTO addressDTO, String userId) {
		PreparedStatement ps = null;

		try {
			//user_id, resi_address1=?, resi_address2=?, resi_pin=?, resi_city=?, resi_state=?, business_add1=?, business_add2=?, business_pin=?, business_city=?, business_state=?
			String query = "UPDATE address_info SET resi_address1=?, resi_address2=?, resi_pin=?, resi_city=?, resi_state=?, business_add1=?, business_add2=?, business_pin=?, business_city=?, business_state=?  WHERE user_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, addressDTO.getResiAddress1());
			ps.setString(2, addressDTO.getResiAddress1());
			ps.setString(3, addressDTO.getResiPin());
			ps.setString(4, addressDTO.getResiCity());
			ps.setString(5, addressDTO.getResiState());
			ps.setString(6, addressDTO.getBusinessAdd1());
			ps.setString(7, addressDTO.getBusinessAdd2());
			ps.setString(8, addressDTO.getBusinessPin());
			ps.setString(9, addressDTO.getBusinessCity());
			ps.setString(10, addressDTO.getBusinessState());
			ps.setString(11, addressDTO.getUserId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	
	
	public static void addEditedAddress(Connection conn, AddressDTO addressDTO, String userId) {
		PreparedStatement ps = null;

		try {
						
			String query = "INSERT INTO address_info_edited (user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state, business_add1, business_add2, business_pin, business_city, business_state) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, addressDTO.getUserId());
			ps.setString(2, addressDTO.getResiAddress1());
			ps.setString(3, addressDTO.getResiAddress2());
			ps.setString(4, addressDTO.getResiPin());
			ps.setString(5, addressDTO.getResiCity());
			ps.setString(6, addressDTO.getResiState());
			ps.setString(7, addressDTO.getBusinessAdd1());
			ps.setString(8, addressDTO.getBusinessAdd2());
			ps.setString(9, addressDTO.getBusinessPin());
			ps.setString(10, addressDTO.getBusinessCity());//always 0 corporate
			ps.setString(11, addressDTO.getBusinessState());
			
			ps.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		
	}
}
