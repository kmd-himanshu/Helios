package com.helio.boomer.rap.engine;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.modellist.BuildingModelList;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class BuildingListController {

	public static class BuildingListControllerHolder {
		private final static BuildingListController INSTANCE = new BuildingListController();
	}

	private BuildingModelList buildingModelList;
	private Map<Integer, Building> buildingMap;

	private BuildingListController() {
		buildingModelList = new BuildingModelList();
		buildingMap = Maps.newHashMap();
		loadAllBuildings();
	}

	public static BuildingListController getInstance() {
		return BuildingListControllerHolder.INSTANCE;
	}

	public BuildingModelList getBuildingList() {
		return buildingModelList;
	}

	public void loadAllBuildings() {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Building> query = em.createQuery("select b from Building b", Building.class);
			for (Building building : query.getResultList()) {
				buildingModelList.addBuilding(building);
				buildingMap.put(building.getId(), building);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void addBuilding(Building building) {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.persist(building); 
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} finally {
			em.close();
		}
		buildingModelList.addBuilding(building);
	}
	
	public Building getBuilding(Long buildingId) {
		return buildingMap.get(buildingId.intValue());
	}

}
