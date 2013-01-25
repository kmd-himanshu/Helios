package com.helio.boomer.rap.view;

import java.sql.Date;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.helio.boomer.rap.utility.DateUtilities;

public class ReportControllerView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.reportcontrollerview";
	private DateTime begDateTime;
	private DateTime endDateTime;

	private DeviceGraphView deviceGraphView = (DeviceGraphView) PlatformUI
																	.getWorkbench()
																	.getActiveWorkbenchWindow()
																	.getActivePage()
																	.findView(DeviceGraphView.ID);
	private CumulativeKWView cumulativeGraphView = (CumulativeKWView) PlatformUI
																	.getWorkbench()
																	.getActiveWorkbenchWindow()
																	.getActivePage()
																	.findView(CumulativeKWView.ID);

	public ReportControllerView() {
		
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		Label lblBegDate = new Label(parent, SWT.NONE);
		lblBegDate.setText("Beg Date:");
		
		begDateTime = new DateTime(parent, SWT.BORDER);
		
		Label lblEndDate = new Label(parent, SWT.NONE);
		lblEndDate.setText("End Date:");
		
		endDateTime = new DateTime(parent, SWT.BORDER);
		endDateTime.setEnabled(false);
		
		final Button btnYesterday = new Button(parent, SWT.CHECK);
		btnYesterday.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deviceGraphView.enableDay(DeviceGraphView.DayType.PREVIOUSDAY, btnYesterday.getSelection());
				cumulativeGraphView.enableDay(CumulativeKWView.DayType.PREVIOUSDAY, btnYesterday.getSelection());
			}
		});
		btnYesterday.setText("Preceding Day");
		
		final Button btnFollowingDay = new Button(parent, SWT.CHECK);
		btnFollowingDay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deviceGraphView.enableDay(DeviceGraphView.DayType.NEXTDAY, btnFollowingDay.getSelection());
				cumulativeGraphView.enableDay(CumulativeKWView.DayType.NEXTDAY, btnYesterday.getSelection());
			}
		});
		btnFollowingDay.setText("Following Day");
		
		final Button btnAverage = new Button(parent, SWT.CHECK);
		btnAverage.setText("Average");
		btnAverage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deviceGraphView.enableDay(DeviceGraphView.DayType.AVERAGEDAY, btnAverage.getSelection());
				cumulativeGraphView.enableDay(CumulativeKWView.DayType.AVERAGEDAY, btnYesterday.getSelection());
			}
		});

		new Label(parent, SWT.NONE);
		
		final Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Date initialDate = DateUtilities.getDateFromWidget(begDateTime);
					Date concludeDate = DateUtilities.incrementDateByDay(initialDate, 1);
					deviceGraphView.setDateRange(initialDate, concludeDate);
					cumulativeGraphView.setDateRange(initialDate, concludeDate);
				} catch (IllegalArgumentException ex) {
					System.out.println("Couldn't construct date from widget. " + ex.toString());
				}
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnNewButton.setText("Refresh");
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
}