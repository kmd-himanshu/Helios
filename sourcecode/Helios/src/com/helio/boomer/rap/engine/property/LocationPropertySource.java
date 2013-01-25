package com.helio.boomer.rap.engine.property;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.persistence.PersistenceManager;
import com.helio.boomer.rap.view.stimson.DistributionNavView;

public class LocationPropertySource implements IPropertySource2 {

	private final Location location;

	private final static String LOCATIONNAME_ID	= "locationName";
	private final static String ABBREVIATION_ID	= "abbreviation";
	private final static String SQFT_ID			= "squareFeet";
	private final static String ESTIMATE_ID		= "estimate";
	private final static String ENERGY_ID		= "energy";
	
	public LocationPropertySource(Location location) {
		this.location = location;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(LOCATIONNAME_ID,	"Location Name"),
				new TextPropertyDescriptor(ABBREVIATION_ID,	"Abbreviation"),
				new TextPropertyDescriptor(SQFT_ID, "Square Feet"),
				new TextPropertyDescriptor(ESTIMATE_ID, "Estimate"),
				new TextPropertyDescriptor(ENERGY_ID, "Energy") };
	}

	@Override
	public Object getPropertyValue(Object id) {
		// Once we start using Java SE 1.7, we can change the following code to a String "switch" statement.
		if (id.equals(LOCATIONNAME_ID))
			return location.getLocationName();
		if (id.equals(ABBREVIATION_ID))
			return location.getAbbreviation();
		if (id.equals(SQFT_ID))
			return location.getSquareFeet();
		if (id.equals(ESTIMATE_ID))
			return location.getEstimate();
		if (id.equals(ENERGY_ID))
			return location.getEnergy();
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
				if (id.equals(LOCATIONNAME_ID))
					location.setLocationName(setString);
				if (id.equals(ABBREVIATION_ID))
					location.setAbbreviation(setString);
				if (id.equals(SQFT_ID))
					location.setSquareFeet(Integer.valueOf(setString));
				if (id.equals(ESTIMATE_ID))
					location.setEstimate(Boolean.valueOf(setString));
				if (id.equals(ENERGY_ID))
					location.setEnergy(Float.valueOf(setString));
				// Persist the updated location
				em.merge(location);
				transaction.commit();
			} finally {
				// This line keeps the preference work from being committed. Uncomment to lock preferences and keep them from committing.
				if (transaction.isActive()) transaction.rollback();
			}
//			DistributionNavView dnv = (DistributionNavView) PlatformUI
//										.getWorkbench()
//										.getActiveWorkbenchWindow()
//										.getActivePage()
//										.findView(DistributionNavView.ID);
//			dnv.refresh();
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
		if (id.equals(LOCATIONNAME_ID))
			return !(Strings.isNullOrEmpty(location.getLocationName()));
		if (id.equals(ABBREVIATION_ID))
			return !(Strings.isNullOrEmpty(location.getAbbreviation()));
		if (id.equals(SQFT_ID))
			return (location.getSquareFeet() != null);
		if (id.equals(ESTIMATE_ID))
			return (location.getEstimate() != null);
		if (id.equals(ENERGY_ID))
			return (location.getEnergy() != null);
		return false;
	}

}
