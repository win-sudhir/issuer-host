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

@WebServlet("/tag/barcodedata")
public class GetBarcodeData extends HttpServlet {
	static Logger log = Logger.getLogger(GetBarcodeData.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String vehicleNumber = request.getParameter("vehicleNumber");
		log.info("GetBarcodeData.java vehicleNumber  = " + vehicleNumber);
		JSONObject jo = null;
		PrintWriter out = response.getWriter();

		StringBuffer jb = new StringBuffer();
		String line = null;

		String getBarCode = null;
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

			String userId = request.getHeader("userId").toString();
			getBarCode = dm.getBarcode(userId, vehicleNumber, conn);
			out.write(getBarCode);

		} catch (Exception e) {
			jo.put("message", e.getMessage());
			jo.put("status", "0");
			out.write(jo.toString());
			log.error("GetBarcodeData.java   :::  Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
