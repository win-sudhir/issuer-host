/**
 * 
 */
package com.winnovature.dispute.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.querries.DisputeQueries;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtility;
import com.winnovature.utils.MoneyUtility;
import com.winnovature.utils.PropertyReader;

public class DisputeSearchDAOImpl implements DisputeSearchDAO {
	Logger log = Logger.getLogger(DisputeSearchDAOImpl.class.getClass());

	@Override
	public List<DisputeMasterDTO> getDisputeTxnsByDate(String fromDate, String toDate, String refNo) {

		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		String sql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO master = new DisputeMasterDTO();
				lst.add(master);
			}
			return lst;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			DatabaseManager.closeConnection(conn);
		}

		return lst;
	}

	@Override
	public List<DisputeMasterDTO> getApprovedTransactionsByDate(String fromDate, String toDate) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		String sql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO master = new DisputeMasterDTO();
				lst.add(master);
			}
			return lst;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			DatabaseManager.closeConnection(conn);
		}

		return lst;
	}

	@Override
	public List<DisputeMasterDTO> getRejectedTransactionsByDate(String fromDate, String toDate) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		String sql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO master = new DisputeMasterDTO();
				lst.add(master);
			}
			return lst;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			DatabaseManager.closeConnection(conn);
		}

		return lst;
	}

	// write queries and method
	@Override
	public List<DisputeMasterDTO> getTransactionsByFilter(DisputeMasterDTO disputeMasterDTO, Connection conn,
			String userId) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		String sql = null;// DisputeQueries.getTxnsByDateToRaiseDispute;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// conn = DatabaseManager.getConnection();
			log.info("---------------------------------------------------");
			if ("date".equalsIgnoreCase(disputeMasterDTO.getFilter())) {

				if ("admin".equalsIgnoreCase(userId)) {
					sql = DisputeQueries.getTxnsByDateToRaiseDisputeByAdmin;
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, disputeMasterDTO.getFromDate() + " 00:00:00");
					pstmt.setString(2, disputeMasterDTO.getToDate() + " 23:59:59");
				} else {
					sql = DisputeQueries.getTxnsByDateToRaiseDispute;
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userId);
					pstmt.setString(2, disputeMasterDTO.getFromDate() + " 00:00:00");
					pstmt.setString(3, disputeMasterDTO.getToDate() + " 23:59:59");
				}

			} else if ("vehicle".equalsIgnoreCase(disputeMasterDTO.getFilter())) {
				sql = DisputeQueries.getTxnsByVehicleNoToRaiseDispute;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, disputeMasterDTO.getFilterValue());
			} else if ("originalTxnId".equalsIgnoreCase(disputeMasterDTO.getFilter())) {
				sql = DisputeQueries.getTxnsByOriginalTxnIdToRaiseDispute;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, disputeMasterDTO.getFilterValue());
			} else if ("walletId".equalsIgnoreCase(disputeMasterDTO.getFilter())) {
				sql = DisputeQueries.getTxnsByWalletIdToRaiseDispute;
				pstmt = conn.prepareStatement(sql);
				String customerId = getCusotmerIdUsingWalletOrMobile(disputeMasterDTO.getFilterValue(), conn);
				pstmt.setString(1, customerId);
			} else if ("mobileNo".equalsIgnoreCase(disputeMasterDTO.getFilter())) {
				sql = DisputeQueries.getTxnsByMobileNoToRaiseDispute;
				pstmt = conn.prepareStatement(sql);
				String customerId = getCusotmerIdUsingWalletOrMobile(disputeMasterDTO.getFilterValue(), conn);
				pstmt.setString(1, customerId);
			}
			log.info("------- Query --------" + pstmt.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO master = new DisputeMasterDTO();
				// txn_time, txn_id, original_txn_id, tag_id, tid, txn_amount, txn_type,
				// vehicle_no
				master.setTxnTime(rs.getString("txn_time"));
				master.setTxnId(rs.getString("txn_id"));
				master.setOriginalTxnId(rs.getString("original_txn_id"));
				master.setTagId(rs.getString("tag_id"));
				master.setTid(rs.getString("tid"));
				master.setTxnAmount(MoneyUtility.getRupeesToRupees(rs.getString("txn_amount")));
				master.setTxnType(rs.getString("txn_type"));
				master.setVehicleNumber(rs.getString("vehicle_no"));
				lst.add(master);
			}
			return lst;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}

		return lst;
	}

	@Override
	public List<DisputeMasterDTO> getApprovalTransactionsByDate(String fromDate, String toDate) {
		// TODO Auto-generated method stub
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		String sql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO master = new DisputeMasterDTO();

				lst.add(master);
			}
			return lst;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			DatabaseManager.closeConnection(conn);
		}

		return lst;
	}

	public Map<String, String> callWallet(String issuerId, String amount) {
		String sql = "select cust.cust_id as custId, req.id as partnerRefNo,req.txn_id as issuerTxnId,dmn.txn_type as ttype, dmn.dispute_desc, dmn.rodt as txnDateTime,req.message_identifier as msgId,req.original_txn_id as orgTxnId,"
				+ " cust.contact_number as mobile,acc.acc_no as accNo,acc.iswallet as isWallet,veh.tag_id as tagId,veh.tid as transId,veh.vehicle_number as vehNo,veh.barcode_data as barcode,veh.tag_class_id as tagClassId,"
				+ " req.toll_plaza_id as tollPlazaId,req.toll_plaza_name as tollPlazaName,req.toll_geo_code as tollGeoCode,req.org_id as orgId"
				+ " from req_pay_master req inner join vehicle_tag_linking veh on req.tag_id = veh.tag_id"
				+ " inner join customer_master cust on veh.customer_id = cust.cust_id"
				+ " inner join account_master acc on veh.customer_id = acc.user_id"
				+ " inner join dispute_master_npci dmn on req.original_txn_id = dmn.acq_txn_id"
				+ " where req.original_txn_id = ? ";

		Connection conn = null;
		PreparedStatement pstmt = null;
		Map<String, String> post = new HashMap<String, String>();
		try {
			conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, issuerId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				post.put("mobileNo", rs.getString("mobile"));
				post.put("userId", "OneMove");
				post.put("walletId", rs.getString("accNo"));
				post.put("amount", amount);

				post.put("remark", "NETC Txn Issuer Id : " + rs.getString("partnerRefNo"));
				post.put("ttype", rs.getString("ttype"));
				post.put("partnerId", "00123");

				if (rs.getString("ttype").equalsIgnoreCase("DEBIT")) {
					post.put("partnerRefNo", rs.getString("issuerTxnId"));
					post.put("paymode", "DRADJ"); // rs.getString("dispute_desc");
				} else {
					post.put("partnerRefNo", rs.getString("issuerTxnId"));
					post.put("paymode", "CRADJ"); // rs.getString("dispute_desc");
				}
				post.put("productId", "DEFAULT");

				post.put("UDF1", "CloseDispute"); // TOLLTXN
				post.put("UDF2", rs.getString("tollGeoCode"));
				post.put("UDF3", rs.getString("barcode"));
				post.put("UDF4", rs.getString("tollPlazaName"));
				post.put("UDF5", rs.getString("vehNo"));

				post.put("customer_id", rs.getString("custId"));
				post.put("date_time", rs.getString("txnDateTime"));

				post.put("errorCode", "000"); // Check Validation in Fleet

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			DatabaseManager.closeConnection(conn);
		}
		return post;
	}

	@Override
	public String approveDispute(String id, String userId, Connection conn) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getApproveDisputeQuery);
			// log.info("Querry for approveDispute ::
			// "+DisputeQueries.getApproveDisputeQuery);
			pstmt.setString(1, DisputeMasterDTO.APPROVE);
			pstmt.setString(2, userId);
			pstmt.setString(3, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			pstmt.setString(4, id);
			int i = pstmt.executeUpdate();
			log.info(i);
			if (i > 0) {
				return ResponseDTO.success;
			} else {
				return ResponseDTO.failure;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return ResponseDTO.failure;
	}

	@Override
	public String rejectDisputeTransaction(String id, String remark, String userId, Connection conn) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getRejectDisputeQuery);
			pstmt.setString(1, DisputeMasterDTO.REJECT);
			pstmt.setString(2, remark);
			pstmt.setString(3, userId);
			pstmt.setString(4, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			pstmt.setString(5, id);
			int i = pstmt.executeUpdate();
			if (i > 0) {
				return ResponseDTO.success;
			} else {
				return ResponseDTO.failure;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return ResponseDTO.failure;
	}
	/*
	 * @Override public String updateDisputeTransaction(String id, int amount,
	 * String function_code, String reason_code, String reason_code_type, String
	 * full_par_indicator, String dispute_desc, String evidence_doc, String mmt,
	 * String filePath1, String filePath2, String filePath3, String filePath4,
	 * String filePath5, String userId) { // TODO Auto-generated method stub String
	 * sql = ""; Connection conn = null; PreparedStatement pstmt = null;
	 * 
	 * try { conn = DatabaseManager.getConnection(); pstmt =
	 * conn.prepareStatement(sql);
	 * 
	 * 
	 * return "success";
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); } finally {
	 * DatabaseManager.closePreparedStatement(pstmt);
	 * DatabaseManager.closeConnection(conn); } return "failed"; }
	 */

	private String getCusotmerIdUsingWalletOrMobile(String searchByValue, Connection conn) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String sql = "select user_id from tbl_wallet_master where (wallet_id=? or contact_number=?) and status_id in(0,1)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, searchByValue);
			ps.setString(2, searchByValue);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("user_id");
			}
		} catch (Exception e) {
			log.error("Exception while getting cust_id by walletId/mobileNo : " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return "";
	}

	@Override
	public List<DisputeMasterDTO> getOneDayBeforeTransactions(String originalTxnId, Connection conn) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CallableStatement cs = null;
		log.info("originalTxnId ::: " + originalTxnId);
		try {
			cs = conn.prepareCall(DisputeQueries.getDateProcQuery);
			cs.setString(1, originalTxnId);
			cs.registerOutParameter(2, java.sql.Types.DATE);
			cs.registerOutParameter(3, java.sql.Types.DATE);
			cs.registerOutParameter(4, java.sql.Types.VARCHAR);// tagId
			cs.execute();

			String fromDate = cs.getString(2);
			String toDate = cs.getString(3);
			String tagId = cs.getString(4);
			log.info("-----TagId----- " + tagId);
			log.info("fromDate :: " + fromDate);
			log.info("toDate :: " + toDate);

			pstmt = conn.prepareStatement(DisputeQueries.getOneDayBeforeTxnListQuery);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, toDate);
			pstmt.setString(3, tagId);
			pstmt.setString(4, originalTxnId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO disputeMasterDTO = new DisputeMasterDTO();
				disputeMasterDTO.setVehicleNumber(rs.getString("vehicle_no"));
				disputeMasterDTO.setTxnId(rs.getString("txn_id"));
				disputeMasterDTO.setOriginalTxnId(rs.getString("original_txn_id"));
				disputeMasterDTO.setTxnAmount(MoneyUtility.getRupeesToRupees(rs.getString("txn_amount")));
				disputeMasterDTO.setTxnTime(rs.getString("txn_time"));
				disputeMasterDTO.setTollPlazaId(rs.getString("toll_plaza_id"));
				disputeMasterDTO.setTollPlazaName(rs.getString("toll_plaza_name"));
				disputeMasterDTO.setReaderTimeStamp(rs.getString("reader_ts"));
				lst.add(disputeMasterDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getOneDayBeforeListOfTransactions()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}

	@Override
	public List<DisputeMasterDTO> getNewTransactionsToApprove(DisputeMasterDTO dto, Connection conn) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {

			pstmt = conn.prepareStatement(DisputeQueries.getNewDisputeTxnListQuery);
			pstmt.setString(1, DisputeMasterDTO.OPENSTATUS);
			pstmt.setString(2, DisputeMasterDTO.NEW);
			pstmt.setString(3, dto.getFromDate() + " 00:00:00");
			pstmt.setString(4, dto.getToDate() + " 23:59:59");

			rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO disputeMasterDTO = new DisputeMasterDTO();
				// id, tag_id, function_code, txn_time, acq_txn_id, issuer_id, acquirer_id,
				// txn_amount, actual_txn_amount, reason_code, toll_plaza_id,
				// full_partial_indicator, tid, mmt, toll_txn_id, txn_type, dispute_desc,
				// created_on, created_by
				disputeMasterDTO.setId(rs.getString("id"));
				disputeMasterDTO.setTagId(rs.getString("tag_id"));
				disputeMasterDTO.setFunctionCode(rs.getString("function_code"));
				disputeMasterDTO.setOriginalTxnId(rs.getString("acq_txn_id"));
				disputeMasterDTO.setIssuerId(rs.getString("issuer_id"));
				disputeMasterDTO.setAcquirerId(rs.getString("acquirer_id"));
				// disputeMasterDTO.setTxnAmount(MoneyUtility.getRupeesFromPaisa(rs.getString("txn_amount")));
				disputeMasterDTO.setTxnAmount(rs.getString("txn_amount"));
				disputeMasterDTO.setActualTxnAmount(rs.getString("actual_txn_amount"));
				disputeMasterDTO.setReasonCode(rs.getString("reason_code"));
				disputeMasterDTO.setTollPlazaId(rs.getString("toll_plaza_id"));
				disputeMasterDTO.setFullPartialIndicator(rs.getString("full_partial_indicator"));
				disputeMasterDTO.setTid(rs.getString("tid"));
				disputeMasterDTO.setMmt(rs.getString("mmt"));
				disputeMasterDTO.setReferenceId(rs.getString("toll_txn_id"));
				disputeMasterDTO.setTxnType(rs.getString("txn_type"));
				disputeMasterDTO.setComment(rs.getString("dispute_desc"));
				disputeMasterDTO.setTxnTime(DateUtility.getNewFormattedDate("yyyy-MM-dd HH:mm:ss", "yyMMddHHmmss",
						rs.getString("txn_time")));
				disputeMasterDTO.setCreatedBy(rs.getString("created_by"));
				disputeMasterDTO.setCreatedOn(rs.getString("created_on"));

				lst.add(disputeMasterDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getNewTransactionsToApprove()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}

	@Override
	public List<DisputeMasterDTO> getTransactionToUpdateById(String id, Connection conn) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getDisputeTxnByIdQuery);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				// function_code, txn_amount, reason_code, full_partial_indicator, dispute_desc,
				// acq_txn_id, mmt
				DisputeMasterDTO disputeMasterDTO = new DisputeMasterDTO();
				disputeMasterDTO.setFunctionCode(rs.getString("function_code"));
				disputeMasterDTO.setReasonCode(rs.getString("reason_code"));
				disputeMasterDTO.setOriginalTxnId(rs.getString("acq_txn_id"));
				// disputeMasterDTO.setTxnAmount(MoneyUtility.getRupeesFromPaisa(rs.getString("txn_amount")));
				disputeMasterDTO.setTxnAmount(rs.getString("txn_amount"));
				disputeMasterDTO.setFullPartialIndicator(rs.getString("full_partial_indicator"));
				disputeMasterDTO.setComment(rs.getString("dispute_desc"));
				disputeMasterDTO.setMmt(rs.getString("mmt"));
				lst.add(disputeMasterDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getTransactionToUpdateById()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;

	}

	@Override
	public List<DisputeMasterDTO> getTransactionDetails(String originalTxnId, Connection conn) {

		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getTransactionDetailsQuery);
			pstmt.setString(1, originalTxnId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// txn_id, txn_amount, toll_plaza_id, toll_plaza_name, plaza_reader_id,
				// toll_plaza_type, reader_ts
				DisputeMasterDTO disputeMasterDTO = new DisputeMasterDTO();
				disputeMasterDTO.setTxnId(rs.getString("txn_id"));
				// disputeMasterDTO.setOriginalTxnId(rs.getString("original_txn_id"));
				disputeMasterDTO.setTxnAmount(MoneyUtility.getRupeesToRupees(rs.getString("txn_amount")));
				disputeMasterDTO.setTollPlazaId(rs.getString("toll_plaza_id"));
				disputeMasterDTO.setTollPlazaName(rs.getString("toll_plaza_name"));
				disputeMasterDTO.setReaderTimeStamp(rs.getString("reader_ts"));
				disputeMasterDTO.setPlazaReaderId(rs.getString("plaza_reader_id"));
				disputeMasterDTO.setTollPlazaType(rs.getString("toll_plaza_type"));
				lst.add(disputeMasterDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getOneDayBeforeListOfTransactions()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}

	/*
	 * @Override public String updateDisputeTransaction(String id, int amount,
	 * String function_code, String reason_code, String reason_code_type, String
	 * full_par_indicator, String dispute_desc, String filePath1, String mmt, String
	 * filePath2, String filePath3, String filePath4, String filePath5, String
	 * filePath6, String userId) { // TODO Auto-generated method stub return null; }
	 */

	@Override
	public String updateDisputeTransaction(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getUpdateDisputeQuery);
			// txn_amount=?, reason_code=?, full_partial_indicator=?, mmt=?, dispute_desc=?,
			// evidence_doc=?, evidence_doc1=?, evidence_doc2=?,
			// evidence_doc3=?, evidence_doc4=?, evidence_doc5=?, last_updated_by=?,
			// last_updated_on=? where id=?
			disputeMasterDTO = new DisputeRaiseDAO().addDisputeDocumentNMT(disputeMasterDTO);
			// pstmt.setString(1,
			// MoneyUtility.getPaiseFromRupees(disputeMasterDTO.getTxnAmount()));
			pstmt.setString(1, disputeMasterDTO.getTxnAmount());
			pstmt.setString(2, disputeMasterDTO.getReasonCode());
			pstmt.setString(3, disputeMasterDTO.getFullPartialIndicator());
			pstmt.setString(4, disputeMasterDTO.getMmt());
			pstmt.setString(5, disputeMasterDTO.getComment());
			/*
			 * pstmt.setString(6, disputeMasterDTO.getEvidenceDoc1()); pstmt.setString(7,
			 * disputeMasterDTO.getEvidenceDoc2()); pstmt.setString(8,
			 * disputeMasterDTO.getEvidenceDoc3()); pstmt.setString(9,
			 * disputeMasterDTO.getEvidenceDoc4()); pstmt.setString(10,
			 * disputeMasterDTO.getEvidenceDoc5()); pstmt.setString(11,
			 * disputeMasterDTO.getEvidenceDoc6());
			 */
			pstmt.setString(6, disputeMasterDTO.getRequestBy());
			pstmt.setString(7, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			pstmt.setString(8, disputeMasterDTO.getId());

			int i = 0;
			i = pstmt.executeUpdate();
			updateDoc(disputeMasterDTO, conn);
			if (i > 0) {
				return ResponseDTO.success;
			}
			return ResponseDTO.failure;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return ResponseDTO.failure;
	}

	private String updateDoc(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		PreparedStatement ps1 = null, ps2 = null, ps3 = null, ps4 = null, ps5 = null, ps6 = null;
		try {
			disputeMasterDTO = new DisputeRaiseDAO().addDisputeDocumentNMT(disputeMasterDTO);
			String query = null;
			if (!disputeMasterDTO.getEvidenceDoc1().equals("NA") && !disputeMasterDTO.getEvidenceDoc1().isEmpty()) {
				query = "UPDATE dispute_master_npci set evidence_doc=? WHERE id=?";
				ps1 = conn.prepareStatement(query);
				ps1.setString(1, disputeMasterDTO.getEvidenceDoc1());
				ps1.setString(2, disputeMasterDTO.getId());
			}
			if (!disputeMasterDTO.getEvidenceDoc2().equals("NA") && !disputeMasterDTO.getEvidenceDoc2().isEmpty()) {
				query = "UPDATE dispute_master_npci set evidence_doc1=? WHERE id=?";
				ps2 = conn.prepareStatement(query);
				ps2.setString(1, disputeMasterDTO.getEvidenceDoc2());
				ps2.setString(2, disputeMasterDTO.getId());
			}
			if (!disputeMasterDTO.getEvidenceDoc3().equals("NA") && !disputeMasterDTO.getEvidenceDoc3().isEmpty()) {
				query = "UPDATE dispute_master_npci set evidence_doc2=? WHERE id=?";
				ps3 = conn.prepareStatement(query);
				ps3.setString(1, disputeMasterDTO.getEvidenceDoc3());
				ps3.setString(2, disputeMasterDTO.getId());
			}
			if (!disputeMasterDTO.getEvidenceDoc4().equals("NA") && !disputeMasterDTO.getEvidenceDoc4().isEmpty()) {
				query = "UPDATE dispute_master_npci set evidence_doc3=? WHERE id=?";
				ps4 = conn.prepareStatement(query);
				ps4.setString(1, disputeMasterDTO.getEvidenceDoc4());
				ps4.setString(2, disputeMasterDTO.getId());
			}
			if (!disputeMasterDTO.getEvidenceDoc5().equals("NA") && !disputeMasterDTO.getEvidenceDoc5().isEmpty()) {
				query = "UPDATE dispute_master_npci set evidence_doc4=? WHERE id=?";
				ps5 = conn.prepareStatement(query);
				ps5.setString(1, disputeMasterDTO.getEvidenceDoc1());
				ps5.setString(2, disputeMasterDTO.getId());
			}
			if (!disputeMasterDTO.getEvidenceDoc6().equals("NA") && !disputeMasterDTO.getEvidenceDoc6().isEmpty()) {
				query = "UPDATE dispute_master_npci set evidence_doc5=? WHERE id=?";
				ps6 = conn.prepareStatement(query);
				ps6.setString(1, disputeMasterDTO.getEvidenceDoc6());
				ps6.setString(2, disputeMasterDTO.getId());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps1);
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps3);
			DatabaseManager.closePreparedStatement(ps4);
			DatabaseManager.closePreparedStatement(ps5);
			DatabaseManager.closePreparedStatement(ps6);
		}
		return "1";
	}

	@Override
	public List<DisputeMasterDTO> getTransactionToCloseByDate(String fromDate, String toDate, Connection conn) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getTransactionToCloseQuery);
			fromDate = fromDate + " 00:00:00";
			toDate = toDate + " 23:59:59";
			pstmt.setString(1, fromDate);
			pstmt.setString(2, toDate);
			pstmt.setString(3, DisputeMasterDTO.OPENSTATUS);
			pstmt.setString(4, DisputeMasterDTO.APPROVE);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// SELECT id, acq_txn_id, txn_amount, txn_type, txn_time, toll_plaza_id,
				// toll_txn_id, tag_id, tid, dispute_desc, status from dispute_master_npci where
				// txn_time BETWEEN ? AND ? and status=? and is_approved=? order by txn_time
				// desc limit 100
				// id, acq_txn_id, txn_amount, txn_type, txn_time, toll_plaza_id, toll_txn_id,
				// tag_id, tid, dispute_desc, status
				DisputeMasterDTO disputeMasterDTO = new DisputeMasterDTO();
				disputeMasterDTO.setId(rs.getString("id"));
				disputeMasterDTO.setOriginalTxnId(rs.getString("acq_txn_id"));
				disputeMasterDTO.setReferenceId(rs.getString("toll_txn_id"));
				// disputeMasterDTO.setTxnAmount(MoneyUtility.getRupeesFromPaisa(rs.getString("txn_amount")));
				disputeMasterDTO.setTxnAmount(rs.getString("txn_amount"));
				disputeMasterDTO.setTollPlazaId(rs.getString("toll_plaza_id"));
				disputeMasterDTO.setTxnType(rs.getString("txn_type"));
				disputeMasterDTO.setTagId(rs.getString("tag_id"));
				disputeMasterDTO.setTid(rs.getString("tid"));
				disputeMasterDTO.setComment(rs.getString("dispute_desc"));
				disputeMasterDTO.setStatus(rs.getString("status"));
				disputeMasterDTO.setTxnTime(DateUtility.getNewFormattedDate("yyyy-MM-dd HH:mm:ss", "yyMMddHHmmss",
						rs.getString("txn_time")));
				lst.add(disputeMasterDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getOneDayBeforeListOfTransactions()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}

	@Override
	public List<DisputeMasterDTO> getEGCSTransactions(DisputeMasterDTO mstDTO, Connection conn) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getEGCSTransactionQuery);
			pstmt.setString(1, mstDTO.getFromDate() + " 00:00:00");
			pstmt.setString(2, mstDTO.getToDate() + " 23:59:59");
			pstmt.setString(3, DisputeMasterDTO.OPENSTATUS);
			pstmt.setString(4, DisputeMasterDTO.APPROVE);
			rs = pstmt.executeQuery();
			DisputeMasterDTO disputeMasterDTO = new DisputeMasterDTO();

			// "
			// Tag_ID,Function_Code,Txn_Time,Txn_Id,Issuer_ID,Acquirer_ID,Txn_Amount,Reason_Code,Full_Partial_Indicator,
			// Toll_Plaza_Id,TID,MMT,Internal Tracking Number;
			while (rs.next()) {
				// id, acq_txn_id, txn_amount, txn_type, txn_time, toll_plaza_id, toll_txn_id,
				// tag_id, tid, dispute_desc, status
				disputeMasterDTO = new DisputeMasterDTO();
				disputeMasterDTO.setTagId(rs.getString("tag_id"));
				disputeMasterDTO.setFunctionCode(rs.getString("function_code"));//
				disputeMasterDTO.setTxnTime(rs.getString("txn_time"));

				// disputeMasterDTO.setTxnId(rs.getString("acq_txn_id"));
				String txnID = rs.getString("acq_txn_id");
				if (txnID.startsWith("'")) {
					txnID = txnID.substring(1);
				}
				disputeMasterDTO.setTxnId(txnID);

				disputeMasterDTO.setIssuerId(PropertyReader.getPropertyValue("issuerIIN"));// issuer_id
				// function_code, issuer_id, reason_code, full_partial_indicator, mmt,
				// acquirer_id
				String acqId = rs.getString("acquirer_id");// AcqirerId
				if (acqId.contains("@")) {
					acqId = acqId.substring(0, acqId.indexOf("@"));
				}
				disputeMasterDTO.setAcquirerId(acqId);
				// disputeMasterDTO.setTxnAmount(rs.getString("txn_amount"));
				disputeMasterDTO.setTxnAmount(MoneyUtility.getPaiseFromRupees(rs.getString("txn_amount")));
				disputeMasterDTO.setReasonCode(rs.getString("reason_code"));
				disputeMasterDTO.setFullPartialIndicator(rs.getString("full_partial_indicator"));
				disputeMasterDTO.setTollPlazaId(rs.getString("toll_plaza_id"));

				disputeMasterDTO.setTid(rs.getString("tid"));
				disputeMasterDTO.setMmt(rs.getString("mmt"));
				disputeMasterDTO.setReferenceId(rs.getString("toll_txn_id"));

				/*
				 * disputeMasterDTO.setEvidenceDoc1("doc1");
				 * if(rs.getString("evidence_doc").equalsIgnoreCase("NA"))
				 * disputeMasterDTO.setEvidenceDoc1("NA");
				 * 
				 * disputeMasterDTO.setEvidenceDoc2("doc2");
				 * if(rs.getString("evidence_doc1").equalsIgnoreCase("NA"))
				 * disputeMasterDTO.setEvidenceDoc2("NA");
				 * 
				 * disputeMasterDTO.setEvidenceDoc3("doc3");
				 * if(rs.getString("evidence_doc2").equalsIgnoreCase("NA"))
				 * disputeMasterDTO.setEvidenceDoc3("NA");
				 * 
				 * disputeMasterDTO.setEvidenceDoc4("doc4");
				 * if(rs.getString("evidence_doc3").equalsIgnoreCase("NA"))
				 * disputeMasterDTO.setEvidenceDoc4("NA");
				 * 
				 * disputeMasterDTO.setEvidenceDoc5("doc5");
				 * if(rs.getString("evidence_doc4").equalsIgnoreCase("NA"))
				 * disputeMasterDTO.setEvidenceDoc5("NA");
				 * 
				 * disputeMasterDTO.setEvidenceDoc6("doc6");
				 * if(rs.getString("evidence_doc5").equalsIgnoreCase("NA"))
				 * disputeMasterDTO.setEvidenceDoc6("NA");
				 */
				// evidence_doc, evidence_doc1, evidence_doc2, evidence_doc3, evidence_doc4,
				// evidence_doc5

				/*
				 * disputeMasterDTO.setEvidenceDoc3(rs.getString("evidence_doc2"));
				 * disputeMasterDTO.setEvidenceDoc4(rs.getString("evidence_doc3"));
				 * disputeMasterDTO.setEvidenceDoc4(rs.getString("evidence_doc4"));
				 * disputeMasterDTO.setEvidenceDoc5(rs.getString("evidence_doc5"));
				 */

				lst.add(disputeMasterDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getOneDayBeforeListOfTransactions()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}

	@Override
	public List<DisputeMasterDTO> searchDisputeList(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		String sql = null;// DisputeQueries.getTxnsByDateToRaiseDispute;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();
			if ("date".equalsIgnoreCase(disputeMasterDTO.getFilter())) {
				sql = DisputeQueries.getSerachDisputeByDateQuery;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, disputeMasterDTO.getFromDate() + " 00:00:00");
				pstmt.setString(2, disputeMasterDTO.getToDate() + " 23:59:59");
			}

			else if ("referenceId".equalsIgnoreCase(disputeMasterDTO.getFilter())) {
				sql = DisputeQueries.getSerachDisputeByReferenceIdQuery;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, disputeMasterDTO.getFilterValue());
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO master = new DisputeMasterDTO();
				if (rs.getString("is_approved").equals(DisputeMasterDTO.APPROVE)) {
					master.setIsApproved(DisputeMasterDTO.APPROVED);
				} else if (rs.getString("is_approved").equals(DisputeMasterDTO.REJECT)) {
					master.setIsApproved(DisputeMasterDTO.REJECTED);
				} else if (rs.getString("is_approved").equals(DisputeMasterDTO.NEW)) {
					master.setIsApproved(DisputeMasterDTO.NEWLY);
				}
				master.setAcqTxnId(rs.getString("acq_txn_id"));
				master.setReferenceId(rs.getString("toll_txn_id"));
				master.setTollPlazaId(rs.getString("toll_plaza_id"));
				master.setTxnType(rs.getString("txn_type"));
				// master.setTxnAmount(MoneyUtility.getRupeesFromPaisa(rs.getString("txn_amount")));
				master.setTxnAmount(rs.getString("txn_amount"));
				master.setTagId(rs.getString("tag_id"));
				master.setStatus(rs.getString("status"));
				master.setTxnTime(DateUtility.getNewFormattedDate("yyyy-MM-dd HH:mm:ss", "yyMMddHHmmss",
						rs.getString("txn_time")));
				master.setComment(rs.getString("dispute_desc"));
				master.setRemark(checkResultSetString(rs.getString("remark"), "NA"));
				master.setCreatedBy(rs.getString("created_by"));
				master.setCreatedOn(rs.getString("created_on"));
				master.setLastUpdatedBy(
						checkResultSetString(rs.getString("last_updated_by"), rs.getString("created_by")));
				master.setLastUpdatedOn(
						checkResultSetString(rs.getString("last_updated_on"), rs.getString("created_on")));
				// acq_txn_id, toll_txn_id, toll_plaza_id, txn_type, txn_amount, tag_id,
				// dispute_desc, status, txn_time, is_approved, remark, created_by, created_on,
				// last_updated_by, last_updated_on
				//

				lst.add(master);
			}
			return lst;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}

	private static String checkResultSetString(String rs1, String rs2) {
		if (rs1 != null)
			return rs1;
		// return "NA";
		return rs2;
	}

	@Override
	public List<DisputeMasterDTO> getRejectedList(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		String sql = null;// DisputeQueries.getTxnsByDateToRaiseDispute;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();
			sql = DisputeQueries.getApproveNRejectTxnByDateQuery;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, disputeMasterDTO.getFromDate() + " 00:00:00");
			pstmt.setString(2, disputeMasterDTO.getToDate() + " 23:59:59");
			pstmt.setString(3, DisputeMasterDTO.REJECT);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO master = new DisputeMasterDTO();
				master.setIsApproved(DisputeMasterDTO.REJECTED);
				master.setAcqTxnId(rs.getString("acq_txn_id"));
				master.setReferenceId(rs.getString("toll_txn_id"));
				master.setTollPlazaId(rs.getString("toll_plaza_id"));
				master.setTxnType(rs.getString("txn_type"));
				// master.setTxnAmount(MoneyUtility.getRupeesFromPaisa(rs.getString("txn_amount")));
				master.setTxnAmount(rs.getString("txn_amount"));
				master.setTagId(rs.getString("tag_id"));
				master.setStatus(rs.getString("status"));
				master.setTxnTime(DateUtility.getNewFormattedDate("yyyy-MM-dd HH:mm:ss", "yyMMddHHmmss",
						rs.getString("txn_time")));
				master.setComment(rs.getString("dispute_desc"));
				master.setRemark(checkResultSetString(rs.getString("remark"), "NA"));
				master.setCreatedBy(rs.getString("created_by"));
				master.setCreatedOn(rs.getString("created_on"));
				master.setLastUpdatedBy(
						checkResultSetString(rs.getString("last_updated_by"), rs.getString("created_by")));
				master.setLastUpdatedOn(
						checkResultSetString(rs.getString("last_updated_on"), rs.getString("created_on")));
				// acq_txn_id, toll_txn_id, toll_plaza_id, txn_type, txn_amount, tag_id,
				// dispute_desc, status, txn_time, is_approved, remark, created_by, created_on,
				// last_updated_by, last_updated_on
				//

				lst.add(master);
			}
			return lst;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}

	@Override
	public List<DisputeMasterDTO> getApprovedList(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		String sql = null;// DisputeQueries.getTxnsByDateToRaiseDispute;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();
			sql = DisputeQueries.getApproveNRejectTxnByDateQuery;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, disputeMasterDTO.getFromDate() + " 00:00:00");
			pstmt.setString(2, disputeMasterDTO.getToDate() + " 23:59:59");
			pstmt.setString(3, DisputeMasterDTO.APPROVE);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO master = new DisputeMasterDTO();
				master.setIsApproved(DisputeMasterDTO.APPROVED);
				master.setAcqTxnId(rs.getString("acq_txn_id"));
				master.setReferenceId(rs.getString("toll_txn_id"));
				master.setTollPlazaId(rs.getString("toll_plaza_id"));
				master.setTxnType(rs.getString("txn_type"));
				// master.setTxnAmount(MoneyUtility.getRupeesFromPaisa(rs.getString("txn_amount")));
				master.setTxnAmount(rs.getString("txn_amount"));
				master.setTagId(rs.getString("tag_id"));
				master.setStatus(rs.getString("status"));
				master.setTxnTime(DateUtility.getNewFormattedDate("yyyy-MM-dd HH:mm:ss", "yyMMddHHmmss",
						rs.getString("txn_time")));
				master.setComment(rs.getString("dispute_desc"));
				master.setRemark(checkResultSetString(rs.getString("remark"), "NA"));
				master.setCreatedBy(rs.getString("created_by"));
				master.setCreatedOn(rs.getString("created_on"));
				master.setLastUpdatedBy(
						checkResultSetString(rs.getString("last_updated_by"), rs.getString("created_by")));
				master.setLastUpdatedOn(
						checkResultSetString(rs.getString("last_updated_on"), rs.getString("created_on")));
				// acq_txn_id, toll_txn_id, toll_plaza_id, txn_type, txn_amount, tag_id,
				// dispute_desc, status, txn_time, is_approved, remark, created_by, created_on,
				// last_updated_by, last_updated_on
				//

				lst.add(master);
			}
			return lst;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}

	@Override
	public List<DisputeMasterDTO> getDisputeStatus(DisputeMasterDTO dto, Connection conn, String userId) {
		List<DisputeMasterDTO> lst = new ArrayList<DisputeMasterDTO>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			if (userId.startsWith("C")) {
				pstmt = conn.prepareStatement(DisputeQueries.getDisputeStatusQuery);
				pstmt.setString(1, userId);
				pstmt.setString(2, dto.getFromDate() + " 00:00:00");
				pstmt.setString(3, dto.getToDate() + " 23:59:59");
			} else {
				pstmt = conn.prepareStatement(DisputeQueries.getDisputeStatusQueryAdmin);
				pstmt.setString(1, dto.getFromDate() + " 00:00:00");
				pstmt.setString(2, dto.getToDate() + " 23:59:59");
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				DisputeMasterDTO disputeMasterDTO = new DisputeMasterDTO();
				// id, tag_id, function_code, txn_time, acq_txn_id, issuer_id, acquirer_id,
				// txn_amount, actual_txn_amount, reason_code, toll_plaza_id,
				// full_partial_indicator, tid, mmt, toll_txn_id, txn_type, dispute_desc,
				// created_on, created_by
				disputeMasterDTO.setId(rs.getString("id"));
				disputeMasterDTO.setTagId(rs.getString("tag_id"));
				disputeMasterDTO.setFunctionCode(rs.getString("function_code"));
				disputeMasterDTO.setOriginalTxnId(rs.getString("acq_txn_id"));
				disputeMasterDTO.setIssuerId(rs.getString("issuer_id"));
				disputeMasterDTO.setAcquirerId(rs.getString("acquirer_id"));
				// disputeMasterDTO.setTxnAmount(MoneyUtility.getRupeesFromPaisa(rs.getString("txn_amount")));
				disputeMasterDTO.setTxnAmount(rs.getString("txn_amount"));
				disputeMasterDTO.setActualTxnAmount(rs.getString("actual_txn_amount"));
				disputeMasterDTO.setReasonCode(rs.getString("reason_code"));
				disputeMasterDTO.setTollPlazaId(rs.getString("toll_plaza_id"));
				disputeMasterDTO.setFullPartialIndicator(rs.getString("full_partial_indicator"));
				disputeMasterDTO.setTid(rs.getString("tid"));
				disputeMasterDTO.setMmt(rs.getString("mmt"));
				disputeMasterDTO.setReferenceId(rs.getString("toll_txn_id"));
				disputeMasterDTO.setTxnType(rs.getString("txn_type"));
				disputeMasterDTO.setComment(rs.getString("dispute_desc"));
				disputeMasterDTO.setTxnTime(DateUtility.getNewFormattedDate("yyyy-MM-dd HH:mm:ss", "yyMMddHHmmss",
						rs.getString("txn_time")));
				disputeMasterDTO.setCreatedBy(rs.getString("created_by"));
				disputeMasterDTO.setCreatedOn(rs.getString("created_on"));
				disputeMasterDTO.setStatus(rs.getString("status"));
				if (rs.getString("is_approved").equals("0")) {
					disputeMasterDTO.setApprovalStatus("PENDING FOR APPROVAL");
				} else if (rs.getString("is_approved").equals("1")) {
					disputeMasterDTO.setApprovalStatus("APPROVED");
				}
				if (rs.getString("is_approved").equals("2")) {
					disputeMasterDTO.setApprovalStatus("REJECTED");
				}

				lst.add(disputeMasterDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getNewTransactionsToApprove()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return lst;
	}
}
