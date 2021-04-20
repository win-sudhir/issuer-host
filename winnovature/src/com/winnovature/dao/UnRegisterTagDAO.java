package com.winnovature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagAllocationDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.utils.DatabaseManager;

public class UnRegisterTagDAO {
	static Logger log = Logger.getLogger(UnRegisterTagDAO.class.getName());

	public static TagAllocationDTO isUnRegisterTag(String tid, String vehicleNumber, Connection conn) {
		log.info("isUnRegisterTag.....");
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT u.id, u.txn_id, v.tag_id,u.amount FROM vehicle_tag_linking v, tbl_unreg_acqliablity_master u WHERE v.tag_id=u.tag_id AND v.tid=? AND u.txn_type=? AND u.status=?";
		TagAllocationDTO tagAllocationDTO = new TagAllocationDTO();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, tid);
			ps.setString(2, "UNREG");
			ps.setString(3, "0");
			rs = ps.executeQuery();
			tagAllocationDTO.setUnRegister(false);
			if (rs.next()) {
				tagAllocationDTO.setUnRegister(true);
				tagAllocationDTO.setId(rs.getString("id"));
				tagAllocationDTO.setTxnId(rs.getString("txn_id"));
				tagAllocationDTO.setTagId(rs.getString("tag_id"));
				tagAllocationDTO.setUnRegTxnAmount(rs.getString("amount"));
				tagAllocationDTO.setTID(tid);
				tagAllocationDTO.setVehicleNumber(vehicleNumber);
				return tagAllocationDTO;
			}

		} catch (Exception e) {
			log.info("Error while checking isUnRegisterTag : " + e.getMessage());
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return tagAllocationDTO;
	}

	public static ResponseDTO unRegisterTagTransaction(TransactionDTO transactionDTO, String userId, Connection conn) {
		log.info("unRegisterTagTransaction => pr_unregtxn.....");
		ResponseDTO responseDTO = new ResponseDTO();
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall("{CALL pr_unregtxn(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			cs.setString(1, transactionDTO.getWalletId());
			cs.setString(2, transactionDTO.getTxnType());
			cs.setString(3, transactionDTO.getTxnAmount());
			cs.setString(4, transactionDTO.getSecurityDeposit());
			cs.setString(5, transactionDTO.getRemarks());
			cs.setString(6, transactionDTO.getPayMode());
			cs.setString(7, transactionDTO.getPartnerId());
			cs.setString(8, transactionDTO.getPartnerRefId());
			cs.setString(9, transactionDTO.getUDF1());
			cs.setString(10, transactionDTO.getUDF2());
			cs.setString(11, transactionDTO.getUDF3());
			cs.setString(12, transactionDTO.getUDF4());
			cs.setString(13, transactionDTO.getUDF5());
			cs.setString(14, transactionDTO.getUDF6());
			cs.setString(15, transactionDTO.getUDF7());
			cs.setString(16, transactionDTO.getUDF8());
			cs.setString(17, userId);
			cs.setString(18, TransactionDTO.SOURCECHANNEL);
			cs.setString(19, TransactionDTO.SOURCECHANNELIP);
			cs.registerOutParameter(20, Types.VARCHAR);
			cs.registerOutParameter(21, Types.VARCHAR);
			cs.registerOutParameter(22, Types.VARCHAR);
			cs.execute();
			responseDTO.setStatus(cs.getString(20));
			responseDTO.setMessage(cs.getString(21));
			log.info("unRegisterTagTransaction Status : "+responseDTO.getStatus()+" Message : "+responseDTO.getMessage());
		} catch (Exception e) {
			log.error("Exception in WalletTransactionDAO.unRegisterTagTransaction() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			// DatabaseManager.closeConnection(conn);
		}

		return responseDTO;
	}

	public static boolean updateUnRegisterTagStatus(TagAllocationDTO tagAllocationDTO, Connection conn) {
		log.info("updateUnRegisterTagStatus.....");
		PreparedStatement ps = null;
		String sql = "UPDATE tbl_unreg_acqliablity_master SET status = ?, closing_date=now() WHERE id=? AND txn_id=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, "1");
			ps.setString(2, tagAllocationDTO.getId());
			ps.setString(3, tagAllocationDTO.getTxnId());
			if (ps.executeUpdate() > 0)
				;
			return true;
		} catch (Exception e) {
			log.info("Error while checking updateUnRegisterTagStatus : " + e.getMessage());
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}
}
