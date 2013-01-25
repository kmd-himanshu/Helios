package com.helio.boomer.rap.engine.provider.label;

import org.eclipse.jface.viewers.LabelProvider;

import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.BuildingAllocation;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.model.Location;

public class DivisionNavTreeLabelProvider extends LabelProvider {
	
	@Override
	public String getText(Object element) {
		if (element instanceof Division) {
			return ((Division) element).getDivisionName();
		}
		if (element instanceof Location) {
			return ((Location) element).getLocationName();
		}
		if (element instanceof Building) {
			Building building = (Building) element;
			return building.getBuildingName() + " < " + building.getPercentageLocationSquareFeet() + "% >";
		}
		if (element instanceof BuildingAllocation) {
			BuildingAllocation buildingAllocation = (BuildingAllocation) element;
			return buildingAllocation.getBuildingAllocationName() + " < " + buildingAllocation.getPercentage() + "% >";
		}
		return super.getText(element);
	}

}
