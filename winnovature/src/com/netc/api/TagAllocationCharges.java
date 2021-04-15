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
import com.winnovature.dao.TagRegChargesDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/tag/allocationcharges")
public class TagAllocationCharges extends HttpServlet {
	static Logger log = Logger.getLogger(TagAllocationCharges.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jreq = new JSONObject();
		JSONObject jresp = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer sbuffer = new StringBuffer();
		String line = null;
		String vehicleNumber;
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sbuffer.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			jreq = new JSONObject(sbuffer.toString());
			jresp = new JSONObject();
			log.info("TagAllocationCharges Request :: " + jreq);
			vehicleNumber = jreq.getString("vehicleNumber");
			String vcId = new TagRegChargesDAO().getVehicleClass(vehicleNumber, conn);
			if (vcId != null) {
				jresp = new TagRegChargesDAO().getTagAllocationCharges(vcId, conn);
				jresp.put("status", "1");
			} else {
				// jresp.put("Message", "TagAllocationCharges not exists");
				jresp.put("status", "0");
				log.info("Tag class id not found or vehicle not found");
			}
			out.write(jresp.toString());
			log.info("*****************Response to /tag/allocationcharges API()****************");
			log.info(jresp.toString());
		} catch (Exception e) {
			log.error("Exception in TagAllocationCharges.java >>> " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
