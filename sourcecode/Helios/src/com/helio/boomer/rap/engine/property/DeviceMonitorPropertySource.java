package com.helio.boomer.rap.engine.property;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.model.DeviceMonitor;

public class DeviceMonitorPropertySource implements IPropertySource2 {

	private final DeviceMonitor deviceMonitor;

	private final static String MONITORNAME_ID		= "monitorName";
	private final static String MONITORNOTES_ID		= "monitorNotes";
	private final static String SERIALNUMBER_ID		= "serialNumber";
	private final static String PERSISTMONITOR_ID	= "persistMonitor";
	
	public DeviceMonitorPropertySource(DeviceMonitor deviceMonitor) {
		this.deviceMonitor = deviceMonitor;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		String[] booleanValues = {"true", "false"};
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(MONITORNAME_ID,			"Monitor Name"),
				new TextPropertyDescriptor(MONITORNOTES_ID,			"Monitor Notes"),
				new TextPropertyDescriptor(SERIALNUMBER_ID,			"Serial Number"),
				new ComboBoxPropertyDescriptor(PERSISTMONITOR_ID,	"Persist Monitor?", booleanValues)
				};
	}

	@Override
	public Object getPropertyValue(Object id) {
		// Once we start using Java SE 1.7, we can change the following code to a String "switch" statement.
		if (id.equals(MONITORNAME_ID))
			return deviceMonitor.getMonitorName();
		if (id.equals(MONITORNOTES_ID))
			return deviceMonitor.getMonitorNotes();
		if (id.equals(SERIALNUMBER_ID))
			return deviceMonitor.getSerialNumber();
		if (id.equals(PERSISTMONITOR_ID))
			return (deviceMonitor.isPersistMonitor()) ? "true" : "false";
		return null;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// Do nothing--not resettable.
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (value != null) return;
		String setString = (String) value;
		if (id.equals(MONITORNAME_ID))
			deviceMonitor.setMonitorName(setString);
		if (id.equals(MONITORNOTES_ID))
			deviceMonitor.setMonitorNotes(setString);
		if (id.equals(SERIALNUMBER_ID))
			deviceMonitor.setSerialNumber(setString);
		if (id.equals(PERSISTMONITOR_ID))
			deviceMonitor.setPersistMonitor(Boolean.parseBoolean(setString));
	}

	@Override
	public boolean isPropertyResettable(Object id) {
		return false;
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (id.equals(MONITORNAME_ID))
			return !(Strings.isNullOrEmpty(deviceMonitor.getMonitorName()));
		if (id.equals(MONITORNOTES_ID))
			return !(Strings.isNullOrEmpty(deviceMonitor.getMonitorNotes()));
		if (id.equals(SERIALNUMBER_ID))
			return !(Strings.isNullOrEmpty(deviceMonitor.getSerialNumber()));
		if (id.equals(PERSISTMONITOR_ID))
			return (deviceMonitor.isPersistMonitor() != null);
		return false;
	}

}
