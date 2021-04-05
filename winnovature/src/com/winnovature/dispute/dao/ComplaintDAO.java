package com.winnovature.dispute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.dto.ComplaintDTO;
import com.winnovature.dto.ComplaintDTO.ComplaintList;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class ComplaintDAO {
	static Logger log = Logger.getLogger(ComplaintDAO.class.getName());
	
	public static List<ComplaintList> getCompliantList(String userId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintList> lst = new ArrayList<ComplaintList>();
		String query = null;
		try {
			query = "select category_id, category_desc from tbl_grievances where status_id=1";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			ComplaintList list = null;
			while (rs.next()) {
				list = new ComplaintList();
				list.setComplaintId(rs.getString("category_id"));
				list.setComplaintDesc(rs.getString("category_desc"));
				lst.add(list);
			}
			return lst;
		} catch (Exception e) {
			log.error("Exception in getting list of complaint() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;
	}
	
	public String updateComplaintStatus(ComplaintDTO complaintDTO, String userId, Connection conn) {
        String currentDate = new DateUtils().getCurrnetDate();
        PreparedStatement ps = null;
        String sql = "UPDATE tbl_grievance_master set status = ?, admin_remark = ?, last_updated_by = ?, last_updated_on = ? where id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, ComplaintDTO.COMPLAINTCLOSE);
            ps.setString(2, complaintDTO.getClosingRemark());
            ps.setString(3, userId);
            ps.setString(4, currentDate);
            ps.setString(5, complaintDTO.getId());
            //log.info("ps.executeUpdate() :: "+ps.executeUpdate());
            if (ps.executeUpdate()>0) {
            	log.info("complaint updated successfully.");
            	return "1";
			}else {
				return "0";
			}
            
        }
        catch (Exception e) {
            log.info(("Exception in updateComplaintStatus() :: " + e.getMessage()));
            e.printStackTrace();
        }
        finally {
        	DatabaseManager.closePreparedStatement(ps);
        }
        return "0";
    }
	
	public String addComplaint(ComplaintDTO complaintDTO, String userId, Connection conn) {
		PreparedStatement ps = null;
		String currentDate = new DateUtils().getCurrnetDate();
		String sql = "INSERT INTO tbl_grievance_master (user_id, category_id, remarks, status, created_by, created_on, last_updated_by, last_updated_on)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, complaintDTO.getComplaintId());
			ps.setString(3, complaintDTO.getUserRemark());
			ps.setString(4, ComplaintDTO.COMPLAINTOPEN);
			ps.setString(5, userId);
			ps.setString(6, currentDate);
			ps.setString(7, userId);
			ps.setString(8, currentDate);
			if (ps.executeUpdate()>0) {
				log.info("Complaint added successfully.");
				return "1";
			}else {
				return "0";
			}
			
			
		} catch (Exception e) {
			log.error("Exception in adding complaint :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}
	
	public static List<ComplaintDTO> getComplaintReport(String userId, String fromDate, String toDate, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        List<ComplaintDTO> lst = new ArrayList<ComplaintDTO>();
        try {
            if (userId.equalsIgnoreCase("admin")) {
                query = "SELECT g1.*, g2.category_id, g2.category_desc FROM tbl_grievance_master g1, tbl_grievances g2 where g1.category_id=g2.category_id and g1.last_updated_on BETWEEN ? AND ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, fromDate);
                ps.setString(2, toDate);
            }
           
            else if (userId.startsWith("C")) {
                query = "SELECT g1.*, g2.category_id,g2.category_desc, c.* FROM tbl_grievance_master g1, tbl_grievances g2, customer_info c where g1.category_id=g2.category_id and c.user_id=g1.user_id and g1.user_id=? and g1.last_updated_on BETWEEN ? AND ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, userId);
                ps.setString(2, fromDate);
                ps.setString(3, toDate);
            }
            else{
                query = "SELECT g1.*, g2.category_id, g2.category_desc FROM tbl_grievance_master g1, tbl_grievances g2 where g1.category_id=g2.category_id and g1.last_updated_on BETWEEN ? AND ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, fromDate);
                ps.setString(2, toDate);
            }
            rs = ps.executeQuery();
            ComplaintDTO complaintList = null;
            while (rs.next()) {
            	complaintList = new ComplaintDTO();
            	complaintList.setId(rs.getString("id"));
            	complaintList.setUserId(rs.getString("user_id"));
            	complaintList.setComplaintDesc(rs.getString("category_desc"));
            	complaintList.setUserRemark(rs.getString("remarks"));
            	complaintList.setStatus(rs.getString("status"));
            	complaintList.setClosingRemark(rs.getString("admin_remark"));
            	complaintList.setCreatedOn(rs.getString("created_on"));
            	complaintList.setCreatedBy(rs.getString("created_by"));
                lst.add(complaintList);
            }
            return lst;
        }
        catch (Exception e) {
            e.printStackTrace();
            
        }
        finally {
        	DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
        }
       
        return lst;
    }
}
