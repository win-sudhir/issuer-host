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

@WebServlet("/purchaseorder/polist")
public class PurchaseOrderList extends HttpServlet {
	static Logger log = Logger.getLogger(PurchaseOrderList.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		StringBuffer jb = new StringBuffer();
		String line = null;
		JSONObject jo = null;
		String polist = null;
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

			String userId = request.getHeader("userId").toString();
			
			String poId = request.getParameter("poid");
			if (poId == null) {
				polist = dm.getPoList(userId);
				log.info("polist ::"+polist);
				out.write(polist.toString());
			} else {
				polist = dm.getSinglePoList(poId);
				log.info("polist ::"+polist);
				out.write(polist.toString());
			}

			/*
			 * } else { jo = new JSONObject(); jo.put("flag","0"); out.write(jo.toString());
			 * }
			 */

		}

		catch (Exception e) {
			jo = new JSONObject();
			jo.put("Message", e.getMessage());
			jo.put("Status", false);
			out.write(jo.toString());
			log.error("GetPOList.java   ::  Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

	/*
	 * protected void doPost(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException {
	 * 
	 * boolean checkdelete = false ; JSONObject jreq = new JSONObject(); JSONObject
	 * jresp = new JSONObject();
	 * 
	 * PrintWriter out = response.getWriter(); DataManager custDao = new
	 * DataManager();
	 * 
	 * StringBuffer sbuffer = new StringBuffer(); String line = null;
	 * 
	 * String agent_id = null;
	 * 
	 * try{
	 * 
	 * BufferedReader reader = request.getReader(); while ((line =
	 * reader.readLine()) != null) { sbuffer.append(line); }
	 * 
	 * jreq = new JSONObject(sbuffer.toString()); jresp = new JSONObject(); agent_id
	 * = jreq.getString("agentId");
	 * 
	 * log.info("Agent id : "+agent_id);
	 * 
	 * checkdelete = custDao.deleteAgent(agent_id); if(checkdelete) {
	 * 
	 * jresp.put("Message","Agent Deleted sucessfully"); jresp.put("Status", true);
	 * } else { jresp.put("Message","Sorry, Can't delete...!!!");
	 * //jresp.put("Status", false); }
	 * 
	 * } catch(Exception e){ log.error("Getting Exception   :::    ",e); }
	 * 
	 * out.write(jresp.toString()); log.info(jresp.toString()); out.flush();
	 * out.close(); }
	 */

}
