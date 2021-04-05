package com.netc.api;

import java.io.BufferedReader;
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

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.DAOManager;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/vehicle/unallocated")
public class UnAllocatedVehicleList extends HttpServlet {
	static Logger log = Logger.getLogger(UnAllocatedVehicleList.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String customerId = request.getParameter("customerId");
		log.info("UnAllocatedVehicleList.java customerId  = " + customerId);

		PrintWriter out = response.getWriter();

		StringBuffer jb = new StringBuffer();
		String line = null;

		String unallocated = null;
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			// String userId = request.getHeader("userId").toString();
			// String auth_token = request.getHeader("Authorization").toString();

			// if (userId != null && auth_token != null && LoginDao.isValidSession(userId,
			// auth_token)) {
			DAOManager dm = new DAOManager();
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			unallocated = dm.getUnAllocatedVechicleList(customerId);
			out.write(unallocated);
			/*
			 * }
			 * 
			 * else { JSONObject jo = new JSONObject(); jo.put("flag", "0");
			 * out.write(jo.toString()); }
			 */

		} catch (Exception e) {
			JSONObject jo = new JSONObject();
			jo.put("message", e.getMessage());
			out.write(jo.toString());
			log.error("UnAllocatedVehicleList.java  :::  Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

	public static void main(String[] args) {
		String s = "2020-12-12 11:11:11.0";
		System.out.println(s.substring(0, s.indexOf(".")));
	}
}
