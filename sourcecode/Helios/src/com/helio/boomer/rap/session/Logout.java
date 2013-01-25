package com.helio.boomer.rap.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.helio.boomer.rap.perspective.BoomerDistributionPerspective;
import com.helio.boomer.rap.view.stimson.BusinessUnitDashboardView;
import com.helio.boomer.rap.view.stimson.BusinessUnitNavView;
import com.helio.boomer.rap.view.stimson.BusinessUnitControllerView;
import com.helio.boomer.rap.view.stimson.DistributionControllerView;
import com.helio.boomer.rap.view.stimson.DistributionNavView;

public class Logout {
	/*
	 * sessionDataStoreLocal : For storing values during the current session.
	 * sessionDataStoreAppl : For storing values in the application cache of the server
	 * BusinessDataStore : To store values of the BusinessUnitPerspective
	 * DistributionDataStore : To store values of the PlantUnitPerspective 
	 */
	
	public  Logout()
	{		
		String sessionId = RWT.getSessionStore().getId();
		SessionDataStore sessionDataStoreLocal = (SessionDataStore) RWT.getSessionStore().getAttribute(sessionId);	
		SessionDataStore sessionDataStoreAppl = (SessionDataStore) RWT.getApplicationStore().
				getAttribute(sessionDataStoreLocal.getUsername());	
		if(sessionDataStoreAppl != null )
		{
			if(sessionDataStoreLocal.getBusinessDataStore()== null && sessionDataStoreLocal.getDistributionDataStore()== null)
			{
				sessionDataStoreLocal.setBusinessDataStore(sessionDataStoreAppl.getBusinessDataStore());
				sessionDataStoreLocal.setDistributionDataStore(sessionDataStoreAppl.getDistributionDataStore());			
			}
		}
		RWT.getApplicationStore().setAttribute(sessionDataStoreLocal.getUsername(),sessionDataStoreLocal);		
	}

}
