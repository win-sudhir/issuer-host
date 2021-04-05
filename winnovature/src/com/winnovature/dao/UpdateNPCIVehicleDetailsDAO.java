package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDetailsDTO;
import com.winnovature.utils.DatabaseManager;


public class UpdateNPCIVehicleDetailsDAO {
	static Logger log = Logger.getLogger(UpdateNPCIVehicleDetailsDAO.class.getName());

	public boolean updateVehicleDetails(String custId, String vehicleNumber, String tagClassId, int isCommercial,
			String vehicleClassId, String tid, String vehicleId, String oldVehicleNumber) {
		boolean isUpdate = false;
		Connection con = null;
		PreparedStatement ps1 = null, ps2 = null, ps3 = null, ps4 = null;
		// String sql1 = "UPDATE customer_vehicle_master SET tag_class_id = ?, vehicle_class_id = ?, is_commercial=?, vehicle_number=? WHERE cust_id=? and vehicle_id=?";
		String sql1 = "UPDATE customer_vehicle_info SET tag_class_id = ?, vehicle_class_id = ?, is_commercial=?, vehicle_number=?, rodt=? WHERE user_id=? and vehicle_id=?";
		String sql2 = "UPDATE vehicle_tag_linking SET tag_class_id = ?, vehicle_number=? WHERE customer_id = ? and tid = ?";
		String sql3 = "UPDATE vehicle_document_info SET vehicle_number = ? WHERE vehicle_id = ?";
		// challan master update
		String sql4 = "UPDATE challan_master SET vehicle_number=? WHERE tid=?";
		//String sql5 = "UPDATE tbl_vehicle_walletlinking_master SET vehicle_id=? WHERE tid=? AND vehicle_id=?";

		try {
			con = DatabaseManager.getConnection();
			con.setAutoCommit(false);

			ps1 = con.prepareStatement(sql1);
			ps1.setString(1, tagClassId);
			ps1.setString(2, vehicleClassId);
			ps1.setInt(3, isCommercial);
			ps1.setString(4, vehicleNumber);
			ps1.setString(5, oldVehicleNumber);
			ps1.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			ps1.setString(7, custId);
			ps1.setString(8, vehicleId);
			int i = ps1.executeUpdate();

			ps2 = con.prepareStatement(sql2);
			ps2.setString(1, tagClassId);
			ps2.setString(2, vehicleNumber);
			ps2.setString(3, custId);
			ps2.setString(4, tid);
			int j = ps2.executeUpdate();

			ps3 = con.prepareStatement(sql3);
			ps3.setString(1, vehicleNumber);
			ps3.setString(2, vehicleId);
			int k = ps3.executeUpdate();

			ps4 = con.prepareStatement(sql4);
			ps4.setString(1, vehicleNumber);
			ps4.setString(2, tid);
			int l = ps4.executeUpdate();
			
			if (i > 0 && j > 0 && k > 0 && l > 0) {
				con.commit();
				log.info("Vehicle details updated successfully .........");
				return true;
			} else
				return false;
		} catch (Exception e) {
			log.info("CustomerDao.java Getting exception while update vehicle details ..." + e);
		} finally {
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps3);
			DatabaseManager.closePreparedStatement(ps4);
			DatabaseManager.closeConnection(con);
		}
		return isUpdate;
	}
	
	
	//unused
	public String getVehicleId(String tid) 
	{
		String vehicleId = "NA";
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		String query = null;
		try {
			con = DatabaseManager.getConnection();
			query = "select vehicle_id from vehicle_tag_linking where tid  = ? ";
			ps = con.prepareStatement(query);
			ps.setString(1, tid);

			rs = ps.executeQuery();

			if (rs.next()) {
				vehicleId = rs.getString("vehicle_id");
			}
			log.info("VehicleId :: " + vehicleId);

		} catch (Exception e) {
			log.error("Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closeConnection(con);
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);

		}
		return vehicleId;
	}
	
	
	
	public boolean isVehicleNumberPresent(String vehicleNumber)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();
			String sql = "SELECT vehicle_number FROM vehicle_tag_linking where vehicle_number=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, vehicleNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return false;
	}
	//unused
	public JSONObject getVehicleDetails(String tagId)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject details = new JSONObject();
		try {
			conn = DatabaseManager.getConnection();
			String sql = "SELECT cv.vehicle_number, cv.is_commercial, cv.vehicle_class_id  FROM vehicle_tag_linking vt, customer_vehicle_master cv WHERE vt.vehicle_number=cv.vehicle_number and vt.tag_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, tagId);
			rs = ps.executeQuery();
			if (rs.next()) {
				details.put("vehicleNumber", rs.getString("vehicle_number"));
				details.put("vehicleClassId", rs.getString("vehicle_class_id"));
				details.put("isCommercial", "F");
				if(rs.getString("is_commercial").equals("1"))
					details.put("isCommercial", "T");
				return details;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return details;
	}
	
	public String getTAgClassID(String vehicleClassId) {
		String tagClassID = null;
        PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		String query = null;
		try {
			con = DatabaseManager.getConnection();
			if (con != null) {
				query = "select tag_class_id from vehicle_class_master where id  = ? ";
				ps = con.prepareStatement(query);
				ps.setString(1, vehicleClassId);

				rs = ps.executeQuery();

				if (rs.next()) {
					tagClassID = rs.getString("tag_class_id");

				}
				log.info("add_vehicleDao.java getTAgClassID() return tag_class_id :: " + tagClassID
						+ "  against the id vehicleClassId ::  " + vehicleClassId + " from vehicle_class_master");

			} else {
				log.info("add_vehicleDao.java connection is null");
			}
		} catch (Exception e) {
			log.error("Gerring Error   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);
		}
		return tagClassID;
	}
	
	public static List<VehicleDetailsDTO> getVehicleDetails() {
		List<VehicleDetailsDTO> lst = new ArrayList<VehicleDetailsDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();
			String sql = "SELECT c.vehicle_id, c.vehicle_number,c.user_id,c.is_commercial,c.vehicle_class_id,c.tag_class_id, v.tid,v.tag_id,v.barcode_data FROM customer_vehicle_info c, vehicle_tag_linking v WHERE c.vehicle_number=v.vehicle_number and c.user_id=v.customer_id";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next())
			{
				VehicleDetailsDTO vehicleDetail = new VehicleDetailsDTO();
				vehicleDetail.setVehicleId(rs.getString("vehicle_id"));
				vehicleDetail.setCustomerId(rs.getString("user_id"));
				vehicleDetail.setVehicleNumber(rs.getString("vehicle_number"));
				vehicleDetail.setTagId(rs.getString("tag_id"));
				vehicleDetail.setTid(rs.getString("tid"));
				vehicleDetail.setTagClassId(rs.getString("tag_class_id"));
				vehicleDetail.setVehicleClassId(rs.getString("vehicle_class_id"));
				vehicleDetail.setIsCommercial(rs.getString("is_commercial"));
				vehicleDetail.setBarcodeData(rs.getString("barcode_data"));
				
				lst.add(vehicleDetail);
			}
			return lst;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
		}
	
		return lst;
	}

	public static ResponseDTO updateVehicleDetals(VehicleDetailsDTO vehicleDetailsDTO, Connection conn, String userId) {
		log.info("In updateVehicleDetals :: "+vehicleDetailsDTO.toString());
		ResponseDTO responseDTO = new ResponseDTO();
		PreparedStatement ps1 = null, ps2 = null, ps3 = null, ps4 = null;
		String sql1 = "UPDATE customer_vehicle_info SET tag_class_id = ?, vehicle_class_id = ?, is_commercial=?, vehicle_number=?, rodt=? WHERE user_id=? and vehicle_id=?";
		String sql2 = "UPDATE vehicle_tag_linking SET tag_class_id = ?, vehicle_number=? WHERE customer_id = ? and tid = ?";
		String sql3 = "UPDATE vehicle_document_info SET vehicle_number = ? WHERE vehicle_id = ?";
		String sql4 = "UPDATE challan_master SET vehicle_number=? WHERE tid=?";

		try {
			//conn.setAutoCommit(false);
			ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, vehicleDetailsDTO.getTagClassId());
			ps1.setString(2, vehicleDetailsDTO.getVehicleClassId());
			ps1.setString(3, vehicleDetailsDTO.getIsCommercial());
			ps1.setString(4, vehicleDetailsDTO.getVehicleNumber());
			ps1.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			ps1.setString(6, vehicleDetailsDTO.getCustomerId());
			ps1.setString(7, vehicleDetailsDTO.getVehicleId());
			int i = ps1.executeUpdate();

			ps2 = conn.prepareStatement(sql2);
			ps2.setString(1, vehicleDetailsDTO.getTagClassId());
			ps2.setString(2, vehicleDetailsDTO.getVehicleNumber());
			ps2.setString(3, vehicleDetailsDTO.getCustomerId());
			ps2.setString(4, vehicleDetailsDTO.getTagId());
			int j = ps2.executeUpdate();

			ps3 = conn.prepareStatement(sql3);
			ps3.setString(1, vehicleDetailsDTO.getVehicleNumber());
			ps3.setString(2, vehicleDetailsDTO.getVehicleId());
			int k = ps3.executeUpdate();

			ps4 = conn.prepareStatement(sql4);
			ps4.setString(1, vehicleDetailsDTO.getVehicleNumber());
			ps4.setString(2, vehicleDetailsDTO.getTid());
			int l = ps4.executeUpdate();
			responseDTO.setStatus(ResponseDTO.failure);
			if (i > 0 && j > 0 && k > 0 && l > 0) {
				log.info("Vehicle details updated successfully .........");
				responseDTO.setStatus(ResponseDTO.success);
			} 
			return responseDTO;
		} catch (Exception e) {
			log.info("CustomerDao.java Getting exception while update vehicle details ..." + e);
		} finally {
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps3);
			DatabaseManager.closePreparedStatement(ps4);
			//DatabaseManager.closeConnection(conn);
		}
		return responseDTO;
	}

	
}
