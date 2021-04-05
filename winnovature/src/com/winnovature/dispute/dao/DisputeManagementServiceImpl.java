/**
 * 
 */
package com.winnovature.dispute.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;

import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.ReasonData;
import com.winnovature.dto.WalletResponse;
import com.winnovature.utils.PropertyReader;
import com.winnovature.utils.Server2ServerCall;


public class DisputeManagementServiceImpl implements DisputeManagementService {

	static Logger log = Logger.getLogger(DisputeManagementServiceImpl.class.getName());

	//Database connectivity & uncomment
	private static DisputeSearchDAO dao = new DisputeSearchDAOImpl();
	//private static DisputeSearchDAO dao = new DisputeSearchDAOImplTest();
	private static volatile DisputeManagementService oInstance = null;
	public static DisputeManagementService getInstance() {
		
		if(oInstance == null) {
			oInstance = new DisputeManagementServiceImpl();
		}
		return oInstance;
	}
	
	@Override
	public Map<String, String> callAPIFleet(String issuerId, String amount) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> dataToCall = dao.callWallet(issuerId, amount);
		String txnAPI = PropertyReader.getPropertyValue("");//
		try {
			WalletResponse resp = Server2ServerCall.sendToWallet(new Gson().toJson(dataToCall), txnAPI);
			String cbsResp = resp.getResponse();	
			if(resp.getHttpCode() == 200 && cbsResp != null)
			{	

				JSONObject jsonResp = new JSONObject(cbsResp);

				String respCode = jsonResp.getString("resp_code");

				if(respCode != null && !respCode.equalsIgnoreCase("EE"))
				{
					String respMsg = null;													
					respMsg = jsonResp.getString("Message");
					if(respCode != null && respCode.equalsIgnoreCase("00") && respMsg != null && (respMsg.contains("Success") || respMsg.contains("success") || respMsg.contains("SUCCESS"))){	    			  
						map.put("resp_code", "000");
						map.put("resp_message", "Fleet Wallet Trnsaction Successfully..");
					} 
					else{
						map.put("resp_code", "003");
						map.put("resp_message", "Fleet Wallet respCode : "+respCode+" , respMsg : "+respMsg+" , Wallet Trnsaction Failed.. ");
					}
				}else{
					map.put("resp_code", "002");
					map.put("resp_message", "Fleet Wallet respCode : "+respCode+" Trnsaction Failed .. ");
				}
			}else{
				map.put("resp_code", "001");
				map.put("resp_message", "Fleet Wallet httpRespCode : "+resp.getHttpCode()+" Connection failed .. ");
			}

		}catch (Exception e) {
			map.put("resp_code", "001");
			map.put("resp_message", e.getMessage());
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public String approveDisputeTransaction(String id, String userId, Connection conn) {
		// TODO Auto-generated method stub
		return dao.approveDispute(id, userId, conn);
	}
	@Override
	public ArrayList<ReasonData> getReasonList1() {
		ArrayList<ReasonData> reasonList1 = new ArrayList<ReasonData>();
		reasonList1.add(new ReasonData("3001", "NETC Toll services not availed/ Tag holder does not recognise the transaction"));
		reasonList1.add(new ReasonData("3002", "Duplicate transaction done at Toll Plaza"));
		reasonList1.add(new ReasonData("3003", "Vehicle was in exempted list"));
		reasonList1.add(new ReasonData("3004", "Vehicle was in black list"));
		reasonList1.add(new ReasonData("3005", "Vehicle was in low balance list"));
		reasonList1.add(new ReasonData("3006", "Toll fare calculation error"));
		reasonList1.add(new ReasonData("3007", "Vehicle class mismatch"));
		reasonList1.add(new ReasonData("3008", "Signature not validated"));
		reasonList1.add(new ReasonData("3009", "Wrong Debit Adjustment raised"));
		reasonList1.add(new ReasonData("3010", "Credit posted as Debit"));
		reasonList1.add(new ReasonData("3011", "Paid by other means"));
		reasonList1.add(new ReasonData("3012", "Fraudulent tagholder not present transaction"));
		reasonList1.add(new ReasonData("3013", "Fraudulent multiple transaction"));
		reasonList1.add(new ReasonData("3014", "Other Specify"));
		// log.info(reasonList1);
		return reasonList1;
	}
	
	@Override
	public String rejectDisputeTransaction(String id, String remark, String userId, Connection conn) {
		return dao.rejectDisputeTransaction(id, remark, userId, conn);
	}
	
	@Override
	public String updateDisputeTransaction(DisputeMasterDTO disputeMasterDTO, Connection conn) {
		return dao.updateDisputeTransaction(disputeMasterDTO, conn);
	}
	
	
	@Override
	public String walletCall(String finalReqParams) {
		JSONObject jsonRespRet = new JSONObject();
		try {

			String fleetAPIURL = PropertyReader.getPropertyValue("");
			log.info("");
			WalletResponse resp = Server2ServerCall.sendToWallet(finalReqParams.toString(), fleetAPIURL);

			String cbsResp = resp.getResponse(); // normal Text just set into
													// XML variable
			log.info("DisputeManagementServiceImpl.java :: Fleet System : cbsResp below :");
			log.info("DisputeManagementServiceImpl.java :: Fleet System : cbsResp " + cbsResp.toString());
			String curBal = null;
			if (resp.getHttpCode() == 200 && cbsResp != null) {

				JSONObject jsonResp = new JSONObject(cbsResp);

				String respCode = jsonResp.getString("resp_code");
				curBal = jsonResp.getString("cur_bal");

				if (respCode != null && !respCode.equalsIgnoreCase("EE")) {
					String respMsg = null;
					respMsg = jsonResp.getString("Message");

					if (respCode != null && respCode.equalsIgnoreCase("00") && respMsg != null
							&& (respMsg.contains("Success") || respMsg.contains("success")
									|| respMsg.contains("SUCCESS"))) {
						jsonRespRet.put("resp_code", "000");
						jsonRespRet.put("resp_message", "Fleet Wallet Trnsaction Successfully..");
						jsonRespRet.put("cur_bal", curBal);
					}

					else {
						jsonRespRet.put("resp_code", "003");
						jsonRespRet.put("resp_message", "Fleet Wallet respCode : " + respCode + " , respMsg : "
								+ respMsg + " , Wallet Trnsaction Failed.. ");
						jsonRespRet.put("cur_bal", curBal);
					}

				}

				else {
					jsonRespRet.put("resp_code", "002");
					jsonRespRet.put("resp_message", "Fleet Wallet respCode : " + respCode + " Trnsaction Failed .. ");
					// jsonRespRet.put("cur_bal", curBal);
				}

			} else {
				jsonRespRet.put("resp_code", "001");
				jsonRespRet.put("resp_message",
						"Fleet Wallet httpRespCode : " + resp.getHttpCode() + " Connection failed .. ");
				// jsonRespRet.put("cur_bal", curBal);
			}
		}

		catch (Exception e) {
			try {
				jsonRespRet.put("resp_code", "001");
				jsonRespRet.put("resp_message", e.getMessage());
			} catch (Exception ex) {
				log.error("DisputeManagementServiceImpl.java :: Excepiton while put error msg in jsonOblect " + ex.getMessage());
				ex.printStackTrace();
			}
			log.error("DisputeManagementServiceImpl.java :: Exception in insert to recon while closing individual dispute :: "
					+ e.getMessage());
			e.printStackTrace();
		}

		log.info("DisputeManagementServiceImpl.java :: CallAPIFleet() :: status " + jsonRespRet.toString());

		return jsonRespRet.toString();

	}

	

}
