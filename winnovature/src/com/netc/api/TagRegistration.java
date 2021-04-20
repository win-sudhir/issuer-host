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
import com.winnovature.dto.TagAllocationDTO;
import com.winnovature.dto.TransactionDTO;
import com.winnovature.service.TransactionService;
import com.winnovature.service.UnRegisterTagService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/tag/registration")
public class TagRegistration extends HttpServlet {
	static Logger log = Logger.getLogger(TagRegistration.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jresp = new JSONObject();
		boolean isAllocated = false, isChallanGenerated = false;
		PrintWriter out = response.getWriter();
		TagRegistrationDAO tagallocationDao = new TagRegistrationDAO();
		StringBuffer sbuffer = new StringBuffer();
		String line = null, isCommercial = null, txnID = null, seqNO = null;
		String TID, vehicleNumber, minThreshold, amtIssuence, amtRecharge, amtRSA, amtSecurity, amtInsurance;
		JSONObject challan = new JSONObject();
		
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
			minThreshold = "0";// js.getString("ThresholdNo");
			amtIssuence = js.getString("amtIssuence");
			amtRecharge = "0";// js.getString("amtRecharge");
			amtRSA = "0";// js.getString("amtRSA");
			amtSecurity = js.getString("amtSecurity");
			amtInsurance = "0";// js.getString("amtInsurance");
			isCommercial = tagallocationDao.getIsCommercial(vehicleNumber, conn);
			log.info("TagAllocation.java ::: isCommercial  : " + isCommercial);

			log.info("TID : " + TID + " VehicleNumber : " + vehicleNumber + " Min Threshold : " + minThreshold);
			log.info("amtIssuence : " + amtIssuence + " amtRecharge : " + amtRecharge + " amtRSA : " + amtRSA
					+ "  amtSecurity : " + amtSecurity + "  amtInsurance  : " + amtInsurance);

			String userId = request.getHeader("userId").toString();
			String authToken = request.getHeader("Authorization");
			
			TagAllocationDTO tagAllocationDTO = UnRegisterTagService.isUnRegisterTag(TID, vehicleNumber, conn);
			if (tagAllocationDTO.isUnRegister()) {
				String result = UnRegisterTagService.removeFromBlackList(tagAllocationDTO.getTagId(), userId, authToken, conn);
				if(result.equalsIgnoreCase("FAILURE")) {
					log.info("Fail to remove renregistered from blacklist tag.");
					jresp.put("message", "Fail to remove renregistered from blacklist.");
					jresp.put("status", "0");
					return;
				}
				UnRegisterTagService.unRegisterTagTransaction(tagAllocationDTO, userId,authToken, conn);
			}

			if (TagRegistrationDAO.checkBalance(vehicleNumber, amtIssuence, amtSecurity, conn)) {
				log.info("Balance not sufficient.");
				jresp.put("message", "In-sufficient balance.");
				jresp.put("status", "0");
				return;
			}

			isAllocated = tagallocationDao.isAllocated(TID, vehicleNumber, minThreshold, conn);
			log.info("is allocated value == " + isAllocated);
			if (isAllocated) {
				jresp.put("message", "TID not found in the inventory.");
				jresp.put("status", "0");
				return;
			} else {

				String XMLData = new ManageTagResponse().mngTagApiCall(TID, vehicleNumber, conn);
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

						isChallanGenerated = new ManageTagResponse().allocateTagInsertChallan(TID, vehicleNumber,
								minThreshold, txnID, seqNO, amtIssuence, amtRecharge, amtRSA, amtSecurity,
								amtInsurance, conn);
						if (isChallanGenerated) {
							// using normal function
							status = "1";
							message = "Tag registered successfully in NPCI.";
							challan = TagRegistrationDAO.challanData(TID, vehicleNumber, minThreshold, conn);
							log.info("CHALLAN return challanData :: " + challan.toString());

							log.info("TID registered and allocated.................");
							// tag allocation txn-----

							TransactionDTO transactionDTO = new TransactionDTO();
							transactionDTO.setTxnAmount(amtIssuence);
							transactionDTO.setSecurityDeposit(amtSecurity);
							transactionDTO.setUserId(vehicleNumber);
							// Connection conn = DatabaseManager.getConnection();
							TransactionService.doTagTransaction(conn, transactionDTO, userId);
							//DatabaseManager.closeConnection(conn);

							// tag allocation end-----
						}

						else {
							message = "CHALLAN is not generated";
							status = "0";
							log.info("CHALLAN is not generated :: isgenerated : " + isChallanGenerated);
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

		} catch (Exception e) {
			log.error("Exception in TagAllcation ::  ", e);
		} finally {
			out.write(jresp.toString());
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
