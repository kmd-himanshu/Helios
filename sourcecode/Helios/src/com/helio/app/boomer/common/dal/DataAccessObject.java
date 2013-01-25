package com.helio.app.boomer.common.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.helio.app.boomer.common.dal.model.DeviceMonitor;
import com.helio.app.boomer.common.dal.model.MonitorTypeEnum;

public class DataAccessObject {
	// Default cache refresh period is set to 60 minutes.
	private static final long DEFAULT_CACHE_REFRESH_PERIOD = 60 * 60 * 1000;
	private Timer cacheRefreshTimer;
	private static Properties PROPS = new Properties();
	private static Log LOG = LogFactory.getLog("DataAccessObject");
	private long unusedSequenceValue = 0L;
	private int sequenceReservationAmount = 0;
	private long maxSequenceValue = 0L;

	private String sequenceReservationSQL = null;
	private String sequenceNextSQL = null;
	
		
	public DataAccessObject() {
		super();
		getProperties();
		sequenceReservationAmount = Integer.valueOf(getProperty("sequenceReservationAmount"));
		sequenceReservationSQL = getProperty("sequenceReservationSQL");
		sequenceNextSQL = getProperty("sequenceNextSQL");
		
	}
	
	protected void refreshCache() {
	}

	protected void enableAutomaticCacheRefresh() {
		enableAutomaticCacheRefresh(DEFAULT_CACHE_REFRESH_PERIOD);
	}

	protected void enableAutomaticCacheRefresh(long time) {
		cacheRefreshTimer = new Timer(true);
		cacheRefreshTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try {
					refreshCache();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, time, time);
	}
	
	private synchronized void getProperties() 
	{
		try {
			PROPS.load( ConnectionFactory.class.getResource( "dal.properties" ).openStream() );
		} 
		catch (Exception e) {
			LOG.error("*** Exception while loading DAL properties *** ", e);
		}
	}
	
	public synchronized long getNextSequence()
	{
		CallableStatement callableStmt = null;
        Connection conn = null;
        PreparedStatement prepareStmt = null;
        ResultSet rs = null;
        long retValue = 0L;
		
        // If still number in reserve, use those before going to DB
		if (unusedSequenceValue > 0 && unusedSequenceValue <= maxSequenceValue) {
			retValue = unusedSequenceValue;
			unusedSequenceValue++;
			return retValue;
		}
		
		// Get next available sequence from DB
		LOG.debug("getNextSequence unusedSequenceValue=" + unusedSequenceValue 
				+ "  maxSequenceValue=" + maxSequenceValue
				+ "  sequenceReservationAmount" + sequenceReservationAmount);

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			callableStmt = conn.prepareCall(sequenceReservationSQL);
			// 1. Number if sequence values to reserve
			callableStmt.setInt(1, sequenceReservationAmount);
			callableStmt.execute();
			prepareStmt = conn.prepareStatement(sequenceNextSQL);
			rs = prepareStmt.executeQuery();
            if (rs != null) {
            	rs.next();
        		unusedSequenceValue = rs.getLong(1);
        		maxSequenceValue = unusedSequenceValue + sequenceReservationAmount - 2;
            }
            ConnectionFactory.closeAll(conn, prepareStmt, rs);
            prepareStmt = null;
            conn = null;
            rs = null;
		} catch (SQLException e) {
			LOG.error("SQL Exception in DataAccessObject.getNextSequence",e);
			return -1L;
		} catch (Exception e) {
			LOG.error("Exception in DataAccessObject.getNextSequence",e);
			return -1L;
		} finally {
            ConnectionFactory.closeAll(conn, prepareStmt, rs);
            prepareStmt = null;
            conn = null;
            rs = null;
		}
		
		retValue = unusedSequenceValue;
		unusedSequenceValue++;
		return retValue;
	}
	
	
	String getProperty(String key) 
	{
		return PROPS.getProperty(key);
	}
	
	/**
	 * This takes a result set and converts it to a DeviceMonitor.
	 * @param pResultSet: contains the packet info
	 * @param pConn: database connection
	 * @return a fully populated DeviceMonitor 
	 * @throws Exception
	 */
	protected DeviceMonitor getDeviceMonitorFromResultSet(ResultSet pResultSet)  throws Exception
	{
		DeviceMonitor deviceMonitor = new DeviceMonitor();
		deviceMonitor.setId(pResultSet.getLong(1));
		deviceMonitor.setMonitorName(pResultSet.getString(2));
		deviceMonitor.setMonitorNotes(pResultSet.getString(3));
		deviceMonitor.setPersistMonitor(pResultSet.getInt(4) == 1 ? true : false );
   		deviceMonitor.setSerialNumber(pResultSet.getString(5));
   		deviceMonitor.setVersion(pResultSet.getInt(6));
   	    deviceMonitor.setPulseConstant(pResultSet.getFloat(8));
   	    deviceMonitor.setMonitorType(   MonitorTypeEnum.fromInt( (Integer)pResultSet.getInt(9))  ); // Pulse or Proxy
   	    return deviceMonitor;
	}

}
