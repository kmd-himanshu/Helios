package com.helio.boomer.rap.session;

public class SessionDataStore {
	
	private BusinessUnitDataStore businessDataStore = null;
	private DistributionUnitDataStore distributionDataStore = null;	
	private String lastPerspective = null;
	private boolean firstScreenBusiness = false;	
	private boolean firstScreenDistribution=false;
	private String username = null;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isFirstScreenBusiness() {
		return firstScreenBusiness;
	}
	public void setFirstScreenBusiness(boolean firstScreenBusiness) {
		this.firstScreenBusiness = firstScreenBusiness;
	}
	public boolean isFirstScreenDistribution() {
		return firstScreenDistribution;
	}
	public void setFirstScreenDistribution(boolean firstScreenDistribution) {
		this.firstScreenDistribution = firstScreenDistribution;
	}
	
	public BusinessUnitDataStore getBusinessDataStore() {
		return businessDataStore;
	}
	public void setBusinessDataStore(BusinessUnitDataStore businessDataStore) {
		this.businessDataStore = businessDataStore;
	}
	public DistributionUnitDataStore getDistributionDataStore() {
		return distributionDataStore;
	}
	public void setDistributionDataStore(
			DistributionUnitDataStore distributionDataStore) {
		this.distributionDataStore = distributionDataStore;
	}
	public String getLastPerspective() {
		return lastPerspective;
	}
	public void setLastPerspective(String lastPerspective) {
		this.lastPerspective = lastPerspective;
	}


}
