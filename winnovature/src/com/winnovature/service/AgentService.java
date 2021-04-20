package com.winnovature.service;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.constants.IDGenerator;
import com.winnovature.constants.WINConstants;
import com.winnovature.dao.AccountDAO;
import com.winnovature.dao.AddressDAO;
import com.winnovature.dao.AgentDAO;
import com.winnovature.dao.CustomerDAO;
import com.winnovature.dao.VirtualAccountDAO;
import com.winnovature.dto.AccountDTO;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.AgentDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VirtualAccountDTO;
import com.winnovature.utils.EmailTemplate;
import com.winnovature.utils.PasswordManager;
import com.winnovature.utils.SendMailService;
import com.winnovature.validation.AgentErrorCode;
import com.winnovature.validation.AgentValidation;

public class AgentService {
	static Logger log = Logger.getLogger(AgentService.class.getName());

	AgentDAO agentDAO = new AgentDAO();
	ResponseDTO responseDTO = new ResponseDTO();
	public ResponseDTO addAgent(AgentDTO agentDTO, AddressDTO addressDTO, AccountDTO accountDTO, String userId,
			Connection conn) {
		responseDTO = AgentValidation.validateAgentRequest(agentDTO, addressDTO, accountDTO, conn);
		if(responseDTO.getStatus().equals("0")) {
			return responseDTO;
		}
		
		String newUserId = new IDGenerator().getAgentId();
		agentDTO.setAgentId(newUserId);
		String response = agentDAO.addAgent(agentDTO, userId, conn);
		if(response.equals("0")){
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
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU000.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU000.name());
		return responseDTO;
	}

	public ResponseDTO updateAgent(AgentDTO agentDTO, AddressDTO addressDTO, AccountDTO accountDTO, String userId,
			Connection conn) {
		String response = agentDAO.updateAgent(agentDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU009.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU009.name());
			return responseDTO;
		}
		addressDTO.setUserId(agentDTO.getAgentId());
		accountDTO.setUserId(agentDTO.getAgentId());
		AddressDAO.updateAddress(conn, addressDTO, userId);
		AccountDAO.updateAccount(conn, accountDTO, userId);
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU0010.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU0010.name());
		return responseDTO;
	}
	
	public ResponseDTO approveAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String response = agentDAO.approveAgent(agentDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU005.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU005.name());
			return responseDTO;
		}
		agentDTO = agentDAO.getAgentById(agentDTO.getAgentId(), conn);
		String password = PasswordManager.getPasswordSaltString();
		CustomerDAO.insertUser(agentDTO.getAgentId(),WINConstants.AGENTROLEID,userId,password,agentDTO.getEmailId(),conn);
		String emailBody = EmailTemplate.getUserEmailBody(agentDTO.getAgentId(), password);
		int emailStatus = new SendMailService().sendMail(agentDTO.getEmailId(),"Created Successfully ", "", emailBody, "");
		log.info("EMAIL STATUS :: "+emailStatus);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU008.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU008.name());
		return responseDTO;
	}

	public ResponseDTO rejectAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String response = agentDAO.rejectAgent(agentDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU004.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU004.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU007.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU007.name());
		return responseDTO;
	}

	public ResponseDTO getAgentList(String userId, Connection conn) {
		List<AgentDTO> data = agentDAO.getAgentList(userId, conn);
		responseDTO.setData(data);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}

	public ResponseDTO deleteAgent(AgentDTO agentDTO, String userId, Connection conn) {
		String response = agentDAO.deleteAgent(agentDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(AgentErrorCode.WINNABU003.getErrorMessage());
			responseDTO.setErrorCode(AgentErrorCode.WINNABU003.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(AgentErrorCode.WINNABU006.getErrorMessage());
		responseDTO.setErrorCode(AgentErrorCode.WINNABU006.name());
		return responseDTO;
	}

	public ResponseDTO getAgentById(String agentId, String userId, Connection conn) {
		AgentDTO agentDTO = agentDAO.getAgentById(agentId, conn);
		AddressDTO addressDTO=new AddressDAO().getAddressById(conn, agentId);
		AccountDTO accountDTO=new AccountDAO().getAccountById(conn, agentId);
		//ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setAgent(agentDTO);
		responseDTO.setAddress(addressDTO);
		responseDTO.setAccount(accountDTO);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}

	
}
