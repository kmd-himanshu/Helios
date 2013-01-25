package com.helio.boomer.rap.graph;

import java.sql.Date;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.type.LineSeries;

public class GraphSeriesSpec {

	public Date startDate;
	public Date endDate;
	public Boolean enabled;
	public LineSeries lineSeries;
	public ColorDefinition colorDefinition;
	
	public GraphSeriesSpec(LineSeries lineSeries, Date startDate, Date endDate, ColorDefinition colorDefinition, Boolean enabled) {
		this.lineSeries = lineSeries;
		this.startDate = startDate;
		this.endDate = endDate;
		this.colorDefinition = colorDefinition;
		this.enabled = true;
	}
	
	public GraphSeriesSpec(LineSeries lineSeries, Date startDate, Date endDate, ColorDefinition colorDefinition) {
		this(lineSeries, startDate, endDate, colorDefinition, true);
	}
	
}
