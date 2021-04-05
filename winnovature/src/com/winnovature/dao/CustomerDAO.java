package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.contants.WINConstants;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.WalletDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.DateUtils;
import com.winnovature.utils.PasswordManager;
import com.winnovature.utils.HashGenerator;


public class CustomerDAO {
	static Logger log = Logger.getLogger(CustomerDAO.class.getName());

	public static void addCustomer(Connection conn, CustomerDTO customerDTO, String userId) {
		PreparedStatement ps = null;

		try {

			String query = "INSERT INTO customer_info (user_id, wallet_id, parent_id, customer_name, email_id, contact_number, dob, is_corporate, customer_type, gender, occupation, is_wallet, status, created_by, created_on) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, customerDTO.getUserId());
			ps.setString(2, customerDTO.getWalletId());
			ps.setString(3, userId);
			ps.setString(4, customerDTO.getCustomerName().toUpperCase());
			ps.setString(5, customerDTO.getEmailId().toLowerCase());
			ps.setString(6, customerDTO.getContactNumber());
			ps.setString(7, customerDTO.getDob());
			ps.setString(8, CustomerDTO.NOTCORPORATE);
			ps.setString(9, CustomerDTO.RETAILCUSTOMER);
			ps.setString(10, customerDTO.getGender());// always 0 corporate
			ps.setString(11, customerDTO.getOccupation());
			ps.setString(12, customerDTO.getIsWallet());
			ps.setString(13, WINConstants.NEW);
			ps.setString(14, userId);
			ps.setString(15, new DateUtils().getCurrnetDate());
			WalletDAO.createLTDKYCWallet(conn, userId, customerDTO.getWalletId());
			//insertUser(customerDTO.getUserId(), "2",userId,"",customerDTO.getEmailId(),conn);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}

	public static void updateCustomer(Connection conn, CustomerDTO customerDTO, String userId) {
		PreparedStatement ps = null;

		try {

			String query = "UPDATE customer_info SET customer_name=?, email_id=?, contact_number=?, dob=?, gender=?, occupation=?,modified_by=?, modified_on=?  WHERE user_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, customerDTO.getCustomerName().toUpperCase());
			ps.setString(2, customerDTO.getEmailId().toLowerCase());
			ps.setString(3, customerDTO.getContactNumber());
			ps.setString(4, customerDTO.getDob());
			ps.setString(5, customerDTO.getGender());
			ps.setString(6, customerDTO.getOccupation());
			ps.setString(7, userId);
			ps.setString(8, new DateUtils().getCurrnetDate());
			ps.setString(9, customerDTO.getUserId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	
	
	public static void addEditedCustomer(Connection conn, CustomerDTO customerDTO, String userId) {
		PreparedStatement ps = null;

		try {

			String query = "INSERT INTO customer_info_edited (user_id, customer_name, email_id, contact_number, dob, gender, occupation, status, created_by, created_on) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?) ";
			
			
			ps = conn.prepareStatement(query);
			ps.setString(1, customerDTO.getUserId());
			ps.setString(2, customerDTO.getCustomerName().toUpperCase());
			ps.setString(3, customerDTO.getEmailId().toLowerCase());
			ps.setString(4, customerDTO.getContactNumber());
			ps.setString(5, customerDTO.getDob());
			ps.setString(6, customerDTO.getGender());
			ps.setString(7, customerDTO.getOccupation());
			ps.setString(8, WINConstants.UPREQ);
			ps.setString(9, userId);
			ps.setString(10, new DateUtils().getCurrnetDate());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	/*
	public void approveCustomer(Connection conn, String userId, String customerId) {
		PreparedStatement ps = null;

		try {

			String query = "UPDATE customer_info SET status=?, approved_by=?, approved_on=?  WHERE user_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, WINConstants.APPROVE);
			ps.setString(2, userId);
			ps.setString(3, new DateUtils().getCurrnetDate());
			ps.setString(4, customerId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	*/
	public void approveCustomer(Connection conn, String userId, String customerId) {
		PreparedStatement ps = null;

		try {
			//UPDATE customer_info c, wallet_info w SET c.status='APPROVE', c.approved_by="admin", c.approved_on=now(), w.max_balance="100000", w.kyc_type="fullkyc"  WHERE c.user_id = "C020071920913" and c.wallet_id=w.wallet_id;
			String query = "UPDATE customer_info c, wallet_info w "
					+ "SET c.status=?, c.approved_by=?, c.approved_on=?, w.max_balance=?, w.kyc_type=?  "
					+ "WHERE c.user_id = ? and c.wallet_id=w.wallet_id";

			ps = conn.prepareStatement(query);
			ps.setString(1, WINConstants.APPROVE);
			ps.setString(2, userId);
			ps.setString(3, new DateUtils().getCurrnetDate());
			ps.setDouble(4, WalletDTO.MAXBALANCE);
			ps.setString(5, WalletDTO.FULLKYC);
			ps.setString(6, customerId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	public void deleteCustomerOnly(Connection conn, String userId, String customerId) {
		PreparedStatement ps = null;

		try {

			String query = "UPDATE customer_info SET status=?, modified_by=?, modified_on=?  WHERE user_id = ?";

			ps = conn.prepareStatement(query);
			ps.setString(1, WINConstants.DELETE);
			ps.setString(2, userId);
			ps.setString(3, new DateUtils().getCurrnetDate());
			ps.setString(4, customerId);
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
	
	public void deleteCustomer(Connection conn, String userId, String customerId) {
		PreparedStatement ps = null;

		try {

			String query = "UPDATE customer_info c, wallet_info w, customer_vehicle_info v, account_info a SET c.status=?, c.modified_by=?, c.modified_on=?, "
					+ "w.status=?, w.modified_by=?, w.modified_on=?, v.status=?, a.status=? "
					+ "WHERE c.user_id = ? and c.wallet_id=w.wallet_id and v.user_id=c.user_id and a.user_id=c.user_id";
			String currentDate = new DateUtils().getCurrnetDate();
			ps = conn.prepareStatement(query);
			ps.setString(1, WINConstants.DELETE);
			ps.setString(2, userId);
			ps.setString(3, currentDate);
			ps.setString(4, WINConstants.DELETE);
			ps.setString(5, userId);
			ps.setString(6, currentDate);
			ps.setString(7, WINConstants.DELETE);
			ps.setString(8, WINConstants.DELETE);
			ps.setString(9, customerId);
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}

	public List<CustomerDTO> getAllCustomers(Connection conn, String userId) {
		List<CustomerDTO> lst = new ArrayList<CustomerDTO>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query= null;
		try {
			
			if(userId.equalsIgnoreCase("admin")) {
				query = "SELECT * FROM customer_info WHERE status in ('NEW', 'ACTIVE', 'APPROVE')";
				ps = conn.prepareStatement(query);
			}else {
				query = "SELECT * FROM customer_info WHERE parent_id = ? and status in ('NEW', 'ACTIVE', 'APPROVE')";
				ps = conn.prepareStatement(query);
				ps.setString(1, userId);
			}
			
			//ps = conn.prepareStatement(query);
			// pstmt.setString(1, id);

			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDTO customerDTO = new CustomerDTO();
				// user_id, wallet_id, parent_id, customer_name, email_id, contact_number, dob,
				// is_corporate, customer_type, gender, occupation, status, created_by,
				// created_on, approved_by, approved_on, modified_by, modified_on, rodt
				customerDTO.setUserId(rs.getString("user_id"));
				customerDTO.setWalletId(rs.getString("wallet_id"));
				customerDTO.setCustomerName(rs.getString("customer_name"));
				customerDTO.setEmailId(rs.getString("email_id"));
				customerDTO.setContactNumber(rs.getString("contact_number"));
				customerDTO.setDob(rs.getString("dob"));
				customerDTO.setIsCorporate(rs.getString("is_corporate"));
				customerDTO.setGender(rs.getString("gender"));
				customerDTO.setOccupation(rs.getString("occupation"));
				customerDTO.setStatus(rs.getString("status"));
				customerDTO.setCreatedBy(rs.getString("created_by"));
				customerDTO.setCreatedOn(rs.getString("created_on"));
				lst.add(customerDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getAllCustomers()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;

	}
	
	public List<CustomerDTO> getAllCustomersForChecker(Connection conn) {
		List<CustomerDTO> lst = new ArrayList<CustomerDTO>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String query = "SELECT * FROM customer_info WHERE status = 'NEW' ";
			ps = conn.prepareStatement(query);
			// pstmt.setString(1, id);

			rs = ps.executeQuery();
			while (rs.next()) {
				CustomerDTO customerDTO = new CustomerDTO();
				// user_id, wallet_id, parent_id, customer_name, email_id, contact_number, dob,
				// is_corporate, customer_type, gender, occupation, status, created_by,
				// created_on, approved_by, approved_on, modified_by, modified_on, rodt
				customerDTO.setUserId(rs.getString("user_id"));
				customerDTO.setWalletId(rs.getString("wallet_id"));
				customerDTO.setCustomerName(rs.getString("customer_name"));
				customerDTO.setEmailId(rs.getString("email_id"));
				customerDTO.setContactNumber(rs.getString("contact_number"));
				customerDTO.setDob(rs.getString("dob"));
				customerDTO.setIsCorporate(rs.getString("is_corporate"));
				customerDTO.setGender(rs.getString("gender"));
				customerDTO.setOccupation(rs.getString("occupation"));
				customerDTO.setStatus(rs.getString("status"));
				customerDTO.setCreatedBy(rs.getString("created_by"));
				customerDTO.setCreatedOn(rs.getString("created_on"));
				lst.add(customerDTO);
			}
			return lst;
		}

		catch (Exception e) {
			log.error("getAllCustomers()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;

	}
	
	public CustomerDTO getOneCustomers(Connection conn, String customerId) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		CustomerDTO customerDTO = new CustomerDTO();
		try {
			String query = "SELECT * FROM customer_info WHERE user_id=? and status in ('NEW', 'ACTIVE', 'APPROVE')";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			// CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				// user_id, wallet_id, parent_id, customer_name, email_id, contact_number, dob,
				// is_corporate, customer_type, gender, occupation, status, created_by,
				// created_on, approved_by, approved_on, modified_by, modified_on,rodt
				customerDTO.setUserId(rs.getString("user_id"));
				customerDTO.setWalletId(rs.getString("wallet_id"));
				customerDTO.setCustomerName(rs.getString("customer_name"));
				customerDTO.setEmailId(rs.getString("email_id"));
				customerDTO.setContactNumber(rs.getString("contact_number"));
				customerDTO.setDob(rs.getString("dob"));
				customerDTO.setIsCorporate(rs.getString("is_corporate"));
				customerDTO.setGender(rs.getString("gender"));
				customerDTO.setOccupation(rs.getString("occupation"));
				customerDTO.setStatus(rs.getString("status"));
				customerDTO.setCreatedBy(rs.getString("created_by"));
				customerDTO.setCreatedOn(rs.getString("created_on"));
			}
			return customerDTO;
		}

		catch (Exception e) {
			log.error("getOneCustomers()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return customerDTO;
	}
	
	public CustomerDTO getCustomerForChecker(Connection conn, String customerId) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		CustomerDTO customerDTO = new CustomerDTO();
		try {
			String query = "SELECT * FROM customer_info_edited WHERE user_id=? order by created_on desc limit 1";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			// CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				
				customerDTO.setUserId(rs.getString("user_id"));
				customerDTO.setCustomerName(rs.getString("customer_name"));
				customerDTO.setEmailId(rs.getString("email_id"));
				customerDTO.setContactNumber(rs.getString("contact_number"));
				customerDTO.setDob(rs.getString("dob"));
				customerDTO.setGender(rs.getString("gender"));
				customerDTO.setOccupation(rs.getString("occupation"));
				customerDTO.setStatus(rs.getString("status"));
				customerDTO.setCreatedBy(rs.getString("created_by"));
				customerDTO.setCreatedOn(rs.getString("created_on"));
			}
			return customerDTO;
		}

		catch (Exception e) {
			log.error("getOneCustomers()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return customerDTO;
	}
	 
	public CustomerDTO geCustomersWalletInfo(Connection conn, String customerId) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		CustomerDTO customerDTO = new CustomerDTO();
		try {
			String query = "SELECT c.user_id,c.wallet_id, c.contact_number,c.email_id,w.max_balance,w.current_balance,w.kyc_type, c.is_wallet " + 
					"FROM customer_info c, wallet_info w WHERE (c.user_id=? OR c.wallet_id=?)and c.status in ('NEW', 'ACTIVE', 'APPROVE') and c.wallet_id=w.wallet_id";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			ps.setString(2, customerId);
			// CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				customerDTO.setUserId(rs.getString("user_id"));
				customerDTO.setWalletId(rs.getString("wallet_id"));
				customerDTO.setEmailId(rs.getString("email_id"));
				customerDTO.setContactNumber(rs.getString("contact_number"));
				customerDTO.setMaxBalance(rs.getDouble("max_balance"));
				customerDTO.setCurrentBalance(rs.getDouble("current_balance"));
				customerDTO.setKycType(rs.getString("kyc_type"));
				customerDTO.setIsWallet(rs.getString("is_wallet"));
			}
			return customerDTO;
		}

		catch (Exception e) {
			log.error("getCustomersWalletInfo()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return customerDTO;
	}
	public CustomerDTO geCustomersWalletByVehicleNumber(Connection conn, String vehicleNumber) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		CustomerDTO customerDTO = new CustomerDTO();
		try {
			String query = "SELECT c.user_id,c.wallet_id, c.contact_number,c.email_id,w.max_balance,w.current_balance,w.kyc_type " + 
					"FROM customer_info c, wallet_info w, customer_vehicle_info v WHERE c.status in ('NEW', 'ACTIVE', 'APPROVE') and c.wallet_id=w.wallet_id and c.user_id=v.user_id and v.vehicle_number=? and v.status='ACTIVE' limit 1";
			ps = conn.prepareStatement(query);
			ps.setString(1, vehicleNumber);
			// CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				customerDTO.setUserId(rs.getString("user_id"));
				customerDTO.setWalletId(rs.getString("wallet_id"));
				customerDTO.setEmailId(rs.getString("email_id"));
				customerDTO.setContactNumber(rs.getString("contact_number"));
				customerDTO.setMaxBalance(rs.getDouble("max_balance"));
				customerDTO.setCurrentBalance(rs.getDouble("current_balance"));
				customerDTO.setKycType(rs.getString("kyc_type"));
			}
			return customerDTO;
		}

		catch (Exception e) {
			log.error("getCustomersWalletInfo()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return customerDTO;
	}
	public static void insertUser(String userId, String roleId, String createdBy, String password, String emailId, Connection conn) {

		PreparedStatement ps = null;

		String sqluser = "insert into user_master (user_id, password, role_id, email_id,last_login_date, last_login_ip, is_active, is_deleted, created_by, created_on, approved_by, approved_on)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		String currentDate = new DateUtils().getCurrnetDate();
		
		password = HashGenerator.sha256(password);
		try {
			
			ps = conn.prepareStatement(sqluser);
			log.info("User ID : " + userId);
			ps.setString(1, userId);
			ps.setString(2, password);
			ps.setString(3, roleId);
			ps.setString(4, emailId);
			ps.setString(5, currentDate);
			ps.setString(6, "0.0.0.0");
			ps.setString(7, "1");
			ps.setString(8, "0");
			ps.setString(9, createdBy);
			ps.setString(10, currentDate);
			ps.setString(11, "");
			ps.setString(12, currentDate);
			ps.executeUpdate();

			//PasswordManager.insertUserHistory(userid, password);

			log.info("insertUser()   :::   User inserted successfully in user master.........");

		} catch (Exception e) {
				log.info("User Insert opration rolled back..." + e.getMessage());
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closeConnection(con);
		}
	}
	
	public CustomerDTO geWalletInfo(String customerId) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		CustomerDTO customerDTO = new CustomerDTO();
		Connection conn = null;
		try {
			String query = "SELECT c.user_id,c.wallet_id, c.contact_number,c.email_id,w.max_balance,w.current_balance,w.kyc_type " + 
					"FROM customer_info c, wallet_info w WHERE (c.user_id=? OR c.wallet_id=?)and c.status in ('NEW', 'ACTIVE', 'APPROVE') and c.wallet_id=w.wallet_id";
			conn = DatabaseManager.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			ps.setString(2, customerId);
			// CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				customerDTO.setUserId(rs.getString("user_id"));
				customerDTO.setWalletId(rs.getString("wallet_id"));
				customerDTO.setEmailId(rs.getString("email_id"));
				customerDTO.setContactNumber(rs.getString("contact_number"));
				customerDTO.setMaxBalance(rs.getDouble("max_balance"));
				customerDTO.setCurrentBalance(rs.getDouble("current_balance"));
				customerDTO.setKycType(rs.getString("kyc_type"));
			}
			return customerDTO;
		}

		catch (Exception e) {
			log.error("getCustomersWalletInfo()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
		}
		return customerDTO;
	}
	
	public CustomerDTO geCustomerWalletInfo(Connection conn, String customerId) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		CustomerDTO customerDTO = new CustomerDTO();
		//Connection conn = null;
		try {
			String query = "SELECT c.user_id,c.wallet_id, c.contact_number,c.email_id,w.max_balance,w.current_balance,w.kyc_type, c.is_wallet " + 
					"FROM customer_info c, wallet_info w WHERE (c.user_id=? OR c.wallet_id=?)and c.status in ('NEW', 'ACTIVE', 'APPROVE') and c.wallet_id=w.wallet_id";
			//conn = DatabaseManager.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			ps.setString(2, customerId);
			// CustomerDTO customerDTO = new CustomerDTO();
			rs = ps.executeQuery();
			if (rs.next()) {
				customerDTO.setUserId(rs.getString("user_id"));
				customerDTO.setWalletId(rs.getString("wallet_id"));
				customerDTO.setEmailId(rs.getString("email_id"));
				customerDTO.setContactNumber(rs.getString("contact_number"));
				customerDTO.setMaxBalance(rs.getDouble("max_balance"));
				customerDTO.setCurrentBalance(rs.getDouble("current_balance"));
				customerDTO.setKycType(rs.getString("kyc_type"));
				customerDTO.setIsWallet(rs.getString("is_wallet"));
			}
			return customerDTO;
		}

		catch (Exception e) {
			log.error("getCustomersWalletInfo()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			//DatabaseManager.closeConnection(conn);
		}
		return customerDTO;
	}
	/*
	 * public CustomerDTO getOneCustomers(Connection conn, String customerId) {
	 * 
	 * ResultSet rs = null; PreparedStatement ps = null;
	 * 
	 * try { String query =
	 * "SELECT * FROM customer_info WHERE user_id=? and status in ('NEW', 'ACTIVE', 'APPROVE')"
	 * ; ps = conn.prepareStatement(query); ps.setString(1, customerId);
	 * //CustomerDTO customerDTO = new CustomerDTO(); rs = ps.executeQuery();
	 * CustomerDTO customerDTO = null; if (rs.next()) { //user_id, wallet_id,
	 * parent_id, customer_name, email_id, contact_number, dob, is_corporate,
	 * customer_type, gender, occupation, status, created_by, created_on,
	 * approved_by, approved_on, modified_by, modified_on, rodt
	 * 
	 * CustomerDTO customerDTO = new CustomerDTO(userId, walletId, customerName,
	 * emailId, contactNumber, dob, isCorporate, gender, occupation, status,
	 * isWallet, createdBy, createdOn) customerDTO = new
	 * CustomerDTO((rs.getString("user_id")), rs.getString("wallet_id"),
	 * rs.getString("customer_name"), rs.getString("email_id"),
	 * rs.getString("contact_number"), rs.getString("dob"),
	 * rs.getString("is_corporate"), rs.getString("gender"),
	 * rs.getString("occupation"), rs.getString("status"),
	 * rs.getString("created_by"), rs.getString("created_on"));
	 * 
	 * 
	 * customerDTO.setStatus(); customerDTO.setCreatedBy();
	 * customerDTO.setCreatedOn();
	 * 
	 * } return customerDTO; }
	 * 
	 * catch (Exception e) { log.error("getOneCustomers()  ::  getting error  : ",
	 * e); e.printStackTrace(); } finally { DatabaseManager.closeResultSet(rs);
	 * DatabaseManager.closePreparedStatement(ps); } return null; }
	 */
	public static void main(String[] args) {
		String ps = HashGenerator.sha256("admin@123");
		System.out.println("ps "+ps);
	}
}
