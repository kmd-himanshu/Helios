package com.helio.boomer.rap.engine.servicedata;

import com.helio.app.serviceapi.kpi.EnergyCostPerSquareFootKPI;
import com.helio.app.serviceapi.kpi.EnergyPerSquareFootKPI;

public class EnergyAndCostPerSqFtKPIs {
	private Long buildingId;
	private EnergyCostPerSquareFootKPI ecpsfKPI;
	private EnergyPerSquareFootKPI epsfKPI;
	
	public EnergyAndCostPerSqFtKPIs(
			Long buildingId,
			EnergyCostPerSquareFootKPI ecpsfKPI,
			EnergyPerSquareFootKPI epsfKPI) {
		this.buildingId = buildingId;
		this.ecpsfKPI = ecpsfKPI;
		this.epsfKPI = epsfKPI;
	}
	
	public Long getBuildingId() {
		return buildingId;
	}
	
	public EnergyCostPerSquareFootKPI getEnergyCostPerSquareFootKPI() {
		return ecpsfKPI;
	}
	
	public EnergyPerSquareFootKPI getEnergyPerSquareFootKPI() {
		return epsfKPI;
	}
}
