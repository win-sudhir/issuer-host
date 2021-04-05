package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

//import com.netc.utils.DBConnection;

public class TagRegChargesDAO 
{
	static Logger log = Logger.getLogger(TagRegChargesDAO.class.getName());
	
	public JSONObject getTagAllocationCharges(String vc_id) 
	{
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONObject charges = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();//DatabaseConnectionManager.getConnection();
			query = "SELECT * from tbl_tag_allocation_charges where tag_class=?";
			ps = con.prepareStatement(query);
			ps.setString(1,vc_id);
			rs = ps.executeQuery();
			if (rs.next()) {
				charges = new JSONObject();
				charges.put("issuanceCharge", rs.getString("issuance_charge"));
				
				charges.put("securityCharge", rs.getString("security_charges"));
				
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeConnection(con);
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			
		}
		return charges;
	}
	
	public String getVehicleClass(String vehicleNumber) 
	{
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();
			query = "SELECT tag_class_id FROM customer_vehicle_info WHERE vehicle_number=?";
			ps = con.prepareStatement(query);
			ps.setString(1,vehicleNumber);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				log.info("Tag class id for vehicle is :: "+vehicleNumber+" tag class id :: "+rs.getString("tag_class_id"));
				return rs.getString("tag_class_id");
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeConnection(con);
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			
		}
		return null;
	}
}
