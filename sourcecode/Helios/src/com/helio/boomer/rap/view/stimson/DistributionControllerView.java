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
import com.helio.boomer.rap.session.DistributionUnitDataStore;
import com.helio.boomer.rap.session.SessionDataStore;

public class DistributionControllerView extends ViewPart {

	public static final String ID = "com.helio.boomer.rap.view.stimson.distributioncontrollerview";

	private final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	
	private DistributionDashboardView distributionDashboardView = (DistributionDashboardView) PlatformUI
		.getWorkbench()
		.getActiveWorkbenchWindow()
		.getActivePage()
		.findView(DistributionDashboardView.ID);
	
	private DistributionNavView distributionNavView = (DistributionNavView) PlatformUI
		.getWorkbench()
		.getActiveWorkbenchWindow()
		.getActivePage()
		.findView(DistributionNavView.ID);
	
	private boolean showProgressMessages = false;
	private boolean includeLocationName = false;

	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;

	private ComboViewer startPeriodComboViewer;
	private ComboViewer endPeriodComboViewer;
	
	private Period sessionOnPeriod;
	private Period sessionLastPeriod;
	

	/*
	 *  RMAP:74 
	 *  Below 3 button has been declared as class variable.
	 *  Earlier is was local final.
	 */
	Button legendButton;
	Button axesButton;
	Button titleButton;
	

	
	public DistributionControllerView() {

	}
	
	/*
	 * RMAP-74
	 * Sprint 1.0.2
	 */
		private void populateLastLoginValues(DistributionUnitDataStore budStore)
		{	
			
			try
			{
				// Controller View
				distributionNavView.expandTree(budStore.getExpandedElements());
				distributionNavView.setSelectedElements(budStore.getSelectedElement());
				
				// Graph View				
				legendButton.setSelection(budStore.isShowLegend());							
				axesButton.setSelection(budStore.isShowAxesLabels());
				titleButton.setSelection(budStore.isShowTitles());
				distributionDashboardView.setShowTitles(budStore.isShowTitles());
				distributionDashboardView.setShowAxesLabels(budStore.isShowAxesLabels());
				distributionDashboardView.setShowLegends(budStore.isShowLegend());
				
				
				// Distribution (LOC)
				startPeriodComboViewer.setSelection(budStore.getSelectedPeriodBegin());
				endPeriodComboViewer.setSelection(budStore.getSelectedPeriodEnd());
				
				dateTimeStart.setDate(budStore.getBeginYear(),budStore.getBeginMonth(),budStore.getBeginDay());
				dateTimeEnd.setDate(budStore.getEndYear(),budStore.getEndMonth(),budStore.getEndDay());
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
		PShelf shelf = new PShelf(parent, SWT.NONE );
		//
		PShelfItem distributionShelfItem = new PShelfItem(shelf, SWT.NONE );
		distributionShelfItem.setText("Distribution (Loc)");
		distributionShelfItem.getBody().setLayout(new FillLayout());

		ScrolledComposite distScrolledComposite = new ScrolledComposite(distributionShelfItem.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		distScrolledComposite.setExpandHorizontal(true);
		distScrolledComposite.setExpandVertical(true);

		Composite distComposite = new Composite(distScrolledComposite, SWT.NONE);
		distComposite.setLayout(new GridLayout(2, false));

		Label lblStartDate = new Label(distComposite, SWT.NONE);
		lblStartDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStartDate.setText("Start Date:");

		dateTimeStart = new DateTime(distComposite, SWT.BORDER);

		Label lblNewLabel = new Label(distComposite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("End Date:");

		dateTimeEnd = new DateTime(distComposite, SWT.BORDER);

		Label lblStartPeriod = new Label(distComposite, SWT.NONE);
		lblStartPeriod.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStartPeriod.setText("Start Period:");

		startPeriodComboViewer = getStartPeriodComboViewer(distComposite);

		Label lblEndPeriod = new Label(distComposite, SWT.NONE);
		lblEndPeriod.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEndPeriod.setText("End Period:");

		endPeriodComboViewer = getEndPeriodComboViewer(distComposite);

		Button btnRecalcCasesHandled = new Button(distComposite, SWT.NONE);
		
	
		btnRecalcCasesHandled.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar begCalendar = new GregorianCalendar(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay());
				Date begDate = new Date(begCalendar.getTimeInMillis());
				Calendar endCalendar = new GregorianCalendar(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay());
				Date endDate = new Date(endCalendar.getTimeInMillis());
				try {
					List<Period> linkedPeriodList = new LinkedList<Period>();
					Period onPeriod = (Period) ((IStructuredSelection) startPeriodComboViewer.getSelection()).getFirstElement();
					Period lastPeriod = (Period) ((IStructuredSelection) endPeriodComboViewer.getSelection()).getFirstElement();
					
					linkedPeriodList.add(onPeriod);
					while ((onPeriod != lastPeriod) && (onPeriod != null)) {
						onPeriod = onPeriod.nextPeriod;
						if (onPeriod != null) linkedPeriodList.add(onPeriod);
					}
					//
					List<Long> buildingIds = distributionNavView.getSelectedBuildingIds();
					
					/*
					 * RMAP-74
					 * Sprint 1.0.2
					 */
					synchronized(this)
					{
						String sessionId = RWT.getSessionStore().getId();
						SessionDataStore sessionDataStore = (SessionDataStore) RWT.getSessionStore().getAttribute(sessionId);
						DistributionUnitDataStore distributionUnitDataStore = new DistributionUnitDataStore();
						
						// Distribution (LOC)
						distributionUnitDataStore.setOnPeriod(onPeriod);
						distributionUnitDataStore.setLastPeriod(lastPeriod);
						
						distributionUnitDataStore.setBeginDay(dateTimeStart.getDay());
						distributionUnitDataStore.setBeginMonth(dateTimeStart.getMonth());
						distributionUnitDataStore.setBeginYear(dateTimeStart.getYear());
						
						distributionUnitDataStore.setEndDay(dateTimeEnd.getDay());
						distributionUnitDataStore.setEndMonth(dateTimeEnd.getMonth());
						distributionUnitDataStore.setEndYear(dateTimeEnd.getYear());						
						distributionUnitDataStore.setListBuilding(buildingIds);
						distributionUnitDataStore.setSelectedPeriodBegin(startPeriodComboViewer.getSelection());
						distributionUnitDataStore.setSelectedPeriodEnd(endPeriodComboViewer.getSelection());						
						
						// Business Unit Navigation
						distributionUnitDataStore.setListBuilding(buildingIds);						
						distributionUnitDataStore.setExpandedElements(distributionNavView.getExpandedList());
						distributionUnitDataStore.setSelectedElement(distributionNavView.getSelectedElements());
						
						// Graph Control
						distributionUnitDataStore.setShowLegend(distributionDashboardView.getShowLegends());
						distributionUnitDataStore.setShowAxesLabels(distributionDashboardView.getShowAxesLabels());
						distributionUnitDataStore.setShowTitles(distributionDashboardView.getShowTitles());	
						
					
					
					
					/*
					 * RMAP:74 :
					 * The update methods have been modified.
					 * a>The return type for them is changed to distributionUnitDataStore
					 * b> Additional parameter distributionUnitDataStore is passed in method
					 * c>These method has been made synchronized
					 * d>distributionUnitDataStore is then stored in session for returning view
					 */
					if ((buildingIds != null) && (buildingIds.size() > 0)) {
						try {
							processWarningMessage("API Information",
									"About to update Energy Cases Handled Chart");
							distributionUnitDataStore=distributionDashboardView.updateEnergyCasesHandledChart(
									distributionNavView.getSelectedLocationIds(), linkedPeriodList,distributionUnitDataStore);
						} catch (Exception ex1) {
							processWarningMessage("API Error", ex1.toString());
							System.out.println(ex1.getMessage());
						}
						try {
							processWarningMessage("API Information",
									"About to update Energy KWH Building Chart");
							distributionUnitDataStore=distributionDashboardView.updateEnergyKWHBuildingPieChart(
									buildingIds, begDate, endDate,distributionUnitDataStore);
						} catch (Exception ex1) {
							processWarningMessage("API Error", ex1.toString());
							System.out.println(ex1.getMessage());
						}
						try {
							processWarningMessage("API Information",
									"About to update Energy KWH Building Chart");
							distributionUnitDataStore=distributionDashboardView.updateEnergyCHPerBuildingPieChart(
									buildingIds, begDate, endDate,distributionUnitDataStore);
						} catch (Exception ex1) {
							processWarningMessage("API Error", ex1.toString());
							System.out.println(ex1.getMessage());
						}
						try {
							processWarningMessage("API Information",
									"About to update Energy Cost per Building");
							distributionUnitDataStore=distributionDashboardView.updateEnergyCostPerBuildingChart(
									buildingIds, begDate, endDate,distributionUnitDataStore);
						} catch (Exception ex1) {
							processWarningMessage("API Error", ex1.toString());
							System.out.println(ex1.getMessage());
						}
						try {
							processWarningMessage("API Information",
									"About to update Energy Cost per SqFt Chart");
							distributionUnitDataStore=distributionDashboardView.updateEnergyCostPerSqFtChart(
									buildingIds, begDate, endDate,distributionUnitDataStore);
						} catch (Exception ex1) {
							processWarningMessage("API Error", ex1.toString());
							System.out.println(ex1.getMessage());
						}
						try {
							processWarningMessage("API Information",
									"About to update Demand per Location Chart");
							distributionUnitDataStore=distributionDashboardView.updateDemandPerLocationChart(
									distributionNavView.getSelectedLocationIds(), begDate, endDate,distributionUnitDataStore);
						} catch (Exception ex1) {
							processWarningMessage("API Error", ex1.toString());
							System.out.println(ex1.getMessage());
						}
					}
					sessionDataStore.setFirstScreenDistribution(false);						
					sessionDataStore.setDistributionDataStore(distributionUnitDataStore);
			/*
			 * END RMAP-74
			 */	
				}					
				} catch (Exception ex) {
					processWarningMessage("API Error", ex.toString());
					System.out.println(ex.getMessage());
				}
			}
		});
		btnRecalcCasesHandled.setText("Update");
		
/* TEMP COMMENT SECTION */	
/* Add building button being commented out for now, and boolean set to "TRUE"*/
/*		
		final Button btnAddLocation = new Button(distComposite, SWT.CHECK);
		btnAddLocation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				includeLocationName = btnAddLocation.getSelection();
			}
		});
		btnAddLocation.setText("Add Location/Building Names");
*/				
/* This label takes the place of the the above add location button. Comment out if restoring the button. */
		includeLocationName = true;
		new Label(distComposite, SWT.NONE);
/* END TEMP COMMENT SECTION */
		new Label(distComposite, SWT.NONE);
		//
		distScrolledComposite.setContent(distComposite);
		distScrolledComposite.setMinSize(new Point(600, 300));
		//
		PShelfItem graphControlShelfItem = new PShelfItem(shelf, SWT.NONE);
		graphControlShelfItem.setText("Graph Controls");
		graphControlShelfItem.getBody().setLayout(new FillLayout());
		ScrolledComposite graphControlScrolledComposite = new ScrolledComposite(graphControlShelfItem.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		graphControlScrolledComposite.setExpandHorizontal(true);
		graphControlScrolledComposite.setExpandVertical(true);
		Composite graphControlComposite = new Composite(graphControlScrolledComposite, SWT.NONE);
		graphControlComposite.setLayout(new GridLayout(2, false));
		graphControlScrolledComposite.setContent(graphControlComposite);
		graphControlScrolledComposite.setMinSize(graphControlComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		graphControlScrolledComposite.setMinSize(new Point(600, 300));
		//
		Label costAllocationLabel = new Label(graphControlComposite, SWT.NONE);
		costAllocationLabel.setText("Cost Allocation = $0.15");
		
		/*
		 *  RMAP:74 
		 *  This button has been declared as class variable.
		 *  Earlier is was local final.
		 */
		legendButton = new Button(graphControlComposite, SWT.CHECK);
		legendButton.setText("Display Legend");
		legendButton.setSelection(distributionDashboardView.getShowLegends());
		legendButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				distributionDashboardView.setShowLegends(legendButton.getSelection());
			}
		});
		
		/*
		 *  RMAP:74 
		 *  This button has been declared as class variable.
		 *  Earlier is was local final.
		 */
		axesButton = new Button(graphControlComposite, SWT.CHECK);
		axesButton.setText("Display Axes Labels");
		axesButton.setSelection(distributionDashboardView.getShowAxesLabels());
		axesButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				distributionDashboardView.setShowAxesLabels(axesButton.getSelection());
			}
		});
		
		/*
		 *  RMAP:74 
		 *  This button has been declared as class variable.
		 *  Earlier is was local final.
		 */
		titleButton = new Button(graphControlComposite, SWT.CHECK);
		titleButton.setText("Display Title");
		titleButton.setSelection(distributionDashboardView.getShowTitles());
		titleButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				distributionDashboardView.setShowTitles(titleButton.getSelection());
			}
		});
		//
		PShelfItem manufacturingShelfItem = new PShelfItem(shelf, SWT.NONE);
		manufacturingShelfItem.setText("Manufacturing (Plants)");
		manufacturingShelfItem.getBody().setLayout(new FillLayout());

//		ScrolledComposite mfgScrolledComposite = new ScrolledComposite(manufacturingShelfItem.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//		mfgScrolledComposite.setExpandHorizontal(true);
//		mfgScrolledComposite.setExpandVertical(true);

		Composite mfgComposite = new Composite(manufacturingShelfItem.getBody(), SWT.BORDER);
		FillLayout mfgFillLayout = new FillLayout();
		mfgFillLayout.type = SWT.VERTICAL;
		mfgComposite.setLayout(mfgFillLayout);

//		mfgScrolledComposite.setContent(mfgComposite);
//		mfgScrolledComposite.setMinSize(mfgComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		mfgScrolledComposite.setMinSize(new Point(600, 300));
		
		Label spacerLabel1 = new Label(mfgComposite, SWT.CENTER);
		Label mfgLoginLabel = new Label(mfgComposite, SWT.CENTER);
		mfgLoginLabel.setText("Manufacturing Login\nRequired");
		//
		
		
		/*
		 * RMAP-74
		 * Sprint 1.0.2
		 */
		synchronized(this)
		{
			String sessionId = RWT.getSessionStore().getId();
			SessionDataStore sessionDataStoreLocal = (SessionDataStore) RWT.getSessionStore().getAttribute(sessionId); 	
			SessionDataStore sessionDataStoreAppl = (SessionDataStore) RWT.getApplicationStore().getAttribute(sessionDataStoreLocal.getUsername());
				
			DistributionUnitDataStore distributionUnitDataStoreAppl=null;
			
			if(sessionDataStoreAppl != null)
			{
				distributionUnitDataStoreAppl = sessionDataStoreAppl.getDistributionDataStore();
				if(distributionUnitDataStoreAppl != null && sessionDataStoreLocal.isFirstScreenDistribution())
				{					
					sessionDataStoreLocal.setFirstScreenDistribution(false);
					populateLastLoginValues(distributionUnitDataStoreAppl);			
				}
			}
			RWT.getSessionStore().setAttribute(sessionId, sessionDataStoreLocal);		
		}
		/*
		 * END RMAP-74
		 */	
		
	}

	private ComboViewer getStartPeriodComboViewer(Composite parent) {
		ComboViewer periodComboViewer = new ComboViewer(parent, SWT.NONE);
		Combo periodCombo = periodComboViewer.getCombo();
		GridData gd_periodCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_periodCombo.widthHint = 150;
		periodCombo.setLayoutData(gd_periodCombo);
		periodComboViewer.setContentProvider(new ArrayContentProvider());
		periodComboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Period) element).toString();
			}
		});
		periodComboViewer.setInput(PeriodListController.getInstance().getPeriodModelListAsArray());
		periodComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection ss = (IStructuredSelection) event.getSelection();
				if (ss.getFirstElement() instanceof Period) {
					Period period = (Period) ss.getFirstElement();
					Calendar startDt = new GregorianCalendar(); 
					startDt.setTime(period.getStartDt());
					Calendar endDt = new GregorianCalendar();
					endDt.setTime(period.getEndDt());
					dateTimeStart.setDate(
							startDt.get(Calendar.YEAR), startDt.get(Calendar.MONTH), startDt.get(Calendar.DAY_OF_MONTH));
					endPeriodComboViewer.setSelection(ss);
				}
			}
		});
		return periodComboViewer;
	}

	private ComboViewer getEndPeriodComboViewer(Composite parent) {
		ComboViewer periodComboViewer = new ComboViewer(parent, SWT.NONE);
		Combo periodCombo = periodComboViewer.getCombo();
		GridData gd_periodCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_periodCombo.widthHint = 150;
		periodCombo.setLayoutData(gd_periodCombo);
		periodComboViewer.setContentProvider(new ArrayContentProvider());
		periodComboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Period) element).toString();
			}
		});
		periodComboViewer.setInput(PeriodListController.getInstance().getPeriodModelListAsArray());
		periodComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection ss = (IStructuredSelection) event.getSelection();
				if (ss.getFirstElement() instanceof Period) {
					Period period = (Period) ss.getFirstElement();
					Calendar startDt = new GregorianCalendar(); 
					startDt.setTime(period.getStartDt());
					Calendar endDt = new GregorianCalendar();
					endDt.setTime(period.getEndDt());
					dateTimeEnd.setDate(
							endDt.get(Calendar.YEAR), endDt.get(Calendar.MONTH), endDt.get(Calendar.DAY_OF_MONTH));
				}
			}
		});
		return periodComboViewer;
	}
	
	private void processWarningMessage(String title, String message) {
		if (showProgressMessages) {
			MessageDialog.openInformation(
					window.getShell(),
					title,
					message);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}