package com.helio.app.boomer.common.dal.model;

/**
 * @author rickschwartz
 *
 *	This is a helper class to provide context for a KPI. 
 *  Zero value ids indicate that the context is at a higher level of specificity.  
 *
 */
public class ContextHolder {

	
	private Long monitorId;
	private Long buildingAllocationId;
	private Long buildingId;
	private Long locationId;
	private Long divisionId;
	private ContextTypeEnum type;


	/*
	 * prevent other uses of this object
	 */
	@SuppressWarnings("unused")
	private ContextHolder() {
		super();
	}

	
	public ContextHolder(long monitorId, long buildingAllocationId, long buildingId, long locationId, long divisionId) 
		throws ContextInvalidException
	{
		super();
		this.monitorId = monitorId;
		this.buildingAllocationId = buildingAllocationId;
		this.buildingId = buildingId;
		this.locationId = locationId;
		this.divisionId = divisionId;
		
		if (this.monitorId > 0) {
			this.type = ContextTypeEnum.MONITOR;
		} 
		else if (this.buildingAllocationId > 0) {
			this.type = ContextTypeEnum.ALLOCATION;
		} 
		else if (this.buildingId > 0) {
			this.type = ContextTypeEnum.BUILDING;
		} 
		else if (this.locationId > 0) {
			this.type = ContextTypeEnum.LOCATION;
		} 
		else if (this.divisionId > 0) {
			this.type = ContextTypeEnum.DIVISION;
		} 
		else {
			throw new ContextInvalidException("No IDs indicated - the conext is not valid");
		} 
	}


	public Long getMonitorId() {
		return monitorId;
	}
	public Long getBuildingAllocationId() {
		return buildingAllocationId;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public Long getDivisionId() {
		return divisionId;
	}
	public ContextTypeEnum getType() {
		return type;
	}
	public boolean isMonitorContext() {
		return (type == ContextTypeEnum.MONITOR);
	}
	public boolean isAllocationContext() {
		return (type == ContextTypeEnum.ALLOCATION);
	}
	public boolean isBuildingContext() {
		return (type == ContextTypeEnum.BUILDING);
	}
	public boolean isLocationContext() {
		return (type == ContextTypeEnum.LOCATION);
	}
	public boolean isDivisionContext() {
		return (type == ContextTypeEnum.DIVISION);
	}
	
	
	
	
}
