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
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dao.CheckSession;
import com.winnovature.dto.AccountDTO;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.KycDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDTO;
import com.winnovature.service.CustomerService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;


@WebServlet("/customer/edit")
public class CustomerUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(CustomerUpdate.class.getName());   
    
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
			
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);
			
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			//responseDTO = SessionValidation.validateSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);
			 
			finalResponse = gson.toJson(responseDTO);
			//responseDTO.setStatus("1");
			//if (responseDTO.getStatus().equals(ResponseDTO.success)) {
				stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
				jsonRequest = new JSONObject(stringBuffer.toString());
				log.info("REQUEST :: " + jsonRequest);
				JSONObject customer = jsonRequest.getJSONObject("customer");
				JSONObject address = jsonRequest.getJSONObject("address");
				JSONObject account = jsonRequest.getJSONObject("account");
				JSONObject kyc = jsonRequest.getJSONObject("kyc");
				//JSONArray vehicles = jsonRequest.getJSONArray("vehicles");
				CustomerDTO customerDTO = new Gson().fromJson(customer.toString(), CustomerDTO.class);
				AddressDTO addressDTO = new Gson().fromJson(address.toString(), AddressDTO.class);
				AccountDTO accountDTO = new Gson().fromJson(account.toString(), AccountDTO.class);
				KycDTO kycDTO = new Gson().fromJson(kyc.toString(), KycDTO.class);
				VehicleDTO vehicleDTO = new Gson().fromJson(stringBuffer.toString(), VehicleDTO.class);
				responseDTO = CustomerService.updateCustomer(conn, customerDTO, addressDTO, accountDTO, kycDTO, vehicleDTO,
						request.getHeader("userId"));//);
				finalResponse = gson.toJson(responseDTO);
			//}
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to /customer/edit API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

}
