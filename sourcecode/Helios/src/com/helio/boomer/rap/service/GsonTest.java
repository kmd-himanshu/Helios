package com.helio.boomer.rap.service;

import java.sql.Date;

import com.helio.app.serviceapi.kpi.HelioKPI;

public class GsonTest {

	public static void main(String[] args) {
		HelioKPI kpi;
		try {
			kpi = ServiceHandler.getInstance().getLocationKwhDateRange(
					1803l,
					Date.valueOf("2011-10-01"),
					Date.valueOf("2011-10-05"));
			System.out.println("KPIType--> " + kpi.getKpiType().toString());
			System.out.println("ContextHolder: MonitorId--> " + kpi.getContext().getMonitorId());
			System.out.println("ContextHolder: BuildingAllocationId--> " + kpi.getContext().getBuildingAllocationId());
			System.out.println("ContextHolder: BuildingId--> " + kpi.getContext().getBuildingId());
			System.out.println("ContextHolder: LocationId--> " + kpi.getContext().getLocationId());
			System.out.println("ContextHolder: DivisionId--> " + kpi.getContext().getDivisionId());
			System.out.println("ContextHolder: ContextTypeEnum--> " + kpi.getContext().getType());
			System.out.println("KPI Value--> " + kpi.getKpiValue().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
