package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.constants.WINConstants;
import com.winnovature.dto.VehicleDTO;
import com.winnovature.dto.VehicleDTO.Vehicles;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class VehicleDAO {
	static Logger log = Logger.getLogger(VehicleDAO.class.getName());

	public static void addVehicle(Connection conn, VehicleDTO vehicleDTO, String customerId) {

		String query = "INSERT INTO customer_vehicle_info (vehicle_id, vehicle_number, user_id, vehicle_class_id, tag_class_id, engine_number, chassis_number, is_commercial, created_on, status) "
				+ "VALUES (?,?,?,?,?,?,?, ?, ?,?) ";
		String query1 = "INSERT INTO vehicle_document_info (vehicle_id, vehicle_number, path_rc_book, path_insurance, path_front_pic, path_back_pic, status) "
				+ "VALUES (?,?,?,?,?,?,?) ";
		
		PreparedStatement ps = null, ps1=null;
		try {
			for (Vehicles vehicle : vehicleDTO.getVehicles()) {
				String vehicleId = new SimpleDateFormat("yyMMddSSSS").format(new Date());
				
				vehicle.setPathRcBook(vehicle.getRcBookDoc());
				vehicle.setPathInsurance(vehicle.getInsuranceDoc());
				vehicle.setPathFrontPic(vehicle.getFrontPicDoc());
				vehicle.setPathBackPic(vehicle.getBackPicDoc());
				
				ps = conn.prepareStatement(query);
				ps.setString(1, vehicleId);
				ps.setString(2, vehicle.getVehicleNumber());
				ps.setString(3, customerId);
				ps.setString(4, vehicle.getVehicleClassId());
				ps.setString(5, vehicle.getVehicleClassId());
				ps.setString(6, vehicle.getEngineNumber());
				ps.setString(7, vehicle.getChassisNumber());
				ps.setString(8, vehicle.getIsCommercial());
				ps.setString(9, new DateUtils().getCurrnetDate());
				ps.setString(10, WINConstants.ACTIVE);
				ps.executeUpdate();
				
				ps1 = conn.prepareStatement(query1);
				ps1.setString(1, vehicleId);
				ps1.setString(2, vehicle.getVehicleNumber());
				ps1.setString(3, vehicle.getPathRcBook());
				ps1.setString(4, vehicle.getPathInsurance());
				ps1.setString(5, vehicle.getPathFrontPic());
				ps1.setString(6, vehicle.getPathBackPic());
				ps1.setString(7, WINConstants.ACTIVE);
				ps1.executeUpdate();
			}

			
		} catch (Exception e) {
			log.error("Exception in VehicleDAO :: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closePreparedStatement(ps1);
		}
	}
	
	
	public List<Vehicles> getVehicleById(Connection conn, String customerId) {
		List<Vehicles> lst = new ArrayList<Vehicles>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "SELECT * FROM customer_vehicle_info WHERE user_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			
			rs = ps.executeQuery();
			Vehicles vehicle = null;
			while (rs.next()) {
				//vehicle_id, vehicle_number, user_id, vehicle_class_id, tag_class_id, engine_number, chassis_number, is_commercial, created_on, status, rodt
				vehicle = new Vehicles();
				vehicle.setVehicleId(rs.getString("vehicle_id"));
				vehicle.setVehicleNumber(rs.getString("vehicle_number"));
				vehicle.setUserId(customerId);
				vehicle.setVehicleClassId(rs.getString("vehicle_class_id"));
				vehicle.setTagClassId(rs.getString("tag_class_id"));
				vehicle.setEngineNumber(rs.getString("engine_number"));
				vehicle.setChassisNumber(rs.getString("chassis_number"));
				vehicle.setIsCommercial(rs.getString("is_commercial"));
				lst.add(vehicle);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getVehicleById()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;
	}
	
	public List<Vehicles> getVehicleByIdForChecker(Connection conn, String customerId) {
		List<Vehicles> lst = new ArrayList<Vehicles>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "SELECT * FROM customer_vehicle_info_edited WHERE user_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			
			rs = ps.executeQuery();
			Vehicles vehicle = null;
			while (rs.next()) {
				//vehicle_id, vehicle_number, user_id, vehicle_class_id, tag_class_id, engine_number, chassis_number, is_commercial, created_on, status, rodt
				vehicle = new Vehicles();
				vehicle.setVehicleId(rs.getString("vehicle_id"));
				vehicle.setVehicleNumber(rs.getString("vehicle_number"));
				vehicle.setUserId(customerId);
				vehicle.setVehicleClassId(rs.getString("vehicle_class_id"));
				vehicle.setTagClassId(rs.getString("tag_class_id"));
				vehicle.setEngineNumber(rs.getString("engine_number"));
				vehicle.setChassisNumber(rs.getString("chassis_number"));
				vehicle.setIsCommercial(rs.getString("is_commercial"));
				lst.add(vehicle);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getVehicleById()  ::  error getting   : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;
	}

	public static void updateVehicle(Connection conn, VehicleDTO vehicleDTO, String userId) {
		//vehicle_id, vehicle_number=?, vehicle_class_id=?, tag_class_id=?, engine_number=?, chassis_number=?, is_commercial=?
		String query = "UPDATE customer_vehicle_info SET vehicle_number=?, vehicle_class_id=?, tag_class_id=?, engine_number=?, chassis_number=?, is_commercial=? WHERE vehicle_id = ?";
		//String query1 = "INSERT INTO vehicle_document_info (vehicle_id, vehicle_number, path_rc_book, path_insurance, path_front_pic, path_back_pic, status) "
			//	+ "VALUES (?,?,?,?,?,?,?) ";
		
		PreparedStatement ps = null;//, ps1=null;
		try {
			for (Vehicles vehicle : vehicleDTO.getVehicles()) {
				//String vehicleId = new SimpleDateFormat("yyMMddSSSS").format(new Date());
				/*
				vehicle.setPathRcBook(vehicle.getRcBookDoc());
				vehicle.setPathInsurance(vehicle.getInsuranceDoc());
				vehicle.setPathFrontPic(vehicle.getFrontPicDoc());
				vehicle.setPathBackPic(vehicle.getBackPicDoc());
				*/
				ps = conn.prepareStatement(query);
				
				ps.setString(1, vehicle.getVehicleNumber());
				ps.setString(2, vehicle.getVehicleClassId());
				ps.setString(3, vehicle.getVehicleClassId());
				ps.setString(4, vehicle.getEngineNumber());
				ps.setString(5, vehicle.getChassisNumber());
				ps.setString(6, vehicle.getIsCommercial());
				ps.setString(7, vehicle.getVehicleId());
				ps.executeUpdate();
				
				/*
				 * ps1 = conn.prepareStatement(query1); ps1.setString(1, vehicleId);
				 * ps1.setString(2, vehicle.getVehicleNumber()); ps1.setString(3,
				 * vehicle.getPathRcBook()); ps1.setString(4, vehicle.getPathInsurance());
				 * ps1.setString(5, vehicle.getPathFrontPic()); ps1.setString(6,
				 * vehicle.getPathBackPic()); ps1.setString(7, WINConstants.ACITVE);
				 * ps1.executeUpdate();
				 */
			}

			
		} catch (Exception e) {
			log.error("Exception in VehicleDAO :: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closePreparedStatement(ps1);
		}
		
	}
	
	public static void addEditedVehicle(Connection conn, VehicleDTO vehicleDTO, String userId) {
		
		//String query = "UPDATE customer_vehicle_info SET vehicle_number=?, vehicle_class_id=?, tag_class_id=?, engine_number=?, chassis_number=?, is_commercial=? WHERE vehicle_id = ?";
		
		String query = "INSERT INTO customer_vehicle_info_edited (vehicle_number, vehicle_class_id, tag_class_id, engine_number, chassis_number, is_commercial) "
				+ "VALUES (?,?,?,?,?,?) ";
		
		PreparedStatement ps = null;
		try {
			for (Vehicles vehicle : vehicleDTO.getVehicles()) {
				
				ps = conn.prepareStatement(query);
				
				ps.setString(1, vehicle.getVehicleNumber());
				ps.setString(2, vehicle.getVehicleClassId());
				ps.setString(3, vehicle.getVehicleClassId());
				ps.setString(4, vehicle.getEngineNumber());
				ps.setString(5, vehicle.getChassisNumber());
				ps.setString(6, vehicle.getIsCommercial());
				ps.executeUpdate();
				
				
			}

			
		} catch (Exception e) {
			log.error("Exception in VehicleDAO :: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closePreparedStatement(ps1);
		}
		
	}
}
