package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.constants.WINConstants;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.UserDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class UserDAO {
	static Logger log = Logger.getLogger(UserDAO.class.getName());
	public String addUser(UserDTO userDTO, AddressDTO addressDTO, String userId, Connection conn) {
		PreparedStatement ps = null;

		try {
						
			String query = "INSERT INTO user_info (user_id, user_name, email_id, contact_number, status, created_by, created_on) "
					+ "VALUES (?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, userDTO.getUserId());
			ps.setString(2, userDTO.getUserName());
			ps.setString(3, userDTO.getEmailId());
			ps.setString(4, userDTO.getContactNumber());
			ps.setString(5, WINConstants.NEW);
			ps.setString(6, userId);
			ps.setString(7, new DateUtils().getCurrnetDate());
			
			if(ps.executeUpdate()>0) {
				log.info("User created successfully.");
				return "1";
			}else {
				return "0";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public String approveUser(UserDTO userDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
        PreparedStatement ps = null;
        String sql = "UPDATE user_info set status = ?, modified_by = ?, modified_on = ? where user_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, WINConstants.APPROVE);
            ps.setString(2, userId);
            ps.setString(3, currentDate);
            ps.setString(4, userDTO.getUserId());
            if (ps.executeUpdate()>0) {
            	log.info("User approved successfully.");
            	return "1";
			}else {
				return "0";
			}
            
        }
        catch (Exception e) {
            log.info(("Exception in approveUser() :: " + e.getMessage()));
            e.printStackTrace();
        }
        finally {
        	DatabaseManager.closePreparedStatement(ps);
        }
        return "0";
	}
	
	public String rejectUser(UserDTO userDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
        PreparedStatement ps = null;
        String sql = "UPDATE user_info set status = ?, modified_by = ?, modified_on = ? where user_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, WINConstants.REJECT);
            ps.setString(2, userId);
            ps.setString(3, currentDate);
            ps.setString(4, userDTO.getUserId());
            if (ps.executeUpdate()>0) {
            	log.info("User rejected successfully.");
            	return "1";
			}else {
				return "0";
			}
            
        }
        catch (Exception e) {
            log.info(("Exception in rejectUser() :: " + e.getMessage()));
            e.printStackTrace();
        }
        finally {
        	DatabaseManager.closePreparedStatement(ps);
        }
        return "0";
	}
	
	public String deleteUser(UserDTO userDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
        PreparedStatement ps = null;
        String sql = "UPDATE user_info set status = ?, modified_by = ?, modified_on = ? where user_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, WINConstants.DELETE);
            ps.setString(2, userId);
            ps.setString(3, currentDate);
            ps.setString(4, userDTO.getUserId());
            if (ps.executeUpdate()>0) {
            	log.info("User rejected successfully.");
            	return "1";
			}else {
				return "0";
			}
            
        }
        catch (Exception e) {
            log.info(("Exception in rejectUser() :: " + e.getMessage()));
            e.printStackTrace();
        }
        finally {
        	DatabaseManager.closePreparedStatement(ps);
        }
        return "0";
	}

	public List<UserDTO> getUserList(String userId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<UserDTO> lst = new ArrayList<UserDTO>();
		String query = null;
		try {
			query = "select * from user_info WHERE status not in ('DELETE') order by created_on desc";
			//query = "select * from user_info ui, user_master um WHERE ui.user_id=um.user_id AND ui.status not in ('DELETE') order by ui.created_on desc";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			UserDTO userDTO = null;
			while (rs.next()) {
				userDTO = new UserDTO();
				//user_id, user_name, email_id, contact_number, status, created_by, created_on
				userDTO.setUserId(rs.getString("user_id"));
				userDTO.setUserName(rs.getString("user_name"));
				userDTO.setEmailId(rs.getString("email_id"));
				userDTO.setContactNumber(rs.getString("contact_number"));
				userDTO.setStatus(rs.getString("status"));
				userDTO.setCreatedBy(rs.getString("created_by"));
				userDTO.setCreatedOn(rs.getString("created_on"));
				if(rs.getString("user_id").startsWith("UM")) {
					userDTO.setUserType("MAKER");
				}
				if(rs.getString("user_id").startsWith("UC")) {
					userDTO.setUserType("CHECKER");
				}
				lst.add(userDTO);
			}
			return lst;
		} catch (Exception e) {
			log.error("Exception in getting list of getUserList() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;
	}

	public UserDTO getUserById(String userId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDTO userDTO= new UserDTO();
		String query = null;
		try {
			query = "select * from user_info WHERE user_id=? AND status not in ('DELETE')";
			ps = conn.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				//user_id, user_name, email_id, contact_number, status, created_by, created_on
				userDTO.setUserId(rs.getString("user_id"));
				userDTO.setUserName(rs.getString("user_name"));
				userDTO.setEmailId(rs.getString("email_id"));
				userDTO.setContactNumber(rs.getString("contact_number"));
				userDTO.setStatus(rs.getString("status"));
				//userDTO.setCreatedBy(rs.getString("created_by"));
				//userDTO.setCreatedOn(rs.getString("created_on"));
			}
			return userDTO;
		} catch (Exception e) {
			log.error("Exception in getting list of getUserById() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return userDTO;
	}
	
	public String updateUserMaster(String userId, Connection conn) {
		//String currentDate = new DateUtils().getCurrnetDate();
        PreparedStatement ps = null;
        String sql = "UPDATE user_master set is_active = ?, is_deleted = ? where user_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "0");
            ps.setString(2, "1");
            ps.setString(3, userId);
            if (ps.executeUpdate()>0) {
            	log.info("User master updated successfully.");
            	return "1";
			}else {
				return "0";
			}
            
        }
        catch (Exception e) {
            log.info(("Exception in rejectUser() :: " + e.getMessage()));
            e.printStackTrace();
        }
        finally {
        	DatabaseManager.closePreparedStatement(ps);
        }
        return "0";
	}

	public String updateUser(UserDTO userDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
        PreparedStatement ps = null;
        String sql = "UPDATE user_info set user_name=?, email_id=?, contact_number=?, modified_by = ?, modified_on = ? where user_id = ?";
        try {
            ps = conn.prepareStatement(sql);
          //user_id, user_name, email_id, contact_number, status, created_by, created_on
            ps.setString(1, userDTO.getUserName());
            ps.setString(2, userDTO.getEmailId());
            ps.setString(3, userDTO.getContactNumber());
            ps.setString(4, userId);
            ps.setString(5, currentDate);
            ps.setString(6, userDTO.getUserId());
            if (ps.executeUpdate()>0) {
            	log.info("User updated successfully.");
            	return "1";
			}else {
				return "0";
			}
            
        }
        catch (Exception e) {
            log.info(("Exception in updateUser() :: " + e.getMessage()));
            e.printStackTrace();
        }
        finally {
        	DatabaseManager.closePreparedStatement(ps);
        }
        return "0";
	}
	
	public static void updateAddress(Connection conn, AddressDTO addressDTO, String userId) {
		PreparedStatement ps = null;

		try {
			//user_id, resi_address1=?, resi_address2=?, resi_pin=?, resi_city=?, resi_state=?, business_add1=?, business_add2=?, business_pin=?, business_city=?, business_state=?
			String query = "UPDATE address_info SET resi_address1=?, resi_address2=?, resi_pin=?, resi_city=?, resi_state=? WHERE user_id = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, addressDTO.getResiAddress1());
			ps.setString(2, addressDTO.getResiAddress1());
			ps.setString(3, addressDTO.getResiPin());
			ps.setString(4, addressDTO.getResiCity());
			ps.setString(5, addressDTO.getResiState());
			ps.setString(6, addressDTO.getUserId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}

}
