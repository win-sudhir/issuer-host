package com.netc.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

@WebServlet("/user/getdashboard")
public class Dashboard extends HttpServlet {
	static Logger log = Logger.getLogger(Dashboard.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		JSONObject jo = null;
		StringBuffer jb = new StringBuffer();
		String line = null;
		String dashboard = null;
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			DAOManager dm = new DAOManager();
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}

			MemoryComponent.closeBufferedReader(reader);
			jo = new JSONObject();

			String userId = request.getParameter("userId");

			Calendar calendar = Calendar.getInstance();
			String currdate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			calendar.add(Calendar.DATE, -1);
			String yestdate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			log.info("CURDATE : '" + currdate + "' , YESDATE : '" + yestdate + "'");

			String auth_token = request.getHeader("Authorization").toString();
			String userIdS = request.getHeader("userId").toString();

			log.info("auth_token :::: " + auth_token + "--- userId ---" + userId + " userIdS " + userIdS);

			if (userId != null && !userId.equalsIgnoreCase("") && !userId.startsWith("C") && !userId.startsWith("B")) {
				log.info("Dashbord.java   :::  With User ID Param user id :: " + userId);
				dashboard = dm.getDashboard(userId, "1", currdate, yestdate);
				out.write(dashboard);

			} else if (userId.startsWith("C")) {
				log.info("Dashbord.java   :::  For Customer :: " + userId);
				dashboard = dm.getDashboard(userId, conn);
				out.write(dashboard);
			} else if (userId.startsWith("B")) {
				log.info("Dashbord.java   :::  For Branch :: " + userId);
				dashboard = dm.getBranchDashboard(userIdS, conn);
				out.write(dashboard);
			} else {
				log.info("Invalid Input Params ::: User Id '" + userId);
			}

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

}
