package com.helio.boomer.rap.view.stimson;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.session.BusinessUnitDataStore;
import com.helio.boomer.rap.session.SessionDataStore;

public class UserManagementControllerView extends ViewPart {

	public static final String ID = "com.helio.boomer.rap.view.stimson.usermanagementcontrollerview";

	private final IWorkbenchWindow window = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow();

	private BusinessUnitDashboardView businessUnitDashboardView = (BusinessUnitDashboardView) PlatformUI
			.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.findView(BusinessUnitDashboardView.ID);

	private BusinessUnitNavView businessUnitNavView = (BusinessUnitNavView) PlatformUI
			.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.findView(BusinessUnitNavView.ID);

	private boolean showProgressMessages = false;
	private boolean includeLocationName = false;


	

	/*
	 *  RMAP:74 
	 *  Below 3 button has been declared as class variable.
	 *  Earlier is was local final.
	 */
	Button legendButton;;
	Button axesButton;
	Button titleButton;
	
	
	public UserManagementControllerView() {

	}
	
	/*
	 * RMAP-74
	 * Sprint 1.0.2
	 */
	
	private void populateLastLoginValues(BusinessUnitDataStore budStore)
	{		
		Calendar begCalendar = new GregorianCalendar(budStore.getBeginYear(), budStore.getBeginMonth(),
				budStore.getBeginDay());
		Date begDate = new Date(begCalendar.getTimeInMillis());
		
		Calendar endCalendar = new GregorianCalendar(budStore.getEndYear(), budStore.getEndMonth(),
				budStore.getEndDay());
		Date endDate = new Date(endCalendar.getTimeInMillis());
		
				try
				{
					// Controller View
					businessUnitNavView.expandTree(budStore.getExpandedElements());
					businessUnitNavView.setSelectedElements(budStore.getSelectedElement());
					
					// Graph View
					
					legendButton.setSelection(budStore.isShowLegend());							
					axesButton.setSelection(budStore.isShowAxesLabels());
					titleButton.setSelection(budStore.isShowTitles());
					businessUnitDashboardView.setShowTitles(budStore.isShowTitles());
					businessUnitDashboardView.setShowAxesLabels(budStore.isShowAxesLabels());
					businessUnitDashboardView.setShowLegends(budStore.isShowLegend());					
					}
				 catch (Exception ex) {
					processWarningMessage("API Error", ex.toString());
					System.out.println(ex.getMessage());
				}
	}

	/*
	 * END RMAP-74
	 */

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		PShelf shelf = new PShelf(parent, SWT.NONE);
		//
		PShelfItem distributionShelfItem = new PShelfItem(shelf, SWT.NONE);
		distributionShelfItem.setText("Distribution (Loc)");
		distributionShelfItem.getBody().setLayout(new FillLayout());

		ScrolledComposite distScrolledComposite = new ScrolledComposite(
				distributionShelfItem.getBody(), SWT.BORDER | SWT.H_SCROLL
						| SWT.V_SCROLL);
		distScrolledComposite.setExpandHorizontal(true);
		distScrolledComposite.setExpandVertical(true);

		Composite distComposite = new Composite(distScrolledComposite, SWT.NONE);
		distComposite.setLayout(new GridLayout(2, false));

		

		Button btnRecalcCasesHandled = new Button(distComposite, SWT.NONE);
		btnRecalcCasesHandled.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		btnRecalcCasesHandled.setText("Update");

		/* TEMP COMMENT SECTION */
		/*
		 * Add building button being commented out for now, and boolean set to
		 * "TRUE"
		 */
		// final Button btnAddLocation = new Button(distComposite, SWT.CHECK);
		// btnAddLocation.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// includeLocationName = btnAddLocation.getSelection();
		// }
		// });
		// btnAddLocation.setText("Add Location/Building Names");

		/*
		 * This label takes the place of the the above add location button.
		 * Comment out if restoring the button.
		 */		
		
	}	

	private void processWarningMessage(String title, String message) {
		if (showProgressMessages) {
			MessageDialog.openInformation(window.getShell(), title, message);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}