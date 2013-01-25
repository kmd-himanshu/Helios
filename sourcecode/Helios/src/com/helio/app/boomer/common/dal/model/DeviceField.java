package com.helio.app.boomer.common.dal.model;


public class DeviceField {

    private long id;
	private String deviceFieldName;
    private int deviceFieldOrder;
    private int deviceFieldType;
    private int version;  
    private boolean persistField;
    private long deviceModelId;
    
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDeviceFieldName() {
		return deviceFieldName;
	}
	public void setDeviceFieldName(String deviceFieldName) {
		this.deviceFieldName = deviceFieldName;
	}
	public int getDeviceFieldOrder() {
		return deviceFieldOrder;
	}
	public void setDeviceFieldOrder(int deviceFieldOrder) {
		this.deviceFieldOrder = deviceFieldOrder;
	}
	public int getDeviceFieldType() {
		return deviceFieldType;
	}
	public void setDeviceFieldType(int deviceFieldType) {
		this.deviceFieldType = deviceFieldType;
	}
	public boolean isPersistField() {
		return persistField;
	}
	public void setPersistField(boolean persistField) {
		this.persistField = persistField;
	}
	public long getDeviceModelId() {
		return deviceModelId;
	}
	public void setDeviceModelId(long deviceModelId) {
		this.deviceModelId = deviceModelId;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getVersion() {
		return version;
	}

}
