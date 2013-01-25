package com.helio.boomer.rap.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class DeckView extends ViewPart {
	public static final String ID = "com.helio.boomer.rap.view.deckview";

	public static final String BASE_URL = "https://secure.deckmonitoring.com/";

	public DeckView() {
	}

	public void createPartControl( Composite parent ) {
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 5;
		parent.setLayout( layout );
		//
		Browser browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browser.setUrl(BASE_URL);
	}

	public void setFocus() {
	}

}