package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.winnovature.contants.WINConstants;
import com.winnovature.dto.VirtualAccountDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class VirtualAccountDAO {
	static Logger log = Logger.getLogger(VirtualAccountDAO.class.getName());
	
	public static void createVirtualAccount(Connection conn, VirtualAccountDTO virtualAccountDTO) {
		PreparedStatement ps = null;
		try {
			String currentDate = new DateUtils().getCurrnetDate();
			//user_id, virtual_account_no, current_balance, max_balance, min_balance, status, created_by, created_on, last_updated_by, last_updated_on
			String query = "INSERT INTO virtual_account_info (user_id, virtual_account_no, current_balance, max_balance, min_balance, status, created_by, created_on)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?)";

			ps = conn.prepareStatement(query);
			ps.setString(1, virtualAccountDTO.getUserId());
			ps.setString(2, virtualAccountDTO.getVirtualAccountNo());
			ps.setDouble(3, VirtualAccountDTO.CURRENTBALANCE);
			ps.setDouble(4, VirtualAccountDTO.MAXBALANCE);
			ps.setDouble(5, VirtualAccountDTO.MINBALANCE);
			ps.setString(6, WINConstants.ACTIVE);
			ps.setString(7, virtualAccountDTO.getCreatedBy());
			ps.setString(8, currentDate);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	
	public boolean checkVirtualAccountBalance(Connection conn, String virtualAccountNumber, String userId, double txnAmount) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			String query = "SELECT virtual_account_no, current_balance, max_balance, min_balance virtual_account_info WHERE user_id=?";

			ps = conn.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if(rs.next()) {
				double balance = rs.getDouble("current_balance")-txnAmount;
				if(balance>=rs.getDouble("min_balance"))
				{
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}
			  
}
