package com.winnovature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.dto.CustomerDTO;
import com.winnovature.utils.DatabaseManager;

public class NETCReportsDAO {
	static Logger log = Logger.getLogger(NETCReportsDAO.class.getName());

	public String getTRANSACTIONReport(String fromDate, String toDate, String userid, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("" + fromDate + "  " + toDate);
		JSONObject obj = null;
		try {

			// conn = DatabaseManager.getConnection();

			if (fromDate != null && toDate != null && !fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {

				if (userid.equalsIgnoreCase("admin")) {
					sql = ReportsQueries.TransactionReportBYDATEAdmin;
					ps = conn.prepareStatement(sql);
					fromDate = fromDate + " 00:00:00";
					toDate = toDate + " 23:59:59";
					ps.setString(1, fromDate);
					ps.setString(2, toDate);
				} else {
					sql = ReportsQueries.TransactionReportBYDATECustomer;
					ps = conn.prepareStatement(sql);
					fromDate = fromDate + " 00:00:00";
					toDate = toDate + " 23:59:59";
					ps.setString(1, userid);
					ps.setString(2, fromDate);
					ps.setString(3, toDate);
				}

			} else {

				if (userid.equalsIgnoreCase("admin")) {
					sql = ReportsQueries.TransactionReportAdmin;
					ps = conn.prepareStatement(sql);
				} else {
					sql = ReportsQueries.TransactionReportCustomer;
					ps = conn.prepareStatement(sql);
					ps.setString(1, userid);
				}

			}

			rs = ps.executeQuery();

			while (rs.next()) {

				obj = new JSONObject();
				obj.put("id", rs.getString("id"));
				obj.put("message_identifier", rs.getString("message_identifier"));
				obj.put("txn_id", rs.getString("txn_id"));
				obj.put("original_txn_id", rs.getString("original_txn_id"));
				obj.put("Vehicle_No", rs.getString("Vehicle_No"));
				obj.put("txn_amount", rs.getString("txn_amount"));
				obj.put("txn_time", rs.getString("txn_time"));
				// obj.put("NPCI_Response_Status", rs.getString("NPCI_Response_Status"));
				// obj.put("Response_Status", rs.getString("Response_Status"));
				// obj.put("npci_resp_code", rs.getString("npci_resp_code"));
				obj.put("error_code", rs.getString("error_code"));
				obj.put("acq_id", rs.getString("acq_id"));
				obj.put("avc", rs.getString("avc"));
				obj.put("txn_type", rs.getString("txn_type"));
				obj.put("toll_plaza_id", rs.getString("toll_plaza_id"));
				obj.put("toll_plaza_name", rs.getString("toll_plaza_name"));
				obj.put("tag_id", rs.getString("tag_id"));
				obj.put("tid", rs.getString("tid"));
				obj.put("reader_ts", rs.getString("reader_ts"));
				obj.put("recon_status", rs.getString("recon_status"));
				obj.put("exception_code", rs.getString("exception_code"));
				obj.put("is_commercial", rs.getString("is_commercial"));
				obj.put("Merchant_Type", rs.getString("Merchant_Type"));
				obj.put("toll_plaza_type", rs.getString("toll_plaza_type"));
				// obj.put("account_id", rs.getString("account_id"));
				// obj.put("opening_bal", rs.getString("opening_bal"));
				// obj.put("credit_amount", rs.getString("credit_amount"));
				// obj.put("debit_amount", rs.getString("debit_amount"));
				// obj.put("closing_balance", rs.getString("closing_balance"));
				arr.put(obj);
			}
			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(conn);
		}

		return arr.toString();
	}

	public String getPURCHASEORDERReport(String fromDate, String toDate, String userid, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("---------" + fromDate + "---------" + toDate);
		JSONObject obj = null;
		try {
			// conn = DatabaseManager.getConnection();
			if (fromDate != null && fromDate != "" && toDate != null && toDate != "" && !fromDate.isEmpty()
					&& !toDate.isEmpty()) {

				sql = ReportsQueries.PuchaseOrderReportBYDATE;
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, fromDate);
				ps.setString(2, toDate);

			} else {
				sql = ReportsQueries.PuchaseOrderReport;
				ps = conn.prepareStatement(sql);
			}
			log.info(("Query::" + sql));

			rs = ps.executeQuery();

			while (rs.next()) {
				// log.info("ff");
				obj = new JSONObject();
				obj.put("id", rs.getString("id"));
				obj.put("po_id", rs.getString("po_id"));
				obj.put("supplier_name", rs.getString("supplier_name"));
				obj.put("supp_id", rs.getString("supp_id"));
				obj.put("tag_class_id", rs.getString("tag_class_id"));
				obj.put("order_qty", rs.getString("order_qty"));
				obj.put("Status", rs.getString("Status"));
				obj.put("total_order_value", rs.getString("total_order_value"));
				obj.put("created_on", rs.getString("created_on"));
				obj.put("created_by", rs.getString("created_by"));
				obj.put("approved_on", rs.getString("approved_on"));
				obj.put("approved_by", rs.getString("approved_by"));

				arr.put(obj);
			}
			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(conn);
		}

		return arr.toString();
	}

	public String getVEHICLEReport(String fromDate, String toDate, String userid, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("---------" + fromDate + "---------" + toDate);
		JSONObject obj = null;
		try {
			// conn = DatabaseManager.getConnection();
			if (fromDate != null && fromDate != "" && toDate != null && toDate != "" && !fromDate.isEmpty()
					&& !toDate.isEmpty()) {

				sql = ReportsQueries.VehicleReportBYDATE;
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, fromDate);
				ps.setString(2, toDate);
			} else {
				sql = ReportsQueries.VehicleReport;
				ps = conn.prepareStatement(sql);

			}
			log.info(("Query::" + sql));

			rs = ps.executeQuery();

			while (rs.next()) {
				obj = new JSONObject();
				obj.put("customer_id", rs.getString("customer_id"));
				obj.put("Customer_Name", rs.getString("Customer_Name"));
				obj.put("vehicle_number", rs.getString("vehicle_number"));
				obj.put("tag_class_id", rs.getString("tag_class_id"));
				obj.put("tid", rs.getString("tid"));
				obj.put("tag_id", rs.getString("tag_id"));
				obj.put("barcode_data", rs.getString("barcode_data"));
				// obj.put("agent_name", rs.getString("agent_name"));
				// obj.put("branch_name", rs.getString("branch_name"));
				obj.put("Is_Commercial", rs.getString("Is_Commercial"));

				arr.put(obj);
			}
			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(con);
		}

		return arr.toString();
	}

	public String getCustomerVEHICLEReport(String fromDate, String toDate, String userId, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("" + fromDate + "  " + toDate);
		JSONObject obj = null;
		try {
			// conn = DatabaseManager.getConnection();
			if (fromDate != null && fromDate != "" && toDate != null && toDate != "" && !fromDate.isEmpty()
					&& !toDate.isEmpty()) {

				sql = ReportsQueries.CustVehicleReportBYDATE;
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, fromDate);
				ps.setString(2, toDate);
				ps.setString(3, userId);

			} else {
				sql = ReportsQueries.CustVehicleReport;
				ps = conn.prepareStatement(sql);
				ps.setString(1, userId);
			}
			log.info(("Query::" + sql));

			rs = ps.executeQuery();

			while (rs.next()) {
				obj = new JSONObject();
				obj.put("customer_id", rs.getString("customer_id"));
				obj.put("Customer_Name", rs.getString("Customer_Name"));
				obj.put("vehicle_number", rs.getString("vehicle_number"));
				obj.put("tag_class_id", rs.getString("tag_class_id"));
				obj.put("tid", rs.getString("tid"));
				obj.put("tag_id", rs.getString("tag_id"));
				obj.put("barcode_data", rs.getString("barcode_data"));
				// obj.put("agent_name", rs.getString("agent_name"));
				// obj.put("branch_name", rs.getString("branch_name"));
				obj.put("Is_Commercial", rs.getString("Is_Commercial"));

				arr.put(obj);
			}
			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(con);
		}

		return arr.toString();
	}

	public String getCUSTOMERReport(String fromDate, String toDate, String userid, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("" + fromDate + "  " + toDate);

		try {
			// conn = DatabaseManager.getConnection();
			if (toDate.equalsIgnoreCase("VEHICLE")) {
				sql = ReportsQueries.CustomerVehicleDetails;
				ps = conn.prepareStatement(sql);
				ps.setString(1, fromDate);
				rs = ps.executeQuery();
				while (rs.next()) {
					JSONObject jo = new JSONObject();

					jo.put("vehicle_number", rs.getString("vehicle_number"));
					jo.put("tag_class_id", rs.getString("tag_class_id"));
					jo.put("engine_number", rs.getString("engine_number"));
					jo.put("chassis_number", rs.getString("chassis_number"));
					jo.put("TagID", rs.getString("TagID"));
					jo.put("BarCode", rs.getString("BarCode"));

					arr.put(jo);
				}
			} else if (toDate.equalsIgnoreCase("AUDIT")) {
				sql = ReportsQueries.CustomerAuditDetails;
				ps = conn.prepareStatement(sql);
				ps.setString(1, fromDate);
				rs = ps.executeQuery();
				while (rs.next()) {
					JSONObject jo = new JSONObject();

					jo.put("staff_name", rs.getString(1));
					jo.put("staff_name1", rs.getString(2));
					jo.put("email_id", rs.getString(3));
					jo.put("email_id1", rs.getString(4));
					jo.put("contact_number", rs.getString(5));
					jo.put("contact_number1", rs.getString(6));
					jo.put("resi_add1", rs.getString(7));
					jo.put("resi_add11", rs.getString(8));
					jo.put("resi_address2", rs.getString(9));
					jo.put("resi_address21", rs.getString(10));
					jo.put("resi_pin", rs.getString(11));
					jo.put("resi_pin1", rs.getString(12));

					jo.put("resi_city", rs.getString(13));
					jo.put("resi_city1", rs.getString(14));
					jo.put("resi_state", rs.getString(15));
					jo.put("resi_state1", rs.getString(16));
					jo.put("isModifiedby", rs.getString(17));

					arr.put(jo);
				}
			} else {
				if (fromDate.equals("0") && toDate.equals("0")) {
					sql = ReportsQueries.CustomerReport;
					ps = conn.prepareStatement(sql);
				} else {
					sql = ReportsQueries.CustomerReportBYDATE;
					ps = conn.prepareStatement(sql);
					fromDate = fromDate + " 00:00:00";
					toDate = toDate + " 23:59:59";
					ps.setString(1, fromDate);
					ps.setString(2, toDate);

				}
				rs = ps.executeQuery();

				while (rs.next()) {
					JSONObject jo = new JSONObject();

					jo.put("cust_id", rs.getString("user_id"));
					jo.put("wallet_id", rs.getString("wallet_id"));
					jo.put("Customer_Name", rs.getString("Customer_Name"));
					jo.put("email_id", rs.getString("email_id"));
					jo.put("contact_number", rs.getString("contact_number"));
					jo.put("Address", rs.getString("Address"));
					jo.put("resi_state", rs.getString("resi_state"));
					jo.put("account_number", rs.getString("account_number"));
					jo.put("bank_name", rs.getString("bank_name"));
					jo.put("account_type", rs.getString("account_type"));
					jo.put("iswallet", rs.getString("is_wallet"));
					jo.put("current_balance", rs.getString("current_balance"));
					jo.put("sec_deposit", rs.getString("sec_deposit"));
					// jo.put("pos_id", rs.getString("pos_id"));
					// jo.put("CustomerType", rs.getString("CustomerType"));
					jo.put("created_by", rs.getString("created_by"));
					jo.put("created_on", rs.getString("created_on"));
					jo.put("approved_by", rs.getString("approved_by"));
					jo.put("approved_on", rs.getString("approved_on"));
					jo.put("status", rs.getString("status"));
					// jo.put("isModifiedby", rs.getString("isModifiedby"));
					// jo.put("rodt", rs.getString("rodt"));

					jo.put("validUpto", rs.getString("valid_upto"));// )getValidUptoDate(rs.getString("created_on").toString()));

					/*
					 * if(rs.getString("status_id").equalsIgnoreCase("2"))
					 * 
					 * {
					 * 
					 * jo.put("walletStatus", "FREEZE/BLOCKED");
					 * 
					 * } else if(rs.getString("status_id").equalsIgnoreCase("3")) {
					 * jo.put("walletStatus", "CLOSED"); } else { jo.put("walletStatus", "ACTIVE");
					 * 
					 * } if(rs.getString("max_balance").equalsIgnoreCase("10000.00")) {
					 * jo.put("kycType", "LTDKYC"); } else
					 * if(rs.getString("max_balance").equalsIgnoreCase("100000.00")) {
					 * jo.put("kycType", "FULLKYC"); }
					 */
					arr.put(jo);
				}
			}
			log.info(("Query::" + sql));

			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(conn);
		}

		return arr.toString();
	}

	public String getINVENTORYReport(String fromDate, String toDate, String userid, String txntype, String ReportType,
			String type, String operation, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		CallableStatement ps1 = null;
		// ResultSet rs1 = null;
		String sql = null;
		log.info("" + fromDate + " " + toDate);

		try {
			// con = DatabaseManager.getConnection();

			if (fromDate.equals("0") && toDate.equals("0")) {

				sql = ReportsQueries.InventoryReport;
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				String tagclassid = null;
				while (rs.next()) {

					tagclassid = rs.getString("tag_class_id");
					log.info("tag classs id ::" + tagclassid);
					sql = ReportsQueries.InventoryReportPRO;
					ps1 = conn.prepareCall(sql);
					ps1.setString(1, tagclassid);
					ps1.registerOutParameter(2, -5);
					ps1.registerOutParameter(3, -5);
					ps1.registerOutParameter(4, -5);
					ps1.execute();
					int ava_ho = ps1.getInt(2);
					int ava_branch = ps1.getInt(3);
					int ava_agent = ps1.getInt(4);
					log.info("ava_ho ::" + ava_ho + "::ava_branch::" + ava_branch + "::ava_agent::" + ava_agent);
					JSONObject jo = new JSONObject();
					jo.put("tagclassid", tagclassid);
					jo.put("ava_ho", ava_ho);
					jo.put("ava_branch", ava_branch);
					jo.put("ava_agent", ava_agent);

					arr.put(jo);
				}

			} else {

				sql = ReportsQueries.InventoryReportPro2;
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, fromDate);
				ps.setString(2, toDate);
				rs = ps.executeQuery();
				while (rs.next()) {
					JSONObject jo = new JSONObject();
					jo.put("branch_name", rs.getString("branch_name"));
					jo.put("branch_id", rs.getString("branch_id"));
					jo.put("tag_class_id", rs.getString("tag_class_id"));

					arr.put(jo);
				}

			}

			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeCallableStatement(ps1);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(con);

		}

		return arr.toString();
	}

	public String getINVENTORYNEWReport(String fromDate, String toDate, String userid, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("------------- " + fromDate + "------------" + toDate);
		JSONObject obj = null;
		try {
			// conn = DatabaseManager.getConnection();
			if (fromDate != null && fromDate != "" && toDate != null && toDate != "" && !fromDate.isEmpty()
					&& !toDate.isEmpty()) {
				sql = ReportsQueries.InventoryNewReportBYDATE;
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, fromDate);
				ps.setString(2, toDate);

			} else {

				sql = ReportsQueries.InventoryNewReport;
				ps = conn.prepareStatement(sql);

			}
			log.info(("Query::" + sql));

			rs = ps.executeQuery();

			while (rs.next()) {
				obj = new JSONObject();
				obj.put("branch_id", rs.getString("Branch User ID"));
				obj.put("TotalCount", rs.getString("Total Count"));
				obj.put("TagClass", rs.getString("Tag Class"));
				obj.put("UsedTag", rs.getString("Used Tag"));
				obj.put("UnUsedTag", rs.getString("UnUsed Tag"));

				arr.put(obj);
			}
			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(conn);
		}

		return arr.toString();
	}

	public String getTAGDETAILSReport(String searchBy, String searchValue, String userid, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("" + searchBy + "  " + searchValue);
		JSONObject obj = null;
		try {

			// con = DatabaseManager.getConnection();

			sql = ReportsQueries.TagDetailsReport;
			ps = conn.prepareStatement(sql);
			ps.setString(1, searchBy);
			ps.setString(2, searchValue);

			rs = ps.executeQuery();

			while (rs.next()) {

				obj = new JSONObject();
				obj.put("TAGID", rs.getString("TAGID"));
				obj.put("TID", rs.getString("TID"));
				obj.put("TAGCLASSID", rs.getString("TAGCLASSID"));
				obj.put("BARCODE", rs.getString("BARCODE"));
				obj.put("TAGOWNER", rs.getString("TAGOWNER"));
				obj.put("TAGSTATUS", rs.getString("TAGSTATUS"));
				obj.put("VEHICLENO", rs.getString("VEHICLENO"));
				obj.put("CUSTOMERID", rs.getString("CUSTOMERID"));
				obj.put("WALLETID", rs.getString("WALLETID"));

				arr.put(obj);
			}
			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			// DatabaseManager.closeConnection(con);
		}

		return arr.toString();
	}

	public String getWalletTxnReport(String fromDate, String toDate, String userId, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("" + fromDate + "  " + toDate);
		JSONObject obj = null;
		try {

			if (fromDate != null && fromDate != "" && toDate != null && toDate != "" && !fromDate.isEmpty()
					&& !toDate.isEmpty()) {

				sql = ReportsQueries.WalletTxnReportByDate;
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, fromDate);
				ps.setString(2, toDate);
			} else {
				sql = ReportsQueries.WalletTxnReport;
				ps = conn.prepareStatement(sql);

			}
			log.info(("Query::" + sql));

			rs = ps.executeQuery();

			while (rs.next()) {
				obj = new JSONObject();
				// txn_id, wallet_id, txn_type, opening_balance, txn_amount, closing_balance,
				// security_balance,
				// narration, txn_date, pay_mode, partner_id, partner_ref_no,
				obj.put("txnId", rs.getString("txn_id"));
				obj.put("walletId", rs.getString("wallet_id"));
				obj.put("txnType", rs.getString("txn_type"));
				obj.put("openingBalance", rs.getString("opening_balance"));
				obj.put("txnAmount", rs.getString("txn_amount"));
				obj.put("closingBalance", rs.getString("closing_balance"));
				obj.put("securityBalance", rs.getString("security_balance"));

				obj.put("remarks", rs.getString("narration"));
				obj.put("txnDate", rs.getString("txn_date"));
				obj.put("payMode", rs.getString("pay_mode"));
				obj.put("partnerId", rs.getString("partner_id"));
				obj.put("partnerRefNo", rs.getString("partner_ref_no"));

				arr.put(obj);
			}
			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}

		return arr.toString();
	}

	public String getCustomerWalletTxnReport(String fromDate, String toDate, String userId, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("" + fromDate + "  " + toDate);
		JSONObject obj = null;
		CustomerDTO customerDTO = new CustomerDAO().geWalletInfo(userId);
		try {

			if (fromDate != null && fromDate != "" && toDate != null && toDate != "" && !fromDate.isEmpty()
					&& !toDate.isEmpty()) {

				sql = ReportsQueries.CustWalletTxnReportByDate;
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, fromDate);
				ps.setString(2, toDate);
				ps.setString(3, customerDTO.getWalletId());

			} else {
				sql = ReportsQueries.CustWalletTxnReport;
				ps = conn.prepareStatement(sql);
				ps.setString(1, customerDTO.getWalletId());
			}
			log.info(("Query::" + sql));

			rs = ps.executeQuery();

			while (rs.next()) {
				obj = new JSONObject();

				obj.put("txnId", rs.getString("txn_id"));
				obj.put("walletId", rs.getString("wallet_id"));
				obj.put("txnType", rs.getString("txn_type"));
				obj.put("openingBalance", rs.getString("opening_balance"));
				obj.put("txnAmount", rs.getString("txn_amount"));
				obj.put("closingBalance", rs.getString("closing_balance"));
				obj.put("securityBalance", rs.getString("security_balance"));

				obj.put("remarks", rs.getString("narration"));
				obj.put("txnDate", rs.getString("txn_date"));
				obj.put("payMode", rs.getString("pay_mode"));
				obj.put("partnerId", rs.getString("partner_id"));
				obj.put("partnerRefNo", rs.getString("partner_ref_no"));

				arr.put(obj);
			}
			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}

		return arr.toString();
	}

	public String getEXCEPTIONReport(String fromDate, String toDate, String userid, Connection conn) {
		JSONArray arr = new JSONArray();
		// Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("" + fromDate + "" + toDate);
		JSONObject obj = null;
		try {

			// con = DatabaseManager.getConnection();

			if (fromDate != null && toDate != null && !fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {

				sql = ReportsQueries.ExceptionReportBYDATE;
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, fromDate);
				ps.setString(2, toDate);

			} else {
				sql = ReportsQueries.ExceptionReport;
				ps = conn.prepareStatement(sql);

			}

			rs = ps.executeQuery();

			while (rs.next()) {

				obj = new JSONObject();
				obj.put("customerId", rs.getString("customer_id"));
				if (rs.getString("customer_id") == null) {
					obj.put("customerId", "NA");
				}
				obj.put("tid", rs.getString("tid"));
				obj.put("tagId", rs.getString("tag_id"));
				obj.put("barCode", rs.getString("barcode_data"));
				obj.put("vehicleNumber", rs.getString("vehicle_number"));
				if (rs.getString("exe_code").equals("01"))
					obj.put("exeCode", "BLACKLIST");
				if (rs.getString("exe_code").equals("02"))
					obj.put("exeCode", "EXEMPTED");
				if (rs.getString("exe_code").equals("02"))
					obj.put("exeCode", "LOWBALANCE");
				obj.put("insertionFlag", rs.getString("insertion_flag"));
				obj.put("tagClassId", rs.getString("tag_class_id"));
				// obj.put("msgId", rs.getString("msg_id"));

				arr.put(obj);

			}

			log.info(("::json data::" + arr.toString()));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}

		return arr.toString();
	}

}
