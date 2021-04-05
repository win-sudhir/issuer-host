/**
 * 
 */
package com.winnovature.dispute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.DisputeUploadFileDTO;
import com.winnovature.dto.DisputeUploadFileDTO.DisputeTransactionInfoDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.querries.DisputeQueries;
import com.winnovature.service.TransactionService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtility;
import com.winnovature.utils.MoneyUtility;
import com.winnovature.utils.PropertyReader;
import com.winnovature.validation.DisputeErrorCode;

/*import com.onepay.recon.dto.DisputeMasterDTO;
import com.onepay.recon.dto.DisputeUploadFileDTO;
import com.onepay.recon.dto.DisputeUploadFileDTO.DisputeTransactionInfoDTO;
import com.onepay.recon.dto.ResponseDTO;
import com.onepay.recon.queries.DisputeQueries;
import com.onepay.recon.utils.DatabaseManager;
import com.onepay.recon.utils.DateUtility;
//import com.onepay.recon.utils.GenericUtils;
import com.onepay.recon.utils.PropertyReader;
import com.onepay.recon.utils.ServiceUtil;
import com.onepay.recon.validation.DisputeErrorCode;*/


public class DisputeAdjDAO {

	static Logger log = Logger.getLogger(DisputeAdjDAO.class.getName());

	public ResponseDTO processDisputeFileUpload(DisputeUploadFileDTO disputeUploadFileDTO, Connection conn, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			log.info("processDisputeFileUpload --> ");

			if (!insertDisputeAdjTxns(disputeUploadFileDTO, conn, userId)
					.equalsIgnoreCase(ResponseDTO.success)) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0024.getErrorMessage());
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0024.name());
				return responseDTO;
			} 
		
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0026.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0026.name());
			return responseDTO;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}

	public static String insertDisputeAdjTxns(DisputeUploadFileDTO disputeUploadFileDTO, Connection conn, String userId) 
	{

		int iteration = 0;
		PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null;
		try {
			for (DisputeTransactionInfoDTO txn : disputeUploadFileDTO.getTransactionInfoDTO()) {
				//log.info("Failed to update."+DisputeQueries.insertDisputeAdjustmentQuery);
				pstmt = conn.prepareStatement(DisputeQueries.insertDisputeAdjustmentQuery);
				pstmt.setString(1, txn.getReportDate());
				pstmt.setString(2, txn.getDisputeRaiseDate());
				pstmt.setString(3, txn.getDisputeRaisedSettlementDate());
				pstmt.setString(4, txn.getCaseNumber());
				pstmt.setString(5, txn.getFunctionCode());
				pstmt.setString(6, txn.getFunctionCodeAndDescription());
				pstmt.setString(7, txn.getTransactionSequenceNumber());
				pstmt.setString(8, txn.getTagId());
				pstmt.setString(9, txn.getTid());
				pstmt.setString(10, txn.getTransactionDateAndTime());
				pstmt.setString(11, txn.getReaderReadDateAndTime());
				pstmt.setString(12, txn.getTransactionSettlementDate());
				pstmt.setString(13, txn.getTransactionAmount());
				pstmt.setString(14, txn.getSettlementAmount());
				pstmt.setString(15, txn.getSettlementCurrencyCode());
				pstmt.setString(16, txn.getNote());
				// pstmt.setString(17, txn.getTransaction_id());
				String txnID = txn.getTransactionId();
				if (txnID.startsWith("'")) {
					txnID = txnID.substring(1);
				}
				pstmt.setString(17, txnID);
				pstmt.setString(18, txn.getTransactionType());
				pstmt.setString(19, txn.getMerchantId());
				pstmt.setString(20, txn.getLaneId());
				pstmt.setString(21, txn.getMerchantType());
				pstmt.setString(22, txn.getSubMerchantType());
				pstmt.setString(23, txn.getTransactionStatus());
				pstmt.setString(24, txn.getTagStatus());
				pstmt.setString(25, txn.getAvc());
				pstmt.setString(26, txn.getWim());
				pstmt.setString(27, txn.getOriginatorPoint());
				pstmt.setString(28, txn.getAccquirerId());
				pstmt.setString(29, txn.getTransactionOriginatorInstitutionPid());
				pstmt.setString(30, txn.getAcquirerNameAndCountry());
				pstmt.setString(31, txn.getIin());
				pstmt.setString(32, txn.getTransactionDestinationInstitutionPid());
				pstmt.setString(33, txn.getIssuerNameAndCountry());
				pstmt.setString(34, txn.getVehicleRegistrationNumber());
				pstmt.setString(35, txn.getVehicleClass());
				pstmt.setString(36, txn.getVehicleType());
				pstmt.setString(37, txn.getFinancialNonFinancialIndicator());
				pstmt.setString(38, txn.getDisputeReasonCode());
				pstmt.setString(39, txn.getDisputeReasonCodeDescription());
				pstmt.setString(40, txn.getDisputedAmount());
				pstmt.setString(41, txn.getFullPartialIndicator());
				pstmt.setString(42, txn.getMemberMessageText());
				pstmt.setString(43, txn.getDocumentIndicator());
				pstmt.setString(44, txn.getDocumentAttachedDate());
				pstmt.setString(45, txn.getDeadlineDate());
				pstmt.setString(46, txn.getDaysToAct());
				pstmt.setString(47, txn.getDirectionOfDispute());
				pstmt.executeUpdate();

				if (validateTransactionId(txnID, conn) && validateFileData(conn, txnID, txn)) {
					String respcode = null;
					log.info("found in reqpay and disputemasternpci, belongs to us then insert in recon");
					pstmt1 = conn.prepareStatement(DisputeQueries.insertReconMastertQuery);
					pstmt1.setString(1, txn.getCaseNumber());
					pstmt1.setString(2, DateUtility.getDateFormatInPattern("dd-MM-yyyy HH:mm:ss", txn.getTransactionDateAndTime()));
					pstmt1.setString(3, txn.getTagId());
					pstmt1.setString(4, txnID);
					pstmt1.setString(5, txn.getAccquirerId());
					pstmt1.setString(6, txn.getMerchantId());
					String txnType = txn.getTransactionType();
					pstmt1.setString(7, txnType);
					pstmt1.setString(8, txn.getSettlementAmount());
					
					if (txnType.equalsIgnoreCase(DisputeMasterDTO.DEBIT)) {
						respcode = DisputeMasterDTO.DEBITPAYMODE;
					} else if (txnType.equalsIgnoreCase(DisputeMasterDTO.CREDIT)) {
						respcode = DisputeMasterDTO.CREDITPAYMODE;
					}
					
					pstmt1.setString(9, respcode);
					pstmt1.setString(10, "RS");
					pstmt1.setString(11, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
					pstmt1.executeUpdate();

					pstmt2 = conn.prepareStatement(DisputeQueries.updatedisputeAdjustmentQuery);
					pstmt2.setString(1, "Matched");
					pstmt2.setString(2, txnID);
					pstmt2.executeUpdate();

				
					try {
						String status = callWallet(txn, conn, userId);

						log.info("DisputeAdjDao.java :: callWallet() :: status : " + status);

						if (status.equalsIgnoreCase("1")) 
						{
							updateDisputeMasterStatus(txnID, txn.getFunctionCodeAndDescription(), userId, conn);
						}
						
					} catch (Exception e) {
						log.info("DisputeAdjDao.java :: getting error while calling the Fleet Wallet Trnsaction... for close dispute... ");
						e.printStackTrace();
					}
					////////////////////////////////////////////////////////////////////////////
					// updateDisputeMasterStatus(txnID); update status of dispute_master as close
					log.info("Recon data inserted successfully...");
				} else {
					log.info("INSERT IN dispute_master NPCI");
					updateDipsuteAdjStatus(txnID, conn);// update dispute adj status as matched/unmatched
					// function_code, reason_code, reason_code_type, full_par_indicator
					//log.info("INSERT IN dispute_master NPCI");
					String issuerId = PropertyReader.getPropertyValue("issuerIIN");
					addToDisputeMaster(conn, issuerId, userId, txn);
					// tagId,issuer_id,acquirer_id
				}

				log.info("Dispute transactions are inserted successfully...........");
				iteration++;
			}

			if (iteration > 0) {
				// conn.commit();
				log.info("Transactions are inserted successfully...........");
				return ResponseDTO.success;
			} else {
				return ResponseDTO.failure;
			}

		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {

			DatabaseManager.closePreparedStatement(pstmt);
			DatabaseManager.closePreparedStatement(pstmt1);
			DatabaseManager.closePreparedStatement(pstmt2);
		}

		return ResponseDTO.failure;
	}

	private static String addToDisputeMaster(Connection conn, String issuerId, String userId,
			DisputeTransactionInfoDTO txn) {

		String mmt = null, status = null, is_approved = null;

		PreparedStatement preparedStmt = null;
		try {

			preparedStmt = conn.prepareStatement(DisputeQueries.addDisputeQuery);
			preparedStmt.setString(1, txn.getTagId());
			preparedStmt.setString(2, txn.getFunctionCode());
			//preparedStmt.setString(3, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			preparedStmt.setString(3, DateUtility.getJulianDateOfDate("yyMMddHHmmss"));
			preparedStmt.setString(4, txn.getTransactionId());
			preparedStmt.setString(5, issuerId);
			preparedStmt.setString(6, txn.getAccquirerId());
			//preparedStmt.setString(7, txn.getDisputedAmount() + "00");
			preparedStmt.setString(7, txn.getDisputedAmount());
			preparedStmt.setString(8, txn.getDisputedAmount() + "");
			preparedStmt.setString(9, "3014");
			preparedStmt.setString(10, txn.getMerchantId());
			preparedStmt.setString(11, txn.getFullPartialIndicator());

			if (txn.getFullPartialIndicator().equalsIgnoreCase("F")) {
				mmt = "Chargeback raised full through file";
			} else {
				mmt = "Chargeback raised partial through file";
			}

			preparedStmt.setString(12, txn.getTid());
			preparedStmt.setString(13, mmt);
			preparedStmt.setString(14, txn.getCaseNumber());
			preparedStmt.setString(15, txn.getTransactionType());
			preparedStmt.setString(16, "DISPUTEADJ FILE TXN NOT FOUND");
			preparedStmt.setString(17, "NA");
			preparedStmt.setString(18, "NA");
			preparedStmt.setString(19, "NA");
			preparedStmt.setString(20, "NA");
			preparedStmt.setString(21, "NA");
			preparedStmt.setString(22, "NA");

			status = DisputeMasterDTO.OPENSTATUS;
			is_approved = DisputeMasterDTO.APPROVE;
			
			preparedStmt.setString(23, status);
			preparedStmt.setString(24, DisputeMasterDTO.AUTOMATICPROCESSSTATUS);
			preparedStmt.setString(25, is_approved);
			preparedStmt.setString(26, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			preparedStmt.setString(27, userId);
			preparedStmt.setString(28, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			preparedStmt.setString(29, userId);
			preparedStmt.setString(30, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));

			preparedStmt.executeUpdate();

			log.info("DisputeRaiseDao :: addToDisputeMaster() Dispute transaction saved successfully...");
		} catch (Exception e) {
			log.error("DisputeRaiseDao :: addToDisputeMaster() Exception while saving Dispute transaction :"
					+ e.getMessage());
			e.printStackTrace();

			return ResponseDTO.failure;
		} finally {
			DatabaseManager.closePreparedStatement(preparedStmt);
		}
		return ResponseDTO.success;
	}

	private static String addNegativeBalanceDispute(Connection conn, String issuerId, String userId,
			DisputeTransactionInfoDTO txn, String originalTxnId) {

		String mmt = null;

		PreparedStatement preparedStmt = null;
		try {

			preparedStmt = conn.prepareStatement(DisputeQueries.addDisputeQuery);
			preparedStmt.setString(1, txn.getTagId());
			preparedStmt.setString(2, DisputeMasterDTO.CHBFUNCTIONCODE);
			preparedStmt.setString(3, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			preparedStmt.setString(4, originalTxnId);//txn.getTransactionId());
			preparedStmt.setString(5, issuerId);
			preparedStmt.setString(6, txn.getAccquirerId());
			//preparedStmt.setString(7, txn.getDisputedAmount() + "00");
			preparedStmt.setString(7, txn.getDisputedAmount());
			preparedStmt.setString(8, txn.getDisputedAmount() + "");
			preparedStmt.setString(9, "3014");
			preparedStmt.setString(10, txn.getMerchantId());
			preparedStmt.setString(11, txn.getFullPartialIndicator());

			if (txn.getFullPartialIndicator().equalsIgnoreCase("F")) {
				mmt = "Chargeback raised full through file";
			} else {
				mmt = "Chargeback raised partial through file";
			}

			preparedStmt.setString(12, txn.getTid());
			preparedStmt.setString(13, mmt);
			preparedStmt.setString(14, txn.getCaseNumber());
			preparedStmt.setString(15, DisputeMasterDTO.CREDIT);
			preparedStmt.setString(16, "Current Balance goes in -ve While processing DebitAdj");
			preparedStmt.setString(17, "NA");
			preparedStmt.setString(18, "NA");
			preparedStmt.setString(19, "NA");
			preparedStmt.setString(20, "NA");
			preparedStmt.setString(21, "NA");
			preparedStmt.setString(22, "NA");

			preparedStmt.setString(23, DisputeMasterDTO.OPENSTATUS);
			preparedStmt.setString(24, "AA");
			preparedStmt.setString(25, DisputeMasterDTO.NEW);
			preparedStmt.setString(26, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			preparedStmt.setString(27, userId);
			preparedStmt.setString(28, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			preparedStmt.setString(29, userId);
			preparedStmt.setString(30, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));

			preparedStmt.executeUpdate();

			log.info("DisputeRaiseDao :: addNegativeBalanceDispute() Dispute transaction saved successfully...");
		} catch (Exception e) {
			log.error("DisputeRaiseDao :: addNegativeBalanceDispute() Exception while saving Dispute transaction :"
					+ e.getMessage());

			return ResponseDTO.failure;
		} finally {
			DatabaseManager.closePreparedStatement(preparedStmt);
		}

		return ResponseDTO.success;
	}

	public static boolean validateTransactionId(String originalTxnId, Connection conn) {
		ResultSet rs = null;
		PreparedStatement st = null;
		String sql = DisputeQueries.validateTxnIdQuery; 
				// validateTxnIdQuery = "SELECT original_txn_id FROM req_pay_master where original_txn_id = ?";
		try {

			st = conn.prepareStatement(sql);
			st.setString(1, originalTxnId);
			rs = st.executeQuery();
			if (rs.next()) {
				return true;
			} 
		} catch (Exception e) {
			log.error("Error while checking adj records in DisputeAdjDao.java" + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(st);
		}
		return false;
	}
	
	private static boolean validateFileData(Connection conn, String acqTxnId, DisputeTransactionInfoDTO txn) {
		ResultSet rs = null;
		PreparedStatement st = null;
		log.info("DisputeAdjDao.java :: validateFileData()  :: acqtxnId : " + acqTxnId + " , acquirerId : "
				+ txn.getAccquirerId() + " , merchantId(TollPlazaId) : " + txn.getMerchantId() + " FunctionCode :"
				+ txn.getFunctionCode() + " Transaction Amount : " + txn.getTransactionAmount());

		String sql = DisputeQueries.validateFileDataQuery; 
				//validateFileDataQuery = "SELECT acq_txn_id,acquirer_id,toll_plaza_id FROM dispute_master_npci where acq_txn_id=? and acquirer_id=? and toll_plaza_id=? and function_code=? and txn_amount=? and status='Open' and is_approved=1";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, acqTxnId);
			st.setString(2, txn.getAccquirerId());
			st.setString(3, txn.getMerchantId());
			st.setString(4, txn.getFunctionCode());
			//st.setString(5, txn.getTransactionAmount() + "00");
			st.setString(5, txn.getTransactionAmount());
			st.setString(6, DisputeMasterDTO.OPENSTATUS);
			st.setString(7, DisputeMasterDTO.APPROVE);
			rs = st.executeQuery();
			if (rs.next()) {
				log.info("File Data validation found : " + rs.getString("acq_txn_id"));
				return true;
			} 
		} catch (Exception e) {
			log.error("Error while checking dispute_master_npci records" + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(st);
		}
		return false;
	}

	public static void updateDipsuteAdjStatus(String txn_id, Connection conn) {
		PreparedStatement preparedStmt = null;
		String sql = DisputeQueries.updateAdjustmentStatusQuery;
		try {
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, "Txn mismatch with reqpay");
			preparedStmt.setString(2, txn_id);
			preparedStmt.executeUpdate();
			log.info("Dispute adj status updated Successfully .........");
		}

		catch (Exception e) {
			log.error("Something wrong while updating ..." + e.getMessage());
			e.printStackTrace();
		} finally {
				DatabaseManager.closePreparedStatement(preparedStmt);
		}
	}

	// public static void updateDisputeMasterStatus(String txn_id, String
	// disputeFileTxnAmount, String closingStatus, String userId)
	private static void updateDisputeMasterStatus(String acqTxnId, String closingStatus, String userId, Connection conn) {

		PreparedStatement preparedStmt = null;

		String sql = DisputeQueries.updateDisputeMasterStatusQuery; 
				//"UPDATE dispute_master_npci set status = ?, rodt = ? ,closing_status=?, last_updated_by=?, last_updated_on=? where acq_txn_id = ? "; // where
		try {
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, DisputeMasterDTO.CLOSESTATUS);
			preparedStmt.setString(2, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			preparedStmt.setString(3, closingStatus);
			preparedStmt.setString(4, userId);
			preparedStmt.setString(5, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			preparedStmt.setString(6, acqTxnId);
			preparedStmt.executeUpdate();
			log.info("dispute_master_npci status updated Successfully .........");
		} catch (Exception e) {
			log.error("Something wrong while updating dispute_master_npci..." + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(preparedStmt);
		}
	}

	public static String callWallet(DisputeTransactionInfoDTO txn, Connection conn, String userId) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String txnID = txn.getTransactionId();
		if (txnID.startsWith("'")) {
			txnID = txnID.substring(1);
		}

		log.info("DisputeAdjDao.java ::  Data Fatch :: from request pay master where original_txn_id : '" + txnID + "'");

		String sql = DisputeQueries.getWalletQuery;

		try {
			log.info("QUERY :: " + sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, txnID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				log.info("<<<<<RESULTSET FOUND>>>>>");
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
				
			}
		} catch (Exception e) {

			log.error("DisputeAdjDao.java :: Exception in insert to recon while closing individual dispute :: "
					+ e.getMessage());
			e.printStackTrace();
			return "0";
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}

		return "0";
	}

	private static boolean isAcquirerLiabilityTxn(String originalTxnId, Connection conn) {
		ResultSet rs = null;
		PreparedStatement st = null;
		String sql = DisputeQueries.getCheckAcqLiabilityQuery; 
				// validateTxnIdQuery = "SELECT original_txn_id FROM req_pay_master where original_txn_id = ?";
		try {

			st = conn.prepareStatement(sql);
			st.setString(1, originalTxnId);
			st.setString(2, DisputeMasterDTO.ACQLIBILITY);// "051");
			rs = st.executeQuery();
			if (rs.next()) {
				return true;
			} 
		} catch (Exception e) {
			log.error("Error while checking acqLiability records in DisputeAdjDao.java" + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(st);
		}
		return false;
	}
	
}
