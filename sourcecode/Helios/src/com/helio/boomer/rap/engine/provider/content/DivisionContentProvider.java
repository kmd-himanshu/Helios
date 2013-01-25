package com.helio.boomer.rap.engine.provider.content;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.BuildingAllocation;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.modellist.DivisionModelList;

public class DivisionContentProvider implements ITreeContentProvider, PropertyChangeListener {

	private Viewer viewer;

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		if ((oldInput != null) && (oldInput instanceof DivisionModelList)) {
			removeListenerFrom((DivisionModelList) oldInput);
		}
		if ((newInput != null) && (newInput instanceof DivisionModelList)) {
			addListenerTo((DivisionModelList) newInput);
			((DivisionModelList) newInput).addPropertyChangeListener(this);
		}

	}
	
	private void addListenerTo(DivisionModelList newInput) {
		newInput.addPropertyChangeListener(this);
	}

	private void removeListenerFrom(DivisionModelList oldInput) {
		oldInput.removePropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equalsIgnoreCase("divisionList")) {
			viewer.refresh();
		}
	}

	@Override
	public void dispose() {
		// Do nothing
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof DivisionModelList) {
			return true;
		}
		if (element instanceof Division) {
			Division division = (Division) element;
			return ((division.getLocations() != null) && (division.getLocations().size() > 0));
		}
		if (element instanceof Location) {
			Location location = (Location) element;
			return ((location.getBuildings() != null) && (location.getBuildings().size() > 0));
		}
		if (element instanceof Building) {
			Building building = (Building) element;
			return ((building.getBuildingAllocations() != null) && (building.getBuildingAllocations().size() > 0));
		}
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof DivisionModelList) {
			DivisionModelList divisionModelList = (DivisionModelList) parentElement;
			return divisionModelList.getDivisionList().toArray();
		}
		if (parentElement instanceof Division) {
			Division division = (Division) parentElement;
			return (division.getLocations() == null) ? null : division.getLocations().toArray();
		}
		if (parentElement instanceof Location) {
			Location location = (Location) parentElement;
			return (location.getBuildings() == null) ? null : location.getBuildings().toArray();
		}
		if (parentElement instanceof Building) {
			Building building = (Building) parentElement;
			return (building.getBuildingAllocations() == null) ? null : building.getBuildingAllocations().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Location) {
			Location location = (Location) element;
			return location.getDivision();
		}
		if (element instanceof Building) {
			Building building = (Building) element;
			return building.getLocation();
		}
		if (element instanceof BuildingAllocation) {
			BuildingAllocation buildingAllocation = (BuildingAllocation) element;
			return buildingAllocation.getBuilding();
		}
		return null;
	}

}