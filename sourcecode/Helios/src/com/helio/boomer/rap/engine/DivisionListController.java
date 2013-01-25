package com.helio.boomer.rap.engine;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.modellist.DivisionModelList;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class DivisionListController {

	public static class DivisionListControllerHolder {
		private final static DivisionListController INSTANCE = new DivisionListController();
	}

	private DivisionModelList divisionModelList;

	private DivisionListController() {
		divisionModelList = new DivisionModelList();
		loadAllDivisions();
	}

	public static DivisionListController getInstance() {
		return DivisionListControllerHolder.INSTANCE;
	}

	public DivisionModelList getDivisionModelList() {
		return divisionModelList;
	}

	public void loadAllDivisions() {
		try {
			EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
			EntityManager em = emf.createEntityManager();
			try {
				TypedQuery<Division> query = em.createQuery("select d from Division d", Division.class);
				for (Division division : query.getResultList()) {
					divisionModelList.addDivision(division);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				em.close();
			}
		} catch (Exception e) {
			MessageDialog.openInformation(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Problem!",
					e.toString());
			e.printStackTrace();
		}
	}

	public void addDivision(Division division) {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.persist(division); 
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} finally {
			em.close();
		}
		divisionModelList.addDivision(division);
	}

}
