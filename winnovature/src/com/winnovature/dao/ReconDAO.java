
package com.winnovature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.dto.Recon861DTO;
import com.winnovature.dto.Recon861DTO.TransactionInfoDTO;
import com.winnovature.dto.ReconSummaryDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.querries.ReconconciliationQueries;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtility;
import com.winnovature.validation.ReconErrorCode;


public class ReconDAO {
	static Logger log = Logger.getLogger(ReconDAO.class.getName());
	
	public static boolean checkFileProcessed(String fileName, Connection conn) {
		boolean check = false;
		//Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = ReconconciliationQueries.getIsfileProcessedQuery;
				//"SELECT file_name FROM npci_recon_status where file_name=?";
		try {
			//conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fileName);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				check = true;
				log.info("file_name : " + rs.getString("file_name"));
				log.info("In IF Flag value " + check);
				return check;
			} 
		} catch (Exception e) {
			log.error("Error while checking cycle records" + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
		return check;
	}
	
	
	public static boolean isCycleProcessed(String reconDate, String reconCycle, Connection conn)// , String // fileName)
	{
		
		//Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = ReconconciliationQueries.getValidatePreviousCycleQuery;
				//"SELECT on_date, cycle FROM npci_recon_status where on_date=? and cycle=?";
		// boolean flag=false;
		try {
			//conn = DatabaseManager.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, reconDate);
			ps.setString(2, reconCycle);
			// st.setString(3, fileName);
			rs = ps.executeQuery();
			if (rs.next()) {
				//log.info("reconDate : " + rs.getString("on_date"));
				//log.info("reconCycle : " + rs.getString("cycle"));
				return true;
			} 
			
		} catch (Exception e) {
			log.error("Error while checking cycle records" + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closeConnection(conn);
		}
		return false;
	}
	
	public static boolean isCycleRevoked(String reconDate, String reconCycle, Connection conn)// , String // fileName)
	{
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = ReconconciliationQueries.getIsCycleRevokedQuery;
				//"SELECT on_date, cycle FROM bank_recon_status where on_date=? and cycle=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, reconDate);
			ps.setString(2, reconCycle);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} 
			
		} catch (Exception e) {
			log.error("Error while checking cycle records" + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}
	
	public static boolean validatePreviousCycle(String reconDate, int reconCycle, Connection conn) {
		log.info("In validatePreviousCycle()");
		boolean reconDone = false;
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int checkCycle = 2;
		try {
			//conn = DatabaseManager.getConnection();
			String checkDate = DateUtility.getpreviousDate(reconDate);
			if (reconCycle == 2) {
				checkCycle = 1;
				checkDate = reconDate;
			}
			String sql = ReconconciliationQueries.getValidatePreviousCycleQuery;
			//String sql = "select on_date, cycle from npci_recon_status where on_date=? and cycle=?";
			//log.info(sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, checkDate);
			ps.setInt(2, checkCycle);
			//ps = conn.createStatement();
			rs = ps.executeQuery();
			if (rs.next()) {
				reconDone = true;
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closeConnection(conn);
		}
		return reconDone;

	}
	
	
	
	public String latestDate(Connection conn) {
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Date> dates = new ArrayList<>();
		String ondate;
		Date date = null;
		Date latest = null;
		String latestDate = null;
		try {
			String sql = ReconconciliationQueries.getLatestDateQuery;
					// getLatestDateQuery = "SELECT on_date FROM npci_recon_status";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ondate = (rs.getString("on_date"));
				date = DateUtility.getReconDate(ondate);
				dates.add(date);
			}
		} catch (Exception e) {
			log.error("Exception while getting date : " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closeConnection(conn);
		}
		if (dates.isEmpty()) {
			log.info("Dates are empty");
			latestDate = "";
		} else {
			latest = Collections.max(dates);
			latestDate = DateUtility.getDateFormatDate("dd/MM/yyyy", latest);
		}
		return latestDate;
	}
	
	//public JSONObject processNRecon(String reconDate, String reconCycle, String fileName)
	public ResponseDTO processReconciliation(Recon861DTO recon861DTO, Connection conn)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		try
		{
			log.info("processReconciliation --> ");
			
			if(!addTransactions(recon861DTO, conn).equalsIgnoreCase(ResponseDTO.success)){
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(ReconErrorCode.RECONBU0010.getErrorMessage());
				responseDTO.setErrorCode(ReconErrorCode.RECONBU0010.name());
				return responseDTO;
			}
			
			if(!reconNPCI(recon861DTO.getReconDate(), recon861DTO.getReconCycle(), recon861DTO.getFileName(), conn).equalsIgnoreCase(ResponseDTO.success)){
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(ReconErrorCode.RECONBU0011.getErrorMessage());
				responseDTO.setErrorCode(ReconErrorCode.RECONBU0011.name());
				return responseDTO;
			}
			
			if(addDatNCycle(recon861DTO.getReconDate(), recon861DTO.getReconCycle(), recon861DTO.getFileName(), conn).equalsIgnoreCase(ResponseDTO.success)){
				log.info("Transactions posted successfully");
				responseDTO.setStatus(ResponseDTO.success);
				responseDTO.setMessage("Total transactions are : "+recon861DTO.getTransactionInfoDTO().size());//+ReconErrorCode.RECONBU000.getErrorMessage());//change response
				responseDTO.setErrorCode(ReconErrorCode.RECONBU000.name());
				responseDTO.setReconSummary(getNPCIReconSummary(conn));
				//responseDTO.setDateList(ReconDatesDao.reconDateList());
				return responseDTO;
			}
			else
			{
				log.info("Transactions can not processed : ");
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage(ReconErrorCode.RECONBU0012.getErrorMessage());
				responseDTO.setErrorCode(ReconErrorCode.RECONBU0012.name());
				return responseDTO;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}
	
	private static String addDatNCycle(String reconDate, String reconCycle, String fileName, Connection conn) 
	{
		log.info("In addDatNCycle()---->");
		//public static String getAddDateNCycleQuery = "INSERT INTO npci_recon_status(on_date, cycle,file_name) VALUES(?,?,?)";
		String sql = ReconconciliationQueries.getAddDateNCycleQuery;
		//log.info("insertCycle :: sql :: "+sql);
		//log.info("insertCycle :: reconDate : " + reconDate + " reconCycle : " + reconCycle);
		//Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			//conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reconDate);// txn.getCycledate());
			pstmt.setString(2, reconCycle);// txn.getCycle());
			pstmt.setString(3, fileName);
			int i = pstmt.executeUpdate();
			if(i>0){
				log.info("Cycle is inserted successfully...........");
				return ResponseDTO.success;
			}
			else{
				return ResponseDTO.failure;
			}
				
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
		return ResponseDTO.failure;
	}
	
	private static String addTransactions(Recon861DTO recon861DTO, Connection conn) {
		
		String sql = ReconconciliationQueries.getAddReconTxnsQuery;
		PreparedStatement pstmt = null;
		String txnId = null, referenceId = null, originalTxnId;
		String txnDateTime = null;
		int i = 0;
		
		try {
			
			for (TransactionInfoDTO txn : recon861DTO.getTransactionInfoDTO()) {
				pstmt = conn.prepareStatement(sql);
				
				Date dt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(txn.getTransactionDateAndTime());
				String dateTimeTrans = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);

				Date dt1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(txn.getReaderReadDateAndTime());
				String readerTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt1);

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, txn.getTransactionSequenceNumber());
				txnId = txn.getTransactionId();
				if (txnId.startsWith("'")) {
					txnId = txnId.substring(1);
				}
				pstmt.setString(2, txnId);
				pstmt.setString(3, txn.getMessageId());
				pstmt.setString(4, txn.getNote());
				referenceId = txn.getReferenceId();
				if (referenceId.startsWith("'")) {
					referenceId = referenceId.substring(1);
				}
				pstmt.setString(5, referenceId);
				pstmt.setString(6, txn.getReferenceURL());
				pstmt.setString(7, dateTimeTrans);
				pstmt.setString(8, txn.getTransactionType());
				
				originalTxnId = txn.getOriginalTransactionId();
				if (originalTxnId.startsWith("'")) {
					originalTxnId = originalTxnId.substring(1);
				}
				
				
				pstmt.setString(9, originalTxnId);
				pstmt.setString(10, txn.getTagId());
				pstmt.setString(11, txn.getTID());
				pstmt.setString(12, txn.getAVC());
				pstmt.setString(13, txn.getWIM());
				pstmt.setString(14, txn.getMerchantId());
				pstmt.setString(15, txn.getMerchantType());
				pstmt.setString(16, txn.getSubMerchantType());
				pstmt.setString(17, txn.getLaneId());
				pstmt.setString(18, txn.getLaneDirection());
				pstmt.setString(19, txn.getLaneReaderId());
				pstmt.setString(20, txn.getParkingFloor());
				pstmt.setString(21, txn.getParkingZone());
				pstmt.setString(22, txn.getParkingSlot());
				pstmt.setString(23, txn.getParkingReaderId());
				pstmt.setString(24, readerTime);
				pstmt.setString(25, txn.getSignatureData());
				pstmt.setString(26, txn.getSignatureAuthentication());
				pstmt.setString(27, txn.getePCVerified());
				pstmt.setString(28, txn.getProcRestrictionRes());
				pstmt.setString(29, txn.getVehicleAuth());
				pstmt.setString(30, txn.getPublicKeyCVV());
				pstmt.setString(31, txn.getReaderTransactionCounter());
				pstmt.setString(32, txn.getReaderTransactionStatus());
				pstmt.setString(33, txn.getPayerAddress());
				pstmt.setString(34, txn.getIssuerId());
				pstmt.setString(35, txn.getPayerCode());
				pstmt.setString(36, txn.getPayerName());
				pstmt.setString(37, txn.getPayerType());
				pstmt.setString(38, txn.getTransactionAmount());
				pstmt.setString(39, txn.getCurrencyCode());
				pstmt.setString(40, txn.getPayeeAddress());
				pstmt.setString(41, txn.getAcquirerId());
				pstmt.setString(42, txn.getPayeeCode());
				pstmt.setString(43, txn.getPayeeName());
				pstmt.setString(44, txn.getPayeeType());
				pstmt.setString(45, txn.getResponseCode());
				pstmt.setString(46, txn.getTransactionStatus());
				pstmt.setString(47, txn.getApprovalNumber());
				pstmt.setString(48, txn.getPayeeErrorCode());
				pstmt.setString(49, txn.getSettledAmount());
				pstmt.setString(50, txn.getSettledCurrency());
				pstmt.setString(51, txn.getAccountType());
				pstmt.setString(52, txn.getAvailableBalance());
				pstmt.setString(53, txn.getLedgerBalance());
				pstmt.setString(54, txn.getAccountNumber());
				pstmt.setString(55, txn.getCustomerName());
				pstmt.setString(56, txn.getInitiatedBy());
				pstmt.setString(57, txn.getInitiatedTime());
				pstmt.setString(58, txn.getLastUpdatedBy());
				pstmt.setString(59, txn.getLastUpdatedTime());
				pstmt.setString(60, txn.getVehicleRegistrationNumber());
				pstmt.setString(61, txn.getVehicleClass());
				pstmt.setString(62, txn.getVehicleType());
				pstmt.setString(63, txn.getTagStatus());
				pstmt.setString(64, txn.getTagIssueDate());
				//pstmt.executeUpdate();

				i = pstmt.executeUpdate();
				i++;
			}
			
			if (i > 0) {
				log.info("861Transactions are inserted successfully...........");
				return ResponseDTO.success;
			} else {
				return ResponseDTO.failure;
			}

		} catch (Exception e) {
			log.error("Exception while insert in temp txns841 of NPCI In ReconDao :: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			// DatabaseManager.closeConnection(conn);
		}
		return ResponseDTO.failure;
	}
	
	private static String reconNPCI(String reconDate, String reconCycle, String fileName, Connection conn)
	{
		CallableStatement cs = null;
		//Connection conn = null;
		String result = null;
		//int i = 0;
		try {
			/*conn = DatabaseManager.getConnection();
			conn.setAutoCommit(false);*/
			SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date recdate = originalFormat.parse(reconDate);
			SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String finalDate = newFormat.format(recdate);
			//public static String getProcRecon841Query = "{call pr_recon_npci841(?,?)}";
			String query = ReconconciliationQueries.getProcRecon841Query;
			cs = conn.prepareCall(query);
			cs.setString(1, finalDate);
			cs.setString(2, reconCycle);
			cs.setString(3, fileName);
			cs.execute();
			//conn.commit();
			result = ResponseDTO.success;
				
			log.info("Procedure executed successfully--->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		} catch (Exception e) {
			result = ResponseDTO.failure;
			log.error("Exception in NPCI procedure <<<>>>>>>>>>>>>>>>>>>>Exception: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			//DatabaseManager.closeConnection(conn);
		}
		return result;
	}
	
	public List<ReconSummaryDTO> getNPCIReconSummary(Connection conn) {
		//Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		log.info("getNPCIReconSummary() ");
		//public static String getRecon841SumaryQuery = "select * from npci_recon_summary order by id desc limit 1";
		String query = ReconconciliationQueries.getRecon841SumaryQuery;
		List<ReconSummaryDTO> summaryList = new ArrayList<ReconSummaryDTO>();
		try {
			//conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(query);
			log.info("Query of getNPCIReconSummary :::: " + query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ReconSummaryDTO reconSummaryDTO = new ReconSummaryDTO();
				//txn.setRec_date(rs.getDate("recon_date"));
				reconSummaryDTO.setFileName(rs.getString("file_name"));
				reconSummaryDTO.setReconCycle(rs.getString("recon_cycle"));
				reconSummaryDTO.setTotalFileRecord(rs.getString("total_file_rec"));
				reconSummaryDTO.setTotalMatchRecord(rs.getString("total_match_rec"));
				reconSummaryDTO.setTotalMismatchRecord(rs.getString("total_mismatch_rec"));
				reconSummaryDTO.setAlreadyRecon(rs.getString("already_recon"));
				reconSummaryDTO.setDeemedAccepted(rs.getString("deemed_accepted"));
				reconSummaryDTO.setRemark(rs.getString("remarks"));
				reconSummaryDTO.setStatus(rs.getString("status"));
				summaryList.add(reconSummaryDTO);
			}

		} catch (Exception e) {
			log.error("Exception while getting list of NPCI recon summary : " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
		return summaryList;
	}
	
	
	public static List<ReconSummaryDTO> getNPCIReconSummaryOnLoad(Connection conn) {
		//Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		
		log.info("getNPCIReconSummaryOnLoad() ");
		String getRecon861SumaryQuery = "SELECT * FROM npci_recon_summary order by id desc limit 5";
		List<ReconSummaryDTO> summaryList = new ArrayList<ReconSummaryDTO>();
		try {
			//conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(getRecon861SumaryQuery);
			log.info("Query of getNPCIReconSummary :::: " + getRecon861SumaryQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				ReconSummaryDTO reconSummaryDTO = new ReconSummaryDTO();
				Date date = dt.parse(rs.getString("recon_date")); 
				reconSummaryDTO.setFileName(rs.getString("file_name"));
				reconSummaryDTO.setReconCycle(rs.getString("recon_cycle"));
				reconSummaryDTO.setTotalFileRecord(rs.getString("total_file_rec"));
				reconSummaryDTO.setTotalMatchRecord(rs.getString("total_match_rec"));
				reconSummaryDTO.setTotalMismatchRecord(rs.getString("total_mismatch_rec"));
				reconSummaryDTO.setAlreadyRecon(rs.getString("already_recon"));
				reconSummaryDTO.setDeemedAccepted(rs.getString("deemed_accepted"));
				reconSummaryDTO.setRemark(rs.getString("remarks"));
				reconSummaryDTO.setReconDate(dt1.format(date));
				reconSummaryDTO.setStatus(rs.getString("status"));
				summaryList.add(reconSummaryDTO);
			}

		} catch (Exception e) {
			log.error("Exception while getting list of NPCI recon summary : " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
		return summaryList;
	}
	
	public static List<ReconSummaryDTO> getNPCIReconSummaryByDate(Connection conn, String fromDate, String toDate) {
		//Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		
		log.info("getNPCIReconSummaryByDate() ");
		String getRecon861SumaryQuery = "SELECT * FROM npci_recon_summary WHERE recon_date between ? AND ? order by id desc";
		List<ReconSummaryDTO> summaryList = new ArrayList<ReconSummaryDTO>();
		try {
			log.info("Query of getNPCIReconSummaryByDate :::: " + getRecon861SumaryQuery);
			pstmt = conn.prepareStatement(getRecon861SumaryQuery);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, toDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReconSummaryDTO reconSummaryDTO = new ReconSummaryDTO();
				//txn.setRec_date(rs.getDate("recon_date"));
				Date date = dt.parse(rs.getString("recon_date")); 
				reconSummaryDTO.setFileName(rs.getString("file_name"));
				reconSummaryDTO.setReconCycle(rs.getString("recon_cycle"));
				reconSummaryDTO.setTotalFileRecord(rs.getString("total_file_rec"));
				reconSummaryDTO.setTotalMatchRecord(rs.getString("total_match_rec"));
				reconSummaryDTO.setTotalMismatchRecord(rs.getString("total_mismatch_rec"));
				reconSummaryDTO.setAlreadyRecon(rs.getString("already_recon"));
				reconSummaryDTO.setDeemedAccepted(rs.getString("deemed_accepted"));
				reconSummaryDTO.setRemark(rs.getString("remarks"));
				reconSummaryDTO.setReconDate(dt1.format(date));
				reconSummaryDTO.setStatus(rs.getString("status"));
				summaryList.add(reconSummaryDTO);
			}

		} catch (Exception e) {
			log.error("Exception while getting list of NPCI recon summary : " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
		return summaryList;
	}
	
	public ResponseDTO getDateList(Connection conn)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		try
		{
			log.info("getDateList --> ");
			responseDTO.setStatus(ResponseDTO.success);
			//responseDTO.setMessage(ReconErrorCode.RECONBU000.getErrorMessage());
			//responseDTO.setErrorCode(ReconErrorCode.RECONBU000.name());
			responseDTO.setDateList(ReconDatesDAO.reconDateList(conn));
			return responseDTO;
		}
			
		catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}
	
	public ResponseDTO getSkippedDateList(Connection conn)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		try
		{
			log.info("getDateList --> ");
			responseDTO.setStatus(ResponseDTO.success);
			//responseDTO.setMessage(ReconErrorCode.RECONBU000.getErrorMessage());
			//responseDTO.setErrorCode(ReconErrorCode.RECONBU000.name());
			responseDTO.setSkippedList(new ReconDatesDAO().reconSkippedDateList(conn));
			return responseDTO;
		}
			
		catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}
	
	public ResponseDTO getRevokedDateList(Connection conn)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		try
		{
			log.info("getDateList --> ");
			responseDTO.setStatus(ResponseDTO.success);
			//responseDTO.setMessage(ReconErrorCode.RECONBU000.getErrorMessage());
			//responseDTO.setErrorCode(ReconErrorCode.RECONBU000.name());
			responseDTO.setRevokedList(new ReconDatesDAO().reconRevokedDateList(conn));
			return responseDTO;
		}
			
		catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}
	
	public ResponseDTO addSkippedCycle(Recon861DTO recon861DTO, Connection conn)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		if(isCycleProcessed(recon861DTO.getReconDate(), recon861DTO.getReconCycle(), conn) ){
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(ReconErrorCode.RECONBU008.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU008.name());
			//responseDTO.setDateList(ReconDatesDao.reconDateList());	
			return responseDTO;
		}
		
		if(new ReconDatesDAO().insertCycle(recon861DTO.getReconDate(), recon861DTO.getReconCycle(), "skipped", conn).equals(ResponseDTO.success))
		{
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(ReconErrorCode.RECONBU000.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU000.name());
			//responseDTO.setDateList(ReconDatesDao.reconDateList());	
			return responseDTO;
		}
		
		else if(new ReconDatesDAO().isDateCycleBlank(conn)){
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(ReconErrorCode.RECONBU009.getErrorMessage()+latestDate(conn));
			responseDTO.setErrorCode(ReconErrorCode.RECONBU009.name());
			return responseDTO;
		}
		
		responseDTO.setStatus(ResponseDTO.failure);
		
		return responseDTO;	
		
	}
	
	public ResponseDTO deleteSkippedCycle(Recon861DTO recon861DTO, Connection conn)
	{
		ResponseDTO responseDTO = new ResponseDTO(); 
		
		if(isCycleRevoked(recon861DTO.getReconDate(), recon861DTO.getReconCycle(), conn) ){
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(ReconErrorCode.RECONBU0017.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU0017.name());
			//responseDTO.setDateList(ReconDatesDao.reconDateList());	
			return responseDTO;
		}
		
		
		if(new ReconDatesDAO().deleteSkippedCycle(recon861DTO.getReconDate(), recon861DTO.getReconCycle(), conn).equals(ResponseDTO.success)){
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(ReconErrorCode.RECONBU000.getErrorMessage());
			responseDTO.setErrorCode(ReconErrorCode.RECONBU000.name());
			//responseDTO.setDateList(ReconDatesDao.reconDateList());	
			return responseDTO;
		}
		//else{
		responseDTO.setStatus(ResponseDTO.failure);
		responseDTO.setMessage(ReconErrorCode.RECONBU0014.getErrorMessage());
		responseDTO.setErrorCode(ReconErrorCode.RECONBU0014.name());
		//}
		
		return responseDTO;
	}


	public static String getExceptionView(String fileName, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}
	
		
}
