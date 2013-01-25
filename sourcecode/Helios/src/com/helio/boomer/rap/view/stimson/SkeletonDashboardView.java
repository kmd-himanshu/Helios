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
import org.eclipse.swt.widgets.Label;

public class SkeletonDashboardView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.stimson.enterprisedashboardview";


	public SkeletonDashboardView() {
		
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		
		SashForm mainSashForm = new SashForm(parent, SWT.HORIZONTAL);
		
		SashForm leftSashForm = new SashForm(mainSashForm, SWT.VERTICAL);
		Composite rightComposite = new Composite(mainSashForm, SWT.FILL);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		Composite rightTopComposite = new Composite(rightComposite, SWT.NONE);
		
		Composite rightMiddleComposite = new Composite(rightComposite, SWT.NONE);
		
		Composite rightBottomComposite = new Composite(rightComposite, SWT.NONE);
		
		Composite stockTickerComposite = new Composite(leftSashForm, SWT.FILL);  // Left-top composite
		stockTickerComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite leftBottomComposite = new Composite(leftSashForm, SWT.FILL);
		leftBottomComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite bottomLeftComposite = new Composite(leftBottomComposite, SWT.NONE);
		
		Composite bottomRightComposite = new Composite(leftBottomComposite, SWT.NONE);
		leftSashForm.setWeights(new int[] {1, 1});
		mainSashForm.setWeights(new int[] {3, 1});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}