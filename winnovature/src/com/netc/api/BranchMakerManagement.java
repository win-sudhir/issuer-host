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
import com.winnovature.dto.BranchAccountDTO;
import com.winnovature.dto.BranchDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.BranchService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.BranchErrorCode;

@WebServlet("/branchm/management")
public class BranchMakerManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(BranchMakerManagement.class.getName());

	public BranchMakerManagement() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

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
			log.info("Branch-Management requestType " + requestType);

			if (("addBranch").equalsIgnoreCase(requestType)) {
				JSONObject branchInfo = jsonRequest.getJSONObject("branchInfo");
				JSONObject accountInfo = jsonRequest.getJSONObject("account");
				BranchDTO branchDTO = new Gson().fromJson(branchInfo.toString(), BranchDTO.class);
				BranchAccountDTO branchAccountDTO = new Gson().fromJson(accountInfo.toString(), BranchAccountDTO.class);
				responseDTO = branchService.addBranch(branchDTO, branchAccountDTO, request.getHeader("userId"), conn,
						ipAddress);
			} else if (("getBranchList").equalsIgnoreCase(requestType)) {
				responseDTO = branchService.getBranchListForMaker(conn);
			} else if (requestType.equalsIgnoreCase("deleteBranch")) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.deleteBranchMaker(conn, branchId, request.getHeader("userId"));
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
