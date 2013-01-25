package com.helio.boomer.rap.engine.property;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.model.DeviceModel;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class DeviceModelPropertySource implements IPropertySource2 {

	private final DeviceModel deviceModel;

	private final static String MODELNAME_ID		= "modelName";
	private final static String DEVICEUSE_ID		= "deviceUse";
	private final static String UTILITYGRADE_ID		= "utilityGrade";

	public DeviceModelPropertySource(DeviceModel deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		String[] booleanValues = {"true", "false"};
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(MODELNAME_ID,		"Model Name"),
				new TextPropertyDescriptor(DEVICEUSE_ID,		"Device Use"),
				new ComboBoxPropertyDescriptor(UTILITYGRADE_ID,	"Utility Grade?", booleanValues)
		};
	}

	@Override
	public Object getPropertyValue(Object id) {
		// Once we start using Java SE 1.7, we can change the following code to a String "switch" statement.
		if (id.equals(MODELNAME_ID))
			return deviceModel.getModelName();
		if (id.equals(DEVICEUSE_ID))
			return deviceModel.getDeviceUse();
		if (id.equals(UTILITYGRADE_ID))
			return (deviceModel.isUtilityGrade()) ? "true" : "false";
		return null;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// Do nothing--not resettable.
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (value != null) return;
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		// Initiate the transaction
		try {
			EntityTransaction transaction = em.getTransaction();
			try {

				String setString = (String) value;
				if (id.equals(MODELNAME_ID))
					deviceModel.setModelName(setString);
				/*
					if (id.equals(DEVICEUSE_ID))
					deviceModel.setDeviceUse(Integer.valueOf(setString));
				 */
				if (id.equals(UTILITYGRADE_ID))
					deviceModel.setUtilityGrade(Boolean.parseBoolean(setString));
				// Persist the updated manufacturer
				em.merge(deviceModel);
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} catch (Exception e) {
			System.out.println("Problem persisting updated device model-->" + deviceModel.getModelName() + ": " + e.toString());
		} finally {
			em.close();
		}
	}

	@Override
	public boolean isPropertyResettable(Object id) {
		return false;
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (id.equals(MODELNAME_ID))
			return !(Strings.isNullOrEmpty(deviceModel.getModelName()));
		if (id.equals(DEVICEUSE_ID))
			return (deviceModel.getDeviceUse() != null);
		if (id.equals(UTILITYGRADE_ID))
			return (deviceModel.isUtilityGrade() != null);
		return false;
	}

}
