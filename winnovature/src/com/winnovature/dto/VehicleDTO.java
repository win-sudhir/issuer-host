package com.winnovature.dto;

import java.io.Serializable;
import java.util.List;

public class VehicleDTO {
	private List<Vehicles> vehicles;
	
	public List<Vehicles> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicles> vehicles) {
		this.vehicles = vehicles;
	}

	public static class Vehicles implements Serializable {
		private static final long serialVersionUID = 1L;
		private String vehicleId;
		private String vehicleNumber;
		private String userId;
		private String vehicleClassId;
		private String tagClassId;
		private String engineNumber;
		private String chassisNumber;
		private String isCommercial;
		private String createdOn;
		private String pathRcBook;
		private String pathInsurance;
		private String pathFrontPic;
		private String pathBackPic;
		
		private String rcBookDoc;
		private String insuranceDoc;
		private String frontPicDoc;
		private String backPicDoc;
		
		private String otherDoc1;
		private String otherDoc2;
		private String otherDoc3;
		private String status;
		
		public Vehicles() {}
		public Vehicles(String vehicleId, String vehicleNumber, String userId, String vehicleClassId, String tagClassId,
				String engineNumber, String chassisNumber, String isCommercial, String createdOn, String pathRcBook,
				String pathInsurance, String pathFrontPic, String pathBackPic, String rcBookDoc, String insuranceDoc,
				String frontPicDoc, String backPicDoc, String otherDoc1, String otherDoc2, String otherDoc3,
				String status) {
			super();
			this.vehicleId = vehicleId;
			this.vehicleNumber = vehicleNumber;
			this.userId = userId;
			this.vehicleClassId = vehicleClassId;
			this.tagClassId = tagClassId;
			this.engineNumber = engineNumber;
			this.chassisNumber = chassisNumber;
			this.isCommercial = isCommercial;
			this.createdOn = createdOn;
			this.pathRcBook = pathRcBook;
			this.pathInsurance = pathInsurance;
			this.pathFrontPic = pathFrontPic;
			this.pathBackPic = pathBackPic;
			this.rcBookDoc = rcBookDoc;
			this.insuranceDoc = insuranceDoc;
			this.frontPicDoc = frontPicDoc;
			this.backPicDoc = backPicDoc;
			this.otherDoc1 = otherDoc1;
			this.otherDoc2 = otherDoc2;
			this.otherDoc3 = otherDoc3;
			this.status = status;
		}

		public String getVehicleId() {
			return vehicleId;
		}

		public void setVehicleId(String vehicleId) {
			this.vehicleId = vehicleId;
		}

		public String getVehicleNumber() {
			return vehicleNumber;
		}

		public void setVehicleNumber(String vehicleNumber) {
			this.vehicleNumber = vehicleNumber;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getVehicleClassId() {
			return vehicleClassId;
		}

		public void setVehicleClassId(String vehicleClassId) {
			this.vehicleClassId = vehicleClassId;
		}

		public String getTagClassId() {
			return tagClassId;
		}

		public void setTagClassId(String tagClassId) {
			this.tagClassId = tagClassId;
		}

		public String getEngineNumber() {
			return engineNumber;
		}

		public void setEngineNumber(String engineNumber) {
			this.engineNumber = engineNumber;
		}

		public String getChassisNumber() {
			return chassisNumber;
		}

		public void setChassisNumber(String chassisNumber) {
			this.chassisNumber = chassisNumber;
		}

		public String getIsCommercial() {
			return isCommercial;
		}

		public void setIsCommercial(String isCommercial) {
			this.isCommercial = isCommercial;
		}

		public String getCreatedOn() {
			return createdOn;
		}

		public void setCreatedOn(String createdOn) {
			this.createdOn = createdOn;
		}

		public String getPathRcBook() {
			return pathRcBook;
		}

		public void setPathRcBook(String pathRcBook) {
			this.pathRcBook = pathRcBook;
		}

		public String getPathInsurance() {
			return pathInsurance;
		}

		public void setPathInsurance(String pathInsurance) {
			this.pathInsurance = pathInsurance;
		}

		public String getPathFrontPic() {
			return pathFrontPic;
		}

		public void setPathFrontPic(String pathFrontPic) {
			this.pathFrontPic = pathFrontPic;
		}

		public String getPathBackPic() {
			return pathBackPic;
		}

		public void setPathBackPic(String pathBackPic) {
			this.pathBackPic = pathBackPic;
		}

		public String getOtherDoc1() {
			return otherDoc1;
		}

		public void setOtherDoc1(String otherDoc1) {
			this.otherDoc1 = otherDoc1;
		}

		public String getOtherDoc2() {
			return otherDoc2;
		}

		public void setOtherDoc2(String otherDoc2) {
			this.otherDoc2 = otherDoc2;
		}

		public String getOtherDoc3() {
			return otherDoc3;
		}

		public void setOtherDoc3(String otherDoc3) {
			this.otherDoc3 = otherDoc3;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
		public String getRcBookDoc() {
			return rcBookDoc;
		}

		public void setRcBookDoc(String rcBookDoc) {
			this.rcBookDoc = rcBookDoc;
		}

		public String getInsuranceDoc() {
			return insuranceDoc;
		}

		public void setInsuranceDoc(String insuranceDoc) {
			this.insuranceDoc = insuranceDoc;
		}

		public String getFrontPicDoc() {
			return frontPicDoc;
		}

		public void setFrontPicDoc(String frontPicDoc) {
			this.frontPicDoc = frontPicDoc;
		}

		public String getBackPicDoc() {
			return backPicDoc;
		}

		public void setBackPicDoc(String backPicDoc) {
			this.backPicDoc = backPicDoc;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			
			builder.append("Vehicle [vehicleId=");
			builder.append(vehicleId);
			builder.append(", vehicleNumber=");
			builder.append(vehicleNumber);
			builder.append(", userId=");
			builder.append(userId);
			builder.append(", vehicleClassId=");
			builder.append(vehicleClassId);
			builder.append(", tagClassId=");
			builder.append(tagClassId);
			builder.append(", engineNumber=");
			builder.append(engineNumber);
			builder.append(", chassisNumber=");
			builder.append(chassisNumber);
			builder.append(", isCommercial=");
			builder.append(isCommercial);
			builder.append(", createdOn=");
			builder.append(createdOn);
			builder.append(", pathRcBook=");
			builder.append(pathRcBook);
			builder.append(", pathInsurance=");
			builder.append(pathInsurance);
			builder.append(", pathFrontPic=");
			builder.append(pathFrontPic);
			builder.append(", pathBackPic=");
			builder.append(pathBackPic);
			builder.append(", rcBookDoc=");
			builder.append(rcBookDoc);
			builder.append(", insuranceDoc=");
			builder.append(insuranceDoc);
			builder.append(", frontPicDoc=");
			builder.append(frontPicDoc);
			builder.append(", backPicDoc=");
			builder.append(backPicDoc);
			builder.append("]");
			return builder.toString();
		}
	}

}
