package com.helio.boomer.rap.security.login;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/** Dialog to change the current user password */
public class ChangePasswordDialog extends TitleAreaDialog {
	private Text userName, currentPassword, newPassword1, newPassword2;

	public ChangePasswordDialog(Shell parentShell) {
		super(parentShell);

		// this.userDetailsManager = securityService;
	}

	protected Point getInitialSize() {
		return new Point(300, 250);
	}

	protected Control createDialogArea(Composite parent) {
		Composite dialogarea = (Composite) super.createDialogArea(parent);
		dialogarea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite composite = new Composite(dialogarea, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		userName = createLU(composite, "UserName ");
		currentPassword = createLP(composite, "Old password");
		newPassword1 = createLP(composite, "New password");
		newPassword2 = createLP(composite, "Confirm new password");
		parent.pack();
		return composite;
	}

	private boolean saveInput() {
		String username = userName.getText();
		String oldPassword = currentPassword.getText();
		String newPassword = newPassword1.getText();

		System.out.println("username :" + username);
		System.out.println("oldPassword :" + oldPassword);
		System.out.println("newPassword :" + newPassword);

		return false;
	}

	@Override
	protected void okPressed() {

		if (userName.getText().equals("")
				|| currentPassword.getText().equals("")
				|| newPassword1.getText().equals("")
				|| newPassword2.getText().equals("")) {

			MessageDialog.openError(getShell(), "Error",
					"Any field should not be blank or Empty");
		} else

		if (!newPassword1.getText().equals(newPassword2.getText())) {

			MessageDialog.openError(getShell(), "Error",
					"New Password and Confirm Password should be same");
		} else

		if (currentPassword.getText().equals(newPassword1.getText())
				|| currentPassword.getText().equals(newPassword2.getText())) {

			MessageDialog.openError(getShell(), "Error",
					"Old Password and New Password should not be same");
		}
		if (saveInput()) {
			MessageDialog.openInformation(getShell(), "Info",
					"new Password successfully changed.");
			super.okPressed();
		} else {
			MessageDialog
					.openError(
							getShell(),
							"Error",
							"Password having some technical issue during change.Please contact to System Admin");
			super.okPressed();
		}

	}

	/** Creates label and text field. */
	protected Text createLU(Composite parent, String label) {
		new Label(parent, SWT.NONE).setText(label);
		Text text = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.FILL
				| SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		return text;
	}

	/** Creates label and password. */
	protected Text createLP(Composite parent, String label) {
		new Label(parent, SWT.NONE).setText(label);
		Text text = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.PASSWORD
				| SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		return text;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Change password");
	}

}