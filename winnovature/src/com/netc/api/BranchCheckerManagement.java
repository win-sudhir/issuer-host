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
			// responseDTO = SessionValidation.validateSession(request.getHeader("userId"),
			// request.getHeader("Authorization"), conn);

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			String ipAddress = request.getRemoteAddr();

			BranchService branchService = new BranchService();
			// if (responseDTO.getStatus().equals(ResponseDTO.success)) {

			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("jsonRequest " + jsonRequest);

			String requestType = jsonRequest.getString("requestType");
			log.info("UserManagement requestType " + requestType);

			if (("getBranchList").equalsIgnoreCase(requestType)) {
				responseDTO = branchService.getBranchListForChecker(conn);
				// finalResponse = gson.toJson(responseDTO);
			} else if (requestType.equalsIgnoreCase("deleteBranch")) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.deleteBranchChecker(conn, branchId, request.getHeader("userId"), ipAddress);
				// finalResponse = gson.toJson(responseDTO);
			} else if (requestType.equalsIgnoreCase("approveBranch")) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.approveBranch(conn, branchId, request.getHeader("userId"), ipAddress);
				// finalResponse = gson.toJson(responseDTO);
			} else if (requestType.equalsIgnoreCase("rejectBranch")) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.rejectBranch(conn, branchId, request.getHeader("userId"), ipAddress);
				// finalResponse = gson.toJson(responseDTO);
			} else {
				log.info("Invalid Request Type");
				// ResponseDTO resp = new ResponseDTO();
				responseDTO.setErrorCode(BranchErrorCode.WINNABU0029.name());
				responseDTO.setMessage(BranchErrorCode.WINNABU0029.getErrorMessage());
				responseDTO.setStatus(ResponseDTO.failure);
				// finalResponse = gson.toJson(responseDTO);
			}

			// }
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
