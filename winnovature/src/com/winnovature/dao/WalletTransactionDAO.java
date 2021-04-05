package com.winnovature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.utils.DatabaseManager;

public class WalletTransactionDAO {
	static Logger log = Logger.getLogger(WalletTransactionDAO.class.getName());
	
	public static ResponseDTO validateLimit(TransactionDTO transactionDTO){
		ResponseDTO responseDTO = new ResponseDTO();
		return responseDTO;
	}

	public static ResponseDTO walletTransaction(TransactionDTO transactionDTO, String userId, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall("{CALL pr_wallet_transaction(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			
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
			} catch (Exception e) {
				log.error("Exception in WalletTransactionDAO.walletTransaction() :: " + e.getMessage());
				log.error(e);
				e.printStackTrace();
			} finally {
				DatabaseManager.closeCallableStatement(cs);
				//DatabaseManager.closeConnection(conn);
			}
		
		return responseDTO;
	}
	
	public static ResponseDTO tagAllocattionTransaction(TransactionDTO transactionDTO, String userId, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall("{CALL pr_tag_transaction(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			
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
			} catch (Exception e) {
				log.error("Exception in WalletTransactionDAO.walletTransaction() :: " + e.getMessage());
				log.error(e);
				e.printStackTrace();
			} finally {
				DatabaseManager.closeCallableStatement(cs);
				//DatabaseManager.closeConnection(conn);
			}
		
		return responseDTO;
	}
}
