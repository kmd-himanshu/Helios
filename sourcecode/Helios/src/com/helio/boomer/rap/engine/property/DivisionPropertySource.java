package com.helio.boomer.rap.engine.property;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.persistence.PersistenceManager;
import com.helio.boomer.rap.view.stimson.BusinessUnitNavView;
import com.helio.boomer.rap.view.stimson.DistributionNavView;

public class DivisionPropertySource implements IPropertySource2 {

	private final Division division;

	private final static String DIVISIONNAME_ID	= "divisionName";
	private final static String ABBREVIATION_ID	= "abbreviation";
	
	public DivisionPropertySource(Division division) {
		this.division = division;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new TextPropertyDescriptor(DIVISIONNAME_ID,	"Division Name"),
				new TextPropertyDescriptor(ABBREVIATION_ID,	"Abbreviation")
				};
	}

	@Override
	public Object getPropertyValue(Object id) {
		// Once we start using Java SE 1.7, we can change the following code to a String "switch" statement.
		if (id.equals(DIVISIONNAME_ID))
			return division.getDivisionName();
		if (id.equals(ABBREVIATION_ID))
			return division.getAbbreviation();
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
				if (id.equals(DIVISIONNAME_ID))
					division.setDivisionName(setString);
				if (id.equals(ABBREVIATION_ID))
					division.setAbbreviation(setString);
				// Persist the updated division
				em.merge(division);
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} catch (Exception e) {
			System.out.println("Problem persisting updated division-->" + division.getName() + ": " + e.toString());
		} finally {
			em.close();
		}
		BusinessUnitNavView bunv = (BusinessUnitNavView) PlatformUI
									.getWorkbench()
									.getActiveWorkbenchWindow()
									.getActivePage()
									.findView(BusinessUnitNavView.ID);
		if (bunv != null) bunv.refresh();
		DistributionNavView dnv = (DistributionNavView) PlatformUI
									.getWorkbench()
									.getActiveWorkbenchWindow()
									.getActivePage()
									.findView(DistributionNavView.ID);
		if (dnv != null) dnv.refresh();
	}

	@Override
	public boolean isPropertyResettable(Object id) {
		return false;
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (id.equals(DIVISIONNAME_ID))
			return !(Strings.isNullOrEmpty(division.getDivisionName()));
		if (id.equals(ABBREVIATION_ID))
			return !(Strings.isNullOrEmpty(division.getAbbreviation()));
		return false;
	}

}

