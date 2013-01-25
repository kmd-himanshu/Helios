package com.helio.app.serviceapi.kpi;

import com.helio.app.boomer.common.dal.model.ContextHolder;

public class EnergyCostPerCasesHandledKPI extends CasesHandledKPI {

	public EnergyCostPerCasesHandledKPI(ContextHolder pContextHolder, Float pKPIValue, HelioKPI pBaseKPI) 
	throws HelioInvalidKPIException
	{
		super(KPIType.COST_PER_CASESHANDLED, pContextHolder, pKPIValue, pBaseKPI);
	}
}
