package com.netc.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dao.CheckSession;
import com.winnovature.dao.CustomerDAO;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.CustomerService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/customer/get")
public class GetAllCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(GetAllCustomer.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		// StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		ResponseDTO responseDTO = new ResponseDTO();
		Gson gson = new GsonBuilder().create();
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();
			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			String userId = request.getHeader("userId");
			finalResponse = gson.toJson(responseDTO);
			if (request.getParameter("customerId") != null) {
				String customerId = request.getParameter("customerId");
				log.info("getcustomer by id : " + customerId);
				responseDTO = CustomerService.getSingleCustomer(conn, customerId);
				finalResponse = gson.toJson(responseDTO);
			} else {
				log.info("All cusotmers");
				List<CustomerDTO> data = new CustomerDAO().getAllCustomers(conn, userId);
				responseDTO.setData(data);
				finalResponse = gson.toJson(responseDTO);
			}
			// finalResponse = gson.toJson(responseDTO);
			// }
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to /customer/get API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

}
