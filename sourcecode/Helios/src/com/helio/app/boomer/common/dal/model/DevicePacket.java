/**
 * 
 */
package com.helio.app.boomer.common.dal.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rickschwartz
 * 
 * This is a logical representation of a Packet which includes
 * a collection of a combination of properties from DeviceKVP and DeviceField
 *
 */
public class DevicePacket {

	private long id;
	private Timestamp packetDate;
	private int version; 
	private long monitorId;
	private List<PacketValue> packetValues;
	private DeviceMonitor deviceMonitor;
	
	public DevicePacket () {
		packetValues = new ArrayList<PacketValue>();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Timestamp getPacketDate() {
		return packetDate;
	}
	public void setPacketDate(Timestamp packetDate) {
		this.packetDate = packetDate;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(long monitorId) {
		this.monitorId = monitorId;
	}
	public List<PacketValue> getPacketValues() {
		return packetValues;
	}
	public DeviceMonitor getDeviceMonitor() {
		return deviceMonitor;
	}
	public void setDeviceMonitor(DeviceMonitor deviceMonitor) {
		this.deviceMonitor = deviceMonitor;
	}

	public void setPacketValues(List<PacketValue> packetValues) {
		this.packetValues = packetValues;
	}
	public void addPacketValue(PacketValue packetValue) {
		this.packetValues.add(packetValue);
	}
	
}
