package com.winnovature.recon.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.TagDetailsDAO;
import com.winnovature.service.NPCICallService;
import com.winnovature.service.NPCIXMLParser;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.PropertyReader;



@WebServlet("/tag/excemanagement")
public class ManageExcetpion extends HttpServlet
{
	static Logger log = Logger.getLogger(ManageExcetpion.class.getName());

	private static final long serialVersionUID = 1L;

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		log.info("ManageExcetpion.java ::: doPost() :: Request Received");

		PrintWriter out = response.getWriter();
		JSONObject jsonObject=null;
		StringBuffer jb = new StringBuffer();
		String line = null;	
		JSONObject respJSON = new JSONObject();

		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null)
			jb.append(line);

		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();
			
			jsonObject = new JSONObject(jb.toString());
			String opid = jsonObject.getString("opid");						
			if(opid != null && (opid.equalsIgnoreCase("ADD") || opid.equalsIgnoreCase("REMOVE")))
			{
				String tagId = jsonObject.getString("tagId");		
				String excCode = jsonObject.getString("excCode");
				String insertionFlag = jsonObject.getString("insertionFlag");

				//Add tag in NPCI MAPPER's Exception List
				// respJSON = new ExceptionDetailDaoImpl().manageTagException(tagId, "", "", excCode, insertionFlag, opid);
				
				String issuerSwitchUrl = PropertyReader.getPropertyValue("issuerSwitchUrl");
				String reqParams = "opid="+opid+"&tagId="+tagId+"&seqNum=1"+"&excCode="+excCode+"&ttype=ReqMngMultipleExceptions";

				String respXML = NPCICallService.httpServerCall(issuerSwitchUrl, reqParams);	
				
				log.info("respXML :: "+respXML);

				if(respXML != null)
				{
					try 
					{
						Map<String,String> parsedXml = new NPCIXMLParser().parseData(respXML);						

						String npciRespResult = parsedXml.get("TxnRespTagResult");
						
						log.info("npciRespResult :: "+npciRespResult);

						if(npciRespResult != null && npciRespResult.equalsIgnoreCase("SUCCESS"))
						{
							respJSON = new TagDetailsDAO().manageTagException(tagId, "", "", excCode, insertionFlag, opid, conn);							
						}
						else
						{
							respJSON.put("status",-1);
							respJSON.put("resp_msg","Error Response not received from NPCI.");
							respJSON.put("operation",opid);
						}							
					} 
					catch (Exception e)
					{							
						log.error("ManageExcetpion.java ::: Error Occurred after NPCI Response : "+e.getMessage());
						e.printStackTrace();
						respJSON.put("status",-1);
						respJSON.put("resp_msg","Error Occurred : "+e.getMessage());
						respJSON.put("operation",opid);
					}
				}
				else
				{		
					respJSON.put("status",-1);
					respJSON.put("resp_msg","Response not received from Issuer Switch.");
					respJSON.put("operation",opid);
				}
			}
			else
			{
				log.info("ManageExcetpion.java :: Invalid Opration : '"+opid+"'");
				respJSON.put("status",-1);
				respJSON.put("resp_msg","Invalid Operation Id received in Reques '"+opid+"'.");
				respJSON.put("operation",opid);
			}

			log.info("ManageExcetpion.java ::: Operation '"+opid+"' Final Response : "+respJSON);
		}
		catch(Exception e)
		{
			log.error("LBHandler.java :: Something Went Wrong.");

			try
			{
				respJSON.put("status",-1);
				respJSON.put("resp_msg","Something Went Wrong : "+e.getMessage());
			} 
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {
			DatabaseManager.commitConnection(conn);
			out.println(respJSON.toString());
			out.flush();
			out.close();
		}
		
	}
}