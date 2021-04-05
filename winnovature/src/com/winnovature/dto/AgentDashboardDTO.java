package com.winnovature.dto;

public class AgentDashboardDTO {

	private int activeVehicle = 0;
	private int totalCustomers = 0;
	private int availableTags = 0;
	private int allocatedTags = 0;

	public int getActiveVehicle() {
		return activeVehicle;
	}

	public void setActiveVehicle(int activeVehicle) {
		this.activeVehicle = activeVehicle;
	}

	public int getTotalCustomers() {
		return totalCustomers;
	}

	public void setTotalCustomers(int totalCustomers) {
		this.totalCustomers = totalCustomers;
	}

	public int getAvailableTags() {
		return availableTags;
	}

	public void setAvailableTags(int availableTags) {
		this.availableTags = availableTags;
	}

	public int getAllocatedTags() {
		return allocatedTags;
	}

	public void setAllocatedTags(int allocatedTags) {
		this.allocatedTags = allocatedTags;
	}

	@Override
	public String toString() {
		return "AgentDashboardDTO [activeVehicle=" + activeVehicle + ", totalCustomers=" + totalCustomers
				+ ", availableTags=" + availableTags + ", allocatedTags=" + allocatedTags + "]";
	}

}
