package com.helio.app.serviceapi.kpi;

import com.helio.app.boomer.common.dal.model.ContextHolder;

public class EnergyPerSquareFootKPI extends SquareFeetKPI {

	public EnergyPerSquareFootKPI(ContextHolder pContextHolder, Float pKPIValue, HelioKPI pBaseKPI) 
	throws HelioInvalidKPIException
	{
		super(KPIType.ENERGY_PER_SQFT, pContextHolder, pKPIValue, pBaseKPI);
	}
}
