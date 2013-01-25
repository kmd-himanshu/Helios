package com.helio.boomer.rap;

import java.util.HashMap;

import org.eclipse.rwt.RWT;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.helio.boomer.rap.perspective.BoomerBusinessUnitPerspective;
import com.helio.boomer.rap.perspective.BoomerDistributionPerspective;
import com.helio.boomer.rap.perspective.BoomerUserMgmtPerspective;
import com.helio.boomer.rap.session.SessionDataStore;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
//	private String PERSPECTIVE_ID = BoomerBusinessUnitPerspective.ID;	
	private String PERSPECTIVE_ID = BoomerUserMgmtPerspective.ID;
	

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		
		/*
		 * RMAP-74
		 * Sprint 1.0.2
		 */
		String sessionId = RWT.getSessionStore().getId();
		SessionDataStore sessionDataStoreLocal = (SessionDataStore)RWT.getSessionStore().getAttribute(sessionId);		
		SessionDataStore sessionDataStoreAppl = (SessionDataStore)RWT.getApplicationStore().getAttribute(sessionDataStoreLocal.getUsername());
		if(sessionDataStoreAppl != null)
		{
			PERSPECTIVE_ID = sessionDataStoreAppl.getLastPerspective();
			sessionDataStoreLocal.setLastPerspective(PERSPECTIVE_ID);			
		}
		else
		{			
			sessionDataStoreLocal.setLastPerspective(PERSPECTIVE_ID);			
		}
		RWT.getSessionStore().setAttribute(sessionId, sessionDataStoreLocal);
		/*
		 * END RMAP-74
		 */	
		return PERSPECTIVE_ID;
	} 
	
}
