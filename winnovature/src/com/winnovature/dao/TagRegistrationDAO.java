package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class TagRegistrationDAO {

	static Logger log = Logger.getLogger(TagRegistrationDAO.class.getName());

	/*
	 * public String getTagId(String tid, String vno) { String tagId = null;
	 * PreparedStatement ps = null; ResultSet rs = null; Connection con = null;
	 * String query = null;
	 * 
	 * try { con = DatabaseManager.getConnection();
	 * 
	 * if (con != null) { if (tid != null && !tid.isEmpty() &&
	 * !tid.equalsIgnoreCase("NA")) { query =
	 * "select tag_id from vehicle_tag_linking where tid = ?"; ps =
	 * con.prepareStatement(query); ps.setString(1, tid); rs = ps.executeQuery(); }
	 * else if (vno != null && !vno.isEmpty() && !vno.equalsIgnoreCase("NA")) {
	 * query = "select tag_id from vehicle_tag_linking where vehicle_number = ?"; ps
	 * = con.prepareStatement(query); ps.setString(1, vno); rs = ps.executeQuery();
	 * } else { System.out.
	 * println("TagAllocationDao.java ::: getTagId() :: Invalid Input Received"); }
	 * 
	 * if (rs != null && rs.next()) { tagId = rs.getString("tag_id"); }
	 * 
	 * System.out.println( "TagAllocationDao.java ::: getTagId() :: Query : '" +
	 * query + "' And Tag Id : '" + tagId + "'"); } } catch (Exception e) {
	 * System.out.println(
	 * "TagAllocationDao.java ::: getTagId() :: Error Occurred while fetching Tag Id : "
	 * + e.getMessage()); e.printStackTrace(); } finally {
	 * DatabaseManager.closeResultSet(rs);
	 * DatabaseManager.closePreparedStatement(ps);
	 * DatabaseManager.closeConnection(con);
	 * 
	 * }
	 * 
	 * return tagId; }
	 */

	/*
	 * public JSONObject getFleetDetails(String tagId) { JSONObject fleetData = new
	 * JSONObject(); ; PreparedStatement ps = null; ResultSet rs = null; Connection
	 * con = null; String query = null;
	 * 
	 * try { con = DatabaseManager.getConnection();
	 * 
	 * if (con != null) { if (tagId != null && !tagId.isEmpty() &&
	 * !tagId.equalsIgnoreCase("NA")) { query =
	 * "select am.user_id,vl.barcode_data,vl.tid from vehicle_tag_linking vl inner join account_master am on vl.customer_id = am.user_id where vl.tag_id = ?"
	 * ; ps = con.prepareStatement(query); ps.setString(1, tagId); rs =
	 * ps.executeQuery(); }
	 * 
	 * if (rs != null && rs.next()) { fleetData.put("fleetId",
	 * rs.getString("user_id")); fleetData.put("barcode",
	 * rs.getString("barcode_data")); // fleetData.put("tid", rs.getString("tid"));
	 * // log.info("TID :: "+rs.getString("tid")); System.out.
	 * println("TagAllocationDao.java ::: getFleetDetails() :: FleetId : '" +
	 * rs.getString("user_id") + "' And Barcode : '" + rs.getString("barcode_data")
	 * + "'."); }
	 * 
	 * System.out.println("TagAllocationDao.java ::: getFleetDetails() :: Query : '"
	 * + query + "' And Tag Id : '" + tagId + "'");
	 * 
	 * return fleetData; } } catch (Exception e) { System.out.
	 * println("TagAllocationDao.java ::: getFleetDetails() :: Error Occurred while fetching Tag Id : "
	 * + e.getMessage()); e.printStackTrace();
	 * 
	 * } finally { DatabaseManager.closeResultSet(rs);
	 * DatabaseManager.closePreparedStatement(ps);
	 * DatabaseManager.closeConnection(con);
	 * 
	 * }
	 * 
	 * return null; }
	 */

	public boolean isAllocated(String TID, String VehicleNumber, String min_threshold, Connection conn)// adding
	// threshold
	{
		log.info("In Allocate tag dao isAllocated...................");
		boolean check = false;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "SELECT * FROM vehicle_tag_linking where tid=?";
		
		try {

			ps = conn.prepareStatement(sql);
			ps.setString(1, TID);
			rs = ps.executeQuery();
			if (rs.next())// && rs1.next())
			{
				if (rs.getString("vehicle_number") != null) {
					if (rs.getString("tid").equals(TID) && rs.getString("vehicle_number").equals(VehicleNumber))// &&
					{
						check = true;
						log.info("TID : " + rs.getString("tid"));
						log.info("VehicleNumber : " + rs.getString("vehicle_number"));
						log.info("In IF Flag value " + check);
						// break;
					}

				} else {

					// not assign now available
					check = false;
					log.info("Outer if Flag value " + check);
				}
			}
		} catch (Exception e) {
			log.info("Error while checking inventory records : " + e.getMessage());
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return check;
	}

	// now user in TagAllocation UAT :::
	public boolean allocateTagGenerateChallan(String TID, String VehicleNumber, String min_threshold, String txnID,
			String seqNO, String amtIssuence, String amtRecharge, String amtRSA, String amtSecurity,
			String amtInsurance)// adding threshold
	{
		boolean check = false;
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		ResultSet rs1 = null;
		Statement st1 = null;
		ResultSet rs2 = null;
		Statement st2 = null;
		Statement st3 = null;
		ResultSet rs3 = null;
		PreparedStatement preparedStmt1 = null;
		PreparedStatement preparedStmt2 = null;
		PreparedStatement preparedStmt3 = null;
		PreparedStatement preparedStmt4 = null;

		String tag_id = null;
		String vehicle_number = VehicleNumber;
		String challan_id = null;
		String chassis_number = null;
		String current_date = null;
		String engine_number = null;
		String tid = TID;
		String bank_name = null;
		String cust_id = null;
		String vehicle_id = null;
		// String tbl_min_threshold = null;

		String sql = "SELECT * FROM vehicle_tag_linking";
		String sql1 = "SELECT * from customer_vehicle_info where vehicle_number='" + VehicleNumber + "'";

		log.info("In generate challan...................");

		try {
			conn = DatabaseManager.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			// rs1 = st.executeQuery(sql1);
			while (rs.next())// && rs1.next())
			{
				// if(rs.getString("vehicle_number")!=null)
				if (rs.getString("tid").equals(TID)) {
					tag_id = rs.getString("tag_id");
					log.info("Tag ID : " + tag_id);
					st1 = conn.createStatement();
					rs1 = st1.executeQuery(sql1);
					while (rs1.next())
						if (rs1.getString("vehicle_number").equals(VehicleNumber)) {
							vehicle_id = rs1.getString("vehicle_id");
							cust_id = rs1.getString("user_id");
							log.info("Customer ID : " + cust_id);
							log.info("vehicle_id : " + vehicle_id);
							vehicle_number = VehicleNumber;
							// String challan_id;
							chassis_number = rs1.getString("chassis_number");
							;
							engine_number = rs1.getString("engine_number");
							log.info("engine_number : " + engine_number);
							log.info("chassis_number : " + chassis_number);
							Date date = new Date();
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							log.info(dateFormat.format(date));// (cuurent_date));
							current_date = dateFormat.format(date);

							String iinNo = "500001";
							challan_id = iinNo + "-" + txnID + "-" + seqNO;
							log.info("current_date : " + current_date);
							log.info("Challan_id : " + challan_id);

							String bank = "select * from account_master where user_id='" + cust_id + "'";
							st2 = conn.createStatement();
							rs2 = st2.executeQuery(bank);
							if (rs2.next()) {
								if (rs2.getString("user_id").equals(cust_id)) {
									bank_name = rs2.getString("bank_name");
									log.info("bank name : " + bank_name);
									break;
								}
							}
						}
					check = true;
					break;
				} else {
					check = false;
					break;
				}
			}
			log.info("Flag value ........." + check);

			log.info("Required data>>>>" + vehicle_number + " challan_id " + challan_id + " chassis_number "
					+ chassis_number + " created date " + current_date + " engine_number " + engine_number + " " + tid
					+ " bank_name " + bank_name + " cust_id " + cust_id);
			String chl = "insert into challan_master (tid, challan_id, tag_id, bank_name, vehicle_number, engine_number, chassis_number, created_date)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
			String invsql = "update inventory_master set status = ? where tid='" + tid + "'";
			String vehsql = "update vehicle_tag_linking set vehicle_id = ?, vehicle_number = ?, customer_id = ? where tid='"
					+ tid + "'";
			preparedStmt3 = conn.prepareStatement(chl);
			preparedStmt3.setString(1, tid);
			preparedStmt3.setString(2, challan_id);
			preparedStmt3.setString(3, tag_id);
			preparedStmt3.setString(4, bank_name);
			preparedStmt3.setString(5, vehicle_number);
			preparedStmt3.setString(6, engine_number);
			preparedStmt3.setString(7, chassis_number);
			preparedStmt3.setString(8, current_date);
			preparedStmt3.executeUpdate();
			log.info("challan_master challan generated successfully......");
			preparedStmt1 = conn.prepareStatement(vehsql);
			preparedStmt1.setString(1, vehicle_id);
			preparedStmt1.setString(2, vehicle_number);
			preparedStmt1.setString(3, cust_id);
			preparedStmt1.executeUpdate();
			log.info("vehicle_tag_linking updated successfully");
			preparedStmt2 = conn.prepareStatement(invsql);
			preparedStmt2.setString(1, "4");
			preparedStmt2.executeUpdate();
			log.info("inventory_master updated successfully");
			log.info("Adding charges to customer for allocation of tag-- ");

			// calling function
			addCharges(tag_id, vehicle_number, cust_id, amtIssuence, amtRecharge, amtRSA, amtSecurity, amtInsurance);

			log.info("Charges added successfully to customer for allocation of tag-- ");
			// adding min_threshold
			/*
			 * String sql2 = "select min_threshold from customer_info where user_id='" +
			 * cust_id + "'"; st3 = conn.createStatement(); rs3 = st3.executeQuery(sql2);
			 * log.info(sql2); while (rs3.next())// min_threshold { //
			 * log.info("Getting last min threshold..."); tbl_min_threshold =
			 * rs3.getString("min_threshold"); log.info("Getting last min threshold..." +
			 * tbl_min_threshold); if (rs3.getString("min_threshold") != null)
			 * tbl_min_threshold = rs3.getString("min_threshold"); else tbl_min_threshold =
			 * "0"; log.info("Last Min Threshold : " + tbl_min_threshold); break; }
			 */
			// tbl_min_threshold = "0";
//			int threshold = Integer.valueOf(tbl_min_threshold) + Integer.valueOf(min_threshold);
//			min_threshold = Integer.toString(threshold);// Integer.toString(Integer.valueOf(tbl_min_threshold)
			// +
			// Integer.valueOf(min_threshold));
//			log.info("Threshold Total = " + min_threshold);
//			String thrupdatesql = "update customer_master set min_threshold = ? where cust_id='" + cust_id + "'";
//			preparedStmt4 = conn.prepareStatement(thrupdatesql);
//			preparedStmt4.setString(1, min_threshold);
//			preparedStmt4.executeUpdate();
//			log.info("customer_master min_threshold amount updated successfully");
		} catch (Exception e) {
			log.info("Error while generating challan : " + e.getMessage());
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs3);
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs1);
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt4);
			DatabaseManager.closePreparedStatement(preparedStmt3);
			DatabaseManager.closePreparedStatement(preparedStmt2);
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closeStatement(st3);
			DatabaseManager.closeStatement(st2);
			DatabaseManager.closeStatement(st1);
			DatabaseManager.closeStatement(st);
			DatabaseManager.closeConnection(conn);

		}

		return check;
	}

	public static void addCharges(String tag_id, String vehicle_number, String cust_id, String amtIssuence,
			String amtRecharge, String amtRSA, String amtSecurity, String amtInsurance) {

		Connection conn = null;
		PreparedStatement preparedStmt = null;

		log.info("In Add Charges...................");

		try {
			conn = DatabaseManager.getConnection();
			String chl = "insert into tag_allocation_charges (cust_id,tag_id,vehicle_id,issuance_charge, insurance_charges, road_side_assistance,security_charges,recharge_amount)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(chl);
			preparedStmt.setString(1, cust_id);
			preparedStmt.setString(2, tag_id);
			preparedStmt.setString(3, vehicle_number);
			preparedStmt.setString(4, amtIssuence);
			preparedStmt.setString(5, amtInsurance);
			preparedStmt.setString(6, amtRSA);
			preparedStmt.setString(7, amtSecurity);
			preparedStmt.setString(8, amtRecharge);
			preparedStmt.executeUpdate();

			log.info("charges inserted successfully");
		} catch (Exception e) {
			log.info("Error while inserting record : " + e.getMessage());
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(preparedStmt);
			DatabaseManager.closeConnection(conn);

		}

	}

	public static JSONObject challanData(String TID, String VehicleNumber, String min_threshold, Connection conn)// adding
	// threshold
	{

		JSONObject insidechallan = new JSONObject();

		String BankName, ChallanId, ChassisNumber, CreatedDate, EngineNumber, TagId;
		PreparedStatement pstmt = null;
		ResultSet rsjson = null;

		try {

			String sqljson = "select * from challan_master where tid=? and vehicle_number= ?";
			pstmt = conn.prepareStatement(sqljson);
			pstmt.setString(1, TID);
			pstmt.setString(2, VehicleNumber);
			rsjson = pstmt.executeQuery();
			log.info("sql :::: " + sqljson);
			if (rsjson.next()) {
				BankName = rsjson.getString("bank_name");
				ChallanId = rsjson.getString("challan_id");
				ChassisNumber = rsjson.getString("chassis_number");
				CreatedDate = rsjson.getString("created_date");
				EngineNumber = rsjson.getString("engine_number");
				TID = rsjson.getString("tid");
				TagId = rsjson.getString("tag_id");
				VehicleNumber = rsjson.getString("vehicle_number");
				insidechallan = insidechallan.put("bankName", BankName);
				insidechallan = insidechallan.put("challanId", ChallanId);
				insidechallan = insidechallan.put("chassisNumber", ChassisNumber);
				insidechallan = insidechallan.put("createdDate", CreatedDate);
				insidechallan = insidechallan.put("engineNumber", EngineNumber);
				insidechallan = insidechallan.put("tid", TID);
				insidechallan = insidechallan.put("tagId", TagId);
				insidechallan = insidechallan.put("vehicleNumber", VehicleNumber);
				log.info("insidechallan......" + insidechallan);
			}

			updateTagStatus(TID, conn);
		} catch (Exception e) {
			log.error("Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(pstmt);
		}
		return insidechallan;
	}

	public static void updateTagStatus(String TID, Connection conn) {

		PreparedStatement ps = null;
		String sqlupdate = "update vehicle_tag_linking_history set tag_status = ? where tid= ? ";
		try {
			ps = conn.prepareStatement(sqlupdate);
			ps.setString(1, TID);
			ps.setString(2, "2");
			ps.executeUpdate();
			log.info("Status changed Successfully.........");
		} catch (Exception e) {
			log.info("Something wrong while changing status ..." + e);
			log.error("Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}

	public String getIsCommercial(String vno, Connection conn) {

		String isCommercial = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = null;
		try {

			query = "select is_commercial from customer_vehicle_info where vehicle_number  = ? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, vno);

			rs = ps.executeQuery();

			if (rs.next()) {
				isCommercial = rs.getString("is_commercial");

			}
			log.info("TagAllocationDao.java getIsCommercial() :::  return is_commercial :: " + isCommercial
					+ "  against the VehicleNo  :::  " + vno + " from customer_vehicle_info ");

		} catch (Exception e) {
			log.error("Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return isCommercial;
	}

	public String getCustomerId(String vno) {

		String isCommercial = null;
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		String query = null;
		try {
			con = DatabaseManager.getConnection();
			if (con != null) {
				query = "select is_commercial from customer_vehicle_master where vehicle_number  = ? ";
				ps = con.prepareStatement(query);
				ps.setString(1, vno);

				rs = ps.executeQuery();

				while (rs.next()) {
					isCommercial = rs.getString("is_commercial");

				}
				log.info("TagAllocationDao.java getIsCommercial() :::  return is_commercial :: " + isCommercial
						+ "  against the VehicleNo  :::  " + vno + " from customer_vehicle_master ");

			} else {
				log.info("TagAllocationDao.java getIsCommercial()  ::: connection is null");
			}
		} catch (Exception e) {
			log.error("Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);

		}
		return isCommercial;
	}

	public static boolean checkBalance(String vehicleNumber, String issuanceCharge, String security, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		double currentBalance = 0;
		String query = null;
		try {

			query = "select current_balance from wallet_info w, customer_vehicle_info v, customer_info c where c.user_id=v.user_id and c.wallet_id=w.wallet_id and v.vehicle_number=?";
			log.info("query :: " + query);
			log.info("vehicleNumber :: " + vehicleNumber);
			ps = conn.prepareStatement(query);
			ps.setString(1, vehicleNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				log.info("current_balance :: " + rs.getString("current_balance"));
				double total = Double.valueOf(issuanceCharge) + Double.valueOf(security);
				currentBalance = rs.getDouble("current_balance");
				currentBalance = currentBalance - total;
				if (currentBalance < 0) {
					return true;
				}
			}
		} catch (Exception e) {
			log.error("Exception in getting checkBalance() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}

}
