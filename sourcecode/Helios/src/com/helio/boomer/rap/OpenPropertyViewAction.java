package com.helio.boomer.rap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * When run, this action will open the property view.
 */
public class OpenPropertyViewAction extends Action {
	
	private final IWorkbenchWindow window;
	public OpenPropertyViewAction(IWorkbenchWindow window) {
		this.window = window;
        // The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN);
		setText("Open Properties View");
        // Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.helio.boomer.rap", "/icons/sample2.gif"));
	}
	
	public void run() {
		if(window != null) {	
			try {
				window.getActivePage().showView("org.eclipse.ui.views.PropertySheet");
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
