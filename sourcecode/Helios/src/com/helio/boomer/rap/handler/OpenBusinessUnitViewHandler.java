package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.rwt.RWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.helio.boomer.rap.perspective.BoomerBusinessUnitPerspective;
import com.helio.boomer.rap.session.SessionDataStore;

public class OpenBusinessUnitViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		
		/*
		 * RMAP-74
		 * Sprint 1.0.2
		 */
			String sessionId = RWT.getSessionStore().getId();	
			SessionDataStore sessionDataStoreLocal = (SessionDataStore) RWT.getSessionStore().getAttribute(sessionId);		
			sessionDataStoreLocal.setLastPerspective(BoomerBusinessUnitPerspective.ID);
			RWT.getSessionStore().setAttribute(sessionId, sessionDataStoreLocal);
		/*
		 * END RMAP-74
		 */	
		
		
		try {
			
			PlatformUI.getWorkbench().showPerspective(
					BoomerBusinessUnitPerspective.ID,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return null;
	}
}

