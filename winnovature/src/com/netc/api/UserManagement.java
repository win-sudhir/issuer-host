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
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.UserDTO;
import com.winnovature.service.UserService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

/**
 * Servlet implementation class UserManagement
 */
@WebServlet("/user/manageuser")
public class UserManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(UserManagement.class.getName());

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

			UserService userService = new UserService();

			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("jsonRequest " + jsonRequest);
			UserDTO userDTO = new UserDTO();
			String requestType = jsonRequest.getString("requestType");
			log.info("UserManagement requestType " + requestType);

			if (("addUser").equalsIgnoreCase(requestType)) {
				JSONObject userInfo = jsonRequest.getJSONObject("userInfo");
				JSONObject address = jsonRequest.getJSONObject("address");
				userDTO = new Gson().fromJson(userInfo.toString(), UserDTO.class);
				AddressDTO addressDTO = new Gson().fromJson(address.toString(), AddressDTO.class);
				responseDTO = userService.addUser(userDTO, addressDTO, request.getHeader("userId"), conn);
				// finalResponse = gson.toJson(responseDTO);
			}

			else if (("updateUser").equalsIgnoreCase(requestType)) {
				JSONObject userInfo = jsonRequest.getJSONObject("userInfo");
				JSONObject address = jsonRequest.getJSONObject("address");
				userDTO = new Gson().fromJson(userInfo.toString(), UserDTO.class);
				AddressDTO addressDTO = new Gson().fromJson(address.toString(), AddressDTO.class);
				responseDTO = userService.updateUser(userDTO, addressDTO, request.getHeader("userId"), conn);
				// finalResponse = gson.toJson(responseDTO);
			}

			else if (("approveUser").equalsIgnoreCase(requestType)) {
				userDTO = new Gson().fromJson(jsonRequest.toString(), UserDTO.class);
				responseDTO = userService.approveUser(userDTO, request.getHeader("userId"), conn);
				// finalResponse = gson.toJson(responseDTO);
			} else if (("rejectUser").equalsIgnoreCase(requestType)) {
				userDTO = new Gson().fromJson(jsonRequest.toString(), UserDTO.class);
				responseDTO = userService.rejectUser(userDTO, request.getHeader("userId"), conn);
				// finalResponse = gson.toJson(responseDTO);
			} else if (("deleteUser").equalsIgnoreCase(requestType)) {
				userDTO = new Gson().fromJson(jsonRequest.toString(), UserDTO.class);
				responseDTO = userService.deleteUser(userDTO, request.getHeader("userId"), conn);
				// finalResponse = gson.toJson(responseDTO);
			}

			else if (("getUserById").equalsIgnoreCase(requestType)) {
				userDTO = new Gson().fromJson(jsonRequest.toString(), UserDTO.class);
				responseDTO = userService.getUserById(userDTO.getUserId(), request.getHeader("userId"), conn);
				// finalResponse = gson.toJson(responseDTO);
			} else if (("getUserList").equalsIgnoreCase(requestType)) {
				responseDTO = userService.getUserList(request.getHeader("userId"), conn);

			}
			finalResponse = gson.toJson(responseDTO);
			log.info("*****************Response to /user/manageuser API()****************");

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
