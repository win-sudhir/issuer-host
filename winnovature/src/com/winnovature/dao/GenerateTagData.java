package com.winnovature.dao;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.tag.TagSigner;
import com.winnovature.tag.Utils;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.PropertyReader;
import com.winnovature.utils.SGTIN96;

public class GenerateTagData {
	static Logger log = Logger.getLogger(GenerateTagData.class.getName());

	public String generateTagData(String tagInfo, Connection conn) throws IOException {

		log.info("---------- tagInfo -----------" + tagInfo);
		// Connection conn = null;
		PreparedStatement ps = null, ps1 = null, ps3 = null;
		String sResp = null;
		JSONArray jsonArray;
		try {
			String poId = null;
			JSONObject tagDataResp = new JSONObject();
			String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

			//jsonArray = new JSONArray(tagInfo);

			//log.info("jsonArray Count : " + jsonArray.length());

			String q3 = "INSERT INTO inventory_master (po_id,tid,tag_class_id,tag_unique_id,branch_id,agent_id,status,date_time)values(?,?,?,?,?,?,?,?)";
			String q4 = "UPDATE po_master set po_status = ? WHERE po_id = ?";
			String q1 = "INSERT INTO vehicle_tag_linking (tid,tag_id,signature_data,tag_status,barcode_data,tag_pwd,kill_pwd,tag_class_id,rfu1) values(?,?,?,?,?,?,?,?,?)";

			ps = conn.prepareStatement(q3);
			ps1 = conn.prepareStatement(q1);

			// Order qty TID qty should be same
			jsonArray = new JSONArray(tagInfo);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i);

				// Get Data and store in inventory
				String branchId = "0";// item.getString("branchId");
				String agentId = "0";// item.getString("agentId");
				// status in inventory
				String status = "0";
				// String datetime = currentDate;//item.getString("dateTime");
				// item.getString("status");
				poId = item.getString("poId");
				String tagClassId = item.getString("tagClassId");
				String tid = item.getString("tagUniqueId");
				String barCode = item.getString("barCode"); // added

				if (checkOrderQuantity(conn, poId, jsonArray.length())) {
					return "Order quantity & TID quantity should be same.";
				} else if (isBarcodePresent(barCode)) { // if barcode is present
					return "Barcode data is already present, Please use correct Barcode.";
				}else if (barCode.length() != 18) {
					// check length of barcode is 18
					return "Barcode data can not be less or greater than length 18.";
				}else if (!barCode.substring(0, 6).equals(PropertyReader.getPropertyValue("issuerIIN"))) // ("607802"))
				{
					// check issuer IIN is set as config file
					return "Barcode not belongs to this issuer bank/IIN mismatch.";
				}

				tagDataResp = getTagData(tid, tagClassId, barCode, conn); // added new

				String tagId = tagDataResp.getString("tagId");
				String signature = tagDataResp.getString("signature");

				String userMemory = tagDataResp.getString("userMemory");

				ps1.setString(1, tid);
				ps1.setString(2, tagId);
				ps1.setString(3, signature);
				ps1.setString(4, "0");
				ps1.setString(5, barCode);
				ps1.setString(6, "winntag001");
				ps1.setString(7, "winnkill001");
				ps1.setString(8, tagClassId);
				ps1.setString(9, userMemory);

				ps1.addBatch();

				ps.setString(1, poId);
				ps.setString(2, tid);
				ps.setString(3, tagClassId);
				ps.setString(4, "0");
				ps.setString(5, branchId);
				ps.setString(6, agentId);
				ps.setString(7, status);
				ps.setString(8, currentDate);

				ps.addBatch();

				log.info("Batch Added ::: " + tid);
			}

			ps1.executeBatch();
			ps.executeBatch();

			ps3 = conn.prepareStatement(q4);
			ps3.setInt(1, 2);
			ps3.setString(2, poId);
			// ps3.setInt(3, 0);

			ps3.executeUpdate();

			// conn.commit();
			sResp = "CS";
			log.info("Batch Executed....");
		} catch (ParseException e) {

			// try {
			// conn.rollback();
			// } catch (SQLException e1) {

			sResp = e.getMessage();
			log.error("Getting Error   :::    ", e);
			// }

			// sResp = e.getMessage();
		} catch (SQLException e1) {
			sResp = e1.getMessage();
			log.error("Getting Error   :::    ", e1);
		}

		finally {
			DatabaseManager.closePreparedStatement(ps1);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closePreparedStatement(ps3);
		}

		return sResp;

	}

	private boolean checkOrderQuantity(Connection conn, String poId, int tidCount) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			log.info("poId :" + poId + " tidCount :" + tidCount);
			String q2 = "SELECT order_qty FROM po_item_master WHERE po_id = ?";

			ps = conn.prepareStatement(q2);
			ps.setString(1, poId);
			rs = ps.executeQuery();

			if (rs.next()) {

				log.info("order_qty :: " + rs.getInt("order_qty"));
				if (rs.getInt("order_qty") != tidCount) {
					return true;
				}
			}

		} catch (Exception e) {
			log.error("Exception " + e.getMessage());
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}

	public JSONObject getTagData(String tid, String tagClassId, String barCode, Connection conn) {
		log.info("GenerateTagData.java ::: TID : " + tid + " And Tag Class Id : " + tagClassId);

		CallableStatement cs = null;
		// Connection con = null;
		JSONObject json = null;

		try {
			log.info("Before Connection");
			// con = DatabaseManager.getConnection();
			log.info("After Connection");
			cs = conn.prepareCall("{CALL pr_perso_data(?,?,?,?)}");
			cs.registerOutParameter(1, Types.VARCHAR); // Prefix number
			cs.registerOutParameter(2, Types.VARCHAR); // IIN Number
			cs.registerOutParameter(3, Types.VARCHAR); // Serial Number
			cs.registerOutParameter(4, Types.VARCHAR); // KI
			cs.execute();

			log.info("Response from prcedure cs1::" + cs.getString(1) + "cs2::" + cs.getString(2) + "cs3::"
					+ cs.getString(3) + "cs4::" + cs.getString(4));
			SGTIN96 stn = new SGTIN96();
			stn.setCompanyPrefix(new BigInteger(cs.getString(1)));

			// stn.setSerialNumber(new
			// BigInteger(getLeftPaddedString(cs.getString(3))));//old one

			stn.setSerialNumber(new BigInteger(barCode.substring(barCode.length() - 7, barCode.length())));
			stn.setFilterValue(0);
			stn.setItemReference(new BigInteger(cs.getString(2)));
			String s = stn.ToEpc();
			s = getFinalString(s);
			log.info("GenerateTagData.java ::: EPC ID :: " + s);

			String dataToSign = tid + s;
			log.info("TagPerso.java ::: Data To Sign :: " + dataToSign);

			// log.info("Signed Data :: " +Utils.byteToHex(s1));
			String hash = TagSigner.getSignedHash(tid, s);

			String str2 = "585858585858585858585858" + getVC(tagClassId) + "00" + hash;

			// String str2 = "5858585858585858585858580400"+hash;
			log.info("TagPerso.java ::: userMemory :: " + str2);

			// String dataToSign = tid + s;
			// log.info("GenerateTagData.java ::: Data To Sign :: TID+EPCID : " +
			// dataToSign);

			// String hash = sha256(dataToSign.getBytes());
			// log.info("GenerateTagData.java ::: Hash :: " + hash.toUpperCase());

			// String genTagURL = PropertyReader.getPropertyValue("generateTagURL");
			// log.info("GenerateTagData.java getPropertyValue() ::: generateTagURL :: " +
			// genTagURL);

			// ------------- String data = "tagData=" + dataToSign;

			// String signedData =
			// "86F9B791A4C3B269ED691CE271E50C29432CE585739706E1BF49B68ADBE53BD4";
			// -------------- String str2 = "585858585858585858585858" +
			// NetcUtils.getVC(tagClassId) + "00" + signedData;
			// String str2 = "585858585858585858585858" + getVC(tagClassId) + "00" +
			// signedData;

			// --------------- String str2 = "5858585858585858585858580400"+hash;
			// log.info("GenerateTagData.java ::: userMemory :: " + str2);

			json = new JSONObject();

			json.put("tagId", s);
			json.put("signature", hash.toUpperCase());
			json.put("userMemory", str2);
			json.put("barCode",
					Utils.getLeftPaddedString(cs.getString(2), "0", 6) + "-"
							+ Utils.getLeftPaddedString(cs.getString(4), "0", 3) + "-"
							+ Utils.getLeftPaddedString(cs.getString(3), "0", 8));
			json.put("TID", tid);

		} catch (Exception e) {
			log.error("GenerateTagData.java ::: Error Occurred in Tag Personlization :: ", e);
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			// DatabaseManager.closeConnection(con);

		}

		return json;
	}

	private static String getFinalString(String binary) {
		String s = "";
		int digitNumber = 1;
		int sum = 0;
		// String binary = s;
		for (int i = 0; i < binary.length(); i++) {
			if (digitNumber == 1)
				sum += Integer.parseInt(binary.charAt(i) + "") * 8;
			else if (digitNumber == 2)
				sum += Integer.parseInt(binary.charAt(i) + "") * 4;
			else if (digitNumber == 3)
				sum += Integer.parseInt(binary.charAt(i) + "") * 2;
			else if (digitNumber == 4 || i < binary.length() + 1) {
				sum += Integer.parseInt(binary.charAt(i) + "") * 1;
				digitNumber = 0;
				if (sum < 10)
					s = s + sum + "";
				else if (sum == 10)
					s = s + "A";
				else if (sum == 11)
					s = s + "B";
				else if (sum == 12)
					s = s + "C";
				else if (sum == 13)
					s = s + "D";
				else if (sum == 14)
					s = s + "E";
				else if (sum == 15)
					s = s + "F";
				sum = 0;
			}
			digitNumber++;

		}
		System.out.print(s);
		return s;
	}

	/*
	 * private String getLeftPaddedString(String str) { String zeroPad = "00000";
	 * String str0 = zeroPad.substring(str.length()) + str; return str0; }
	 */
	public static String sha256(byte[] base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base);// base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			log.info(hexString.toString());

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void main1(String[] args) {
		String pet = "PETROL'PUMP";
		if (pet.contains("'")) {
			pet = pet.replace("'", "");
			System.out.println("String :: " + pet);
		} else {
			System.out.println("NOT CONTAINS PETROL");
		}
	}

	public static String getVC(String vcCode) {
		if (vcCode.equals("VC4") || vcCode.equals("VC5") || vcCode.equals("VC6") || vcCode.equals("VC7")
				|| vcCode.equals("VC8") || vcCode.equals("VC9"))
			return "0" + vcCode.substring(2);

		else if (vcCode.equals("VC10"))
			return "0A";
		else if (vcCode.equals("VC11"))
			return "0B";
		else if (vcCode.equals("VC12"))
			return "0C";
		else if (vcCode.equals("VC13"))
			return "0D";
		else if (vcCode.equals("VC14"))
			return "0E";
		else if (vcCode.equals("VC15"))
			return "0F";
		else
			return "10";
	}

	public static void main(String[] args) throws Exception {

		String hash = TagSigner.getSignedHash("E4161FA82032D69926004600", "34161FA8202F424203ED3CC0");

		String str2 = "585858585858585858585858" + getVC("VC4") + "00" + hash;

		// String str2 = "5858585858585858585858580400"+hash;
		log.info("TagPerso.java ::: userMemory :: " + str2);

		/*
		 * String barCode = "607012-001-0001234";
		 * 
		 * barCode = barCode.substring(barCode.length() - 7, barCode.length());
		 * System.out.println("barCode = " + barCode); barCode = "607012-001-0001234";
		 * barCode = barCode.substring(0, 6); System.out.println(barCode); barCode =
		 * "607012-001-0001234"; if (!barCode.substring(0, 6).equals("607012")) //
		 * ("607802")) {
		 * System.out.println("Barcode not belongs to this issuer bank/IIN mismatch.");
		 */
	}

	// JSONObject jsonObj = new
	// GenerateTagData().getTagData("E5151FA82032D69926004978","VC4","607012-001-0001234");
	// log.info("Response JsonObject ::"+jsonObj);

	// }

	private boolean isBarcodePresent(String barCode) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getConnection();
			String sql = "SELECT barcode_data FROM vehicle_tag_linking where barcode_data=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, barCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);

		}
		return false;
	}

	private String getLeftPaddedString(String str) {
		String zeroPad = "00000";
		String str0 = zeroPad.substring(str.length()) + str;
		return str0;
	}
}
