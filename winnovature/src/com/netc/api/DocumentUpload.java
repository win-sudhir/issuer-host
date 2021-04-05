/**
 * 
 */
package com.netc.api;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.CheckSession;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.PropertyReader;





@WebServlet("/documentupload/*")
public class DocumentUpload extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DocumentUpload.class.getClass());
	//final String UPLOAD_DIRECTORY = "E:/doc/";
	final String UPLOAD_DIRECTORY = PropertyReader.getPropertyValue("tempDocDirectory");

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONObject responseJsonObject = new JSONObject();
		
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}	

			String docType = request.getRequestURI().substring(request.getRequestURI().indexOf("documentupload") + 15);
			log.info("DOCTYPE : "+docType);
			String fileName = docType + "_" + new SimpleDateFormat("yyyymmddhhmmsssss").format(new Date());
			
			String inputFileName = null, extension = null;
			log.info(ServletFileUpload.isMultipartContent(request));

			List<FileItem> multiparts = null;
			multiparts = (new ServletFileUpload(new DiskFileItemFactory())).parseRequest(request);

			Iterator<FileItem> itmes = multiparts.iterator();

			if (itmes.hasNext()) {
				FileItem item = (FileItem) itmes.next();
				if (!item.isFormField()) {
								
					inputFileName = (new File(item.getName())).getName();
					log.info("INPUT FILENAME :: "+inputFileName);
					extension = inputFileName.substring(inputFileName.indexOf("."));
					//fileName += extension;
					log.info("EXTENSION OF FILENAME :: "+extension);
					if(!extension.equalsIgnoreCase(".jpg")){
						responseJsonObject.put("message", "Only .jpg file allowed.");
						responseJsonObject.put("status", false);
						return;
					}
					
					inputFileName = new File(item.getName()).getName();
					extension = inputFileName.substring(inputFileName.indexOf("."));
					fileName = fileName + extension;
					item.write(new File(UPLOAD_DIRECTORY + fileName));
				}
			}
			responseJsonObject.put("fileName", fileName);
			responseJsonObject.put("status", true);
			
		} catch (FileUploadException e) {
			e.printStackTrace();
			responseJsonObject.put("message", e.getMessage()!=null ? e.getMessage() : "Getting Error while Uploading Document. !!");
			responseJsonObject.put("status", false);
             return;
		} catch (Exception ex) {
			ex.printStackTrace();
			responseJsonObject.put("message", ex.getMessage()!=null ? ex.getMessage() : "Getting Error while Uploading Document. !!");
			responseJsonObject.put("status", false);
		}finally {
			out.write(responseJsonObject.toString());
			DatabaseManager.commitConnection(conn);
			
			MemoryComponent.closePrintWriter(out);
		}

	}
}
