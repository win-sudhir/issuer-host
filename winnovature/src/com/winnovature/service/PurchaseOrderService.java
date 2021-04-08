package com.winnovature.service;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.winnovature.dao.PurchaseOrderDao;
import com.winnovature.dto.PurchaseOrderDTO;
import com.winnovature.dto.ResponseDTO;

public class PurchaseOrderService {
	static Logger log = Logger.getLogger(PurchaseOrderService.class.getName());
	public static ResponseDTO getOnePurchaseOrder(String poId, Connection conn){
		ResponseDTO responseDTO = new ResponseDTO();
		PurchaseOrderDTO purchaseOrder = PurchaseOrderDao.getOnePurchaseOrder(conn, poId);
		if (purchaseOrder==null) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage("Purchase order not found or not in initial state.");
			responseDTO.setErrorCode("WINPOBU0002");
			return responseDTO;
		}
		List<PurchaseOrderDTO> data = PurchaseOrderDao.getOrderList(conn, poId);
		responseDTO.setPurchaseOrder(purchaseOrder);
		responseDTO.setData(data);
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage("SUCCESS.");
		responseDTO.setErrorCode("WINPOBU0000");
		return responseDTO;
		
	}
	public static ResponseDTO updatePurchaseOrder(Connection conn, PurchaseOrderDTO purchaseOrder, JSONArray orderList,
			String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO = PurchaseOrderDao.updatePurchaseOrder(conn, purchaseOrder, orderList, userId);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)){
			responseDTO.setMessage("Something went wrong while updating purchase order");
			responseDTO.setErrorCode("WINPOBU0003");
			return responseDTO;
		}
		responseDTO.setMessage("Purchase Order updated successfully.");
		responseDTO.setErrorCode("WINPOBU0000");
		return responseDTO;
	}
}
