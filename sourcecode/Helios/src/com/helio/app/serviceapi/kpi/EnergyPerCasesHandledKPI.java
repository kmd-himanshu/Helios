package com.helio.app.serviceapi.kpi;

import com.helio.app.boomer.common.dal.model.ContextHolder;

public class EnergyPerCasesHandledKPI extends CasesHandledKPI {

	public EnergyPerCasesHandledKPI(ContextHolder pContextHolder, Float pKPIValue, HelioKPI pBaseKPI) 
	throws HelioInvalidKPIException
	{
		super(KPIType.ENERGY_PER_CASESHANDLED, pContextHolder, pKPIValue, pBaseKPI);
	}
}
