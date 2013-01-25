package com.helio.boomer.rap.engine.servicedata;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.helio.app.serviceapi.CoreServices;
import com.helio.app.serviceapi.CoreServicesImpl;
import com.helio.app.serviceapi.kpi.EnergyCostAllocationKPI;
import com.helio.app.serviceapi.kpi.HelioKPI;
import com.helio.app.serviceapi.kpi.KWHUsageKPI;
import com.helio.app.serviceapi.kpi.UsageKPI;
import com.helio.boomer.rap.engine.BuildingListController;
import com.helio.boomer.rap.engine.LocationListController;
import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.model.Period;

public class BusinessUnitReportDAO {
	
//	public static CoreServices coreServices = ServiceHandler.getInstance();
	public static CoreServices coreServices = CoreServicesImpl.getInstance();

	public static Double[] getCasesHandledKWhForLocation(Long locationId, Date begDate, Date endDate) throws Exception {
		Double[] returnArray = new Double[2];
		long offset = endDate.getTime() - begDate.getTime();
		HelioKPI kpi = null;
		try {
// Get previous time interval
			kpi = coreServices.getEnergyPerCasesHandledPerLocation(
					locationId,
					new Date(begDate.getTime() - offset),
					begDate);
			returnArray[0] = (double) kpi.getKpiValue();
			// Get requested time interval
			kpi = coreServices.getEnergyPerCasesHandledPerLocation(locationId, begDate, endDate);
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
				HelioKPI kpi = coreServices.getLocationKwhDateRange(
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

	public static Map<String, Double> getEnergyPerBuildingPerSquareFoot(
			List<Long> buildingIds,
			Date begDate,
			Date endDate) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long buildingId : buildingIds) {
				HelioKPI kpi = coreServices.getEnergyPerBuildingPerSqft(
						buildingId,
						begDate,
						endDate);
				Location location = LocationListController.getInstance().getLocation(buildingId);
				String locationString = (location == null) ? "Missing" : location.getName();
				valueMap.put(locationString, kpi.getKpiValue().doubleValue());
			}
			return valueMap;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Map<String, Double> getEnergyPerBuildingPerSquareFoot(
			List<Long> buildingIds, 
			List<Period> periods,
			boolean includeLocationName) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long buildingId : buildingIds) {
				Double summedValue = 0.0;
				Building building = BuildingListController.getInstance().getBuilding(buildingId);
				String buildingString = (building == null) ? "Missing" : building.getName();
				if ((includeLocationName) && (building != null)) {
//					buildingString = buildingString.concat(" <").concat(building.getLocation().getName() + ">");
					buildingString = buildingString.concat("\u2013").concat(building.getLocation().getName());
				}
				for (Period period : periods) {
					try {
						HelioKPI kpi = coreServices.getEnergyPerBuildingPerSqft(
								buildingId,
								period.getStartDt(),
								period.getEndDt());
						summedValue += kpi.getKpiValue().doubleValue();
					} catch (Exception e) {
						System.out.println("Problem with obtaining KPI: " + e.toString());
					}
				}
				valueMap.put(buildingString, summedValue);
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	public static Map<String, Double> getEnergyPerBuilding(
			List<Long> buildingIds, 
			Date startDt,
			Date endDt,
			boolean includeLocationName) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long buildingId : buildingIds) {
				Building building = BuildingListController.getInstance().getBuilding(buildingId);
				String buildingString = (building == null) ? "Missing" : building.getName();
				if ((includeLocationName) && (building != null)) {
//					buildingString = buildingString.concat(" <").concat(building.getLocation().getName() + ">");
					buildingString = buildingString.concat("\u2013").concat(building.getLocation().getName());
				}
				try {
					HelioKPI kpi = coreServices.getBuildingKwhDateRange(
							buildingId,
							startDt,
							endDt);
					valueMap.put(buildingString, kpi.getKpiValue().doubleValue());
				} catch (Exception e) {
					System.out.println("Problem with obtaining KPI: " + e.toString());
				}
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}
	
	public static Map<String, EnergyAndCostPerBuildingKPIs> getEnergyAndCostPerBuilding(
			List<Long> buildingIds, 
			Date startDt,
			Date endDt,
			boolean includeLocationName) throws Exception {
		try {
			Map<String, EnergyAndCostPerBuildingKPIs> valueMap = Maps.newHashMap();
			for (Long buildingId : buildingIds) {
				Building building = BuildingListController.getInstance().getBuilding(buildingId);
				String buildingString = (building == null) ? "Missing" : building.getName();
				if ((includeLocationName) && (building != null)) {
//					buildingString = buildingString.concat(" <").concat(building.getLocation().getName() + ">");
					buildingString = buildingString.concat("\u2013").concat(building.getLocation().getName());
				}
				try {
					KWHUsageKPI energyKpi = coreServices.getBuildingKwhDateRange(
							buildingId,
							startDt,
							endDt);
					EnergyCostAllocationKPI costKpi = coreServices.getEnergyCostPerBuilding(
							buildingId,
							startDt,
							endDt);
					if ((energyKpi != null) && (costKpi != null)) {
						EnergyAndCostPerBuildingKPIs eacpbKPIs = new EnergyAndCostPerBuildingKPIs(buildingId, energyKpi, costKpi); 
						valueMap.put(buildingString, eacpbKPIs);
					}
				} catch (Exception e) {
					System.out.println("Problem with energy and cost KPIs for building: " + e.toString());
				}
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	public static Map<String, Double> getPrimaryCasesHandledKWhForBuilding(
			List<Long> buildingIds, 
			List<Period> periods,
			boolean includeLocationName) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long buildingId : buildingIds) {
				Double summedValue = 0.0;
				Building building = BuildingListController.getInstance().getBuilding(buildingId);
				String buildingString = (building == null) ? "Missing" : building.getName();
				if ((includeLocationName) && (building != null)) {
//					buildingString = buildingString.concat(" <").concat(building.getLocation().getName() + ">");
					buildingString = buildingString.concat("\u2013").concat(building.getLocation().getName());
				}
				for (Period period : periods) {
					try {
						HelioKPI kpi = coreServices.getEnergyPerCasesHandledPerBuilding(
								buildingId,
								period.getStartDt(),
								period.getEndDt());
						summedValue += kpi.getKpiValue().doubleValue();
					} catch (Exception e) {
						System.out.println("Problem with obtaining KPI: " + e.toString());
					}
				}
				valueMap.put(buildingString, summedValue);
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}


	public static Map<String, Double> getPrimaryCasesHandledKWhForLocation(
			List<Long> locationIds, 
			List<Period> periods) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long locationId : locationIds) {
				Double summedValue = 0.0;
				Location location = LocationListController.getInstance().getLocation(locationId);
				String locationString = (location == null) ? "Missing" : location.getName();
				for (Period period : periods) {
					try {
						HelioKPI kpi = coreServices.getEnergyPerCasesHandledPerLocation(
								locationId,
								period.getStartDt(),
								period.getEndDt());
						summedValue += kpi.getKpiValue().doubleValue();
					} catch (Exception e) {
						System.out.println("Problem with obtaining KPI: " + e.toString());
					}
				}
				valueMap.put(locationString, summedValue);
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}
	public static Map<String, Double> getEnergyCostPerLocation(
			List<Long> locationIds, 
			Date startDt,
			Date endDt) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long locationId : locationIds) {
				Location location = LocationListController.getInstance().getLocation(locationId);
				String locationString = (location == null) ? "Missing" : location.getName();
				try {
					HelioKPI kpi = coreServices.getEnergyCostPerLocation(
							locationId,
							startDt,
							endDt);
					valueMap.put(locationString, ((UsageKPI) kpi).getDemand().doubleValue());
				} catch (Exception e) {
					System.out.println("Problem with obtaining KPI: " + e.toString());
				}
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	public static Map<String, Double> getKwhPerLocation(
			List<Long> locationIds, 
			Date startDt,
			Date endDt) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long locationId : locationIds) {
				Location location = LocationListController.getInstance().getLocation(locationId);
				String locationString = (location == null) ? "Missing" : location.getName();
				try {
					HelioKPI kpi = coreServices.getLocationKwhDateRange(
							locationId,
							startDt,
							endDt);
					valueMap.put(locationString, ((UsageKPI) kpi).getDemand().doubleValue());
				} catch (Exception e) {
					System.out.println("Problem with obtaining KPI: " + e.toString());
				}
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	public static Map<String, Double> getDemandPerLocation(
			List<Long> locationIds, 
			Date startDt,
			Date endDt) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long locationId : locationIds) {
				Location location = LocationListController.getInstance().getLocation(locationId);
				String locationString = (location == null) ? "Missing" : location.getName();
				try {
					HelioKPI kpi = coreServices.getEnergyPerLocationPerSqft(
							locationId,
							startDt,
							endDt);
					valueMap.put(locationString, ((UsageKPI) kpi).getDemand().doubleValue());
				} catch (Exception e) {
					System.out.println("Problem with obtaining KPI: " + e.toString());
				}
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}
	
	public static Map<String, Double> getEnergyPerLocation(
			List<Long> locationIds, 
			Date startDt,
			Date endDt) throws Exception {
		try {
			Map<String, Double> valueMap = Maps.newHashMap();
			for (Long locationId : locationIds) {
				Location location = LocationListController.getInstance().getLocation(locationId);
				String locationString = (location == null) ? "Missing" : location.getName();
				try {
					HelioKPI kpi = coreServices.getLocationKwhDateRange(
							locationId,
							startDt,
							endDt);
					valueMap.put(locationString, ((KWHUsageKPI) kpi).getKpiValue().doubleValue());
				} catch (Exception e) {
					System.out.println("Problem with obtaining KPI: " + e.toString());
				}
			}
			return valueMap;
		}
		catch (Exception ex) {
			throw ex;
		}
	}
	

}
