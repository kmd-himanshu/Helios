package com.helio.boomer.rap.message;

import org.eclipse.osgi.util.NLS;

public class LoginMessages extends NLS {
	private static final String BUNDLE_NAME = "com.helio.boomer.rap.message.loginMessages"; //$NON-NLS-1$
	public static String Application_Application_LOGIN_ERROR_DESC;
	public static String Application_LOGIN_ERROR_TITLE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, LoginMessages.class);
	}

	private LoginMessages() {
	}
}
