/**
 * 
 */
package com.winnovature.dto;

import java.io.Serializable;
import java.util.List;

import com.winnovature.dto.VehicleDTO.Vehicles;

public class ResponseDTO implements Serializable {
	public static String failure = "0";
	public static String success = "1";

	private static final long serialVersionUID = 1L;
	private String status;
	private String message;
	private String errorCode;
	private String fileName;
	private String flag;
	private Object customer;
	private AddressDTO address;
	private AccountDTO account;
	private KycDTO kyc;
	private UserDTO user;
	private AgentDTO agent;
	private PurchaseOrderDTO purchaseOrder;
	private BranchDTO branch;
	private ChallanDTO challan;

	private List<ReconSummaryDTO> reconSummary;
	private List<ReconDates> dateList;
	private List<ReconDates> skippedList;
	private List<ReconDates> revokedList;
	private List<Vehicles> vehicles;
	private List<String> branchList;

	/*
	 * private List<CustomerDTO> customers; public List<CustomerDTO> getCustomers()
	 * { return customers; } public void setCustomers(List<CustomerDTO> customers) {
	 * this.customers = customers; }
	 */
	/*
	 * private List<ReconSummaryDTO> reconSummary; //private List<ReconDateDTO>
	 * reconDates; private List<ReconDates> dateList; private List<ReconDates>
	 * skippedList; private List<ReconDates> revokedList;
	 */

	public List<String> getBranchList() {
		return branchList;
	}

	public void setBranchList(List<String> branchList) {
		this.branchList = branchList;
	}

	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/*
	 * public List<ReconSummaryDTO> getReconSummary() { return reconSummary; }
	 * 
	 * public void setReconSummary(List<ReconSummaryDTO> reconSummary) {
	 * this.reconSummary = reconSummary; }
	 */

	/*
	 * public List<ReconDateDTO> getReconDates() { return reconDates; }
	 * 
	 * public void setReconDates(List<ReconDateDTO> reconDates) { this.reconDates =
	 * reconDates; }
	 */

	/*
	 * public List<ReconDates> getDateList() { return dateList; }
	 * 
	 * public void setDateList(List<ReconDates> dateList) { this.dateList =
	 * dateList; } public List<ReconDates> getSkippedList() { return skippedList; }
	 * public void setSkippedList(List<ReconDates> skippedList) { this.skippedList =
	 * skippedList; } public List<ReconDates> getRevokedList() { return revokedList;
	 * } public void setRevokedList(List<ReconDates> revokedList) { this.revokedList
	 * = revokedList; }
	 */
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/*
	 * public CustomerDTO getCustomer() { return customer; } public void
	 * setCustomer(CustomerDTO customer) { this.customer = customer; }
	 */
	public Object getCustomer() {
		return customer;
	}

	public void setCustomer(Object customer) {
		this.customer = customer;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public AccountDTO getAccount() {
		return account;
	}

	public void setAccount(AccountDTO account) {
		this.account = account;
	}

	public KycDTO getKyc() {
		return kyc;
	}

	public void setKyc(KycDTO kyc) {
		this.kyc = kyc;
	}

	public List<Vehicles> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicles> vehicles) {
		this.vehicles = vehicles;
	}

	public List<ReconSummaryDTO> getReconSummary() {
		return reconSummary;
	}

	public void setReconSummary(List<ReconSummaryDTO> reconSummary) {
		this.reconSummary = reconSummary;
	}

	public List<ReconDates> getDateList() {
		return dateList;
	}

	public void setDateList(List<ReconDates> dateList) {
		this.dateList = dateList;
	}

	public List<ReconDates> getSkippedList() {
		return skippedList;
	}

	public void setSkippedList(List<ReconDates> skippedList) {
		this.skippedList = skippedList;
	}

	public List<ReconDates> getRevokedList() {
		return revokedList;
	}

	public void setRevokedList(List<ReconDates> revokedList) {
		this.revokedList = revokedList;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public AgentDTO getAgent() {
		return agent;
	}

	public void setAgent(AgentDTO agent) {
		this.agent = agent;
	}

	public PurchaseOrderDTO getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public BranchDTO getBranch() {
		return branch;
	}

	public void setBranch(BranchDTO branch) {
		this.branch = branch;
	}

	public ChallanDTO getChallan() {
		return challan;
	}

	public void setChallan(ChallanDTO challan) {
		this.challan = challan;
	}

}
