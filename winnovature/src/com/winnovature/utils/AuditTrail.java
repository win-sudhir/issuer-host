package com.winnovature.utils;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;

public class AuditTrail {
	static Logger log = Logger.getLogger(AuditTrail.class.getName());

	public boolean addAuditData(Connection con, String userId, String entityId, String moduleId, String actionDesc,
			Map<String, String> userData, String ipAddress) {

		Statement stmt = null;

		try {
			String data = "<UserData>";

			for (Map.Entry entry : userData.entrySet()) {
				log.info(entry.getKey() + " = " + entry.getValue());
				data = data + "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">";
			}

			/*
			 * for(int i=0;i<userData.size();i++){ log.info("DATA >>>>" +data);
			 * data=data+"<Value"+i+">"+userData.get(i).+"</Value>"+i+">"; }
			 */
			data = data + "</UserData>";
			String sql = "insert into audit_trail (user_id,module_id,action_desc,date_time,entity_id,user_data,ip_address) "
					+ "values('" + userId + "','" + moduleId + "','" + actionDesc + "',now(),'" + entityId + "','"
					+ data + "','" + ipAddress +"')";
			log.info("sql :::: " + sql);
			if (con != null) {

				stmt = con.createStatement();
				stmt.executeUpdate(sql);

			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {

			DatabaseManager.closeStatement(stmt);
		}
		return true;

	}

	public boolean addAuditDataEdit(Connection con, String userId, String entityId, String moduleId, String actionDesc,
			Map<String, String> userData, Map<String, String> oldUserData) {
		Statement stmt = null;
		try {
			String data = "<UserData>";

			for (Map.Entry entry : userData.entrySet()) {
				log.info(entry.getKey() + " = " + entry.getValue());
				data = data + "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">";
			}

			/*
			 * for(int i=0;i<userData.size();i++){ log.info("DATA >>>>" +data);
			 * data=data+"<Value"+i+">"+userData.get(i).+"</Value>"+i+">"; }
			 */
			data = data + "</UserData>";
			String dataOld = "<UserDataOld>";
			log.info("oldData::::::::::::::::::" + dataOld);
			String sql = "insert into audit_trail (user_id,module_id,action_desc,date_time,entity_id,user_data) "
					+ "values('" + userId + "','" + moduleId + "','" + actionDesc + "',now(),'" + entityId + "','"
					+ data + dataOld + "');";
			log.info("sql :::: " + sql);
			if (con != null) {
				stmt = con.createStatement();
				stmt.executeUpdate(sql);

			}
		} catch (Exception e) {
			log.error("Getting Exception   :::    ", e);
		} finally {

			try {///////////////////////// ********************88
				if (stmt != null)

					if (con != null)
						con.close();
			} catch (Exception localSQLException7) {
			}
		}
		return true;

	}

}
