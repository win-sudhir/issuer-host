package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dto.TxnResponseDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class TxnDao {
	static Logger log = Logger.getLogger(TxnDao.class.getName());

	public static TxnResponseDTO addTxn(Connection conn, JSONObject jsonRequest) {
		
		PreparedStatement ps = null;
		TxnResponseDTO txnResponse = new TxnResponseDTO();
		
		try {
			String tis = DateUtils.getCurrentTime("yyyy-MM-dd'T'HH:mm:ss");
			String id = DateUtils.getCurrentTime("yyyyMMddHHmmssSSSSSS");
			String txnId = DateUtils.getUniqueIssuerId();
			
			String query = "INSERT INTO req_pay_master (message_identifier, txn_timestamp, org_id, txn_id, acq_id, txn_type, "
					+ "original_txn_id, tag_id, tid, avc, toll_plaza_id, toll_plaza_name, toll_geo_code, "
					+ "toll_plaza_type, plaza_reader_id, reader_ts, txn_amount, txn_time, vehicle_no, is_commercial, "
					+ "current_date_time, resp_status, resp_id, exception_code, "
					+ "recon_status, error_code, laneId, laneDirection, wim) "
					
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, tis);
			ps.setString(3, "EQTB");
			ps.setString(4, txnId);
			ps.setString(5, "500002@iin.npci");
			ps.setString(6, "DEBIT");
			ps.setString(7, id);
			ps.setString(8, jsonRequest.getString("tagId"));
			ps.setString(9, jsonRequest.getString("tId"));
			ps.setString(10, jsonRequest.getString("avc"));
			ps.setString(11, "100101");
			ps.setString(12, "EQTBTOLL");
			ps.setString(13, "11.11");
			ps.setString(14, "National");
			ps.setString(15, "10010101");
			ps.setString(16, tis);
			ps.setString(17, jsonRequest.getString("amount"));
			ps.setString(18, tis);
			ps.setString(19, jsonRequest.getString("vehicleNumber"));
			ps.setString(20, "F");
			ps.setString(21, tis);
			ps.setString(22, "0");
			ps.setString(23, "0");
			ps.setString(24, "000");
			ps.setString(25, "NRNS");
			ps.setString(26, "000");
			ps.setString(27, "Lane1");
			ps.setString(28, "S");
			ps.setString(29, "");
			
			ps.executeUpdate();
			log.info("Data Inserted Into Req Pay Master txnId "+txnId);
			
			txnResponse.setAmount(jsonRequest.getString("amount"));
			txnResponse.setRespCode("00");
			txnResponse.setRespMessage("Success");
			txnResponse.setTagId(jsonRequest.getString("tagId"));
			txnResponse.settId(jsonRequest.getString("tagId"));
			txnResponse.setTxnId(txnId);
			txnResponse.setTxnTime(tis);
			txnResponse.setVehicleNo(jsonRequest.getString("vehicleNumber"));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return txnResponse;
	}
	
	public static TxnResponseDTO getTxn(Connection conn, String txnId) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		TxnResponseDTO txnResponse = new TxnResponseDTO();
		String query = null;
		try {
			query = "SELECT message_identifier, txn_id, txn_type, tag_id, tid, toll_plaza_id, toll_plaza_name, reader_ts, "
					+ "txn_amount, vehicle_no, is_commercial, cbs_reference_no, cbs_response_date, avc FROM tollpay.req_pay_master where txn_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, txnId);
			rs = ps.executeQuery();

			while (rs.next()) {

				txnResponse.setTollTxnId(rs.getString("message_identifier"));
				txnResponse.setTxnId(rs.getString("txn_id"));
				txnResponse.setTxnType(rs.getString("txn_type"));
				txnResponse.setTagId(rs.getString("tag_id"));
				txnResponse.settId(rs.getString("tid"));
				txnResponse.setTollPlazaId(rs.getString("toll_plaza_id"));
				txnResponse.setTollPlazaName(rs.getString("toll_plaza_name"));
				txnResponse.setTxnTime(rs.getString("reader_ts"));
				txnResponse.setAmount(rs.getString("txn_amount"));
				txnResponse.setRespCode(rs.getString("00"));
				txnResponse.setRespMessage(rs.getString("Success"));
				txnResponse.setVehicleNo(rs.getString("vehicle_no"));
				txnResponse.setCbsId(rs.getString("cbs_reference_no"));
				txnResponse.setCbsResponseTime(rs.getString("cbs_response_date"));
				txnResponse.setIsCommercial(rs.getString("is_commercial"));
				txnResponse.setVehicleClass(rs.getString("avc"));

			}
		} catch (Exception e) {
			log.error("Exception in getting list of getAgentList() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return txnResponse;
	}
}
