package com.helio.boomer.rap.view.stimson;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;

public class ReportGenerationView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.stimson.reportgenerationview";

	public ReportGenerationView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(3, false));
		
		Label lblGenerateReports = new Label(parent, SWT.NONE);
		lblGenerateReports.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		lblGenerateReports.setAlignment(SWT.CENTER);
		lblGenerateReports.setText("Generate Reports");
		
		DateTime dateTime = new DateTime(parent, SWT.BORDER | SWT.CALENDAR);
		dateTime.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3));
		new Label(parent, SWT.NONE);
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		lblNewLabel.setText("Periods:");
		
		Label lblStart = new Label(parent, SWT.NONE);
		lblStart.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblStart.setText("Start:");
		
		DateTime dateTime_1 = new DateTime(parent, SWT.BORDER);
		dateTime_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		Label lblEnd = new Label(parent, SWT.NONE);
		lblEnd.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblEnd.setText("End:");
		
		DateTime dateTime_2 = new DateTime(parent, SWT.BORDER);
		dateTime_2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		Group grpOutput = new Group(parent, SWT.NONE);
		grpOutput.setText("Output");
		grpOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		
		Button btnRadioButton = new Button(grpOutput, SWT.RADIO);
		btnRadioButton.setBounds(10, 0, 236, 18);
		btnRadioButton.setText("Print to Screen");

		Button btnRadioButton2 = new Button(grpOutput, SWT.RADIO);
		btnRadioButton2.setBounds(10, 20, 236, 18);
		btnRadioButton2.setText("PDF File");

		Button btnRadioButton3 = new Button(grpOutput, SWT.RADIO);
		btnRadioButton3.setBounds(10, 40, 236, 18);
		btnRadioButton3.setText("CSV File");

		Group grpOutput2 = new Group(parent, SWT.NONE);
		grpOutput2.setText("Standard Reports");
		grpOutput2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		
		Button btnRadioButton10 = new Button(grpOutput2, SWT.RADIO);
		btnRadioButton10.setBounds(10, 0, 236, 18);
		btnRadioButton10.setText("CH/Period (SR)-Energy & Cost");

		Button btnRadioButton11 = new Button(grpOutput2, SWT.RADIO);
		btnRadioButton11.setBounds(10, 20, 236, 18);
		btnRadioButton11.setText("CH/Building (SR)-Energy & Cost");

		Button btnRadioButton12 = new Button(grpOutput2, SWT.RADIO);
		btnRadioButton12.setBounds(10, 40, 246, 18);
		btnRadioButton12.setText("Building Allocation (SB) (SR)-Energy & Cost");

		Button btnRadioButton13 = new Button(grpOutput2, SWT.RADIO);
		btnRadioButton13.setBounds(10, 60, 236, 18);
		btnRadioButton13.setText("Energy Use/sub-meter (SM) (SR)");

		Button btnRadioButton14 = new Button(grpOutput2, SWT.RADIO);
		btnRadioButton14.setBounds(10, 80, 236, 18);
		btnRadioButton14.setText("Energy Use By Building Type (ST)");
		
		Button btnSubmit = new Button(parent, SWT.PUSH);
		btnSubmit.setText("SUBMIT");
		btnSubmit.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));

		Group grpLegend = new Group(parent, SWT.NONE);
		grpLegend.setText("Legend");
		grpLegend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		
		Label lblNewLabel_1 = new Label(grpLegend, SWT.NONE);
		lblNewLabel_1.setBounds(10, 0, 236, 14);
		lblNewLabel_1.setText("CH-Cases Handled");
		
		Label lblNewLabel_2 = new Label(grpLegend, SWT.NONE);
		lblNewLabel_2.setBounds(10, 20, 236, 14);
		lblNewLabel_2.setText("SR-Select Range");
		
		Label lblNewLabel_3 = new Label(grpLegend, SWT.NONE);
		lblNewLabel_3.setBounds(10, 40, 236, 14);
		lblNewLabel_3.setText("SB-Select Building");
		
		Label lblNewLabel_4 = new Label(grpLegend, SWT.NONE);
		lblNewLabel_4.setBounds(10, 60, 236, 14);
		lblNewLabel_4.setText("SM-Select Meter (Monitor Device)");
		
		Label lblNewLabel_5 = new Label(grpLegend, SWT.NONE);
		lblNewLabel_5.setBounds(10, 80, 236, 14);
		lblNewLabel_5.setText("ST-Select Type (Frozen, Refrig, Dry)");
		
		Button btnCustom = new Button(parent, SWT.PUSH);
		btnCustom.setText("Custom Reports");
		btnCustom.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));

}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}

	@Override
	public void dispose() {
		super.dispose();
	}
}