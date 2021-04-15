package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.winnovature.utils.DatabaseManager;

public class TagAssignmentDAO {
	static Logger log = Logger.getLogger(TagAssignmentDAO.class.getName());

	public String assignTagToUser(String tagClassId, String id, String idValue, String count, String userId,
			String startBarcode, Connection conn) {
		String status = "-1";
		PreparedStatement ps1 = null, ps2 = null, ps3 = null, ps4 = null, ps = null;
		String colIndex = null;
		ResultSet rs1 = null, rs2 = null;
		String qcolId = null;

		try {

			if (userId.equalsIgnoreCase("admin") || userId.startsWith("ST")) {
				qcolId = "select im.id from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and im.tid =(select tid from vehicle_tag_linking where vehicle_number is null and customer_id is null and barcode_data = ? and tag_class_id = ? )";
				ps = conn.prepareStatement(qcolId);
				ps.setString(1, tagClassId);
				ps.setString(2, "3");
				ps.setString(3, "0");
				ps.setString(4, "0");
				ps.setString(5, startBarcode);
				ps.setString(6, tagClassId);
			} else { // branch

				qcolId = "select im.id from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and im.tid =(select tid from vehicle_tag_linking where vehicle_number is null and customer_id is null and barcode_data= ? and tag_class_id = ? )";
				ps = conn.prepareStatement(qcolId);
				ps.setString(1, tagClassId);
				ps.setString(2, "3");
				ps.setString(3, "0");
				ps.setString(4, userId);
				ps.setString(5, startBarcode);
				ps.setString(6, tagClassId);
			}

			rs2 = ps.executeQuery();

			if (rs2 != null && rs2.next()) {
				colIndex = rs2.getString("id");
				log.info("Start from barcode and tid (Inventory master ) ColId Value : " + colIndex);

				String sql = "select im.tid from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and vtl.vehicle_number is null and vtl.customer_id is null and im.id >= ? limit "
						+ count;
				String sql1 = "update inventory_master set " + id + " = '" + idValue + "' where tid = ?";

				// branch to agent
				String q1 = "select im.tid from inventory_master im inner join vehicle_tag_linking vtl on vtl.tid = im.tid where im.tag_class_id = ? and im.status = ? and im.agent_id = ? and im.branch_id = ? and vtl.vehicle_number is null and vtl.customer_id is null and im.id >= ? limit "
						+ count;
				String q2 = "update inventory_master set " + id + " = '" + idValue + "' where tid = ?";

				// String q2 = "update inventory_master set agent = '" + idValue
				// + "' where tid = ?";

				if (userId.equalsIgnoreCase("admin") || userId.startsWith("ST")) {
					log.info("QUERY1 :: " + sql);
					ps1 = conn.prepareStatement(sql);
					ps1.setString(1, tagClassId);
					ps1.setString(2, "3");
					ps1.setString(3, "0");
					ps1.setString(4, "0");
					ps1.setString(5, colIndex);
					rs1 = ps1.executeQuery();
					// log.info("Result Set ::: "+rs.getFetchSize());

					ps2 = conn.prepareStatement(sql1);
					while (rs1 != null && rs1.next()) {
						log.info("QUERY2 :: " + sql1);
						ps2.setString(1, rs1.getString("tid"));
						ps2.addBatch();
					}
					ps2.executeBatch();

					log.info("TagAssignmentDAO.java :: inside the admin and ST.........");
				} else { // branch

					log.info("Branch : " + userId + " ,  Tag allocated to agent id : " + idValue + "  , No of Tag :: "
							+ count);
					log.info("QUERY3 :: " + q1);
					ps3 = conn.prepareStatement(q1);
					ps3.setString(1, tagClassId);
					ps3.setString(2, "3");
					ps3.setString(3, "0");
					ps3.setString(4, userId); // login as a branch id
					ps3.setString(5, colIndex);
					rs1 = ps3.executeQuery();

					log.info("QUERY4 :: " + q2);
					ps4 = conn.prepareStatement(q2);
					while (rs1 != null && rs1.next()) {

						ps4.setString(1, rs1.getString("tid"));
						ps4.addBatch();
					}
					ps4.executeBatch();

					log.info("TagAssignmentDAO.java :: inside Branch and agent .........");
				}

				status = "1";

			} else {
				status = "2";
			}

		} catch (Exception e) {
			log.error("TagAssignmentDAO.java Getting Error   :::    " + e);
			status = e.getMessage();
		} finally {
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs1);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closePreparedStatement(ps4);
			DatabaseManager.closePreparedStatement(ps3);
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps1);
		}
		return status;
	}
}
