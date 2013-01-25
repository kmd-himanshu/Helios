package com.helio.app.boomer.common.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.helio.app.boomer.common.dal.model.BoomerMetric;
import com.helio.app.boomer.common.dal.model.Building;
import com.helio.app.boomer.common.dal.model.Client;
import com.helio.app.boomer.common.dal.model.ContextHolder;
import com.helio.app.boomer.common.dal.model.ContextTypeEnum;
import com.helio.app.boomer.common.dal.model.DeviceModel;
import com.helio.app.boomer.common.dal.model.DeviceMonitor;
import com.helio.app.boomer.common.dal.model.DevicePacket;
import com.helio.app.boomer.common.dal.model.Division;
import com.helio.app.boomer.common.dal.model.Location;
import com.helio.app.boomer.common.dal.model.MetricTypeEnum;
import com.helio.app.boomer.common.dal.model.MonitorTypeEnum;
import com.helio.app.boomer.common.dal.model.PacketValue;

/**
 * Data Access Object for Core Services
 * This is a singleton. 
 * 
 * @author rickschwartz
 *
 */
public class CoreServicesDAO extends DataAccessObject {

	/*
	 * singleton instance of this object
	 */
	private static CoreServicesDAO instance = null;
	/*
	 * Cache store for values retrieved by this DAO
	 */
	private static HashMap<String, Object> cache = new HashMap<String, Object>();
	/*
	 * Commons logging 
	 */
	private static Log LOG = LogFactory.getLog("CoreServicesDAO");

	/*
	 * enforces singleton
	 */
	private CoreServicesDAO() {
		super();
	}

	/**
	 * Gets a singleton of the DAO
	 * @return singleton instance of this DAO
	 */
	public synchronized static CoreServicesDAO getInstance() 
	{
		if (instance == null) {
			instance = new CoreServicesDAO();
		}
		return instance;
	}
	
	/**
	 * This takes a result set and converts it to a DevicePacket.
	 * @param pDeviceMonitor: Parent object to be added to packet
	 * @param pResultSet: contains the packet info
	 * @param pConn: database connection
	 * @return a fully populated DevicePacket with associated meter readings. 
	 * @throws Exception
	 */
	private DevicePacket getDevicePacketFromResultSet(
			ResultSet pResultSet,
			Connection pConn)  throws Exception
	{
		PreparedStatement stmt = null;
		
		DevicePacket devicePacket = new DevicePacket();
		
		devicePacket.setId(pResultSet.getLong(1));
		devicePacket.setPacketDate(pResultSet.getTimestamp(2));
		devicePacket.setVersion(pResultSet.getInt(3));
		devicePacket.setMonitorId(pResultSet.getLong(4));
		devicePacket.setDeviceMonitor(getMonitor(devicePacket.getMonitorId()));
		
		stmt = pConn.prepareStatement(getProperty("getPacketValuesSQL"));
        stmt.setLong(1, devicePacket.getId());
        ResultSet rs = stmt.executeQuery();
        
        if (rs != null) {
        	while (rs.next()) {
        		PacketValue packetValue = new PacketValue();
        		packetValue.setName(rs.getString(1));
        		packetValue.setStrValue(rs.getString(2));
        		packetValue.setFieldType(rs.getInt(3));
        		packetValue.setVersion(rs.getInt(4));
        		devicePacket.addPacketValue(packetValue);
        	}
        }
    	else {
    		throw new DAOException("ERROR: Field Values not available for packet=" + devicePacket.getId());
    	}
        return devicePacket;
	}

	/**
	 * Returns a collection of packets in a date range. As of this writing, the packets are 
	 * persisted each minute, so the number of DevicePackets returned will be 1440 for a single day. 
	 * Caution is urged in use of the date range. 
	 *
	 * @param pMonitorId: the unique key of the device monitor
	 * @param pBegTime: the beginning of the time stamp range. 
	 * @param pEndTime: the end of the time stamp range. 
	 * @return a collection of devicePackets
	 */
	@SuppressWarnings("unchecked")
	public Collection<DevicePacket> getPacketsForMonitorsInRange(List<Long> pMonitorIds, Timestamp pBegTime, Timestamp pEndTime )
		throws DAOException
	{
		String sql = getProperty("getPacketsForMonitorsInRangeSQL");
		String inClause = pMonitorIds.get(0).toString();
		for (int i = 1; i < pMonitorIds.size(); i++) {
			inClause = inClause + "," + pMonitorIds.get(i).toString();
		}
		
		String cacheKey = "PacketsForMonitorsInRange|"+inClause+"|"+pBegTime+"|"+pEndTime;
		Object packetCollectionObj = (Object) cache.get(cacheKey);
		if (!(packetCollectionObj==null)) {
			return (Collection<DevicePacket>)packetCollectionObj;
		}
		
		sql = sql.replaceFirst("REPLACE",inClause);
	    Connection conn = null;
        PreparedStatement stmt = null;
    	ResultSet rs = null;
    	Collection<DevicePacket> packets = new ArrayList<DevicePacket>();
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, pBegTime);
            stmt.setTimestamp(2, pEndTime);
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		DevicePacket devicePacket = getDevicePacketFromResultSet(rs,conn);
            		packets.add(devicePacket);
            	}
            }
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        /*
         * save results to cache so next call will not use DB
         */
    	cache.put(cacheKey, packets);
		return packets;
	}
	
	/**
	 * Returns a collection of packets in a date range. As of this writing, the packets are 
	 * persisted each minute, so the number of DevicePackets returned will be 1440 for a single day. 
	 * Caution is urged in use of the date range. 
	 *
	 * @param pMonitorId: the unique key of the device monitor
	 * @param pBegTime: the beginning of the time stamp range. 
	 * @param pEndTime: the end of the time stamp range. 
	 * @return a collection of devicePackets
	 */
	public Collection<DevicePacket> getPacketsForMonitorInRange(long pMonitorId, Timestamp pBegTime, Timestamp pEndTime )
		throws DAOException
	{
        Connection conn = null;
        PreparedStatement stmt = null;
    	ResultSet rs = null;
    	Collection<DevicePacket> packets = new ArrayList<DevicePacket>();
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(getProperty("getPacketsForMonitorInRangeSQL"));
            stmt.setLong(1, pMonitorId);
            stmt.setTimestamp(2, pBegTime);
            stmt.setTimestamp(3, pEndTime);
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		DevicePacket devicePacket = getDevicePacketFromResultSet(rs,conn);
            		packets.add(devicePacket);
            	}
            }
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
		return packets;
	}
	/**
	 * Retrieves a DevicePacket for a monitor and time
	 * @param pMonitorId: unique id of the monitor
	 * @param pTimestamp: time of requested packet.
	 * @param pNext: boolean indicating whether to get the first DevicePacket after (true) or 
	 * 				 the previous before (false) the passed Timestamp 
	 * @return DevicePacket for request 
	 * @throws DAOException if any errors in retrieving requested packet
	 */
	public DevicePacket getMonitorPacket(long pMonitorId, Timestamp pTimestamp, boolean pNext )
		throws DAOException
	{		
		DevicePacket devicePacket = null;
        Connection conn = null;
        PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
			if (pNext) {
	            stmt = conn.prepareStatement(getProperty("getNextPacketForMonitorAndDateSQL"));
			}
			else {
	            stmt = conn.prepareStatement(getProperty("getPriorPacketForMonitorAndDateSQL"));
			}
            stmt.setLong(1, pMonitorId);
            stmt.setTimestamp(2, pTimestamp);
//			long startTime = System.currentTimeMillis();
            rs = stmt.executeQuery();
//			long elapsedTime = System.currentTimeMillis() - startTime;
//			System.out.println("Elapsed time for getNextPacket in milliseconds="+elapsedTime);
            if (rs != null) {
            	if (rs.next()) {
            		devicePacket = getDevicePacketFromResultSet(rs,conn);
            	}
            	else {
            		throw new DAOException("ERROR: Packet not retreived for monitorId/Timestamp=" +pMonitorId+"/"+pTimestamp);
            	}
            }
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
		return devicePacket;

	}
	
	/**
	 * Retrieves the most recent DevicePacket for a monitor
	 * @param pMonitorId: unique id of the monitor
	 * @return DevicePacket for request 
	 * @throws DAOException if any errors in retrieving requested packet
	 */
	public DevicePacket getCurrentMonitorPacket(long pMonitorId)
		throws DAOException
	{
		DevicePacket devicePacket = null;
        Connection conn = null;
        PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
	        stmt = conn.prepareStatement(getProperty("getMaxPacketForMonitorSQL"));
            stmt.setLong(1, pMonitorId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		devicePacket = getDevicePacketFromResultSet(rs,conn);
            	}
            	else {
            		throw new DAOException("ERROR: Packet not retreived for monitorId =" +pMonitorId);
            	}
            }
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
		return devicePacket;

	}
	/**
	 * Retrieves a monitor and it's associated model
	 * @param pMonitorId: unique identifier for monitor
	 * @return DeviceMonitor object with it's embedded DeviceModel
	 * @throws DAOException when cannot retrieve the model
 	 */
	private DeviceMonitor getMonitor(long pMonitorId ) 
		throws DAOException 
	{
		LOG.debug("getMonitor " + pMonitorId);
		/*
		 * Check cache first to reduce I/O
		 */
		Object deviceMonitorObj = (Object) cache.get("DeviceMonitorCS-Persist"+pMonitorId);
		if (!(deviceMonitorObj==null)) {
			return (DeviceMonitor)deviceMonitorObj;
		}
		
		DeviceMonitor 		deviceMonitor 	= null;
		DeviceModel 		deviceModel 	= null;
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the monitor 
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(getProperty("getMonitorByIdSQL"));
            stmt.setLong(1, pMonitorId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		deviceMonitor = getDeviceMonitorFromResultSet(rs);
            		/*
            		 * If we have a proxy meter, go get the original
            		 */
            		if (deviceMonitor.getMonitorType() == MonitorTypeEnum.PULSEMETERPROXY) {
                        stmt = conn.prepareStatement(getProperty("getMonitorIdForProxyIdSQL"));
                        stmt.setLong(1, pMonitorId);
                        rs = stmt.executeQuery();
                        if (rs != null) {
                        	if (rs.next()) {
                        		Long monitorId = rs.getLong(1);
                                stmt = conn.prepareStatement(getProperty("getMonitorByIdSQL"));
                                stmt.setLong(1, monitorId);
                                rs = stmt.executeQuery();
                                if (rs != null) {
                                	if (rs.next()) {
                                		DeviceMonitor origMonitor = getDeviceMonitorFromResultSet(rs);
                                		origMonitor.setProxyRole(pMonitorId);
                                		origMonitor.setPulseConstant(deviceMonitor.getPulseConstant());
                                		deviceMonitor = origMonitor;
                                	}
                                }
                        	}
                        }                        
            		}
               		/*
               		 * Get the related model
               		 */
                    stmt = conn.prepareStatement(getProperty("getModelForMonitorSQL"));
                    stmt.setLong(1, pMonitorId);
                    rs = stmt.executeQuery();
                    if (rs != null) {
                    	if (rs.next()) {
                    		deviceModel = new DeviceModel();
                    		deviceModel.setDeviceUse(rs.getInt(1));
                    		deviceModel.setModelName(rs.getString(2));
                    		deviceModel.setVersion(rs.getInt(3));
                    		deviceModel.setUtilityGrade(rs.getBoolean(4));
                    		deviceMonitor.setDeviceModel(deviceModel);
                    	}
                	}
                	else {
                		throw new DAOException("ERROR: DeviceModel not retreived for monitorId=" + pMonitorId );
                	}
            	}
        	}
        	else {
        		throw new DAOException("ERROR: DeviceMonitor not retreived for monitorId=" + pMonitorId );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        /*
         * save results to cache so next call will not use DB
         */
    	cache.put("DeviceMonitorCS-Persist"+pMonitorId, deviceMonitor);
		return deviceMonitor;
	}

	/**
	 * Given a type and Id will return a ContextHolder which is used in 
	 * HelioKPI to determine the hierarchical context of the KPI
	 * @param pType: the type of context object requested
	 * @param pId: the id for the corresponding level in the hierarchy
	 * @return a ContextHolder object
	 * @throws DAOException: when the request is invalid or the context could not be 
	 * obtained. 
	 */
	public ContextHolder getHierarchyContext(ContextTypeEnum pType, long pId) 
	throws DAOException
	{
		switch (pType) {
		
		case MONITOR: return getMonitorContext(pId);
		case ALLOCATION: return getAllocationContext(pId);
		case BUILDING: return getBuildingContext(pId);
		case LOCATION: return getLocationContext(pId);
		case DIVISION: return getDivisionContext(pId);
		default: throw new DAOException("Cannot create hierarchy for unknown context: id=" + pId);
		}
	}
	/*
	 * If the KPI id monitor based then zero out hierarchy
	 */
	private ContextHolder getMonitorContext(long pMonitorId) 
	throws DAOException
	{	
		ContextHolder context = null;
		try {
			context = new ContextHolder(pMonitorId, 0L, 0L, 0L, 0L);
		}
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
		return context;
	}
	/*
	 * the following methods are helpers which select the appropriate SQL statement
	 */
	private ContextHolder getAllocationContext(long pAllocationId) 
	throws DAOException
	{
		return getHierarchyContext(pAllocationId, "getParentHierarchyFromAllocationIdSQL");
	}
	private ContextHolder getBuildingContext(long pBuildingId) 
	throws DAOException
	{
		return getHierarchyContext(pBuildingId, "getParentHierarchyFromBuildingIdSQL");
	}
	private ContextHolder getLocationContext(long pLocationId) 
	throws DAOException
	{
		return getHierarchyContext(pLocationId, "getParentHierarchyFromLocationIdSQL");
	}
	private ContextHolder getDivisionContext(long pDivisionId) 
	throws DAOException
	{
		return getHierarchyContext(pDivisionId, "getParentHierarchyFromDivisionIdSQL");
	}

	/*
	 * Generic method to obtain the hierarchy context.
	 * By passing a unique id and name of an SQL property, this
	 * instantiates and returns a ContextHolder
	 */
	private ContextHolder getHierarchyContext(long pId, String pSQLName ) 
		throws DAOException
	{
		LOG.debug("getHierarchyContext " + pId + ":" + pSQLName);
		/*
		 * Check cache first to reduce I/O
		 */
		ContextHolder context = (ContextHolder) cache.get("HierarchyContext:"+pId+":"+pSQLName);
		if (!(context==null)) {
			return context;
		}
		
        Connection 			conn 	= null;
        PreparedStatement 	stmt 	= null;
    	ResultSet 			rs 		= null;
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(getProperty(pSQLName));
            stmt.setLong(1, pId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		/*
            		 * If SQL successful then instantiate a ContextHolder
            		 */
            		context = new ContextHolder(0L, rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4));
            	}
        	}
        	else {
        		throw new DAOException("ERROR: ID Hierarchy not found for id=" + pId );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
    	cache.put("HierarchyContext:"+pId+":"+pSQLName, context);
		return context;
	}
	
	/**
	 * Method to obtain cases handled for a location.
	 */
	public float getCasesHandledForLocation(long pLocationId, Timestamp pTimestamp ) 
		throws DAOException
	{
    	String metricTypeStr   = "CASES HANDLED";
    	String metricStr = getMetricForPeriod(pLocationId, pTimestamp, metricTypeStr);
    	
		return Float.valueOf(metricStr);
	}
	
	/**
	 * Method to obtain cost allocation for a location.
	 */
	public float getCostAllocationForLocation(long pLocationId, Timestamp pTimestamp ) 
		throws DAOException
	{
    	String metricTypeStr   = "ACTUAL TARIFF";
    	String metricStr = getMetricForPeriod(pLocationId, pTimestamp, metricTypeStr);
    	
		return Float.valueOf(metricStr);
	}
	
	
	/**
	 * Method to obtain a metric for a period for a location.
	 */
	private String getMetricForPeriod(long pLocationId, Timestamp pTimestamp, String metricTypeStr) 
		throws DAOException
	{
        Connection conn = null;
        PreparedStatement stmt = null;
    	ResultSet rs = null;
    	String metricValueStr = null;
    	String sqlStatementStr = getProperty("getMetricForLocationPeriodSQL");
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
	        stmt = conn.prepareStatement(sqlStatementStr);
            stmt.setTimestamp(1, pTimestamp);
            stmt.setString(2, metricTypeStr);
            stmt.setLong(3, pLocationId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		metricValueStr = rs.getString(1);
            	}
            	else {
            		throw new DAOException("ERROR: Metric not found for location/Timestamp=" +pLocationId+"/"+pTimestamp);
            	}
            }
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
		return metricValueStr;
	}
	

	/**
	 * Given a type and Id will return a ContextHolder which is used in 
	 * HelioKPI to determine the hierarchical context of the KPI
	 * @param pType: the type of context object requested
	 * @param pId: the id for the corresponding level in the hierarchy
	 * @return a ContextHolder object
	 * @throws DAOException: when the request is invalid or the context could not be 
	 * obtained. 
	 */
	public List<Long> getMonitorIds(ContextTypeEnum pType, long pId) 
	throws DAOException
	{
		List<Long> monitorIds = null;
		switch (pType) {
			case ALLOCATION:  monitorIds = getIdsByAllocation(pId); break;
			case BUILDING: monitorIds = getIdsByBuilding(pId); break;
			case LOCATION: monitorIds = getIdsByLocation(pId); break;
			case DIVISION: monitorIds = getIdsByDivision(pId); break;
			default: throw new DAOException("Cannot retrieve IDs for unknown context: id=" + pId);
		}
		return deDupIds(monitorIds);
	}
	/*
	 * The following methods get the monitor ids associated with that level
	 * in the hierarchy, and then recursively get monitor ids that may be 
	 * associated with children of that level. 
	 */
	private List<Long> getIdsByAllocation(long pBuildingAllocationId) 
	throws DAOException
	{
		return getIds(pBuildingAllocationId, "getMonitorIdsByAllocationIdSQL");
	}
	private List<Long> getIdsByBuilding(long pId) 
	throws DAOException
	{
		List<Long> monitorIds = getIds(pId, "getMonitorIdsByBuildingIdSQL");
		List<Long> childIds = getIds(pId, "getAllocationIdsFromBuildingIdSQL");
		if (childIds.size() > 0) {
			Iterator<Long> childIdItr = childIds.iterator();
			while (childIdItr.hasNext()) {
				List<Long> childMonitorIds = getIdsByAllocation(childIdItr.next()); 
				if (childMonitorIds.size() > 0) {
					Iterator<Long> monitorItr = childMonitorIds.iterator();
					while (monitorItr.hasNext()) {
						monitorIds.add(monitorItr.next()); 
					}
				}
			}
		}
		return monitorIds;
		
	}
	private List<Long> getIdsByLocation(long pId) 
	throws DAOException
	{
		List<Long> monitorIds = getIds(pId, "getMonitorIdsByLocationIdSQL");
		List<Long> childIds = getIds(pId, "getBuildingIdsFromLocationIdSQL");
		if (childIds.size() > 0) {
			Iterator<Long> childIdItr = childIds.iterator();
			while (childIdItr.hasNext()) {
				List<Long> childMonitorIds = getIdsByBuilding(childIdItr.next()); 
				if (childMonitorIds.size() > 0) {
					Iterator<Long> monitorItr = childMonitorIds.iterator();
					while (monitorItr.hasNext()) {
						monitorIds.add(monitorItr.next()); 
					}
				}
			}
		}
		return monitorIds;
	}
	private List<Long> getIdsByDivision(long pId) 
	throws DAOException
	{
		List<Long> monitorIds = getIds(pId, "getMonitorIdsByDivisionIdSQL");
		List<Long> childIds = getIds(pId, "getLocationIdsFromDivisionIdSQL");
		if (childIds.size() > 0) {
			Iterator<Long> childIdItr = childIds.iterator();
			while (childIdItr.hasNext()) {
				List<Long> childMonitorIds = getIdsByLocation(childIdItr.next()); 
				if (childMonitorIds.size() > 0) {
					Iterator<Long> monitorItr = childMonitorIds.iterator();
					while (monitorItr.hasNext()) {
						monitorIds.add(monitorItr.next()); 
					}
				}
			}
		}
		return monitorIds;
	}
	
	/**
	 * Helper method to get a list of object identifiers 
	 * @param pId: unique identifier for object in context hierarchy
	 * @param pSQLName: name of SQL string property
	 * @return List of Long identifiers 
	 * @throws DAOException if identifiers cannot be obtained
	 */
	private List<Long> getIds(long pId, String pSQLName ) throws DAOException
	{
		LOG.debug("getIds for pId=" + pId + " and SQL=" +  pSQLName);
		/*
		 * Not cached due to potential volatility - may consider caching later 
		 */
		
        Connection 			conn 	= null;
        PreparedStatement 	stmt 	= null;
    	ResultSet 			rs 		= null;
    	
    	List<Long> monitorIds = new ArrayList<Long>();
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(getProperty(pSQLName));
            stmt.setLong(1, pId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		Long monitorId = rs.getLong(1);
            		monitorIds.add(monitorId);
            	}
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex.getMessage());
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
		return monitorIds;
	}
	
	/**
	 * Removes duplicate monitor ids from a list such that values are not counted twice
	 * @param pIdList as an array of Long monitor ids
	 * @return a sorted and deduped list of monitor ids
	 */
	private List<Long> deDupIds(List<Long> pIdList) 
	{
		Long[] idArray = (Long[])pIdList.toArray(new Long[ pIdList.size() ]);
		Arrays.sort(idArray);
		List<Long> deDupedList = new ArrayList<Long>();
        for (int i = 0; i < idArray.length; i++) {
            if (i == 0 || !idArray[i].equals(idArray[i-1]))
                deDupedList.add(idArray[i]);
        }
		return deDupedList;
	}
	
	public Float getPercentageSquareFeet(Long pId) throws DAOException {
		String sqlName = "getPercentageSqftSQL";
		Float percentage = (Float) getValue(pId, sqlName );
		return percentage;
	}
	
	/**
	 * Helper method to get a column from a record 
	 * @param pId: unique identifier for object in context hierarchy
	 * @param pSQLName: name of SQL string property
	 * @return Object value of return Id 
	 * @throws DAOException if identifiers cannot be obtained
	 */
	private Object getValue(long pId, String pSQLName ) throws DAOException
	{
		LOG.debug("getValue for pId=" + pId + " and SQL=" +  pSQLName);
		/*
		 * Not cached due to potential volatility - may consider caching later 
		 */
		
        Connection 			conn 	= null;
        PreparedStatement 	stmt 	= null;
    	ResultSet 			rs 		= null;
    	
    	Object 				value	= null;		
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(getProperty(pSQLName));
            stmt.setLong(1, pId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		value = rs.getObject(1);
            	}
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex.getMessage());
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
		return value;
	}
	
	
	public List<Division> getDivisionsByClient(Client pClient)
	throws DAOException
	{
	
		List<Division> divisions = new ArrayList<Division>();
	    Connection conn = null;
	    PreparedStatement stmt = null;
		ResultSet rs = null;
		
	    try {
			conn = ConnectionFactory.getInstance().getConnection();
	        stmt = conn.prepareStatement(getProperty("selectDivisionsByClientIdSQL"));
	        stmt.setLong(1, pClient.getId());
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	while (rs.next()) {
	        		Division division = new Division();
	        		division.setId(rs.getLong(1));
	        		division.setName(rs.getString(2));
	        		division.setAbbreviation(rs.getString(3));
	        		division.setLocations(getLocationsByDivisionId(division.getId()));
	        		divisions.add(division);
	        	}
	        }
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }
	    
	    return divisions;
	}	

	public Location getLocationFromResultSet(ResultSet rs)
		throws DAOException
	{
		Location location = new Location();
		try {
			location.setId(rs.getLong(1));
			location.setName(rs.getString(2));
			location.setSquareFeet(rs.getInt(3));
			location.setEstimate(rs.getInt(4));
			location.setEnergy(rs.getFloat(5));
			location.setGeoLocationId(rs.getLong(6));
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
		
		return location;
	}
	
	public Location getLocationFromId(long pLocationId)
	throws DAOException
	{
	
		Location location = null;
	    Connection conn = null;
	    PreparedStatement stmt = null;
		ResultSet rs = null;
		
	    try {
			conn = ConnectionFactory.getInstance().getConnection();
	        stmt = conn.prepareStatement(getProperty("getLocationFromIdSQL"));
	        stmt.setLong(1, pLocationId);
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	if (rs.next()) {
	        		location = getLocationFromResultSet(rs);
	        		location.setMetrics(getMetricsForLocation(pLocationId));
	        		location.setBuildings(getBuildingsForLocation(pLocationId));
	        	}
	        	else {
	        		throw new DAOException("ERROR: Location not retreived for ID = " + pLocationId);
	        	}
	        }
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }
	    
	    return location;
	}	
	
	public List<Location> getLocationsByDivisionId(long pDivisionId)
	throws DAOException
	{
	
		List<Location> locations = new ArrayList<Location>();
	    Connection conn = null;
	    PreparedStatement stmt = null;
		ResultSet rs = null;
		
	    try {
			conn = ConnectionFactory.getInstance().getConnection();
	        stmt = conn.prepareStatement(getProperty("getLocationsByDivisionIdSQL"));
	        stmt.setLong(1, pDivisionId);
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	while (rs.next()) {
	        		Location location = getLocationFromResultSet(rs);
	        		location.setMetrics(getMetricsForLocation(location.getId()));
	        		location.setBuildings(getBuildingsForLocation(location.getId()));
	        		locations.add(location);
	        	}
	        }
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }
	    
	    return locations;
	}	
	
	
	public List<Building> getBuildingsForLocation(long pLocationId)
	throws DAOException
	{
	
		Building building = null;
	    Connection conn = null;
	    PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Building> buildings = new ArrayList<Building>();
		
	    try {
			conn = ConnectionFactory.getInstance().getConnection();
	        stmt = conn.prepareStatement(getProperty("getBuildingsFromLocationSQL"));
	        stmt.setLong(1, pLocationId);
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	while (rs.next()) {
	        		building = populateBuildingFromResultSet(rs);
	        		buildings.add(building);
	        	}
	        }
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }
	    
	    return buildings;
	}	

	public Building getBuildingFromId(long pBuildingId)
	throws DAOException
	{
		Building building = null;
	    Connection conn = null;
	    PreparedStatement stmt = null;
		ResultSet rs = null;
		
	    try {
			conn = ConnectionFactory.getInstance().getConnection();
	        stmt = conn.prepareStatement(getProperty("getBuildingSQL"));
	        stmt.setLong(1, pBuildingId);
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	while (rs.next()) {
	        		building = populateBuildingFromResultSet(rs);
	        	}
	        }
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }
	    
	    return building;
	}	

	private Building populateBuildingFromResultSet(ResultSet pResultSet) 
		throws SQLException, DAOException  
	{
		Building building = new Building();
		building.setId(pResultSet.getLong(1));
		building.setName(pResultSet.getString(2));
		building.setSquareFeet(pResultSet.getInt(3));	        		
		building.setEstimate(pResultSet.getInt(4));
		building.setPercentSquareFeet(pResultSet.getFloat(5));
		building.setEnergy(pResultSet.getFloat(6));
		building.setBuildingType(pResultSet.getLong(7));
		building.setMetrics(getMetricsForBuilding(building.getId()));
		return building;
	}
	
	
	public List<BoomerMetric> getMetricsForBuilding(long pBuildingId)
	throws DAOException
	{
	    return getMetricsForId(pBuildingId, "getMetricsForBuildingSQL");
	}	
	
	
	public List<BoomerMetric> getMetricsForLocation(long pLocationId)
	throws DAOException
	{
	    return getMetricsForId(pLocationId, "getMetricsForLocationSQL");
	}	
	
	
	public List<BoomerMetric> getMetricsForId(long pId, String pSqlProperty)
	throws DAOException
	{
	
		List<BoomerMetric> metrics = new ArrayList<BoomerMetric>();
	    Connection conn = null;
	    PreparedStatement stmt = null;
		ResultSet rs = null;
		
	    try {
			conn = ConnectionFactory.getInstance().getConnection();
	        stmt = conn.prepareStatement(getProperty(pSqlProperty));
	        stmt.setLong(1, pId);
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	while (rs.next()) {
	        		BoomerMetric metric = new BoomerMetric();
	        		metric.setMetricStr(rs.getString(1));
	        		metric.setMetricType(MetricTypeEnum.fromString(rs.getString(2)));
	        		metric.setBegDate(rs.getTimestamp(3));
	        		metric.setEndDate(rs.getTimestamp(4));
	        		metric.setPeriodName(rs.getString(5));
	        		metric.setPeriodId(rs.getLong(6));
	        		metrics.add(metric);
	        	}
	        }
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }
	    
	    return metrics;

	}	
	
}
