package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.winnovature.contants.WINConstants;
import com.winnovature.dto.WalletDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class WalletDAO {
	static Logger log = Logger.getLogger(WalletDAO.class.getName());
	public static void createLTDKYCWallet(Connection conn, String userId, String walletId) {
		PreparedStatement ps = null;
		try {
			String currentDate = new DateUtils().getCurrnetDate();
			//String walletId = new IDGenerator().getWalletId();
			String query = "INSERT INTO wallet_info (wallet_id, current_balance, max_balance, min_balance, sec_deposit, min_sec_deposit, kyc_type, valid_upto, status, created_by, created_on)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			ps = conn.prepareStatement(query);
			ps.setString(1, walletId);
			ps.setDouble(2, WalletDTO.CURRENTBALANCE);
			ps.setDouble(3, WalletDTO.LDTMAXBALANCE);
			ps.setDouble(4, WalletDTO.MINBALANCE);
			ps.setDouble(5, WalletDTO.SECURITY);
			ps.setDouble(6, WalletDTO.MINSECURITY);
			ps.setString(7, WalletDTO.LTDKYC);
			ps.setString(8, DateUtils.getValidUptoDate(currentDate));
			ps.setString(9, WINConstants.ACTIVE);
			ps.setString(10, userId);
			ps.setString(11, currentDate);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	
	public void createWalletKYC(Connection conn, String walletId, String userId) {
		PreparedStatement ps = null;
		try {
			
			String query = "UPDATE wallet_info SET max_balance=?, kyc_type=?, modified_by=?, modified_on=? WHERE wallet_id=?";

			ps = conn.prepareStatement(query);
			ps.setDouble(1, WalletDTO.MAXBALANCE);
			ps.setString(2, WalletDTO.FULLKYC);
			ps.setString(3, userId);
			ps.setString(4, new DateUtils().getCurrnetDate());
			ps.setString(5, walletId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
			  
}
