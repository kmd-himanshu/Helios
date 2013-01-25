package com.helio.boomer.rap.engine.modellist;

import java.util.List;

import com.google.common.collect.Lists;
import com.helio.boomer.rap.engine.AbstractModelObject;
import com.helio.boomer.rap.engine.model.DeviceModel;

public class DeviceModelModelList extends AbstractModelObject {
	
	private List<DeviceModel> deviceModelList;
	
	public DeviceModelModelList() {
		this.deviceModelList = Lists.newArrayList();
	}
	
	public DeviceModelModelList(List<DeviceModel> deviceMonitorList) {
		this.deviceModelList = deviceMonitorList;
	}
	
	public List<DeviceModel> getDeviceMonitorList() {
		return deviceModelList;
	}
	
	public void addDeviceMonitor(DeviceModel deviceModel) {
		deviceModelList.add(deviceModel);
		firePropertyChange("deviceModelList", null, null);
	}
	
	public void removeDeviceMonitor(DeviceModel deviceModel) {
		deviceModelList.remove(deviceModel);
		firePropertyChange("deviceModelList", null, null);
	}

}
