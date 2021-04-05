package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.winnovature.contants.WINConstants;
import com.winnovature.dto.AccountDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class AccountDAO {
	static Logger log = Logger.getLogger(AccountDAO.class.getName());
	public static void addAccount(Connection conn, AccountDTO accountDTO, String userId) 
	{
		PreparedStatement ps = null;

		try {
						
			String query = "INSERT INTO account_info (user_id, bank_name, account_number, account_type, ifsc_code, branch_address, status, created_on) "
					+ "VALUES (?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, accountDTO.getUserId());
			ps.setString(2, accountDTO.getBankName());
			ps.setString(3, accountDTO.getAccountNumber());
			ps.setString(4, accountDTO.getAccountType());
			ps.setString(5, accountDTO.getIfscCode());
			ps.setString(6, accountDTO.getBranchAddress());
			ps.setString(7, WINConstants.NEW);
			ps.setString(8, new DateUtils().getCurrnetDate());
			
			ps.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	public AccountDTO getAccountById(Connection conn, String customerId) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		AccountDTO accountDTO = new AccountDTO();
		try {
			String query = "SELECT * FROM account_info WHERE user_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			//CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				//System.out.println(customerId+"============");
				//user_id, bank_name, account_number, account_type, ifsc_code, branch_address, status, created_on, rodt
				accountDTO.setUserId(rs.getString("user_id"));
				accountDTO.setBankName(rs.getString("bank_name"));
				accountDTO.setAccountNumber(rs.getString("account_number"));
				accountDTO.setAccountType(rs.getString("account_type"));
				accountDTO.setIfscCode(rs.getString("ifsc_code"));
				accountDTO.setBranchAddress(rs.getString("branch_address"));
			}
			return accountDTO;
		}

		catch (Exception e) {
			log.error("getSingleAccount()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return accountDTO;
	}
	
	public AccountDTO getAccountByIdForChecker(Connection conn, String customerId) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		AccountDTO accountDTO = new AccountDTO();
		try {
			String query = "SELECT * FROM account_info_edited WHERE user_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			//CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				//System.out.println(customerId+"============");
				//user_id, bank_name, account_number, account_type, ifsc_code, branch_address, status, created_on, rodt
				accountDTO.setUserId(rs.getString("user_id"));
				accountDTO.setBankName(rs.getString("bank_name"));
				accountDTO.setAccountNumber(rs.getString("account_number"));
				accountDTO.setAccountType(rs.getString("account_type"));
				accountDTO.setIfscCode(rs.getString("ifsc_code"));
				accountDTO.setBranchAddress(rs.getString("branch_address"));
			}
			return accountDTO;
		}

		catch (Exception e) {
			log.error("getSingleAccount()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return accountDTO;
	}
	
	public static void updateAccount(Connection conn, AccountDTO accountDTO, String userId) {
		PreparedStatement ps = null;

		try {
			//user_id, bank_name=?, account_number=?, account_type=?, ifsc_code=?, branch_address=?
			String query = "UPDATE account_info SET bank_name=?, account_number=?, account_type=?, ifsc_code=?, branch_address=? WHERE user_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, accountDTO.getBankName());
			ps.setString(2, accountDTO.getAccountNumber());
			ps.setString(3, accountDTO.getAccountType());
			ps.setString(4, accountDTO.getIfscCode());
			ps.setString(5, accountDTO.getBranchAddress());
			ps.setString(6, accountDTO.getUserId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		
	}
	public static void addEditedAccount(Connection conn, AccountDTO accountDTO, String userId) {
		PreparedStatement ps = null;

		try {
						
			String query = "INSERT INTO account_info_edited (user_id, bank_name, account_number, account_type, ifsc_code, branch_address) "
					+ "VALUES (?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, accountDTO.getUserId());
			ps.setString(2, accountDTO.getBankName());
			ps.setString(3, accountDTO.getAccountNumber());
			ps.setString(4, accountDTO.getAccountType());
			ps.setString(5, accountDTO.getIfscCode());
			ps.setString(6, accountDTO.getBranchAddress());
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
