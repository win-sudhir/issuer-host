package com.netc.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.TagDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/tag/downloadtagdata")
public class DownloadTagData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DownloadTagData.class.getName());
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		JSONObject jreq = new JSONObject();
		StringBuffer sbuffer = new StringBuffer();
		String line = null;
		String poId = null;
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
			poId = jreq.getString("poId");

			log.info("POID : " + poId);
			JSONArray tagData = new TagDAO().getTagData(poId, conn);
			log.info("TAG DATA : " + tagData.toString());
			String fileName = poId+"_TagData.xls";
			log.info("TAG DATA : FILENAME : " + fileName);
			HSSFWorkbook hwb = new HSSFWorkbook();
	
			HSSFSheet sheet = hwb.createSheet("tagData");
			HSSFRow rowhead = sheet.createRow(0);
			int row_count = 1;
	
			rowhead.createCell((short) 0).setCellValue("TID");
			rowhead.createCell((short) 1).setCellValue("TAGID");
			rowhead.createCell((short) 2).setCellValue("TAG_CLASS");
			rowhead.createCell((short) 3).setCellValue("SIGNATURE_DATA");
			rowhead.createCell((short) 4).setCellValue("BARCODE_DATA");
			rowhead.createCell((short) 5).setCellValue("TAG_PWD");
			rowhead.createCell((short) 6).setCellValue("KILL_PWD");
			rowhead.createCell((short) 7).setCellValue("DATE");
			rowhead.createCell((short) 8).setCellValue("USER_MEMORY");
	
			for (int i = 0; i < tagData.length(); i++) {
				JSONObject gt = tagData.getJSONObject(i);
				HSSFRow row = sheet.createRow((short) row_count);
				row.createCell((short) 0).setCellValue(gt.getString("tid"));
				row.createCell((short) 1).setCellValue(gt.getString("tag_id"));
				row.createCell((short) 2).setCellValue(gt.getString("tag_class_id"));
				row.createCell((short) 3).setCellValue(gt.getString("signature_data"));
				row.createCell((short) 4).setCellValue(gt.getString("barcode_data"));
				row.createCell((short) 5).setCellValue(gt.getString("tag_pwd"));
				row.createCell((short) 6).setCellValue(gt.getString("kill_pwd"));
				row.createCell((short) 7).setCellValue(gt.getString("date")); //.substring(0,gt.getString("date").indexOf("."))
				row.createCell((short) 8).setCellValue(gt.getString("user_memory"));
				row_count++;
			}
	
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			//log.info(""+outByteStream.toString());
			hwb.write(outByteStream);
	
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename="+fileName);
			outByteStream.write(outArray);
			outByteStream.flush();
			outByteStream.close();
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DatabaseManager.commitConnection(conn);
		}

	}
}
