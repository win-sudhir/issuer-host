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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dao.CheckSession;
import com.winnovature.dto.TagSinglePageDTO;
import com.winnovature.service.TagSinglePageService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

/**
 * Servlet implementation class TagSinglePageSummary
 */
@WebServlet("/tag-mp/summary")
public class TagSinglePageSummary extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(TagSinglePageSummary.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TagSinglePageSummary() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String finalResponse = null;
		
		Gson gson = new GsonBuilder().create();
		Connection conn = null;
		try {
			
			conn = DatabaseManager.getAutoCommitConnection();
			
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			
			
			TagSinglePageDTO tagSignedData = new TagSinglePageService().getTagSummary();
			finalResponse = gson.toJson(tagSignedData);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to /customer/add API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			MemoryComponent.closePrintWriter(out);
			DatabaseManager.commitConnection(conn);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
