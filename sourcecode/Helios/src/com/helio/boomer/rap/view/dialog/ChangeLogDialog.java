package com.helio.boomer.rap.view.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/*
@Date   : 29-Aug-2012
@Author : RSystems International Ltd
@purpose: Creating a new Dialog to open change Log PDF file in application
@Task   : RMAP-104
*/

public class ChangeLogDialog extends Dialog {

	private static String CHANGE_LOG_DIALOG_TITLE = "PredictEnergy Change Log";

	private Browser browser;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ChangeLogDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CHANGE_LOG_DIALOG_TITLE);
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		browser = new Browser(container, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		browser.setUrl("html/PredictEnergy_Change_Log.pdf");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {		
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

}
