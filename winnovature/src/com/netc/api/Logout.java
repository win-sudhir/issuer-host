package com.netc.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.LoginDao;
import com.winnovature.utils.MemoryComponent;


@WebServlet("/user/logout")
public class Logout extends HttpServlet {
	static Logger log = Logger.getLogger(Logout.class.getName());
	private static final long serialVersionUID = 1L;

	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("Logout.java :: getMethod");

		String userIdH = request.getHeader("userId").toString();
		String auth_tokenH = request.getHeader("Authorization").toString(); // request.getParameter("auth_token");

		log.info("Logout.java :: " + userIdH + " , auth_token :: " + userIdH);

		JSONObject jo = new JSONObject();

		PrintWriter out = response.getWriter();
		try {
			if (new LoginDao().deleteSessionIdLogout(userIdH, auth_tokenH)) {
				log.info("Delete Successfull");

				jo.put("Message", "Successfully logged out.");
				jo.put("Status", true);
				log.info(jo);
			}

			else {
				jo.put("Message", "userId and session id is not remove.");
				jo.put("Status", false);
			}

			log.info(jo);

			out.write(jo.toString());
		} catch (Exception e) {
			log.info("Exception occured in Logout.java");
		} finally {
			MemoryComponent.closePrintWriter(out);
		}

	}

}
