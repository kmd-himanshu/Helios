package com.helio.app.serviceapi.kpi;

import com.helio.app.boomer.common.dal.model.ContextHolder;

public class EnergyCostAllocationKPI extends UsageKPI {

	public EnergyCostAllocationKPI(ContextHolder pContextHolder, Float pKPIValue, HelioKPI pBaseKPI) 
	throws HelioInvalidKPIException
	{
		super(KPIType.COST_OF_ENERGY, pContextHolder, pKPIValue);
		if (pBaseKPI == null) {
			throw new HelioInvalidKPIException("Base KPI must not be null");
		}
		this.baseKPI = pBaseKPI;
	}
	public HelioKPI getBaseKPI() {
		return baseKPI;
	}
}
