package com.helio.boomer.rap.view.dialog;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.helio.boomer.rap.engine.model.Manufacturer;

public class AddManufacturerDialog extends Dialog {

	private Text manufacturerText;
	private Text address1Text;
	private Text address2Text;
	private Text cityText;
	private Text addStateText;
	private Text zipText;
	private Text phoneText;
	private Text contactNameText;

	private Manufacturer mfgCandidate;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddManufacturerDialog(Shell parentShell) {
		super(parentShell);
		parentShell.setText("Add Manufacturer");
		this.mfgCandidate = new Manufacturer();
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		//
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(6, false));
		
		Label lblManufacturer = new Label(container, SWT.NONE);
		lblManufacturer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblManufacturer.setText("Manufacturer:");
		
		manufacturerText = new Text(container, SWT.BORDER);
		manufacturerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Label lblAddress1 = new Label(container, SWT.NONE);
		lblAddress1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAddress1.setAlignment(SWT.RIGHT);
		lblAddress1.setText("Address1:");
		
		address1Text = new Text(container, SWT.BORDER);
		address1Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Label lblAddress2 = new Label(container, SWT.NONE);
		lblAddress2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAddress2.setAlignment(SWT.RIGHT);
		lblAddress2.setText("Address2:");
		
		address2Text = new Text(container, SWT.BORDER);
		address2Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Label lblCity = new Label(container, SWT.NONE);
		lblCity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCity.setAlignment(SWT.RIGHT);
		lblCity.setText("City:");
		
		cityText = new Text(container, SWT.BORDER);
		GridData gd_cityText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_cityText.widthHint = 198;
		cityText.setLayoutData(gd_cityText);
		
		Label lblAddState = new Label(container, SWT.NONE);
		lblAddState.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAddState.setAlignment(SWT.RIGHT);
		lblAddState.setText("State:");
		
		addStateText = new Text(container, SWT.BORDER);
		GridData gd_addStateText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_addStateText.widthHint = 31;
		addStateText.setLayoutData(gd_addStateText);
		
		Label lblZip = new Label(container, SWT.NONE);
		lblZip.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblZip.setAlignment(SWT.RIGHT);
		lblZip.setText("Zip:");
		
		zipText = new Text(container, SWT.BORDER);
		zipText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblContactName = new Label(container, SWT.NONE);
		lblContactName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblContactName.setAlignment(SWT.RIGHT);
		lblContactName.setText("Contact:");
		
		contactNameText = new Text(container, SWT.BORDER);
		contactNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblPhone = new Label(container, SWT.NONE);
		lblPhone.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPhone.setAlignment(SWT.RIGHT);
		lblPhone.setText("Phone:");
		
		phoneText = new Text(container, SWT.BORDER);
		phoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.get().OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.get().CANCEL_LABEL, false);
		//
		initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 300);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Add Manufacturer");
	}
	
	public Manufacturer getMfgCandidate() {
		return mfgCandidate;
	}
	
	private DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
		IObservableValue manufacturerNameModel = PojoProperties.value(Manufacturer.class, "manufacturerName").observe(mfgCandidate);
		IObservableValue manufacturerNameWidget = WidgetProperties.text(SWT.Modify).observe(manufacturerText);
		bindingContext.bindValue(manufacturerNameWidget, manufacturerNameModel);
		//
		IObservableValue address1Model = PojoProperties.value(Manufacturer.class, "address1").observe(mfgCandidate);
		IObservableValue address1Widget = WidgetProperties.text(SWT.Modify).observe(address1Text);
		bindingContext.bindValue(address1Widget, address1Model);
		//
		IObservableValue address2Model = PojoProperties.value(Manufacturer.class, "address2").observe(mfgCandidate);
		IObservableValue address2Widget = WidgetProperties.text(SWT.Modify).observe(address2Text);
		bindingContext.bindValue(address2Widget, address2Model);
		//
		IObservableValue cityModel = PojoProperties.value(Manufacturer.class, "city").observe(mfgCandidate);
		IObservableValue cityWidget = WidgetProperties.text(SWT.Modify).observe(cityText);
		bindingContext.bindValue(cityWidget, cityModel);
		//
		IObservableValue stateModel = PojoProperties.value(Manufacturer.class, "addState").observe(mfgCandidate);
		IObservableValue stateWidget = WidgetProperties.text(SWT.Modify).observe(addStateText);
		bindingContext.bindValue(stateWidget, stateModel);
		//
		IObservableValue zipModel = PojoProperties.value(Manufacturer.class, "zip").observe(mfgCandidate);
		IObservableValue zipWidget = WidgetProperties.text(SWT.Modify).observe(zipText);
		bindingContext.bindValue(zipWidget, zipModel);
		//
		IObservableValue xxxModel = PojoProperties.value(Manufacturer.class, "phone").observe(mfgCandidate);
		IObservableValue xxxWidget = WidgetProperties.text(SWT.Modify).observe(phoneText);
		bindingContext.bindValue(xxxWidget, xxxModel);
		//
		IObservableValue contactNameModel = PojoProperties.value(Manufacturer.class, "contactName").observe(mfgCandidate);
		IObservableValue contactNameWidget = WidgetProperties.text(SWT.Modify).observe(contactNameText);
		bindingContext.bindValue(contactNameWidget, contactNameModel);
		//
        return bindingContext;
}
	
}
