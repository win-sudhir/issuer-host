/**
 * 
 */
package com.winnovature.dispute.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;


import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.ReasonData;




public interface DisputeManagementService {

	public Map<String, String> callAPIFleet(String issuerId, String amount);
	public String approveDisputeTransaction(String id, String userId, Connection conn);
	public ArrayList<ReasonData> getReasonList1();
	public String rejectDisputeTransaction(String id, String remark, String userId, Connection conn);
	public String updateDisputeTransaction(DisputeMasterDTO disputeMasterDTO, Connection conn);
	
	public String walletCall(String finalReqParams);
	
}
