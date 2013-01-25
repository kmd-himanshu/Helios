package com.helio.app.serviceapi;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.helio.app.serviceapi.kpi.EnergyCostAllocationKPI;
import com.helio.app.serviceapi.kpi.EnergyCostPerCasesHandledKPI;
import com.helio.app.serviceapi.kpi.EnergyCostPerSquareFootKPI;
import com.helio.app.serviceapi.kpi.EnergyPerCasesHandledKPI;
import com.helio.app.serviceapi.kpi.EnergyPerSquareFootKPI;
import com.helio.app.serviceapi.kpi.KWHUsageKPI;
import com.helio.app.serviceapi.kpi.StandardDeviationKPI;

public interface CoreServices {

	/**
	 * Returns a collection of packets from a specified monitor for a specified date range.
	 * 
	 * The ID is a long which is unique within boomer.
	 * The date portions of the input may be null for default logic to be applied. If startDT
	 * (the start date timestamp) is null, the startDT will be considered as the earliest date
	 * that packets have been recorded for. If the endDT is null, all packets recorded for the monitor
	 * from the startDT to present time will be returned. If both startDT and endDT are specified,
	 * the packet collection will be within the timestamps, inclusive. 
	 * 
	 * @param deviceMonitorId: Long uniquely representing the Device Monitor
	 * @param startDT: Beginning timestamp for the desired packet list. May be null, indicating returning packets
	 * from the earliest recorded date.
	 * @param endDT Ending timestamp for the desired packet list. May be null, indicating returning packets
	 * from the startDT to present time.
	 * @return A Collection of packets within the defined timestamp range.
	 * @throws HelioInvalidIdException if the provided DeviceMonitorId is not expressed in a valid UUID
	 * string lower case format.
	 * @throws HelioMissingDeviceMonitorException if the provided DeviceMonitorId does not exist in the database.
	 */
	@SuppressWarnings("rawtypes")
	public Collection getPacketsInRange (Long pDeviceMonitorId,Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;
	
	/**
	 * Returns a HelioKPI object indicating the kilowatt hours consumed by a specified monitor
	 * within a specified date range.
	 * 
	 * The ID is a long which is unique within boomer.
	 * The date portions of the input may be null for default logic to be applied. If startDT
	 * (the start date timestamp) is null, the startDT will be considered as the earliest date
	 * that packets have been recorded for. If the endDT is null, all packets recorded for the monitor
	 * from the startDT to present time will be returned. If both startDT and endDT are specified,
	 * the kwh will be computed from the timestamps, inclusive. 
	 * 
	 * @param pDeviceMonitorId: Long uniquely representing the Device Monitor
	 * @param pBegDT: Beginning timestamp for the desired packet list for kwh computation.
	 * @param pEndDT: Ending timestamp for the desired packet list for kwh computation.
	 * @return A HelioKPI expressing kwh consumed within the defined timestamp range.
	 * @throws HelioInvalidIdException if the provided DeviceMonitorId is not expressed in a valid UUID
	 * string lower case format.
	 * @throws HelioMissingDeviceMonitorException if the provided DeviceMonitorId does not exist in the database.
	 */
	public KWHUsageKPI getKwhInRange(Long pDeviceMonitorId,Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;
	
	/**
	 * Returns a list of HelioKPI values indicating the kilowatt hours consumed by a specified monitor
	 * within a specified date range, subdivided in time slices defined by the provided time interval.
	 * 
	 * The ID is a string UUID-formatted string, where alpha characters are expressed in lower-case.
	 * The time slice interval defines the period of time that subdivides the time expressed within
	 * the date range, where the kwh is computed for each time slice. 
	 * The date portions of the input may be null for default logic to be applied. If startDT
	 * (the start date timestamp) is null, the startDT will be considered as the earliest date
	 * that packets have been recorded for. If the endDT is null, all packets recorded for the monitor
	 * from the startDT to present time will be returned. If both startDT and endDT are specified,
	 * the kwh will be computed from the timestamps, inclusive. 
	 * 
	 * @param pDeviceMonitorId: long uniquely representing the Device Monitor
	 * @param pTimeSliceInterval: number of seconds in interval as a float
	 * @param pBegDT: Beginning timestamp for the desired packet list for kwh computation.
	 * @param pEndDT Ending timestamp for the desired packet list for kwh computation.
	 * @return A List of HelioKPI expressing kwh consumed for each time slice interval 
	 * within the defined timestamp range.
	 * @throws HelioInvalidIdException if the provided DeviceMonitorId is not expressed in a valid UUID
	 * string lower case format.
	 * @throws HelioMissingDeviceMonitorException if the provided DeviceMonitorId does not exist in the database.
	 */
	public List<KWHUsageKPI> getKwhDateRangeSlices(
			Long pDeviceMonitorId,
			Float pTimeSliceInterval,
			Date pBegDT, Date pEndDT) 
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	/**
	 * Returns a HelioKPI object indicating the kilowatt hours consumed by an entire location
	 * within a specified date range.
	 * 
	 * The ID is a string UUID-formatted string, where alpha characters are expressed in lower-case.
	 * The date portions of the input may be null for default logic to be applied. If startDT
	 * (the start date timestamp) is null, the startDT will be considered as the earliest date
	 * that packets have been recorded for. If the endDT is null, all packets recorded for the monitor
	 * from the startDT to present time will be returned. If both startDT and endDT are specified,
	 * the kwh will be computed from the timestamps, inclusive. 
	 * 
	 * @param locationId: UUID-formatted string uniquely representing a building allocation
	 * @param startDT: Beginning timestamp for the desired packet list for kwh computation.
	 * May be null, indicating returning packets from the earliest recorded date.
	 * @param endDT Ending timestamp for the desired packet list for kwh computation.
	 * May be null, indicating returning packets from the startDT to present time.
	 * @return A HelioKPI expressing kwh consumed within the defined timestamp range.
	 * @throws HelioInvalidIdException if the provided DeviceMonitorId is not expressed in a valid UUID
	 * string lower case format.
	 * @throws HelioMissingDeviceMonitorException if the provided DeviceMonitorId does not exist in the database.
	 */
	public KWHUsageKPI getAllocationKwhDateRange(
			Long  pBuildingAllocationId,
			Date pBegDT, Date pEndDT) 
		throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	public KWHUsageKPI getBuildingKwhDateRange(Long pBuildingId,Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	public KWHUsageKPI getLocationKwhDateRange(Long pLocationId,Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	public KWHUsageKPI getDivisionKwhDateRange(Long pDivisionId,Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	/**
	 * Returns a list of HelioKPI values indicating the kilowatt hours consumed by a specified Building
	 * Allocation within a specified date range, subdivided in time slices defined by the provided time
	 * interval.
	 * 
	 * The ID is a string UUID-formatted string, where alpha characters are expressed in lower-case.
	 * The date portions of the input may be null for default logic to be applied. If startDT
	 * (the start date timestamp) is null, the startDT will be considered as the earliest date
	 * that packets have been recorded for. If the endDT is null, all packets recorded for the monitor
	 * from the startDT to present time will be returned. If both startDT and endDT are specified,
	 * the kwh will be computed from the timestamps, inclusive. 
	 * 
	 * @param locationId: UUID-formatted string uniquely representing a building allocation
	 * @param startDT: Beginning timestamp for the desired packet list for kwh computation.
	 * May be null, indicating returning packets from the earliest recorded date.
	 * @param endDT Ending timestamp for the desired packet list for kwh computation.
	 * May be null, indicating returning packets from the startDT to present time.
	 * @return A List of HelioKPI expressing kwh consumed for each time slice interval 
	 * within the defined timestamp range.
	 * @throws HelioInvalidIdException if the provided DeviceMonitorId is not expressed in a valid UUID
	 * string lower case format.
	 * @throws HelioMissingDeviceMonitorException if the provided DeviceMonitorId does not exist in the database.
	 */
	public List<KWHUsageKPI> getAllocationKwhDateRangeSlices( Long pBuildingAllocationId, Float pTimeSliceInterval, Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	public List<KWHUsageKPI> getBuildingKwhDateRangeSlices( Long pBuildingAllocationId, Float pTimeSliceInterval, Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	public List<KWHUsageKPI> getLocationKwhDateRangeSlices( Long pBuildingAllocationId, Float pTimeSliceInterval, Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	public List<KWHUsageKPI> getDivisionKwhDateRangeSlices( Long pBuildingAllocationId, Float pTimeSliceInterval, Date pBegDT, Date pEndDT) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;

	/**
	 * For a single monitor, this calculates a standard deviation KPI List. Each KPI in the 
	 * list represents the standard deviation in kWh of a time slice (pTimeSliceInterval) for
	 * a period within the begin and end dates (pEndDate - pBegDate) over a range of days 
	 * (pPrecedingDaysInterval). 
	 * 
	 * OUTPUT: For each time slice interval:
	 * 1) kWh consumed
	 * 2) the max kWh recorded for the specified "precedingDays" interval
	 * 3) the min kWh recorded for the specified precedingDays interval
	 * 4) the standard deviation for the specified precedingDays interval  
	 * 
	 * @param pDeviceMonitorId: primary key of device monitor.
	 * @param pTimeSliceInterval: number of seconds that kWh is to be calculated for.
	 * @param pPrecedingDaysInterval: range of days prior to the pBegDate with which to determine
	 * standard deviation.
	 * @param pBegDate: Beginning timestamp for the desired packet list for kwh computation.
	 * @param pEndDate Ending timestamp for the desired packet list for kwh computation.
	 * @return A list of StandardDeviationKPI objects. 
	 * @throws HelioInvalidIdException if the provided DeviceMonitorId is not expressed in a valid UUID
	 * @throws HelioMissingDeviceMonitorException if the provided DeviceMonitorId does not exist in the database.
	 * @throws HelioInvalidPacketException if the provided DeviceMonitorId have the requested packets.
	 */
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriod(
			Long pDeviceMonitorId,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Date pBegDate, Date pEndDate) 
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByDivision(
			Long pDivisionId,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Date pBegDT, Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByLocation(
			Long pLocationId,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Date pBegDT, Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByBuilding(
			Long pBuildingId,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Date pBegDT, Date pEndDT)
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException;
	
	
	/**
	 * Given a building and period dates, this returns a KPI of the cost of energy per building per Sqft. 
	 * 
	 * @param pBuildingId of the building
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a HelioKPI representing the cost of energy for the building
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyCostPerSquareFootKPI getEnergyCostPerBuildingPerSqft(Long pBuildingId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;
	
	/**
	 * Given a location and period dates, this returns a KPI of the total energy per cases handled 
	 * NOTE: The cases handled are stored by period, however only the first cases handled value in 
	 * the passed date range will be retrieved.  
	 * 
	 * @param pLocationId of the location
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a KCH HelioKPI representing the energy used per case handled.
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyPerCasesHandledKPI getEnergyPerCasesHandledPerBuilding(Long pBuildingId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;
	
	/**
	 * Given a building and period dates, this returns a KPI of the total energy per cases handled 
	 * NOTE: The cases handled are stored by period, however only the first cases handled value in 
	 * the passed date range will be retrieved.  
	 * 
	 * @param pBuilding of the building
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a KCH HelioKPI representing the energy used per case handled.
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyPerCasesHandledKPI getEnergyPerCasesHandledPerLocation(Long pLocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;

	/**
	 * Given a location and period dates, this returns a KPI of the energy cost per cases handled 
	 * NOTE: The cases handled and cost are stored by period, however only the metric value in 
	 * the passed date range will be retrieved.  
	 * 
	 * @param pBuildingId of the building
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a  HelioKPI representing the cost of energy used per case handled.
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyCostPerCasesHandledKPI getEnergyCostPerCasesHandledPerBuilding(Long pBuildingId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;

	/**
	 * Given a location and period dates, this returns a KPI of the energy cost per cases handled 
	 * NOTE: The cases handled and cost are stored by period, however only the metric value in 
	 * the passed date range will be retrieved.  
	 * 
	 * @param pLocationId of the location
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a  HelioKPI representing the cost of energy used per case handled.
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyCostPerCasesHandledKPI getEnergyCostPerCasesHandledPerLocation(Long pLocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;

	/**
	 * Given a building and period dates, this returns a KPI of the cost of energy per building. 
	 * 
	 * @param pBuildingId of the building
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a HelioKPI representing the cost of energy for the building
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyCostAllocationKPI getEnergyCostPerBuilding(Long pBuildingId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;

	/**
	 * Given a location and period dates, this returns a KPI of the cost of energy per location. 
	 * 
	 * @param pLocationId of the location
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a HelioKPI representing the cost of energy for the building
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyCostAllocationKPI getEnergyCostPerLocation(Long pLocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;

	/**
	 * Given a building and period dates, this returns a KPI of the energy per building 
	 * 
	 * @param pBuildingId of the building
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a HelioKPI representing the energy used per building.
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyPerSquareFootKPI getEnergyPerBuildingPerSqft(Long pBuildingId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;
	
	/**
	 * Given a building and period dates, this returns a KPI of the energy per building 
	 * per square feet
	 * 
	 * @param pBuildingId of the building
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a HelioKPI representing the energy used per building.
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyPerSquareFootKPI getEnergyPerLocationPerSqft(Long pLocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException;

}
