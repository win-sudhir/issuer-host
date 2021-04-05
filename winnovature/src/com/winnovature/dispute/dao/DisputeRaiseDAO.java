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
import com.winnovature.dto.RequestPayDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.querries.DisputeQueries;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtility;
import com.winnovature.utils.FileTransferUtility;
import com.winnovature.utils.MoneyUtility;
import com.winnovature.utils.PropertyReader;
import com.winnovature.validation.DisputeErrorCode;

public class DisputeRaiseDAO {
	static Logger log = Logger.getLogger(DisputeRaiseDAO.class.getClass());
	
	public ResponseDTO addDisputeInfo(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		if(!checkOriginalTransactionId(disputeMasterDTO.getOriginalTxnId(), conn)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU008.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU008.name());
			return responseDTO;
		}
		
		if(checkDisputeRaised(disputeMasterDTO.getOriginalTxnId(), conn)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU005.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU005.name());
			return responseDTO;
		}
		
		disputeMasterDTO.setFunctionCode(DisputeMasterDTO.CHBFUNCTIONCODE);
		if(checkGoodFaith(disputeMasterDTO.getOriginalTxnId(), conn)){
			disputeMasterDTO.setFunctionCode(DisputeMasterDTO.GOODFAITH);
		}
		String referenceId = DisputeRaiseDAO.getDisputeReferenceNumber();
		disputeMasterDTO.setReferenceId(referenceId);
		
		if(addDisputeMasterInfo(disputeMasterDTO, conn, userId).equals(ResponseDTO.failure)){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU007.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU007.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(DisputeErrorCode.DISPUTEBU003.getErrorMessage()+referenceId);
		//responseDTO.setMessage(DisputeErrorCode.DISPUTEBU003.getErrorMessage());
		responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU003.name());
		return responseDTO;
	}
	
	private String addDisputeMasterInfo(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId){
		PreparedStatement pstmt = null;
		try {
			log.info("------ADDING IN DISPUTE-----");
			disputeMasterDTO = addDisputeDocumentNMT(disputeMasterDTO);
			pstmt = conn.prepareStatement(DisputeQueries.getAddDisputeInfoQuery);
			//System.out.println("HERE ....");
			RequestPayDTO requestPayDTO = getRequestPayTxn(disputeMasterDTO.getOriginalTxnId(), conn);
			pstmt.setString(1, requestPayDTO.getTagId());
			
			//pstmt.setString(2, DisputeMasterDTO.FUNCTIONCODE);
			pstmt.setString(2, disputeMasterDTO.getFunctionCode());
			
			pstmt.setString(3, DateUtility.getDateFormatInPattern("yyyy-MM-dd HH:mm:ss",requestPayDTO.getTxnTime()));
			pstmt.setString(4, disputeMasterDTO.getOriginalTxnId());
			pstmt.setString(5, PropertyReader.getPropertyValue("issuerIIN"));
			pstmt.setString(6, requestPayDTO.getAcquirerId());
			pstmt.setString(7, disputeMasterDTO.getTxnAmount());
			pstmt.setString(8, MoneyUtility.getRupeesToRupees(requestPayDTO.getTxnAmount()));
			
			pstmt.setString(9, disputeMasterDTO.getReasonCode());
			pstmt.setString(10, requestPayDTO.getTollPlazaId());
			pstmt.setString(11, disputeMasterDTO.getFullPartialIndicator());
			pstmt.setString(12, requestPayDTO.getTid());
			pstmt.setString(13, disputeMasterDTO.getMmt());
			pstmt.setString(14, disputeMasterDTO.getReferenceId());
			pstmt.setString(15, DisputeMasterDTO.CREDIT);
			pstmt.setString(16, disputeMasterDTO.getComment());
			
			pstmt.setString(17, disputeMasterDTO.getEvidenceDoc1());
			pstmt.setString(18, disputeMasterDTO.getEvidenceDoc2());
						
			pstmt.setString(19, DisputeMasterDTO.OPENSTATUS);
			pstmt.setString(20, DisputeMasterDTO.MANUALPROCESSSTATUS);
			pstmt.setString(21, DisputeMasterDTO.NEW);
			pstmt.setString(22, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			pstmt.setString(23, userId);
			pstmt.setString(24, DateUtility.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			
			int i = pstmt.executeUpdate();
			if(i>0){
				return ResponseDTO.success;
			}
			else{
				return ResponseDTO.failure;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return ResponseDTO.failure;
	}
	
	
	
	private RequestPayDTO getRequestPayTxn(String originalTxnId, Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RequestPayDTO requestPayDTO = new RequestPayDTO();
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getRequestPayQuery);
			pstmt.setString(1, originalTxnId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				//txn_time, txn_id, original_txn_id, tag_id, tid, txn_amount, txn_type, vehicle_no, acq_id,toll_plaza_id,toll_plaza_name
				requestPayDTO.setAcquirerId(rs.getString("acq_id"));
				requestPayDTO.setTxnAmount(rs.getString("txn_amount"));
				requestPayDTO.setTxnTime(rs.getString("txn_time"));
				requestPayDTO.setTxnType(rs.getString("txn_type"));
				requestPayDTO.setTagId(rs.getString("tag_id"));
				requestPayDTO.setTid(rs.getString("tid"));
				requestPayDTO.setTollPlazaId(rs.getString("toll_plaza_id"));
				requestPayDTO.setTollPlazaName(rs.getString("toll_plaza_name"));
				requestPayDTO.setTxnId(rs.getString("txn_id"));
				requestPayDTO.setOriginalTxnId(originalTxnId);
				return requestPayDTO; 
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return requestPayDTO;
	}
	
	public DisputeMasterDTO addDisputeDocumentNMT(DisputeMasterDTO disputeMasterDTO){
		try {
			String evidenceDoc1="NA", evidenceDoc2="NA";//, evidenceDoc3="NA", evidenceDoc4="NA", evidenceDoc5="NA", evidenceDoc6="NA";
			String originalTxnId=disputeMasterDTO.getOriginalTxnId();		
			String extention = null;
			if(disputeMasterDTO.getEvidenceDoc1()!=null && disputeMasterDTO.getEvidenceDoc1()!="" && !disputeMasterDTO.getEvidenceDoc1().equalsIgnoreCase("NA")){
				extention = disputeMasterDTO.getEvidenceDoc1().substring(disputeMasterDTO.getEvidenceDoc1().indexOf("."));
				evidenceDoc1 = disputeMasterDTO.getEvidenceDoc1();//FileTransferUtility.moveDocument(disputeMasterDTO.getEvidenceDoc1(), originalTxnId+"_Doc1"+extention, originalTxnId);
			}
			if(disputeMasterDTO.getEvidenceDoc2()!=null && disputeMasterDTO.getEvidenceDoc2()!="" && !disputeMasterDTO.getEvidenceDoc2().equalsIgnoreCase("NA")){
				extention = disputeMasterDTO.getEvidenceDoc2().substring(disputeMasterDTO.getEvidenceDoc2().indexOf("."));
				evidenceDoc2 = disputeMasterDTO.getEvidenceDoc2();//FileTransferUtility.moveDocument(disputeMasterDTO.getEvidenceDoc2(), originalTxnId+"_Doc2"+extention, originalTxnId);
			}
			/*
			 * if(disputeMasterDTO.getEvidenceDoc3()!=null &&
			 * disputeMasterDTO.getEvidenceDoc3()!="" &&
			 * !disputeMasterDTO.getEvidenceDoc3().equalsIgnoreCase("NA")){ extention =
			 * disputeMasterDTO.getEvidenceDoc3().substring(disputeMasterDTO.getEvidenceDoc3
			 * ().indexOf(".")); evidenceDoc3 =
			 * FileTransferUtility.moveDocument(disputeMasterDTO.getEvidenceDoc3(),
			 * originalTxnId+"_Doc3"+extention, originalTxnId); }
			 * if(disputeMasterDTO.getEvidenceDoc4()!=null &&
			 * disputeMasterDTO.getEvidenceDoc4()!="" &&
			 * !disputeMasterDTO.getEvidenceDoc4().equalsIgnoreCase("NA")){ extention =
			 * disputeMasterDTO.getEvidenceDoc4().substring(disputeMasterDTO.getEvidenceDoc4
			 * ().indexOf(".")); evidenceDoc4 =
			 * FileTransferUtility.moveDocument(disputeMasterDTO.getEvidenceDoc4(),
			 * originalTxnId+"_Doc4"+extention, originalTxnId); }
			 * if(disputeMasterDTO.getEvidenceDoc5()!=null &&
			 * disputeMasterDTO.getEvidenceDoc5()!="" &&
			 * !disputeMasterDTO.getEvidenceDoc5().equalsIgnoreCase("NA")){ extention =
			 * disputeMasterDTO.getEvidenceDoc5().substring(disputeMasterDTO.getEvidenceDoc5
			 * ().indexOf(".")); evidenceDoc5 =
			 * FileTransferUtility.moveDocument(disputeMasterDTO.getEvidenceDoc5(),
			 * originalTxnId+"_Doc5"+extention, originalTxnId); }
			 * if(disputeMasterDTO.getEvidenceDoc6()!=null &&
			 * disputeMasterDTO.getEvidenceDoc6()!="" &&
			 * !disputeMasterDTO.getEvidenceDoc6().equalsIgnoreCase("NA")){ extention =
			 * disputeMasterDTO.getEvidenceDoc6().substring(disputeMasterDTO.getEvidenceDoc6
			 * ().indexOf(".")); evidenceDoc6 =
			 * FileTransferUtility.moveDocument(disputeMasterDTO.getEvidenceDoc6(),
			 * originalTxnId+"_Doc6"+extention, originalTxnId); }
			 */
			
			disputeMasterDTO.setEvidenceDoc1(evidenceDoc1);
			disputeMasterDTO.setEvidenceDoc2(evidenceDoc2);
			/*
			 * disputeMasterDTO.setEvidenceDoc3(evidenceDoc3);
			 * disputeMasterDTO.setEvidenceDoc4(evidenceDoc4);
			 * disputeMasterDTO.setEvidenceDoc5(evidenceDoc5);
			 * disputeMasterDTO.setEvidenceDoc6(evidenceDoc6);
			 */
			
			//MMT
			//adding mmt as per reason_code 1 2 4 5 11
			String mmt = null;
			if(disputeMasterDTO.getReasonCode().equals("3001"))
				mmt = "Customer has not avail the NETC Service";
			else if(disputeMasterDTO.getReasonCode().equals("3002"))
				mmt = "Txn Reference Number "+disputeMasterDTO.getOriginalTxnId()+" "+disputeMasterDTO.getDuplicateTxnId();
			else if(disputeMasterDTO.getReasonCode().equals("3004"))
				mmt = "Date and time of adding the Tag in Black list in NPCI Mapper";
			else if(disputeMasterDTO.getReasonCode().equals("3005"))
				mmt = "Date and time of adding the Tag in Black list in NPCI Mapper";
			else if(disputeMasterDTO.getReasonCode().equals("3011"))
				mmt = "Detail description of payment made or paid by cash or other mode";
			else 
				mmt = "Detail description of dispute";
			
			disputeMasterDTO.setMmt(mmt);
			
			return disputeMasterDTO;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return disputeMasterDTO;
	}
	
	public static String getDisputeReferenceNumber() {
		String id = "D" + System.currentTimeMillis() + ""; 
		log.info("getDisputeReferenceNumber : " + id);
		return id;
	}
	
	private boolean checkDisputeRaised(String originalTxnId, Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getCheckDisputeQuery);
			pstmt.setString(1, originalTxnId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return false;
	}
	
	public boolean checkGoodFaith(String originalTxnId, Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getCheckGoodFaithQuery);
			pstmt.setString(1, originalTxnId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return false;
	}
	
	private boolean checkOriginalTransactionId(String originalTxnId, Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(DisputeQueries.getCheckRequestPayQuery);
			pstmt.setString(1, originalTxnId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return true;
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return false;
	}
	
	public ResponseDTO approveDispute(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		// log.info("ID :: "+disputeMasterDTO.getId());
		// validation
		if (disputeMasterDTO.getId() == null || "".equals(disputeMasterDTO.getId())) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		/*
		 * if (disputeMasterDTO.getRequestBy() == null ||
		 * "".equals(disputeMasterDTO.getRequestBy())) {
		 * responseDTO.setStatus(ResponseDTO.failure);
		 * responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
		 * responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name()); return
		 * responseDTO; }
		 */
		/*String result = ServiceUtil.getDisputeManagementService().approveDisputeTransaction(disputeMasterDTO.getId(),
				disputeMasterDTO.getRequestBy(), conn);*/
		log.info("ID :: "+disputeMasterDTO.getId());
		if (DisputeServiceUtil.getDisputeManagementService().approveDisputeTransaction(disputeMasterDTO.getId(),
				userId, conn).equals(ResponseDTO.failure)) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU009.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU009.name());
			return responseDTO;
		}
		log.info("---ID :: "+disputeMasterDTO.getId());
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0010.getErrorMessage());
		responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0010.name());
		return responseDTO;
	}

	public ResponseDTO rejectDispute(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		// validation
		if (disputeMasterDTO.getId() == null || "".equals(disputeMasterDTO.getId())) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if (disputeMasterDTO.getRemark() == null || "".equals(disputeMasterDTO.getRemark())) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		
		/*String result = ServiceUtil.getDisputeManagementService().rejectDisputeTransaction(disputeMasterDTO.getId(),
				disputeMasterDTO.getRemark(), disputeMasterDTO.getRequestBy(), conn);*/
		
		//if (result.equals(ResponseDTO.failure)) {
		if (DisputeServiceUtil.getDisputeManagementService().rejectDisputeTransaction
				(disputeMasterDTO.getId(),disputeMasterDTO.getRemark(), userId, conn)
				.equals(ResponseDTO.failure)) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0011.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0011.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0012.getErrorMessage());
		responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0012.name());
		return responseDTO;
	}

	public ResponseDTO updateDisputeTransaction(DisputeMasterDTO disputeMasterDTO, Connection conn, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		// validation
		if (disputeMasterDTO.getId() == null || "".equals(disputeMasterDTO.getId())) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if (disputeMasterDTO.getComment() == null || "".equals(disputeMasterDTO.getComment())) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		/*
		 * if (disputeMasterDTO.getRequestBy() == null ||
		 * "".equals(disputeMasterDTO.getRequestBy())) {
		 * responseDTO.setStatus(ResponseDTO.failure);
		 * responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
		 * responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name()); return
		 * responseDTO; }
		 */
		if (disputeMasterDTO.getReasonCode() == null || "".equals(disputeMasterDTO.getReasonCode())) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if (disputeMasterDTO.getTxnAmount() == null || "".equals(disputeMasterDTO.getTxnAmount())) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		if (disputeMasterDTO.getFullPartialIndicator() == null || "".equals(disputeMasterDTO.getFullPartialIndicator())) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU004.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU004.name());
			return responseDTO;
		}
		
		if(disputeMasterDTO.getTxnAmount().equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0027.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0027.name());
			return responseDTO;
		}
		/*String result = ServiceUtil.getDisputeManagementService().rejectDisputeTransaction(disputeMasterDTO.getId(),
				disputeMasterDTO.getRemark(), disputeMasterDTO.getRequestBy(), conn);*/
		
		// if (result.equals(ResponseDTO.failure)) {
		if (DisputeServiceUtil.getDisputeManagementService().updateDisputeTransaction(disputeMasterDTO, conn)
				.equals(ResponseDTO.failure)) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0013.getErrorMessage());
			responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0013.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0014.getErrorMessage());
		responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0014.name());
		return responseDTO;
	}

	public static String getFileName() {
		String bankParticipantId = PropertyReader.getPropertyValue("bankParticipantId");
		String date = DateUtility.getJulianDateOfDate("yyDDD"); //new SimpleDateFormat("yyDDD").format(new Date());
		String fileName = bankParticipantId + date + "00" + ".csv";
		return fileName;
	}

	
}
