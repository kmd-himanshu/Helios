package com.helio.boomer.rap.engine.model;

import java.util.Date;
import java.util.List;

public class BusinessUnitDataStore {
	private List<Long> buildingIds = null;
	private List<Long> locationIds = null;
	private List<Period> linkedPeriodList = null;
	private Date begDate = null;
	private Date endDate = null;
	private Period onPeriod = null;
	private Period lastPeriod = null;

	/**
	 * @return the buildingIds
	 */
	public List<Long> getBuildingIds() {
		return buildingIds;
	}

	/**
	 * @param buildingIds
	 *            the buildingIds to set
	 */
	public void setBuildingIds(List<Long> buildingIds) {
		this.buildingIds = buildingIds;
	}

	/**
	 * @return the locationIds
	 */
	public List<Long> getLocationIds() {
		return locationIds;
	}

	/**
	 * @param locationIds
	 *            the locationIds to set
	 */
	public void setLocationIds(List<Long> locationIds) {
		this.locationIds = locationIds;
	}

	/**
	 * @return the linkedPeriodList
	 */
	public List<Period> getLinkedPeriodList() {
		return linkedPeriodList;
	}

	/**
	 * @param linkedPeriodList
	 *            the linkedPeriodList to set
	 */
	public void setLinkedPeriodList(List<Period> linkedPeriodList) {
		this.linkedPeriodList = linkedPeriodList;
	}

	/**
	 * @return the onPeriod
	 */
	public Period getOnPeriod() {
		return onPeriod;
	}

	/**
	 * @param onPeriod
	 *            the onPeriod to set
	 */
	public void setOnPeriod(Period onPeriod) {
		this.onPeriod = onPeriod;
	}

	/**
	 * @return the lastPeriod
	 */
	public Period getLastPeriod() {
		return lastPeriod;
	}

	/**
	 * @param lastPeriod
	 *            the lastPeriod to set
	 */
	public void setLastPeriod(Period lastPeriod) {
		this.lastPeriod = lastPeriod;
	}

	/**
	 * @return the begDate
	 */
	public Date getBegDate() {
		return begDate;
	}

	/**
	 * @param begDate
	 *            the begDate to set
	 */
	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
