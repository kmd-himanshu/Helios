package com.helio.boomer.rap.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.helio.boomer.rap.NavigationView;
import com.helio.boomer.rap.View;
import com.helio.boomer.rap.view.DeckView;
import com.helio.boomer.rap.view.DeviceNavView;
import com.helio.boomer.rap.view.LocationMapView;
import com.helio.boomer.rap.view.stimson.BusinessUnitControllerView;
import com.helio.boomer.rap.view.stimson.BusinessUnitDashboardView;
import com.helio.boomer.rap.view.stimson.BusinessUnitNavView;
import com.helio.boomer.rap.view.stimson.ReportGenerationView;
import com.helio.boomer.rap.view.stimson.UserManagementControllerView;
import com.helio.boomer.rap.view.stimson.UserManagementDashboardView;
import com.helio.boomer.rap.view.stimson.UserManagementNavView;

public class BoomerUserMgmtPerspective implements IPerspectiveFactory {

  public static final String ID
    = "com.helio.boomer.rap.perspective.boomerusermgmtperspective";

  public void createInitialLayout( final IPageLayout layout ) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		System.out.println("Inside boomer perspective");
		//
		IFolderLayout navFolder = layout.createFolder("navigation", IPageLayout.LEFT, 0.23f, editorArea);
		navFolder.addView(UserManagementNavView.ID);		
//		navFolder.addView(ReportGenerationView.ID);
//		navFolder.addView(DeviceNavView.ID);
		//
//		IFolderLayout controllerFolder = layout.createFolder("controller", IPageLayout.BOTTOM, 0.65f, "navigation");
//		controllerFolder.addView(UserManagementControllerView.ID);
		//
		IFolderLayout folder = layout.createFolder("central", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		folder.addView(UserManagementDashboardView.ID);
//		folder.addView(DeckView.ID);
//		folder.addView(LocationMapView.ID);

		layout.addPlaceholder("org.eclipse.ui.views.PropertySheet", IPageLayout.RIGHT, 0.8f, "central");

		layout.getViewLayout(NavigationView.ID).setCloseable(false);
  }

}