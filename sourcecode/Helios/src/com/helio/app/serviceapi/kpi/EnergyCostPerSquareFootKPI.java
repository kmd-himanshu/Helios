package com.helio.app.serviceapi.kpi;

import com.helio.app.boomer.common.dal.model.ContextHolder;

public class EnergyCostPerSquareFootKPI extends SquareFeetKPI {

	public EnergyCostPerSquareFootKPI(ContextHolder pContextHolder, Float pKPIValue, HelioKPI pBaseKPI) 
	throws HelioInvalidKPIException
	{
		super(KPIType.COST_OF_ENERGY_PER_SQFT, pContextHolder, pKPIValue, pBaseKPI);
	}
}
