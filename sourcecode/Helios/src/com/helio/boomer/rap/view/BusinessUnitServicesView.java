package com.helio.boomer.rap.view;

import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class BusinessUnitServicesView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.businessunitservicesview";


	public BusinessUnitServicesView() {
		
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		PShelf shelf = new PShelf(parent, SWT.NONE );
		//
		PShelfItem distributionShelfItem = new PShelfItem(shelf, SWT.NONE);
		distributionShelfItem.setText("DISTRIBUTION");
		distributionShelfItem.getBody().setLayout(new FillLayout());
		//
		PShelfItem manufacturingShelfItem = new PShelfItem(shelf, SWT.NONE);
		manufacturingShelfItem.setText("MANUFACTURING");
		manufacturingShelfItem.getBody().setLayout(new FillLayout());
		//
		PShelfItem retailShelfItem = new PShelfItem(shelf, SWT.NONE);
		retailShelfItem.setText("RETAIL");
		retailShelfItem.getBody().setLayout(new FillLayout());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}