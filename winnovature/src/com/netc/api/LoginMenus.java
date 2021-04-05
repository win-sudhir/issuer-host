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

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.MenuSubMenuDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/user/getmenu")
public class LoginMenus extends HttpServlet {
	static Logger log = Logger.getLogger(LoginMenus.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		JSONObject jo = null;
		// StringBuffer jb = new StringBuffer();
		// String line = null;

		String getMod = null;
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			log.info("LoginMenus.java auth token " + request.getHeader("Authorization").toString());
			MenuSubMenuDAO mdm = new MenuSubMenuDAO();
			/*
			 * BufferedReader reader = request.getReader(); while ((line =
			 * reader.readLine()) != null) { jb.append(line); }
			 * CommonComponent.closeBufferedReader(reader);
			 */

			// log.info("LoginMenus.java auth token
			// "+request.getHeader("Authorization").toString());
			String roleId = request.getParameter("roleId");
			String userId = request.getHeader("userId").toString();
			// String auth_token =
			// request.getHeader("Authorization").toString();//request.getParameter("auth_token");

			jo = new JSONObject();

			// if( userId != null && auth_token != null && LoginDao.isValidSession(userId,
			// auth_token))
			// {
			getMod = mdm.getLoginMenu(conn, roleId, userId);
			// jo.put("MenuList",checkMenu);

			out.write(getMod);
			/*
			 * }
			 * 
			 * else { log.info("Invalid Session : "+auth_token+"  , flag = 0 ");
			 * jo.put("flag","0"); out.write(jo.toString()); }
			 */

		} catch (Exception e) {

			jo.put("message", e.getMessage());
			jo.put("status", "0");
			out.write(jo.toString());
			log.error("LoginMenus.java   ::  Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
