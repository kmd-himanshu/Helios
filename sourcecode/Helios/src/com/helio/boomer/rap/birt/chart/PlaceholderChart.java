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
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
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
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.preference.PreferenceConstants;
import com.helio.boomer.rap.utility.NumericUtility;

public class PlaceholderChart extends BoomerBirtChart implements IBoomerChart {

	private Map<String, Double> valueMap;
	private Map<String, Double> valueMap2;

	private NumberDataSet orthoValuesDataSet1;
	private NumberDataSet orthoValuesDataSet2;

	private Axis xAxis;
	private Axis yAxis;
	
	private LabelBlock explainLabel;
	
	private double rotateYAxisLabel = 0.0;

	private boolean showLegend = true;
	private boolean showChartTitle = true;
	private boolean showXAxisLabel = false;
	private boolean showYAxisLabel = true;
	private boolean showChartNotes = true;
	
	private String valueLabelFormatString = null;

	public PlaceholderChart(String chartTitle) {
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
		this.valueMap = Maps.newHashMap(newValueMap);
		for (Entry<String, Double> valueEntry : valueMap.entrySet()) {
			Double newValue = NumericUtility.getPrecDouble(valueEntry.getValue(), 2);
			valueEntry.setValue(newValue);
		}
	}

	public void reassignValueMap2(Map<String, Double> newValueMap) {
		this.valueMap2 = Maps.newHashMap(newValueMap);
		for (Entry<String, Double> valueEntry : valueMap2.entrySet()) {
			Double newValue = NumericUtility.modifyByRandom(valueEntry.getValue());
			if (newValue < 0.0) newValue = 0.0;
			valueEntry.setValue(NumericUtility.getPrecDouble(newValue, 2));
		}
	}

	public Chart createChart() {

		birtChart = ChartWithAxesImpl.create();
		birtChart.setDimension( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );
		/*
		 * @Date : 24-August-2012
		 * @Author : RSystems International Ltd
		 * @Purpose: RMAP - 6 , Sprint -1
		 */
		/*
		explainLabel = (LabelBlock) LabelBlockImpl.create();
		explainLabel.getLabel().getCaption().setValue("Left bar is previous year");
		explainLabel.getLabel().getCaption().getFont().setName("Arial");
		explainLabel.getLabel().getCaption().getFont().setSize(10);
		explainLabel.getLabel().getCaption().getFont().setItalic(true);
		explainLabel.setAnchor(Anchor.NORTH_WEST_LITERAL);
		explainLabel.getLabel().setVisible(showChartNotes);
		birtChart.getPlot().add(explainLabel); */
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
						TooltipValueImpl.create(100, "Boomer")));
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
		if (valueLabelFormatString != null) {
			// TODO: Code to go here to format label for appropriate precision. 
		}
		//
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxis.getSeriesDefinitions().add( sdY );
		yAxis.getTriggers().add(trigger);
		sdX.getSeries().get(0).getTriggers().add(trigger);
		sdY.getSeries().add( bs2 );
		sdY.getSeries().add( bs1 );
		sdY.getSeries().get(0).getTriggers().add(trigger);
		
		/*
		 * @Date   : 30-July-2012
		 * @Author : RSystems International Ltd
		 * @Purpose: Graphic Controls so that when the Display Legend
		 * 			 is turned on, and the Display Axes is turned off,
		 * 			 no legends are displayed on the x axis.
		 */
		xAxis.getLabel().setVisible(showXAxisLabel);
		// RSI changes | END
		
		
		
	}
	
	public void setValueLabelFormatString(String valueLabelFormatString) {
		this.valueLabelFormatString = valueLabelFormatString;
	}


	private void adjustFont( FontDefinition font ) {
		font.setSize( 8 );
		font.setName( "Verdana" );
	}

	@Override
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
		if (birtChart != null) {
			birtChart.getLegend().setVisible(showLegend);
		}
	}

	@Override
	public void setShowChartTitle(boolean showChartTitle) {
		this.showChartTitle = showChartTitle;
		if (birtChart != null) {
			birtChart.getTitle().setVisible(showChartTitle);
		}
		if (explainLabel != null) {
			explainLabel.getLabel().setVisible(showChartNotes);
		}
	}

	@Override
	public void setShowXAxisLabel(boolean showXAxisLabel) {
		this.showXAxisLabel = showXAxisLabel;
		if (xAxis != null) {
			xAxis.getTitle().setVisible( showXAxisLabel );
		}
	}

	@Override
	public void setShowYAxisLabel(boolean showYAxisLabel) {
		this.showYAxisLabel = showYAxisLabel;
		if (yAxis != null) {
			yAxis.getTitle().setVisible( showYAxisLabel );
		}
	}
	
	@Override
	public void setShowChartNotes(boolean showChartNotes) {
		this.showChartNotes = showChartNotes;
		if (explainLabel != null) {
			explainLabel.getLabel().setVisible(showChartNotes);
		}
	}
	
	public void setYAxisUOM(String uom) {
		this.yAxis.setFormatSpecifier(
				JavaNumberFormatSpecifierImpl.create(uom));
	}
	
	public void setRotateYAxisLabel(double rotateYAxisLabel) {
		this.rotateYAxisLabel = rotateYAxisLabel;
	}
	
	private void setChartScripts(Chart chart) {
		chart.setScript((
				getBeforeGenerationScript()
//				getBeforeDrawSeriesScript()
//					.append("\n")
//					.append(getDataPointFillScript())
//					.append("\n")
//					.append(getDataPointLabelScript())
					).toString());
	}
	
	public StringBuffer getBeforeGenerationScript() {
		StringBuffer sb = new StringBuffer("function beforeGeneration( chart, icsc )");
		sb.append("{");
		sb.append("}");
		return sb;
	}
	
	public StringBuffer getBeforeDrawSeriesScript() {
		StringBuffer sb = new StringBuffer("function beforeDrawSeries(series, isr, icsc) {");
		sb.append(" series.getLabel().getCaption().setValue(\"foo\");");
		sb.append(" }");
		return sb;
	}
	
	public StringBuffer getDataPointFillScript() {
		StringBuffer sb = new StringBuffer("function beforeDrawDataPoint(dph, fill, icsc)");
		sb.append(" {");
		sb.append(" importPackage( Packages.org.eclipse.birt.chart.model.attribute.impl );");
		sb.append(" val = dph.getOrthogonalValue();");
		sb.append(" if( fill.getClass().isAssignableFrom(ColorDefinitionImpl)){");
		sb.append(" var cd = fill.copyInstance();");
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
		sb.append(" label.getCaption().setValue(val);");
		sb.append(" label.getCaption().getFont().setBold(false);");
		sb.append(" label.getCaption().getFont().setSize(10);");
		sb.append(" label.getCaption().getFont().setName(\"Arial\");");
		sb.append(" } else {");
		sb.append(" }");
		sb.append(" }");
		return sb;
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
//			}
			if (event.getProperty().equals(
					PreferenceConstants.P_SHOWCHARTNOTES)) {
				showChartNotes = preferences.getBoolean(PreferenceConstants.P_SHOWCHARTNOTES);
				explainLabel.getLabel().setVisible(showChartNotes);
			}
		}
	};

}