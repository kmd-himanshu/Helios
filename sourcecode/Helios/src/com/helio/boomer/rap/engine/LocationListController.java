package com.helio.boomer.rap.engine;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.modellist.LocationModelList;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class LocationListController {

	public static class LocationListControllerHolder {
		private final static LocationListController INSTANCE = new LocationListController();
	}

	private LocationModelList locationModelList;
	private Map<Integer, Location> locationMap;

	private LocationListController() {
		locationModelList = new LocationModelList();
		locationMap = Maps.newHashMap();
		loadAllLocations();
	}

	public static LocationListController getInstance() {
		return LocationListControllerHolder.INSTANCE;
	}

	public LocationModelList getLocationList() {
		return locationModelList;
	}

	public void loadAllLocations() {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Location> query = em.createQuery("select l from Location l", Location.class);
			for (Location location : query.getResultList()) {
				locationModelList.addLocation(location);
				locationMap.put(location.getId(), location);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void addLocation(Location location) {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.persist(location); 
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} finally {
			em.close();
		}
		locationModelList.addLocation(location);
	}
	
	public Location getLocation(Long locationId) {
		return locationMap.get(locationId.intValue());
	}

}
