package com.netc.api;

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
import com.winnovature.dao.CheckSession;
import com.winnovature.dao.TagReissuanceDAO;
import com.winnovature.dto.BarCodeDataDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDetailsDTO;
import com.winnovature.service.TagReissuanceService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.TagErrorCode;

@WebServlet("/tag/reissuance")
public class TagReissuance extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(TagReissuance.class.getName());

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
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("jsonRequest " + jsonRequest);
			conn = DatabaseManager.getAutoCommitConnection();
			
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			String requestType = jsonRequest.getString("requestType");
			String vehicleNumber = jsonRequest.getString("vehicleNumber");
			VehicleDetailsDTO vehicleDetailsDTO = null;
			if (("getVehicleDetails").equalsIgnoreCase(requestType)) {

				vehicleDetailsDTO = TagReissuanceDAO.getReissuanceTagDetails(vehicleNumber, conn);
				if (vehicleDetailsDTO == null) {
					responseDTO.setErrorCode(TagErrorCode.TAGBU0043.name());
					responseDTO.setMessage(TagErrorCode.TAGBU0043.getErrorMessage());
					responseDTO.setStatus(ResponseDTO.failure);
					finalResponse = gson.toJson(responseDTO);
					return;
				}
				List<BarCodeDataDTO> data = TagReissuanceDAO.getBarcodeData(vehicleDetailsDTO.getTagClassId(), conn);
				vehicleDetailsDTO.setData(data);
				finalResponse = gson.toJson(vehicleDetailsDTO);
				// vehicleDetailsDTO = TagReissuanceService.getVehicleDetails(vehicleNumber,
				// conn);
			}

			else if (("reIssueTag").equalsIgnoreCase(requestType)) {
				// responseDTO = TagReissuanceService.getVehicleDetails(vehicleNumber, conn);
				vehicleDetailsDTO = new Gson().fromJson(jsonRequest.toString(), VehicleDetailsDTO.class);
				responseDTO = TagReissuanceService.reIssueTag(vehicleDetailsDTO, conn, request.getHeader("userId"));
				finalResponse = gson.toJson(responseDTO);
			} else {
				log.info("Invalid Request Type");
				// ResponseDTO resp = new ResponseDTO();
				responseDTO.setErrorCode(TagErrorCode.TAGBU0042.name());
				responseDTO.setMessage(TagErrorCode.TAGBU0042.getErrorMessage());
				responseDTO.setStatus(ResponseDTO.failure);
				finalResponse = gson.toJson(responseDTO);
			}

			log.info("*****************Response to tag/reissuance API()****************");
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}
}