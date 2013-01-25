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
@purpose: Creating a new Handler class for Os monitoring items,which is added to Accounts menu in application
@Task   : RMAP-86
*/
public class OpenDeckMonitorHandler extends AbstractHandler {

	public static String ID = "com.helio.boomer.rap.command.opendeck";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ExternalBrowser.open("com.helio.boomer.locations.deck",
                DeckView.BASE_URL, 
                ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR | 
                ExternalBrowser.STATUS);
		
		return null;
	}

}

