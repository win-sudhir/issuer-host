package com.winnovature.dispute.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dispute.dao.DisputeRaiseDAO;
import com.winnovature.dispute.dao.DisputeServiceUtil;
import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.DisputeErrorCode;
import com.winnovature.validation.SessionValidation;


@WebServlet("/dispute/managedispute")
public class Dispute extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(Dispute.class.getName());   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		//System.out.println("DisputeManagementRestApi Post() called............");
		Gson gson = new GsonBuilder().create();
		ResponseDTO responseDTO = new ResponseDTO();
		Connection conn = null;
		try {
			
			conn = DatabaseManager.getAutoCommitConnection();
			responseDTO = SessionValidation.validateSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			finalResponse = gson.toJson(responseDTO);
			//responseDTO.setStatus("1");
			if (responseDTO.getStatus().equals(ResponseDTO.success)) {

				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("jsonRequest " + jsonRequest);
				DisputeMasterDTO disputeMasterDTO = new Gson().fromJson(jsonRequest.toString(), DisputeMasterDTO.class);
				String requestType = jsonRequest.getString("requestType");
				log.info("requestType " + requestType);
				//customers only
				if (("getTransactionsByFilter").equalsIgnoreCase(requestType)) {
					List<DisputeMasterDTO> data = DisputeServiceUtil.getDisputeSearchService().getTransactionsByFilter(disputeMasterDTO, conn, request.getHeader("userId"));//
					//(jsonRequest.getString("fromDate"), jsonRequest.getString("toDate"), conn);
					responseDTO.setData(data);
					finalResponse = gson.toJson(responseDTO);
				}
				else if(("getReasonCodeList").equalsIgnoreCase(requestType)) {
					responseDTO.setData(DisputeServiceUtil.getDisputeManagementService().getReasonList1());
					finalResponse = gson.toJson(responseDTO);
				} 
				else if(("getOneDayTransactions").equalsIgnoreCase(requestType)) {
					List<DisputeMasterDTO> data = DisputeServiceUtil.getDisputeSearchService().getOneDayBeforeTransactions(disputeMasterDTO.getOriginalTxnId(), conn);
					responseDTO.setData(data);
					finalResponse = gson.toJson(responseDTO);
				} 
				
				else if(("getTransactionsToApprove").equalsIgnoreCase(requestType)) {
					List<DisputeMasterDTO> data = DisputeServiceUtil.getDisputeSearchService().getNewTransactionsToApprove(disputeMasterDTO, conn);
					responseDTO.setData(data);
					finalResponse = gson.toJson(responseDTO);
				} 
				else if(("getDisputeStatus").equalsIgnoreCase(requestType)) {
					List<DisputeMasterDTO> data = DisputeServiceUtil.getDisputeSearchService().getDisputeStatus(disputeMasterDTO, conn, request.getHeader("userId"));
					responseDTO.setData(data);
					finalResponse = gson.toJson(responseDTO);
				} 
				
				else if(("checkGoodFaith").equalsIgnoreCase(requestType)) {
					if(new DisputeRaiseDAO().checkGoodFaith(disputeMasterDTO.getOriginalTxnId(), conn)){
						responseDTO.setStatus(ResponseDTO.failure);
						responseDTO.setMessage(DisputeErrorCode.DISPUTEBU006.getErrorMessage());
						responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU006.name());
					}
					responseDTO.setStatus(ResponseDTO.success);
					finalResponse = gson.toJson(responseDTO);
				}
				else if(("getTransactionToUpadate").equalsIgnoreCase(requestType)) {
					List<DisputeMasterDTO> data = DisputeServiceUtil.getDisputeSearchService().getTransactionToUpdateById(disputeMasterDTO.getId(), conn);
					responseDTO.setData(data);
					finalResponse = gson.toJson(responseDTO);
				}
				
				else if(("getTransactionDetails").equalsIgnoreCase(requestType)) {
					List<DisputeMasterDTO> data = DisputeServiceUtil.getDisputeSearchService().getTransactionDetails(disputeMasterDTO.getOriginalTxnId(), conn);
					responseDTO.setData(data);
					finalResponse = gson.toJson(responseDTO);
				}
				
				else {
					log.info("Invalid Request Type");
					// ResponseDTO resp = new ResponseDTO();
					responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0025.name());
					responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0025.getErrorMessage());
					responseDTO.setStatus(ResponseDTO.failure);
					finalResponse = gson.toJson(responseDTO);
				}

			}

			log.info("*****************Response to dispute/managedispute API()****************");

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}
}