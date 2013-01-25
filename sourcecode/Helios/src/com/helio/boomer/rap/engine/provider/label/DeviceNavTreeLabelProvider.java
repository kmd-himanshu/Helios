package com.helio.boomer.rap.engine.provider.label;

import org.eclipse.jface.viewers.LabelProvider;

import com.helio.boomer.rap.engine.model.DeviceModel;
import com.helio.boomer.rap.engine.model.DeviceMonitor;
import com.helio.boomer.rap.engine.model.Manufacturer;

public class DeviceNavTreeLabelProvider extends LabelProvider {
	
	@Override
	public String getText(Object element) {
		if (element instanceof Manufacturer) {
			return ((Manufacturer) element).getManufacturerName();
		}
		if (element instanceof DeviceModel) {
			return ((DeviceModel) element).getModelName();
		}
		if (element instanceof DeviceMonitor) {
			return ((DeviceMonitor) element).getMonitorName();
		}
		return super.getText(element);
	}

}
