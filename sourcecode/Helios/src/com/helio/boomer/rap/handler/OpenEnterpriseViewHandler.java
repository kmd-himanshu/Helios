package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.helio.boomer.rap.perspective.BoomerEnterprisePerspective;

public class OpenEnterpriseViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			PlatformUI.getWorkbench().showPerspective(
					BoomerEnterprisePerspective.ID,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return null;
	}
}

