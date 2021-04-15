package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class RegenerateChallanDAO {
	static Logger log = Logger.getLogger(RegenerateChallanDAO.class.getName());

	public static JSONObject getChallanData(String vehicleNumber, Connection conn) {
		ResultSet rs = null;
		PreparedStatement st = null;
		String sql = null;
		JSONObject obj = new JSONObject();
		try {

			sql = "select cm.challan_id,cm.tid,cm.tag_id,cm.engine_number,cm.chassis_number,cm.created_date,vtl.barcode_data from challan_master cm ,vehicle_tag_linking vtl where cm.vehicle_number=vtl.vehicle_number and cm.vehicle_number=?";
			st = conn.prepareStatement(sql);
			st.setString(1, vehicleNumber);
			rs = st.executeQuery();
			if (rs.next()) {
				obj.put("challanId", rs.getString("challan_id"));
				obj.put("tid", rs.getString("tid"));
				obj.put("tagId", rs.getString("tag_id"));
				// obj.put("vehicle_number", rs.getString("vehicle_number"));
				obj.put("engineNumber", rs.getString("engine_number"));
				obj.put("chassisNumber", rs.getString("chassis_number"));
				obj.put("vehStatus", "present");
				obj.put("createdDate", rs.getString("created_date"));
				obj.put("barCode", rs.getString("barcode_data"));
			} else {
				obj.put("vehStatus", "nf");
			}
			return obj;

		} catch (Exception e) {
			log.info("Exception in getChallanData :: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeStatement(st);
		}
		return null;
	}
}
