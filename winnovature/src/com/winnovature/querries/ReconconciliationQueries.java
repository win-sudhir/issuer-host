/**
 * 
 */
package com.winnovature.querries;


public class ReconconciliationQueries {
	public static String getValidatePreviousCycleQuery = "SELECT on_date, cycle FROM npci_recon_status WHERE on_date=? and cycle=?";
	public static String getIsfileProcessedQuery = "SELECT file_name FROM npci_recon_status WHERE file_name=?";
	public static String getIsCycleRevokedQuery = "SELECT on_date, cycle FROM bank_recon_status WHERE on_date=? and cycle=?";
	public static String getLatestDateQuery = "SELECT on_date FROM npci_recon_status";
	/*
	public static String getAddReconTxnsQuery = "INSERT INTO temp_txns841("
			+ "transaction_sequence_number, tag_id, function_code, transaction_date_and_time, rrn, issuer_id, "
			+ "acquirer_id, transaction_amount, settlement_amount, settlement_indicator_dr_cr, "
			+ "settlement_currency, financial_non_financial_indicator, settlement_date, fee_type_code_1, "
			+ "interchange_category_1, fee_amount_1, fee_dr_cr_indicator_1, fee_currency_1, fee_type_code_2, "
			+ "interchange_category_2, fee_amount_2, fee_dr_cr_indicator_2, fee_currency_2, fee_type_code_3, "
			+ "interchange_category_3, fee_amount_3, fee_dr_cr_indicator_3, fee_currency_3, transaction_type, "
			+ "merchant_id, tid, transaction_status" + ") "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	*/
	public static String getAddReconTxnsQuery = "INSERT INTO temp_txns("
			+ "transaction_Sequence_Number, transaction_ID, message_ID, note, reference_ID, reference_URL, transaction_Date_and_Time,"
			+ "transaction_Type, original_transaction_ID, tag_ID, tID, aVC, wim, merchant_Id, merchant_Type, sub_Merchant_Type,"
			+ "lane_Id, lane_Direction, lane_Reader_ID, parking_Floor, parking_Zone, parking_Slot, parking_Reader_ID, reader_Read_Date_and_Time, "
			+ "signature_Data, signature_Authentication, ePC_Verified, proc_Restriction_Res, vehicle_Auth, public_Key_CVV, "
			+ "reader_Transaction_Counter, reader_Transaction_Status, payer_Address, issuer_ID, payer_Code, payer_name, payer_Type, "
			+ "transaction_Amount, currency_Code, payee_Address, acquirer_ID, payee_Code, payee_name, payee_Type, response_Code, "
			+ "transaction_Status, approval_Number, payee_Error_Code, settled_Amount, settled_Currency, account_Type, available_Balance,"
			+ "ledger_Balance, account_Number, customer_Name, initiated_By, initiated_Time, last_Updated_By, last_Updated_Time, "
			+ "vehicle_Registration_number, vehicle_Class, vehicle_Type, tag_status, tag_Issue_Date" + ") "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String getProcRecon841Query = "{call pr_recon_npci(?,?)}";
	public static String getRecon841SumaryQuery = "SELECT * FROM npci_recon_summary order by id desc limit 1";
	public static String getReconSkippedListQuery = "SELECT on_date,cycle FROM npci_recon_status WHERE file_name='skipped' ORDER BY STR_TO_DATE(on_date, '%d/%m/%Y') DESC";
	public static String getReconRevokedListQuery = "SELECT on_date,cycle FROM bank_recon_status ORDER BY STR_TO_DATE(on_date, '%d/%m/%Y') DESC";
	public static String getDeleteSkippedCycleQuery = "DELETE FROM npci_recon_status WHERE on_date=? and cycle=?";
	public static String getInsertRevokedCycleQuery = "INSERT INTO bank_recon_status(on_date, cycle,file_name) VALUES(?,?,?)";
	public static String getInsertSkippedCycleQuery = "INSERT INTO npci_recon_status(on_date, cycle,file_name) VALUES(?,?,?)";
	public static String getCheckCycleBlankQuery = "SELECT on_date, cycle FROM npci_recon_status";
	public static String getAddDateNCycleQuery = "INSERT INTO npci_recon_status(on_date, cycle,file_name) VALUES(?,?,?)";
}
