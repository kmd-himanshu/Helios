package com.helio.boomer.rap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Opens an &quot;About Boomer&quot; message dialog.
 */
public class AboutAction extends Action {
	
	private final IWorkbenchWindow window;
	
	public AboutAction(IWorkbenchWindow window) {
		super("About");
		setId(this.getClass().getName());
		this.window = window;
	}
	
	public void run() {
		if(window != null) {	
			String title = "About Boomer";

			String copyright = "Copyright 2011-2012 Helio Energy Management Solutions, Inc. All rights reserved.";
			
			StringBuffer msgBuffer = new StringBuffer("PredictEnergy version and build (Release 1.0.1)");
			
			msgBuffer.append("Boomer Control created by PredictEnergy and Helio Energy Solutions.\n\n")
				.append("You can learn more about Boomer at:\n\n")
				.append("http://helioenergysolutions.virtual.vps-host.net")
				.append("\n\n")
				.append("Warning; This software program is protected by copyright laws.")
				.append("Unauthorized reproduction of this program, or any portion of it, may result in severe civil and criminal penalties, ")
				.append("and will be prosecuted to the maximum extent possible under the law.")
				.append("\n\n")
				.append(copyright);
			
			MessageDialog.openInformation( window.getShell(), title, msgBuffer.toString() ); 
		}
	}
	
}
