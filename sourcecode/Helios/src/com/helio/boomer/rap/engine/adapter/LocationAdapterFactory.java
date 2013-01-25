package com.helio.boomer.rap.engine.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.BuildingAllocation;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.property.BuildingAllocationPropertySource;
import com.helio.boomer.rap.engine.property.BuildingPropertySource;
import com.helio.boomer.rap.engine.property.DivisionPropertySource;
import com.helio.boomer.rap.engine.property.LocationPropertySource;

public class LocationAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IPropertySource.class && adaptableObject instanceof Division){
			return new DivisionPropertySource((Division) adaptableObject);
		}
		if (adapterType == IPropertySource.class && adaptableObject instanceof Location){
			return new LocationPropertySource((Location) adaptableObject);
		}
		if (adapterType == IPropertySource.class && adaptableObject instanceof Building){
			return new BuildingPropertySource((Building) adaptableObject);
		}
		if (adapterType == IPropertySource.class && adaptableObject instanceof BuildingAllocation){
			return new BuildingAllocationPropertySource((BuildingAllocation) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
