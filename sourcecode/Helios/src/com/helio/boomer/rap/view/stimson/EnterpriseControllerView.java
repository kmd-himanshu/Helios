package com.helio.boomer.rap.view.stimson;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.helio.app.serviceapi.kpi.StandardDeviationKPI;
import com.helio.boomer.rap.engine.servicedata.EnterpriseReportDAO;

public class EnterpriseControllerView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.stimson.enterprisecontrollerview";
	
	private EnterpriseDashboardView enterpriseDashboardView = (EnterpriseDashboardView) PlatformUI
			.getWorkbench()
			.getActiveWorkbenchWindow()
			.getActivePage()
			.findView(EnterpriseDashboardView.ID);

	private Text kpiText;
	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;
	
	public EnterpriseControllerView() {
		
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
		distributionShelfItem.setText("Distribution (Loc)");
		distributionShelfItem.getBody().setLayout(new GridLayout(2, false));
		
		Label lblResult = new Label(distributionShelfItem.getBody(), SWT.NONE);
		lblResult.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblResult.setText("KPI Result");
		
		kpiText = new Text(distributionShelfItem.getBody(), SWT.BORDER);
		kpiText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblStartDate = new Label(distributionShelfItem.getBody(), SWT.NONE);
		lblStartDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStartDate.setText("Start Date:");
		
		dateTimeStart = new DateTime(distributionShelfItem.getBody(), SWT.BORDER);
		
		Label lblNewLabel = new Label(distributionShelfItem.getBody(), SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("End Date:");
		
		dateTimeEnd = new DateTime(distributionShelfItem.getBody(), SWT.BORDER);
		
		Button btnRecalcCasesHandled = new Button(distributionShelfItem.getBody(), SWT.NONE);
		btnRecalcCasesHandled.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar begCalendar = new GregorianCalendar(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay());
				Date begDate = new Date(begCalendar.getTimeInMillis());
				Calendar endCalendar = new GregorianCalendar(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay());
				Date endDate = new Date(endCalendar.getTimeInMillis());
				long id = 1801l;
				try {
					// HelioKPI kpi = null;
					// kpi = CoreServicesImpl.getInstance().getEnergyPerCasesHandledPerLocation(id, begDate, endDate);
					List<StandardDeviationKPI> sdKpis = EnterpriseReportDAO.getDeviationForSlicesInPeriod();
					String timePattern = "hh aa";
				    SimpleDateFormat format = new SimpleDateFormat(timePattern);
				    for (StandardDeviationKPI sdKPI : sdKpis) {
						StringBuffer sb = new StringBuffer("StandardDeviationKPI--> ");
						sb.append(  "MAX: ").append(sdKPI.getMaxValue())
						  .append("; MIN: ").append(sdKPI.getMinValue())
						  .append("; MEAN:").append(sdKPI.getMeanValue())
						  .append("; SD:  ").append(sdKPI.getStandardDeviation())
						  .append("; BEG: ").append(format.format(sdKPI.getBegTime()))
						  .append("; OBS: ").append(format.format(sdKPI.getObservationTime()));
						System.out.println(sb);
					}
					Double[] kpis = EnterpriseReportDAO.getCasesHandledKWhForLocation(id, begDate, endDate);
					String results = "Previous-->" + kpis[0] + "; Latest-->" + kpis[1]; 
					kpiText.setText(results);
					enterpriseDashboardView.updateCasesHandledByLocationChart(id, begDate, endDate);				
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		btnRecalcCasesHandled.setText("Recalc");
		new Label(distributionShelfItem.getBody(), SWT.NONE);
		//
		PShelfItem manufacturingShelfItem = new PShelfItem(shelf, SWT.NONE);
		manufacturingShelfItem.setText("Manufacturing (Plants)");
		manufacturingShelfItem.getBody().setLayout(new FillLayout());
		//
		PShelfItem retailShelfItem = new PShelfItem(shelf, SWT.NONE);
		retailShelfItem.setText("Retail (Stores)");
		retailShelfItem.getBody().setLayout(new FillLayout());
		//
		PShelfItem transportationShelfItem = new PShelfItem(shelf, SWT.NONE);
		transportationShelfItem.setText("Transportation");
		transportationShelfItem.getBody().setLayout(new FillLayout());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}