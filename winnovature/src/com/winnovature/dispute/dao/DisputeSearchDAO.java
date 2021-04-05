/**
 * 
 */
package com.winnovature.dispute.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.winnovature.dto.DisputeMasterDTO;


public interface DisputeSearchDAO {
	public List<DisputeMasterDTO> getDisputeTxnsByDate(String fromDate, String toDate, String refNo);
	public List<DisputeMasterDTO> getApprovedTransactionsByDate(String fromDate, String toDate);
	public List<DisputeMasterDTO> getRejectedTransactionsByDate(String fromDate, String toDate);
	public List<DisputeMasterDTO> getTransactionsByFilter(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId);
	public List<DisputeMasterDTO> getApprovalTransactionsByDate(String fromDate, String toDate);
	public Map<String, String> callWallet(String issuerId, String amount);
	public String approveDispute(String id, String userId, Connection conn);
	public String rejectDisputeTransaction(String id, String remark, String userId, Connection conn) ;
	public String updateDisputeTransaction(DisputeMasterDTO disputeMasterDTO, Connection conn);
	//for 24 hours txns
	public List<DisputeMasterDTO> getOneDayBeforeTransactions(String originalTxnId, Connection conn);
	/**
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getNewTransactionsToApprove(DisputeMasterDTO disputeMasterDTO, Connection conn);
	/**
	 * @param id
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getTransactionToUpdateById(String id, Connection conn);
	/**
	 * @param originalTxnId
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getTransactionDetails(String originalTxnId, Connection conn);
	/**
	 * @param fromDate
	 * @param toDate
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getTransactionToCloseByDate(String fromDate, String toDate, Connection conn);
	/**
	 * @param disputeMasterDTO
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getEGCSTransactions(DisputeMasterDTO disputeMasterDTO, Connection conn);
	/**
	 * @param disputeMasterDTO
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> searchDisputeList(DisputeMasterDTO disputeMasterDTO, Connection conn);
	/**
	 * @param disputeMasterDTO
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getRejectedList(DisputeMasterDTO disputeMasterDTO, Connection conn);
	/**
	 * @param disputeMasterDTO
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getApprovedList(DisputeMasterDTO disputeMasterDTO, Connection conn);
	public List<DisputeMasterDTO> getDisputeStatus(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId);
	
}
