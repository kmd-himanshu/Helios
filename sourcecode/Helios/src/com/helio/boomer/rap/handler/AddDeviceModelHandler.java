package com.helio.boomer.rap.handler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helio.boomer.rap.engine.model.DeviceModel;
import com.helio.boomer.rap.engine.model.Manufacturer;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class AddDeviceModelHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() > 1) {
				System.out.println("More than one element selected. Just using first one.");
			}
			if (ss.getFirstElement() instanceof Manufacturer) {
				EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
				EntityManager em = emf.createEntityManager();
				try {
					EntityTransaction transaction = em.getTransaction();
					try {
						transaction.begin();
						Manufacturer manufacturer = (Manufacturer) ss.getFirstElement();
						DeviceModel deviceModel = new DeviceModel("New Device Model");
						deviceModel.setManufacturer(manufacturer);
						manufacturer.addDeviceModel(deviceModel);
						em.persist(deviceModel);
						em.merge(manufacturer); 
						transaction.commit();
					} finally {
						if (transaction.isActive()) transaction.rollback();
					}
				} finally {
					em.close();
				}
			}
		}
		return null;
	}
}

