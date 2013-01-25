package com.helio.boomer.rap.birt.stimson.distribution;

import java.sql.Date;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;

import com.helio.boomer.rap.birt.chart.BoomerBirtChart;
import com.helio.boomer.rap.service.HelioServiceDelegate;

public class EnergyAndEnergyCostPerSqFtChart extends BoomerBirtChart {

	public EnergyAndEnergyCostPerSqFtChart(String chartTitle) {
		super(chartTitle);
	}

	private ChartWithAxes birtChart;

	long trackingMonitor = 1;

	private Date startDt = Date.valueOf("2011-07-25");
	private Date endDt = Date.valueOf("2011-07-26");
	
	//////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES //
	//////////////////////////////////////////////////////////////////////////////////
	
	private Series getDaySeries() {
		ColorDefinition colorDefinition = ColorDefinitionImpl.BLUE().brighter();
		BarSeries fullDaySeries = (BarSeries) BarSeriesImpl.create();
		fullDaySeries.setRiserOutline(colorDefinition);
		List<Double> deviceValues = HelioServiceDelegate.getDeviceValuesForDate(
				trackingMonitor,
				startDt,
				endDt);
 		NumberDataSet dsValues = NumberDataSetImpl.create(deviceValues);
		fullDaySeries.setDataSet(dsValues);
		//
		return fullDaySeries; 
	}

	//////////////////////////////////////////////////////////////////////////////////
	// PUBLIC CLASSES //
	//////////////////////////////////////////////////////////////////////////////////
	
	public Chart createChart() {
		String[] dayTimes = {
				"12am", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am",
				"noon", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm" 
				};
		
		birtChart = ChartWithAxesImpl.create( );

		// Legend
		birtChart.getLegend( ).setVisible( true );
		Text caption = birtChart.getTitle().getLabel().getCaption();
		caption.setValue( chartTitle );
		adjustFont( caption.getFont(), 12 );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) birtChart ).getPrimaryBaseAxes( )[0];

		Axis yAxisPrimary = ( (ChartWithAxesImpl) birtChart ).getPrimaryOrthogonalAxis( xAxisPrimary );

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create( dayTimes );

		// Create X-Series
		
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet( categoryValues );
		
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);
		
		adjustFont(xAxisPrimary.getLabel().getCaption().getFont(), 8);
		
		// Create Y-Series

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		Palette palette = sdY.getSeriesPalette();
		palette.getEntries().add(ColorDefinitionImpl.RED());
		palette.getEntries().add(ColorDefinitionImpl.ORANGE());
		palette.getEntries().add(ColorDefinitionImpl.YELLOW());
		palette.getEntries().add(ColorDefinitionImpl.GREEN());
		palette.getEntries().add(ColorDefinitionImpl.BLUE());
		palette.getEntries().add(ColorDefinitionImpl.PINK());
		
		yAxisPrimary.getSeriesDefinitions().add( sdY );
		
		sdY.getSeries().add( getDaySeries() );

		adjustFont(yAxisPrimary.getLabel().getCaption().getFont(), 8);

		return birtChart;
	}
	
}