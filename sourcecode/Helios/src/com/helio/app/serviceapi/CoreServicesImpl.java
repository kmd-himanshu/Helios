/**
 * 
 */
package com.helio.app.serviceapi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.helio.app.boomer.common.dal.CoreServicesDAO;
import com.helio.app.boomer.common.dal.DAOException;
import com.helio.app.boomer.common.dal.model.BoomerMetric;
import com.helio.app.boomer.common.dal.model.Building;
import com.helio.app.boomer.common.dal.model.ContextHolder;
import com.helio.app.boomer.common.dal.model.ContextTypeEnum;
import com.helio.app.boomer.common.dal.model.DevicePacket;
import com.helio.app.boomer.common.dal.model.Location;
import com.helio.app.boomer.common.dal.model.MetricTypeEnum;
import com.helio.app.serviceapi.kpi.EnergyCostAllocationKPI;
import com.helio.app.serviceapi.kpi.EnergyCostPerCasesHandledKPI;
import com.helio.app.serviceapi.kpi.EnergyCostPerSquareFootKPI;
import com.helio.app.serviceapi.kpi.EnergyPerCasesHandledKPI;
import com.helio.app.serviceapi.kpi.EnergyPerSquareFootKPI;
import com.helio.app.serviceapi.kpi.HelioInvalidKPIException;
import com.helio.app.serviceapi.kpi.KWHUsageKPI;
import com.helio.app.serviceapi.kpi.StandardDeviationKPI;
import com.helio.app.serviceapi.rules.KPIRules;
import com.helio.app.serviceapi.rules.RuleRequest;
import com.helio.app.serviceapi.rules.RulesServiceException;
import com.thoughtworks.xstream.XStream;

/**
 * @author rickschwartz
 *
 */
public class CoreServicesImpl implements CoreServices {
	
	private static CoreServicesImpl instance = null;
	
	private CoreServicesImpl() {
		super();
	}
	/** 
	 * Static method to get singleton instance of CoreServices. 
	 * @return
	 */
	public synchronized static CoreServicesImpl getInstance() 
	{
		if (instance == null) {
			instance = new CoreServicesImpl();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.helio.app.serviceapi.CoreServices#getPacketsInRange(java.lang.String, java.util.Date, java.util.Date)
	 */
	public Collection<DevicePacket> getPacketsInRange (
			Long deviceMonitorId,
			Date startDT,
			Date endDT) throws HelioInvalidIdException, HelioMissingDeviceMonitorException {
		
		Timestamp begTime = new Timestamp(startDT.getTime());
		Timestamp endTime = new Timestamp(endDT.getTime());
		
		Collection<DevicePacket> packets = null;
		try {
			packets = CoreServicesDAO.getInstance().getPacketsForMonitorInRange(deviceMonitorId, begTime, endTime);
		}
		catch (DAOException ex) {
			throw new HelioInvalidIdException(ex);
		}
		
		return packets;
	}

	/**
	 * Gets the current packet for the given monitor
	 * 
	 * @param deviceMonitorId
	 * @return
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingDeviceMonitorException
	 */
	private DevicePacket getCurrentPacket (Long deviceMonitorId) 
		throws HelioInvalidIdException, HelioMissingDeviceMonitorException {
		
		DevicePacket packet = null;
		try {
			packet = CoreServicesDAO.getInstance().getCurrentMonitorPacket(deviceMonitorId);
		}
		catch (DAOException ex) {
			throw new HelioInvalidIdException(ex);
		}
		
		return packet;
	}
	
	public String getCurrentPacketasXML (String pDeviceMonitorIdStr) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException {
	
		Long deviceMonitorId = Long.valueOf(pDeviceMonitorIdStr);
		DevicePacket packet = null;
		packet = getCurrentPacket (deviceMonitorId);
		
		XStream xstream = new XStream();
		xstream.alias("devicePacket", com.helio.app.boomer.common.dal.model.DevicePacket.class);
		xstream.alias("packetValue", com.helio.app.boomer.common.dal.model.PacketValue.class);
		String xml = xstream.toXML(packet);
		return xml;
	}

	/* (non-Javadoc)
	 * @see com.helio.app.serviceapi.CoreServices#getKwhDateRange(java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public KWHUsageKPI getKwhInRange(Long pDeviceMonitorId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{

		Timestamp begTime = new Timestamp(pBegDate.getTime());
		Timestamp endTime = new Timestamp(pEndDate.getTime());
		return getHelioKPIInRange(pDeviceMonitorId, begTime, endTime);
	}

	/*
	 * This gets a kWh KPI for a date range specified with java.sql.Timestamp
	 */
	private KWHUsageKPI getHelioKPIInRange(Long pDeviceMonitorId, Timestamp pBegTime, Timestamp pEndTime) 
		throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		/*
		 * Get the kWh value and then return a KPI
		 */
		float kWh = getKWHInRange(pDeviceMonitorId, pBegTime, pEndTime);
		ContextHolder context = getHierarchyContext(ContextTypeEnum.MONITOR, pDeviceMonitorId);	
		/*
		 * Construct Usage KPI
		 */
		try {
			KWHUsageKPI kpi = new KWHUsageKPI(context, kWh, pBegTime, pEndTime);
			return kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}
	
	/*
	 * Given a monitor identifier and a date range, this gets the kWh value
	 */
	private float getKWHInRange(Long pDeviceMonitorId, Timestamp pBegTime,
			Timestamp pEndTime) throws HelioInvalidIdException,
			HelioInvalidPacketException 
			{

		/*
		 * Get an instance of the rules layer. 
		 * These handle the  specifics of pulse and induction meters. 
		 */
		KPIRules 		rules 		= KPIRules.getInstance();
		CoreServicesDAO dao 		= CoreServicesDAO.getInstance();
		
		DevicePacket 	begPacket 	= null;
		DevicePacket 	endPacket 	= null;
		RuleRequest 	request 	= new RuleRequest();
		float 			kWh			= 0f;
		
		/*
		 * Get the two packets that bracket the date range.
		 * Then call rules to get kWh based on specifics of monitors
		 */
		try {
			begPacket = dao.getMonitorPacket(pDeviceMonitorId.longValue(), pBegTime, true);
			endPacket = dao.getMonitorPacket(pDeviceMonitorId.longValue(), pEndTime, true);

			request.setBegTime(pBegTime);
			request.setEndTime(pEndTime);
			request.setBegPacket(begPacket);
			request.setEndPacket(endPacket);
			kWh = rules.calculateKWH(request);
		}
		catch (DAOException ex) {
			/*
			 * THIS WAS COMMENTED OUT SINCE MONITORS WITH NO PACKETS 
			 * WILL THROW EXCEPTION. MAYBE BETTER TO RETURN ZERO KWH
			 */
			//throw new HelioInvalidIdException(ex);
		}
		catch (RulesServiceException ex) {
			throw new HelioInvalidPacketException(ex);
		}

		return kWh;
	}

	/* (non-Javadoc)
	 * @see com.helio.app.serviceapi.CoreServices#getKwhDateRangeSlices(java.lang.String, java.lang.Float, java.util.Date, java.util.Date)
	 */
	@Override
	public List<KWHUsageKPI> getKwhDateRangeSlices(Long pDeviceMonitorId,
			Float pTimeSliceInterval, Date pStartDT, Date pEndDT)
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pStartDT.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDT.getTime());
		
		return getKwhKPIDateRangeSlices(pDeviceMonitorId, pTimeSliceInterval, begTimestamp, endTimestamp);
	}

	/*
	 * This gets a list of KPI objects which represent kWh time slices for the indicated range
	 */
	private List<KWHUsageKPI> getKwhKPIDateRangeSlices(Long pDeviceMonitorId,
			Float pTimeSliceInterval, Timestamp pBegTime, Timestamp pEndTime)
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		
		/*
		 * Normalize all variables to longs. The float represents seconds. 
		 */
		long begTime = pBegTime.getTime();
		long endTime = pEndTime.getTime();
		long interval = (long) (Float.valueOf(pTimeSliceInterval) * 1000);
		long nextTime = begTime+interval;

		ArrayList<KWHUsageKPI>  kpiList = new ArrayList<KWHUsageKPI>();
		/*
		 * Get a KPI for a time slice interval and add it to the list
		 */
		while (nextTime <= endTime) {
			Timestamp begTimestamp = new Timestamp(begTime);
			Timestamp endTimestamp = new Timestamp(nextTime);
			float kWh = getKWHInRange(pDeviceMonitorId, begTimestamp, endTimestamp);
			ContextHolder context = getHierarchyContext(ContextTypeEnum.MONITOR, pDeviceMonitorId);	
			try {
				KWHUsageKPI kpi = new KWHUsageKPI(context, kWh, begTimestamp, endTimestamp);
				kpiList.add(kpi);
			}
			catch (HelioInvalidKPIException ex) {
				throw new HelioInvalidIdException(ex);
			}
			
			begTime = nextTime;
			nextTime = begTime+interval;
		}

		return kpiList;
	}

	
	/* (non-Javadoc)
	 * @see com.helio.app.serviceapi.CoreServices#getLocationKwhDateRange(java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public KWHUsageKPI getAllocationKwhDateRange(Long pAllocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		return getKWHForDateRange(ContextTypeEnum.ALLOCATION, pAllocationId, begTimestamp, endTimestamp);
	}
	public KWHUsageKPI getBuildingKwhDateRange(Long pBuildingId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		KWHUsageKPI kwhKPI = getKWHForDateRange(ContextTypeEnum.BUILDING, pBuildingId, begTimestamp, endTimestamp);
		if (kwhKPI==null || kwhKPI.getKpiValue() <= 0f) {
			/*
			 * If the kWh cannot be determined by Building because there are no meters at that level
			 * then ratio the amount based on the building square feet from the location KWH.
			 */
			ContextHolder context = kwhKPI.getContext(); // get the building context
			kwhKPI = (KWHUsageKPI)getLocationKwhDateRange(context.getLocationId(), pBegDate, pEndDate);
			Building building = getBuilding(pBuildingId);
			float kWh = kwhKPI.getKpiValue() * (building.getPercentSquareFeet() / 100);
			try {
				kwhKPI = new KWHUsageKPI(context, kWh, begTimestamp, endTimestamp);
				return kwhKPI;
			} catch (HelioInvalidKPIException ex) {
				throw new HelioInvalidIdException(ex);
			}
		}
		return kwhKPI;
	}
	public KWHUsageKPI getLocationKwhDateRange(Long pLocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		return getKWHForDateRange(ContextTypeEnum.LOCATION, pLocationId, begTimestamp, endTimestamp);
	}
	public KWHUsageKPI getDivisionKwhDateRange(Long pDivisionId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		return getKWHForDateRange(ContextTypeEnum.DIVISION, pDivisionId, begTimestamp, endTimestamp);
	}	
	
	/*
	 * Provides the HelioKPI based on context and time stamp range
	 */
	private KWHUsageKPI getKWHForDateRange(ContextTypeEnum pContext, Long pId, Timestamp pBegTime, Timestamp pEndTime) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		float kWh = getKwhInRange(pContext, pId, pBegTime, pEndTime);
		ContextHolder context = getHierarchyContext(pContext, pId);
		try {
			KWHUsageKPI kwhKPI = new KWHUsageKPI(context, kWh, pBegTime, pEndTime);
			return kwhKPI;
		} catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}

	/*
	 * This gets a list of monitors ids for a particular context
	 */
	private List<Long> getMonitorsForContext(ContextTypeEnum pContext, Long pId) 
	throws HelioInvalidIdException
	{
		/*
		 * Get all the monitor IDs for the context
		 */
		List<Long> monitorIds = null;
		try {
			monitorIds 	= CoreServicesDAO.getInstance().getMonitorIds(pContext, pId);
			return monitorIds;
		}
		catch (DAOException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}

	/*
	 * This adds up the individual monitor kWh readings for a group of monitor ids
	 */
	private float getKwhInRange(ContextTypeEnum pContext, Long pId, Timestamp pBegTime, Timestamp pEndTime) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		List<Long> monitorIds = getMonitorsForContext(pContext, pId);
		return getKwhInRangeForMonitors(monitorIds, pBegTime, pEndTime);
	}

	/*
	 * This adds up the individual monitor kWh readings for a list of monitors
	 */
	private float getKwhInRangeForMonitors(List<Long> monitorIds,  Timestamp pBegTime, Timestamp pEndTime) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Iterator<Long> itr = monitorIds.iterator();
		float kWh = 0f;
		/*
		 * Iterate through each monitor and accumulate the kWh value
		 */
		while (itr.hasNext()) {
			kWh = kWh + getKWHInRange(itr.next(), pBegTime, pEndTime);
		}
		
		return kWh;
	}
		
	
	/* (non-Javadoc)
	 * @see com.helio.app.serviceapi.CoreServices#getLocationKwhDateRangeSlices(java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<KWHUsageKPI> getAllocationKwhDateRangeSlices(Long pAllocationId,
			Float pTimeSliceInterval, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		return getHelioKPIDateRangeSlices(ContextTypeEnum.ALLOCATION, pAllocationId,
				pTimeSliceInterval, begTimestamp, endTimestamp);
	}

	public List<KWHUsageKPI> getBuildingKwhDateRangeSlices(Long pBuildingId,
			Float pTimeSliceInterval, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		return getHelioKPIDateRangeSlices(ContextTypeEnum.BUILDING, pBuildingId,
				pTimeSliceInterval, begTimestamp, endTimestamp);
	}

	public List<KWHUsageKPI> getLocationKwhDateRangeSlices(Long pLocationId,
			Float pTimeSliceInterval, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		return getHelioKPIDateRangeSlices(ContextTypeEnum.LOCATION, pLocationId,
				pTimeSliceInterval, begTimestamp, endTimestamp);
	}

	public List<KWHUsageKPI> getDivisionKwhDateRangeSlices(Long pDivisionId,
			Float pTimeSliceInterval, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		return getHelioKPIDateRangeSlices(ContextTypeEnum.DIVISION, pDivisionId,
				pTimeSliceInterval, begTimestamp, endTimestamp);
	}

	/*
	 * Return a list of KPIs for a series of interval time slices defined by a context and 
	 * a range of time stamps.
	 */
	private List<KWHUsageKPI> getHelioKPIDateRangeSlices(ContextTypeEnum pContextTypeEnum, Long pId,
			Float pTimeSliceInterval, Timestamp pBegTime, Timestamp pEndTime) 
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		/*
		 * Normalize all variables to longs. The float interval represents seconds. 
		 */
		long begTime = pBegTime.getTime();
		long endTime = pEndTime.getTime();
		long interval = (long) (Float.valueOf(pTimeSliceInterval) * 1000);
		long nextTime = begTime+interval;

		ArrayList<KWHUsageKPI>  kpiList = new ArrayList<KWHUsageKPI>();
		/*
		 * Get a KPI for a particular interval for a building allocation and add it to the list.
		 * In effect this gets the kWh for each associated monitor for the time slice and adds
		 * it to a total for the building allocation.
		 */
		while (nextTime <= endTime) {
			Timestamp begTimestamp = new Timestamp(begTime);
			Timestamp endTimestamp = new Timestamp(nextTime);
			/*
			 * Get the kWhs for the time range and context
			 */
			KWHUsageKPI kpi = null;
			switch (pContextTypeEnum) {
			case ALLOCATION:
				kpi = getAllocationKwhDateRange(pId, begTimestamp, endTimestamp); break;
			case BUILDING:
				kpi = getBuildingKwhDateRange(pId, begTimestamp, endTimestamp); break;
			case LOCATION:
				kpi = getLocationKwhDateRange(pId, begTimestamp, endTimestamp); break;
			case DIVISION:
				kpi = getDivisionKwhDateRange(pId, begTimestamp, endTimestamp); break;
			}
			/*
			 * Create a KPI for the kWh value and add it to the list
			 */
			kpiList.add(kpi);
			/*
			 * Calculate the next time slice interval
			 */
			begTime = nextTime;
			nextTime = begTime+interval;
		}

		return kpiList;
	}
	
	/*
	 * Given a Context (level) and unique id for an object at that context level 
	 * this obtains a ContextHolder objects which is needed to ensure the 
	 * HelioKPI context is clear.
	 */
	private ContextHolder getHierarchyContext(ContextTypeEnum pType, Long pId) 
	throws HelioInvalidIdException 
	{
		ContextHolder context =  null;
		try {
			context = CoreServicesDAO.getInstance().getHierarchyContext(pType, pId);
		}
		catch (DAOException ex) {
			throw new HelioInvalidIdException(ex);
		}		
		return context;
	}

	/**
	 * Calculates two energy consumption KPI Lists for a set of time periods by time slice intervals.
	 * The earlier period contains min, max and standard deviation for each interval. 
	 * 
	 * OUTPUT: For each time slice interval:
	 * 1) kWh consumed
	 * 2) the max kWh recorded for the specified "precedingDays" interval
	 * 3) the min kWh recorded for the specified precedingDays interval
	 * 4) the standard deviation for the specified precedingDays interval  
	 * 
	 * @param pDeviceMonitorId: primary key of device monitor.
	 * @param pTimeSliceInterval: number of seconds that kWh is to be calculated for.
	 * @param pPrecedingDaysInterval: number of days prior to the pBegDate with which to determine
	 * standard deviation.
	 * @param pBegDate: Beginning timestamp for the desired packet list for kwh computation.
	 * @param pEndDate Ending timestamp for the desired packet list for kwh computation.
	 * @return A two dimensional array of HelioKPI List object. The first represents a List of KWH HelioKPI objects.
	 * The second is a list of SDV HelioKPI objects. 
	 * @throws HelioInvalidIdException if the provided DeviceMonitorId is not expressed in a valid UUID
	 * @throws HelioMissingDeviceMonitorException if the provided DeviceMonitorId does not exist in the database.
	 * @throws HelioInvalidPacketException if the provided DeviceMonitorId have the requested packets.
	 */
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriod(
			Long pDeviceMonitorId,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Date pBegDT, Date pEndDT)
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDT.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDT.getTime());
		List<Long> monitorIds = new ArrayList<Long>();
		monitorIds.add(pDeviceMonitorId);
		ContextHolder context 	= getHierarchyContext(ContextTypeEnum.MONITOR, pDeviceMonitorId);		
		return getKwhDateRangeSliceDeviations(
				monitorIds, 	
				pTimeSliceInterval, 
				pPrecedingDaysInterval,
				begTimestamp, 
				endTimestamp, 
				context);
	}
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByDivision(
			Long pDivisionId,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Date pBegDT, Date pEndDT)
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDT.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDT.getTime());

		ContextHolder context = getHierarchyContext(ContextTypeEnum.DIVISION, pDivisionId);	
		List<Long> monitorIds = getMonitorsForContext(context.getType(), pDivisionId);
		return getKwhDateRangeSliceDeviations(
				monitorIds, 	
				pTimeSliceInterval, 
				pPrecedingDaysInterval,
				begTimestamp, 
				endTimestamp, 
				context);
	}
	
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByLocation(
			Long pLocationId,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Date pBegDT, Date pEndDT)
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDT.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDT.getTime());

		ContextHolder context = getHierarchyContext(ContextTypeEnum.LOCATION, pLocationId);	
		List<Long> monitorIds = getMonitorsForContext(context.getType(), pLocationId);
		return getKwhDateRangeSliceDeviations(
				monitorIds, 	
				pTimeSliceInterval, 
				pPrecedingDaysInterval,
				begTimestamp, 
				endTimestamp, 
				context);
	}
	
	public List<StandardDeviationKPI> getDeviationForSlicesInPeriodByBuilding(
			Long pBuildingId,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Date pBegDT, Date pEndDT)
	throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException
	{
		Timestamp begTimestamp = new Timestamp(pBegDT.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDT.getTime());

		ContextHolder context = getHierarchyContext(ContextTypeEnum.BUILDING, pBuildingId);	
		List<Long> monitorIds = getMonitorsForContext(context.getType(), pBuildingId);
		return getKwhDateRangeSliceDeviations(
				monitorIds, 	
				pTimeSliceInterval, 
				pPrecedingDaysInterval,
				begTimestamp, 
				endTimestamp, 
				context);
	}
	
	private List<StandardDeviationKPI> getKwhDateRangeSliceDeviations(
			List<Long> pDeviceMonitorIds,
			Float pTimeSliceInterval,
			Float pPrecedingDaysInterval,
			Timestamp pBegDT, Timestamp pEndDT,
			ContextHolder pContext) 
			throws HelioInvalidIdException, HelioMissingDeviceMonitorException, HelioInvalidPacketException 
	{
		/*
		 * The previous date range is indicated by pPrecedingDaysInterval
		 */
		long oneDay = 1000 * 60 * 60 * 24;
		/*
		 *	The preceding days in milliseconds 
		 */
		long precedingDaysMs = pPrecedingDaysInterval.longValue() * oneDay;
		/*
		 * Period Milliseconds is used to calculate number of slices
		 */
		long periodMs = (((Timestamp) pEndDT).getTime() - ((Timestamp) pBegDT).getTime());
		/*
		 * Time slice interval in milliseconds
		 */
		long sliceMs = pTimeSliceInterval.longValue() * 1000;
		/*
		 * Slices in period is the number of divisions (or slices) in the range
		 */
		long slicesInPeriod = periodMs / sliceMs;

		/*
		 * determine the range of the first slice for the preceding interval prior
		 * to looping through the entire range 
		 */

		ArrayList<StandardDeviationKPI> standardDeviations = new ArrayList<StandardDeviationKPI>();
		KPIRules 			rules 		= KPIRules.getInstance();
		RuleRequest 		request 	= new RuleRequest();
		
		/*
		 * The outer loop sets the time slice within the period such that
		 * the inner loop can accumulate packets for that slice for each period
		 */
		for (long slice=0; slice<slicesInPeriod; slice++) {
			
			long sliceOffset = slice * sliceMs;
			long begTime = ((Timestamp) pBegDT).getTime() - (precedingDaysMs + sliceOffset);
			long endTime = begTime+sliceMs;
			long begDateSlice = pBegDT.getTime() + sliceOffset;

			List<List<DevicePacket>> listOfPacketLists= new ArrayList<List<DevicePacket>>();
			
			/*
			 * Get packets for a time slice interval for all periods
			 */
			while (begTime <= begDateSlice+100) { // 100 is for rounding errors in slice calc
				Timestamp begTimestamp = new Timestamp(begTime);
				Timestamp endTimestamp = new Timestamp(endTime);
				
				try {
					listOfPacketLists.add((List<DevicePacket>)CoreServicesDAO.getInstance().
							getPacketsForMonitorsInRange(pDeviceMonitorIds, begTimestamp, endTimestamp));
				}
				catch (DAOException ex) {
					throw new HelioInvalidIdException(ex);
				}
				begTime = begTime + periodMs;
				endTime = begTime + sliceMs;
			}
			
			/*
			 * Now request standard deviation for the time slice be calculated.
			 */
			request.setPacketLists(listOfPacketLists);
			request.setContext(pContext);
			try {
				standardDeviations.add(rules.calculateStandardDeviation(request));
			}
			catch (RulesServiceException ex) {
				throw new HelioInvalidPacketException(ex);
			}
		}
		
		return standardDeviations;
	}
	
	
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
	 * @throws HelioInvalidKPIException 
	 */
	public EnergyPerCasesHandledKPI getEnergyPerCasesHandledPerLocation(Long pLocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		/*
		 * gets the kwh for the location and date range (period for Safeway)
		 */
		KWHUsageKPI 	kwhKPI 		= getLocationKwhDateRange(pLocationId, begTimestamp, endTimestamp);
		Location 		location 	= getLocation(pLocationId);
		int casesHandled 			= getCasesHandledForLocation(location,begTimestamp, endTimestamp);
		if (casesHandled <= 0) {
			throw new HelioMissingMetricException("Cases Handled not found for location/time=" + pLocationId + "/" + begTimestamp);
		}
		
		/*
		 * Calculate and construct KPI 
		 */
		float 			energyPerCH = kwhKPI.getKpiValue() / casesHandled;
		try {
			EnergyPerCasesHandledKPI kpi = new EnergyPerCasesHandledKPI(kwhKPI.getContext(), energyPerCH, kwhKPI);
			return kpi;
		}
		catch(HelioInvalidKPIException ex) {
			throw new HelioMissingMetricException(ex);
		}
	}
	/**
	 * Given a building and period dates, this returns a KPI of the total energy per cases handled 
	 * NOTE: The cases handled are stored by period, however only the first cases handled value in 
	 * the passed date range will be retrieved.  
	 * 
	 * @param pBuildingId of the building
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a KCH HelioKPI representing the energy used per case handled.
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyPerCasesHandledKPI getEnergyPerCasesHandledPerBuilding(Long pBuildingId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		/*
		 * gets the kwh for the location and date range (period for Safeway)
		 */
		KWHUsageKPI 	kwhKPI 		= getBuildingKwhDateRange(pBuildingId, begTimestamp, endTimestamp);
		Building 		building 	= getBuilding(pBuildingId);
		int casesHandled 			= getCasesHandledForBuilding(building,begTimestamp, endTimestamp);
		if (casesHandled <= 0) {
			throw new HelioMissingMetricException("Cases Handled not found for location/time=" + pBuildingId + "/" + begTimestamp);
		}
		
		/*
		 * Calculate and construct KPI 
		 */
		float 			energyPerCH = kwhKPI.getKpiValue() / casesHandled;
		try {
			EnergyPerCasesHandledKPI kpi = new EnergyPerCasesHandledKPI(kwhKPI.getContext(), energyPerCH, kwhKPI);
			return kpi;
		}
		catch(HelioInvalidKPIException ex) {
			throw new HelioMissingMetricException(ex);
		}
	}

	/**
	 * Add up all the cases handled metrics for the buildings in a location by date range. 
	 * @param pLocation
	 * @param pBegTime
	 * @param pEndTime
	 * @return
	 */
 	private int getCasesHandledForLocation(Location pLocation, Timestamp pBegTime, Timestamp pEndTime ) {
		int casesHandled = 0;
		
		/*
		 * Add up the cases handled for all Buildings at Location
		 */
		List<Building> buildings = pLocation.getBuildings();
		Iterator<Building> buildingItr = buildings.iterator();
		while (buildingItr.hasNext()) {
			Building building = buildingItr.next();
			List<BoomerMetric> metrics = building.getMetrics();
			casesHandled += countCasesHandledForPeriod(metrics, pBegTime, pEndTime);
		}
		/*
		 * If no cases handled found at building, try location
		 */
		if (casesHandled == 0) {
			List<BoomerMetric> metrics = pLocation.getMetrics();
			casesHandled = countCasesHandledForPeriod(metrics, pBegTime, pEndTime);
		}
		
		return casesHandled;
	}
		
 	private int getCasesHandledForBuilding(Building pBuilding, Timestamp pBegTime, Timestamp pEndTime ) {
		List<BoomerMetric> metrics 		= pBuilding.getMetrics();
		int casesHandled 				= countCasesHandledForPeriod(metrics, pBegTime, pEndTime);
		return casesHandled;
 	}
 	
	private int countCasesHandledForPeriod(List<BoomerMetric> pMetrics, Timestamp pBegTime, Timestamp pEndTime) {
		int casesHandled = 0;
		
		Iterator<BoomerMetric> metricItr = pMetrics.iterator();
		while (metricItr.hasNext()) {
			BoomerMetric metric = metricItr.next();
			if (metric.getBegDate().compareTo(pBegTime) <= 0
			&&  metric.getEndDate().compareTo(pBegTime) >= 0) {
				if (metric.getMetricType() == MetricTypeEnum.CASESHANDLED) {
					casesHandled += Integer.valueOf(metric.getMetricStr());
				}
			}
		}
		return casesHandled;
	}

 	private float getCostAllocationForLocation(Location pLocation, Timestamp pBegTime, Timestamp pEndTime ) {
		float costAllocation = 0;
		
		/*
		 * If no cases handled found at building, try location
		 */
		if (costAllocation == 0) {
			List<BoomerMetric> metrics = pLocation.getMetrics();
			costAllocation = countCostAllocationForPeriod(metrics, pBegTime, pEndTime);
		}
		
		return costAllocation;
	}
 	
	private float countCostAllocationForPeriod(List<BoomerMetric> pMetrics, Timestamp pBegTime, Timestamp pEndTime) {
		float costAllocation = 0f;
		
		Iterator<BoomerMetric> metricItr = pMetrics.iterator();
		while (metricItr.hasNext()) {
			BoomerMetric metric = metricItr.next();
			if (metric.getBegDate().compareTo(pBegTime) <= 0
			&&  metric.getEndDate().compareTo(pBegTime) >= 0) {
				if (metric.getMetricType() == MetricTypeEnum.COSTALLOCATION) {
					costAllocation += Float.valueOf(metric.getMetricStr());
				}
			}
		}
		return costAllocation;
	}


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
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		
		/*
		 * gets the kwh for the location and date range (period for Safeway)
		 */
		KWHUsageKPI 	kwhKPI 			= (KWHUsageKPI)getLocationKwhDateRange(pLocationId, begTimestamp, endTimestamp);
		Location 		location 		= getLocation(pLocationId);
		int 			casesHandled 	= getCasesHandledForLocation(location,begTimestamp, endTimestamp);
		float costAllocation 			= getCostAllocationForLocation(location, begTimestamp, begTimestamp );
		if (costAllocation <= 0f) {
			throw new HelioMissingMetricException("Cost Allocation not found for location/time=" + pLocationId + "/" + begTimestamp);
		}
		
		/*
		 * Calculate and construct KPI 
		 */
		float costofEnergyPerCasesHandled = (kwhKPI.getKpiValue() / casesHandled) * costAllocation;
		ContextHolder 	context 		  = getHierarchyContext(ContextTypeEnum.LOCATION, pLocationId);
		try {
			EnergyCostPerCasesHandledKPI kpi = new EnergyCostPerCasesHandledKPI(context, costofEnergyPerCasesHandled, kwhKPI);
			return kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}


	/**
	 * Given a building and period dates, this returns a KPI of the energy cost per cases handled 
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
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		
		/*
		 * gets the kwh for the location and date range (period for Safeway)
		 */
		KWHUsageKPI 	kwhKPI 			= (KWHUsageKPI)getBuildingKwhDateRange(pBuildingId, pBegDate, pEndDate);
		ContextHolder 	context  		= kwhKPI.getContext();
		Location 		location 		= getLocation(context.getLocationId());
		Building		building 		= getBuildingFromLocation(location, pBuildingId);
		List<BoomerMetric> metrics 		= building.getMetrics();
		int casesHandled 				= countCasesHandledForPeriod(metrics, begTimestamp, endTimestamp);
		
		float costAllocation	 		= getCostAllocationForLocation(location, begTimestamp, begTimestamp );
		if (costAllocation <= 0f) {
			throw new HelioMissingMetricException("Cost Allocation not found for location/time=" + context.getLocationId() + "/" + begTimestamp);
		}
		
		/*
		 * Calculate and construct KPI 
		 */
		float costofEnergyPerCasesHandled = (kwhKPI.getKpiValue() / casesHandled) * costAllocation;
		try {
			EnergyCostPerCasesHandledKPI kpi = new EnergyCostPerCasesHandledKPI(context, costofEnergyPerCasesHandled, kwhKPI);
			return kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}
	private Building getBuildingFromLocation(Location pLocation, Long pBuildingId) 
	{
		List<Building>  buildings		= pLocation.getBuildings();
		Iterator<Building> buildingItr  = buildings.iterator();
		Building 		building 		= null;
		while (buildingItr.hasNext()) {
			building = buildingItr.next();
			if (building.getId() == pBuildingId) 
				break;
		}
		return building;
	}

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
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		Location 	location 	= getLocation(pLocationId);
		KWHUsageKPI kwhKPI 		= (KWHUsageKPI)getLocationKwhDateRange(pLocationId, pBegDate, pEndDate);
		/*
		 * Calculate and construct KPI 
		 */
		
		float 			energyPerSqft 	= kwhKPI.getKpiValue()  / location.getSquareFeet();
		try {
			EnergyPerSquareFootKPI kpi 	= new EnergyPerSquareFootKPI(kwhKPI.getContext(), energyPerSqft, kwhKPI);
			return 	kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}

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
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		Building 	building 	= getBuilding(pBuildingId);
		KWHUsageKPI kwhKPI 		= (KWHUsageKPI)getBuildingKwhDateRange(pBuildingId, pBegDate, pEndDate);
		/*
		 * Calculate and construct KPI 
		 */
		float 	energyPerSqft 	= kwhKPI.getKpiValue()  / building.getSquareFeet();
		try {
			EnergyPerSquareFootKPI kpi = new EnergyPerSquareFootKPI(kwhKPI.getContext(), energyPerSqft, kwhKPI);
			return 	kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}

	/**
	 * Given a location and period dates, this returns a KPI of the energy cost location 
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
	public EnergyCostAllocationKPI getEnergyCostPerLocation(Long pLocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		Timestamp begTimestamp = new Timestamp(pBegDate.getTime());
		Timestamp endTimestamp = new Timestamp(pEndDate.getTime());
		
		/*
		 * gets the kwh for the location and date range (period for Safeway)
		 */
		KWHUsageKPI		kwhKPI			= getKWHForDateRange(ContextTypeEnum.LOCATION, pLocationId, begTimestamp, endTimestamp);
		Location 		location 		= getLocation(pLocationId);
		float costAllocation 			= getCostAllocationForLocation(location, begTimestamp, begTimestamp );
		if (costAllocation <= 0f) {
			throw new HelioMissingMetricException("Cost Allocation not found for location/time=" + pLocationId + "/" + begTimestamp);
		}
		
		/*
		 * Calculate and construct KPI 
		 */
		float costOfEnergyPerLocation 	= kwhKPI.getKpiValue() * costAllocation;
		try {
			EnergyCostAllocationKPI	kpi = new EnergyCostAllocationKPI(kwhKPI.getContext(), costOfEnergyPerLocation, kwhKPI);
			return kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}

	/**
	 * Given a location and period dates, this returns a KPI of the energy cost location per Sqft
	 * NOTE: The cases handled and cost are stored by period, however only the metric value in 
	 * the passed date range will be retrieved.  
	 * 
	 * @param pLocationId of the location
	 * @param pBegDate of the period
	 * @param pEndDate of the period
	 * @return a HelioKPI representing the cost of energy used per case handled.
	 * @throws HelioInvalidIdException
	 * @throws HelioMissingMetricException
	 * @throws HelioMissingDeviceMonitorException
	 * @throws HelioInvalidPacketException
	 */
	public EnergyCostPerSquareFootKPI getEnergyCostPerLocationPerSquareFoot(Long pLocationId, Date pBegDate, Date pEndDate) 
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		EnergyCostAllocationKPI energyCostKPI = (EnergyCostAllocationKPI)getEnergyCostPerLocation(pLocationId, pBegDate, pEndDate);
		Location location = getLocation(pLocationId);
		/*
		 * Calculate and construct KPI 
		 */
		float costOfEnergyPerSqft = energyCostKPI.getKpiValue() / location.getSquareFeet();
		try {
			EnergyCostPerSquareFootKPI kpi 	= new EnergyCostPerSquareFootKPI(energyCostKPI.getContext(), costOfEnergyPerSqft, energyCostKPI);
			return kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}


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
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		EnergyCostAllocationKPI energyCostKPI = getEnergyCostPerBuilding(pBuildingId, pBegDate, pEndDate);
		Building building = getBuilding(pBuildingId);
		
		/*
		 * Calculate and construct KPI 
		 */
		float 	costOfEnergyPerSqft = energyCostKPI.getKpiValue() / building.getSquareFeet();
		try {
			EnergyCostPerSquareFootKPI kpi = new EnergyCostPerSquareFootKPI(energyCostKPI.getContext(), costOfEnergyPerSqft, energyCostKPI);
			return kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioInvalidIdException(ex);
		}
	}
	
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
	throws HelioInvalidIdException, HelioMissingMetricException, HelioInvalidPacketException, HelioMissingDeviceMonitorException
	{
		Timestamp 		begTimestamp	= new Timestamp(pBegDate.getTime());
		KWHUsageKPI 	kpiKWH 			= (KWHUsageKPI)getBuildingKwhDateRange(pBuildingId, pBegDate, pEndDate);
		ContextHolder 	context 		= getHierarchyContext(ContextTypeEnum.BUILDING, pBuildingId);
		Long 			locationId 		= context.getLocationId();		
		Location 		location 		= getLocation(locationId);
		
		float 			costAllocation 	= getCostAllocationForLocation(location, begTimestamp, begTimestamp );
		if (costAllocation <= 0f) {
			throw new HelioMissingMetricException("Cost Allocation not found for location/time=" + locationId + "/" + begTimestamp);
		}
		float costOfEnergyPerBuilding 	= kpiKWH.getKpiValue()  * costAllocation;
		try {
			EnergyCostAllocationKPI kpi = new EnergyCostAllocationKPI(context,costOfEnergyPerBuilding,kpiKWH);
			return kpi;
		}
		catch (HelioInvalidKPIException ex) {
			throw new HelioMissingMetricException(ex);
		}
	}
	
	private Building getBuilding(Long pBuildingId) throws HelioInvalidIdException {
		
		Building building 	= null;
		try {
			building = CoreServicesDAO.getInstance().getBuildingFromId(pBuildingId);
		}
		catch (DAOException ex) {
			throw new HelioInvalidIdException(ex);
		}
		return building;
	}
	
	private Location getLocation(Long pLocationId) throws HelioInvalidIdException {
		
		Location location 	= null;
		try {
			location = CoreServicesDAO.getInstance().getLocationFromId(pLocationId);
		}
		catch (DAOException ex) {
			throw new HelioInvalidIdException(ex);
		}
		return location;
	}
	
}
