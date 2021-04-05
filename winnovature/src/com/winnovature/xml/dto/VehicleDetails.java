/**
 * 
 */
package com.winnovature.xml.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class VehicleDetails {
	private List<Detail> detailList;
	
	public List<Detail> getDetailList() {
		return detailList;
	}
	
	@XmlElement(name="Detail")
	public void setDetailList(List<Detail> detailList) {
		this.detailList = detailList;
	}
}
