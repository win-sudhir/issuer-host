/**
 * 
 */
package com.winnovature.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;


public class DocumentDao 
{
	static Logger log = Logger.getLogger(DocumentDao.class.getName());
	
	public JSONObject getDocumnetDetails(String customerId, String reqType)
	{
		log.info("customerId :: "+customerId+" reqType :: "+reqType);
		JSONObject resp = new JSONObject();
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String query = null;
		if(reqType.equalsIgnoreCase("addressProof"))
		{
			query = "SELECT address_proof_doc_path as doc from customer_kyc_master where is_deleted=0 and cust_id=?";
		}
		else if(reqType.equalsIgnoreCase("idProof"))
		{
			query = "SELECT id_proof_doc_path as doc from customer_kyc_master where is_deleted=0 and cust_id=?";
		}
		else if(reqType.equalsIgnoreCase("applicationForm"))
		{
			query = "SELECT application_form_doc_path as doc from customer_kyc_master where is_deleted=0 and cust_id=?";
		}
		else if(reqType.equalsIgnoreCase("rcBook"))
		{
			query = "select path_rc_book as doc from customer_vehicle_master c, vehicle_document_master v where v.vehicle_number=c.vehicle_number and c.cust_id=?";
		}
		else if(reqType.equalsIgnoreCase("insuranceDoc"))
		{
			query = "select path_insurance as doc from customer_vehicle_master c, vehicle_document_master v where v.vehicle_number=c.vehicle_number and c.cust_id=?";
		}
		
		else if(reqType.equalsIgnoreCase("frontDoc"))
		{
			query = "select path_front_pic as doc from customer_vehicle_master c, vehicle_document_master v where v.vehicle_number=c.vehicle_number and c.cust_id=?";
		}
		else if(reqType.equalsIgnoreCase("backDoc"))
		{
			query = "select path_back_pic as doc from customer_vehicle_master c, vehicle_document_master v where v.vehicle_number=c.vehicle_number and c.cust_id=?";
		}
		
		else
		{
			resp.put("doc", "NA");
			return resp;
		}
		try
		{
			con = DatabaseManager.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, customerId);
			rs = ps.executeQuery();
			if(rs.next()) 
			{
				String docPath = rs.getString("doc");
				log.info("PATH :: "+docPath);
				resp.put("doc", encodePathDocumnetToBase64String(docPath));
				return resp;
			}
		}
		catch (Exception e) 
		{
			log.info("Exception in DocummentDao : "+e.getMessage());
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);
		}
		resp.put("doc", "NA");
		return resp;
		//return null;
	}
	
	private static String encodePathDocumnetToBase64String(String srcPath) {
        File srcPathF = new File(srcPath);
        String encodedStr = null;
        try {
            if (srcPathF.exists()) {
                byte[] pdfBytes = Files.readAllBytes(srcPathF.toPath());
                encodedStr = Base64.getEncoder().encodeToString(pdfBytes);
                
            }
            else {
                encodedStr = "NA";
            }
        }
        catch (IOException e) 
        {
            log.error("DocumnetDao Getting Error   :::    ", e);
        }
        return encodedStr;
    }
}
