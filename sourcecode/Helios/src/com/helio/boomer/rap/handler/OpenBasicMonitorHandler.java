package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helio.boomer.rap.view.DeckView;
import com.helio.boomer.rap.view.dialog.LicenseDialog;
/*
@Date   : 14-Aug-2012
@Author : RSystems International Ltd
@purpose: Creating a new Handler class for Basic monitoring items,which is added to Accounts menu in application
@Task   : RMAP-86
*/
public class OpenBasicMonitorHandler extends AbstractHandler {

	public static String ID = "com.helio.boomer.rap.command.openBasicMonitoring";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
				
		return "";
	}

}

