package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helio.boomer.rap.view.dialog.ChangeLogDialog;
import com.helio.boomer.rap.view.dialog.LicenseDialog;


/*
 @Date   : 29-Aug-2012
 @Author : RSystems International Ltd
 @purpose: Creating a new Handler class for Change Log items,which is added to Help menu in application
 @Task   : RMAP-104
 */

public class ChangeLogHandler extends AbstractHandler {

	public static String ID = "com.helio.boomer.rap.command.changeLog";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {	
		Dialog dialog = new ChangeLogDialog(HandlerUtil.getActiveShell(event));
		dialog.open();
		return null;
	}

}
