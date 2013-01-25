package com.helio.boomer.rap;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.helio.boomer.rap.view.DeckView;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(NavigationView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		folder.addView(View.ID);
		folder.addView(com.helio.boomer.rap.view.DeviceNavView.ID);
		folder.addView(DeckView.ID);
		
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}
}
