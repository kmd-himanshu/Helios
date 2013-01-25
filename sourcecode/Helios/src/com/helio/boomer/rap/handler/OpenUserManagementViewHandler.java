package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import com.helio.boomer.rap.perspective.BoomerUserMgmtPerspective;

public class OpenUserManagementViewHandler extends AbstractHandler{

	public static String ID = "com.helio.boomer.rap.command.userManagementBoomer";
	
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		
		try {
			System.out.println("inside OpenUserManagementViewHandler ");
			PlatformUI.getWorkbench().showPerspective(
					BoomerUserMgmtPerspective.ID,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}		
		return null;
	}

}
