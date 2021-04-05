/**
 * 
 */
package com.winnovature.dispute.dao;

import java.sql.Connection;
import java.util.List;

import com.winnovature.dto.DisputeMasterDTO;


public class DisputeSearchServiceImpl implements DisputeSearchService {

	//Database connectivity & uncomment
	private static DisputeSearchDAO dao = new DisputeSearchDAOImpl();
	//private static DisputeSearchDAO dao = new DisputeSearchDAOImplTest();
	private static volatile DisputeSearchService oInstance = null;
	public static DisputeSearchService getInstance() {
		
		if(oInstance == null) {
			oInstance = new DisputeSearchServiceImpl();
		}
		return oInstance;
	}
	
	public List<DisputeMasterDTO> searchDisputeTxnsByDate(String fromDate, String toDate, String refNo) {
		
		List<DisputeMasterDTO> results =  dao.getDisputeTxnsByDate(fromDate, toDate, refNo);
		return results;
		 
	}
	
	@Override
	public List<DisputeMasterDTO> approvedDisputeTxnsByDate(String fromDate, String toDate) {
		List<DisputeMasterDTO> results =  dao.getApprovedTransactionsByDate(fromDate, toDate);
		return results;
	}

	@Override
	public List<DisputeMasterDTO> rejectedDisputeTxnsByDate(String fromDate, String toDate) {
		List<DisputeMasterDTO> results =  dao.getRejectedTransactionsByDate(fromDate, toDate);
		return results;
	}

	@Override
	public List<DisputeMasterDTO> getTransactionsByFilter(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId) {
		List<DisputeMasterDTO> results =  dao.getTransactionsByFilter(disputeMasterDTO, conn, userId);
		return results;
	}
	
	@Override
	public List<DisputeMasterDTO> getApprovalTransactionsByDate(String fromDate, String toDate) {
		List<DisputeMasterDTO> results =  dao.getApprovalTransactionsByDate(fromDate, toDate);
		return results;
	}

	
	@Override
	public List<DisputeMasterDTO> getOneDayBeforeTransactions(String originalTxnId, Connection conn) {
		// TODO Auto-generated method stub
		List<DisputeMasterDTO> results =  dao.getOneDayBeforeTransactions(originalTxnId, conn);
		return results;
	}

	
	@Override
	public List<DisputeMasterDTO> getNewTransactionsToApprove(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		List<DisputeMasterDTO> results =  dao.getNewTransactionsToApprove(disputeMasterDTO, conn);
		return results;
	}

	
	@Override
	public List<DisputeMasterDTO> getTransactionToUpdateById(String id, Connection conn) {
		List<DisputeMasterDTO> results =  dao.getTransactionToUpdateById(id, conn);
		return results;
	}

	
	@Override
	public List<DisputeMasterDTO> getTransactionDetails(String originalTxnId, Connection conn) {
		List<DisputeMasterDTO> results =  dao.getTransactionDetails(originalTxnId, conn);
		return results;
	}


	@Override
	public List<DisputeMasterDTO> getTransactionToCloseByDate(String fromDate, String toDate, Connection conn) {
		List<DisputeMasterDTO> results =  dao.getTransactionToCloseByDate(fromDate, toDate, conn);
		return results;
	}


	@Override
	public List<DisputeMasterDTO> getEGCSTransactions(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		List<DisputeMasterDTO> results =  dao.getEGCSTransactions(disputeMasterDTO, conn);
		return results;
	}

	
	@Override
	public List<DisputeMasterDTO> getSearchDisputeList(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		List<DisputeMasterDTO> results =  dao.searchDisputeList(disputeMasterDTO, conn);
		return results;
	}


	@Override
	public List<DisputeMasterDTO> getRejectedList(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		List<DisputeMasterDTO> results =  dao.getRejectedList(disputeMasterDTO, conn);
		return results;
	}

	
	@Override
	public List<DisputeMasterDTO> getApprovedList(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		List<DisputeMasterDTO> results =  dao.getApprovedList(disputeMasterDTO, conn);
		return results;
	}

	@Override
	public List<DisputeMasterDTO> getDisputeStatus(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId) {
		List<DisputeMasterDTO> results =  dao.getDisputeStatus(disputeMasterDTO, conn, userId);
		return results;
	}


	
}
