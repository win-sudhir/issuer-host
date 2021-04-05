/**
 * 
 */
package com.winnovature.dispute.dao;

import java.sql.Connection;
import java.util.List;

import com.winnovature.dto.DisputeMasterDTO;


public interface DisputeSearchService {

	public List<DisputeMasterDTO> searchDisputeTxnsByDate(String fromDate, String toDate, String refNo);
	public List<DisputeMasterDTO> approvedDisputeTxnsByDate(String fromDate, String toDate);
	public List<DisputeMasterDTO> rejectedDisputeTxnsByDate(String fromDate, String toDate);
	//public List<DisputeMasterDTO> getTransactionsByDate(String fromDate, String toDate);
	public List<DisputeMasterDTO> getApprovalTransactionsByDate(String fromDate, String toDate);
	/**
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getTransactionsByFilter(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId);
	//to get one day before transaction list
	/**
	 * @param originalTxnId
	 * @param conn
	 * @return
	 */
	public List<DisputeMasterDTO> getOneDayBeforeTransactions(String originalTxnId, Connection conn);
	public List<DisputeMasterDTO> getNewTransactionsToApprove(DisputeMasterDTO disputeMasterDTO, Connection conn);
	public List<DisputeMasterDTO> getDisputeStatus(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId);
	
	public List<DisputeMasterDTO> getTransactionToUpdateById(String id, Connection conn);
	public List<DisputeMasterDTO> getTransactionDetails(String originalTxnId, Connection conn);
	public List<DisputeMasterDTO> getTransactionToCloseByDate(String fromDate, String toDate, Connection conn);
	public List<DisputeMasterDTO> getEGCSTransactions(DisputeMasterDTO disputeMasterDTO, Connection conn);
	public List<DisputeMasterDTO> getSearchDisputeList(DisputeMasterDTO disputeMasterDTO, Connection conn);
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
}
