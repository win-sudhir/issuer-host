package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.contants.WINConstants;
import com.winnovature.dto.BranchAccountDTO;
import com.winnovature.dto.BranchDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;
import com.winnovature.utils.HashGenerator;

public class BranchDAO {
	static Logger log = Logger.getLogger(AgentMakerDAO.class.getName());

	public String addBranchInfo(BranchDTO branchDTO, String userId, Connection conn) {
		PreparedStatement ps = null;
		String currentDate = new DateUtils().getCurrnetDate();
		try {

			String query = "INSERT INTO branch_info (branch_id, bank_branch_id, branch_name, email_id, contact_number, status, action_status, created_by, created_on) "
					+ "VALUES (?,?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, branchDTO.getBranchId());
			ps.setString(2, branchDTO.getBankBranchId());
			ps.setString(3, branchDTO.getBranchName());
			ps.setString(4, branchDTO.getEmailId());
			ps.setString(5, branchDTO.getContactNumber());
			ps.setString(6, WINConstants.NEW);
			ps.setString(7, WINConstants.NEW);
			ps.setString(8, userId);
			ps.setString(9, currentDate);

			if (ps.executeUpdate() > 0) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public String addBranchAccountInfo(BranchDTO branchDTO, BranchAccountDTO branchAccountDTO, Connection conn) {
		PreparedStatement ps = null;
		String currentDate = new DateUtils().getCurrnetDate();
		try {

			String query = "INSERT INTO account_info (user_id, bank_name, account_number, account_type, ifsc_code, branch_address, status, created_on) "
					+ "VALUES (?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, branchDTO.getBranchId());
			ps.setString(2, branchAccountDTO.getBankName());
			ps.setString(3, branchAccountDTO.getAccountNumber());
			ps.setString(4, branchAccountDTO.getAccountType());
			ps.setString(5, branchAccountDTO.getIfscCode());
			ps.setString(6, branchAccountDTO.getBranchAddress());
			ps.setString(7, WINConstants.NEW);
			ps.setString(8, currentDate);

			if (ps.executeUpdate() > 0) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public String addBranchAddressInfo(BranchDTO branchDTO, String userId, Connection conn) {
		PreparedStatement ps = null;

		try {

			String query = "INSERT INTO address_info (user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state, business_add1, business_add2, business_pin, business_city, business_state) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, branchDTO.getBranchId());
			ps.setString(2, branchDTO.getAddress1());
			ps.setString(3, branchDTO.getAddress2());
			ps.setString(4, branchDTO.getPin());
			ps.setString(5, branchDTO.getCity());
			ps.setString(6, branchDTO.getState());
			ps.setString(7, branchDTO.getAddress1());
			ps.setString(8, branchDTO.getAddress2());
			ps.setString(9, branchDTO.getPin());
			ps.setString(10, branchDTO.getCity());
			ps.setString(11, branchDTO.getState());

			if (ps.executeUpdate() > 0) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public List<BranchDTO> getBranchListForMaker(Connection conn) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BranchDTO> lst = new ArrayList<BranchDTO>();
		String query = null;
		try {
			query = "select * from branch_info WHERE status in ('ACTIVE','APPROVE') order by created_on desc";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			BranchDTO branchDTO = null;

			while (rs.next()) {

				branchDTO = new BranchDTO();
				branchDTO.setBranchId(rs.getString("branch_id"));
				branchDTO.setBranchName(rs.getString("branch_name"));
				branchDTO.setContactNumber(rs.getString("contact_number"));
				branchDTO.setEmailId(rs.getString("email_id"));

				lst.add(branchDTO);
			}
			return lst;
		} catch (Exception e) {
			log.error("Exception in getting list of getAgentList() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;

	}

	public List<BranchDTO> getBranchListForChecker(Connection conn) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BranchDTO> lst = new ArrayList<BranchDTO>();
		String query = null;
		try {
			query = "select * from branch_info WHERE action_status in ('NEW','ACTIVE') order by created_on desc";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			BranchDTO branchDTO = null;

			while (rs.next()) {

				branchDTO = new BranchDTO();
				branchDTO.setBranchId(rs.getString("branch_id"));
				branchDTO.setBranchName(rs.getString("branch_name"));
				branchDTO.setContactNumber(rs.getString("contact_number"));
				branchDTO.setEmailId(rs.getString("email_id"));

				if (rs.getString("action_status").equalsIgnoreCase("NEW")) {
					branchDTO.setReason("Approve For New Branch");
					branchDTO.setAction("NEW");
				} else {
					branchDTO.setReason("Approved");
					branchDTO.setAction("Approve");
				}
				lst.add(branchDTO);
			}
			return lst;
		} catch (Exception e) {
			log.error("Exception in getting list of getAgentList() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;

	}

	public void deleteBranchChecker(Connection conn, String userId, String branchId) {
		PreparedStatement ps = null;

		try {

			String query = "UPDATE branch_info SET status=?, approved_by=?, approved_on=?, is_approved=?, is_deleted=? WHERE branch_id = ? ";
			String currentDate = new DateUtils().getCurrnetDate();
			ps = conn.prepareStatement(query);
			ps.setString(1, WINConstants.DELETE);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, "1");
			ps.setString(5, "1");
			ps.setString(6, branchId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}

	}

	public String approveBranch(Connection conn, String userId, String branchId) {
		PreparedStatement ps = null;

		try {

			String query = "UPDATE branch_info SET status=?, approved_by=?, approved_on=?, is_approved=?, action_status =? WHERE branch_id = ? ";
			String currentDate = new DateUtils().getCurrnetDate();
			ps = conn.prepareStatement(query);
			ps.setString(1, WINConstants.ACTIVE);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, "1");
			ps.setString(5, WINConstants.ACTIVE);
			ps.setString(6, branchId);

			if (ps.executeUpdate() > 0) {
				log.info("Agent Approve Successfully.");
				return "1";
			} else {
				return "0";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public void rejectBranch(Connection conn, String userId, String branchId) {
		PreparedStatement ps = null;

		try {

			String query = "UPDATE branch_info SET status=?, approved_by=?, approved_on=?, is_approved=?, action_status =? WHERE branch_id = ? ";
			String currentDate = new DateUtils().getCurrnetDate();
			ps = conn.prepareStatement(query);
			ps.setString(1, WINConstants.REJECT);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, "1");
			ps.setString(5, WINConstants.REJECT);
			ps.setString(6, branchId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}

	}

	public void deleteBranchMaker(Connection conn, String userId, String branchId) {
		PreparedStatement ps = null;

		try {

			String query = "UPDATE branch_info SET action_status =?, approved_by=?, approved_on=? WHERE branch_id = ? ";
			String currentDate = new DateUtils().getCurrnetDate();
			ps = conn.prepareStatement(query);
			ps.setString(1, WINConstants.DELETE);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, branchId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}

	}

	public static void insertUser(String userId, String roleId, String createdBy, String password, String emailId,
			Connection conn) {

		PreparedStatement ps = null;

		String sqluser = "insert into user_master (user_id, password, role_id, email_id,last_login_date, last_login_ip, is_active, is_deleted, created_by, created_on, approved_by, approved_on)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		String currentDate = new DateUtils().getCurrnetDate();

		password = HashGenerator.sha256(password);
		try {

			ps = conn.prepareStatement(sqluser);
			log.info("User ID : " + userId);
			ps.setString(1, userId);
			ps.setString(2, password);
			ps.setString(3, roleId);
			ps.setString(4, emailId);
			ps.setString(5, currentDate);
			ps.setString(6, "0.0.0.0");
			ps.setString(7, "1");
			ps.setString(8, "0");
			ps.setString(9, createdBy);
			ps.setString(10, currentDate);
			ps.setString(11, "");
			ps.setString(12, currentDate);
			ps.executeUpdate();

			// PasswordManager.insertUserHistory(userid, password);

			log.info("insertUser()   :::   User inserted successfully in user master.........");

		} catch (Exception e) {
			log.info("User Insert opration rolled back..." + e.getMessage());
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(con);
		}
	}

	public static String getBranchEmailId(String branchId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String query = "select email_id from branch_info WHERE branch_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, branchId);
			rs = ps.executeQuery();

			while (rs.next()) {
				return rs.getString("email_id");
			}

		} catch (Exception e) {
			log.error("Exception in getting list of getAgentList() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return null;
	}

	public List<String> getBrancgListForSaleAgent(Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> branchLst = new ArrayList<String>();
		try {
			String query = "SELECT branch_id FROM branch_info where status = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, "ACTIVE");
			rs = ps.executeQuery();

			while (rs.next()) {
				log.info("------------------"+rs.getString("branch_id"));
				branchLst.add(rs.getString("branch_id"));
			}

		} catch (Exception e) {
			log.error("Exception in getting list of getAgentList() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return branchLst;
	}

	public static BranchDTO getBranchById(String branchId, Connection conn) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		BranchDTO branchDTO = new BranchDTO();
		try {
			String query = "SELECT * FROM branch_info WHERE branch_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, branchId);
			rs = ps.executeQuery();
			//branch_id, bank_branch_id, branch_name, email_id, contact_number, is_active, status, created_by, created_on, approved_by, approved_on, is_approved, is_deleted, rodt, isModifiedby, act_deact_status, act_date, dact_date, last_updated_by, delete_status, action_status
			if (rs.next()) {
				
				branchDTO.setBranchId(rs.getString("branch_id"));
				branchDTO.setBankBranchId(rs.getString("bank_branch_id"));
				branchDTO.setBranchName(rs.getString("branch_name"));
				branchDTO.setEmailId(rs.getString("email_id"));
				branchDTO.setContactNumber(rs.getString("contact_number"));
				
			}
			return branchDTO;
		}

		catch (Exception e) {
			log.error("getBranchById()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return branchDTO;
	}

	public static String updateBranch(BranchDTO branchDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
		PreparedStatement ps = null;
		//branch_id, bank_branch_id, branch_name, email_id, contact_number, is_active, status, created_by, created_on, approved_by, approved_on, is_approved, is_deleted, rodt, isModifiedby, act_deact_status, act_date, dact_date, last_updated_by, delete_status, action_status
		String sql = "UPDATE branch_info SET bank_branch_id=?, branch_name=?, email_id=?, contact_number=?, last_updated_by=?, act_date= ? WHERE branch_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, branchDTO.getBankBranchId());
			ps.setString(2, branchDTO.getBranchName());
			ps.setString(3, branchDTO.getEmailId());
			ps.setString(4, branchDTO.getContactNumber());
			ps.setString(5, userId);
			ps.setString(6, currentDate);
			ps.setString(7, branchDTO.getBranchId());
			
			if (ps.executeUpdate() > 0) {
				log.info("branch updated successfully.");
				return "1";
			} else {
				return "0";
			}

		} catch (Exception e) {
			log.info(("Exception in updateAgent() :: " + e.getMessage()));
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

}
