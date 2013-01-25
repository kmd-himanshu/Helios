/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 *******************************************************************************/
package com.helio.boomer.rap.design;

import org.eclipse.rap.ui.interactiondesign.layout.model.ILayoutSetInitializer;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;


public class FancyLogoInitializer implements ILayoutSetInitializer {

	public static final String SET_ID
	= "com.helio.boomer.rap.design.fancy.layoutset.logo";

	public static final String LOGO = "header.logo";
	public static final String LOGO_POSITION = "header.logo.position";

	public void initializeLayoutSet( final LayoutSet layoutSet ) {
		//		layoutSet.addImagePath( LOGO, "icons/HES_logo.png" );
		layoutSet.addImagePath( LOGO, "icons/PredictEnergy_Logo_Quartersize.png" );

		// positions
		FormData fdLogo = new FormData();
		fdLogo.right = new FormAttachment( 100, 0 );
		fdLogo.top = new FormAttachment( 0, 57 );
		layoutSet.addPosition( LOGO_POSITION, fdLogo );
	}

}
