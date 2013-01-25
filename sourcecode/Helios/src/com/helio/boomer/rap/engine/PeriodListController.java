package com.helio.boomer.rap.engine;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.modellist.PeriodModelList;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class PeriodListController {

	public static class PeriodListControllerHolder {
		private final static PeriodListController INSTANCE = new PeriodListController();
	}

	private PeriodModelList periodModelList;
	private Map<Integer, Period> periodMap;

	private PeriodListController() {
		periodModelList = new PeriodModelList();
		periodMap = Maps.newHashMap();
		loadPeriodsForCustomer("SAFEWAY");
	}

	public static PeriodListController getInstance() {
		return PeriodListControllerHolder.INSTANCE;
	}

	public PeriodModelList getPeriodList() {
		return periodModelList;
	}

//	public void loadAllPeriods() {
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager em = emf.createEntityManager();
//		try {
//			TypedQuery<Period> query = em.createQuery("select p from Period p", Period.class);
//			for (Period period : query.getResultList()) {
//				periodModelList.addPeriod(period);
//				periodMap.put(period.getId(), period);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			em.close();
//		}
//	}
	
	public void loadPeriodsForCustomer(String customerName) {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			String queryString = "SELECT p FROM Period p " +
					"order by p.startDt asc";
//					"where p.DOMAINTABLE = ?1 " +
//					"and p.DOMAINCOLUMN = ?2 " +
//					"and p.FIELDNAME like ?3";
			TypedQuery<Period> query = em.createQuery(queryString, Period.class);
//			query.setParameter(1, SchemaConstants.PERIODDOMAINTABLE).setParameter(2, SchemaConstants.PERIODTYPEID_COL);
//			query.setParameter(3, customerName + "%");
			List<Period> periods = query.getResultList();
			Period prevPeriod = null;
			for (Period period : periods) {
				if (prevPeriod != null) prevPeriod.nextPeriod = period;
				period.prevPeriod = prevPeriod;
				prevPeriod = period;
				//
				periodModelList.addPeriod(period);
				periodMap.put(period.getId(), period);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void addPeriod(Period period) {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.persist(period); 
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} finally {
			em.close();
		}
		periodModelList.addPeriod(period);
	}
	
	public Period getPeriod(Long periodId) {
		return periodMap.get(periodId.intValue());
	}

	public Period[] getPeriodModelListAsArray() {
		Period[] periodModelListArray = new Period[periodModelList.getPeriodList().size()];
		int i = 0;
		for (Period period : periodModelList.getPeriodList()) {
			periodModelListArray[i++] = period;
		}
		return periodModelListArray;
	}
	
}
