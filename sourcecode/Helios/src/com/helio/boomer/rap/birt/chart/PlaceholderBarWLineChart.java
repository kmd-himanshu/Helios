package com.helio.boomer.rap.birt.chart;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.preference.PreferenceConstants;
import com.helio.boomer.rap.utility.NumericUtility;

public class PlaceholderBarWLineChart extends BoomerBirtChart implements IBoomerChart {

	private Map<String, Double> valueMap;
	private Map<String, Double> valueMap2;
	
	private Map<String, Double> lineValueMap;

	private NumberDataSet orthoValuesDataSet1;
	private NumberDataSet orthoValuesDataSet2;
	
	private NumberDataSet overlayLineDataSet = null;

	private LabelBlock explainLabel;
	
	private double rotateYAxisLabel = 0.0;

	private Axis xAxis;
	private Axis yAxis;
	private Axis costAxis;
	
	private boolean showLegend = false;
	private boolean showChartTitle = true;
	private boolean showXAxisLabel = false;
	private boolean showYAxisLabel = true;
	private boolean showChartNotes = true;

	private String secondaryYAxisLabel = "";

	public PlaceholderBarWLineChart(String chartTitle) {
		super(chartTitle);
		this.setXAxisLabel("X-Axis");
		this.setYAxisLabel("Y-Axis");
		setupValueMaps();
		initPreferences();
	}

	//////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES //
	//////////////////////////////////////////////////////////////////////////////////
	
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
		explainLabel = (LabelBlock) LabelBlockImpl.create();
		/*
		 * @Date : 24-August-2012
		 * @Author : RSystems International Ltd
		 * @Purpose: RMAP - 6 , Sprint -1
		 */
//		explainLabel.getLabel().getCaption().setValue("");
//		explainLabel.getLabel().getCaption().getFont().setName("Arial");
//		explainLabel.getLabel().getCaption().getFont().setSize(10);
//		explainLabel.getLabel().getCaption().getFont().setItalic(true);
//		explainLabel.setAnchor(Anchor.NORTH_WEST_LITERAL);
//		explainLabel.getLabel().setVisible(showChartNotes);
//		birtChart.getPlot().add(explainLabel);
		//END
		Plot plot = birtChart.getPlot();
		plot.setBackground( ColorDefinitionImpl.WHITE() );
		plot.getClientArea().setBackground( ColorDefinitionImpl.WHITE() );
		Legend legend = birtChart.getLegend();
		adjustFont(legend.getText().getFont());
		legend.setItemType( LegendItemType.CATEGORIES_LITERAL );
		legend.setVisible( showLegend );
		if (showChartTitle) {
			Text caption = birtChart.getTitle().getLabel().getCaption();
			adjustFont( caption.getFont() );
			caption.setValue( chartTitle );
		}
		birtChart.getTitle().setVisible(showChartTitle);
		//
		xAxis = (( ChartWithAxes ) birtChart).getPrimaryBaseAxes()[ 0 ];
		xAxis.getTitle().setVisible( true );
		xAxis.getTitle().setVisible( showXAxisLabel && xAxisLabel.length() > 0 );
		xAxis.getTitle().getCaption().setValue( xAxisLabel );
		adjustFont(xAxis.getTitle().getCaption().getFont());
		adjustFont(xAxis.getLabel().getCaption().getFont());
		//
		yAxis = ( ( ChartWithAxes ) birtChart).getPrimaryOrthogonalAxis(xAxis);
		yAxis.getTitle().setVisible( showYAxisLabel && yAxisLabel.length() > 0 );
		yAxis.getTitle().getCaption().setValue( yAxisLabel );
		adjustFont(yAxis.getTitle().getCaption().getFont());
		adjustFont(yAxis.getLabel().getCaption().getFont());
		//
		costAxis = AxisImpl.create(Axis.ORTHOGONAL);
		costAxis.getTitle().setVisible( showYAxisLabel && secondaryYAxisLabel.length() > 0 );
		costAxis.getTitle().getCaption().setValue( secondaryYAxisLabel );
		costAxis.getTitle().getCaption().getFont().setRotation(90.0);
		costAxis.setTitlePosition(Position.LEFT_LITERAL);
		costAxis.setGapWidth(25.0);
		costAxis.getScale().setStepNumber(24);
		costAxis.getMajorGrid( ).setTickStyle( TickStyle.RIGHT_LITERAL );
		costAxis.setStaggered(false);
		adjustFont(costAxis.getTitle().getCaption().getFont());
		adjustFont(costAxis.getLabel().getCaption().getFont());
		( ( ChartWithAxes ) birtChart).getPrimaryBaseAxes()[0].getAssociatedAxes().add(costAxis);
		//
		populateValues();
		/*
		 * @Date   : 30-July-2012
		 * @Author : RSystems International Ltd
		 * @Purpose: Graphic Controls so that when the Display Legend
		 * 			is turned on, and the Display Axes is turned off,
		 * 			no legends are displayed on the x axis.
		 */
		xAxis.getLabel().setVisible(true);
		// RSI changes | END
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		setChartScripts(birtChart);
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		return birtChart;
	}
	
	public void populateValues() {
		TextDataSet categoryValues = TextDataSetImpl.create( valueMap.keySet() );
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet( categoryValues );
		adjustFont( seCategory.getLabel().getCaption().getFont(), 8 );
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		// Set palette details
		ChartFillHelper.boomerSolidPalette(sdX.getSeriesPalette());
		sdX.getSeriesPalette().shift( 1 );
		xAxis.getSeriesDefinitions().clear();
		xAxis.getSeriesDefinitions().add( sdX );
		sdX.getSeries().add( seCategory );
		xAxis.getLabel().getCaption().getFont().setRotation(rotateYAxisLabel);
		//
		orthoValuesDataSet1 = NumberDataSetImpl.create( valueMap.values() );
		orthoValuesDataSet2 = NumberDataSetImpl.create( valueMap2.values() );
		yAxis.getSeriesDefinitions().clear();
		BarSeries bs1 = ( BarSeries ) BarSeriesImpl.create();
		Trigger trigger = TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(
						ActionType.SHOW_TOOLTIP_LITERAL,
						TooltipValueImpl.create(100, "Custom Tool Tip")));
		BarSeries bs2 = ( BarSeries ) BarSeriesImpl.create();
		bs1.setDataSet( orthoValuesDataSet1 );
		bs2.setDataSet( orthoValuesDataSet2 );
		bs1.getTriggers().add(trigger);
		bs2.getTriggers().add(trigger);
		adjustFont( bs1.getLabel().getCaption().getFont(), 8 );
		adjustFont( bs2.getLabel().getCaption().getFont(), 8 );
		//
		bs1.setLabelPosition(Position.OUTSIDE_LITERAL);
		bs1.getLabel().setVisible(true);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);
		bs2.getLabel().setVisible(true);
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxis.getSeriesDefinitions().add( sdY );
		if ((lineValueMap != null) && (lineValueMap.size() > 0)) {
			SeriesDefinition sdLine = renderLineSeries();
			adjustFont( sdLine.getSeries().get(0).getLabel().getCaption().getFont(), 8);
			costAxis.getSeriesDefinitions().clear();
			costAxis.getSeriesDefinitions().add(sdLine);
			adjustFont(costAxis.getTitle().getCaption().getFont());
			adjustFont(costAxis.getLabel().getCaption().getFont());
		}
		yAxis.getTriggers().add(trigger);
		sdX.getSeries().get(0).getTriggers().add(trigger);
		sdY.getSeries().add( bs2 );
		sdY.getSeries().add( bs1 );
		sdY.getSeries().get(0).getTriggers().add(trigger);
		
		/*
		 * @Date   : 30-July-2012
		 * @Author : RSystems International Ltd
		 * @Purpose: Graphic Controls so that when the Display Legend
		 * 			is turned on, and the Display Axes is turned off,
		 * 			no legends are displayed on the x axis.
		 */
		xAxis.getLabel().setVisible(showXAxisLabel);
		// RSI changes | END
		
	}
	
	private SeriesDefinition renderLineSeries() {
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		overlayLineDataSet = NumberDataSetImpl.create( lineValueMap.values() );
		ls.setDataSet(overlayLineDataSet);
		ls.getLineAttributes().setColor(ColorDefinitionImpl.create(0, 127, 0));
		ls.getLineAttributes().setThickness(0);
		// This turns the line display off--markers are still shown.
		ls.getLineAttributes().setVisible(false);
		ls.getMarkers().get(0).setVisible(false);
//		ls.getMarkers().get(0).setType(MarkerType.CROSS_LITERAL);
//		ls.getMarkers().get(0).setSize(1);
		SeriesDefinition sdYLine = SeriesDefinitionImpl.create();
		sdYLine.getSeries().add(ls);
		return sdYLine;
	}
	
	public void addLineSeries(Map<String, Double> newLineMap) {
		this.lineValueMap = newLineMap;
	}
	
	private void adjustFont( FontDefinition font ) {
		font.setSize( 8 );
		font.setName( "Verdana" );
	}

	/* (non-Javadoc)
	 * @see com.helio.boomer.rap.birt.chart.IBoomerChart#setShowLegend(boolean)
	 */
	@Override
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
		birtChart.getLegend().setVisible(showLegend);
	}

	/* (non-Javadoc)
	 * @see com.helio.boomer.rap.birt.chart.IBoomerChart#setShowChartTitle(boolean)
	 */
	@Override
	public void setShowChartTitle(boolean showChartTitle) {
		this.showChartTitle = showChartTitle;
		birtChart.getTitle().setVisible(showChartTitle);
		explainLabel.getLabel().setVisible(showChartNotes);
	}

	/* (non-Javadoc)
	 * @see com.helio.boomer.rap.birt.chart.IBoomerChart#setShowChartNotes(boolean)
	 */
	@Override
	public void setShowChartNotes(boolean showChartNotes) {
		this.showChartNotes = showChartNotes;
		explainLabel.getLabel().setVisible(showChartNotes);
	}
	
	/* (non-Javadoc)
	 * @see com.helio.boomer.rap.birt.chart.IBoomerChart#setShowXAxisLabel(boolean)
	 */
	@Override
	public void setShowXAxisLabel(boolean showXAxisLabel) {
		this.showXAxisLabel = showXAxisLabel;
		xAxis.getTitle().setVisible( showXAxisLabel );
	}

	/* (non-Javadoc)
	 * @see com.helio.boomer.rap.birt.chart.IBoomerChart#setShowYAxisLabel(boolean)
	 */
	@Override
	public void setShowYAxisLabel(boolean showYAxisLabel) {
		this.showYAxisLabel = showYAxisLabel;
		yAxis.getTitle().setVisible( showYAxisLabel );
		costAxis.getTitle().setVisible( showYAxisLabel );
	}
	
	public void setLineAxisUOM(String uom) {
		this.costAxis.setFormatSpecifier(
				JavaNumberFormatSpecifierImpl.create(uom));
	}
	
	public void setYAxisUOM(String uom) {
		this.yAxis.setFormatSpecifier(
				JavaNumberFormatSpecifierImpl.create(uom));
	}
	
	public void setRotateYAxisLabel(double rotateYAxisLabel) {
		this.rotateYAxisLabel = rotateYAxisLabel;
	}
	
	private void setChartScripts(Chart chart) {
		StringBuffer scripts = 
					getDataPointFillScript()
					.append("\n")
//					.append(getBeforeDrawMarkerScript())
//					.append("\n")
					.append(getBeforeGenerationScript())
					.append("\n");
		chart.setScript(scripts.toString());
	}
	
	public StringBuffer getBeforeDrawSeriesScript() {
		StringBuffer sb = new StringBuffer("function beforeDrawSeries(series, isr, icsc) {")
//			.append(" Packages.java.lang.System.out.println(\"In beforeDrawSeries\");")
			.append(" series.getLabel().getCaption().setValue(\"foo\");")
			.append(" }");
		return sb;
	}
	
	public StringBuffer getDataPointFillScript() {
		StringBuffer sb = new StringBuffer("function beforeDrawDataPoint(dph, fill, icsc)")
			.append(" {")
			.append(" importPackage( Packages.org.eclipse.birt.chart.model.attribute.impl );")
//			.append(" Packages.java.lang.System.out.println(\"In beforeDrawDataPoint\");")
			.append(" val = dph.getOrthogonalValue();")
			.append(" if ( fill.getClass().isAssignableFrom(ColorDefinitionImpl)) {")
			.append(" var cd = fill.copyInstance();")
			.append(" }")
			.append(" }");
		return sb;
	}
	
	public StringBuffer getBeforeDrawMarkerScript() {
		StringBuffer sb = new StringBuffer("function beforeDrawMarker(marker, dph, icsc)");
		sb	.append(" {")
			.append(" importPackage( Packages.org.eclipse.birt.chart.model.attribute) ;")
//			.append(" Packages.java.lang.System.out.println(\"In beforeDrawMarker\");")
			.append(" if ( marker.getClass().isAssignableFrom(MarkerImpl)) {")
			.append(" var cd = marker.getFill().copyInstance();")
			.append(" cd.set(255, 255, 255, 0);")
			.append(" marker.setFill(cd);")
			.append(" }")
			.append(" }")
			;
		return sb;
	}
	
	public StringBuffer getDataPointLabelScript() {
		StringBuffer sb = new StringBuffer("function beforeDrawDataPointLabel(dph, label, icsc)");
		sb	.append(" {")
//			.append(" Packages.java.lang.System.out.println(\"In beforeDrawDataPointLabel\");")
			.append(" val = dph.getOrthogonalValue();")
			.append(" if (val > 2.0) {")
			.append(" label.setVisible(true);")
			.append(" label.getCaption().setValue(val);")
			.append(" label.getCaption().getFont().setBold(false);")
			.append(" label.getCaption().getFont().setSize(10);")
			.append(" label.getCaption().getFont().setName(\"Arial\");")
			.append(" } else {")
			.append(" label.setVisible(false);")
			.append(" }")
			.append(" }");
		return sb;
	}
	
	public StringBuffer getAfterDataSetFilledScript() {
		StringBuffer sb = new StringBuffer("function afterDataSetFilled(series, dataSet, icsc)")
			.append("{")
			.append("")
			.append("}");
		return sb;
	}
	
	public StringBuffer getBeforeGenerationScript() {
		StringBuffer sb = new StringBuffer("function beforeGeneration( chart, icsc )" )
			.append( " {" )
			.append( "importPackage( Packages.org.eclipse.birt.chart.model.attribute ) ;" )
			.append( "importPackage( Packages.org.eclipse.birt.chart.model.data.impl ) ;" )
//			.append(" Packages.java.lang.System.out.println(\"In beforeGenerationScript\"); ")
			.append( "" )
			.append( "var xAxis = chart.getBaseAxes( )[0];" )
// The following gets the main Y-Axis. The series attached to the first definition are for the bar graphs.
			.append( "var yAxis = chart.getOrthogonalAxes( xAxis, true )[0];" )
			.append( "var mainSd = yAxis.getSeriesDefinitions().get(0);")
// Variable declaration for bar series iteration.
			.append( "var iSeries, onSeries, yDataValues, i, yValue;")
			.append( "var minYValue = -1.0, maxYValue = -1.0;")
// Get the min and maxs for the bar series.
			.append( "for (iSeries = mainSd.getSeries().iterator(); iSeries.hasNext();) {")
			.append( " onSeries = iSeries.next();")
			.append( " yDataValues = onSeries.getDataSet().getValues();")
			.append( " for (i = yDataValues.iterator(); i.hasNext(); ) {")
			.append( "   yValue = parseFloat(i.next());")
			.append( "   if ((yValue < minYValue) || (minYValue < 0)) { minYValue = yValue; }" )
			.append( "   if ((yValue > maxYValue) || (maxYValue < 0)) { maxYValue = yValue; }" )
			.append( " }" )
			.append( "}")
			.append( "minYValue -= (minYValue * 0.10);")
			.append( "maxYValue += (maxYValue * 0.10);")
			.append( "" )
			.append( " yAxis.getScale().setMin(NumberDataElementImpl.create( minYValue ));")
			.append( " yAxis.getScale().setMax(NumberDataElementImpl.create( maxYValue ));")
// Now we'll go through the same process for the line series
// Start by getting the costAxis, representing the line series			
			.append( "var costAxis = chart.getPrimaryBaseAxes()[0].getAssociatedAxes().get(1);" )
			.append( "costAxis.getScale().setMin(NumberDataElementImpl.create( minYValue * 0.15 ));")
			.append( "costAxis.getScale().setMax(NumberDataElementImpl.create( maxYValue * 0.15 ));")
			.append( " }" );
		return sb;
	}
	
	public void setSecondaryYAxisLabel(String secondaryYAxisLabel) {
		this.secondaryYAxisLabel  = secondaryYAxisLabel;
	}


	
	///////////////////////////////////////////////////////////////////////////////////////////	
	//	PREFERENCES AREA
	///////////////////////////////////////////////////////////////////////////////////////////

	private void initPreferences() {
		IPreferenceStore preferences = PlatformUI.getPreferenceStore();
		// Remember, the node method creates the preference node if it does not already exist
//		showLegend = preferences.getBoolean(PreferenceConstants.P_SHOWLEGEND);
//		showXAxisLabel = preferences.getBoolean(PreferenceConstants.P_SHOWXAXISLABEL);
//		showYAxisLabel = preferences.getBoolean(PreferenceConstants.P_SHOWYAXISLABEL);
//		secondaryYAxisLabel = preferences.getBoolean(PreferenceConstants.P_SHOWYAXISLABEL);
		showChartNotes = preferences.getBoolean(PreferenceConstants.P_SHOWCHARTNOTES);
		preferences.addPropertyChangeListener((org.eclipse.jface.util.IPropertyChangeListener) propertyChangeListener);
	}

	/////////////////////////////////////////////////////////////////////
	//	Private classes supporting the view							   //	
	/////////////////////////////////////////////////////////////////////

	private final IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener(){
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			IPreferenceStore preferences = PlatformUI.getPreferenceStore();
//			if (event.getProperty().equals(
//					PreferenceConstants.P_SHOWLEGEND)) {
//				showLegend = preferences.getBoolean(PreferenceConstants.P_SHOWLEGEND);
//				birtChart.getLegend().setVisible(showLegend);
//			}
//			if (event.getProperty().equals(
//					PreferenceConstants.P_SHOWXAXISLABEL)) {
//				showXAxisLabel = preferences.getBoolean(PreferenceConstants.P_SHOWXAXISLABEL);
//				xAxis.getTitle().setVisible( showXAxisLabel && xAxisLabel.length() > 0 );
//			}
//			if (event.getProperty().equals(
//					PreferenceConstants.P_SHOWYAXISLABEL)) {
//				showYAxisLabel = preferences.getBoolean(PreferenceConstants.P_SHOWYAXISLABEL);
//				yAxis.getTitle().setVisible( showYAxisLabel && yAxisLabel.length() > 0 );
//  			costAxis.getTitle().setVisible( showYAxisLabel && secondaryYAxisLabel.length() > 0 );
//			}
			if (event.getProperty().equals(
					PreferenceConstants.P_SHOWCHARTNOTES)) {
				showChartNotes = preferences.getBoolean(PreferenceConstants.P_SHOWCHARTNOTES);
				explainLabel.getLabel().setVisible(showChartNotes);
			}
		}
	};

}