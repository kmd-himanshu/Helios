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

public class BusinessHeaderInitializer implements ILayoutSetInitializer {

	public static final String SET_ID = "com.helio.boomer.rap.layoutset.header.extended";

	// Header Constants
	public static final String HEADER_LEFT = "header.left"; //$NON-NLS-1$
	public static final String HEADER_LEFT_BG = "header.left.bg"; //$NON-NLS-1$
	public static final String HEADER_WAVE = "header.wave"; //$NON-NLS-1$
	public static final String HEADER_WAVE_LAYER = "header.wave.layer";
	public static final String HEADER_RIGHT_BG = "header.right.bg"; //$NON-NLS-1$
	public static final String HEADER_RIGHT = "header.right";  //$NON-NLS-1$
	
	public static final String IMAGE_PATH = "icons/"; //$NON-NLS-1$

	public void initializeLayoutSet( final LayoutSet layoutSet ) {
		// images
		String path = IMAGE_PATH;
//		layoutSet.addImagePath( HEADER_LEFT, 
//				path + "header_left.png" );  //$NON-NLS-1$
//		layoutSet.addImagePath( HEADER_LEFT_BG, 
//				path + "header_left_bg.png" ); //$NON-NLS-1$
//		layoutSet.addImagePath( HEADER_WAVE, 
//				path + "header_wave.png" ); //$NON-NLS-1$
//		layoutSet.addImagePath( HEADER_RIGHT_BG, 
//				path + "header_right.png" ); //$NON-NLS-1$
//		layoutSet.addImagePath( HEADER_RIGHT, 
//				path + "header_right_bg.png" ); //$NON-NLS-1$
		layoutSet.addImagePath( HEADER_LEFT, 
				path + "header_left.png" );  //$NON-NLS-1$
		layoutSet.addImagePath( HEADER_LEFT_BG, 
				path + "header_left.png" ); //$NON-NLS-1$
		layoutSet.addImagePath( HEADER_WAVE, 
				path + "header_left.png" ); //$NON-NLS-1$
		layoutSet.addImagePath( HEADER_WAVE_LAYER, 
				path + "header_left.png" ); //$NON-NLS-1$
		layoutSet.addImagePath( HEADER_RIGHT_BG, 
				path + "header_left.png" ); //$NON-NLS-1$
		layoutSet.addImagePath( HEADER_RIGHT, 
				path + "header_right_w_logo.png" ); //$NON-NLS-1$

	}

}
