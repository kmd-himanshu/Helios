package com.helio.boomer.rap.engine.provider.content;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.helio.boomer.rap.engine.model.DeviceModel;
import com.helio.boomer.rap.engine.model.DeviceMonitor;
import com.helio.boomer.rap.engine.model.Manufacturer;
import com.helio.boomer.rap.engine.modellist.ManufacturerModelList;

public class ManufacturerContentProvider implements ITreeContentProvider, PropertyChangeListener {

	private Viewer viewer;

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		if ((oldInput != null) && (oldInput instanceof ManufacturerModelList)) {
			removeListenerFrom((ManufacturerModelList) oldInput);
		}
		if ((newInput != null) && (newInput instanceof ManufacturerModelList)) {
			addListenerTo((ManufacturerModelList) newInput);
			((ManufacturerModelList) newInput).addPropertyChangeListener(this);
		}

	}
	
	private void addListenerTo(ManufacturerModelList newInput) {
		newInput.addPropertyChangeListener(this);
	}

	private void removeListenerFrom(ManufacturerModelList oldInput) {
		oldInput.removePropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equalsIgnoreCase("manufacturerList")) {
			viewer.refresh();
		}
	}

	@Override
	public void dispose() {
		// Do nothing
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ManufacturerModelList) {
			return true;
		}
		if (element instanceof Manufacturer) {
			Manufacturer manufacturer = (Manufacturer) element;
			return ((manufacturer.getDeviceModels() != null) && (manufacturer.getDeviceModels().size() > 0));
		}
		if (element instanceof DeviceModel) {
			DeviceModel deviceModel = (DeviceModel) element;
			return ((deviceModel.getDeviceMonitors() != null) && (deviceModel.getDeviceMonitors().size() > 0));
		}
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ManufacturerModelList) {
			ManufacturerModelList manList = (ManufacturerModelList) parentElement;
			return manList.getManufacturerList().toArray();
		}
		if (parentElement instanceof Manufacturer) {
			Manufacturer manufacturer = (Manufacturer) parentElement;
			return (manufacturer.getDeviceModels() == null) ? null : manufacturer.getDeviceModels().toArray();
		}
		if (parentElement instanceof DeviceModel) {
			DeviceModel deviceModel = (DeviceModel) parentElement;
			return (deviceModel.getDeviceMonitors() == null) ? null : deviceModel.getDeviceMonitors().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof DeviceModel) {
			DeviceModel deviceModel = (DeviceModel) element;
			return deviceModel.getManufacturer();
		}
		if (element instanceof DeviceMonitor) {
			DeviceMonitor deviceMonitor = (DeviceMonitor) element;
			return deviceMonitor.getDeviceModel();
		}
		return null;
	}

}
