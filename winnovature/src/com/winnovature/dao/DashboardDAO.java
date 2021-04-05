package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.dto.DashboardDTO;
import com.winnovature.dto.DashboardDTO.DayWise;
import com.winnovature.dto.DashboardDTO.MonthWise;
import com.winnovature.dto.MonthWiseTxnDTO;
import com.winnovature.dto.MonthWiseTxnDTO.April;
import com.winnovature.dto.MonthWiseTxnDTO.August;
import com.winnovature.dto.MonthWiseTxnDTO.December;
import com.winnovature.dto.MonthWiseTxnDTO.February;
import com.winnovature.dto.MonthWiseTxnDTO.January;
import com.winnovature.dto.MonthWiseTxnDTO.July;
import com.winnovature.dto.MonthWiseTxnDTO.June;
import com.winnovature.dto.MonthWiseTxnDTO.March;
import com.winnovature.dto.MonthWiseTxnDTO.May;
import com.winnovature.dto.MonthWiseTxnDTO.November;
import com.winnovature.dto.MonthWiseTxnDTO.October;
import com.winnovature.dto.MonthWiseTxnDTO.September;
import com.winnovature.utils.DatabaseManager;

public class DashboardDAO {
	static Logger log = Logger.getLogger(DashboardDAO.class.getName());

	public DashboardDTO getDashboardDetails() {
		DashboardDTO dashboardDTO = new DashboardDTO();

		List<DayWise> dayWise = getDayWiseTxnDetails();

		List<MonthWise> monthWise = getMonthWiseTxnDetails();

		dashboardDTO.setDayWise(dayWise);
		dashboardDTO.setMonthWise(monthWise);

		log.info("dashboardDTO >>>> " + dashboardDTO.toString());
		return dashboardDTO;
	}

	public List<DayWise> getDayWiseTxnDetails() {
		LocalDate currentDate = LocalDate.now();
		LocalDate minus30Date = LocalDate.now().minusDays(30);
		List<DayWise> dayWise = new ArrayList<DayWise>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseManager.getConnection();
			if (conn == null) {
				// TODO Pass Error Response Message & Error Code
				return null;
			}
			String dayWiseTxnDetails = "select count(*) as txnCount, sum(txn_amount) as amount, day(txn_timestamp) as day  from req_pay_master "
					+ "where txn_timestamp BETWEEN ? AND ? group by day(txn_timestamp) ";
			ps = conn.prepareStatement(dayWiseTxnDetails);
			ps.setString(1, minus30Date + " 00:00:00");
			ps.setString(2, currentDate + " 23:59:59");

			rs = ps.executeQuery();

			while (rs.next()) {

				DayWise day = new DayWise();

				day.setDate(rs.getString("day"));
				day.setTxnAmount(rs.getString("amount"));
				day.setTxnCount(rs.getString("txnCount"));

				dayWise.add(day);
			}

			log.info("dayList " + dayWise.toString());

		} catch (Exception e) {
			log.error("Getting Error   :::   getDayWiseTxnDetails() :: ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}

		return dayWise;
	}

	public List<MonthWise> getMonthWiseTxnDetails() {
		LocalDate currentDate = LocalDate.now();
		LocalDate minus12Month = LocalDate.now().minusMonths(12);
		List<MonthWise> monthWise = new ArrayList<MonthWise>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DatabaseManager.getConnection();
			if (conn == null) {
				// TODO Pass Error Response Message & Error Code
				return null;
			}
			String dayWiseTxnDetails = "select count(txn_id) as txnCount, sum(txn_amount) as amount, month(txn_timestamp) as month  from req_pay_master "
					+ "where txn_timestamp BETWEEN ? AND ? group by month(txn_timestamp);";
			ps = conn.prepareStatement(dayWiseTxnDetails);
			ps.setString(1, minus12Month + " 00:00:00");
			ps.setString(2, currentDate + " 23:59:59");

			rs = ps.executeQuery();

			while (rs.next()) {

				MonthWise month = new MonthWise();

				month.setMonth(rs.getString("month"));
				month.setTxnAmount(rs.getString("amount"));
				month.setTxnCount(rs.getString("txnCount"));

				monthWise.add(month);
			}

			log.info("dayList " + monthWise.toString());

		} catch (Exception e) {
			log.error("Getting Error   :::   getMonthWiseTxnDetails :: ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}

		return monthWise;

	}

	public List<MonthWiseTxnDTO> getMonthWiseTxnDetails(String userId) {
		List<MonthWiseTxnDTO> monthwiseLst = new ArrayList<MonthWiseTxnDTO>();
		if(userId.equalsIgnoreCase("admin")) {
			
			MonthWiseTxnDTO monthwiseInfo = new MonthWiseTxnDTO();

			January jan = new January();
			monthwiseInfo.setJan(jan);

			February feb = new February();
			feb.setTotTxnCnt(100);
			feb.setTotTxnAmount(50000.00);
			monthwiseInfo.setFeb(feb);

			March march = new March();
			march.setTotTxnCnt(80);
			march.setTotTxnAmount(45000.00);
			monthwiseInfo.setMar(march);

			April april = new April();
			monthwiseInfo.setApr(april);

			May may = new May();
			monthwiseInfo.setMay(may);

			June june = new June();
			monthwiseInfo.setJun(june);

			July july = new July();
			monthwiseInfo.setJul(july);

			August aug = new August();
			monthwiseInfo.setAug(aug);

			September sept = new September();
			monthwiseInfo.setSept(sept);

			October oct = new October();
			monthwiseInfo.setOct(oct);

			November nov = new November();
			monthwiseInfo.setNov(nov);

			December dec = new December();
			monthwiseInfo.setDec(dec);

			monthwiseLst.add(monthwiseInfo);
			
		}else {
			
			MonthWiseTxnDTO monthwiseInfo = new MonthWiseTxnDTO();

			January jan = new January();
			monthwiseInfo.setJan(jan);

			February feb = new February();
			feb.setTotTxnCnt(50);
			feb.setTotTxnAmount(25000.00);
			monthwiseInfo.setFeb(feb);

			March march = new March();
			march.setTotTxnCnt(35);
			march.setTotTxnAmount(30000.00);
			monthwiseInfo.setMar(march);

			April april = new April();
			monthwiseInfo.setApr(april);

			May may = new May();
			monthwiseInfo.setMay(may);

			June june = new June();
			monthwiseInfo.setJun(june);

			July july = new July();
			monthwiseInfo.setJul(july);

			August aug = new August();
			monthwiseInfo.setAug(aug);

			September sept = new September();
			monthwiseInfo.setSept(sept);

			October oct = new October();
			monthwiseInfo.setOct(oct);

			November nov = new November();
			monthwiseInfo.setNov(nov);

			December dec = new December();
			monthwiseInfo.setDec(dec);

			monthwiseLst.add(monthwiseInfo);
		}
		

		return monthwiseLst;
	}
}
