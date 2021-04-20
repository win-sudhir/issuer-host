package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.constants.WINConstants;
import com.winnovature.dto.AgentDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;

public class AgentDAO {
	static Logger log = Logger.getLogger(AgentDAO.class.getName());

	public String addAgent(AgentDTO agentDTO, String userId, Connection conn) {
		PreparedStatement ps = null;
		String currentDate = new DateUtils().getCurrnetDate();
		try {

			String query = "INSERT INTO agent_info (agent_id, agent_name, contact_person_name, email_id, contact_number, status, created_by, created_on) "
					+ "VALUES (?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, agentDTO.getAgentId());
			ps.setString(2, agentDTO.getAgentName());
			ps.setString(3, agentDTO.getContactPersonName());
			ps.setString(4, agentDTO.getEmailId());
			ps.setString(5, agentDTO.getContactNumber());
			ps.setString(6, WINConstants.NEW);
			ps.setString(7, userId);
			ps.setString(8, currentDate);
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
		String sql = "UPDATE agent_info set status=?, modified_by=?, modified_on= ? where agent_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, WINConstants.APPROVE);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, agentDTO.getAgentId());
			if (ps.executeUpdate() > 0) {
				log.info("Agent Approve Successfully.");
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
		String sql = "UPDATE agent_info set status=?, modified_by=?, modified_on= ? where agent_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, WINConstants.REJECT);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, agentDTO.getAgentId());
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
				query = "select * from agent_info WHERE status not in ('DELETE') order by created_on desc";
				ps = conn.prepareStatement(query);
			} else {
				query = "select * from agent_info WHERE branch_id = ? and status not in ('DELETE') order by created_on desc";
				ps = conn.prepareStatement(query);
				ps.setString(1, userId);
			}
			rs = ps.executeQuery();
			AgentDTO agentDTO = null;
			while (rs.next()) {
				agentDTO = new AgentDTO();
				agentDTO.setAgentId(rs.getString("agent_id"));
				agentDTO.setAgentName(rs.getString("agent_name"));
				agentDTO.setContactPersonName(rs.getString("contact_person_name"));
				agentDTO.setEmailId(rs.getString("email_id"));
				agentDTO.setContactNumber(rs.getString("contact_number"));
				agentDTO.setStatus(rs.getString("status"));
				agentDTO.setCreatedBy(rs.getString("created_by"));
				agentDTO.setCreatedOn(rs.getString("created_on"));
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
		String sql = "UPDATE agent_info set status=?, modified_by=?, modified_on= ? where agent_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, WINConstants.DELETE);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, agentDTO.getAgentId());
			if (ps.executeUpdate() > 0) {
				log.info("agent updated successfully.");
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
		String sql = "UPDATE agent_info set agent_name=?, contact_person_name=?, email_id=?, contact_number=?, modified_by=?, modified_on= ? where agent_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, agentDTO.getAgentName());
			ps.setString(2, agentDTO.getContactPersonName());
			ps.setString(3, agentDTO.getEmailId());
			ps.setString(4, agentDTO.getContactNumber());
			ps.setString(5, userId);
			ps.setString(6, currentDate);
			ps.setString(7, agentDTO.getAgentId());
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

}
