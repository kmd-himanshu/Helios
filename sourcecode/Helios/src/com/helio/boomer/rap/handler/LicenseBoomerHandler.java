package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helio.boomer.rap.view.dialog.LicenseDialog;

public class LicenseBoomerHandler extends AbstractHandler {

	public static String ID = "com.helio.boomer.rap.command.licenseBoomer";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Dialog dialog = new LicenseDialog(HandlerUtil.getActiveShell(event));
		dialog.open();
		
		return null;
	}

}

