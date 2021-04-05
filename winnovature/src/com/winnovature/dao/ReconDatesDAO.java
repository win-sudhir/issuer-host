
package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.winnovature.dto.ReconDates;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.querries.ReconconciliationQueries;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtility;


public class ReconDatesDAO {
	
	static Logger log = Logger.getLogger(ReconDatesDAO.class.getName());
	
	
	public static ArrayList<ReconDates> reconDateList(Connection conn)
	{
		log.info("In reconDateList()");
		ArrayList<ReconDates> reconDates = new ArrayList<>();
		try {
			log.info("--------------------------------------------- ");
			String lastReconDate = new ReconDAO().latestDate(conn);// "29/11/2018";
			if (lastReconDate.equals("") || lastReconDate == null) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -2);
				lastReconDate = DateUtility.getDateFormatDate("dd/MM/yyyy", cal.getTime());//originalFormat.format(cal.getTime());
				log.info("Date = " + lastReconDate);
			}
			log.info("Last recon date :: " + lastReconDate);
			if (!ReconDAO.isCycleProcessed(lastReconDate, "1", conn)) { //1
				reconDates.add(new ReconDates(lastReconDate, "1"));
			}
			if (!ReconDAO.isCycleProcessed(lastReconDate, "2", conn)) { //2
				reconDates.add(new ReconDates(lastReconDate, "2"));
			}

			Date reconDate = DateUtility.getParsedDate("dd/MM/yyyy", lastReconDate); //originalFormat.parse(lastReconDate);
			String finalDate = DateUtility.getDateFormatDate("yyyy-MM-dd", reconDate); //newFormat.format(reconDate);

			Long l = DateUtility.getDateDiff(reconDate, new Date(), TimeUnit.DAYS);
			long days = TimeUnit.MILLISECONDS.toDays(l);
			log.info("Since Days :: " + days);

			for (int i = 1; i <= days - 1; i++) {
				String date = DateUtility.addOneDay(finalDate, i);
				Date newFormatDate = DateUtility.getParsedDate("yyyy-MM-dd", date);//newFormat.parse(date);
				String sDate = DateUtility.getDateFormatDate("dd/MM/yyyy", newFormatDate); // originalFormat.format(newFormatDate);

				reconDates.add(new ReconDates(sDate, "1"));
				reconDates.add(new ReconDates(sDate, "2"));
			}
			//log.info("--------------------------------------------- ");
			
		} catch (Exception e) {
			log.error("Exception while listing dates : " + e.getMessage());
			e.printStackTrace();
		}
		reconDates = remainingDateNCycle(conn);
		return reconDates;
		
	}
	
	public static ArrayList<ReconDates> remainingDateNCycle(Connection conn) {
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Date> dates = new ArrayList<>();
		String ondate;
		Date date = null;
		ArrayList<ReconDates> reconDates = new ArrayList<>();
		try {
			//conn = DBConnection.getConnection();
			String sql = "SELECT on_date, cycle FROM npci_recon_status";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ondate = (rs.getString("on_date"));
				date = DateUtility.getReconDate(ondate);
				dates.add(date);
			}
			if (dates.isEmpty()) {
				return reconDates;
			} else {
				for (int i = 0; i < dates.size(); i++) {
					String dateToCheck = DateUtility.getDateFormatDate("dd/MM/yyyy", dates.get(i));
					if (!ReconDAO.isCycleProcessed(dateToCheck, "1", conn)) {
						reconDates.add(new ReconDates(dateToCheck, "1"));
					}
					if (!ReconDAO.isCycleProcessed(dateToCheck, "2", conn)) {
						reconDates.add(new ReconDates(dateToCheck, "2"));
					}
					System.out.println(dates.get(i));
				}
				for (int i = 0; i < reconDates.size(); i++) {
					System.out.println("DATE : " + reconDates.get(i).getCycledate());
					System.out.println("CYCLE : " + reconDates.get(i).getCycle());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return reconDates;
	}
	
	public ArrayList<ReconDates> reconSkippedDateList(Connection conn)
	{
		log.info("In reconSkippedDateList()");
		ArrayList<ReconDates> skippedDateList = new ArrayList<>();
		//Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			String query = ReconconciliationQueries.getReconSkippedListQuery; 
					//"select on_date,cycle from npci_recon_status where file_name='skipped' ORDER BY STR_TO_DATE(on_date, '%d/%m/%Y') DESC";
			//log.info("--------------------------------------------- ");
			//conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				skippedDateList.add(new ReconDates(rs.getString("on_date"), rs.getString("cycle")));
			}
			//log.info("--------------------------------------------- ");
			
		} catch (Exception e) {
			log.error("Exception while listing skipped dates : " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
		return skippedDateList;
	}
	
	public ArrayList<ReconDates> reconRevokedDateList(Connection conn)
	{
		log.info("In reconRevokedDateList()");
		ArrayList<ReconDates> revokedDateList = new ArrayList<>();
		//Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			//public static String getReconRevokedListQuery = "select on_date,cycle from bank_recon_status ORDER BY STR_TO_DATE(on_date, '%d/%m/%Y') DESC";
			String query = ReconconciliationQueries.getReconRevokedListQuery; 
			//log.info("--------------------------------------------- ");
			log.info("Query :: "+query);
			//conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				revokedDateList.add(new ReconDates(rs.getString("on_date"), rs.getString("cycle")));
			}
			//log.info("--------------------------------------------- ");
			
		} catch (Exception e) {
			log.error("Exception while listing reconRevokedDateList : " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
		return revokedDateList;
	}
	
	public String deleteSkippedCycle(String reconDate, String reconCycle, Connection conn) 
	{
		//log.info("In deleteSkippedCycle()---->");
		log.info("deleteSkippedCycle :: reconDate : " + reconDate + " reconCycle : " + reconCycle);
		//Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		int i = 0;
		String sql = ReconconciliationQueries.getDeleteSkippedCycleQuery;
				// public static String getDeleteSkippedCycleQuery = "DELETE FROM npci_recon_status where on_date=? and cycle=?";
		String sql1 = ReconconciliationQueries.getInsertRevokedCycleQuery;
				//public static String getInsertSkippedCycleQuery = "INSERT INTO bank_recon_status(on_date, cycle,file_name) VALUES(?,?,?)";
		try {
			/*conn = DatabaseManager.getConnection();
			conn.setAutoCommit(false);*/
			pstmt = conn.prepareStatement(sql);
			//pstmt.setString(1, "revoked");
			pstmt.setString(1, reconDate);
			pstmt.setString(2, reconCycle);
			pstmt.executeUpdate();
			//log.info("J="+j);
			//int i = pstmt.executeUpdate();
			log.info("deleteSkippedCycle :: Query :: "+sql1);
			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setString(1, reconDate);
			pstmt1.setString(2, reconCycle);
			pstmt1.setString(3, "revoked");
			
			i=pstmt1.executeUpdate();
			log.info("I="+i);
			if(i>0){
				//conn.commit();
				log.info("IIIIIIIIIII="+i);
				return ResponseDTO.success;
			}
			log.info("Cycle is revoked successfully...........");
			return ResponseDTO.failure;
						
		} catch (SQLException e) {
			log.error(e.getMessage());
			return ResponseDTO.failure;
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			DatabaseManager.closePreparedStatement(pstmt1);
			//DatabaseManager.closeConnection(conn);
		}
	}
	
	
	/*public boolean checkDate(String reconDate, int reconCycle) {
		return ReconDao.validatePreviousCycle(reconDate, reconCycle);
		//return chkdate;
	}*/
	
	public String insertCycle(String reconDate, String reconCycle, String fileName, Connection conn) 
	{
		log.info("In insertCycle()---->");
		//public static String getInsertSkippedCycleQuery = "INSERT INTO npci_recon_status(on_date, cycle,file_name) VALUES(?,?,?)";
		String sql = ReconconciliationQueries.getInsertSkippedCycleQuery;
		//Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			//conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reconDate);
			pstmt.setString(2, reconCycle);
			pstmt.setString(3, "skipped");
			int i = pstmt.executeUpdate();
			if(i>0){
				return ResponseDTO.success;
			}
			return ResponseDTO.failure;
		} catch (SQLException e) {
			log.error(e.getMessage());
			return ResponseDTO.failure;
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
	}
	
	public boolean isDateCycleBlank(Connection conn) {
		log.info("In isDateCycleBlank()");
		//Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			//public static String getCheckCycleBlankQuery = "SELECT on_date, cycle FROM npci_recon_status";
			String sql = ReconconciliationQueries.getCheckCycleBlankQuery;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (!rs.isBeforeFirst()) {
				return true;
			}
			
		} catch (Exception e) {
			log.error("isDateCycleBlank() Exception while checking cycle and date : ");
			return false;
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
			//DatabaseManager.closeConnection(conn);
		}
		return false;
	}
	
	/*public static void main(String[] args) {
		//System.out.println(reconDateList());
		ArrayList<ReconDates> list = reconDateList();
		for(int i=0; i<list.size();i++){
			System.out.println("listd "+reconDateList().get(i).getCycledate());
			System.out.println("listc "+reconDateList().get(i).getCycle());
		}
		List<ReconDateDTO> l = reconDateList1();
		//System.out.println(reconDateList1());
		for(int i=0; i<l.size();i++){
			System.out.println("---d "+reconDateList1().get(i).getReconDate());
			System.out.println("---c "+reconDateList1().get(i).getReconCycle());
		}
	}*/
}
