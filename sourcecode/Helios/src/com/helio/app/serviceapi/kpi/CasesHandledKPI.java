package com.helio.app.serviceapi.kpi;

import com.helio.app.boomer.common.dal.model.ContextHolder;

public class CasesHandledKPI extends UsageKPI {
	/**
	 * Creates an KPI with value.
	 * @param {@link KpiType} indicating the KPI type.
	 * @param Float indicating the observed KPI value.
	 */
	CasesHandledKPI(KPIType pKPIType, ContextHolder pContextHolder, Float pKPIValue, HelioKPI pBaseKPI) 
		throws HelioInvalidKPIException
	{
		super(pKPIType, pContextHolder, pKPIValue);
		if (pBaseKPI == null) {
			throw new HelioInvalidKPIException("Base KPI must not be null");
		}
		this.baseKPI = pBaseKPI;
	}
	public HelioKPI getBaseKPI() {
		return baseKPI;
	}
	
}
