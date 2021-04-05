/**
 * 
 */
package com.winnovature.querries;


public class TagQueries {
	public static String getCheckFileQuery = "SELECT filename FROM tbl_temp_blacklist WHERE filename=?";
	public static String getCheckTidQuery = "SELECT tid FROM inventory_master  WHERE tid=?";
	public static String getTempInsertQuery = "INSERT INTO tbl_temp_blacklist(userid,tagid,tid,status_id,date,filename) values (?, ?,?,?,?,?)";
	public static String getProcBlackListQuery = "{CALL pr_blacklisttags(?,?)}";
	public static String getBLSummaryQuery = "SELECT filename,userid,sum(case when status_id=2 then 1 else 0 end)  'mismatched' ,sum(case when status_id=0 then 1 else 0 end)  'Unprocessed',sum(case when status_id=1 then 1 else 0 end)  'Processed' FROM tbl_temp_blacklist WHERE userid=? and filename=? ";
	
	public static String getProcAddExcepQuery = "{call pr_add_excep(?,?,?,?,?,?,?)}";
	public static String getCheckTagInExceptionQuery = "SELECT tag_id, exe_code FROM tag_exception_list WHERE tag_id=? and exe_code=?";
	public static String getTagDetailsQuery = "SELECT tag_id,tid,vehicle_number,tag_class_id FROM vehicle_tag_linking WHERE tag_id=? or tid =? or vehicle_number=?";
	public static String getTagIdByTidQuery = "SELECT tag_id FROM vehicle_tag_linking WHERE tid = ?";
	public static String getTagIdByVehicleNoQuery = "SELECT tag_id FROM vehicle_tag_linking WHERE vehicle_number = ?";
	
	public static String getCheckCustTagInfo="SELECT action1,action2,action3 FROM cust_tag_info WHERE tag_id=?";
	public static String getUpdateCustTagInfoQuery="UPDATE cust_tag_info set action1='NA' WHERE  tag_id=?";
}
