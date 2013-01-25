package com.helio.boomer.rap.engine.property;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.model.BuildingAllocation;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class BuildingAllocationPropertySource implements IPropertySource2 {

	private final BuildingAllocation buildingAllocation;

	private final static String BLDALLOCNAME_ID		= "buildingAllocationName";
	private final static String ABBREVIATION_ID		= "abbreviation";
	private final static String ALLOCATIONTYPE_ID	= "allocationType";
	private final static String ESTSQFT_ID			= "estimatedSquareFeet";
	private final static String PERCENTAGE_ID		= "percentage";
	private final static String STORAGETYPE_ID		= "storageType";

	public BuildingAllocationPropertySource(BuildingAllocation buildingAllocation) {
		this.buildingAllocation = buildingAllocation;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(BLDALLOCNAME_ID,		"Allocation Name"),
				new TextPropertyDescriptor(ABBREVIATION_ID,		"Abbreviation"),
				new TextPropertyDescriptor(ALLOCATIONTYPE_ID,	"Allocation Type"),
				new TextPropertyDescriptor(ESTSQFT_ID,			"Estimate Sq Ft"),
				new TextPropertyDescriptor(PERCENTAGE_ID,		"Percentage"),
				new TextPropertyDescriptor(STORAGETYPE_ID,		"Storage Type") };
	}

	@Override
	public Object getPropertyValue(Object id) {
		// Once we start using Java SE 1.7, we can change the following code to a String "switch" statement.
		if (id.equals(BLDALLOCNAME_ID))
			return buildingAllocation.getBuildingAllocationName();
		if (id.equals(ABBREVIATION_ID))
			return buildingAllocation.getAbbreviation();
		if (id.equals(ALLOCATIONTYPE_ID)) {
			if (buildingAllocation.getAllocationType() != null) {
				return buildingAllocation.getAllocationType().getAllocationTypeName();
			}
		}
		if (id.equals(ESTSQFT_ID))
			return buildingAllocation.getEstimatedSquareFeet();
		if (id.equals(PERCENTAGE_ID))
			return buildingAllocation.getPercentage();
		if (id.equals(STORAGETYPE_ID))
			return buildingAllocation.getStorageType().getFieldValue();
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
				if (id.equals(BLDALLOCNAME_ID))
					buildingAllocation.setBuildingAllocationName(setString);
				if (id.equals(ABBREVIATION_ID))
					buildingAllocation.setAbbreviation(setString);
				if (id.equals(ALLOCATIONTYPE_ID))
					System.out.println("Not installed yet.");
				if (id.equals(ESTSQFT_ID))
					buildingAllocation.setEstimatedSquareFeet(Integer.valueOf(setString));
				if (id.equals(PERCENTAGE_ID))
					buildingAllocation.setPercentage(Float.valueOf(setString));
				if (id.equals(STORAGETYPE_ID))
					System.out.println("Not installed yet.");
				// Persist the updated Building Allocation
				em.merge(buildingAllocation);
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} catch (Exception e) {
			System.out.println("Problem persisting updated building allocation-->" + buildingAllocation.getBuildingAllocationName()
					+ ": " + e.toString());
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
		if (id.equals(BLDALLOCNAME_ID))
			return !(Strings.isNullOrEmpty(buildingAllocation.getBuildingAllocationName()));
		if (id.equals(ABBREVIATION_ID))
			return !(Strings.isNullOrEmpty(buildingAllocation.getAbbreviation()));
		if (id.equals(ALLOCATIONTYPE_ID))
			return (buildingAllocation.getAllocationType() != null);
		if (id.equals(ESTSQFT_ID))
			return (buildingAllocation.getEstimatedSquareFeet() != null);
		if (id.equals(PERCENTAGE_ID))
			return (buildingAllocation.getPercentage() != null);
		if (id.equals(STORAGETYPE_ID))
			return (buildingAllocation.getStorageType() != null);
		return false;
	}

}
