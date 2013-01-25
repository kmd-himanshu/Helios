package com.helio.boomer.rap.handler;

import javax.servlet.http.HttpSession;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.helio.boomer.rap.session.Logout;
import com.ibm.icu.text.MessageFormat;

/*
 @Date   : 13-Aug-2012
 @Author : RSystems International Ltd
 @purpose: Creating a new Handler class for Logout items,which is added to Admin menu in application
 @Task   : RMAP-86
 */
public class OpenLogoutViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Display display = Display.getCurrent();

		Shell shell = new Shell(display, SWT.NONE);
		shell.setBounds(400, 200,20,50);
		boolean result = MessageDialog.openConfirm(shell, "Confirm",
				"Do you really want to logout?");

		if (result) {
			//FOR STORING THE SESSION
			new Logout();			
            performLogout();

		} else {
			// Cancel Button selected do something
		}

		return null;
	}

	public static void performLogout() {		
        String defaultUrl =
            MessageFormat.format("{0}://{1}:{2}{3}",
                new Object[] { RWT.getRequest().getScheme(),               
                               RWT.getRequest().getServerName(),
                               String.valueOf(RWT.getRequest().getServerPort()),
                               RWT.getRequest().getRequestURI()+"?startup=main" });
//        invalidate(RWT.getSessionStore().getHttpSession());
        String browserText = MessageFormat.format(
                "parent.window.location.href = \"{0}\";", new Object[] { defaultUrl});
        JSExecutor.executeJS(browserText);
    }

    private static void invalidate(final HttpSession session) {
        session.setMaxInactiveInterval(1);

        Thread threadInvalidate = new Thread() {
            public void run() {
                session.invalidate();
            }
        };
        threadInvalidate.start();
    }	
}
