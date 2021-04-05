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
import com.winnovature.dao.DAOManager;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

/**
 * Servlet implementation class GetSupplierList
 */
@WebServlet("/supplier/get")
public class GetSupplierList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(GetSupplierList.class.getName());   
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		JSONObject jo = new JSONObject();
		//StringBuffer jb = new StringBuffer();
		//String line = null;
		log.info("GetSupplierList>>>");
		String supplierList = null;
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
			DAOManager dm = new DAOManager();
			/*
			 * BufferedReader reader = request.getReader(); while ((line =
			 * reader.readLine()) != null) { jb.append(line); }
			 */

			/*
			 * CommonComponent.closeBufferedReader(reader); 
			 */
			//log.info(" auth token " + request.getHeader("Authorization").toString());
			
			String supplierId = request.getParameter("supplierId");
			
			String userId = request.getHeader("userId").toString();
			//String auth_token = request.getHeader("Authorization").toString();

			//if (userId != null && auth_token != null && LoginDao.isValidSession(userId, auth_token)) {
				if (supplierId == null) {
					log.info("GetSupplierList>>> ALL");
					supplierList = dm.getAllSupplierList(userId);
				} 
				else if (supplierId.equalsIgnoreCase("all")) {
					log.info("GetSupplierList>>> ALL");
					supplierList = dm.getAllSuppliers();
				}
				else {
					supplierList = dm.getSingleSupplierData(supplierId, userId);
				}
				out.write(supplierList);
			/*
			 * }
			 * 
			 * else { jo.put("flag", "0"); out.write(jo.toString());
			 * 
			 * }
			 */

		} catch (Exception e) {
			jo.put("message", e.getMessage());
			jo.put("status", false);
			out.write(jo.toString());
			log.error("Getting Exception   :::    ", e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

}
