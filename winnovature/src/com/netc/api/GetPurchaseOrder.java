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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dao.CheckSession;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.PurchaseOrderService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/purchaseorder/getpo")
public class GetPurchaseOrder extends HttpServlet {
	static Logger log = Logger.getLogger(GetPurchaseOrder.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
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
			if (request.getParameter("poId") == null) {
				responseDTO.setStatus(ResponseDTO.failure);
				responseDTO.setMessage("Parameter not found.");
				responseDTO.setErrorCode("WINPOBU0001");
				return;
			}
			String poId = request.getParameter("poId");
			//String userId = request.getHeader("userId");
			responseDTO = PurchaseOrderService.getOnePurchaseOrder(poId, conn);
			finalResponse = gson.toJson(responseDTO);
		}

		catch (Exception e) {
			log.error("purchaseorder/getpo   ::  Getting Exception   :::    "+ e.getMessage());
		} finally {
			log.info("*****************Response to /purchaseorder/getpo API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}
}
