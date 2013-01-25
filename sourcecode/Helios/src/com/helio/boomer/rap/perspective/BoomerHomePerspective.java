package com.helio.boomer.rap.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.helio.boomer.rap.NavigationView;
import com.helio.boomer.rap.View;
import com.helio.boomer.rap.view.BusinessUnitServicesView;
import com.helio.boomer.rap.view.CumulativeKWView;
import com.helio.boomer.rap.view.DeckView;
import com.helio.boomer.rap.view.DeviceGraphView;
import com.helio.boomer.rap.view.DeviceNavView;
import com.helio.boomer.rap.view.DivisionNavView;
import com.helio.boomer.rap.view.LandingPageView;
import com.helio.boomer.rap.view.LocationMapView;
import com.helio.boomer.rap.view.OverlayBarKWView;
import com.helio.boomer.rap.view.ReportControllerView;
import com.helio.boomer.rap.view.stimson.MainLeftControlNavView;

public class BoomerHomePerspective implements IPerspectiveFactory {

  public static final String ID
    = "com.helio.boomer.rap.perspective.boomerhomeperspective";

  public void createInitialLayout( final IPageLayout layout ) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		//
		IFolderLayout navFolder = layout.createFolder("navigation", IPageLayout.LEFT, 0.23f, editorArea);
		navFolder.addView(DivisionNavView.ID);
		navFolder.addView(DeviceNavView.ID);
//		navFolder.addView(NavigationView.ID);
		//
		IFolderLayout controllerFolder = layout.createFolder("controller", IPageLayout.BOTTOM, 0.65f, "navigation");
		controllerFolder.addView(BusinessUnitServicesView.ID);
		controllerFolder.addView(ReportControllerView.ID);
		controllerFolder.addView(MainLeftControlNavView.ID);
		//
		IFolderLayout folder = layout.createFolder("central", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		folder.addView(DeviceGraphView.ID);
		folder.addView(CumulativeKWView.ID);
		folder.addView(LandingPageView.ID);
		folder.addView(OverlayBarKWView.ID);
		folder.addView(DeckView.ID);
		folder.addView(LocationMapView.ID);
//		folder.addView(BoomerHTMLView.ID);
//		folder.addView(BoomerPDFView.ID);

		//layout.addView("org.eclipse.ui.views.PropertySheet", IPageLayout.RIGHT, 0.8f, "central");
		layout.addPlaceholder("org.eclipse.ui.views.PropertySheet", IPageLayout.RIGHT, 0.8f, "central");

//		layout.getViewLayout(NavigationView.ID).setCloseable(false);
  }

}