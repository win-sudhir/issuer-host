package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

//import com.netc.utils.DBConnection;

public class MenuSubMenuDAO {
	static Logger log = Logger.getLogger(MenuSubMenuDAO.class.getName());
	
	public String getLoginMenu(Connection con, String roleId,String userId){
		PreparedStatement ps = null;
		//Connection con =null;
		ResultSet rs = null;		
		JSONArray report = null;
		String query = null;
		try {
			 //con =DatabaseManager.getConnection();
			//query ="Select mm.menu_id,mm.menu_description,mm.menu_link,mm.short_name,mm.is_active,mm.is_deleted,rmm.submenu_id from role_menu_mapping rmm inner join menu_master mm on rmm.menu_id = mm.menu_id "+ 
						//"inner join role_master rm on rmm.role_id = rm.role_id where rm.role_id=?";
			query ="Select group_concat(distinct mm.menu_id) as menu_id,group_concat(distinct mm.menu_description) as menu_description,group_concat(distinct mm.menu_link) as menu_link,group_concat(distinct mm.short_name)as short_name,mm.is_active,mm.is_deleted ,group_concat(rmm.submenu_id) as submenu_id from role_menu_mapping rmm inner join menu_master mm on rmm.menu_id = mm.menu_id "+ 
					"inner join role_master rm on rmm.role_id = rm.role_id where rm.role_id=? and rm.is_deleted=? and rmm.is_deleted=? and mm.is_deleted=? group by mm.menu_id"; 
			ps = con.prepareStatement(query);
			ps.setString(1,roleId);
			ps.setString(2,"0");
			ps.setString(3,"0");
			ps.setString(4,"0");

			//ps.setString(2,toDate);		
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) 
			{
				JSONObject jo = new JSONObject();
				if(rs.getString("is_deleted").equals("1")){
					jo.put("isDeleted", true);
				}
				else{
					jo.put("isDeleted", false);
				}
				if(rs.getString("is_active").equals("1")){
					jo.put("isActive", true);
				}
				else{
					jo.put("isActive", false);
				}
				
				jo.put("menuId", rs.getString("menu_id"));
				jo.put("menuDescription", rs.getString("menu_description"));
				jo.put("menuLink", rs.getString("menu_link"));
				jo.put("menushortName", rs.getString("short_name"));
				jo.put("submenus", getLoginSubMenu(rs.getString("submenu_id")));
				report.put(jo);				
			}	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Getting Exception   :::    ",e);
		} 
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closeConnection(con);
			
		}
		JSONObject mainObj = new JSONObject();
		mainObj.put("Menus", report);
		mainObj.put("RoleId", roleId);
		mainObj.put("UserId", userId);		
		return mainObj.toString();
		
	}
	
	public JSONArray getLoginSubMenu(String MenuId){
		PreparedStatement ps = null;
		Connection con =null;
		ResultSet rs = null;		
		JSONArray report = null;
		String query = null;
		//System.out.print("Submenu :  "+MenuId);
		try {
			 con = DatabaseManager.getConnection();//new DBConnection().getConnection();
			query ="SELECT * FROM sub_menu_master where sub_menu_id IN ("+MenuId+")and is_deleted=?";
			ps = con.prepareStatement(query);
			ps.setString(1,"0");
			//ps.setString(2,toDate);		
			rs = ps.executeQuery();
			report = new JSONArray();
			while (rs.next()) 
			{
				JSONObject jo = new JSONObject();
				if(rs.getString("is_deleted").equals("1")){
					jo.put("isDeleted", true);
				}
				else{
					jo.put("isDeleted", false);
				}
				if(rs.getString("is_active").equals("1")){
					jo.put("isactive", true);
				}
				else{
					jo.put("isactive", false);
				}
				
				jo.put("submenuId", rs.getString("sub_menu_id"));
				jo.put("menuId", rs.getString("menu_id"));		
				jo.put("submenushortname", rs.getString("sub_menu_short_name"));
				jo.put("submenuurl", rs.getString("sub_menu_url"));		
				report.put(jo);				
			}	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Getting Exception   :::    ",e);
		} 
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);
			
		}
		return report;
	}
	
}
