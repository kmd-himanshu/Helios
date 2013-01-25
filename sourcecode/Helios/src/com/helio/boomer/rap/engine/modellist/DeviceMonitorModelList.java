package com.helio.boomer.rap.engine.modellist;

import java.util.List;

import com.google.common.collect.Lists;
import com.helio.boomer.rap.engine.AbstractModelObject;
import com.helio.boomer.rap.engine.model.DeviceMonitor;

public class DeviceMonitorModelList extends AbstractModelObject {
	
	private List<DeviceMonitor> deviceMonitorList;
	
	public DeviceMonitorModelList() {
		this.deviceMonitorList = Lists.newArrayList();
	}
	
	public DeviceMonitorModelList(List<DeviceMonitor> deviceMonitorList) {
		this.deviceMonitorList = deviceMonitorList;
	}
	
	public List<DeviceMonitor> getDeviceMonitorList() {
		return deviceMonitorList;
	}
	
	public void addDeviceMonitor(DeviceMonitor deviceMonitor) {
		deviceMonitorList.add(deviceMonitor);
		firePropertyChange("deviceMonitorList", null, null);
	}
	
	public void removeDeviceMonitor(DeviceMonitor deviceMonitor) {
		deviceMonitorList.remove(deviceMonitor);
		firePropertyChange("deviceMonitorList", null, null);
	}

}
