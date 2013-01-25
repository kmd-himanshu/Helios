package com.helio.boomer.rap.engine.modellist;

import java.util.List;

import com.google.common.collect.Lists;
import com.helio.boomer.rap.engine.AbstractModelObject;
import com.helio.boomer.rap.engine.model.Location;

public class LocationModelList extends AbstractModelObject {
	
	private List<Location> locationList;
	
	public LocationModelList() {
		this.locationList = Lists.newArrayList();
	}
	
	public LocationModelList(List<Location> locationList) {
		this.locationList = locationList;
	}
	
	public List<Location> getLocationList() {
		return locationList;
	}
	
	public void addLocation(Location location) {
		locationList.add(location);
		this.firePropertyChange("locationList", null, null);
	}
	
	public void removeLocation(Location location) {
		locationList.remove(location);
		this.firePropertyChange("locationList", null, null);
	}

}
