package com.helio.boomer.rap.security.login;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.login.LoginException;

import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.helio.boomer.rap.session.SessionDataStore;

public class BoomerLoginModule implements javax.security.auth.spi.LoginModule {

	static Properties PROPS = new Properties();
	private static Properties USERS = new Properties();
	public static final String FILE_LOCATION = "boomer.authentication.file";
	private CallbackHandler callbackHandler;
	private boolean loggedIn;
	private Subject subject;

	public BoomerLoginModule() {
	}

	public void initialize( Subject subject,
			CallbackHandler callbackHandler,
			Map sharedState,
			Map options )
	{
		this.subject = subject;
		this.callbackHandler = callbackHandler;
        try {
        	String fileLocation = System.getenv(FILE_LOCATION);
        	USERS.load(new FileInputStream(fileLocation));
			System.out.println("BoomerLoginModule loaded user properties file");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("BoomerLoginModule properties file not loaded: ");
		}
		
	}

	public boolean login() throws LoginException {
		String sessionId = null;
		Callback label = new TextOutputCallback(
				TextOutputCallback.INFORMATION,
				"Please login! Hint: USERNAME/PASS" );
		NameCallback nameCallback = new NameCallback( "Username:" );
		PasswordCallback passwordCallback = new PasswordCallback( "Password:", false );
		try {
			callbackHandler.handle( new Callback[]{
					label, nameCallback, passwordCallback
			} );
		} catch( Exception e ) {
			e.printStackTrace();
		}

		String username = nameCallback.getName();

		String password = "";
		if( passwordCallback.getPassword() != null ) {
			password = String.valueOf( passwordCallback.getPassword() );
		}

		if( USERS.get( "user."+username ).equals( password ) ) {
			loggedIn = true;
			/*
			 * RMAP-74
			 * Sprint 1.0.2
			 */
				synchronized(this)
				{
					sessionId = RWT.getSessionStore().getId();				
					SessionDataStore sessionDataStoreLocal = new SessionDataStore();
					sessionDataStoreLocal.setFirstScreenBusiness(true);
					sessionDataStoreLocal.setFirstScreenDistribution(true);
					sessionDataStoreLocal.setUsername(username);		
					RWT.getSessionStore().setAttribute(sessionId ,sessionDataStoreLocal);
				}
				
			/*
			 * END RMAP-74
			 */	
			return true;
		}
		return false;
	}

	public boolean commit() throws LoginException {
		//subject.getPublicCredentials().add( USERS );
		subject.getPrivateCredentials().add( Display.getCurrent() );
		subject.getPrivateCredentials().add( SWT.getPlatform() );
		return loggedIn;
	}

	public boolean abort() throws LoginException {
		loggedIn = false;
		return true;
	}

	public boolean logout() throws LoginException {
		loggedIn = false;
		return true;
	}
}
