/**
 * 
 */
package com.winnovature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.TagInfoDTO;
import com.winnovature.querries.TagQueries;
import com.winnovature.service.XMLParser;
import com.winnovature.service.XMLParserService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.validation.TagErrorCode;


public class TagDetailsDAO {
	static Logger log = Logger.getLogger(TagDetailsDAO.class.getClass());
	
	//search tag details to if any in exception(SEARCH)
	public ResponseDTO getSearchDetails(TagInfoDTO tagInfoDTO, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		List<TagInfoDTO> data = manageTagException(tagInfoDTO, conn);
		if(data.size()<=0){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0041.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0041.name());
			responseDTO.setData(data);
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(TagErrorCode.TAGBU000.getErrorMessage());
		responseDTO.setErrorCode(TagErrorCode.TAGBU000.name());
		responseDTO.setData(data);
		return responseDTO;
	}
	private List<TagInfoDTO> manageTagException(TagInfoDTO tagInfoDTO, Connection conn) {
		List<TagInfoDTO> lst = new ArrayList<TagInfoDTO>();
		
		CallableStatement cs = null;
		ResultSet rs = null;
		
		try {
				String query = TagQueries.getProcAddExcepQuery;//"{call pr_add_excep(?,?,?,?,?,?,?)}";

				cs = conn.prepareCall(query);

				cs.setString(1, tagInfoDTO.getTagId());
				cs.setString(2, tagInfoDTO.getTid());
				cs.setString(3, tagInfoDTO.getVehicleNumber());
				cs.setString(4, tagInfoDTO.getExceptionCode());
				cs.setString(5, TagInfoDTO.MANUALINSERTIONFLAG);
				cs.setString(6, tagInfoDTO.getRequestType());
				cs.registerOutParameter(7, Types.VARCHAR);

				cs.execute();

				if (tagInfoDTO.getRequestType().equalsIgnoreCase("SEARCH")) 
				{
					rs = cs.getResultSet();
					TagInfoDTO info = new TagInfoDTO();
					if (rs != null) {
						
						/*info.setId("ID");
						info.setTagId("TAGID");
						info.setTid("TID");
						info.setVehicleNumber("VEHICLENUMBER");
						info.setExceptionCode("EXCEPTIONCODE");
						info.setMessageId("MSGID");
						info.setNpciUpdatedDate("NPCIUPDATEDTIME");
						lst.add(info);*/
						while (rs.next()) {
							//exceptionData = new JSONObject();
							info = new TagInfoDTO();
							info.setId(rs.getString("id"));
							info.setTagId(rs.getString("tag_id"));//checkResultSetString(rs.getString("tag_id"))
							info.setTid(checkResultSetString(rs.getString("tid")));
							info.setVehicleNumber(checkResultSetString(rs.getString("vehicle_number")));
							info.setExceptionCode(rs.getString("exe_code"));
							info.setMessageId(rs.getString("msg_id"));
							info.setNpciUpdatedDate(rs.getString("npci_updated_date"));
							
							lst.add(info);
						}
						return lst;
					} 
					
				} 
				
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();

		} finally {
			
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeCallableStatement(cs);
		}

		return lst;
	}
			
	public JSONObject tagAddRemoveException(TagInfoDTO tagInfoDTO, Connection conn) {
		//TagInfoDTO info = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		JSONObject resp = null;
		try {
				//String query = "{call pr_add_excep(?,?,?,?,?,?,?)}";
				String query = TagQueries.getProcAddExcepQuery;
				cs = conn.prepareCall(query);

				cs.setString(1, tagInfoDTO.getTagId());
				cs.setString(2, tagInfoDTO.getTid());
				cs.setString(3, tagInfoDTO.getVehicleNumber());
				cs.setString(4, tagInfoDTO.getExceptionCode());
				cs.setString(5, TagInfoDTO.MANUALINSERTIONFLAG);
				cs.setString(6, tagInfoDTO.getRequestType());
				cs.registerOutParameter(7, Types.VARCHAR);

				cs.execute();
				
				String parsedXml = new XMLParser().createFinalResponse(cs.getString(7), "Response");
				resp = new JSONObject(parsedXml.toString());
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();

		} finally {
			
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeCallableStatement(cs);
		}

		return resp;
	}
	
	public static boolean checkTagInException(TagInfoDTO tagInfoDTO, Connection conn){
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {
			//query = "select tag_id, exe_code from tag_exception_list where tag_id=? and exe_code=?";
			query = TagQueries.getCheckTagInExceptionQuery;
			ps = conn.prepareStatement(query);
			ps.setString(1, tagInfoDTO.getTagId());
			ps.setString(2, tagInfoDTO.getExceptionCode());
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return false;
	}
	
	public ResponseDTO getAddSearchDetails(TagInfoDTO tagInfoDTO, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		List<TagInfoDTO> data = getTagIdDetails(tagInfoDTO, conn);
		if(data.size()<=0){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0041.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0041.name());
			responseDTO.setData(data);
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(TagErrorCode.TAGBU000.getErrorMessage());
		responseDTO.setErrorCode(TagErrorCode.TAGBU000.name());
		responseDTO.setData(data);
		return responseDTO;
	}
	
	private List<TagInfoDTO> getTagIdDetails(TagInfoDTO tagInfoDTO, Connection conn){
		List<TagInfoDTO> lst = new ArrayList<TagInfoDTO>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		TagInfoDTO info = null;
		try {
			//
			query = TagQueries.getTagDetailsQuery;
			ps = conn.prepareStatement(query);
			ps.setString(1, tagInfoDTO.getTagId());
			ps.setString(2, tagInfoDTO.getTid());
			ps.setString(3, tagInfoDTO.getVehicleNumber());
			rs = ps.executeQuery();
			/*info = new TagInfoDTO();
			info.setTagId("TAGID");
			info.setTid("TID");
			info.setVehicleNumber("VEHICLENUMBER");
			info.setExceptionCode("EXCEPTIONCODE");
			lst.add(info);*/
			//info.setTagClassId("TAGCLASSID");
			if (rs.next()) {
				info = new TagInfoDTO();
				info.setTagId(rs.getString("tag_id"));
				info.setTid(rs.getString("tid"));
				info.setVehicleNumber(checkResultSetString(rs.getString("vehicle_number")));
				info.setExceptionCode(tagInfoDTO.getExceptionCode());
				
				//info.setTagClassId(rs.getString("tag_class_id"));
				lst.add(info);
			}
			return lst;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return lst;
	}
	
	private static String checkResultSetString(String rs1)
	{
		if(rs1!=null)
			return rs1;
		return "NA";
	}
	// LBHandler uses this method
	public JSONObject manageTagException(String tagid, String tid, String vno, String exce_type, String insertionFlag,
			String operation, Connection conn) {
		JSONObject resp = null;
		JSONArray exceptionDataArray = null;
		JSONObject exceptionData = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		
		log.info("ExceptionDetailsDaoImpl.java :: manageTagException() :: Call procedure :: pr_add_excep ::  operation :: "+operation+" , tagid : "+tagid+" , exce_type : "+exce_type);
		try {
			
			if (conn != null)
			{
				//conn.setAutoCommit(false);
				//String query = "{call pr_add_excep(?,?,?,?,?,?,?)}";
				String query = TagQueries.getProcAddExcepQuery;
				callableStatement = conn.prepareCall(query);

				callableStatement.setString(1, tagid);
				callableStatement.setString(2, tid);
				callableStatement.setString(3, vno);
				callableStatement.setString(4, exce_type);
				callableStatement.setString(5, insertionFlag);
				callableStatement.setString(6, operation);
				callableStatement.registerOutParameter(7, Types.VARCHAR);

				callableStatement.execute();

				if (operation.equalsIgnoreCase("SEARCH")) 
				{
					rs = callableStatement.getResultSet();

					if (rs != null) {
						resp = new JSONObject();

						resp.put("operation", operation);
						resp.put("resp_msg", "Data Found.");
						resp.put("status", 1);

						exceptionDataArray = new JSONArray();

						while (rs.next()) {
							exceptionData = new JSONObject();

							exceptionData.put("id", rs.getString("id"));
							exceptionData.put("tag_id", rs.getString("tag_id"));
							//new added as pallav said 12-03-2020
							exceptionData.put("tid", rs.getString("tid"));
							exceptionData.put("vehicle_number", rs.getString("vehicle_number"));
							
							exceptionData.put("exe_code", rs.getString("exe_code"));
							exceptionData.put("insertion_flag", rs.getString("insertion_flag"));
							exceptionData.put("msg_id", rs.getString("msg_id"));
							exceptionData.put("npci_updated_date", rs.getString("npci_updated_date"));

							exceptionDataArray.put(exceptionData);
						}

						resp.put("exceptionDataArray", exceptionDataArray);
					} 
					else 
					{
						resp = new JSONObject();
						resp.put("operation", operation);
						resp.put("status", 2);
						resp.put("resp_msg","No Data Found for the Entered Tag Id/TID/Vehicle Number and Exception Code "+ exce_type);
					}
				} 
				else 
				{
					//conn.commit();
					String parsedXml = new XMLParser().createFinalResponse(callableStatement.getString(7), "Response");
					resp = new JSONObject(parsedXml.toString());
				}
			}
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();
			resp = new JSONObject();
			try 
			{
				resp.put("operation", operation);
				resp.put("status", -1);
				resp.put("resp_msg", e.getMessage());
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}

		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeCallableStatement(callableStatement);
		}

		return resp;
	}


/*
	//OLD WAY PARSING
	public ResponseDTO addTagInException(TagInfoDTO tagInfoDTO, Connection conn) {
		ResponseDTO responseDTO = new ResponseDTO();
		//responseDTO = new TagValidation().validateSearchRequest(tagInfoDTO);
		if (tagInfoDTO.getTagId() == null || tagInfoDTO.getTagId().isEmpty() || tagInfoDTO.getExceptionCode() == null
				|| tagInfoDTO.getExceptionCode().isEmpty()) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0021.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0021.name());
			return responseDTO;
		}
		
		if (TagDetailsDAO.checkTagInException(tagInfoDTO, conn)) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0025.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0025.name());
			return responseDTO;
		}
		
		String respXML = new NPCICallService().callNPCI(tagInfoDTO); 
		if (respXML == null || respXML.isEmpty()) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0034.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0034.name());
			return responseDTO;
		}
		responseDTO = NPCICallService.parseResponse(respXML, tagInfoDTO, conn);
		
		if(responseDTO.getStatus().equals(ResponseDTO.success)){
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(responseDTO.getMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0031.name());
			return responseDTO;
		}
		
		return responseDTO;
	}
*/

	//New WAY PARSING
	public ResponseDTO addTagInException(TagInfoDTO tagInfoDTO, Connection conn) {
		ResponseDTO responseDTO = new ResponseDTO();
		//responseDTO = new TagValidation().validateSearchRequest(tagInfoDTO);
		if (tagInfoDTO.getTagId() == null || tagInfoDTO.getTagId().isEmpty() || tagInfoDTO.getExceptionCode() == null
				|| tagInfoDTO.getExceptionCode().isEmpty()) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0021.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0021.name());
			return responseDTO;
		}
		
		if (TagDetailsDAO.checkTagInException(tagInfoDTO, conn)) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0025.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0025.name());
			return responseDTO;
		}
		
		responseDTO = new com.winnovature.service.XMLParserService().parseRespMngException(tagInfoDTO);
		
		if(responseDTO.getStatus().equals(ResponseDTO.success)){
			JSONObject result = new TagDetailsDAO().tagAddRemoveException(tagInfoDTO, conn);
			if (result.getInt("status") != -1 && result.getInt("status") != 2) {
				responseDTO.setStatus(ResponseDTO.success);
				responseDTO.setMessage(result.getString("resp_msg"));
				responseDTO.setErrorCode(TagErrorCode.TAGBU0031.name());
				return responseDTO;
			}
		}
		
		return responseDTO;
	}

/*
	//old Way Parsing REMOVE EXCEPTION
	public ResponseDTO removeTagFromException(TagInfoDTO tagInfoDTO, Connection conn) {
		ResponseDTO responseDTO = new ResponseDTO();
		//responseDTO = new TagValidation().validateAddSearchRequest(tagInfoDTO, conn);
		if (responseDTO.getStatus().equals(ResponseDTO.failure)) {
			return responseDTO;
		}
		if (tagInfoDTO.getTagId() == null || tagInfoDTO.getTagId().isEmpty() || tagInfoDTO.getExceptionCode() == null
				|| tagInfoDTO.getExceptionCode().isEmpty()) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0021.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0021.name());
			return responseDTO;
		}
		
		String respXML = new NPCICallService().callNPCI(tagInfoDTO); 
		if (respXML == null || respXML.isEmpty()) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0034.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0034.name());
			return responseDTO;
		}
		responseDTO = NPCICallService.parseResponse(respXML, tagInfoDTO, conn);
		
		if(responseDTO.getStatus().equals(ResponseDTO.success)){
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(responseDTO.getMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0032.name());
			return responseDTO;
		}
		
		return responseDTO;
	}
*/	
	
	//NEW Way 
	public ResponseDTO removeTagFromException(TagInfoDTO tagInfoDTO, Connection conn) {
		ResponseDTO responseDTO = new ResponseDTO();
		
		if (tagInfoDTO.getTagId() == null || tagInfoDTO.getTagId().isEmpty() || tagInfoDTO.getExceptionCode() == null
				|| tagInfoDTO.getExceptionCode().isEmpty()) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(TagErrorCode.TAGBU0021.getErrorMessage());
			responseDTO.setErrorCode(TagErrorCode.TAGBU0021.name());
			return responseDTO;
		}
		
		responseDTO = new XMLParserService().parseRespMngException(tagInfoDTO);
		
		if(responseDTO.getStatus().equals(ResponseDTO.success)){
			JSONObject result = new TagDetailsDAO().tagAddRemoveException(tagInfoDTO, conn);
			if (result.getInt("status") != -1 && result.getInt("status") != 2) {
				
			boolean checkcusttaginfo=checkCustTagInfo(tagInfoDTO.getTagId());
				//if action1 !=null && actiona1.equalsignorecase("SUSPEND") && action2=NS && sction3=NA then action1=na
				if(checkcusttaginfo)
				{
					updateCustTagInfo(tagInfoDTO.getTagId());
				}
				responseDTO.setStatus(ResponseDTO.success);
				responseDTO.setMessage(result.getString("resp_msg"));
				responseDTO.setErrorCode(TagErrorCode.TAGBU0032.name());
				return responseDTO;
				
			}
		}
		
		
		return responseDTO;
	}

	private void updateCustTagInfo(String tagId) {
		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		try {
			 conn = DatabaseManager.getConnection();
			query = TagQueries.getUpdateCustTagInfoQuery;
			ps = conn.prepareStatement(query);
			ps.setString(1, tagId);
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeConnection(conn);
		}
		
		
	}
	private boolean checkCustTagInfo(String tagId) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn =null;
		try {
			 conn = DatabaseManager.getConnection();
			query = TagQueries.getCheckCustTagInfo;
			ps = conn.prepareStatement(query);
			ps.setString(1, tagId);
			rs = ps.executeQuery();
			if (rs.next()) {

				if (rs.getString("action1") != null && rs.getString("action1").equalsIgnoreCase("SUSPEND")
						&& rs.getString("action2").equalsIgnoreCase("NA")
						&& rs.getString("action3").equalsIgnoreCase("NA")) {
					return true;
				}

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
	public String getTagId(TagInfoDTO tagInfoDTO, Connection conn) {
		String tagId = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;

		try {
				if (tagInfoDTO.getId() != null && !tagInfoDTO.getId().isEmpty() && !tagInfoDTO.getId().equalsIgnoreCase("NA")) {
					//public static String getTagIdByTidQuery = "select tag_id from vehicle_tag_linking where tid = ?";
					query = TagQueries.getTagIdByTidQuery;
					ps = conn.prepareStatement(query);
					ps.setString(1, tagInfoDTO.getId());
					rs = ps.executeQuery();
				} else if (tagInfoDTO.getVehicleNumber() != null && !tagInfoDTO.getVehicleNumber().isEmpty() && !tagInfoDTO.getVehicleNumber().equalsIgnoreCase("NA")) {
					//public static String getTagIdByVehicleNoQuery =  "select tag_id from vehicle_tag_linking where vehicle_number = ?";
					query = TagQueries.getTagIdByVehicleNoQuery;
					ps = conn.prepareStatement(query);
					ps.setString(1, tagInfoDTO.getVehicleNumber());
					rs = ps.executeQuery();
				} else {
					log.info("ExceptionDetailDaoImpl.java ::: getTagId() :: Invalid Input Received");
					return null;
				}

				if (rs != null && rs.next()) {
					tagId = rs.getString("tag_id");
					return tagId;
				}

				log.info("getTagId() :: Query : '" + query + "' And Tag Id : '" + tagId+ "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}

		return tagId;
	}

}
