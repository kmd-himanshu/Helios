package com.helio.boomer.rap.engine.servicedata;

import com.helio.app.serviceapi.kpi.EnergyCostAllocationKPI;
import com.helio.app.serviceapi.kpi.KWHUsageKPI;

public class EnergyAndCostPerBuildingKPIs {
	private Long buildingId;
	private KWHUsageKPI usageKPI;
	private EnergyCostAllocationKPI costKPI;
	
	public EnergyAndCostPerBuildingKPIs(
			Long buildingId,
			KWHUsageKPI usageKPI,
			EnergyCostAllocationKPI costKPI) {
		this.buildingId = buildingId;
		this.usageKPI = usageKPI;
		this.costKPI = costKPI;
	}
	
	public Long getBuildingId() {
		return buildingId;
	}
	
	public KWHUsageKPI getKWHUsageKPI() {
		return usageKPI;
	}
	
	public EnergyCostAllocationKPI getEnergyCostAllocationKPI() {
		return costKPI;
	}
}
