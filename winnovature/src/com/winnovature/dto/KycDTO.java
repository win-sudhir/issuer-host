package com.winnovature.dto;

public class KycDTO {
	
	private String userId;
	private String addressProofId;
	private String addressProofNo;
	private String addressProofDocPath;
	private String idProof;
	private String idProofNo;
	private String idProofDocPath;
	private String addressProofDoc;
	private String idProofDoc;
	public KycDTO() {}
	public KycDTO(String userId, String addressProofId, String addressProofNo, String addressProofDocPath,
			String idProof, String idProofNo, String idProofDocPath, String addressProofDoc, String idProofDoc) {
		super();
		this.userId = userId;
		this.addressProofId = addressProofId;
		this.addressProofNo = addressProofNo;
		this.addressProofDocPath = addressProofDocPath;
		this.idProof = idProof;
		this.idProofNo = idProofNo;
		this.idProofDocPath = idProofDocPath;
		this.addressProofDoc = addressProofDoc;
		this.idProofDoc = idProofDoc;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAddressProofId() {
		return addressProofId;
	}
	public void setAddressProofId(String addressProofId) {
		this.addressProofId = addressProofId;
	}
	public String getAddressProofNo() {
		return addressProofNo;
	}
	public void setAddressProofNo(String addressProofNo) {
		this.addressProofNo = addressProofNo;
	}
	public String getAddressProofDocPath() {
		return addressProofDocPath;
	}
	public void setAddressProofDocPath(String addressProofDocPath) {
		this.addressProofDocPath = addressProofDocPath;
	}
	public String getIdProof() {
		return idProof;
	}
	public void setIdProof(String idProof) {
		this.idProof = idProof;
	}
	public String getIdProofNo() {
		return idProofNo;
	}
	public void setIdProofNo(String idProofNo) {
		this.idProofNo = idProofNo;
	}
	public String getIdProofDocPath() {
		return idProofDocPath;
	}
	public void setIdProofDocPath(String idProofDocPath) {
		this.idProofDocPath = idProofDocPath;
	}
	public String getAddressProofDoc() {
		return addressProofDoc;
	}
	public void setAddressProofDoc(String addressProofDoc) {
		this.addressProofDoc = addressProofDoc;
	}
	public String getIdProofDoc() {
		return idProofDoc;
	}
	public void setIdProofDoc(String idProofDoc) {
		this.idProofDoc = idProofDoc;
	}
	
	
}
