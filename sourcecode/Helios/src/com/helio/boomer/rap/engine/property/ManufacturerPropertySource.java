package com.helio.boomer.rap.engine.property;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.model.Manufacturer;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class ManufacturerPropertySource implements IPropertySource2 {

	private final Manufacturer manufacturer;

	private final static String MANUFACTURERNAME_ID	= "manufacturerName";
	private final static String ADDRESS1_ID			= "address1";
	private final static String ADDRESS2_ID			= "address2";
	private final static String CITY_ID				= "city";
	private final static String ADDSTATE_ID			= "addState";
	private final static String ZIP_ID				= "zip";
	private final static String PHONE_ID			= "phone";
	private final static String CONTACTNAME_ID		= "contactName";
	
	public ManufacturerPropertySource(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(MANUFACTURERNAME_ID,	"Manufacturer Name"),
				new TextPropertyDescriptor(ADDRESS1_ID,			"Address1"),
				new TextPropertyDescriptor(ADDRESS2_ID,			"Address2"),
				new TextPropertyDescriptor(CITY_ID,				"City"),
				new TextPropertyDescriptor(ADDSTATE_ID,			"State"),
				new TextPropertyDescriptor(ZIP_ID,				"ZIP"),
				new TextPropertyDescriptor(PHONE_ID,			"Phone"),
				new TextPropertyDescriptor(CONTACTNAME_ID,		"Contact Name") };
	}

	@Override
	public Object getPropertyValue(Object id) {
		// Once we start using Java SE 1.7, we can change the following code to a String "switch" statement.
		if (id.equals(MANUFACTURERNAME_ID))
			return manufacturer.getManufacturerName();
		if (id.equals(ADDRESS1_ID))
			return manufacturer.getAddress1();
		if (id.equals(ADDRESS2_ID))
			return manufacturer.getAddress2();
		if (id.equals(CITY_ID))
			return manufacturer.getCity();
		if (id.equals(ADDSTATE_ID))
			return manufacturer.getAddState();
		if (id.equals(ZIP_ID))
			return manufacturer.getZip();
		if (id.equals(PHONE_ID))
			return manufacturer.getPhone();
		if (id.equals(CONTACTNAME_ID))
			return manufacturer.getContactName();
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
				transaction.begin();
				String setString = (String) value;
				if (id.equals(MANUFACTURERNAME_ID))
					manufacturer.setManufacturerName(setString);
				if (id.equals(ADDRESS1_ID))
					manufacturer.setAddress1(setString);
				if (id.equals(ADDRESS2_ID))
					manufacturer.setAddress2(setString);
				if (id.equals(CITY_ID))
					manufacturer.setCity(setString);
				if (id.equals(ADDSTATE_ID))
					manufacturer.setAddState(setString);
				if (id.equals(ZIP_ID))
					manufacturer.setZip(setString);
				if (id.equals(PHONE_ID))
					manufacturer.setPhone(setString);
				if (id.equals(CONTACTNAME_ID))
					manufacturer.setContactName(setString);
				// Persist the updated manufacturer
				em.merge(manufacturer);
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} catch (Exception e) {
			System.out.println("Problem persisting updated manufacturer-->" + manufacturer.getManufacturerName() + ": " + e.toString());
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
		if (id.equals(MANUFACTURERNAME_ID))
			return !(Strings.isNullOrEmpty(manufacturer.getManufacturerName()));
		if (id.equals(ADDRESS1_ID))
			return !(Strings.isNullOrEmpty(manufacturer.getAddress1()));
		if (id.equals(ADDRESS2_ID))
			return !(Strings.isNullOrEmpty(manufacturer.getAddress2()));
		if (id.equals(CITY_ID))
			return !(Strings.isNullOrEmpty(manufacturer.getCity()));
		if (id.equals(ADDSTATE_ID))
			return !(Strings.isNullOrEmpty(manufacturer.getAddState()));
		if (id.equals(ZIP_ID))
			return !(Strings.isNullOrEmpty(manufacturer.getZip()));
		if (id.equals(PHONE_ID))
			return !(Strings.isNullOrEmpty(manufacturer.getPhone()));
		if (id.equals(CONTACTNAME_ID))
			return !(Strings.isNullOrEmpty(manufacturer.getContactName()));
		return false;
	}

}
