/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;


public class ReconSummaryDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String reconCycle;
	private String totalFileRecord;
	private String totalMatchRecord;
	private String totalMismatchRecord;
	private String alreadyRecon;
	private String deemedAccepted;
	private String remark;
	private String reconDate;
	
	public String getReconCycle() {
		return reconCycle;
	}
	public void setReconCycle(String reconCycle) {
		this.reconCycle = reconCycle;
	}
	public String getTotalFileRecord() {
		return totalFileRecord;
	}
	public void setTotalFileRecord(String totalFileRecord) {
		this.totalFileRecord = totalFileRecord;
	}
	public String getTotalMatchRecord() {
		return totalMatchRecord;
	}
	public void setTotalMatchRecord(String totalMatchRecord) {
		this.totalMatchRecord = totalMatchRecord;
	}
	public String getTotalMismatchRecord() {
		return totalMismatchRecord;
	}
	public void setTotalMismatchRecord(String totalMismatchRecord) {
		this.totalMismatchRecord = totalMismatchRecord;
	}
	public String getAlreadyRecon() {
		return alreadyRecon;
	}
	public void setAlreadyRecon(String alreadyRecon) {
		this.alreadyRecon = alreadyRecon;
	}
	public String getDeemedAccepted() {
		return deemedAccepted;
	}
	public void setDeemedAccepted(String deemedAccepted) {
		this.deemedAccepted = deemedAccepted;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReconDate() {
		return reconDate;
	}
	public void setReconDate(String reconDate) {
		this.reconDate = reconDate;
	}
	
	
}
