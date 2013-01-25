package com.helio.boomer.rap.engine.servicedata;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.helio.app.serviceapi.CoreServicesImpl;
import com.helio.app.serviceapi.kpi.HelioKPI;
import com.helio.app.serviceapi.kpi.StandardDeviationKPI;
import com.helio.boomer.rap.engine.LocationListController;
import com.helio.boomer.rap.engine.model.Location;

public class EnterpriseReportDAO {

	public static Double[] getCasesHandledKWhForLocation(Long locationId, Date begDate, Date endDate) throws Exception {
		Double[] returnArray = new Double[2];
		long offset = endDate.getTime() - begDate.getTime();
		HelioKPI kpi = null;
		try {
			// Get previous time interval
			kpi = CoreServicesImpl.getInstance().getEnergyPerCasesHandledPerLocation(
					locationId,
					new Date(begDate.getTime() - offset),
					begDate);
			returnArray[0] = (double) kpi.getKpiValue();
			// Get requested time interval
			kpi = CoreServicesImpl.getInstance().getEnergyPerCasesHandledPerLocation(locationId, begDate, endDate);
			returnArray[1] = (double) kpi.getKpiValue();
			return returnArray;
		}
		catch (Exception ex) {
			throw ex;
		}
	}
	
	public static Map<String, Double> getPrimaryCasesHandledKWhForLocation(List<Long> locationIds, Date begDate, Date endDate) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long locationId : locationIds) {
				HelioKPI kpi = CoreServicesImpl.getInstance().getLocationKwhDateRange(
						locationId,
						begDate,
						endDate);
				Location location = LocationListController.getInstance().getLocation(locationId);
				String locationString = (location == null) ? "Missing" : location.getName();
				valueMap.put(locationString, kpi.getKpiValue().doubleValue());
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	public static List<StandardDeviationKPI> getDeviationForSlicesInPeriod() {
		Date begDate = Date.valueOf("2011-08-14");
		Date endDate = Date.valueOf("2011-08-15"); 
		long monitorId = 15l;
		float timeSliceInterval = 3600.0f;  // seconds
		float precedingDaysInterval = 30f;
		List<StandardDeviationKPI> kpis = null;
		try {
			kpis = CoreServicesImpl.getInstance().getDeviationForSlicesInPeriod(monitorId, timeSliceInterval, precedingDaysInterval, begDate, endDate);
			return kpis;
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
}
