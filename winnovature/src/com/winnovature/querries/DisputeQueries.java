/**
 * 
 */
package com.winnovature.querries;


public class DisputeQueries {
	//queries for get transaction to raise dispute
	//public static String getTxnsByDateToRaiseDispute= "SELECT txn_time, txn_id, original_txn_id, tag_id, tid, txn_amount, txn_type, vehicle_no FROM req_pay_master WHERE txn_time between ? and ? and original_txn_id not in (SELECT acq_txn_id FROM dispute_master_npci) order by txn_time desc limit 100";// desc limit 100";
	public static String getTxnsByDateToRaiseDispute= "SELECT r.txn_time, r.txn_id, r.original_txn_id, r.tag_id, r.tid, r.txn_amount, r.txn_type, r.vehicle_no "
			+ "FROM req_pay_master r, vehicle_tag_linking v WHERE v.customer_id=? and r.tag_id=v.tag_id and r.txn_time between ? and ? and r.original_txn_id not in (SELECT acq_txn_id FROM dispute_master_npci) order by txn_time desc limit 100";// desc limit 100";
	public static String getTxnsByDateToRaiseDisputeByAdmin= "SELECT r.txn_time, r.txn_id, r.original_txn_id, r.tag_id, r.tid, r.txn_amount, r.txn_type, r.vehicle_no "
			+ "FROM req_pay_master r, vehicle_tag_linking v WHERE r.tag_id=v.tag_id and r.txn_time between ? and ? and r.original_txn_id not in (SELECT acq_txn_id FROM dispute_master_npci) order by txn_time desc limit 100";// desc limit 100";
	public static String getTxnsByWalletIdToRaiseDispute= "SELECT r.txn_time, r.txn_id, r.original_txn_id, r.tag_id, r.tid, r.txn_amount, r.txn_type, r.vehicle_no FROM vehicle_tag_linking v, req_pay_master r WHERE v.tag_id=r.tag_id and v.tid=r.tid and v.customer_id=? and r.original_txn_id not in (select acq_txn_id FROM dispute_master_npci) order by txn_time desc limit 50";
	public static String getTxnsByMobileNoToRaiseDispute= "SELECT r.txn_time, r.txn_id, r.original_txn_id, r.tag_id, r.tid, r.txn_amount, r.txn_type, r.vehicle_no FROM vehicle_tag_linking v, req_pay_master r WHERE v.tag_id=r.tag_id and v.tid=r.tid and v.customer_id=? and r.original_txn_id not in (select acq_txn_id FROM dispute_master_npci) order by txn_time desc limit 50";
	public static String getTxnsByVehicleNoToRaiseDispute= "SELECT txn_time, txn_id, original_txn_id, tag_id, tid, txn_amount, txn_type, vehicle_no FROM req_pay_master WHERE vehicle_no=? and original_txn_id not in (SELECT acq_txn_id FROM dispute_master_npci) order by txn_time desc limit 50";
	public static String getTxnsByOriginalTxnIdToRaiseDispute= "SELECT txn_time, txn_id, original_txn_id, tag_id, tid, txn_amount, txn_type, vehicle_no FROM req_pay_master WHERE original_txn_id=? and original_txn_id not in (SELECT acq_txn_id FROM dispute_master_npci) order by txn_time desc limit 50";
	public static String getTxnAmountQuery= "SELECT original_txn_id, txn_amount FROM req_pay_master WHERE original_txn_id=?";
	public static String getOneDayBeforeTxnListQuery= "SELECT r.txn_time, r.vehicle_no, r.org_id, r.original_txn_id, r.txn_id, r.tag_id, r.tid, r.txn_amount, r.txn_type, r.toll_plaza_id, r.toll_plaza_name, r.reader_ts FROM vehicle_tag_linking v, req_pay_master r WHERE v.tag_id=r.tag_id and v.tid=r.tid and txn_time BETWEEN ? AND ? and r.tag_id=? and r.original_txn_id<>? and r.original_txn_id not in (SELECT acq_txn_id FROM dispute_master_npci) order by txn_time desc limit 5"; 
	public static String getDateProcQuery= "{CALL pr_getDate(?,?,?,?)}";
	//public static String getAddDisputeInfoQuery= "INSERT INTO dispute_master_npci(tag_id, function_code, txn_time, acq_txn_id, issuer_id, acquirer_id, txn_amount, actual_txn_amount, reason_code, toll_plaza_id, full_partial_indicator, tid, mmt, toll_txn_id, txn_type, dispute_desc, evidence_doc, evidence_doc1, evidence_doc2, evidence_doc3, evidence_doc4, evidence_doc5, status, process_status, is_approved, rodt, created_by, created_on) VALUES(?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?)";
	public static String getAddDisputeInfoQuery= "INSERT INTO dispute_master_npci(tag_id, function_code, txn_time, acq_txn_id, issuer_id, acquirer_id, txn_amount, actual_txn_amount, reason_code, toll_plaza_id, full_partial_indicator, tid, mmt, toll_txn_id, txn_type, dispute_desc, evidence_doc, evidence_doc1, status, process_status, is_approved, rodt, created_by, created_on) "
			+ "VALUES(?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?)";
	public static String getRequestPayQuery= "SELECT txn_time, txn_id, original_txn_id, tag_id, tid, txn_amount, txn_type, vehicle_no, acq_id, toll_plaza_id,toll_plaza_name FROM req_pay_master WHERE original_txn_id=?";
	
	public static String getCheckDisputeQuery= "SELECT acq_txn_id FROM dispute_master_npci WHERE acq_txn_id=?";
	public static String getCheckGoodFaithQuery= "SELECT txn_time, original_txn_id FROM req_pay_master WHERE date(txn_time) < NOW() - INTERVAL 35 DAY and original_txn_id=?";
	public static String getCheckRequestPayQuery= "SELECT original_txn_id FROM req_pay_master WHERE original_txn_id=?";
	
	public static String getNewDisputeTxnListQuery = "SELECT id, tag_id, function_code, txn_time, acq_txn_id, issuer_id, acquirer_id, txn_amount, actual_txn_amount, reason_code, toll_plaza_id, full_partial_indicator, tid, mmt, toll_txn_id, txn_type, dispute_desc, created_on, created_by FROM dispute_master_npci WHERE status=? and is_approved=? and created_on BETWEEN ? AND ? order by id desc";// limit 50";
	public static String getDisputeStatusQuery = "SELECT id, tag_id, function_code, txn_time, acq_txn_id, issuer_id, acquirer_id, txn_amount, actual_txn_amount, reason_code, toll_plaza_id, full_partial_indicator, tid, mmt, toll_txn_id, txn_type, dispute_desc, created_on, created_by,status, is_approved FROM dispute_master_npci WHERE created_by=? and created_on BETWEEN ? AND ? order by id desc limit 200";
	public static String getDisputeStatusQueryAdmin = "SELECT id, tag_id, function_code, txn_time, acq_txn_id, issuer_id, acquirer_id, txn_amount, actual_txn_amount, reason_code, toll_plaza_id, full_partial_indicator, tid, mmt, toll_txn_id, txn_type, dispute_desc, created_on, created_by,status, is_approved FROM dispute_master_npci WHERE created_on BETWEEN ? AND ? order by id desc limit 200";
	public static String getApproveDisputeQuery= "UPDATE dispute_master_npci set is_approved=?, last_updated_by=?,last_updated_on=?  WHERE id=?";
	public static String getRejectDisputeQuery= "UPDATE dispute_master_npci set is_approved=?, remark =?, last_updated_by=?,last_updated_on=?  WHERE id=?";
	
	//public static String getUpdateDisputeQuery= "UPDATE dispute_master_npci set txn_amount=?, reason_code=?, full_partial_indicator=?, mmt=?, dispute_desc=?, evidence_doc=?, evidence_doc1=?, evidence_doc2=?, evidence_doc3=?, evidence_doc4=?, evidence_doc5=?, last_updated_by=?, last_updated_on=? WHERE id=?";
	public static String getUpdateDisputeQuery= "UPDATE dispute_master_npci set txn_amount=?, reason_code=?, full_partial_indicator=?, mmt=?, dispute_desc=?, last_updated_by=?, last_updated_on=? WHERE id=?";
	public static String getDisputeTxnByIdQuery= "SELECT function_code, txn_amount, reason_code, full_partial_indicator, dispute_desc, acq_txn_id, mmt  FROM dispute_master_npci WHERE id=?";
	public static String getTransactionDetailsQuery= "SELECT txn_id, txn_amount, toll_plaza_id, toll_plaza_name, plaza_reader_id, toll_plaza_type, reader_ts FROM req_pay_master WHERE original_txn_id=?";
	//
	public static String getTransactionToCloseQuery = "SELECT id, acq_txn_id, txn_amount, txn_type, txn_time, toll_plaza_id, toll_txn_id, tag_id, tid, dispute_desc, status FROM dispute_master_npci WHERE created_on BETWEEN ? AND ? and status=? and is_approved=? order by created_on desc";// limit 100";
	
	public static String getWalletQuery = "SELECT r.txn_id, d.txn_type, d.txn_amount, d.toll_plaza_id, c.wallet_id, c.user_id FROM customer_info c, req_pay_master r, vehicle_tag_linking v, dispute_master_npci d WHERE r.original_txn_id = ? AND c.user_id=v.customer_id AND v.tag_id=r.tag_id AND d.acq_txn_id=r.original_txn_id";
	
	public static String getCheckAcqTxnIdExistQuery = "SELECT id, acq_txn_id, status, is_approved FROM dispute_master_npci WHERE id=? and acq_txn_id=? and status=? and is_approved=?";
	public static String getCloseDisputeQuery= "UPDATE dispute_master_npci set status=?, last_updated_by=?, last_updated_on=?  WHERE id=?";
	
	//akki qry
	public static String insertDisputeAdjustmentQuery= "INSERT INTO dispute_adjustment("
														+ "report_date, dispute_raise_date, dispute_raised_settlement_date, case_number, function_code, function_code_and_description, transaction_sequence_number,"
														+ "tag_id, tid, transaction_date_time, reader_read_date_time, transaction_settlement_date, transaction_amount, settlement_amount, settlement_currency_code,"
														+ "note, transaction_id, transaction_type, merchant_id, lane_id, merchant_type, sub_merchant_type, transaction_status, tag_status, avc, wim, originator_point,"
														+ "acquirer_id, transaction_orig_institution_pid, acquirer_name_and_country, iin, transaction_destination_institution_pid, issuer_name_and_country, vehicle_registration_number,"
														+ "vehicle_class, vehicle_type, financial_non_financial_indicator, dispute_reason_code, dispute_reason_code_description, disputed_amount, full_partial_indicator,"
														+ "member_message_text, document_indicator, document_attached_date, deadline_date, days_to_act, direction_of_dispute"
														+ ")"
														+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


	public static String insertReconMastertQuery="INSERT INTO recon_master("
														+ "msg_identifier, txn_timestamp, tag_id, acq_txn_id, acq_id, toll_plaza_id, txn_type, txn_amount, resp_code, recon_status, recon_date_time"
														+ ")" + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	public static String updatedisputeAdjustmentQuery="UPDATE dispute_adjustment set status = ? where transaction_id = ? ";


	public static String addDisputeQuery="INSERT INTO dispute_master_npci(tag_id, function_code, txn_time, acq_txn_id, issuer_id,acquirer_id, txn_amount, actual_txn_amount, "
														+ "reason_code, toll_plaza_id, full_partial_indicator, tid, mmt, toll_txn_id, txn_type, dispute_desc, "
														+ "evidence_doc, evidence_doc1, evidence_doc2, evidence_doc3, evidence_doc4, evidence_doc5,"
														+ "status, process_status, is_approved, rodt, created_by, created_on,last_updated_by,last_updated_on) "
														+ "VALUES(?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static String updateDisputeMasterStatusQuery = "UPDATE dispute_master_npci set status = ?, rodt = ? ,closing_status=?, last_updated_by=?, last_updated_on=? where acq_txn_id = ?";
	public static String validateTxnIdQuery = "SELECT original_txn_id FROM req_pay_master where original_txn_id = ?";

	public static String validateFileDataQuery = "SELECT acq_txn_id,acquirer_id,toll_plaza_id FROM dispute_master_npci where acq_txn_id=? and acquirer_id=? and toll_plaza_id=? and function_code=? and txn_amount=? and status=? and is_approved=?";
	public static String updateAdjustmentStatusQuery = "UPDATE dispute_adjustment set status = ? where transaction_id = ?";
	
	public static String getEGCSTransactionQuery = "SELECT id, acq_txn_id, txn_amount, txn_type, txn_time, toll_plaza_id, toll_txn_id, tag_id, tid, dispute_desc, status, function_code, issuer_id, reason_code, full_partial_indicator, mmt, acquirer_id, evidence_doc, evidence_doc1, evidence_doc2, evidence_doc3, evidence_doc4, evidence_doc5 FROM dispute_master_npci WHERE created_on BETWEEN ? AND ? and status=? and is_approved=?";
	
	public static String getSerachDisputeByDateQuery = "SELECT acq_txn_id, toll_txn_id, toll_plaza_id, txn_type, txn_amount, tag_id, dispute_desc, status, txn_time, is_approved, remark, created_by, created_on, last_updated_by, last_updated_on FROM dispute_master_npci WHERE created_on BETWEEN ? AND ?";
	public static String getSerachDisputeByReferenceIdQuery = "SELECT acq_txn_id, toll_txn_id, toll_plaza_id, txn_type, txn_amount, tag_id, dispute_desc, status, txn_time, is_approved, remark, created_by, created_on, last_updated_by, last_updated_on FROM dispute_master_npci WHERE toll_txn_id=?";
	
	public static String getApproveNRejectTxnByDateQuery = "SELECT acq_txn_id, toll_txn_id, toll_plaza_id, txn_type, txn_amount, tag_id, dispute_desc, status, txn_time, is_approved, remark, created_by, created_on, last_updated_by, last_updated_on FROM dispute_master_npci WHERE created_on BETWEEN ? AND ? and is_approved=?";
	
	public static String getRCBookPathQuery = "SELECT path_rc_book as doc FROM vehicle_tag_linking vtl, vehicle_document_master vdm WHERE vdm.vehicle_id=vtl.vehicle_id and vdm.vehicle_number=vtl.vehicle_number and vtl.tag_id=?";
	
	public static String getCheckAcqLiabilityQuery = "SELECT original_txn_id,error_code FROM req_pay_master where original_txn_id = ?, error_code = ?";
}
