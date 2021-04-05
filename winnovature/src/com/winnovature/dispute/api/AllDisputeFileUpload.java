/**
 * 
 */
package com.winnovature.dispute.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dispute.dao.DisputeAdjDAO;
import com.winnovature.dto.DisputeUploadFileDTO;

import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.DisputeErrorCode;
import com.winnovature.validation.DisputeValidation;
import com.winnovature.validation.SessionValidation;


@WebServlet("/dispute/alldispute")
public class AllDisputeFileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(AllDisputeFileUpload.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();

		String finalResponse = null;
		Gson gson = new GsonBuilder().create();
		ResponseDTO responseDTO = new ResponseDTO();
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();

			responseDTO = SessionValidation.validateSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);
			finalResponse = gson.toJson(responseDTO);
			if (responseDTO.getStatus().equals(ResponseDTO.success)) {

				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("jsonRequest " + jsonRequest);
				// jsonResponse = new JSONObject();

				DisputeUploadFileDTO disputeUploadFileDTO = new Gson().fromJson(jsonRequest.toString(),
						DisputeUploadFileDTO.class);
				log.info("disputeUploadFileDTO " + disputeUploadFileDTO.toString());
				log.info("disputeUploadFileDTO " + disputeUploadFileDTO.getTransactionInfoDTO().get(0).getTransactionDateAndTime().toString());

				if (disputeUploadFileDTO.getRequestType().equalsIgnoreCase("insertDisputeAdjTxns")) {

					responseDTO = DisputeValidation.validateDisputeUploadFile(disputeUploadFileDTO);
					if (responseDTO.getStatus().equals(ResponseDTO.success)) {
						responseDTO = new DisputeAdjDAO().processDisputeFileUpload(disputeUploadFileDTO, conn, request.getHeader("userId") );
					}
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

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to dispute/alldispute API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
