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
import com.winnovature.dao.CheckSession;
import com.winnovature.dao.DashboardDAO;
import com.winnovature.dto.MonthWiseTxnDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/dashboard/monthly-info")
public class DashboardMonthlyAPI extends HttpServlet {
	static Logger log = Logger.getLogger(DashboardMonthlyAPI.class.getName());
	private static final long serialVersionUID = 1L;

	public DashboardMonthlyAPI() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		JSONObject jo = new JSONObject();
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			DashboardDAO dm = new DashboardDAO();

			String userId = request.getHeader("userId").toString();

			log.info("Monthly Details Info For UserId :: " + userId);
			List<MonthWiseTxnDTO> monthDashboardInfo = dm.getMonthWiseTxnDetails(userId);
			Gson gson = new Gson();
			String jsonResponse = gson.toJson(monthDashboardInfo);
			out.write(jsonResponse);

		}

		catch (Exception e) {
			jo.put("message", e.getMessage());
			jo.put("status", "0");
			out.write(jo.toString());
			log.error("GetDashBorad.java   ::: Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
