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
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.PropertyReader;

public class ReissuDao {
	static Logger log;

	static {
		log = Logger.getLogger(ReissuDao.class.getName());
	}

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
			log.error("NETCREPORT  :::java  ::  updateLinkingInv() :: Error Occurred :");
			e.printStackTrace();
		}

		finally {
			// DatabaseConnectionManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(preparedStmt3);
			DatabaseManager.closePreparedStatement(preparedStmt2);
			DatabaseManager.closePreparedStatement(preparedStmt1);
			DatabaseManager.closeConnection(con);
		}
	}

	public String blackListApiCall(String tag_id) {
		String checkresp = null;
		String ttype = "ReqMngException";
		String opid = "ADD";
		String seqNum = "1";
		String excCode = "03";
		String blacklistParameters = "ttype=" + ttype + "&opid=" + opid + "&tagId=" + tag_id + "&seqNum=" + seqNum
				+ "&excCode=" + excCode;
		String line = null;
		String blacklistResp = null;
		StringBuffer blkResp = new StringBuffer();
		try {
			String blacklistURL = PropertyReader.getPropertyValue("manageTagURL");
			log.info(("blacklistURL  :: getfrom netcissuer.properties file manageTagURL " + blacklistURL));
			URL url = new URL(blacklistURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			log.info("NETCREPORT  :::java  ::  blackListApiCall() :: Output Stream Open");
			wr.write(blacklistParameters);
			wr.flush();
			log.info(("NETCREPORT  :::java  ::  blackListApiCall() :: Response Code : " + con.getResponseCode()));
			if (con.getResponseCode() == 200) {
				log.info(
						("ReissuDao.blackListApiCall() :: Response Code " + con.getResponseCode() + " is HTTP OK...."));
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = br.readLine()) != null) {
					log.info(("NETCREPORT  :::java  ::  blackListApiCall() :: Line : " + line));
					blkResp.append(line);
				}
				MemoryComponent.closeBufferedReader(br);
			}
			con.disconnect();
			blacklistResp = blkResp.toString();
			wr.write(blacklistResp);
			MemoryComponent.closeOutputStreamWriter(wr);
			log.info(("NETCREPORT  :::java  ::  blackListApiCall() :: Response : " + blkResp));
			if (blacklistResp != null) {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(blacklistResp));
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
				}
				if ((errCode.equals("000") || errCode.equals("240") || errCode.equals("155"))
						&& result.equalsIgnoreCase("SUCCESS")) {
					log.info("NETCREPORT.java  ::: ReissueDao.java  :: Result Success...");
					checkresp = "success";
				} else {
					String msg = "-RESULT-" + result + "-ERRORCODE-" + errCode;
					log.info(("Response Result :: " + msg));
					checkresp = msg;
				}
				log.info(("errCode : " + errCode));
				log.info(("result :" + result));
				log.info(("Root element of the response is :: " + doc.getDocumentElement().getNodeName()));
			} else {
				log.info("blackListApiCall :: Response is null ");
				checkresp = "Fail-Response is null";
			}
		} catch (Exception e) {
			log.error("NETCREPORT  :::java  ::  blackListApiCall() :: Error Occurred : ", (Throwable) e);
			e.printStackTrace();
		}
		return checkresp;
	}

	public static String mngTagApiCall(String tid, String regNum) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Statement stmt2 = null;
		ResultSet rs2 = null;
		String tagId = null;
		String vehicleClass = null;
		String comVehicle = null;

		try {
			conn = DatabaseManager.getConnection();// new
																// DBConnection().getConnection();
			stmt = conn.createStatement();
			
			String sql2 = "select * from customer_vehicle_master where vehicle_number='" + regNum + "'";
			String query = "SELECT vtl.tid, vtl.tag_id, im.tid, im.tag_class_id from vehicle_tag_linking vtl JOIN inventory_master im on vtl.tid = im.tid and vtl.tid='"
					+ tid + "'";
			rs = stmt.executeQuery(query);
			log.info(("query :::: " + query));
			if (rs.next()) {
				tagId = rs.getString("tag_id");
				log.info(("Tag Id :: " + tagId));
				vehicleClass = rs.getString("tag_class_id");
				log.info(("vehicleClass :: " + vehicleClass));
			}
			stmt2 = conn.createStatement();
			rs2 = stmt2.executeQuery(sql2);
			log.info(("sql2 :::: " + sql2));
			if (rs2.next()) {
				comVehicle = rs2.getString("is_commercial");
				if (comVehicle.equals("0")) {
					comVehicle = "F";
				} else {
					comVehicle = "T";
				}
				log.info(("NETCREPORT  :::java   mngTagApiCall()  ::comVehicle :: " + comVehicle));
			}
		} catch (Exception e) {
			log.info(("NETCREPORT  :::java  ::  Exception while ManageTagEntries : " + e.getMessage()));
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs);
			// DatabaseConnectionManager.closePreparedStatement(ps);
			DatabaseManager.closeStatement(stmt2);
			DatabaseManager.closeStatement(stmt);
			DatabaseManager.closeConnection(conn);
		}

		String resp = null;
		String ttype = "ReqMngTagEntries";
		String opid = "ADD";
		String issueDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String excCode = "00";
		String postData = "ttype=" + ttype + "&opid=" + opid + "&tagId=" + tagId + "&tid=" + tid + "&issueDate="
				+ issueDate + "&excCode=" + excCode + "&vehicleClass=" + vehicleClass + "&regNum=" + regNum
				+ "&comVehicle=" + comVehicle;
		String mngTagApiURL = PropertyReader.getPropertyValue("manageTagURL");
		log.info(
				("ReissuDao.java  :: mngTagApiCall() :: mngTagApiURL  :: getfrom netcissuer.properties file manageTagURL "
						+ mngTagApiURL));
		URL url = new URL(mngTagApiURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.connect();
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		log.info("NETCREPORT  :::java   ::: Output Stream Open");
		wr.write(postData);
		MemoryComponent.closeOutputStreamWriter(wr);
		StringBuffer outputResp = new StringBuffer();
		String line = null;
		log.info(("NETCREPORT  :::java ::: Response Code : " + con.getResponseCode()));
		if (con.getResponseCode() == 200) {
			log.info(("NETCREPORT  :::java Response Code " + con.getResponseCode() + " is HTTP OK...."));
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((line = br.readLine()) != null) {
				log.info(("NETCREPORT  :::java ::: Line : " + line));
				outputResp.append(line);
			}
			resp = outputResp.toString();
			MemoryComponent.closeBufferedReader(br);
		}
		log.info(("NETCREPORT  :::java ::  Response Buffer :: " + outputResp));
		return resp;
	}

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
			String registrationURL = PropertyReader.getPropertyValue("manageTagURL");
			log.info(
					("ReissuDao.java :: registrationApiCall() :: registrationURL  :: getfrom netcissuer.properties file manageTagURL "
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
			log.info("NETCREPORTS ::registrationApiCall() :: Output Stream Open");
			wr.write(postData);
			wr.flush();
			log.info(("NETCREPORT  :::java  ::  registrationApiCall() :: Response Code : " + con.getResponseCode()));
			if (con.getResponseCode() == 200) {
				log.info(("ReissuDao.registrationApiCall() :: Response Code " + con.getResponseCode()
						+ " is HTTP OK...."));
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = br.readLine()) != null) {
					log.info(("NETCREPORT  :::java  ::  registrationApiCall() :: Line : " + line));
					registrationResp.append(line);
				}
				MemoryComponent.closeBufferedReader(br);
			}
			con.disconnect();
			regResp = registrationResp.toString();
			wr.write(regResp);
			MemoryComponent.closeOutputStreamWriter(wr);
			log.info(("NETCREPORT  :::java  ::  registrationApiCall() :: Response : " + regResp));
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
					log.info("NETCREPORT  :::java  ::   TID can allocated.................");
					checkresp = "success";
				} else {
					String msg = "-RESULT-" + result + "-ERRORCODE-" + errCode;
					log.info(("Response Result :: " + msg));
					checkresp = msg;
				}
				log.info(("errCode : " + errCode));
				log.info(("result :" + result));
				log.info(("NETCREPORT  :::java  ::   Root element of the response is :: "
						+ doc.getDocumentElement().getNodeName()));
			} else {
				log.info("Tag could not be registered. regResp is null.");
				checkresp = "Fail-Response is null.";
			}
		} catch (Exception e) {
			log.info(("NETCREPORT  :::java  ::  registrationApiCall() :: Error Occurred : " + e.getMessage()));
			e.printStackTrace();

		} finally {
			DatabaseManager.closeResultSet(rs3);
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs);
			// DatabaseConnectionManager.closePreparedStatement(ps);
			DatabaseManager.closeStatement(stmt3);
			DatabaseManager.closeStatement(stmt2);
			DatabaseManager.closeStatement(stmt);
			DatabaseManager.closeConnection(conn);
		}
		return checkresp;
	}

	private void insertChallan(String tagId, String TID, String VehicleNumber, String txnID, String seqNO) {
		Connection conn = null;
		CallableStatement callableStatement = null;
		try {
			String iinNo = PropertyReader.getPropertyValue("issuerIIN");
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

	public JSONObject getReissueTagDetails(String vehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String taglinksql = null;
		JSONArray barcode = new JSONArray();
		JSONObject obj = new JSONObject();
		String tagClassId = null;
		
		try {
			con = DatabaseManager.getConnection();// new
															// DBConnection().getConnection();
			String query1 = "select * from vehicle_tag_linking where vehicle_number = ? ";

			ps = con.prepareStatement(query1);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				 //String vehicleId = rs.getString("vehicle_id");
				tagClassId = rs.getString("tag_class_id");
				log.info("reissuance.jsp ::  TagClassID :: " + tagClassId
						+ " show available barcode releted to tag class id");
				String tagId = rs.getString("tag_id");
				String vehicleId = rs.getString("vehicle_id");
				String vehicleNumber = rs.getString("vehicle_number");
				String custId = rs.getString("customer_id");
				String tid = rs.getString("tid");

				log.info("tag_id :: " + tagId + " vehicle_id :: " + vehicleId + " vehicle_number :: " + vehicleNumber
						+ " customer_id :: " + custId);
				obj.put("tagId", tagId);
				obj.put("vehicleId", vehicleId);
				obj.put("vehicleNumber", vehicleNumber);
				obj.put("custId", custId);
				obj.put("status", true);
				obj.put("tagClassId", tagClassId);
				obj.put("tid", tid);
				
			}

			else {
				obj.put("status", false);
				obj.put("Message", "Records not Found for given vehicle number.");
			}
			String query2 = "select barcode_data from vehicle_tag_linking where ((vehicle_id is null or vehicle_id = '') and (vehicle_number is null or vehicle_number = '')) and tid in (select tid from inventory_master where branch_id = ? and agent_id = ? and status = ? and tag_class_id = ? )";
			ps1 = con.prepareStatement(query2);
			ps1.setString(1, "0");
			ps1.setString(2, "0");
			ps1.setString(3, "3");
			ps1.setString(4, tagClassId);
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				JSONObject json1 = new JSONObject();
				json1.put("barcode", rs1.getString("barcode_data"));
				barcode.put(json1);
			//	log.info("count  ::"+count+"barcode  ::"+json1.toString());
				
				
			}
			
			obj.put("barcode_data", barcode);

			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs1);
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps1);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(con);
		}
		return obj;
	}
}