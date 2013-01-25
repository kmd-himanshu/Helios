package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helio.boomer.rap.view.dialog.PredictEnergyAboutDialog;

public class AboutBoomerHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String title = "About PredictEnergy";

		String copyright = "Copyright 2011-2012 Helio Energy Management Solutions, Inc. All rights reserved.";
		/*
		 * RMAP-105 START
		 */
		StringBuffer msgBuffer = new StringBuffer(
				"PredictEnergy version and build (Release 1.0.2) ");
		/*
		 * RMAP-105 END
		 */

		msgBuffer
				.append("PredictEnergy Control created by PredictEnergy and Helio Energy Solutions.\n\n")
				.append("You can learn more about PredictEnergy at:\n\n")
				.append("http://www.heliopower.com/energy-solutions")
				.append("\n\n")
				.append("Warning; This software program is protected by copyright laws.")
				.append("Unauthorized reproduction of this program, or any portion of it, may result in severe civil and criminal penalties, ")
				.append("and will be prosecuted to the maximum extent possible under the law.")
				.append("\n\n").append(copyright);

		String[] buttons = { "OK", "Cancel" };
		MessageDialog dialog = new PredictEnergyAboutDialog(
				HandlerUtil.getActiveShell(event), title,
				MessageDialog.getDefaultImage(), msgBuffer.toString(),
				MessageDialog.INFORMATION, buttons, 0);
		dialog.open();

		return null;
	}

}
