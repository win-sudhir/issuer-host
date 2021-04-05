package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.dto.AuditTrailDTO;
import com.winnovature.utils.DatabaseManager;

public class AuditDAO {
	static Logger log = Logger.getLogger(AuditDAO.class.getName());
	
	public static List<AuditTrailDTO> getAuditLogOnLoad(Connection conn) {
		//Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		log.info("getAuditLogOnLoad() ");
		String auditQuery = "SELECT * FROM audit_trail order by audit_id desc limit 5";
		List<AuditTrailDTO> summaryList = new ArrayList<AuditTrailDTO>();
		try {
			//conn = DatabaseManager.getConnection();
			pstmt = conn.prepareStatement(auditQuery);
			log.info("auditQuery :::: " + auditQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AuditTrailDTO auditTrailDTO = new AuditTrailDTO();
				auditTrailDTO.setAuditId(rs.getString("audit_id"));
				auditTrailDTO.setUserBy(rs.getString("user_id"));
				auditTrailDTO.setActionName(rs.getString("module_id"));
				auditTrailDTO.setActionDesc(rs.getString("action_desc"));
				auditTrailDTO.setDateTime(rs.getString("date_time"));
				auditTrailDTO.setEntityId(rs.getString("entity_id"));
				auditTrailDTO.setUserData(rs.getString("user_data"));
				auditTrailDTO.setIpAddress(rs.getString("ip_address"));
				summaryList.add(auditTrailDTO);
			}

		} catch (Exception e) {
			log.error("Exception while getting list of audit : " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return summaryList;
	}
	
	public static List<AuditTrailDTO> getAuditLogByDate(Connection conn, String fromDate, String toDate) {
		//Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		log.info("getAuditLogByDate() ");
		String getRecon861SumaryQuery = "SELECT * FROM audit_trail WHERE date_time between ? AND ? order by audit_id desc";
		List<AuditTrailDTO> summaryList = new ArrayList<AuditTrailDTO>();
		try {
			log.info("Query of getAuditLogByDate :::: " + getRecon861SumaryQuery);
			pstmt = conn.prepareStatement(getRecon861SumaryQuery);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, toDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AuditTrailDTO auditTrailDTO = new AuditTrailDTO();
				auditTrailDTO.setAuditId(rs.getString("audit_id"));
				auditTrailDTO.setUserBy(rs.getString("user_id"));
				auditTrailDTO.setActionName(rs.getString("module_id"));
				auditTrailDTO.setActionDesc(rs.getString("action_desc"));
				auditTrailDTO.setDateTime(rs.getString("date_time"));
				auditTrailDTO.setEntityId(rs.getString("entity_id"));
				auditTrailDTO.setUserData(rs.getString("user_data"));
				auditTrailDTO.setIpAddress(rs.getString("ip_address"));
				summaryList.add(auditTrailDTO);
			}

		} catch (Exception e) {
			log.error("Exception while getting list of audit : " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return summaryList;
	}
}
