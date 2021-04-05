package com.winnovature.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.winnovature.contants.IDGenerator;
import com.winnovature.dao.AccountDAO;
import com.winnovature.dao.AddressDAO;
import com.winnovature.dao.AgentMakerDAO;
import com.winnovature.dao.VirtualAccountDAO;
import com.winnovature.dto.AccountDTO;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.AgentDTO;
import com.winnovature.dto.AgentDashboardDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VirtualAccountDTO;
import com.winnovature.utils.AuditTrail;
import com.winnovature.validation.AgentErrorCode;
import com.winnovature.validation.AgentValidation;

public class AgentMakerService {
	static Logger log = Logger.getLogger(AgentMakerService.class.getName());

	AgentMakerDAO agentMakerDAO = new AgentMakerDAO();
	ResponseDTO responseDTO = new ResponseDTO();

	public ResponseDTO addAgent(AgentDTO agentDTO, AddressDTO addressDTO, AccountDTO accountDTO, String userId,
			Connection conn, String ipAddress) {
		responseDTO = AgentValidation.validateAgentRequest(agentDTO, addressDTO, accountDTO, conn);
		if (responseDTO.getStatus().equals("0")) {
			return responseDTO;
		}

		String newUserId = new IDGenerator().getAgentId();
		agentDTO.setAgentId(newUserId);
		String response = agentMakerDAO.addAgent(agentDTO, userId, conn);
		if (response.equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU002.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU002.name());
			return responseDTO;
		}
		addressDTO.setUserId(newUserId);
		AddressDAO.addAddress(conn, addressDTO);
		accountDTO.setUserId(newUserId);
		AccountDAO.addAccount(conn, accountDTO, userId);
		VirtualAccountDTO virtualAccountDTO = new VirtualAccountDTO();
		String virtualAccountNo = new IDGenerator().getVirtualAccountNumber();
		virtualAccountDTO.setUserId(newUserId);
		virtualAccountDTO.setVirtualAccountNo(virtualAccountNo);
		virtualAccountDTO.setCreatedBy(userId);
		VirtualAccountDAO.createVirtualAccount(conn, virtualAccountDTO);

		Map<String, String> hm = addSalagentAudit(conn, agentDTO, addressDTO, accountDTO, userId, ipAddress);

		AuditTrail auditTrail = new AuditTrail();
		auditTrail.addAuditData(conn, userId, agentDTO.getAgentId(), "ADD-SALEAGENT", "ADD-SALEAGENT-SUCCESS", hm, ipAddress);

		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU0011.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU0011.name());
		return responseDTO;
	}

	private Map<String, String> addSalagentAudit(Connection conn, AgentDTO agentDTO, AddressDTO addressDTO,
			AccountDTO accountDTO, String userId, String ipAddress) {
		Map<String, String> hm = new HashMap<String, String>();

		hm.put("agentId", agentDTO.getAgentId());
		hm.put("bankAgentId", agentDTO.getBankAgentId());
		hm.put("branchId", agentDTO.getBranchId());
		hm.put("agentName", agentDTO.getAgentName());
		hm.put("contactPersonName", agentDTO.getContactPersonName());
		hm.put("contactNumber", agentDTO.getContactNumber());
		hm.put("emailId", agentDTO.getEmailId());
		
		hm.put("accountType", accountDTO.getAccountType());
		hm.put("branchAddress", accountDTO.getBranchAddress());
		hm.put("bankName", accountDTO.getBankName());
		hm.put("accountNumber", accountDTO.getAccountNumber());
		hm.put("ifscCode", accountDTO.getIfscCode());
		
		hm.put("businessPin", addressDTO.getBusinessPin());
		hm.put("resiCity", addressDTO.getResiCity());
		hm.put("businessCity", addressDTO.getBusinessCity());
		hm.put("businessState", addressDTO.getBusinessState());
		hm.put("resiPin", addressDTO.getResiPin());
		hm.put("resiState", addressDTO.getResiState());
		
		hm.put("businessAdd2", addressDTO.getBusinessAdd2());
		hm.put("resiAddress2", addressDTO.getResiAddress2());
		hm.put("businessAdd1", addressDTO.getBusinessAdd1());
		hm.put("resiAddress1", addressDTO.getResiAddress1());
		hm.put("createdBy", userId);
		hm.put("ipAddress", ipAddress);

		return hm;
	}

	public ResponseDTO updateAgent(AgentDTO agentDTO, AddressDTO addressDTO, AccountDTO accountDTO, String userId,
			Connection conn) {
		String response = agentMakerDAO.updateAgent(agentDTO, userId, conn);
		if (response.equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU009.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU009.name());
			return responseDTO;
		}
		addressDTO.setUserId(agentDTO.getAgentId());
		accountDTO.setUserId(agentDTO.getAgentId());
		AddressDAO.addEditedAddress(conn, addressDTO, userId);// TODO
		AccountDAO.addEditedAccount(conn, accountDTO, userId);// TODO
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU0012.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU0012.name());
		return responseDTO;
	}

	public ResponseDTO approveAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String response = agentMakerDAO.approveAgent(agentDTO, userId, conn);
		if (response.equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU005.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU005.name());
			return responseDTO;
		}
		/*
		 * agentDTO = agentMakerDAO.getAgentById(agentDTO.getAgentId(), conn); String
		 * password = PasswordManager.getPasswordSaltString();
		 * CustomerDAO.insertUser(agentDTO.getAgentId(),WINConstants.AGENTROLEID,userId,
		 * password,agentDTO.getEmailId(),conn); String emailBody =
		 * EmailTemplate.getUserEmailBody(agentDTO.getAgentId(), password); int
		 * emailStatus = new
		 * SendMailService().sendMail(agentDTO.getAgentId(),"Created Successfully ", "",
		 * emailBody, ""); log.info("EMAIL STATUS : "+emailStatus);
		 */
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU0013.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU0013.name());
		return responseDTO;
	}

	public ResponseDTO rejectAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String response = agentMakerDAO.rejectAgent(agentDTO, userId, conn);
		if (response.equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU004.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU004.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU0014.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU0014.name());
		return responseDTO;
	}

	public ResponseDTO getAgentList(String userId, Connection conn) {
		List<AgentDTO> data = agentMakerDAO.getAgentList(userId, conn);
		responseDTO.setData(data);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}

	public ResponseDTO deleteAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String response = agentMakerDAO.deleteAgent(agentDTO, userId, conn);
		if (response.equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU003.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU003.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU0015.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU0015.name());
		return responseDTO;
	}

	public ResponseDTO getAgentById(String agentId, String userId, Connection conn) {
		AgentDTO agentDTO = agentMakerDAO.getAgentById(agentId, conn);
		AddressDTO addressDTO = new AddressDAO().getAddressById(conn, agentId);
		AccountDTO accountDTO = new AccountDAO().getAccountById(conn, agentId);
		// ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setAgent(agentDTO);
		responseDTO.setAddress(addressDTO);
		responseDTO.setAccount(accountDTO);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}

	public AgentDashboardDTO getAgentDashboard(String userId, Connection conn) {

		AgentDashboardDTO agentDashboard = new AgentDashboardDTO();

		agentDashboard.setActiveVehicle(agentMakerDAO.getDashboardActiveVehicle(userId, conn));
		agentDashboard.setTotalCustomers(agentMakerDAO.getDashboardTotalCustomers(userId, conn));
		agentDashboard.setAllocatedTags(agentMakerDAO.getDashboardAllocateTags(userId, conn));
		agentDashboard.setAvailableTags(agentMakerDAO.getDashboardAvailableTags(userId, conn));

		return agentDashboard;
	}

}
