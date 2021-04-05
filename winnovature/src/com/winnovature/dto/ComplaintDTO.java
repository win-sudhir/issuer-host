package com.winnovature.dto;

import java.util.List;

public class ComplaintDTO {
	public static String COMPLAINTOPEN="OPEN";
	public static String COMPLAINTCLOSE="CLOSE";
	
	private String id;
	private String userId;
	private String userRemark;
	private String closingRemark;
	private String complaintId;
	private String complaintDesc;
	private String createdOn;
	private String createdBy;
	private String closedOn;
	private String closedBy;
	private String status;
	private List<ComplaintList> complaintList;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public String getClosingRemark() {
		return closingRemark;
	}
	public void setClosingRemark(String closingRemark) {
		this.closingRemark = closingRemark;
	}
	public String getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}
	public String getComplaintDesc() {
		return complaintDesc;
	}
	public void setComplaintDesc(String complaintDesc) {
		this.complaintDesc = complaintDesc;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getClosedOn() {
		return closedOn;
	}
	public void setClosedOn(String closedOn) {
		this.closedOn = closedOn;
	}
	public String getClosedBy() {
		return closedBy;
	}
	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

	public List<ComplaintList> getComplaintList() {
		return complaintList;
	}
	public void setComplaintList(List<ComplaintList> complaintList) {
		this.complaintList = complaintList;
	}


	public static class ComplaintList{
		private String complaintId;
		private String complaintDesc;
		public String getComplaintId() {
			return complaintId;
		}
		public void setComplaintId(String complaintId) {
			this.complaintId = complaintId;
		}
		public String getComplaintDesc() {
			return complaintDesc;
		}
		public void setComplaintDesc(String complaintDesc) {
			this.complaintDesc = complaintDesc;
		}
	}
}
