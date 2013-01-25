package com.helio.app.serviceapi.kpi;

import com.helio.app.boomer.common.dal.model.ContextHolder;
import com.helio.app.serviceapi.HelioInvalidPacketException;

public class UsageKPI extends HelioKPI {

	UsageKPI(KPIType pKPIType, ContextHolder pContextHolder, Float pKPIValue ) throws HelioInvalidKPIException 
	{
		super(pKPIType, pContextHolder, pKPIValue);
	}
	/**
	 * Gets the demand for this KPI
	 */
	public Float getDemand() throws HelioInvalidKPIException, HelioInvalidPacketException
	{
		return super.getDemand();
	}
}
