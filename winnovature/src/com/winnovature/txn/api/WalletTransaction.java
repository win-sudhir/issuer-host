package com.winnovature.txn.api;

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
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.service.TransactionService;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.SessionValidation;
import com.winnovature.validation.TransactionValidation;


@WebServlet("/wallet/transaction")
public class WalletTransaction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(WalletTransaction.class.getName());   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		ResponseDTO responseDTO = new ResponseDTO();
		Gson gson = new GsonBuilder().create();
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();
			
			responseDTO = SessionValidation.validateSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			 
			finalResponse = gson.toJson(responseDTO);
			//responseDTO.setStatus("1");
			if (responseDTO.getStatus().equals(ResponseDTO.success)) {
				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("REQUEST :: " + jsonRequest);
				TransactionDTO transactionDTO = new Gson().fromJson(jsonRequest.toString(), TransactionDTO.class);
				responseDTO = TransactionValidation.validateTransaction(conn, transactionDTO, request.getHeader("userId"));
				if (responseDTO.getStatus().equals(ResponseDTO.success)) {
					responseDTO = TransactionService.doTransaction(conn, transactionDTO, request.getHeader("userId")); 
				}
				finalResponse = gson.toJson(responseDTO);
			}
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to /wallet/transaction API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	
	}

}
