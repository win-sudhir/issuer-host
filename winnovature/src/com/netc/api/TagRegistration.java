package com.netc.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.ManageTagResponse;
import com.winnovature.dao.TagAllocationService;
import com.winnovature.dao.TagRegistrationDAO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.service.TransactionService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/tag/registration")
public class TagRegistration extends HttpServlet {
	static Logger log = Logger.getLogger(TagRegistration.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jresp = new JSONObject();
		boolean isallocated = false;
		boolean isgenerated = false;
		PrintWriter out = response.getWriter();

		TagRegistrationDAO tagallocationDao = new TagRegistrationDAO();

		StringBuffer sbuffer = new StringBuffer();
		String line = null;

		String isCommercial = null;
		String TID, vehicleNumber, min_threshold; // for allocation
		String amtIssuence, amtRecharge, amtRSA, amtSecurity, amtInsurance;
		JSONObject challan = new JSONObject();

		String txnID = null, seqNO = null;

		// boolean respStatus = false;
		String status = "0";
		String message = null;

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

			log.info("TagAllocation.java ::: JSON REQUEST :: " + js);

			TID = js.getString("TID");
			vehicleNumber = js.getString("vehicleNumber");
			min_threshold = "0";// js.getString("ThresholdNo");
			amtIssuence = js.getString("amtIssuence");
			amtRecharge = "0";// js.getString("amtRecharge");
			amtRSA = "0";// js.getString("amtRSA");
			amtSecurity = js.getString("amtSecurity");
			amtInsurance = "0";// js.getString("amtInsurance");
			isCommercial = tagallocationDao.getIsCommercial(vehicleNumber);
			log.info("TagAllocation.java ::: isCommercial  : " + isCommercial);

			log.info("TID : " + TID + " VehicleNumber : " + vehicleNumber + " Min Threshold : " + min_threshold);
			log.info("amtIssuence : " + amtIssuence + " amtRecharge : " + amtRecharge + " amtRSA : " + amtRSA
					+ "  amtSecurity : " + amtSecurity + "  amtInsurance  : " + amtInsurance);

			String userId = request.getHeader("userId").toString();
			String auth_token = request.getHeader("Authorization").toString();//
			// if (userId != null && auth_token != null && LoginDao.isValidSession(userId,
			// auth_token))
			// {

			if (TagRegistrationDAO.checkBalance(vehicleNumber, amtIssuence, amtSecurity)) {
				log.info("Balance not sufficient.");
				message = "In-sufficient balance.";
				status = "0";
				jresp.put("message", message);
				jresp.put("status", status);
				return;
			}

			isallocated = tagallocationDao.allocateTag(TID, vehicleNumber, min_threshold);
			log.info("is allocated value == " + isallocated);
			if (isallocated) {
				message = "TID not found in the inventory";
				status = "0";
				jresp.put("message", message);
				jresp.put("status", status);
				return;
			} else {

				String XMLData = new ManageTagResponse().mngTagApiCall(TID, vehicleNumber);
				JSONObject parsedXML = new TagAllocationService().parseTagAllocationProcess(XMLData);
				if (parsedXML != null) {

					String errCode = parsedXML.getString("errCode");
					String result = parsedXML.getString("result");
					seqNO = parsedXML.getString("seqNO");
					txnID = parsedXML.getString("txnID");
					/*
					 * String errCode = "000"; String result = "SUCCESS"; seqNO = "1"; txnID =
					 * "123";
					 */
					if ((errCode.equals("000") || errCode.equals("240")) && result.equalsIgnoreCase("SUCCESS")) {
						log.info("Result Success...");
						///////////////////////////////////////////////////
						// String tagClassId = null;
						// tagClassId = new DAOManager().getTagclassID(vehicleNumber);
						// amtIssuence = tagallocationDao.getTagAllocationCharges(tagClassId);

						isgenerated = new ManageTagResponse().allocateTagInsertChallan(TID, vehicleNumber,
								min_threshold, txnID, seqNO, amtIssuence, amtRecharge, amtRSA, amtSecurity,
								amtInsurance);
						if (isgenerated) {
							// using normal function
							status = "1";
							message = "Tag registered successfully in NPCI.";
							challan = TagRegistrationDAO.challanData(TID, vehicleNumber, min_threshold);
							log.info("CHALLAN return challanData :: " + challan.toString());

							log.info("TID registered and allocated.................");
							// tag allocation txn-----

							TransactionDTO transactionDTO = new TransactionDTO();
							transactionDTO.setTxnAmount(amtIssuence);
							transactionDTO.setSecurityDeposit(amtSecurity);
							transactionDTO.setUserId(vehicleNumber);
							// Connection conn = DatabaseManager.getConnection();
							TransactionService.doTagTransaction(conn, transactionDTO, userId);
							DatabaseManager.closeConnection(conn);

							// tag allocation end-----
						}

						else {
							message = "CHALLAN is not generated";
							status = "0";
							log.info("CHALLAN is not generated :: isgenerated : " + isgenerated);
						}
					}

					else {
						message = "Tag could not be registered-RESULT" + result + "-ERRORCODE-" + errCode;
						status = "0";
					}
				}
			}
			jresp.put("message", message);
			jresp.put("status", status);
			jresp.put("challan", challan);

			log.info("Tag Allocation Response >>>>>" + jresp.toString());

			/*
			 * }
			 * 
			 * else { jresp.put("flag", "0"); out.write(jresp.toString()); }
			 */
		} // try end

		catch (Exception e) {
			log.error("Exception in TagAllcation ::  ", e);
		}

		finally {
			out.write(jresp.toString());
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
