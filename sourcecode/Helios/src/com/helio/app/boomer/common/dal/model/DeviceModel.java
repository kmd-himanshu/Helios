package com.helio.app.boomer.common.dal.model;

public class DeviceModel {

    private String modelName;
    private Boolean utilityGrade;
    private DeviceUseEnum deviceUse;
	private int version; 
    
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public Boolean getUtilityGrade() {
		return utilityGrade;
	}
	public void setUtilityGrade(Boolean utilityGrade) {
		this.utilityGrade = utilityGrade;
	}
	public DeviceUseEnum getDeviceUse() {
		return deviceUse;
	}
	public void setDeviceUse(int deviceUse) {
		this.deviceUse = DeviceUseEnum.fromInt(deviceUse);;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
}
