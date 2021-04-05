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
import com.winnovature.dao.TagVehicleClassDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;


@WebServlet("/tag/tagclasslist")
public class TagClassList extends HttpServlet {
	static Logger log = Logger.getLogger(TagClassList.class.getName());
	private static final long serialVersionUID = 1L;
       
    public TagClassList() {
        super();
      
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		PrintWriter out = response.getWriter();
		//String userId = request.getHeader("userId").toString();
		//String auth_token = request.getHeader("Authorization").toString();//request.getParameter("auth_token");
					
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			//if( userId != null &&  auth_token != null && LoginDao.isValidSession(userId, auth_token))
			//{
				
				TagVehicleClassDAO dm =new TagVehicleClassDAO();
				String js =null;		
				try {
					js = dm.getTagClassList();	
					out.write(js);
				} 
				catch (Exception e) 
				{
					JSONObject jo = new JSONObject();
					jo.put("message",e.getMessage());
					out.write(jo.toString());
					log.error("TagClassList.java :: Getting Exception   :::    ",e);
				}
				
			/*
			 * } else { JSONObject jo = new JSONObject(); jo.put("flag","0");
			 * out.write(jo.toString()); out.write("Session Timeout. Error !!"); }
			 */
		} 
		
		catch (Exception e) 
		{
			/*JSONObject jo = new JSONObject();
			jo.put("Message",e.getMessage());
			out.write(jo.toString());*/
			
			e.printStackTrace();
		}	
		finally
		{
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
		
	}

	
}
