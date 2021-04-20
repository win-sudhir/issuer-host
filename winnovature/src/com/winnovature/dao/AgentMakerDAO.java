package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.constants.WINConstants;
import com.winnovature.dto.AgentDTO;
import com.winnovature.dto.AgentDashboardDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class AgentMakerDAO {
	static Logger log = Logger.getLogger(AgentMakerDAO.class.getName());

	public String addAgent(AgentDTO agentDTO, String userId, Connection conn) {
		PreparedStatement ps = null;
		String currentDate = new DateUtils().getCurrnetDate();
		try {

			String query = "INSERT INTO agent_info (agent_id, bank_agent_id, branch_id, agent_name, contact_person_name, email_id, contact_number, status, created_by, created_on, status_message) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, agentDTO.getAgentId());
			ps.setString(2, agentDTO.getBankAgentId());
			ps.setString(3, agentDTO.getBranchId());
			ps.setString(4, agentDTO.getAgentName());
			ps.setString(5, agentDTO.getContactPersonName());
			ps.setString(6, agentDTO.getEmailId());
			ps.setString(7, agentDTO.getContactNumber());
			ps.setString(8, WINConstants.NEW);
			ps.setString(9, userId);
			ps.setString(10, currentDate);
			ps.setString(11, WINConstants.NEWREQ);
			if (ps.executeUpdate() > 0) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public String approveAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
		PreparedStatement ps = null;
		String sql = "UPDATE agent_info set status=?, modified_by=?, modified_on= ?, status_message=? where agent_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, WINConstants.ACTPENDING);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, WINConstants.ACTREQ);
			ps.setString(5, agentDTO.getAgentId());
			if (ps.executeUpdate() > 0) {
				log.info("agent rejected successfully.");
				return "1";
			} else {
				return "0";
			}

		} catch (Exception e) {
			log.info(("Exception in rejectAgent() :: " + e.getMessage()));
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public String rejectAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
		PreparedStatement ps = null;
		String sql = "UPDATE agent_info set status=?, modified_by=?, modified_on= ?, status_message=? where agent_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, WINConstants.REJPENDING);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, WINConstants.REJREQ);
			ps.setString(5, agentDTO.getAgentId());
			if (ps.executeUpdate() > 0) {
				log.info("agent rejected successfully.");
				return "1";
			} else {
				return "0";
			}

		} catch (Exception e) {
			log.info(("Exception in rejectAgent() :: " + e.getMessage()));
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public List<AgentDTO> getAgentList(String userId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AgentDTO> lst = new ArrayList<AgentDTO>();
		String query = null;
		try {
			if (userId.equalsIgnoreCase("admin")) {
				query = "select * from agent_info WHERE status<>'DELETE' order by created_on desc";
			} else {
				query = "select * from agent_info WHERE created_by=? AND status in ('ACTIVE','APPROVE','REJECT') order by created_on desc";
			}
			ps = conn.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			AgentDTO agentDTO = null;
			while (rs.next()) {
				// agent_id, agent_name, contact_person_name, email_id, contact_number, status,
				// created_by, created_on, approved_by, approved_on, modified_by, modified_on
				agentDTO = new AgentDTO();
				agentDTO.setAgentId(rs.getString("agent_id"));
				agentDTO.setAgentName(rs.getString("agent_name"));
				agentDTO.setContactPersonName(rs.getString("contact_person_name"));
				agentDTO.setEmailId(rs.getString("email_id"));
				agentDTO.setContactNumber(rs.getString("contact_number"));
				agentDTO.setStatus(rs.getString("status"));
				agentDTO.setCreatedBy(rs.getString("created_by"));
				agentDTO.setCreatedOn(rs.getString("created_on"));
				agentDTO.setStatusMessage(rs.getString("status_message"));
				agentDTO.setRejectReason(rs.getString("reject_reason"));
				lst.add(agentDTO);
			}
			return lst;
		} catch (Exception e) {
			log.error("Exception in getting list of getAgentList() :: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;
	}

	public String deleteAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
		PreparedStatement ps = null;
		String sql = "UPDATE agent_info set status=?, modified_by=?, modified_on= ?, status_message=? where agent_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, WINConstants.DELPENDING);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, WINConstants.DELREQ);
			ps.setString(5, agentDTO.getAgentId());
			if (ps.executeUpdate() > 0) {
				log.info("agent delete request initiated successfully.");
				return "1";
			} else {
				return "0";
			}

		} catch (Exception e) {
			log.info(("Exception in deleteAgent() :: " + e.getMessage()));
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public AgentDTO getAgentById(String agentId, Connection conn) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		AgentDTO agentDTO = new AgentDTO();
		try {
			String query = "SELECT * FROM agent_info WHERE agent_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, agentId);
			rs = ps.executeQuery();
			if (rs.next()) {
				// agent_id, agent_name, contact_person_name, email_id, contact_number, status,
				// created_by, created_on, approved_by, approved_on, modified_by, modified_on
				agentDTO.setAgentId(rs.getString("agent_id"));
				agentDTO.setAgentName(rs.getString("agent_name"));
				agentDTO.setContactPersonName(rs.getString("contact_person_name"));
				agentDTO.setEmailId(rs.getString("email_id"));
				agentDTO.setContactNumber(rs.getString("contact_number"));
				agentDTO.setStatus(rs.getString("status"));
				agentDTO.setCreatedBy(rs.getString("created_by"));
				agentDTO.setCreatedOn(rs.getString("created_on"));
			}
			return agentDTO;
		}

		catch (Exception e) {
			log.error("getAgentById()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return agentDTO;
	}

	public String updateAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String currentDate = new DateUtils().getCurrnetDate();
		PreparedStatement ps = null;
		String sql = "UPDATE agent_info set status=?, modified_by=?, modified_on= ?, status_message=? where agent_id = ?";
		try {
			addEditedAgent(agentDTO, conn);
			ps = conn.prepareStatement(sql);
			ps.setString(1, WINConstants.DELPENDING);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, WINConstants.DELREQ);
			ps.setString(5, agentDTO.getAgentId());
			if (ps.executeUpdate() > 0) {
				log.info("agent updated successfully.");
				return "1";
			} else {
				return "0";
			}

		} catch (Exception e) {
			log.info(("Exception in updateAgent() :: " + e.getMessage()));
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public String addEditedAgent(AgentDTO agentDTO, Connection conn) {
		PreparedStatement ps = null;
		try {

			String query = "INSERT INTO agent_info_edited (agent_id, agent_name, contact_person_name, email_id, contact_number) "
					+ "VALUES (?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, agentDTO.getAgentId());
			ps.setString(2, agentDTO.getAgentName());
			ps.setString(3, agentDTO.getContactPersonName());
			ps.setString(4, agentDTO.getEmailId());
			ps.setString(5, agentDTO.getContactNumber());

			if (ps.executeUpdate() > 0) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
		return "0";
	}

	public int getDashboardActiveVehicle(String userId, Connection conn) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "select count(cv.user_id) as activeVehicle "
					+ "from customer_vehicle_info cv, customer_info ci "
					+ "where ci.user_id=cv.user_id and ci.parent_id= ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("activeVehicle");
			}

		} catch (Exception e) {
			log.error("getAgentById()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return 0;
	}

	public int getDashboardTotalCustomers(String userId, Connection conn) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "select count(user_id) as totalCustomers from customer_info where parent_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("totalCustomers");
			}

		} catch (Exception e) {
			log.error("getAgentById()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return 0;
	}

	public int getDashboardAllocateTags(String userId, Connection conn) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "select count(tid)  as 'allocatedTags' from inventory_master where agent_id= ? and status = 4";
			ps = conn.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {

				return rs.getInt("allocatedTags");
			}

		} catch (Exception e) {
			log.error("getAgentById()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return 0;
	}

	public int getDashboardAvailableTags(String userId, Connection conn) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "select count(tid)  as 'availableTags' from inventory_master where agent_id= ? and status = 3";
			ps = conn.prepareStatement(query);
			ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {

				return rs.getInt("availableTags");
			}

		} catch (Exception e) {
			log.error("getAgentById()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return 0;
	}

}
