/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;

public class AuditTrailDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String auditId;
	private String userBy;
	private String actionName;
	private String actionDesc;
	private String entityId;
	private String dateTime;
	private String userData;
	private String ipAddress;

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getUserBy() {
		return userBy;
	}

	public void setUserBy(String userBy) {
		this.userBy = userBy;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
