package com.helio.boomer.rap.birt.chart;

import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;

public class ChartFillHelper {

	public static void boomerGradientPalette(Palette palette) {
		palette.getEntries( ).clear( );

		palette.getEntries().add( GradientImpl.create( ColorDefinitionImpl.create(220,237,248), ColorDefinitionImpl.create(80,166,218), 0, false));
		palette.getEntries().add( GradientImpl.create( ColorDefinitionImpl.create(255,213,213), ColorDefinitionImpl.create(242,88,106), 0, false));
		palette.getEntries().add( GradientImpl.create( ColorDefinitionImpl.create(255,247,213), ColorDefinitionImpl.create(232,172,57), 0, false));
		palette.getEntries().add( GradientImpl.create( ColorDefinitionImpl.create(213,255,213), ColorDefinitionImpl.create(128,255,128), 0, false));
		palette.getEntries().add( GradientImpl.create( ColorDefinitionImpl.create(213,255,255), ColorDefinitionImpl.create(64,128,128), 0, false));
		palette.getEntries().add( GradientImpl.create( ColorDefinitionImpl.create(226,226,241), ColorDefinitionImpl.create(128,128,192), 0, false));
	}
	
	public static void boomerSolidPalette(Palette palette) {
		palette.getEntries( ).clear( );

		palette.getEntries().add( ColorDefinitionImpl.create(80,166,218));
		palette.getEntries().add( ColorDefinitionImpl.create(242,88,106));
		palette.getEntries().add( ColorDefinitionImpl.create(232,172,57));
		palette.getEntries().add( ColorDefinitionImpl.create(128,255,128));
		palette.getEntries().add( ColorDefinitionImpl.create(64,128,128));
		palette.getEntries().add( ColorDefinitionImpl.create(128,128,192));
	}
	
}
