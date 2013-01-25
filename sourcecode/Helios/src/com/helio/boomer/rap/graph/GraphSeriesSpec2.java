package com.helio.boomer.rap.graph;

import java.sql.Date;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.component.Series;

public class GraphSeriesSpec2 {

	public Date startDate;
	public Date endDate;
	public Boolean enabled;
	public Series series;
	public ColorDefinition colorDefinition;
	
	public GraphSeriesSpec2(Series series, Date startDate, Date endDate, ColorDefinition colorDefinition, Boolean enabled) {
		this.series = series;
		this.startDate = startDate;
		this.endDate = endDate;
		this.colorDefinition = colorDefinition;
		this.enabled = true;
	}
	
	public GraphSeriesSpec2(Series series, Date startDate, Date endDate, ColorDefinition colorDefinition) {
		this(series, startDate, endDate, colorDefinition, true);
	}
	
}
