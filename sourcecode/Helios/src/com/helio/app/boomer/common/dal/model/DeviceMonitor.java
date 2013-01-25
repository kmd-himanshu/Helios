package com.helio.app.boomer.common.dal.model;


public class DeviceMonitor {

    private long id;
    private String monitorName;
    private String monitorNotes;
    private boolean persistMonitor;
	private String serialNumber;
	private int version; 
	private float pulseConstant;
	private MonitorTypeEnum monitorType;
	private long proxyRole = 0;	//  When > 0 a non-proxy is the proxy 
	
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMonitorNotes() {
		return monitorNotes;
	}
	public void setMonitorNotes(String monitorNotes) {
		this.monitorNotes = monitorNotes;
	}
	public boolean isPersistMonitor() {
		return persistMonitor;
	}
	public void setPersistMonitor(boolean persistMonitor) {
		this.persistMonitor = persistMonitor;
	}
	private DeviceModel deviceModel;
    
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public DeviceModel getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(DeviceModel deviceModel) {
		this.deviceModel = deviceModel;
	}
	public float getPulseConstant() {
		return pulseConstant;
	}
	public void setPulseConstant(float pulseConstant) {
		this.pulseConstant = pulseConstant;
	}
	public MonitorTypeEnum getMonitorType() {
		return monitorType;
	}
	public void setMonitorType(MonitorTypeEnum monitorType) {
		this.monitorType = monitorType;
	}

	public boolean isProxyRole() {
		return proxyRole > 0;
	}
	/** 
	 * Proxy Role is the id of the Proxy
	 * This is volatile, not persisted 
	 * @return
	 */
	public long getProxyRole() {
		return proxyRole;
	}
	public void setProxyRole(long proxyRole) {
		this.proxyRole = proxyRole;
	}
    
}
