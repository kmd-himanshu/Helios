package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helio.boomer.rap.engine.ManufacturerListController;
import com.helio.boomer.rap.engine.model.Manufacturer;
import com.helio.boomer.rap.view.dialog.AddManufacturerDialog;

public class AddManufacturerDialogHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AddManufacturerDialog mfrDialog = new AddManufacturerDialog(
				HandlerUtil.getActiveShell(event));
		if (mfrDialog.open() == Window.OK) {
			try {
				Manufacturer mfgCandidate = mfrDialog.getMfgCandidate();
				if (MessageDialog.openConfirm(
						HandlerUtil.getActiveShell(event),
						"New Manufacturer Entered",
						"Are you sure you want to add manufacturer "
							+ mfgCandidate.toString()
							+ " to the system?")) {
					ManufacturerListController.getInstance().addManufacturer(mfgCandidate);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		return null;
	}

}
