package com.helio.boomer.rap.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.helio.app.serviceapi.CoreServices;
import com.helio.app.serviceapi.HelioInvalidIdException;
import com.helio.app.serviceapi.HelioInvalidPacketException;
import com.helio.app.serviceapi.HelioMissingDeviceMonitorException;
import com.helio.app.serviceapi.HelioMissingMetricException;
import com.helio.app.serviceapi.kpi.EnergyCostAllocationKPI;
import com.helio.app.serviceapi.kpi.EnergyCostPerCasesHandledKPI;
import com.helio.app.serviceapi.kpi.EnergyCostPerSquareFootKPI;
import com.helio.app.serviceapi.kpi.EnergyPerCasesHandledKPI;
import com.helio.app.serviceapi.kpi.EnergyPerSquareFootKPI;
import com.helio.app.serviceapi.kpi.KWHUsageKPI;
import com.helio.app.serviceapi.kpi.StandardDeviationKPI;

public class ServiceHandler implements CoreServices {
	
	public static class ServiceHandlerHolder {
		private final static ServiceHandler INSTANCE = new ServiceHandler();
	}

	public static ServiceHandler getInstance() {
		return ServiceHandlerHolder.INSTANCE;
	}


//	public static KWHUsageKPI getKWHUsageKPI(Long locationId, Date begDT, Date endDT) {
//		String json = WSHandler.getWebResourceService()
//			.path("locationKwhDateRange")
//			.path(locationId.toString())
//			.path(begDT.toString())
//			.path(endDT.toString())
//			.accept("text/json")
//			.get(String.class);
//		return GsonHandler.getKWHUsageKPIFromJson(json);
//	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Call to RESTful service with pattern:
	 * "/packetsInRange/{monitorId}/{begDate}/{endDate}"
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Collection getPacketsInRange(Long pDeviceMonitorId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("packetsInRange")
				.path(pDeviceMonitorId.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getDevicePacketCollectionFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/kwhInRange/{monitorId}/{begDate}/{endDate}"
	 * 
	 * Returns type KWHUsageKPI
	 */
	@Override
	public KWHUsageKPI getKwhInRange(Long pDeviceMonitorId, java.util.Date pBegDT,
			java.util.Date pEndDT) throws HelioInvalidIdException,
			HelioMissingDeviceMonitorException, HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("kwhInRange")
				.path(pDeviceMonitorId.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/kwhDateRangeSlices/{monitorId}/{timeSliceInterval}/{begDate}/{endDate}"
	 * 
	 * Returns type List<KWHUsageKPI>
	 */
	@Override
	public List<KWHUsageKPI> getKwhDateRangeSlices(Long pDeviceMonitorId,
			Float pTimeSliceInterval, java.util.Date pBegDT,
			java.util.Date pEndDT) throws HelioInvalidIdException,
			HelioMissingDeviceMonitorException, HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("kwhDateRangeSlices")
				.path(pDeviceMonitorId.toString())
				.path(pTimeSliceInterval.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIListFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/allocationKwhDateRange/{allocationId}/{begDate}/{endDate}"
	 * 
	 * Returns type KWHUsageKPI
	 */
	@Override
	public KWHUsageKPI getAllocationKwhDateRange(Long pBuildingAllocationId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("allocationKwhDateRange")
				.path(pBuildingAllocationId.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/buildingKwhDateRange/{buildingId}/{begDate}/{endDate}"
	 * 
	 * Returns type KWHUsageKPI
	 */
	@Override
	public KWHUsageKPI getBuildingKwhDateRange(Long pBuildingId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("buildingKwhDateRange")
				.path(pBuildingId.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/locationKwhDateRange/{locationId}/{begDate}/{endDate}"
	 * 
	 * Returns type KWHUsageKPI
	 */
	@Override
	public KWHUsageKPI getLocationKwhDateRange(Long pLocationId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("locationKwhDateRange")
				.path(pLocationId.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * /divisionKwhDateRange/{divisionId}/{begDate}/{endDate}"
	 * 
	 * Returns type KWHUsageKPI
	 */
	@Override
	public KWHUsageKPI getDivisionKwhDateRange(Long pDivisionId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("divisionKwhDateRange")
				.path(pDivisionId.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/allocationKwhDateRangeSlices/{allocationId}/{timeSliceInterval}/{begDate}/{endDate}"
	 * 
	 * Returns type KWHUsageKPI
	 */
	@Override
	public List<KWHUsageKPI> getAllocationKwhDateRangeSlices(
			Long pBuildingAllocationId, Float pTimeSliceInterval,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("allocationKwhDateRangeSlices")
				.path(pBuildingAllocationId.toString())
				.path(pTimeSliceInterval.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIListFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/buildingKwhDateRangeSlices/{buildingId}/{timeSliceInterval}/{begDate}/{endDate}"
	 * 
	 * Returns type List<KWHUsageKPI>
	 */
	@Override
	public List<KWHUsageKPI> getBuildingKwhDateRangeSlices(
			Long pBuildingId, Float pTimeSliceInterval,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("buildingKwhDateRangeSlices")
				.path(pBuildingId.toString())
				.path(pTimeSliceInterval.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIListFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/locationKwhDateRangeSlices/{locationId}/{timeSliceInterval}/{begDate}/{endDate}"
	 * 
	 * Returns type List<KWHUsageKPI>
	 */
	@Override
	public List<KWHUsageKPI> getLocationKwhDateRangeSlices(
			Long pLocationId, Float pTimeSliceInterval,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("locationKwhDateRangeSlices")
				.path(pLocationId.toString())
				.path(pTimeSliceInterval.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIListFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/divisionKwhDateRangeSlices/{divisionId}/{timeSliceInterval}/{begDate}/{endDate}"
	 * 
	 * Returns type List<KWHUsageKPI>
	 */
	@Override
	public List<KWHUsageKPI> getDivisionKwhDateRangeSlices(
			Long pDivisionId, Float pTimeSliceInterval,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		try {
			String json = WSHandler.getWebResourceService()
				.path("divisionKwhDateRangeSlices")
				.path(pDivisionId.toString())
				.path(pTimeSliceInterval.toString())
				.path(pBegDT.toString())
				.path(pEndDT.toString())
				.accept("text/json")
				.get(String.class);
			return GsonHandler.getKWHUsageKPIListFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	
	/**
	 * Call to RESTful service with pattern:
	 * "/kwhDateRangeSliceIntervals/{monitorId}/{timeSliceInterval}/{precedingDaysInterval}/{begDate}/{endDate}"
	 * 
	 * Returns type List<KWHUsageKPI>
	 */
//	@Override
//	public KwhDateRangeSliceIntervalsKPI getKwhDateRangeSliceIntervals(
//			Long pDeviceMonitorId, Float pTimeSliceInterval,
//			Float pPrecedingDaysInterval, java.util.Date pBegDT,
//			java.util.Date pEndDT) throws HelioInvalidIdException,
//			HelioMissingDeviceMonitorException, HelioInvalidPacketException {
//		try {
//			String json = WSHandler.getWebResourceService()
//				.path("kwhDateRangeSliceIntervals")
//				.path(pDeviceMonitorId.toString())
//				.path(pTimeSliceInterval.toString())
//				.path(pPrecedingDaysInterval.toString())
//				.path(pBegDT.toString())
//				.path(pEndDT.toString())
//				.accept("text/json")
//				.get(String.class);
//			return GsonHandler.getKwhDateRangeSliceIntervalsKPI(json);
//		} catch (Exception e) {
//			throw new HelioInvalidPacketException(e);
//		}
//	}

	/**
	 * Call to RESTful service with pattern:
	 * "/energyPerCasesHandledPerBuilding/{buildingId}/{begDate}/{endDate}"
	 * 
	 * Returns type EnergyPerCasesHandledKPI
	 */
	@Override
	public EnergyPerCasesHandledKPI getEnergyPerCasesHandledPerBuilding(Long pBuildingId,
			java.util.Date pBegDT, java.util.Date pEndDT)
	throws HelioInvalidIdException, HelioMissingMetricException,
	HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		try {
			String json = WSHandler.getWebResourceService()
			.path("energyPerCasesHandledPerBuilding")
			.path(pBuildingId.toString())
			.path(pBegDT.toString())
			.path(pEndDT.toString())
			.accept("text/json")
			.get(String.class);
			return GsonHandler.getEnergyPerCasesHandledKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/energyPerCasesHandledPerLocation/{locationId}/{begDate}/{endDate}"
	 * 
	 * Returns type EnergyPerCasesHandledKPI
	 */
	@Override
	public EnergyPerCasesHandledKPI getEnergyPerCasesHandledPerLocation(Long pLocationId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingMetricException,
			HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		try {
			String json = WSHandler.getWebResourceService()
			.path("energyPerCasesHandledPerLocation")
			.path(pLocationId.toString())
			.path(pBegDT.toString())
			.path(pEndDT.toString())
			.accept("text/json")
			.get(String.class);
			return GsonHandler.getEnergyPerCasesHandledKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/energyCostPerCasesHandledPerBuilding/{buildingId}/{begDate}/{endDate}"
	 * 
	 * Returns type EnergyCostPerCasesHandledKPI
	 */
	@Override
	public EnergyCostPerCasesHandledKPI getEnergyCostPerCasesHandledPerBuilding(Long pBuildingId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingMetricException,
			HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		try {
			String json = WSHandler.getWebResourceService()
			.path("energyCostPerCasesHandledPerBuilding")
			.path(pBuildingId.toString())
			.path(pBegDT.toString())
			.path(pEndDT.toString())
			.accept("text/json")
			.get(String.class);
			return GsonHandler.getEnergyCostPerCasesHandledKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/energyCostPerCasesHandledPerLocation/{locationId}/{begDate}/{endDate}"
	 * 
	 * Returns type EnergyCostPerCasesHandledKPI
	 */
	@Override
	public EnergyCostPerCasesHandledKPI getEnergyCostPerCasesHandledPerLocation(Long pLocationId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingMetricException,
			HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		try {
			String json = WSHandler.getWebResourceService()
			.path("energyCostPerCasesHandledPerLocation")
			.path(pLocationId.toString())
			.path(pBegDT.toString())
			.path(pEndDT.toString())
			.accept("text/json")
			.get(String.class);
			return GsonHandler.getEnergyCostPerCasesHandledKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/energyCostPerBuilding/{buildingId}/{begDate}/{endDate}"
	 * 
	 * Returns type EnergyCostAllocationKPI
	 */
	@Override
	public EnergyCostAllocationKPI getEnergyCostPerBuilding(Long pBuildingId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingMetricException,
			HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		try {
			String json = WSHandler.getWebResourceService()
			.path("energyCostPerBuilding")
			.path(pBuildingId.toString())
			.path(pBegDT.toString())
			.path(pEndDT.toString())
			.accept("text/json")
			.get(String.class);
			return GsonHandler.getEnergyCostAllocationKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/energyCostPerLocation/{locationId}/{begDate}/{endDate}"
	 * 
	 * Returns type EnergyCostAllocationKPI
	 */
	@Override
	public EnergyCostAllocationKPI getEnergyCostPerLocation(Long pLocationId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingMetricException,
			HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		try {
			String json = WSHandler.getWebResourceService()
			.path("energyCostPerLocation")
			.path(pLocationId.toString())
			.path(pBegDT.toString())
			.path(pEndDT.toString())
			.accept("text/json")
			.get(String.class);
			return GsonHandler.getEnergyCostAllocationKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/energyPerBuildingPerSqft/{buildingId}/{begDate}/{endDate}"
	 * 
	 * Returns type EnergyPerSquareFootKPI
	 */
	@Override
	public EnergyPerSquareFootKPI getEnergyPerBuildingPerSqft(Long pBuildingId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingMetricException,
			HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		try {
			String json = WSHandler.getWebResourceService()
			.path("energyPerBuildingPerSqft")
			.path(pBuildingId.toString())
			.path(pBegDT.toString())
			.path(pEndDT.toString())
			.accept("text/json")
			.get(String.class);
			return GsonHandler.getEnergyPerSquareFootKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}

	/**
	 * Call to RESTful service with pattern:
	 * "/energyPerLocationPerSqft/{locationId}/{begDate}/{endDate}"
	 * 
	 * Returns type EnergyPerSquareFootKPI
	 */
	@Override
	public EnergyPerSquareFootKPI getEnergyPerLocationPerSqft(Long pLocationId,
			java.util.Date pBegDT, java.util.Date pEndDT)
			throws HelioInvalidIdException, HelioMissingMetricException,
			HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		try {
			String json = WSHandler.getWebResourceService()
			.path("energyPerLocationPerSqft")
			.path(pLocationId.toString())
			.path(pBegDT.toString())
			.path(pEndDT.toString())
			.accept("text/json")
			.get(String.class);
			return GsonHandler.getEnergyPerSquareFootKPIFromJson(json);
		} catch (Exception e) {
			throw new HelioInvalidPacketException(e);
		}
	}


	@Override
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriod(
			Long pDeviceMonitorId, Float pTimeSliceInterval,
			Float pPrecedingDaysInterval, Date pBegDate, Date pEndDate)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByDivision(
			Long pDivisionId, Float pTimeSliceInterval,
			Float pPrecedingDaysInterval, Date pBegDT, Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByLocation(
			Long pLocationId, Float pTimeSliceInterval,
			Float pPrecedingDaysInterval, Date pBegDT, Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByBuilding(
			Long pBuildingId, Float pTimeSliceInterval,
			Float pPrecedingDaysInterval, Date pBegDT, Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException,
			HelioInvalidPacketException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public EnergyCostPerSquareFootKPI getEnergyCostPerBuildingPerSqft(
			Long pBuildingId, Date pBegDate, Date pEndDate)
			throws HelioInvalidIdException, HelioMissingMetricException,
			HelioInvalidPacketException, HelioMissingDeviceMonitorException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
