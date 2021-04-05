/**
 * 
 */
package com.winnovature.validation;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDetailsDTO;

/**
 * @author Sudhir Khobragade
 *
 * 18-Feb-2021
 */
public class UpdateVehicleValidation {
	static Logger log = Logger.getLogger(UpdateVehicleValidation.class.getName());
	
	public static ResponseDTO velidateVehicleInfo(VehicleDetailsDTO vehicleDetailsDTO, Connection conn, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (vehicleDetailsDTO.getVehicleNumber() == null || vehicleDetailsDTO.getVehicleNumber().isEmpty() 
				|| vehicleDetailsDTO.getVehicleId() == null || vehicleDetailsDTO.getVehicleId().isEmpty()
				|| vehicleDetailsDTO.getOldVehicleNumber() == null || vehicleDetailsDTO.getOldVehicleNumber().isEmpty()
				|| vehicleDetailsDTO.getTagId() == null || vehicleDetailsDTO.getTagId().isEmpty()
				
				|| vehicleDetailsDTO.getTid() == null || vehicleDetailsDTO.getTid().isEmpty()
				|| vehicleDetailsDTO.getTagClassId()== null || vehicleDetailsDTO.getTagClassId().isEmpty()
				|| vehicleDetailsDTO.getVehicleClassId() == null || vehicleDetailsDTO.getVehicleClassId().isEmpty()
				
				|| userId == null || userId.isEmpty()
				) 
		{
			responseDTO.setStatus(ResponseDTO.failure);
			return responseDTO;
		}	
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
}
