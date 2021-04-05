package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class TagDAO {
	static Logger log = Logger.getLogger(TagDAO.class.getName());
	public JSONArray getTagData(String poid, Connection conn)
	{
		//Connection conn = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		String tid, tag_id, signature_data, barcode_data, tag_pwd,  kill_pwd, date,tag_class_id,user_memory;
		//String sql = null;
		JSONObject obj = null;
		JSONArray out=new JSONArray();
		try {
			//conn = DatabaseManager.getConnection();
			String sql = "select v.tid, tag_id, signature_data, barcode_data, tag_pwd, kill_pwd, v.rodt,v.tag_class_id FROM vehicle_tag_linking v INNER JOIN inventory_master i ON i.po_id=? AND i.tid=v.tid" ;
			st = conn.prepareStatement(sql);
			st.setString(1, poid);
			rs = st.executeQuery();
			while (rs.next()) 
			{
				obj=new JSONObject();
				tid = rs.getString("tid");
				tag_id = rs.getString("tag_id");
				tag_class_id = rs.getString("tag_class_id");
				signature_data = rs.getString("signature_data"); 
				barcode_data = rs.getString("barcode_data");
				tag_pwd = rs.getString("tag_pwd");
				kill_pwd = rs.getString("kill_pwd");
				date = rs.getString("rodt");
				user_memory = "585858585858585858585858"+getVC(tag_class_id)+"00"+signature_data;
				obj.put("tid", tid);
				obj.put("tag_id", tag_id);
				obj.put("tag_class_id", tag_class_id);
				obj.put("signature_data", signature_data);
				obj.put("barcode_data", barcode_data);
				obj.put("tag_pwd", tag_pwd);
				obj.put("kill_pwd", kill_pwd);
				obj.put("date", date);
				obj.put("user_memory", user_memory);
				out.put(obj);
				}
			log.info(""+out.toString());
			return out;

		} catch (Exception e) {
			log.info("Exception in getChallanData :: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeStatement(st);
			//DatabaseManager.closeConnection(conn);
		}
		return out;
	}
	
	private String getVC(String vcCode) {
		if(vcCode.equals("VC4")||vcCode.equals("VC5")||vcCode.equals("VC6")||vcCode.equals("VC7")||vcCode.equals("VC8")||vcCode.equals("VC9"))
			return "0"+vcCode.substring(2);
				
		else if(vcCode.equals("VC10"))
			return "0A";
		else if(vcCode.equals("VC11"))
			return "0B";
		else if(vcCode.equals("VC12"))
			return "0C";
		else if(vcCode.equals("VC13"))
			return "0D";
		else if(vcCode.equals("VC14"))
			return "0E";
		else if(vcCode.equals("VC15"))
			return "0F";
		else		
			return "00";
	}
	
	public List<List<String>> downloadExcel(String poId)
	{
		List<List<String>> excelData = null;
		List<String> headerNames = new ArrayList<String>();
		
		String tid, tag_id, sagnature_data, barcode_data, tag_pwd,  kill_pwd, date,tag_class_id,user_memory;
		Connection con = null;
		//Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		//String sql = "select v.tid, tag_id, signature_data, barcode_data, tag_pwd, kill_pwd, v.rodt FROM vehicle_tag_linking v INNER JOIN inventory_master i ON i.po_id='"+po_id+"'"+" AND i.tid=v.tid AND i.status='0'" ;
		//String sql = "select v.tid, tag_id, signature_data, barcode_data, tag_pwd, kill_pwd, v.rodt,v.tag_class_id FROM vehicle_tag_linking v INNER JOIN inventory_master i ON i.po_id=? AND i.tid=v.tid AND i.status=?" ;
		String sql = "select v.tid, tag_id, signature_data, barcode_data, tag_pwd, kill_pwd, v.rodt,v.tag_class_id FROM vehicle_tag_linking v INNER JOIN inventory_master i ON i.po_id=? AND i.tid=v.tid" ;
		try
		{
			con = DatabaseManager.getConnection();//new DBConnection().getConnection();
			//stmt = con.createStatement();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, poId);
			//pstmt.setString(2, "0");
			//rs = stmt.executeQuery(sql);
			rs = pstmt.executeQuery();
			excelData = new ArrayList<List<String>>();
			headerNames.add("TID");
			headerNames.add("Tag_ID");
			headerNames.add("TAG_CLASS");
			headerNames.add("Signature_Data");
			headerNames.add("Barcode_Data");
			headerNames.add("Tag_PWD");
			headerNames.add("Kill_PWD");
			headerNames.add("Date");
			headerNames.add("UserMemory");
			excelData.add(headerNames);
			
			//if (rs!=null) 
			while(rs.next())
			{
				List<String> tagList = new ArrayList<String>();
				tid = rs.getString("tid");
				tag_id = rs.getString("tag_id");
				tag_class_id = rs.getString("tag_class_id");
				sagnature_data = rs.getString("signature_data"); 
				barcode_data = rs.getString("barcode_data");
				tag_pwd = rs.getString("tag_pwd");
				kill_pwd = rs.getString("kill_pwd");
				date = rs.getString("rodt");
				tagList.add(tid);
				tagList.add(tag_id);
				tagList.add(tag_class_id);
				tagList.add(sagnature_data);
				tagList.add(barcode_data);
				tagList.add(tag_pwd);
				tagList.add(kill_pwd);
				tagList.add(date);
				user_memory = "585858585858585858585858"+getVC(tag_class_id)+"00"+sagnature_data;
				tagList.add(user_memory);
				
				excelData.add(tagList);
				tagList = null;
	 		}
			
			log.info("Successfully fetch tagdata........."+excelData);
		    //getdata = true;
		    //log.info("Successfully fetch tagdata........."+tagdata);
		}
		catch(Exception e)
		{
			log.info("Something wrong while fetching..."+e);
		}
		finally
		{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(pstmt);
			DatabaseManager.closeConnection(con);
			
		}
		return excelData;
	}
}
