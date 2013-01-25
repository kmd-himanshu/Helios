package com.helio.boomer.rap.engine;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import com.helio.boomer.rap.engine.model.DeviceMonitor;
import com.helio.boomer.rap.engine.modellist.DeviceMonitorModelList;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class DeviceMonitorListController {

	public static class DeviceMonitorListControllerHolder {
		private final static DeviceMonitorListController INSTANCE = new DeviceMonitorListController();
	}

	private DeviceMonitorModelList deviceMonitorList;

	private DeviceMonitorListController() {
		deviceMonitorList = new DeviceMonitorModelList();
		loadAllDeviceMonitors();
	}

	public static DeviceMonitorListController getInstance() {
		return DeviceMonitorListControllerHolder.INSTANCE;
	}

	public DeviceMonitorModelList getDeviceMonitorList() {
		return deviceMonitorList;
	}

	public void loadAllDeviceMonitors() {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<DeviceMonitor> query = em.createQuery("select d from DeviceMonitor m", DeviceMonitor.class);
			for (DeviceMonitor deviceMonitor : query.getResultList()) {
				deviceMonitorList.addDeviceMonitor(deviceMonitor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

}
