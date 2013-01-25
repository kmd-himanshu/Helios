package com.helio.boomer.rap.engine.modellist;

import java.util.List;

import com.google.common.collect.Lists;
import com.helio.boomer.rap.engine.AbstractModelObject;
import com.helio.boomer.rap.engine.model.Manufacturer;

public class ManufacturerModelList extends AbstractModelObject {
	
	private List<Manufacturer> manufacturerList;
	
	public ManufacturerModelList() {
		this.manufacturerList = Lists.newArrayList();
	}
	
	public ManufacturerModelList(List<Manufacturer> manufacturerList) {
		this.manufacturerList = manufacturerList;
	}
	
	public List<Manufacturer> getManufacturerList() {
		return manufacturerList;
	}
	
	public void addManufacturer(Manufacturer manufacturer) {
		manufacturerList.add(manufacturer);
		this.firePropertyChange("manufacturerList", null, null);
	}
	
	public void removeManufacturer(Manufacturer manufacturer) {
		manufacturerList.remove(manufacturer);
		this.firePropertyChange("manufacturerList", null, null);
	}

}
