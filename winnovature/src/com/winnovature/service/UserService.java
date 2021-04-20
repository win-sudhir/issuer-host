package com.winnovature.service;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.constants.IDGenerator;
import com.winnovature.constants.WINConstants;
import com.winnovature.dao.AddressDAO;
import com.winnovature.dao.CustomerDAO;
import com.winnovature.dao.UserDAO;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.UserDTO;
import com.winnovature.utils.EmailTemplate;
import com.winnovature.utils.PasswordManager;
import com.winnovature.utils.SendMailService;
import com.winnovature.validation.UserErrorCode;
import com.winnovature.validation.UserValidation;

public class UserService {
	static Logger log = Logger.getLogger(UserService.class.getName());
	UserDAO userDAO = new UserDAO();
	ResponseDTO responseDTO = new ResponseDTO();
	public ResponseDTO addUser(UserDTO userDTO, AddressDTO addressDTO, String userId, Connection conn) {
		responseDTO = UserValidation.validateUserRequest(userDTO, addressDTO, conn);
		if(responseDTO.getStatus().equals("0")) {
			return responseDTO;
		}
		
		String newUserId = null;
		if(userDTO.getRoleId().equals("10")) {
			newUserId = "UM"+new IDGenerator().getUserId();
			userDTO.setUserId(newUserId);
		}
		if(userDTO.getRoleId().equals("11")) {
			newUserId = "UC"+new IDGenerator().getUserId();
			userDTO.setUserId(newUserId);
		}
		String response = userDAO.addUser(userDTO, addressDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(UserErrorCode.WINNUBU002.getErrorMessage());
			responseDTO.setErrorCode(UserErrorCode.WINNUBU002.name());
			return responseDTO;
		}
		addressDTO.setUserId(newUserId);
		addressDTO.setBusinessAdd1("NA");
		addressDTO.setBusinessAdd2("NA");
		addressDTO.setBusinessPin("0");
		addressDTO.setBusinessCity("NA");
		addressDTO.setBusinessState("NA");
		AddressDAO.addAddress(conn, addressDTO);
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(UserErrorCode.WINNUBU000.getErrorMessage());
		responseDTO.setErrorCode(UserErrorCode.WINNUBU000.name());
		return responseDTO;
	}

	public ResponseDTO approveUser(UserDTO userDTO, String userId, Connection conn) {
		String response = userDAO.approveUser(userDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(UserErrorCode.WINNUBU005.getErrorMessage());
			responseDTO.setErrorCode(UserErrorCode.WINNUBU005.name());
			return responseDTO;
		}
		String roleId = null;
		if(userDTO.getUserId().startsWith("UM")) {
			roleId = WINConstants.MAKERROLEID;//"10";
		}
		if(userDTO.getUserId().startsWith("UC")) {
			roleId = WINConstants.CHECKERROLEID;//"11";
		}
		userDTO = userDAO.getUserById(userDTO.getUserId(), conn);
		String password = PasswordManager.getPasswordSaltString();
		CustomerDAO.insertUser(userDTO.getUserId(),roleId,userId,password,userDTO.getEmailId(),conn);
		String emailBody = EmailTemplate.getUserEmailBody(userDTO.getUserId(), password);
		int emailStatus = new SendMailService().sendMail(userDTO.getEmailId(),"Created Successfully ", "", emailBody, "");
		log.info("EMAIL STATUS : "+emailStatus);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(UserErrorCode.WINNUBU008.getErrorMessage());
		responseDTO.setErrorCode(UserErrorCode.WINNUBU008.name());
		return responseDTO;
	}

	public ResponseDTO rejectUser(UserDTO userDTO, String userId, Connection conn) {
		String response = userDAO.rejectUser(userDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(UserErrorCode.WINNUBU004.getErrorMessage());
			responseDTO.setErrorCode(UserErrorCode.WINNUBU004.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(UserErrorCode.WINNUBU007.getErrorMessage());
		responseDTO.setErrorCode(UserErrorCode.WINNUBU007.name());
		return responseDTO;
	}
	
	public ResponseDTO deleteUser(UserDTO userDTO, String userId, Connection conn) {
		String response = userDAO.deleteUser(userDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(UserErrorCode.WINNUBU003.getErrorMessage());
			responseDTO.setErrorCode(UserErrorCode.WINNUBU003.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(UserErrorCode.WINNUBU006.getErrorMessage());
		responseDTO.setErrorCode(UserErrorCode.WINNUBU006.name());
		return responseDTO;
	}

	public ResponseDTO getUserList(String userId, Connection conn) {
		List<UserDTO> data = userDAO.getUserList(userId, conn);
		responseDTO.setData(data);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	public ResponseDTO getUserById(String userId, String hUserId, Connection conn) {
		UserDTO userDTO = userDAO.getUserById(userId, conn);
		AddressDTO addressDTO=new AddressDAO().getAddressById(conn, userId);
		//AccountDTO accountDTO=new AccountDAO().getAccountById(conn, userId);
		//ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setUser(userDTO);
		responseDTO.setAddress(addressDTO);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}

	public ResponseDTO updateUser(UserDTO userDTO, AddressDTO addressDTO, String userId, Connection conn) {
		String response = userDAO.updateUser(userDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(UserErrorCode.WINNUBU009.getErrorMessage());
			responseDTO.setErrorCode(UserErrorCode.WINNUBU009.name());
			return responseDTO;
		}
		UserDAO.updateAddress(conn, addressDTO, userId);
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(UserErrorCode.WINNUBU0010.getErrorMessage());
		responseDTO.setErrorCode(UserErrorCode.WINNUBU0010.name());
		return responseDTO;
	}

}
