package com.helio.boomer.rap.view.dialog;

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

import com.helio.boomer.rap.engine.model.DeviceModel;
import org.eclipse.swt.widgets.Button;

public class AddDeviceModelDialog extends Dialog {

	private Text modelNameText;

	private DeviceModel deviceModelCandidate;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddDeviceModelDialog(Shell parentShell) {
		super(parentShell);
		parentShell.setText("Add Device Model");
		this.deviceModelCandidate = new DeviceModel();
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		//
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		
		Label lblModelName = new Label(container, SWT.NONE);
		lblModelName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblModelName.setText("Model Name:");
		
		modelNameText = new Text(container, SWT.BORDER);
		modelNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Button utilityGradeCheckButton = new Button(container, SWT.CHECK);
		utilityGradeCheckButton.setText("Utility Grade?");
		
		Label lblDeviceUse = new Label(container, SWT.NONE);
		lblDeviceUse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDeviceUse.setText("Device Use:");
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
//		initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 230);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Add Device Model");
	}
	
	public DeviceModel getDeviceModelCandidate() {
		return deviceModelCandidate;
	}
	
//	private DataBindingContext initDataBindings() {
//        DataBindingContext bindingContext = new DataBindingContext();
//        //
//		IObservableValue manufacturerNameModel = PojoProperties.value(Manufacturer.class, "manufacturerName").observe(mfgCandidate);
//		IObservableValue manufacturerNameWidget = WidgetProperties.text(SWT.Modify).observe(manufacturerText);
//		bindingContext.bindValue(manufacturerNameWidget, manufacturerNameModel);
//		//
//		IObservableValue address1Model = PojoProperties.value(Manufacturer.class, "address1").observe(mfgCandidate);
//		IObservableValue address1Widget = WidgetProperties.text(SWT.Modify).observe(address1Text);
//		bindingContext.bindValue(address1Widget, address1Model);
//		//
//		IObservableValue address2Model = PojoProperties.value(Manufacturer.class, "address2").observe(mfgCandidate);
//		IObservableValue address2Widget = WidgetProperties.text(SWT.Modify).observe(address2Text);
//		bindingContext.bindValue(address2Widget, address2Model);
//		//
//		IObservableValue cityModel = PojoProperties.value(Manufacturer.class, "city").observe(mfgCandidate);
//		IObservableValue cityWidget = WidgetProperties.text(SWT.Modify).observe(cityText);
//		bindingContext.bindValue(cityWidget, cityModel);
//		//
//		IObservableValue stateModel = PojoProperties.value(Manufacturer.class, "addState").observe(mfgCandidate);
//		IObservableValue stateWidget = WidgetProperties.text(SWT.Modify).observe(addStateText);
//		bindingContext.bindValue(stateWidget, stateModel);
//		//
//		IObservableValue zipModel = PojoProperties.value(Manufacturer.class, "zip").observe(mfgCandidate);
//		IObservableValue zipWidget = WidgetProperties.text(SWT.Modify).observe(zipText);
//		bindingContext.bindValue(zipWidget, zipModel);
//		//
//		IObservableValue xxxModel = PojoProperties.value(Manufacturer.class, "phone").observe(mfgCandidate);
//		IObservableValue xxxWidget = WidgetProperties.text(SWT.Modify).observe(phoneText);
//		bindingContext.bindValue(xxxWidget, xxxModel);
//		//
//		IObservableValue contactNameModel = PojoProperties.value(Manufacturer.class, "contactName").observe(mfgCandidate);
//		IObservableValue contactNameWidget = WidgetProperties.text(SWT.Modify).observe(contactNameText);
//		bindingContext.bindValue(contactNameWidget, contactNameModel);
//		//
//        return bindingContext;
//}
	
}
