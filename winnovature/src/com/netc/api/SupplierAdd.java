package com.netc.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.constants.IDGenerator;
import com.winnovature.dao.CheckSession;
import com.winnovature.dao.DAOManager;
import com.winnovature.utils.AuditTrail;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.PasswordManager;

@WebServlet("/supplier/add")
public class SupplierAdd extends HttpServlet {
	static Logger log = Logger.getLogger(SupplierAdd.class.getName());
	private static final long serialVersionUID = 1L;

	public SupplierAdd() {
		super();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String checkadd = null;

		JSONObject jresp = new JSONObject();

		PrintWriter out = response.getWriter();
		DAOManager daoManager = new DAOManager();

		StringBuffer sbuffer = new StringBuffer();
		String line = null;

		String supplierName, emailId, contactPerson, contactNumber1, contactNumber2, deliveryPeriod, maxOrderQty,
				minOrderQty, is_npci_certified, npci_certification_expiry, GSTN, webSite, status;// for
																									// agent

		String resiAddr1, resiAddr2, resiPin, resiCity, resiState, businessAddr1, businessAddr2, businessPin,
				businessCity, businessState;// for address

		String accountNo, bankName, branchAddress, ifscCode, accountType;// for

		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sbuffer.append(line);

			}
			MemoryComponent.closeBufferedReader(reader);
			JSONObject js = new JSONObject(sbuffer.toString());

			String ipAddress = request.getRemoteAddr();
			
			String supplierId = new IDGenerator().getTagSupplierId();

			log.info("supplier ID =  " + supplierId);
			log.info("supplier Request =  " + js);

			String createdBy = null;
			String password = PasswordManager.getPasswordSaltString(); // GenerateRandom.randomAlphaNumeric(8);

			
			JSONObject supplierJson = js.getJSONObject("supplier");
			supplierName = supplierJson.getString("supplierName");
			emailId = supplierJson.getString("emailId");
			contactPerson = supplierJson.getString("contactPerson");
			contactNumber1 = supplierJson.getString("contactNumber1");
			contactNumber2 = supplierJson.getString("contactNumber2");
			deliveryPeriod = supplierJson.getString("deliveryPeriod");
			maxOrderQty = supplierJson.getString("maxOrderQty");
			minOrderQty = supplierJson.getString("minOrderQty");

			createdBy = request.getHeader("userId").toString();

			is_npci_certified = supplierJson.getString("isNpciCertified");
			npci_certification_expiry = supplierJson.getString("npciCertificationExpiry");
			GSTN = supplierJson.getString("GSTN");
			webSite = supplierJson.getString("webSite");
			status = supplierJson.getString("status");
			if (is_npci_certified == "true") {
				is_npci_certified = "1";
			}

			JSONObject address = js.getJSONObject("address");

			resiAddr1 = address.getString("resiAddress1");
			resiAddr2 = address.getString("resiAddress2");
			resiPin = address.getString("resiPin");
			resiCity = address.getString("resiCity");
			resiState = address.getString("resiState");
			businessAddr1 = address.getString("businessAdd1");
			businessAddr2 = address.getString("businessAdd2");
			businessPin = address.getString("businessPin");
			businessCity = address.getString("businessCity");
			businessState = address.getString("businessState");

			JSONObject account = js.getJSONObject("account");
			accountNo = account.getString("accountNumber");
			bankName = account.getString("bankName");
			branchAddress = account.getString("branchAddress");
			ifscCode = account.getString("ifscCode");
			accountType = account.getString("accountType");

			if (!DAOManager.validateSupplierID(emailId, conn)) {

				checkadd = daoManager.addSupplier(supplierId, supplierName, emailId, contactPerson, contactNumber1,
						contactNumber2, deliveryPeriod, maxOrderQty, minOrderQty, is_npci_certified,
						npci_certification_expiry, GSTN, webSite, status, resiAddr1, resiAddr2, resiPin, resiCity,
						resiState, businessAddr1, businessAddr2, businessPin, businessCity, businessState, accountNo,
						bankName, branchAddress, ifscCode, accountType, createdBy, password, conn);

				if (checkadd.equalsIgnoreCase("1")) {
					jresp.put("message", "Supplier Added successfully");
					jresp.put("status", "1");
					jresp.put("supplierId", supplierId);

					Map<String, String> hm = new HashMap<String, String>();
					hm.put("SUPPLIERID", supplierId);
					hm.put("SUPPLIERNAME", supplierName);
					hm.put("EMAILID", emailId);

					hm.put("CONTACTNO", contactNumber1);
					hm.put("RESIDENCYADDRESS", resiAddr1);
					hm.put("RESIDENCYPIN", resiPin);
					hm.put("RESIDENCYCITY", resiCity);
					hm.put("RESIDENCYSTATE", resiState);

					hm.put("BUSINESSADDRESS1", businessAddr1);
					hm.put("BUSINESSADDRESS2", businessAddr2);
					hm.put("BUSINESSPIN", businessPin);
					hm.put("BUSINESSCITY", businessCity);
					hm.put("BUSINESSSTATE", businessState);

					//Connection con = DatabaseManager.getConnection();
					AuditTrail auditTrail = new AuditTrail();
					auditTrail.addAuditData(conn, createdBy, supplierId, "ADDSUPPLIER", "ADDSUPPLIERSUCCESS", hm, ipAddress);

				}

				else if (checkadd.equalsIgnoreCase("-1")) {
					jresp.put("message", "Error code 1420 : Sorry, Can't add supplier. !!!");
					jresp.put("status", "0");
				}

				else if (checkadd.equalsIgnoreCase("-2")) {
					jresp.put("message", "Error code 1400 : Sorry, Can't add supplier...");
					jresp.put("status", "0");
				}

				else {
					jresp.put("message", "Sorry, Can't add supplier...!!!");
					jresp.put("status", "0");
				}
			} else {
				jresp.put("message", "Supplier email id already present...!!!");
				jresp.put("status", "0");
			}

			/*
			 * } else { jresp.put("flag", "0"); }
			 */
			out.write(jresp.toString());
		}

		catch (Exception e) {
			log.error("SupplierAdd.java :: Getting Exception   :::    " + e.getMessage());
			jresp.put("message",
					"Error code 1440 :  Getting error while process request.  Sorry, Can't add supplier...");
			jresp.put("status", "0");
			log.error("Getting Exception   :::    ", e);
			out.write(jresp.toString());
		}

		finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
