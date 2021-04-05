package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

import org.apache.log4j.Logger;

import com.winnovature.dto.KycDTO;
import com.winnovature.utils.DatabaseManager;

public class KycDAO {
	static Logger log = Logger.getLogger(CustomerDAO.class.getName());

	public static void addKYC(Connection conn, KycDTO kycDTO, String userId) {
		PreparedStatement ps = null;

		try {

			String query = "INSERT INTO customer_kyc_info (user_id, address_proof_id, address_proof_no, address_proof_doc_path, id_proof, id_proof_no, id_proof_doc_path) "
					+ "VALUES (?,?,?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, kycDTO.getUserId());
			ps.setString(2, kycDTO.getAddressProofId());
			ps.setString(3, kycDTO.getAddressProofNo());
			ps.setString(4, kycDTO.getAddressProofDocPath());
			ps.setString(5, kycDTO.getIdProof());
			ps.setString(6, kycDTO.getIdProofNo());
			ps.setString(7, kycDTO.getIdProofDocPath());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}

	public KycDTO getKycById(Connection conn, String customerId) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		KycDTO kycDTO = new KycDTO();
		try {
			String query = "SELECT * FROM customer_kyc_info WHERE user_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);

			rs = ps.executeQuery();
			if (rs.next()) {
				// user_id, address_proof_id, address_proof_no, address_proof_doc_path,
				// id_proof, id_proof_no, id_proof_doc_path, is_deleted, rodt
				kycDTO.setUserId(rs.getString("user_id"));
				kycDTO.setAddressProofId(rs.getString("address_proof_id"));
				kycDTO.setAddressProofNo(new String(Base64.getDecoder().decode(rs.getString("address_proof_no"))));
				kycDTO.setIdProof(rs.getString("id_proof"));
				kycDTO.setIdProofNo(new String(Base64.getDecoder().decode(rs.getString("id_proof_no"))));
				// kycDTO.setBranchAddress(rs.getString("branch_address"));
			}
			return kycDTO;
		}

		catch (Exception e) {
			log.error("getSingleAccount()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return kycDTO;
	}
	
	public KycDTO getKycByIdForChecker(Connection conn, String customerId) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		KycDTO kycDTO = new KycDTO();
		try {
			String query = "SELECT * FROM customer_kyc_info_edited WHERE user_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);

			rs = ps.executeQuery();
			if (rs.next()) {
				// user_id, address_proof_id, address_proof_no, address_proof_doc_path,
				// id_proof, id_proof_no, id_proof_doc_path, is_deleted, rodt
				kycDTO.setUserId(rs.getString("user_id"));
				kycDTO.setAddressProofId(rs.getString("address_proof_id"));
				kycDTO.setAddressProofNo(new String(Base64.getDecoder().decode(rs.getString("address_proof_no"))));
				kycDTO.setIdProof(rs.getString("id_proof"));
				kycDTO.setIdProofNo(new String(Base64.getDecoder().decode(rs.getString("id_proof_no"))));
				// kycDTO.setBranchAddress(rs.getString("branch_address"));
			}
			return kycDTO;
		}

		catch (Exception e) {
			log.error("getSingleAccount()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return kycDTO;
	}

	public static void main(String[] args) {
		// Base64.Decoder decoder = Base64.getDecoder();
		String dStr = new String(Base64.getDecoder().decode("Q0hFUEwxMDUwTA=="));
		System.out.println(dStr);
	}

	public static void updateKYC(Connection conn, KycDTO kycDTO, String userId) {
		PreparedStatement ps = null;

		try {
			// user_id, address_proof_id=?, address_proof_no=?, id_proof=?, id_proof_no=?
			String query = "UPDATE customer_kyc_info SET address_proof_id=?, address_proof_no=?, id_proof=?, id_proof_no=? WHERE user_id = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, kycDTO.getAddressProofId());
			ps.setString(2, kycDTO.getAddressProofNo());
			ps.setString(3, kycDTO.getIdProof());
			ps.setString(4, kycDTO.getIdProofNo());
			ps.setString(5, kycDTO.getUserId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}

	public static void addEditedKYC(Connection conn, KycDTO kycDTO, String userId) {
		PreparedStatement ps = null;

		try {
			// user_id, address_proof_id=?, address_proof_no=?, id_proof=?, id_proof_no=?
			String query = "INSERT INTO customer_kyc_info_edited (user_id, address_proof_id, address_proof_no, id_proof, id_proof_no) "
					+ "VALUES (?,?,?,?,?) ";

			ps = conn.prepareStatement(query);
			ps.setString(1, kycDTO.getUserId());
			ps.setString(2, kycDTO.getAddressProofId());
			ps.setString(3, kycDTO.getAddressProofNo());
			ps.setString(4, kycDTO.getIdProof());
			ps.setString(5, kycDTO.getIdProofNo());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
		}
	}
}
