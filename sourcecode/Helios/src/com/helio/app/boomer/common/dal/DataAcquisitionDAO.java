package com.helio.app.boomer.common.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.helio.app.boomer.common.dal.model.DeviceField;
import com.helio.app.boomer.common.dal.model.DeviceMonitor;

public class DataAcquisitionDAO extends DataAccessObject {

	
	private static int MAX_INSTANCES = 5;
	private static int instanceIndex = 0;
	private int instanceNum = 0;
	private static DataAcquisitionDAO instance[] = new DataAcquisitionDAO[MAX_INSTANCES];
	private static HashMap<String, Object> cache = new HashMap<String, Object>();
	private static Log LOG = LogFactory.getLog("DataAcquisitionDAO");
	private boolean checkForDuplicatePackets = false;

	private DataAcquisitionDAO() {
		super();
		checkForDuplicatePackets = Boolean.valueOf(getProperty("checkDuplicatePacketsSwitch"));	
		LOG.info("I"+ instanceIndex + " *** DUPLICATE PACKET CHECKING IS TURNED " + (checkForDuplicatePackets ? "ON" : "OFF") + " ***");
		instanceNum = instanceIndex;
	}

	public synchronized static DataAcquisitionDAO getInstance() 
	{
		if (instanceIndex > (MAX_INSTANCES-1)) {
			instanceIndex=0;
		}
		if (instance[instanceIndex] == null) {
			instance[instanceIndex] = new DataAcquisitionDAO();
		}
		return instance[instanceIndex++];
	}
	
	public boolean isMonitorPersisted(String pDeviceMonitorIdStr)
	{
		long monitorId = Long.valueOf(pDeviceMonitorIdStr);
		DeviceMonitor deviceMonitor = getDeviceMonitorById(monitorId);
		return deviceMonitor.isPersistMonitor();
	}
	
	
	public boolean isDuplicatePacket(long pDeviceMonitorId, String pTimestamp )
	{
		if (!checkForDuplicatePackets) return false;

        String serialNumber = null;
        Connection conn = null;
        PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(getProperty("isPacketDuplicateSQL"));
            stmt.setTimestamp(1, Timestamp.valueOf(pTimestamp));			// DevicePacket.PacketDT
            stmt.setLong(2, pDeviceMonitorId);								// DevicePacket.DeviceMonitor_Id 
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		serialNumber = rs.getString(1);
            	}
            }
        }
        catch (Exception ex) {} 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        if (serialNumber != null) {
			LOG.warn("I"+ instanceNum + " WARNING: DUPLICATE PACKET FOUND FOR serialNumber/date=" +serialNumber+"/"+pTimestamp);
    		return true;
        }
		return false;

	}

	private DeviceMonitor getDeviceMonitorById(long pMonitorId) 
	{
		LOG.debug("I"+ instanceNum + " getDeviceMonitorById " + pMonitorId);
		DeviceMonitor deviceMonitor = (DeviceMonitor) cache.get("DeviceMonitorId-Persist"+pMonitorId);
		if (!(deviceMonitor==null)) {
			return deviceMonitor;
		}
				
        Connection conn = null;
        PreparedStatement stmt = null;
        LOG.debug("I"+ instanceNum + " Checking whether or not to persist device monitor for id =" + pMonitorId );
    	ResultSet rs = null;
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(getProperty("getDeviceMonitorByIdSQL"));
            stmt.setLong(1, pMonitorId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		deviceMonitor = getDeviceMonitorFromResultSet(rs);
            	}
            }
        }
        catch (Exception ex) {} 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
    	cache.put("DeviceMonitorId-Persist"+pMonitorId, deviceMonitor);
		return deviceMonitor;
	}
	
	public String getDeviceMonitorIdByUniqueKey(String pUniqueKey) 
	{
		LOG.debug("I"+ instanceNum + " getDeviceMonitorIdByUniqueKey " + pUniqueKey);
		String deviceMonitorIdStr = (String) cache.get("DeviceMonitorId"+pUniqueKey);
		if (!(deviceMonitorIdStr==null)) {
			return deviceMonitorIdStr;
		}
		
		String serialNumber = pUniqueKey.substring(0, pUniqueKey.indexOf("***"));
		String monitorName  = pUniqueKey.substring(pUniqueKey.indexOf("***")+3);
		
        Connection conn = null;
        PreparedStatement stmt = null;
        LOG.debug("I"+ instanceNum + " Checking device monitor for serialNumber=" + serialNumber + "   and monitorName=" + monitorName);
        deviceMonitorIdStr = "NONE";
        long deviceMonitorId = -1L;
    	ResultSet rs = null;
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            stmt = conn.prepareStatement(getProperty("getDeviceMonitorIdByUniqueKeySQL"));
            stmt.setString(1, monitorName);
            stmt.setString(2, serialNumber);
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		deviceMonitorId = rs.getLong(1);
            	}
            }
        }
        catch (Exception ex) {} 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        if (deviceMonitorId !=-1L) {
        	deviceMonitorIdStr = String.valueOf(deviceMonitorId);
    		cache.put("DeviceMonitorId"+pUniqueKey, deviceMonitorIdStr);
        }
		return deviceMonitorIdStr;
	}

		
	public String saveLogEntry(String pLogEntry) {
		StringTokenizer token = new StringTokenizer(pLogEntry, ",");
		String deviceMonitorIdStr = token.nextToken(); 										// First token is device monitor id
		Long deviceMonitorId = Long.valueOf(deviceMonitorIdStr);

		DeviceMonitor deviceMonitor = getDeviceMonitorById(deviceMonitorId);
		if (deviceMonitor.isPersistMonitor()) {											    // Skip persistence steps if monitor indicates
			ArrayList<DeviceField> fields = getDeviceFieldsFromMonitorId(deviceMonitorId); 	// Get List of DeviceFields
			boolean retval = persistLogEntry(deviceMonitor.getId(),pLogEntry,fields);
			return (retval ? "SUCCESS:I" : "FAILURE:I") + instanceNum;
		}

		return "SUCCESS:I" + instanceNum;
	}
	
	public String saveDeckLogEntry(String pLogEntry, String[] pFieldNames, Float pPulseConstant) {
		StringTokenizer token = new StringTokenizer(pLogEntry, ",");
		String deviceMonitorIdStr = token.nextToken(); 										// First token is device monitor id
		Long deviceMonitorId = Long.valueOf(deviceMonitorIdStr);

		DeviceMonitor deviceMonitor = getDeviceMonitorById(deviceMonitorId);
		if (deviceMonitor.isPersistMonitor()) {											    // Skip persistence steps if monitor indicates
			ArrayList<DeviceField> fields = getDeviceFieldsFromMonitorId(deviceMonitorId); 	// Get List of DeviceFields
			boolean retval = persistDeckLogEntry(deviceMonitor.getId(),pLogEntry,fields,pFieldNames,pPulseConstant);
			return (retval ? "SUCCESS:I" : "FAILURE:I") + instanceNum;
		}

		return "SUCCESS:I" + instanceNum;
	}
	
	private synchronized boolean persistLogEntry(long pDeviceMonitorId, String pLogEntry, ArrayList<DeviceField> pFields) 
	{
		boolean success = true;

		String timeStamp     = null; 	// time stamp
		int packetError      = 0; 		// error value
        int lowRange         = 0; 		// low range
        int highRange        = 0; 		// high range
		
		StringTokenizer token = new StringTokenizer(pLogEntry, ",", true);
		try {
							   token.nextToken(); // First token is always device monitor id
							   token.nextToken(); // Next is delim
		timeStamp            = token.nextToken(); // Next token is always time stamp
							   token.nextToken(); // Next is delim
		packetError          = Integer.valueOf(token.nextToken()); // Next token is error value
			                   token.nextToken(); // Next is delim
		lowRange             = Integer.valueOf(token.nextToken()); // Next token is low range
							   token.nextToken(); // Next is delim
		highRange            = Integer.valueOf(token.nextToken()); // Next token is high range
							   token.nextToken(); // Next is delim
		}
		catch (NumberFormatException ex) {
			LOG.error("I"+ instanceNum + " NumberFormatException in persistLogEntry. pDeviceMonitorId=" + pDeviceMonitorId + "   pLogEntry=" + pLogEntry);
			return false;
		}
		catch (Exception ex) {
			LOG.error("I"+ instanceNum + " Exception in persistLogEntry. pDeviceMonitorId=" + pDeviceMonitorId + "   pLogEntry=" + pLogEntry);
			return false;
		}

		long seq = getNextSequence();		// get the next unique ID
		PreparedStatement prepareStmt = null;
        Connection conn = null;

        int tokenCount = 0, fieldCount = 0; // for debugging only
       
		try {
			timeStamp = timeStamp.substring(1, timeStamp.length()-1); 	// Eliminate surrounding quotes

			if (!isDuplicatePacket(pDeviceMonitorId, timeStamp)) {
				conn = ConnectionFactory.getInstance().getConnection();
				prepareStmt = conn.prepareCall(getProperty("insertDevicePacketSQL"));
				prepareStmt.setLong(1, seq);								// DevicePacket.Id
				prepareStmt.setTimestamp(2, Timestamp.valueOf(timeStamp));	// DevicePacket.PacketDT
				prepareStmt.setLong(3, pDeviceMonitorId);					// DevicePacket.DeviceMonitor_Id (FK)
				prepareStmt.executeUpdate();  								// insert a DEVICEPACKET
				ConnectionFactory.closeAll(null, prepareStmt, null);
				
				long foreignKey = seq;
				seq = getNextSequence(); 							// get the next available unique ID
				prepareStmt = conn.prepareCall(getProperty("insertPacketCommonSQL"));
				prepareStmt.setLong(1, seq);						// PacketCommon.Id
				prepareStmt.setInt(2, highRange);					// PacketCommon.HighRange
				prepareStmt.setInt(3, lowRange);					// PacketCommon.LowRange
				prepareStmt.setInt(4, packetError);					// PacketCommon.PacketError
				prepareStmt.setLong(5, foreignKey);  				// PacketCommon.PCDEVICEPACKET_Id (FK)
				prepareStmt.executeUpdate();  						// insert a PACKETCOMMON
				            
	            Iterator <DeviceField>fieldItr = pFields.iterator();
	            while (token.hasMoreElements()) {
	            	DeviceField deviceField = fieldItr.next();
	            	String      logValue    = token.nextToken();
	            	if (logValue.equals(",")) {
	            		continue; // field is null so skip
	            	}
	            	tokenCount++; fieldCount++; 
	    			if (deviceField.isPersistField()) {
	    				seq = getNextSequence();
		    			prepareStmt = conn.prepareCall(getProperty("insertDeviceKVPSQL"));
		    			prepareStmt.setLong(1, seq);					// DeviceKVP.Id
		    			prepareStmt.setString(2, logValue);				// DeviceKVP.FieldValue
		    			prepareStmt.setLong(3, deviceField.getId());	// DeviceKVP.DeviceField_Id (FK)
		    			prepareStmt.setLong(4, foreignKey);  			// DeviceKVP.DevicePacket_id (FK)
		    			prepareStmt.executeUpdate();  					// insert a DeviceKVP
	    			}
	    			try {
	    				token.nextToken(); // Next is delim except last token
	    			}
	    			catch (NoSuchElementException ex) {}
	            }
			}
		} catch (SQLException e) {
			LOG.error("I"+ instanceNum + " SQL Exception in DataAcquisitionDAO.savedDevicePacketForMonitor",e);
			success = false;
		} catch (Exception e) {
			LOG.error("I"+ instanceNum + " ### ERROR in KVP persistence  tokenCount=" + tokenCount + "  fieldCount=" + fieldCount);
			LOG.error("I"+ instanceNum + " Exception in DataAcquisitionDAO.savedDevicePacketForMonitor",e);
			success = false;
		} finally {
            ConnectionFactory.closeAll(conn, prepareStmt, null);
            prepareStmt = null;
            conn = null;
		}
				
		return success;
	}
	
	
	private synchronized boolean persistDeckLogEntry(long pDeviceMonitorId, 
			String pLogEntry, 
			ArrayList<DeviceField> pFields, 
			String[] pFieldNames, 
			Float pPulseConstant) 
	{
		boolean success = true;

		String timeStamp     = null; 	// time stamp
		int packetError      = 0; 		// error value
        int lowRange         = 0; 		// low range
        int highRange        = 0; 		// high range
		
		StringTokenizer token = new StringTokenizer(pLogEntry, ",", true);
		try {
							   token.nextToken(); // First token is always device monitor id
							   token.nextToken(); // Next is delim
		                       token.nextToken(); // Next token is local time stamp
							   token.nextToken(); // Next is delim
							   
		timeStamp            = token.nextToken(); // Next token is UTC time stamp
		if (timeStamp.contains("/")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		    Date convertedDate = dateFormat.parse(timeStamp);
			SimpleDateFormat stdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		    timeStamp = stdFormat.format(convertedDate) + ":00";
		}
		
		
							   token.nextToken(); // Next is delim
							   
		packetError          = Integer.valueOf(token.nextToken()); // Next token is error value
			                   token.nextToken(); // Next is delim
		lowRange             = Integer.valueOf(token.nextToken()); // Next token is low range
							   token.nextToken(); // Next is delim
		highRange            = Integer.valueOf(token.nextToken()); // Next token is high range
							   token.nextToken(); // Next is delim
		}
		catch (NumberFormatException ex) {
			LOG.error("I"+ instanceNum + " NumberFormatException in persistLogEntry. pDeviceMonitorId=" + pDeviceMonitorId + "   pLogEntry=" + pLogEntry);
			return false;
		}
		catch (Exception ex) {
			LOG.error("I"+ instanceNum + " Exception in persistLogEntry. pDeviceMonitorId=" + pDeviceMonitorId + "   pLogEntry=" + pLogEntry);
			return false;
		}

		long seq = getNextSequence();		// get the next unique ID
		PreparedStatement prepareStmt = null;
        Connection conn = null;

        int tokenCount = 0, fieldCount = 0; // for debugging only
       
		try {
			//timeStamp = timeStamp.substring(1, timeStamp.length()-1); 	// Eliminate surrounding quotes

			if (!isDuplicatePacket(pDeviceMonitorId, timeStamp)) {
				conn = ConnectionFactory.getInstance().getConnection();
				prepareStmt = conn.prepareCall(getProperty("insertDevicePacketSQL"));
				prepareStmt.setLong(1, seq);								// DevicePacket.Id
				prepareStmt.setTimestamp(2, Timestamp.valueOf(timeStamp));	// DevicePacket.PacketDT
				prepareStmt.setLong(3, pDeviceMonitorId);					// DevicePacket.DeviceMonitor_Id (FK)
				prepareStmt.executeUpdate();  								// insert a DEVICEPACKET
				ConnectionFactory.closeAll(null, prepareStmt, null);
				
				long foreignKey = seq;
				seq = getNextSequence(); 							// get the next available unique ID
				prepareStmt = conn.prepareCall(getProperty("insertPacketCommonSQL"));
				prepareStmt.setLong(1, seq);						// PacketCommon.Id
				prepareStmt.setInt(2, highRange);					// PacketCommon.HighRange
				prepareStmt.setInt(3, lowRange);					// PacketCommon.LowRange
				prepareStmt.setInt(4, packetError);					// PacketCommon.PacketError
				prepareStmt.setLong(5, foreignKey);  				// PacketCommon.PCDEVICEPACKET_Id (FK)
				prepareStmt.executeUpdate();  						// insert a PACKETCOMMON
				            
	            Iterator <DeviceField>fieldItr = pFields.iterator();
	            while (token.hasMoreElements()) {
	            	DeviceField deviceField = fieldItr.next();
	            	String      logValue    = token.nextToken();
	            	if (logValue.equals(",")) {
	            		continue; // field is null so skip
	            	}

	            	/*
	            	 * Check for pulse value
	            	 */
	            	boolean isPulseField = false;
	            	for (int i=0; i<pFieldNames.length;i++) {
	            		if (deviceField.getDeviceFieldName().equalsIgnoreCase(pFieldNames[i])) {
	            			isPulseField = true;
	            		}
	            	}
	            	/*
	            	 * If found, divide by pulse constant
	            	 */
	            	if (isPulseField) {
		    			float logValueFloat = Float.valueOf(logValue);
		    			float origLogValue = logValueFloat / pPulseConstant;
		    			logValue = String.valueOf(origLogValue);
	            	}
	            	
	            	tokenCount++; fieldCount++; 
	    			if (deviceField.isPersistField()) {
	    				seq = getNextSequence();
		    			prepareStmt = conn.prepareCall(getProperty("insertDeviceKVPSQL"));
		    			prepareStmt.setLong(1, seq);					// DeviceKVP.Id
		    			prepareStmt.setString(2, logValue);				// DeviceKVP.FieldValue
		    			prepareStmt.setLong(3, deviceField.getId());	// DeviceKVP.DeviceField_Id (FK)
		    			prepareStmt.setLong(4, foreignKey);  			// DeviceKVP.DevicePacket_id (FK)
		    			prepareStmt.executeUpdate();  					// insert a DeviceKVP
	    			}
	    			try {
	    				token.nextToken(); // Next is delim except last token
	    			}
	    			catch (NoSuchElementException ex) {}
	            }
			}
		} catch (SQLException e) {
			LOG.error("I"+ instanceNum + " SQL Exception in DataAcquisitionDAO.savedDevicePacketForMonitor",e);
			success = false;
		} catch (Exception e) {
			LOG.error("I"+ instanceNum + " ### ERROR in KVP persistence  tokenCount=" + tokenCount + "  fieldCount=" + fieldCount);
			LOG.error("I"+ instanceNum + " Exception in DataAcquisitionDAO.savedDevicePacketForMonitor",e);
			success = false;
		} finally {
            ConnectionFactory.closeAll(conn, prepareStmt, null);
            prepareStmt = null;
            conn = null;
		}
				
		return success;
	}
	private ArrayList<DeviceField> getDeviceFieldsFromMonitorId(Long pDeviceMonitorId) {
		/*
		 * First check cache
		 */
		@SuppressWarnings("unchecked")
		ArrayList<DeviceField> deviceFields = (ArrayList<DeviceField>) cache.get("DeviceFields" + pDeviceMonitorId);
		if (!(deviceFields==null)) {
			return deviceFields;
		}

		boolean success = true;
        Connection conn = null;
        PreparedStatement stmt = null;
    	ResultSet rs = null;
        try {
			conn = ConnectionFactory.getInstance().getConnection();
            /*
             * Get the device fields using the validated serial number. 
             */
            stmt = conn.prepareStatement(getProperty("getDeviceFieldsByMonitorIdSQL"));
            stmt.setLong(1, pDeviceMonitorId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	deviceFields = new ArrayList<DeviceField>();
            	while (rs.next()) {
            		DeviceField field = new DeviceField();
            		field.setId(rs.getLong(1));
            		field.setDeviceFieldName(rs.getString(2));
            		field.setDeviceFieldOrder(rs.getInt(3));
            		field.setDeviceFieldType(rs.getInt(4));
            		field.setVersion(rs.getInt(5));
            		field.setPersistField(rs.getInt(6) == 1 ? true : false);
            		field.setDeviceModelId(rs.getLong(7));
            		deviceFields.add(field);
            	}
            }
        }
        catch (SQLException ex) {
        	LOG.error("I"+ instanceNum + " Error when attempting to get DeviceField records for monitor id " + pDeviceMonitorId, ex);
        	success = false;
        	deviceFields = null;
        }
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        // Save to cache
        if (success) {
        	cache.put("DeviceFields" + pDeviceMonitorId, deviceFields);
        }

		return deviceFields;
	}
}
