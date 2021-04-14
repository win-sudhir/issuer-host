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
import com.winnovature.dao.TagAssignmentDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/tag/assignment")
public class TagAssignment extends HttpServlet {
	static Logger log = Logger.getLogger(TagAssignment.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("TagAssignment.java :: Calling.....");
		String status = "0";
		JSONObject jreq = null;
		JSONObject jresp = new JSONObject();
		PrintWriter out = response.getWriter();
		TagAssignmentDAO tagAssignmentDAO = null;

		StringBuffer sbuffer = new StringBuffer();
		String line = null;

		String id = null, count = null, startBarcode = null, idValue = null;

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
			String tagClassId = jreq.getString("tagClassId");
			log.info("TagClassId : " + tagClassId);
			log.info("************* TagAssignment to Branch and Sales Agent  ***************");
			log.info("TagAssignment.java :: request params : " + jreq.toString());
			log.info("*************  *****************  *************** ********** ");

			String userid = null;
			if (jreq.has("userId")) {
				userid = jreq.getString("userId");
			} else {
				userid = request.getHeader("userId").toString();
			}

			log.info(" User Id In Header : " + userid);

			if (userid != null) {
				if (userid.equalsIgnoreCase("admin") || userid.startsWith("ST")) {
					if (jreq.has("agentId")) {
						id = "agent_id";
						idValue = jreq.getString("agentId");
					} else if (jreq.has("branchId")) {
						id = "branch_id";
						idValue = jreq.getString("branchId");
					} else
						id = "NA";
				}

				else // branch
				{
					if (jreq.has("agentId")) {
						id = "agent_id";
						idValue = jreq.getString("agentId");
					} else {
						log.info("TagAssignment agentId params not found!!");
					}
				}

				count = jreq.getString("count");
				startBarcode = jreq.getString("startBarCode");

				log.info("TagAssignment.java ::::   id : " + id + "  idValue  :: " + idValue + " , count : " + count);

				if (!id.equalsIgnoreCase("NA") && idValue != null && !idValue.equals("") && count != null
						&& !count.equals("")) {
					tagAssignmentDAO = new TagAssignmentDAO();
					status = tagAssignmentDAO.assignTagToUser(tagClassId, id, idValue, count, userid, startBarcode, conn);
				}

				if (status.equalsIgnoreCase("1")) {
					jresp.put("message", "Tags allocated Successfully to " + id + " = " + idValue);
					jresp.put("status", "1");
				}

				else if (status.equalsIgnoreCase("2")) {
					jresp.put("message", "Barcode data is not available For " + tagClassId
							+ " Please Enter the correct Barcode. '" + startBarcode
							+ " ' or Tag Class Id , May be Allocated to another Branch or Sales Agent Or Customer.");
					jresp.put("status", "0");
				}

				else if (status.equalsIgnoreCase("-2")) {
					jresp.put("message", "Error Occurred while allocating Tags to " + idValue);
					jresp.put("status", "0");
				} else {
					jresp.put("message", status);
					jresp.put("status", "0");
				}
			}

			else {
				jresp.put("flag", "0");
			}
			out.write(jresp.toString());
			log.info(jresp.toString());
		}

		catch (Exception e) {
			log.error("Getting Exception   :::    " + e);
			jresp.put("message", e.getMessage());
			jresp.put("status", "0");
			out.write(jresp.toString());
			log.info(jresp.toString());
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}
}