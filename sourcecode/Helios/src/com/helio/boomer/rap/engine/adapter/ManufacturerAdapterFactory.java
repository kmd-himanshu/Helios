package com.helio.boomer.rap.engine.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.helio.boomer.rap.engine.model.DeviceModel;
import com.helio.boomer.rap.engine.model.DeviceMonitor;
import com.helio.boomer.rap.engine.model.Manufacturer;
import com.helio.boomer.rap.engine.property.DeviceModelPropertySource;
import com.helio.boomer.rap.engine.property.DeviceMonitorPropertySource;
import com.helio.boomer.rap.engine.property.ManufacturerPropertySource;

public class ManufacturerAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IPropertySource.class && adaptableObject instanceof Manufacturer){
			return new ManufacturerPropertySource((Manufacturer) adaptableObject);
		}
		if (adapterType == IPropertySource.class && adaptableObject instanceof DeviceMonitor){
			return new DeviceMonitorPropertySource((DeviceMonitor) adaptableObject);
		}
		if (adapterType == IPropertySource.class && adaptableObject instanceof DeviceModel){
			return new DeviceModelPropertySource((DeviceModel) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
