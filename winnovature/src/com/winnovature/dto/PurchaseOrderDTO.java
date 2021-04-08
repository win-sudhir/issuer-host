package com.winnovature.dto;

public class PurchaseOrderDTO {
	private String id;
	private String poId;
	private String poDate;
	private String supplierId;
	private String sGst;
	private String cGst;
	private String orderValue;
	private String totalOrderValue;
	private String tagClassId;
	private String orderQty;
	private String unitPrice;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPoId() {
		return poId;
	}
	public void setPoId(String poId) {
		this.poId = poId;
	}
	public String getPoDate() {
		return poDate;
	}
	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getsGst() {
		return sGst;
	}
	public void setsGst(String sGst) {
		this.sGst = sGst;
	}
	public String getcGst() {
		return cGst;
	}
	public void setcGst(String cGst) {
		this.cGst = cGst;
	}
	public String getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}
	public String getTotalOrderValue() {
		return totalOrderValue;
	}
	public void setTotalOrderValue(String totalOrderValue) {
		this.totalOrderValue = totalOrderValue;
	}
	public String getTagClassId() {
		return tagClassId;
	}
	public void setTagClassId(String tagClassId) {
		this.tagClassId = tagClassId;
	}
	public String getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	
}
