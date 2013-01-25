package com.helio.boomer.rap.engine;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.helio.boomer.rap.engine.model.Manufacturer;
import com.helio.boomer.rap.engine.modellist.ManufacturerModelList;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class ManufacturerListController {

	public static class ManufacturerListControllerHolder {
		private final static ManufacturerListController INSTANCE = new ManufacturerListController();
	}

	private ManufacturerModelList manufacturerModelList;

	private ManufacturerListController() {
		manufacturerModelList = new ManufacturerModelList();
		loadAllManufacturers();
	}

	public static ManufacturerListController getInstance() {
		return ManufacturerListControllerHolder.INSTANCE;
	}

	public ManufacturerModelList getManufacturerList() {
		return manufacturerModelList;
	}

	public void loadAllManufacturers() {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Manufacturer> query = em.createQuery("select m from Manufacturer m", Manufacturer.class);
			for (Manufacturer manufacturer : query.getResultList()) {
				manufacturerModelList.addManufacturer(manufacturer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void addManufacturer(Manufacturer manufacturer) {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.persist(manufacturer); 
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} finally {
			em.close();
		}
		manufacturerModelList.addManufacturer(manufacturer);
	}

}
