package com.helio.boomer.rap.birt.chart;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.MarkerLineImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.script.IChartScriptContext;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.service.HelioServiceDelegate;
import com.helio.boomer.rap.utility.NumericUtility;

public class PlaceholderHorizBarChart extends BoomerBirtChart {

	long trackingMonitor = 8L;

	private Date startDt	= Date.valueOf("2011-10-01");
	private Date endDt		= Date.valueOf("2011-10-02");

	private Map<String, Double> valueMap;
	private Map<String, Double> valueMap2;

	private NumberDataSet orthoValuesDataSet1;
	private NumberDataSet orthoValuesDataSet2;

	private Axis xAxis;
	private Axis yAxis;

	private boolean showPreviousSeries;

	public PlaceholderHorizBarChart(String chartTitle, boolean showPreviousSeries) {
		super(chartTitle);
		this.setXAxisLabel("X-Axis");
		this.setYAxisLabel("Y-Axis");
		this.showPreviousSeries = showPreviousSeries;
		setupValueMaps();
	}

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

	private void setupValueMaps() {
		valueMap = Maps.newHashMap();
		valueMap.put("Tracy",				0.0);
		valueMap.put("El Monte",			0.0);
		valueMap.put("Santa Fe Springs",	0.0);

		valueMap2 = Maps.newHashMap();
		valueMap2.put("Tracy",				0.0);
		valueMap2.put("El Monte",			0.0);
		valueMap2.put("Santa Fe Springs",	0.0);

	}

	//////////////////////////////////////////////////////////////////////////////////
	// PUBLIC CLASSES //
	//////////////////////////////////////////////////////////////////////////////////

	public void reassignValueMap(Map<String, Double> newValueMap) {
		this.valueMap.clear();
		for (Entry<String, Double> valueEntry : newValueMap.entrySet()) {
			this.valueMap.put(valueEntry.getKey(), NumericUtility.getPrecDouble(valueEntry.getValue(), 2));
		}
	}

	public void reassignValueMap2(Map<String, Double> newValueMap) {
		this.valueMap2.clear();
		for (Entry<String, Double> valueEntry : newValueMap.entrySet()) {
			Double newValue = NumericUtility.modifyByRandom(valueEntry.getValue());
			if (newValue < 0.0) newValue = 0.0;
			this.valueMap2.put(valueEntry.getKey(), NumericUtility.getPrecDouble(newValue, 2));
		}
	}

	public Chart createChart() {

		birtChart = ChartWithAxesImpl.create();
		birtChart.setDimension( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );
		((ChartWithAxes) birtChart).setOrientation(Orientation.HORIZONTAL_LITERAL);
		// Still experimental
		Interactivity interactive = InteractivityImpl.create();
		interactive.setEnable(true);
		interactive.setLegendBehavior(LegendBehaviorType.HIGHLIGHT_SERIE_LITERAL);
		((ChartWithAxes) birtChart).setInteractivity(interactive);
		// End experimental section
		Plot plot = birtChart.getPlot();
		plot.setBackground( ColorDefinitionImpl.WHITE() );
		plot.getClientArea().setBackground( ColorDefinitionImpl.WHITE() );
		Legend legend = birtChart.getLegend();
		adjustFont(legend.getText().getFont());
		legend.setItemType( LegendItemType.CATEGORIES_LITERAL );
		legend.setVisible( false );
		Text caption = birtChart.getTitle().getLabel().getCaption();
		adjustFont( caption.getFont() );
		caption.setValue( chartTitle );
		//
		xAxis = (( ChartWithAxes ) birtChart).getPrimaryBaseAxes()[ 0 ];
		//		xAxis.getTitle().setVisible( true );
		//		xAxis.getTitle().getCaption().setValue( xAxisLabel );
		//		adjustFont(xAxis.getTitle().getCaption().getFont());
		//		adjustFont(xAxis.getLabel().getCaption().getFont());
		//
		yAxis = ( ( ChartWithAxes ) birtChart).getPrimaryOrthogonalAxis(xAxis);
		//		yAxis.getTitle().setVisible( yAxisLabel.length() > 0 );
		//		yAxis.getTitle().getCaption().setValue( yAxisLabel );
		//		yAxis.getScale().setStep( 1.0 );
		//		adjustFont(yAxis.getTitle().getCaption().getFont());
		//		adjustFont(yAxis.getLabel().getCaption().getFont());
		//
		populateValues();
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		setChartScripts(birtChart);
		setMarkers(birtChart);
		//		birtChart.setScript(
		//				"com.helio.boomer.rap.birt.chart.ThresholdChartEventHandlers" );
		return birtChart;
	}

	public void populateValues() {
		TextDataSet categoryValues = TextDataSetImpl.create( valueMap.keySet() );
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet( categoryValues );
		adjustFont( seCategory.getLabel().getCaption().getFont(), 8 );
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift( 1 );
		xAxis.getSeriesDefinitions().clear();
		xAxis.getSeriesDefinitions().add( sdX );
		sdX.getSeries().add( seCategory );
		//
		orthoValuesDataSet1 = NumberDataSetImpl.create( valueMap.values() );
		if (showPreviousSeries) {
			orthoValuesDataSet2 = NumberDataSetImpl.create( valueMap2.values() );
		}
		yAxis.getSeriesDefinitions().clear();
		BarSeries bs1 = ( BarSeries ) BarSeriesImpl.create();
		Trigger trigger = TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(
						ActionType.SHOW_TOOLTIP_LITERAL,
						TooltipValueImpl.create(100, "Boomer")));
		bs1.setDataSet( orthoValuesDataSet1 );
		bs1.getTriggers().add(trigger);
		bs1.getDataPoint( ).getComponents( ).clear( );
		bs1.getDataPoint( ).getComponents( ).add( DataPointComponentImpl.create(DataPointComponentType.SERIES_VALUE_LITERAL, null ) );
		bs1.setLabelPosition(Position.OUTSIDE_LITERAL);
		adjustFont( bs1.getLabel().getCaption().getFont(), 8 );

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxis.getSeriesDefinitions().add( sdY );
		yAxis.getTriggers().add(trigger);
		sdX.getSeries().get(0).getTriggers().add(trigger);
		sdY.getSeries().add( bs1 );

		if (showPreviousSeries) {
			BarSeries bs2 = ( BarSeries ) BarSeriesImpl.create();
			bs2.setDataSet( orthoValuesDataSet2 );
			bs2.getTriggers().add(trigger);
			bs2.getDataPoint( ).getComponents( ).clear( );
			bs2.getDataPoint( ).getComponents( ).add( DataPointComponentImpl.create(DataPointComponentType.SERIES_VALUE_LITERAL, null ) );
			bs2.setLabelPosition(Position.OUTSIDE_LITERAL);
			adjustFont( bs2.getLabel().getCaption().getFont(), 8 );
			sdY.getSeries().add( bs2 );
		}

		sdY.getSeries().get(0).getTriggers().add(trigger);
	}

	private void setChartScripts(Chart chart) {
//		chart.setScript(getDataPointFillScript().append("\n").append(getDataPointLabelScript()).toString());
	}
	
	public StringBuffer getDataPointFillScript() {
		StringBuffer sb = new StringBuffer("function beforeDrawDataPoint(dph, fill, icsc)");
		sb.append(" {");
		sb.append(" importPackage( Packages.org.eclipse.birt.chart.model.attribute.impl );");
		sb.append(" val = dph.getOrthogonalValue();");
		sb.append(" if( fill.getClass().isAssignableFrom(ColorDefinitionImpl)){");
		sb.append(" var cd = fill.copyInstance();");
		sb.append(" if (val < 1.5) {");
		sb.append( " fill.set(cd.darker().getRed(), cd.darker().getGreen(), cd.darker().getBlue());");
		sb.append(" } else if (val > 2.5) {");
		sb.append( " fill.set(cd.brighter().getRed(), cd.brighter().getGreen(), cd.brighter().getBlue());");
		sb.append(" }");
		sb.append(" }");
		sb.append(" }");
		return sb;
	}
	
	public StringBuffer getDataPointLabelScript() {
		StringBuffer sb = new StringBuffer("function beforeDrawDataPointLabel(dph, label, icsc)");
		sb.append(" {");
		sb.append(" val = dph.getOrthogonalValue();");
		sb.append(" label.setVisible(true);");
		sb.append(" if (val > 2.0) {");
		sb.append(" label.getCaption().setValue(dph.getSeriesDisplayValue() + val);");
		sb.append(" label.getCaption().getFont().setBold(false);");
		sb.append(" label.getCaption().getFont().setSize(10);");
		sb.append(" label.getCaption().getFont().setName(\"Arial\");");
		sb.append(" } else {");
		sb.append(" }");
		sb.append(" }");
		return sb;
	}

	private void setMarkers(Chart chart) {
		MarkerRange mr = MarkerRangeImpl.create(
				yAxis,
				NumberDataElementImpl.create(1.5),
				NumberDataElementImpl.create(2.5),
				ColorDefinitionImpl.CREAM()
				);
		mr.setLabelAnchor(Anchor.NORTH_EAST_LITERAL);
		MarkerLine ml = MarkerLineImpl.create(yAxis, NumberDataElementImpl.create(1.0));
		ml.setLabelAnchor(Anchor.WEST_LITERAL);
		MarkerLine ml2 = MarkerLineImpl.create(yAxis, NumberDataElementImpl.create(2.0));
		ml2.setLabelAnchor(Anchor.EAST_LITERAL);
		ml2.getLabel().getCaption().setValue("foo");
	}

	private void adjustFont( FontDefinition font ) {
		font.setSize( 8 );
		font.setName( "Verdana" );
	}

}