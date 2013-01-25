package com.helio.boomer.rap.engine.property;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class BuildingPropertySource implements IPropertySource2 {

	private final Building building;

	private final static String BUILDINGNAME_ID			= "buildingName";
	private final static String ABBREVIATION_ID			= "abbreviation";
	private final static String SQFT_ID					= "squaryFeet";
	private final static String ESTIMATE_ID				= "estimate";
	private final static String PERCENTAGELOCSQFT_ID	= "percentageLocSqFt";
	private final static String ENERGY_ID				= "energy";
	
	public BuildingPropertySource(Building building) {
		this.building = building;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(BUILDINGNAME_ID,			"Building Name"),
				new TextPropertyDescriptor(ABBREVIATION_ID,			"Abbreviation"),
				new TextPropertyDescriptor(SQFT_ID,					"Square Feet"),
				new TextPropertyDescriptor(ESTIMATE_ID,				"Estimate"),
				new TextPropertyDescriptor(PERCENTAGELOCSQFT_ID,	"% Loc Sq Ft"),
				new TextPropertyDescriptor(ENERGY_ID,				"Energy") };
	}

	@Override
	public Object getPropertyValue(Object id) {
		// Once we start using Java SE 1.7, we can change the following code to a String "switch" statement.
		if (id.equals(BUILDINGNAME_ID))
			return building.getBuildingName();
		if (id.equals(ABBREVIATION_ID))
			return building.getAbbreviation();
		if (id.equals(SQFT_ID))
			return building.getSquareFeet();
		if (id.equals(ESTIMATE_ID))
			return building.getEstimate();
		if (id.equals(PERCENTAGELOCSQFT_ID))
			return building.getPercentageLocationSquareFeet();
		if (id.equals(ENERGY_ID))
			return building.getEnergy();
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
				if (id.equals(BUILDINGNAME_ID))
					building.setBuildingName(setString);
				if (id.equals(ABBREVIATION_ID))
					building.setAbbreviation(setString);
				if (id.equals(SQFT_ID))
					building.setSquareFeet(Integer.valueOf(setString));
				if (id.equals(ESTIMATE_ID))
					building.setEstimate(Boolean.valueOf(setString));
				if (id.equals(PERCENTAGELOCSQFT_ID))
					building.setPercentageLocationSquareFeet(Float.valueOf(setString));
				if (id.equals(ENERGY_ID))
					building.setEnergy(Float.valueOf(setString));
				// Persist the updated building
				em.merge(building);
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} catch (Exception e) {
			System.out.println("Problem persisting updated building-->" + building.getName() + ": " + e.toString());
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
		if (id.equals(BUILDINGNAME_ID))
			return !(Strings.isNullOrEmpty(building.getBuildingName()));
		if (id.equals(ABBREVIATION_ID))
			return !(Strings.isNullOrEmpty(building.getAbbreviation()));
		if (id.equals(SQFT_ID))
			return (building.getSquareFeet() != null);
		if (id.equals(ESTIMATE_ID))
			return (building.getEstimate() != null);
		if (id.equals(PERCENTAGELOCSQFT_ID))
			return (building.getPercentageLocationSquareFeet() != null);
		if (id.equals(ENERGY_ID))
			return (building.getEnergy() != null);
		return false;
	}

}
