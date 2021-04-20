package com.winnovature.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
//import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.PropertyReader;

public class ManageTagResponse {
	static Logger log = Logger.getLogger(ManageTagResponse.class.getName());

	public static String checkMapperRegistration(String XMLData) {
		/*
		 * XMLData =
		 * "<?xml version='1.0' encoding='UTF-8'?><etc:RespMngTagEntries xmlns:etc='http://npci.org/etc/schema/'> "
		 * +"<Head msgId='0000000000000001111112' orgId='DCBX' ts='2016-08-10T18:35:18' ver='1.0'/>"
		 * +"<Txn id='0000000000000001111112' note='' orgTxnId='' refId='' refUrl='' ts='2016-08-10T18:35:18' type='ManageTag'>"
		 * +
		 * "<Resp respCode='000' result='SUCCESS' sucessReqCnt='1' totReqCnt='1' ts='2016-08-10T19:00:44'>"
		 * +
		 * "<Tag errCode='000' op='ADD' result='SUCCESS' seqNum='1' tagId='34161FA82032D69802A7B160'/>"
		 * +"</Resp>" +"</Txn>"
		 * +"<Signature xmlns='http://www.w3.org/2000/09/xmldsig#'><SignedInfo><CanonicalizationMethod Algorithm='http://www.w3.org/TR/2001/REC-xml-c14n-20010315'/><SignatureMethod Algorithm='http://www.w3.org/2000/09/xmldsig#rsa-sha1'/><Reference URI=''><Transforms><Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/></Transforms><DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><DigestValue>D36ar+u5hpRecQz8K4rRs9rL/qcdduIYpuDwLYErUeM=</DigestValue></Reference></SignedInfo><SignatureValue>svh/O1JkwJFbYNfAl+DtGQ+6v6RrDG6PjUNqJd43xY0+I26Wuayt6rTlZEbUhXmdHPi4LuZW6RzC"
		 * +"c3Ag/3L5Kyl4+XPgY4T5Ib87AZ2FtX+H+O8Gp477c4hYlr8nm3d2if1J6lzts5uoRhPPkEuo/dbv"
		 * +"dCFsmXdhAHMe5fle1slFwcQf7OhW5gM45zmJ6f1cWNeojOzbl8vEdtQPYMmZDM3neZbF2Gx0FGJT"
		 * +"io69DjnEq7b+jC36qVh6+fj3AnviNguWVkHTLUFYaFJXvZJdIQrbyl6GY/6DiFyBEPnR+24oTPjU"
		 * +"VXzIY/FPJNgi88F4iGNgsxTKnTAF72n1dxd6og==</SignatureValue><KeyInfo><X509Data><X509SubjectName>CN=cm.npci.org.in,ST=TN,OU=Centralized Mapper,O=NPCI,C=IN</X509SubjectName><X509Certificate>MIIFfTCCBGWgAwIBAgIKECMkJ8Fo6zEs8zANBgkqhkiG9w0BAQsFADCB9jELMAkGA1UEBhMCSU4x"
		 * +"RTBDBgNVBAoTPEluc3RpdHV0ZSBmb3IgRGV2ZWxvcG1lbnQgYW5kIFJlc2VhcmNoIGluIEJhbmtp"
		 * +"bmcgVGVjaG5vbG9neTEdMBsGA1UECxMUQ2VydGlmeWluZyBBdXRob3JpdHkxDzANBgNVBBETBjUw"
		 * +"MDA1NzESMBAGA1UECBMJVGVsYW5nYW5hMSkwJwYDVQQJEyBSb2FkIE5vLjEsIE1hc2FiIFRhbmss"
		 * +"IEh5ZGVyYWJhZDEVMBMGA1UEMxMMQ2FzdGxlIEhpbGxzMRowGAYDVQQDExFJRFJCVCBDQSBTUEwg"
		 * +"MjAxNjAeFw0xNjAyMjYxMDAyMzZaFw0xODAyMjYxMDAyMzZaMF8xCzAJBgNVBAYTAklOMQ0wCwYD"
		 * +"VQQKEwROUENJMRswGQYDVQQLExJDZW50cmFsaXplZCBNYXBwZXIxCzAJBgNVBAgTAlROMRcwFQYD"
		 * +"VQQDEw5jbS5ucGNpLm9yZy5pbjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALnFiqPU"
		 * +"62LW9nrWS7D97ToLv72u51pGixLdazIg30hh6/ZjkNSlqtIGSBC+TT8My01a4lWRkWuj4khy83RJ"
		 * +"8TMIrEM8AHghESzXp5aHbN65XXuWefTVy3HdACIOMgK/guC6cbH1fFsfv/FpO4RIZjoEJDHYUDrr"
		 * +"Mxmua6pXi3buHQN/DhNWk4jZ9wEibIkpK/d0J/xOyVUyGPdskQH6ejOHBMnp3VoYGQXTCuLOkLJP"
		 * +"g6LMNXry1+PpezSyuq4ns+IVbEWmJoQC6VefrZvbASFqzFiI6P5HkrFTev/mNMWlsFTwUVqxZRqz"
		 * +"WfEYbKKIjC0fwdCU6mOqnJ+6Tm2a/ikCAwEAAaOCAaEwggGdMA4GA1UdDwEB/wQEAwIFoDAdBgNV"
		 * +"HSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwgbUGA1UdIASBrTCBqjCBpwYGYIJkZAIDMIGcMIGZ"
		 * +"BggrBgEFBQcCAjCBjBqBiUNsYXNzIDMgbGV2ZWwgaXMgcmVsZXZhbnQgdG8gZW52aXJvbm1lbnRz"
		 * +"IHdoZXJlIHRocmVhdHMgdG8gZGF0YSBhcmUgaGlnaCBvciB0aGUgY29uc2VxdWVuY2VzIG9mIHRo"
		 * +"ZSBmYWlsdXJlIG9mIHNlY3VyaXR5IHNlcnZpY2VzIGFyZSBoaWdoMFkGA1UdHwRSMFAwJKAioCCG"
		 * +"Hmh0dHA6Ly8xMC4wLjY1LjY1L2NybF8yN0JBLmNybDAooCagJIYiaHR0cDovL2lkcmJ0Y2Eub3Jn"
		 * +"LmluL2NybF8yN0JBLmNybDAfBgNVHSMEGDAWgBRTPr+bw5KofjGVW9KXh1YFP/TqEzAZBgNVHREE"
		 * +"EjAQgg5jbS5ucGNpLm9yZy5pbjAdBgNVHQ4EFgQUsxGtCEaHH+sxEkkb9ukhi7Y98vIwDQYJKoZI"
		 * +"hvcNAQELBQADggEBAMZbQ2FSpCTKDQK/LSKeWOsDaWfhE7lp7EKskCgWKfCdmcV5SVXz0+29vQ6I"
		 * +"wY93kXndGdNSg9RuO17OOeX8Ow6uJXtnrmyBCT7LFD0BrTtnqOIu+l7Y4KUjJGbyprJErVLIluqI"
		 * +"NkeHcO1qmyFsd6yut21b0GT8p7q9/5gIciTkQvMhsBBSsVPf4TLuHEzBFoBe7v2DD19DncWGiPYm"
		 * +"bwM7EaTy2SCcR1vEZjAkLYgMLT2OsSnOBPVf1zoc6Pt+g6pSbvmPA5maLgm7AAAcYDwERYXHRdsE"
		 * +"JvylE/w5lIg1KNwIcMdBMIn7ZbHyeMKE7wngYr8ZcR022kqN9DEFkPU=</X509Certificate></X509Data></KeyInfo></Signature></etc:RespMngTagEntries>";
		 */
		String txnID = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(XMLData));

			Document doc = db.parse(is);

			// for Resp node
			NodeList resp = doc.getElementsByTagName("Resp");
			Element respelement = (Element) resp.item(0);

			NamedNodeMap respattributes = respelement.getAttributes();

			for (int i = 0; i < respattributes.getLength(); i++) {
				Attr attr = (Attr) respattributes.item(i);
				String attrName = attr.getNodeName();
				String attrValue = attr.getNodeValue();
				if (attrName.equalsIgnoreCase("respCode")) {
					String respCode = attr.getNodeValue();
					log.info("respCode : " + respCode);
					break;
				}
			}
			// for tag node
			NodeList exception = doc.getElementsByTagName("Tag");
			Element element = (Element) exception.item(0);
			NamedNodeMap attributes = element.getAttributes();
			String errCode = null, result = null;
			// boolean flag = false;
			for (int i = 0; i < attributes.getLength(); i++) {
				Attr attr = (Attr) attributes.item(i);
				String attrName = attr.getNodeName();
				String attrValue = attr.getNodeValue();
				if (attrName.equals("errCode")) {
					errCode = attr.getNodeValue();
				}
				if (attrName.equals("result")) {
					result = attr.getNodeValue();
				}
			}
			if (errCode.equals("000") && result.equalsIgnoreCase("SUCCESS")) {

				log.info("Result Success...");
				NodeList txn = doc.getElementsByTagName("Txn");
				Element txnelement = (Element) txn.item(0);
				NamedNodeMap txnattributes = txnelement.getAttributes();
				for (int i = 0; i < txnattributes.getLength(); i++) {
					Attr attr = (Attr) txnattributes.item(i);
					String attrName = attr.getNodeName();
					String attrValue = attr.getNodeValue();
					if (attrName.equals("id")) {
						txnID = attr.getNodeValue();
						log.info("txnID : " + txnID);
					}
				}
			}

			log.info("errCode : " + errCode);
			log.info("result :" + result);
			log.info("ManageTagResponse.java :: Root element of the response is :: "
					+ doc.getDocumentElement().getNodeName());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return txnID;
	}

	public String mngTagApiCall(String tid, String regNum, Connection conn) throws Exception {
		// (String tagId, String tid, String vehicleClass, String regNum, String
		// comVehicle) throws Exception

		// ttype=ReqMngTagEntries&opid=ADD&tagId=34161FA820328C6E02001680&tid=E200341201471D0002279B60&issueDate=17-04-2018&excCode=00&vehicleClass=VC7&regNum=KA01AA4010&comVehicle=F
		CallableStatement callableStatement = null;

		String tagId = null;
		String vehicleClass = null;
		String comVehicle = null;
		try {
			if (tid != null && regNum != null) {

				String query = "{call pr_registration_data(?,?,?,?,?,?,?,?,?)}";
				callableStatement = conn.prepareCall(query);
				callableStatement.setString(1, tid);
				callableStatement.setString(2, regNum);
				callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);

				callableStatement.execute();

				tagId = callableStatement.getString(3);
				vehicleClass = callableStatement.getString(4);
				comVehicle = callableStatement.getString(5);

				if (comVehicle.equals("Y"))
					comVehicle = "T";
				else
					comVehicle = "F";

				log.info("tagId : " + tagId + " vehicleClass : " + vehicleClass + " comVehicle : " + comVehicle);
			}
		} catch (Exception e) {
			log.info("ManageTagResponse.java  :::  Exception while ManageTagEntries in mngTagApiCall() : "
					+ e.getMessage());
			log.error("Getting Exception in  mngTagApiCall() ::: ", e);
		} finally {
			DatabaseManager.closeCallableStatement(callableStatement);
		}

		String resp = null;
		if (!tagId.equalsIgnoreCase("NA") || !vehicleClass.equalsIgnoreCase("NA")) {
			String ttype = "ReqMngTagEntries";
			String opid = "ADD";
			String issueDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			String excCode = "00";
			String postData = "ttype=" + ttype + "&opid=" + opid + "&tagId=" + tagId + "&tid=" + tid + "&issueDate="
					+ issueDate + "&excCode=" + excCode + "&vehicleClass=" + vehicleClass + "&regNum=" + regNum
					+ "&comVehicle=" + comVehicle;
			// URL url = new URL("http://10.45.16.11:8082/TestServlet");
			log.info("<<<<< ManageTagResponse.java >>>>>  :: "
					+ new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
			String mngTagURL = PropertyReader.getPropertyValue("manageTagURL");
			log.info("<<<<< ManageTagResponse.java >>>>>"
					+ new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
			// String mngTagURL = //NetcUtils.getPropertyValue("manageTagURL");
			log.info("ManageTagResponse.java GetURL  manageTagURL :: from property file  :: " + mngTagURL);

			log.info("ManageTagResponse.java final request ::  postData :: " + postData);
			URL url = new URL(mngTagURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			log.info("ManageTagResponse.java ::: Output Stream Open");
			wr.write(postData);
			MemoryComponent.closeOutputStreamWriter(wr);

			StringBuffer outputResp = new StringBuffer();
			String line = null;

			log.info("ManageTagResponse.java ::: Response Code : " + con.getResponseCode());
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				log.info("ManageTagResponse.java ::: Response Code " + con.getResponseCode() + " is HTTP OK....");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while ((line = br.readLine()) != null) {
					log.info("ManageTagResponse.java ::: Line : " + line);
					outputResp.append(line);
				}
				MemoryComponent.closeBufferedReader(br);
				resp = outputResp.toString();

			} else {
				log.info("ManageTagResponse.java ::: mngTagURL : " + mngTagURL + " response code :: "
						+ con.getResponseCode());
				resp = outputResp.toString();
			}

			log.info("ManageTagResponse.java ::: Response Buffer :: " + outputResp.toString());

		}
		return resp;

	}

	public boolean allocateTagInsertChallan(String TID, String VehicleNumber, String min_threshold, String txnID,
			String seqNO, String amtIssuence, String amtRecharge, String amtRSA, String amtSecurity,
			String amtInsurance, Connection conn) {

		CallableStatement callableStatement = null;
		boolean check = false;
		try {
			// String iinNo =
			// NetcUtils.getPropertyValue("iinNNSB");//"609977";//NetcUtils.getPropertyValue("iinNNSB");
			// //"609977";//NetcUtils.getPropertyValue("iinNNSB");
			String iinNo = PropertyReader.getPropertyValue("issuerIIN");// "607799";//NetcUtils.getPropertyValue("issuerIIN");//iinHDFC
			if (iinNo != null) {
				log.info("allocateTagInsertChallan PROCEDURE call ");

				String query = "{call pr_allocate_tag_challan(?,?,?,?,?,?,?,?,?,?,?,?)}";
				callableStatement = conn.prepareCall(query);
				callableStatement.setString(1, TID);
				callableStatement.setString(2, VehicleNumber);
				callableStatement.setString(3, min_threshold);
				callableStatement.setString(4, txnID);
				callableStatement.setString(5, seqNO);
				callableStatement.setString(6, amtIssuence);
				callableStatement.setString(7, amtRecharge);
				callableStatement.setString(8, amtRSA);
				callableStatement.setString(9, amtSecurity);
				callableStatement.setString(10, amtInsurance);
				callableStatement.setString(11, iinNo);
				callableStatement.registerOutParameter(12, java.sql.Types.BOOLEAN);
				callableStatement.execute();
				check = callableStatement.getBoolean(12);
				
				log.info("allocateTagInsertChallan PROCEDURE return :: " + check);
				if (check)
					log.info(
							"allocateTagInsertChallan PROCEDURE insert challan and updates inventory, vehicle_tag_linking");
				else
					log.info(
							"allocateTagInsertChallan PROCEDURE could not insert challan and updates inventory, vehicle_tag_linking");
				return check;

			} else {
				log.info("allocateTagInsertChallan() iin number not found :: " + check);
				return check;
			}
		} catch (Exception e) {
			log.info("Excepiton in allocateTagInsertChallan() : " + e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeCallableStatement(callableStatement);
		}
		return check;
	}

}
