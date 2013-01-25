package com.helio.boomer.rap.view.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import com.helio.boomer.rap.handler.LicenseBoomerHandler;

public class PredictEnergyAboutDialog extends MessageDialog {

	public PredictEnergyAboutDialog(Shell parentShell, String dialogTitle,
			Image dialogTitleImage, String dialogMessage, int dialogImageType,
			String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
				dialogImageType, dialogButtonLabels, defaultIndex);
	}

	@Override
	protected Control createCustomArea(Composite parent) {
		//
		parent.setLayout(new GridLayout(1, false));
		//
		Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		//
		Button btnLicenseDetails = new Button(parent, SWT.NONE);
		btnLicenseDetails.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		btnLicenseDetails.setText("License Details");
		btnLicenseDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
		          IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService( IHandlerService.class );
		          service.executeCommand( LicenseBoomerHandler.ID, null );
				} catch (Exception ex) {
					System.out.println("License display command not found");
					System.err.print(ex);
				}
			}
		});
		return parent;
	}
}
