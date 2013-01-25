package com.helio.boomer.rap.handler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.helio.boomer.rap.engine.ManufacturerListController;
import com.helio.boomer.rap.engine.model.Manufacturer;
import com.helio.boomer.rap.persistence.PersistenceManager;

public class AddManufacturerHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			Manufacturer mfgCandidate = new Manufacturer();
			mfgCandidate.setManufacturerName("Mfg1");
			ManufacturerListController.getInstance().addManufacturer(mfgCandidate);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		return null;
	}

}
