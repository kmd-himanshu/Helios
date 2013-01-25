package com.helio.boomer.rap.birt.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.FontDefinition;

public abstract class BoomerBirtChart {
	
	public String chartTitle;
	public String xAxisLabel;
	public String yAxisLabel;
	
	public Chart birtChart;
	
	public enum DayType {
		MAINDAY,
		PREVIOUSDAY,
		NEXTDAY,
		AVERAGEDAY,
		CUMULATIVEDAY,
		OTHERDAY
	}

	public BoomerBirtChart(String chartTitle) {
		this.chartTitle = chartTitle;
	}
	
	public void setXAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}
	
	public void setYAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}
	
	protected void adjustFont( FontDefinition font, int newSize ) {
		font.setSize( newSize );
		font.setName( "Verdana" );
	}
	
	public abstract Chart createChart();
	
	public Chart getChart() {
		return birtChart;
	}
	
}
