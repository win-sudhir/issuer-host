package com.winnovature.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.winnovature.dto.BarCodeDataDTO;
import com.winnovature.dto.VehicleDetailsDTO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.IssuerPropertyReader;
import com.winnovature.utils.MemoryComponent;

public class TagReissuanceDAO {
	static Logger log = Logger.getLogger(TagReissuanceDAO.class.getName());
	
	//old updateInventory
	public void updateLinkingInv(String tag_id, String barcode_data, String vehicle_id, String vehicle_number,
			String customer_id) {
		Connection con = null;
		PreparedStatement preparedStmt1 = null;
		PreparedStatement preparedStmt2 = null;
		PreparedStatement preparedStmt3 = null;
		try {
			String invsql = "update inventory_master set status = ? where barcode_data='" + barcode_data + "'";
			String vehlinksql = "update vehicle_tag_linking set vehicle_id = ?, vehicle_number = ?, customer_id = ? where barcode_data='"
					+ barcode_data + "'";
			String tagdelsql = "delete from vehicle_tag_linking where tag_id = '" + tag_id + "'";
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();
			preparedStmt1 = con.prepareStatement(vehlinksql);
			preparedStmt1.setString(1, vehicle_id);
			preparedStmt1.setString(2, vehicle_number);
			preparedStmt1.setString(3, customer_id);
			preparedStmt1.executeUpdate();
			preparedStmt2 = con.prepareStatement(invsql);
			preparedStmt2.setString(1, "4");
			preparedStmt2.executeUpdate();
			preparedStmt3 = con.prepareStatement(tagdelsql);
			preparedStmt3.setString(1, tag_id);
			preparedStmt3.executeUpdate();
		} catch (Exception e) {
			log.error("TagReissuanceDAO.updateLinkingInv() :: Error Occurred :");
			e.printStackTrace();
		}

		finally {
			// DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt3);
			DatabaseManager.closePreparedStatement(preparedStmt2);
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closeConnection(con);
		}
	}

	public static void updateReissuanceInventoty(VehicleDetailsDTO vehicleDetailsDTO, Connection conn) {
		CallableStatement cs = null;
		log.info("vehicleDetailsDTO "+vehicleDetailsDTO.toString());
		try {
			String prSql = "{call pr_reissuance(?,?,?,?,?)}";
			cs = conn.prepareCall(prSql);
			cs.setString(1, vehicleDetailsDTO.getTid());
			cs.setString(2, vehicleDetailsDTO.getOldTagId());
			cs.setString(3, vehicleDetailsDTO.getVehicleId());
			cs.setString(4, vehicleDetailsDTO.getVehicleNumber());
			cs.setString(5, vehicleDetailsDTO.getCustomerId());
			cs.execute();
		} catch (Exception e) {
			log.info("Exception in update reissue inventory." + e.getMessage());
		}
		finally {
			DatabaseManager.closeCallableStatement(cs);
		}
	}
	
	//old call
	public String registrationApiCall(String tag_id, String tid, String vehicle_number) {
		String checkresp = null;
		// String resp = null;
		String ttype = "ReqMngTagEntries";
		String opid = "ADD";
		String issueDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String excCode = "00";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Statement stmt2 = null;
		ResultSet rs2 = null;
		Statement stmt3 = null;
		ResultSet rs3 = null;
		String vehicleClass = null;
		String comVehicle = null;
		String seqNO = null;
		String txnID = null;
		String line = null;
		StringBuffer registrationResp = new StringBuffer();
		String regResp = null;
		try {
			String registrationURL = IssuerPropertyReader.getPropertyValue("manageTagURL");
			
			log.info(
					("TagReissuanceDAO.java :: registrationApiCall() :: registrationURL  :: getfrom netcissuer.properties file manageTagURL "
							+ registrationURL));
			conn = DatabaseManager.getConnection();// new
																// DBConnection().getConnection();
			stmt = conn.createStatement();
			String sql1 = "select * from inventory_master where tid='" + tid + "'";
			String sql2 = "select * from customer_vehicle_master where vehicle_number='" + vehicle_number + "'";
			stmt2 = conn.createStatement();
			rs2 = stmt2.executeQuery(sql1);
			log.info(("sql1 :::: " + sql1));
			if (rs2.next()) {
				vehicleClass = rs2.getString("tag_class_id");
				log.info(("vehicleClass :: " + vehicleClass));
			}
			stmt3 = conn.createStatement();
			rs3 = stmt3.executeQuery(sql2);
			log.info(("sql2 :::: " + sql2));
			if (rs3.next()) {
				comVehicle = rs3.getString("is_commercial");
				if (comVehicle.equals("0")) {
					comVehicle = "F";
				} else {
					comVehicle = "T";
				}
				log.info(("comVehicle :: " + comVehicle));
			}
			String postData = "ttype=" + ttype + "&opid=" + opid + "&tagId=" + tag_id + "&tid=" + tid + "&issueDate="
					+ issueDate + "&excCode=" + excCode + "&vehicleClass=" + vehicleClass + "&regNum=" + vehicle_number
					+ "&comVehicle=" + comVehicle;
			URL url = new URL(registrationURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			log.info("TagReissuanceDAO.registrationApiCall() :: Output Stream Open");
			wr.write(postData);
			wr.flush();
			log.info(("TagReissuanceDAO.registrationApiCall() :: Response Code : " + con.getResponseCode()));
			if (con.getResponseCode() == 200) {
				log.info(("TagReissuanceDAO.registrationApiCall() :: Response Code " + con.getResponseCode()
						+ " is HTTP OK...."));
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = br.readLine()) != null) {
					log.info(("TagReissuanceDAO.registrationApiCall() :: Line : " + line));
					registrationResp.append(line);
				}
				MemoryComponent.closeBufferedReader(br);
			}
			con.disconnect();
			regResp = registrationResp.toString();
			wr.write(regResp);
			MemoryComponent.closeOutputStreamWriter(wr);
			log.info(("TagReissuanceDAO.registrationApiCall() :: Response : " + regResp));
			if (regResp != null) {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(regResp));
				Document doc = db.parse(is);
				NodeList respnode = doc.getElementsByTagName("Resp");
				Element respelement = (Element) respnode.item(0);
				NamedNodeMap respattributes = respelement.getAttributes();
				for (int i = 0; i < respattributes.getLength(); ++i) {
					Attr attr = (Attr) respattributes.item(i);
					String attrName = attr.getNodeName();
					if (attrName.equalsIgnoreCase("respCode")) {
						String respCode = attr.getNodeValue();
						log.info(("respCode : " + respCode));
						break;
					}
				}
				NodeList exception = doc.getElementsByTagName("Tag");
				Element element = (Element) exception.item(0);
				NamedNodeMap attributes = element.getAttributes();
				String errCode = null;
				String result = null;
				for (int j = 0; j < attributes.getLength(); ++j) {
					Attr attr2 = (Attr) attributes.item(j);
					String attrName2 = attr2.getNodeName();
					if (attrName2.equals("errCode")) {
						errCode = attr2.getNodeValue();
					}
					if (attrName2.equals("result")) {
						result = attr2.getNodeValue();
					}
					if (attrName2.equals("seqNum")) {
						seqNO = attr2.getNodeValue();
					}
				}
				if ((errCode.equals("000") || errCode.equals("240")) && result.equalsIgnoreCase("SUCCESS")) {
					log.info("Result Success...");
					NodeList txn = doc.getElementsByTagName("Txn");
					Element txnelement = (Element) txn.item(0);
					NamedNodeMap txnattributes = txnelement.getAttributes();
					for (int k = 0; k < txnattributes.getLength(); ++k) {
						Attr attr3 = (Attr) txnattributes.item(k);
						String attrName3 = attr3.getNodeName();
						if (attrName3.equals("id")) {
							txnID = attr3.getNodeValue();
							log.info(("txnID : " + txnID));
						}
					}
					this.insertChallan(tag_id, tid, vehicle_number, txnID, seqNO);
					log.info("  :::java  ::   TID can allocated.................");
					checkresp = "success";
				} else {
					String msg = "-RESULT-" + result + "-ERRORCODE-" + errCode;
					log.info(("Response Result :: " + msg));
					checkresp = msg;
				}
				log.info(("errCode : " + errCode));
				log.info(("result :" + result));
				log.info(("  :::java  ::   Root element of the response is :: "
						+ doc.getDocumentElement().getNodeName()));
			} else {
				log.info("Tag could not be registered. regResp is null.");
				checkresp = "Fail-Response is null.";
			}
		} catch (Exception e) {
			log.info(("registrationApiCall() :: Error Occurred : " + e.getMessage()));
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs3);
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs);
			// DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeStatement(stmt3);
			DatabaseManager.closeStatement(stmt2);
			DatabaseManager.closeStatement(stmt);
			DatabaseManager.closeConnection(conn);
		}
		return checkresp;
	}
	
	//old insert challan
	private void insertChallan(String tagId, String TID, String VehicleNumber, String txnID, String seqNO) {
		Connection conn = null;
		CallableStatement callableStatement = null;
		try {
			String iinNo = IssuerPropertyReader.getPropertyValue("issuerIIN");
			if (iinNo != null) {
				log.info("insertChallan PROCEDURE pr_reissuance_challan call ");
				conn = DatabaseManager.getConnection();// new
																	// DBConnection().getConnection();
				conn.setAutoCommit(false);
				String query = "{call pr_reissuance_challan(?, ?, ?, ?, ?, ?)}";
				callableStatement = conn.prepareCall(query);
				callableStatement.setString(1, tagId);
				callableStatement.setString(2, TID);
				callableStatement.setString(3, VehicleNumber);
				callableStatement.setString(4, txnID);
				callableStatement.setString(5, seqNO);
				callableStatement.setString(6, iinNo);
				callableStatement.execute();
				conn.commit();
			} else {
				log.info("insertChallan() iin number not found :: ");
			}
		} catch (Exception e) {
			log.info(("Excepiton in insertChallan() : " + e));
			e.printStackTrace();

		} finally {
			DatabaseManager.closeCallableStatement(callableStatement);
			DatabaseManager.closeConnection(conn);
		}
	}
	
	public static void insertChallan(VehicleDetailsDTO vehicleDetailsDTO, String txnId, String sequenceNumber) {
		Connection conn = null;
		CallableStatement callableStatement = null;
		try {
			String iinNo = IssuerPropertyReader.getPropertyValue("issuerIIN");
			if (iinNo != null) {
				log.info("insertChallan PROCEDURE pr_reissuance_challan call ");
				conn = DatabaseManager.getConnection();// new
																	// DBConnection().getConnection();
				conn.setAutoCommit(false);
				String query = "{call pr_reissuance_challan(?, ?, ?, ?, ?, ?)}";
				callableStatement = conn.prepareCall(query);
				callableStatement.setString(1, vehicleDetailsDTO.getTagId());
				callableStatement.setString(2, vehicleDetailsDTO.getTid());
				callableStatement.setString(3, vehicleDetailsDTO.getVehicleNumber());
				callableStatement.setString(4, txnId);
				callableStatement.setString(5, sequenceNumber);
				callableStatement.setString(6, iinNo);
				callableStatement.execute();
				conn.commit();
			} else {
				log.info("insertChallan() iin number not found :: ");
			}
		} catch (Exception e) {
			log.info(("Excepiton in insertChallan() : " + e));
			e.printStackTrace();

		} finally {
			DatabaseManager.closeCallableStatement(callableStatement);
			DatabaseManager.closeConnection(conn);
		}
	}
	
	
	
	public static VehicleDetailsDTO getReissuanceTagDetails(String vehicleNumber, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		VehicleDetailsDTO vehicleDetailsDTO = null;
		try {
			String sql1 = "select * from vehicle_tag_linking where vehicle_number = ? ";
			ps = conn.prepareStatement(sql1);
			ps.setString(1, vehicleNumber);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				vehicleDetailsDTO = new VehicleDetailsDTO();
				vehicleDetailsDTO.setTagClassId(rs.getString("tag_class_id"));
				vehicleDetailsDTO.setTagId(rs.getString("tag_id"));
				vehicleDetailsDTO.setVehicleId(rs.getString("vehicle_id"));
				vehicleDetailsDTO.setVehicleNumber(rs.getString("vehicle_number"));
				vehicleDetailsDTO.setCustomerId(rs.getString("customer_id"));
				vehicleDetailsDTO.setTid(rs.getString("tid"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return vehicleDetailsDTO;
	}
	
	public static VehicleDetailsDTO getBarcodeTagDetails(VehicleDetailsDTO vehicleDetailsDTO, Connection conn) {
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null, rs1 = null;
		//VehicleDetailsDTO vehicleDetailsDTO = null;
		log.info("barCode "+vehicleDetailsDTO.toString());
		try {
			String sql = "select * from vehicle_tag_linking where barcode_data = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, vehicleDetailsDTO.getBarcodeData());
			rs = ps.executeQuery();
			//vehicleDetailsDTO = new VehicleDetailsDTO();
			vehicleDetailsDTO.setOldTagId(vehicleDetailsDTO.getTagId());
			if (rs.next()) {
				//vehicleDetailsDTO.setTagClassId(rs.getString("tag_class_id"));
				vehicleDetailsDTO.setVehicleClassId(rs.getString("tag_class_id"));
				vehicleDetailsDTO.setTagId(rs.getString("tag_id"));
				//vehicleDetailsDTO.setVehicleId(rs.getString("vehicle_id"));
				//vehicleDetailsDTO.setVehicleNumber(rs.getString("vehicle_number"));
				//vehicleDetailsDTO.setCustomerId(rs.getString("customer_id"));
				vehicleDetailsDTO.setTid(rs.getString("tid"));
			}
			
			log.info("vehicleDetailsDTO "+vehicleDetailsDTO.toString());
			String sql1 = "select is_commercial from customer_vehicle_info where vehicle_number = ? ";
			ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, vehicleDetailsDTO.getVehicleNumber());
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				vehicleDetailsDTO.setIsCommercial(rs1.getString("is_commercial"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeResultSet(rs1);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closePreparedStatement(ps1);
		}
		return vehicleDetailsDTO;
	}
	
	public static List<BarCodeDataDTO> getBarcodeData(String tagClassId, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BarCodeDataDTO> lst = null;
		try {
			
			String sql = "select barcode_data, tag_id, tid from vehicle_tag_linking where ((vehicle_id is null or vehicle_id = '') and (vehicle_number is null or vehicle_number = '')) and tid in (select tid from inventory_master where branch_id = ? and agent_id = ? and status = ? and tag_class_id = ? )";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "0");
			ps.setString(2, "0");
			ps.setString(3, "3");
			ps.setString(4, tagClassId);
			rs = ps.executeQuery();
			lst = new ArrayList<>();
			while (rs.next()) {
				BarCodeDataDTO barCodeDataDTO = new BarCodeDataDTO();
				barCodeDataDTO.setBarCodeData(rs.getString("barcode_data"));
				barCodeDataDTO.setTagId(rs.getString("tag_id"));
				barCodeDataDTO.setTid(rs.getString("tid"));
				lst.add(barCodeDataDTO);
			}
			
			return lst;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;
	}
}