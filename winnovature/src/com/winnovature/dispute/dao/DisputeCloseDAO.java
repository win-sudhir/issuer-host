/**
 * 
 */
package com.winnovature.dispute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.querries.DisputeQueries;
import com.winnovature.service.TransactionService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtility;
import com.winnovature.utils.MoneyUtility;
import com.winnovature.validation.DisputeErrorCode;


public class DisputeCloseDAO {
	static Logger log = Logger.getLogger(DisputeCloseDAO.class.getClass());

	public ResponseDTO closeDispute(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			// validation
			if (disputeMasterDTO.getId() == null || disputeMasterDTO.getId().isEmpty()) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
				return responseDTO;
			}
			if (disputeMasterDTO.getAcqTxnId() == null || disputeMasterDTO.getAcqTxnId().isEmpty()) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
				return responseDTO;
			}
			if (disputeMasterDTO.getRequestBy() == null || disputeMasterDTO.getRequestBy().isEmpty()) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
				return responseDTO;
			}
			if (!isValidAcqId(disputeMasterDTO.getId(), disputeMasterDTO.getAcqTxnId(), conn)) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0015.getErrorMessage());
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0015.name());
				return responseDTO;
			}
			String response = callWalletTxn(disputeMasterDTO.getAcqTxnId(), conn, disputeMasterDTO.getRequestBy());
			if (response.equals("1")) {
				log.info("---ID :: " + disputeMasterDTO.getId());
				updateDisputeToClose(disputeMasterDTO.getId(), disputeMasterDTO.getRequestBy(), conn);
				responseDTO.setStatus(ResponseDTO.success);
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0016.getErrorMessage());
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0016.name());
				return responseDTO;
			}
			else{
				responseDTO.setStatus(ResponseDTO.failure);
				//responseDTO.setMessage(resp.getString("resp_message"));
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0017.getErrorMessage());
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0017.name());
				return responseDTO;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}

	private String updateDisputeToClose(String id, String userId, Connection conn) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getCloseDisputeQuery);
			pstmt.setString(1, DisputeMasterDTO.CLOSESTATUS);
			pstmt.setString(2, userId);
			pstmt.setString(3, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			pstmt.setString(4, id);
			int i = pstmt.executeUpdate();
			log.info(i);
			if(i>0){
				return ResponseDTO.success;
			}
			else{
				return ResponseDTO.failure;
			}
						
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return ResponseDTO.failure;
	}
	
	private static boolean isValidAcqId(String id, String acqTxnId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = DisputeQueries.getCheckAcqTxnIdExistQuery;
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, acqTxnId);
			ps.setString(3, DisputeMasterDTO.OPENSTATUS);
			ps.setString(4, DisputeMasterDTO.APPROVE);
			rs = ps.executeQuery();
			if (rs.next()) {
					return true;
				} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}
	
	private String callWalletTxn(String originalTxnId, Connection conn, String userId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getWalletQuery);
			pstmt.setString(1, originalTxnId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				TransactionDTO transactionDTO = new TransactionDTO();
				transactionDTO.setWalletId(rs.getString("wallet_id"));
				transactionDTO.setPayMode(DisputeMasterDTO.CREDITPAYMODE);
				if(rs.getString("txn_type").equalsIgnoreCase("DEBIT")) {
					transactionDTO.setPayMode(DisputeMasterDTO.DEBITPAYMODE);
				}
				transactionDTO.setTxnType(rs.getString("txn_type"));
				transactionDTO.setPartnerRefId(rs.getString("txn_id"));
				transactionDTO.setSecurityDeposit("0");
				transactionDTO.setPartnerId(rs.getString("toll_plaza_id"));
				transactionDTO.setRemarks("DISPUTE ADJUSTMENT FOR TXNID "+rs.getString("txn_id"));
				String txnAmount = MoneyUtility.getRupeesFromPaisa(rs.getString("txn_amount"));
				transactionDTO.setTxnAmount(txnAmount);
				ResponseDTO responseDTO = TransactionService.doTransaction(conn, transactionDTO, userId);
				return responseDTO.getStatus();
				/*
				transactionDTO.setUDF1("NA");
				transactionDTO.setUDF2("NA");
				transactionDTO.setUDF3("NA");
				transactionDTO.setUDF4("NA");
				transactionDTO.setUDF5("NA");
				transactionDTO.setUDF6("NA");
				transactionDTO.setUDF7("NA");
				transactionDTO.setUDF8("NA");
				*/
				
			}
				

		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return "0";
	}

}
