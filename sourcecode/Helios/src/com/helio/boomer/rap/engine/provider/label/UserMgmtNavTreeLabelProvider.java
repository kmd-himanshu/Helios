package com.helio.boomer.rap.engine.provider.label;

import org.eclipse.jface.viewers.LabelProvider;

import com.helio.boomer.rap.engine.model.PE_User;
import com.helio.boomer.rap.engine.modellist.UserModelList;

public class UserMgmtNavTreeLabelProvider extends LabelProvider {
	
//	@Override
//	public String getText(Object element) {
//		if (element instanceof Manufacturer) {
//			return ((Manufacturer) element).getManufacturerName();
//		}
//		if (element instanceof DeviceModel) {
//			return ((DeviceModel) element).getModelName();
//		}
//		if (element instanceof DeviceMonitor) {
//			return ((DeviceMonitor) element).getMonitorName();
//		}
//		return super.getText(element);
//	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof PE_User) {
			return ((PE_User) element).getUserName();
		}
//		if (element instanceof Location) {
//			return ((Location) element).getLocationName();
//		}
//		if (element instanceof Building) {
//			Building building = (Building) element;
//			return building.getBuildingName() + " < " + building.getPercentageLocationSquareFeet() + "% >";
//		}
//		if (element instanceof BuildingAllocation) {
//			BuildingAllocation buildingAllocation = (BuildingAllocation) element;
//			return buildingAllocation.getBuildingAllocationName() + " < " + buildingAllocation.getPercentage() + "% >";
//		}
		return super.getText(element);
	}

}
