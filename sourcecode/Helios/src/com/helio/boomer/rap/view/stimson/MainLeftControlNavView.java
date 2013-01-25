package com.helio.boomer.rap.view.stimson;

import java.sql.Date;

import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.helio.boomer.rap.service.HelioServiceDelegate;

public class MainLeftControlNavView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.stimson.mainleftcontrolnavview";
	private Text apiResultText;


	public MainLeftControlNavView() {
		
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		
		Composite topComposite = new Composite(sashForm, SWT.FILL);
		topComposite.setLayout(new GridLayout(1, false));
		
		apiResultText = new Text(topComposite, SWT.BORDER);
		GridData gd_apiResultText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_apiResultText.widthHint = 223;
		apiResultText.setLayoutData(gd_apiResultText);
		
		Button btnRunApiCall = new Button(topComposite, SWT.NONE);
		btnRunApiCall.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				long x = 9L;
				Object obj = HelioServiceDelegate.getDeviceValuesForDate(x, Date.valueOf("2011-10-01"), Date.valueOf("2011-10-02"));
				apiResultText.setText("Result Text");
			}
		});
		btnRunApiCall.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnRunApiCall.setText("Run API Call");
		
		Composite bottomComposite = new Composite(sashForm, SWT.FILL);
		PShelf shelf = new PShelf(bottomComposite, SWT.NONE );
		shelf.setSize(198, 469);
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
		sashForm.setWeights(new int[] {234, 232});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}