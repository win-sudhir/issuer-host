package com.netc.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dao.CheckSession;
import com.winnovature.dto.PurchaseOrderDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.PurchaseOrderService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

@WebServlet("/purchaseorder/update")
public class PurchaseOrderUpdate extends HttpServlet {
	static Logger log = Logger.getLogger(PurchaseOrderUpdate.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		ResponseDTO responseDTO = new ResponseDTO();
		Gson gson = new GsonBuilder().create();
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("PO UPDATE REQUEST :: " + jsonRequest);
			JSONObject order = jsonRequest.getJSONObject("purchaseOrder");
			PurchaseOrderDTO purchaseOrder = new Gson().fromJson(order.toString(), PurchaseOrderDTO.class);
			JSONArray orderList = jsonRequest.getJSONArray("orderList");
			
			responseDTO = PurchaseOrderService.updatePurchaseOrder(conn, purchaseOrder, orderList, request.getHeader("userId"));
			finalResponse = gson.toJson(responseDTO);
		}

		catch (Exception e) {
			log.error("purchaseorder/update   ::  Getting Exception   :::    "+ e.getMessage());
		} finally {
			log.info("*****************Response to purchaseorder/update API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}
}
