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
import com.winnovature.dao.GenerateTagData;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/tag/generatetagdata")
public class GenerateTag extends HttpServlet {
	static Logger log = Logger.getLogger(GenerateTag.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		JSONObject jsonObject = null;
		JSONObject jo = null;
		StringBuffer jb = new StringBuffer();
		String line = null;

		// boolean flag = false;
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			GenerateTagData generateTagDAO = new GenerateTagData();
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			jsonObject = new JSONObject(jb.toString());
			jo = new JSONObject();
			log.info("TAG GENERATION ");

			String tagInfo = jsonObject.getString("tagInfo");
			log.info("TAG INFORAMTION : " + tagInfo);
			String sResp = generateTagDAO.generateTagData(tagInfo, conn);
			log.info(" sResp ::"+sResp);
			if (sResp.equalsIgnoreCase("CS")) {
				jo.put("message", "New tag data generated successfully.");
			} else {
				jo.put("message", sResp);
			}

			log.info("Response For Tag Generation :: " + jo.toString());
			out.write(jo.toString());

		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {
			MemoryComponent.closePrintWriter(out);
			DatabaseManager.commitConnection(conn);
		}

	}

}
