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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dao.CheckSession;
import com.winnovature.dao.DashboardDAO;
import com.winnovature.dto.DashboardDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

/**
 * Servlet implementation class DashboardPiechart
 */
@WebServlet("/user/dashboardpiechart")
public class DashboardPiechart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DashboardPiechart.class.getClass());
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		String finalResponse = null;
		Gson gson = new GsonBuilder().create();
		DashboardDTO dashboardDTO = new DashboardDTO();
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}			
			dashboardDTO = new DashboardDAO().getDashboardDetails(conn);
			finalResponse = gson.toJson(dashboardDTO);
			log.info("*****************Response to user/dashboardpiecharts API()****************");
			out.write(finalResponse);
			log.info(finalResponse);

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
