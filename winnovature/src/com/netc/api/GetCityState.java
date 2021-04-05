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
import org.json.JSONArray;

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.CityStateDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/GetCityState")
public class GetCityState extends HttpServlet {
	static Logger log = Logger.getLogger(GetCityState.class.getName());
	private static final long serialVersionUID = 1L;

	public GetCityState() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String loginuserId = request.getHeader("userId").toString();
		PrintWriter out = response.getWriter();
		JSONArray cityState = new JSONArray();

		String type = request.getParameter("type");
		String rtoCode = request.getParameter("rtoCode");
		CityStateDAO dao = new CityStateDAO();
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
			if (type.equalsIgnoreCase("city"))//
			{
				cityState = dao.getCityList(rtoCode);
			} else if (type.equalsIgnoreCase("state"))//
			{
				cityState = dao.getStateList(rtoCode);
			} else if (type.equalsIgnoreCase("cityAll"))// NA
			{
				cityState = dao.getAllCityList(rtoCode);
			} else if (type.equalsIgnoreCase("stateAll"))// NA
			{
				cityState = dao.getAllStateList(rtoCode);
			}
			out.write(cityState.toString());
			log.info("*****************Response to GetCityState API()****************");
			log.info("--------------------------------------------------------------------------------");
			log.info(cityState.length());
			log.info("--------------------------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception occured in GetCityState.java");
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
