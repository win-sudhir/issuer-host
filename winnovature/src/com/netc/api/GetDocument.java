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
import com.winnovature.dao.DocumentDao;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/GetDocument")
public class GetDocument extends HttpServlet {
	static Logger log = Logger.getLogger(GetDocument.class.getName());
	private static final long serialVersionUID = 1L;

	public GetDocument() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONObject jreq = new JSONObject();
		JSONObject jresp = new JSONObject();
		StringBuffer sbuff = new StringBuffer();
		String line = null;
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
				sbuff.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			jreq = new JSONObject(sbuff.toString());
			log.info("GetDocument Request :: " + jreq);

			String reqType = jreq.getString("reqType");
			String customerId = jreq.getString("customerId");

			jresp = new DocumentDao().getDocumnetDetails(customerId, reqType, conn);
			out.write(jresp.toString());
		} catch (Exception e) {
			log.error("Error in get base64 document " + e.getMessage());
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
