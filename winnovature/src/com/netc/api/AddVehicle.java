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
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDTO;
import com.winnovature.service.CustomerService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

@WebServlet("/vehicle/add")
public class AddVehicle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(AddVehicle.class.getName());

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

			// responseDTO = SessionValidation.validateSession(request.getHeader("userId"),
			// request.getHeader("Authorization"), conn);

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);
			
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			finalResponse = gson.toJson(responseDTO);
			// responseDTO.setStatus("1");
			// if (responseDTO.getStatus().equals(ResponseDTO.success)) {
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("REQUEST :: " + jsonRequest);

			VehicleDTO vehicleDTO = new Gson().fromJson(stringBuffer.toString(), VehicleDTO.class);
			responseDTO = CustomerService.addVehicle(conn, vehicleDTO, request.getHeader("userId"),
					jsonRequest.getString("customerId"));// );
			finalResponse = gson.toJson(responseDTO);
			// }
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to /vehicle/add API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

}
