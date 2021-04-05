/**
 * 
 */
package com.winnovature.dispute.dao;

public class DisputeServiceUtil {

	public static DisputeSearchService getDisputeSearchService() {
		return DisputeSearchServiceImpl.getInstance();
	}
	public static DisputeManagementService getDisputeManagementService() {
		return DisputeManagementServiceImpl.getInstance();
	}
}
