package com.helio.boomer.rap.engine.modellist;

import java.util.List;

import com.google.common.collect.Lists;
import com.helio.boomer.rap.engine.AbstractModelObject;
import com.helio.boomer.rap.engine.model.Building;

public class BuildingModelList extends AbstractModelObject {
	
	private List<Building> buildingList;
	
	public BuildingModelList() {
		this.buildingList = Lists.newArrayList();
	}
	
	public BuildingModelList(List<Building> buildingList) {
		this.buildingList = buildingList;
	}
	
	public List<Building> getBuildingList() {
		return buildingList;
	}
	
	public void addBuilding(Building building) {
		buildingList.add(building);
		this.firePropertyChange("buildingList", null, null);
	}
	
	public void removeBuilding(Building building) {
		buildingList.remove(building);
		this.firePropertyChange("buildingList", null, null);
	}

}
