package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class ChargebackReportDAO {
	static Logger log = Logger.getLogger(ChargebackReportDAO.class.getName());

	public String getChargebackReport(String fromDate, String toDate, String userid, Connection conn) {
		JSONArray arr = new JSONArray();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		log.info("fromDate " + fromDate + "toDate " + toDate);
		JSONObject obj = null;
		try {
			if (fromDate != null && fromDate != "" && toDate != null && toDate != "" && !fromDate.isEmpty()
					&& !toDate.isEmpty()) {
				sql = "SELECT r.txn_id, r.acq_id, u.txn_type, u.wallet_id, r.original_txn_id, r.vehicle_no, r.tag_id, r.tid, r.toll_plaza_id, r.txn_amount, u.date_time, u.status FROM tbl_unreg_acqliablity_master u, req_pay_master r WHERE u.txn_type=? AND u.txn_id=r.txn_id AND u.date_time BETWEEN ? AND ?";
				ps = conn.prepareStatement(sql);
				fromDate = fromDate + " 00:00:00";
				toDate = toDate + " 23:59:59";
				ps.setString(1, "ACQLBL");
				ps.setString(2, fromDate);
				ps.setString(3, toDate);

			} else {
				sql = "SELECT r.txn_id, r.acq_id, u.txn_type, u.wallet_id, r.original_txn_id, r.vehicle_no, r.tag_id, r.tid, r.toll_plaza_id, r.txn_amount, u.date_time, u.status FROM tbl_unreg_acqliablity_master u, req_pay_master r WHERE u.txn_type=? AND u.txn_id=r.txn_id";
				ps = conn.prepareStatement(sql);
				ps.setString(1, "ACQLBL");
			}
			log.info(("Query::" + sql));

			rs = ps.executeQuery();
//txn_id, acq_id, txn_type, wallet_id, original_txn_id, vehicle_no, tag_id, tid, toll_plaza_id, txn_amount, date_time, status
			while (rs.next()) {
				obj = new JSONObject();
				obj.put("txnId", rs.getString("txn_id"));
				obj.put("acqTxnId", rs.getString("original_txn_id"));
				obj.put("customerId", rs.getString("wallet_id"));
				obj.put("vehicleNumber", rs.getString("vehicle_no"));
				obj.put("txnAmount", rs.getString("txn_amount"));
				obj.put("tagId", rs.getString("tag_id"));
				obj.put("tid", rs.getString("tid"));
				obj.put("acquirerId", rs.getString("acq_id"));
				obj.put("tollPlazaId", rs.getString("toll_plaza_id"));
				obj.put("dateTime", rs.getString("date_time"));
				obj.put("status", "OPEN");
				if (rs.getString("status").equals("1"))
					obj.put("status", "CLOSE");
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
