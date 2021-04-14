package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class TagVehicleClassDAO {
	static Logger log = Logger.getLogger(TagVehicleClassDAO.class.getName());

	public String getTagClassList(Connection conn) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {

			query = "SELECT id,tag_class_id, tag_color_id from tag_class_master where is_deleted = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, "0");

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();

				jo.put("id", rs.getString("id"));
				jo.put("tagClassId", rs.getString("tag_class_id"));
				jo.put("tagColor", rs.getString("tag_color_id"));
				report.put(jo);
			}

		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		JSONObject mainObj = new JSONObject();
		mainObj.put("tagClasslist", report);
		return mainObj.toString();
	}

	public String getVehicleClassList() {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		JSONArray report = null;
		String query = null;
		try {
			con = DatabaseManager.getConnection();
			query = "SELECT class_code,tag_class_id, description from vehicle_class_master where is_deleted = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, "0");

			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();

				jo.put("vehicleClassId", rs.getString("class_code"));
				jo.put("tagClassId", rs.getString("tag_class_id"));
				jo.put("description", rs.getString("description"));
				report.put(jo);
			}

		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		JSONObject mainObj = new JSONObject();
		mainObj.put("vehicleClasslist", report);
		return mainObj.toString();
	}

}
