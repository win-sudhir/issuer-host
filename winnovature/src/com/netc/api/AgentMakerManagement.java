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
import com.winnovature.dto.AgentDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.AgentMakerService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.AgentErrorCode;

/**
 * Servlet implementation class AgentManagement
 */
@WebServlet("/agentm/management")
public class AgentMakerManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(AgentMakerManagement.class.getName());

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
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			String ipAddress = request.getRemoteAddr();
			
			AgentMakerService agentMakerService = new AgentMakerService();
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("jsonRequest " + jsonRequest);

			AgentDTO agentDTO = new AgentDTO();
			String requestType = jsonRequest.getString("requestType");
			log.info("UserManagement requestType " + requestType);
			// customers only
			if (("addAgent").equalsIgnoreCase(requestType)) {
				JSONObject agentInfo = jsonRequest.getJSONObject("agentInfo");
				JSONObject address = jsonRequest.getJSONObject("addressInfo");
				JSONObject account = jsonRequest.getJSONObject("accountInfo");
				AddressDTO addressDTO = new Gson().fromJson(address.toString(), AddressDTO.class);
				AccountDTO accountDTO = new Gson().fromJson(account.toString(), AccountDTO.class);
				agentDTO = new Gson().fromJson(agentInfo.toString(), AgentDTO.class);
				responseDTO = agentMakerService.addAgent(agentDTO, addressDTO, accountDTO, request.getHeader("userId"),
						conn, ipAddress);

			} else if (("updateAgent").equalsIgnoreCase(requestType)) {
				JSONObject agentInfo = jsonRequest.getJSONObject("agentInfo");
				JSONObject address = jsonRequest.getJSONObject("address");
				JSONObject account = jsonRequest.getJSONObject("account");
				AddressDTO addressDTO = new Gson().fromJson(address.toString(), AddressDTO.class);
				AccountDTO accountDTO = new Gson().fromJson(account.toString(), AccountDTO.class);
				agentDTO = new Gson().fromJson(agentInfo.toString(), AgentDTO.class);
				agentDTO.setAgentId(jsonRequest.getString("agentId"));
				responseDTO = agentMakerService.updateAgent(agentDTO, addressDTO, accountDTO,
						request.getHeader("userId"), conn);
			} else if (("deleteAgent").equalsIgnoreCase(requestType)) {
				agentDTO = new Gson().fromJson(jsonRequest.toString(), AgentDTO.class);
				responseDTO = agentMakerService.deleteAgent(agentDTO, request.getHeader("userId"), conn);
			} else if (("getAgentById").equalsIgnoreCase(requestType)) {
				agentDTO = new Gson().fromJson(jsonRequest.toString(), AgentDTO.class);
				responseDTO = agentMakerService.getAgentById(agentDTO.getAgentId(), request.getHeader("userId"), conn);
			} else if (("getAgentList").equalsIgnoreCase(requestType)) {
				log.info("------------------------------------------------");
				responseDTO = agentMakerService.getAgentList(request.getHeader("userId"), conn);
			} else {
				log.info("Invalid Request Type");
				responseDTO.setErrorCode(AgentErrorCode.WINNABU0029.name());
				responseDTO.setMessage(AgentErrorCode.WINNABU0029.getErrorMessage());
				responseDTO.setStatus(ResponseDTO.failure);
			}

			finalResponse = gson.toJson(responseDTO);
			log.info("*****************Response to /agent/manageagent API()****************");

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
