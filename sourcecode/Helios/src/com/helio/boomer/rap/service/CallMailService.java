package com.helio.boomer.rap.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 @Date   : 24-Aug-2012
 @Author : RSystems International Ltd
 @purpose: Creating a new Handler class for Request Support items,which is added to Help menu in application
 @Task   : RMAP-87
 */
public class CallMailService {

	public void callMail() {

		Display display = Display.getCurrent();

		Shell shell = new Shell(display);

		shell.setLayout(new FillLayout());

		shell.setBounds(0, 0, 0, 0);

		final Browser browser;

		try {

			browser = new Browser(shell, SWT.NONE);

		} catch (SWTError e) {

			System.out.println("Could not instantiate Browser: "
					+ e.getMessage());

			display.dispose();

			return;

		}
		browser.setText(createHTML());

		final BrowserFunction function = new CustomFunction(browser,
				"theJavaFunction");

		browser.addProgressListener(new ProgressAdapter() {

			public void completed(ProgressEvent event) {

				browser.addLocationListener(new LocationAdapter() {

					public void changed(LocationEvent event) {

						browser.removeLocationListener(this);

						System.out
								.println("left java function-aware page, so disposed CustomFunction");

						function.dispose();

					}

				});

			}

		});

		shell.open();

		display.sleep();

		shell.close();

	}

	static class CustomFunction extends BrowserFunction {

		CustomFunction(Browser browser, String name) {

			super(browser, name);

		}

		public Object function(Object[] arguments) {

			System.out
					.println("theJavaFunction() called from javascript with args:");

			for (int i = 0; i < arguments.length; i++) {

				Object arg = arguments[i];

				if (arg == null) {

					System.out.println("\t-->null");

				} else {

					System.out.println("\t-->" + arg.getClass().getName()
							+ ": " + arg.toString());

				}

			}

			Object returnValue = new Object[] {

			new Short((short) 3),

			new Boolean(true),

			null,

			new Object[] { "a string", new Boolean(false) },

			"hi",

			new Float(2.0f / 3.0f),

			};

			// int z = 3 / 0; // uncomment to cause a java error instead

			return returnValue;

		}
	}

	static String createHTML() {

		StringBuffer buffer = new StringBuffer();

		buffer.append("<html>\n");

		buffer.append("<head>\n");

		buffer.append("<script language=\"JavaScript\">\n");

		buffer.append("function email() {\n");

		buffer.append("location.href = \"mailto:support@helioenergysolutions.com?subject=Support\";\n");

		buffer.append("window.close();\n");

		buffer.append("}\n");

		buffer.append("</script>\n");

		buffer.append("</head>\n");

		buffer.append("<body onLoad=\"email();\"\n");

		buffer.append("</body>\n");

		buffer.append("</html>\n");

		return buffer.toString();

	}

}
