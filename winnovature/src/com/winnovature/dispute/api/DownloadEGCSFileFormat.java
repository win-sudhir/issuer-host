package com.winnovature.dispute.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.winnovature.dao.CheckSession;
import com.winnovature.dispute.dao.DisputeRaiseDAO;
import com.winnovature.dispute.dao.DisputeServiceUtil;
import com.winnovature.dto.DisputeMasterDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.DisputeErrorCode;

@WebServlet("/dispute/egcsfiledownload")
public class DownloadEGCSFileFormat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DownloadEGCSFileFormat.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonRequest = new JSONObject();

		StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		Gson gson = new GsonBuilder().create();
		ResponseDTO responseDTO = new ResponseDTO();
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();
			/*
			 * responseDTO = SessionValidation.validateSession(request.getHeader("userId"),
			 * request.getHeader("Authorization"), conn);
			 * 
			 * finalResponse = gson.toJson(responseDTO); responseDTO.setStatus("1");
			 */
			// if (responseDTO.getStatus().equals(ResponseDTO.success)) {
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("jsonRequest " + jsonRequest);
			DisputeMasterDTO disputeMasterDTO = new Gson().fromJson(jsonRequest.toString(), DisputeMasterDTO.class);
			String requestType = jsonRequest.getString("requestType");

			if (("getEGCSTransactions").equalsIgnoreCase(requestType)) {
				List<DisputeMasterDTO> data = DisputeServiceUtil.getDisputeSearchService()
						.getEGCSTransactions(disputeMasterDTO, conn);

				log.info("Data SIZE : " + data.size());
				// String bankParticipantId =
				// PropertyReader.getPropertyValue("bankParticipantId");
				// PropertyReader.getUrlString("bankParticipantId");// 00
				// String date = new SimpleDateFormat("yyDDD").format(new Date());
				if (data.size() > 0) {
					response.setContentType("text/csv");
					String fileName = DisputeRaiseDAO.getFileName();// bankParticipantId + date + "00" + ".csv";
					ArrayList<String> rows = new ArrayList<String>();
					try {
						response.setHeader("Content-disposition", "attachment; " + "filename=" + fileName);

						// ArrayList<String> rows = new ArrayList<String>();
						rows.add(
								"Tag_ID,Function_Code,Txn_Time,Txn_Id,Issuer_ID,Acquirer_ID,Txn_Amount,Reason_Code,Full_Partial_Indicator,Toll_Plaza_Id,TID,MMT,Internal Tracking Number");
						rows.add("\n");

						log.info("EGCS---->downloadCSV()");

						for (DisputeMasterDTO dispute : data) {

							String acqId = dispute.getAcquirerId();
							/*
							 * if(acqId.contains("@")) { acqId = acqId.substring(0, acqId.indexOf("@")); }
							 */
							rows.add(dispute.getTagId() + "," + dispute.getFunctionCode() + "," + dispute.getTxnTime()
									+ "," + "'" + dispute.getTxnId() + "," + dispute.getIssuerId() + "," + acqId + ","
									+ dispute.getTxnAmount() + "," + dispute.getReasonCode() + ","
									+ dispute.getFullPartialIndicator() + "," + dispute.getTollPlazaId() + ","
									+ dispute.getTid() + "," + dispute.getMmt() + "," + dispute.getReferenceId());
							rows.add("\n");
						}
					} finally {
						Iterator<String> iter = rows.iterator();
						while (iter.hasNext()) {
							String outputString = (String) iter.next();
							response.getOutputStream().print(outputString);
						}
						// response.getOutputStream().print(outputString);
					}

					/*
					 * Iterator<String> iter = rows.iterator(); while (iter.hasNext()) { String
					 * outputString = (String) iter.next();
					 * response.getOutputStream().print(outputString); }
					 * response.getOutputStream().flush(); response.getOutputStream().close(); }
					 * catch (Exception e) { log.error(e.getMessage()); e.printStackTrace(); }
					 */
				} else {
					log.info("EGCS RECORDS NOT FOUND.");
					PrintWriter out = response.getWriter();
					responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0028.name());
					responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0028.getErrorMessage());
					responseDTO.setStatus(ResponseDTO.failure);
					finalResponse = gson.toJson(responseDTO);
					out.write(finalResponse);
					MemoryComponent.closePrintWriter(out);
				}
			} else {
				log.info("Invalid Request Type");
				PrintWriter out = response.getWriter();
				responseDTO.setErrorCode(DisputeErrorCode.DISPUTEBU0025.name());
				responseDTO.setMessage(DisputeErrorCode.DISPUTEBU0025.getErrorMessage());
				responseDTO.setStatus(ResponseDTO.failure);
				finalResponse = gson.toJson(responseDTO);
				out.write(finalResponse);
				MemoryComponent.closePrintWriter(out);
			}

			log.info("*****************Response to dispute/egcsfiledownload API()****************");
			// out.write(finalResponse);
			// log.info(finalResponse);

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			DatabaseManager.commitConnection(conn);
			// response.getOutputStream().flush();
			// response.getOutputStream().close();
			// MemoryComponent.closePrintWriter(out);
		}

	}

}
