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
import com.winnovature.dto.BranchDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.BranchService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.BranchErrorCode;

/**
 * Servlet implementation class BranchCheckerManagement
 */
@WebServlet("/branchc/management")
public class BranchCheckerManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(BranchMakerManagement.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BranchCheckerManagement() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

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

			boolean checkSession = true;//CheckSession.isValidSession(request.getHeader("userId"),
					//request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			String ipAddress = request.getRemoteAddr();

			BranchService branchService = new BranchService();

			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("jsonRequest " + jsonRequest);

			String requestType = jsonRequest.getString("requestType");
			log.info("UserManagement requestType " + requestType);
			
			if (("updateBranch").equalsIgnoreCase(requestType)) {
				JSONObject branchInfo = jsonRequest.getJSONObject("branchInfo");
				JSONObject address = jsonRequest.getJSONObject("address");
				JSONObject account = jsonRequest.getJSONObject("account");
				AddressDTO addressDTO = new Gson().fromJson(address.toString(), AddressDTO.class);
				AccountDTO accountDTO = new Gson().fromJson(account.toString(), AccountDTO.class);
				BranchDTO branchDTO = new Gson().fromJson(branchInfo.toString(), BranchDTO.class);
				branchDTO.setBranchId(jsonRequest.getString("branchId"));
				responseDTO = branchService.updateBranch(branchDTO, addressDTO, accountDTO, request.getHeader("userId"),
						conn);
			}
			
			else if (("getBranchById").equalsIgnoreCase(requestType)) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.getBranchById(branchId, request.getHeader("userId"), conn);
			}
			else if (("getBranchList").equalsIgnoreCase(requestType)) {
				responseDTO = branchService.getBranchListForChecker(conn);
			} else if (requestType.equalsIgnoreCase("deleteBranch")) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.deleteBranchChecker(conn, branchId, request.getHeader("userId"), ipAddress);
			} else if (requestType.equalsIgnoreCase("approveBranch")) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.approveBranch(conn, branchId, request.getHeader("userId"), ipAddress);
			} else if (requestType.equalsIgnoreCase("rejectBranch")) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.rejectBranch(conn, branchId, request.getHeader("userId"), ipAddress);
			} else {
				log.info("Invalid Request Type");
				responseDTO.setErrorCode(BranchErrorCode.WINNABU0029.name());
				responseDTO.setMessage(BranchErrorCode.WINNABU0029.getErrorMessage());
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
