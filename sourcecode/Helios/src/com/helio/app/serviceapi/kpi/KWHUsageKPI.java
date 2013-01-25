package com.helio.app.serviceapi.kpi;

import java.sql.Timestamp;

import com.helio.app.boomer.common.dal.model.ContextHolder;


public final class KWHUsageKPI extends UsageKPI {
	/**
	 * Creates an kWh Usage KPI. Since this is a base KPI it has no base. 
	 * @param pContextHolder the level in the hierarchy this pertains to
	 * @param pKPIValue the value of kWh
	 * @param pBegTime beginning time of observation
	 * @param pObservationTime end time of observation
	 */
	public KWHUsageKPI(ContextHolder pContextHolder, Float pKPIValue, Timestamp pBegTime, Timestamp pObservationTime) 
	throws HelioInvalidKPIException
	{
		super(KPIType.KWH, pContextHolder, pKPIValue);
		this.begTime = pBegTime;
		this.observationTime = pObservationTime;
	}
	
	public Timestamp getBegTime() {
		return this.begTime;
	}
	public Timestamp getEndTime() {
		return this.observationTime;
	}
}
