package com.helio.boomer.rap.birt.chart;

import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;

import com.google.common.collect.Maps;

public class PlaceholderPieChart extends BoomerBirtChart {

	private Map<String, Double> valueMap;
	
	private ChartWithoutAxes chart;
	private Series seCategory;
	private SeriesDefinition sd;
	private PieSeries sePie;
	
	private TextDataSet categoryValues;
	private NumberDataSet seriesOneValues;

	public PlaceholderPieChart(String chartTitle) {
		super(chartTitle);
		setupValueMaps();
	}

	//////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES //
	//////////////////////////////////////////////////////////////////////////////////
	
	private void setupValueMaps() {
		valueMap = Maps.newHashMap();
//		valueMap.put("Produce",		75.0);
//		valueMap.put("Perishables",	25.0);
	}
	
	public void reassignValueMap(Map<String, Double> newValueMap) {
		this.valueMap = newValueMap;
	}
		
	//////////////////////////////////////////////////////////////////////////////////
	// PUBLIC CLASSES //
	//////////////////////////////////////////////////////////////////////////////////

	private void adjustFont( FontDefinition font ) {
		font.setSize( 8 );
		font.setName( "Verdana" );
	}

	public Chart createChart() {
		
		chart = ChartWithoutAxesImpl.create();
		chart.setDimension( ChartDimension.TWO_DIMENSIONAL_LITERAL );
		Text caption = chart.getTitle().getLabel().getCaption();
		caption.setValue( chartTitle );
		adjustFont( caption.getFont() );
		Legend legend = chart.getLegend();
		legend.setItemType( LegendItemType.CATEGORIES_LITERAL );
		legend.setVisible( true );
		adjustFont( legend.getText().getFont() );
		// Base Series
		seCategory = SeriesImpl.create();
		sd = SeriesDefinitionImpl.create();
		chart.getSeriesDefinitions().add( sd );
		sd.getSeriesPalette().shift( 0 );
		// Orthogonal Series
		sePie = ( PieSeries )PieSeriesImpl.create();
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		populateValues();
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		return chart;
	}
	
	public void populateValues() {
		categoryValues = TextDataSetImpl.create(valueMap.keySet());
		seriesOneValues = NumberDataSetImpl.create(valueMap.values());
		seCategory.setDataSet( categoryValues );
		sd.getSeries().add( seCategory );
		ChartFillHelper.boomerSolidPalette(sd.getSeriesPalette());
		sePie.setDataSet( seriesOneValues );
		sePie.setExplosion( 5 );
		sePie.setRotation( 40 );
		sePie.getLabel().setVisible( false );
		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add( sdCity );
		sdCity.getSeries().add( sePie );
	}
	
}