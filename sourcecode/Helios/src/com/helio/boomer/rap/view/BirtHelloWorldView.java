package com.helio.boomer.rap.view;

import org.eclipse.birt.chart.extension.datafeed.StockEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;

import com.helio.boomer.rap.birt.ChartCanvas;
import com.helio.boomer.rap.service.ServiceFacade;

public class BirtHelloWorldView extends ViewPart {
	public BirtHelloWorldView() {
	}
	public static final String ID = "com.helio.boomer.rap.view.birthelloworldview";

	// Copied class from http://wiki.eclipse.org/RAP/BIRT_Integration

	private TabFolder tabFolder;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl( Composite parent ) {
		parent.setLayout( new FillLayout() );
		createChartFolder( parent );
	}

	private void createChartFolder( Composite parent ) {
		tabFolder = new TabFolder( parent, SWT.TOP );
		ChartCanvas pieChart = new ChartCanvas( tabFolder, SWT.NONE );
		pieChart.setChart( createPieChart() );
		ChartCanvas barChart = new ChartCanvas( tabFolder, SWT.NONE );
		barChart.setChart( createBarChart() );
		ChartCanvas prefabChartCanvas = new ChartCanvas( tabFolder, SWT.NONE );
		prefabChartCanvas.setChart( createPrefabChart() );
		ChartCanvas fullDayChartCanvas = new ChartCanvas( tabFolder, SWT.NONE );
		fullDayChartCanvas.setChart( createFullDayChart() );
		//
		TabItem barTabItem = new TabItem( tabFolder, SWT.NONE );
		barTabItem.setText( "Bar Chart" );
		barTabItem.setControl( barChart );
		TabItem pieTabItem = new TabItem( tabFolder, SWT.NONE );
		pieTabItem.setText( "Pie Chart" );
		pieTabItem.setControl( pieChart );
		TabItem prefabTabItem = new TabItem( tabFolder, SWT.NONE);
		prefabTabItem.setText("Prefab Chart");
		prefabTabItem.setControl( prefabChartCanvas );
		TabItem fullDayTabItem = new TabItem( tabFolder, SWT.NONE );
		fullDayTabItem.setText("Full Day");
		fullDayTabItem.setControl(fullDayChartCanvas);
	}

	private Chart createPrefabChart() {
		ChartWithAxes cwaStockBar = ChartWithAxesImpl.create( );

		// Legend
		cwaStockBar.getLegend( ).setVisible( false );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaStockBar ).getPrimaryBaseAxes( )[0];

		// Y-Axis
		Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaStockBar ).getPrimaryOrthogonalAxis( xAxisPrimary );

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(
				new String[] { "One", "Two", "Three" }
		);

		StockDataSet dsStockValues = StockDataSetImpl
			.create(new StockEntry[] {
				new StockEntry(12, 10, 16, 14),
				new StockEntry(12, 10, 16, 14),
				new StockEntry(12, 10, 16, 14) });

		NumberDataSet dsBarValues = NumberDataSetImpl.create(
				new double[] { 11, 13, 15 }
		);

		// Create X-Series
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet( categoryValues );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seBase );

//		// Y-BarSeries
//		BarSeries bs = (BarSeries) BarSeriesImpl.create();
//		bs.setDataSet(dsBarValues);
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setDataSet(dsBarValues);
		
		// Y-StockSeries
		StockSeries ss = (StockSeries) StockSeriesImpl.create( );
		ss.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ss.setDataSet( dsStockValues );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
//		sdY.getSeries( ).add( bs );
		sdY.getSeries( ).add( ls );
		sdY.getSeries( ).add( ss );

		return cwaStockBar;
	}

	private Chart createFullDayChart() {
		String[] dayTimes = {
				"12am", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am",
				"noon", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm" 
				};
		
		ChartWithAxes dayLineChart = ChartWithAxesImpl.create( );

		// Legend
		dayLineChart.getLegend( ).setVisible( true );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) dayLineChart ).getPrimaryBaseAxes( )[0];

		// Y-Axis
		Axis yAxisPrimary = ( (ChartWithAxesImpl) dayLineChart ).getPrimaryOrthogonalAxis( xAxisPrimary );

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create( dayTimes );

		LineSeries fullDaySeries = (LineSeries) LineSeriesImpl.create();
		
		fullDaySeries.setVisible(true);
		
		NumberDataSet dsLineValues = NumberDataSetImpl.create(
				ServiceFacade.getSampleReportValues()
		);
		
		fullDaySeries.setDataSet(dsLineValues);

		// Create X-Series
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet( categoryValues );
		
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);
		
		// Create Y-Series

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries().add( fullDaySeries );

		return dayLineChart;
	}

	private Chart createPieChart() {
		ChartWithoutAxes chart = ChartWithoutAxesImpl.create();
		chart.setDimension( ChartDimension.TWO_DIMENSIONAL_LITERAL );
		Text caption = chart.getTitle().getLabel().getCaption();
		caption.setValue( "Hourly Distribution" );
		adjustFont( caption.getFont() );
		Legend legend = chart.getLegend();
		legend.setItemType( LegendItemType.CATEGORIES_LITERAL );
		legend.setVisible( true );
		adjustFont( legend.getText().getFont() );
		TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
				"Peak Hours", "non-Peak Hours"} );
		NumberDataSet seriesOneValues = NumberDataSetImpl.create( new double[]{
				80, 20
		} );
		// Base Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet( categoryValues );
		SeriesDefinition sd = SeriesDefinitionImpl.create();
		chart.getSeriesDefinitions().add( sd );
		sd.getSeriesPalette().shift( 0 );
		sd.getSeries().add( seCategory );
		// new colors
		final Fill[] fiaBase = {
				ColorDefinitionImpl.ORANGE(), ColorDefinitionImpl.GREY()
		};
		sd.getSeriesPalette().getEntries().clear();
		for( int i = 0; i < fiaBase.length; i++ ) {
			sd.getSeriesPalette().getEntries().add( fiaBase[ i ] );
		}
		// Orthogonal Series
		PieSeries sePie = ( PieSeries )PieSeriesImpl.create();
		sePie.setDataSet( seriesOneValues );
		sePie.setExplosion( 5 );
		sePie.setRotation( 40 );
		sePie.getLabel().setVisible( false );
		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add( sdCity );
		sdCity.getSeries().add( sePie );
		return chart;
	}

	private Chart createBarChart() {
		ChartWithAxes chart = ChartWithAxesImpl.create();
		chart.setDimension( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );
		Plot plot = chart.getPlot();
		plot.setBackground( ColorDefinitionImpl.WHITE() );
		plot.getClientArea().setBackground( ColorDefinitionImpl.WHITE() );
		Legend legend = chart.getLegend();
		legend.setItemType( LegendItemType.CATEGORIES_LITERAL );
		legend.setVisible( true );
		adjustFont( legend.getText().getFont() );
		Text caption = chart.getTitle().getLabel().getCaption();
		caption.setValue( "Distribution of Chart Column Heights" );
		adjustFont( caption.getFont() );
		Axis xAxis = ( ( ChartWithAxes )chart ).getPrimaryBaseAxes()[ 0 ];
		xAxis.getTitle().setVisible( true );
		xAxis.getTitle().getCaption().setValue( "" );
		Axis yAxis = ( ( ChartWithAxes )chart ).getPrimaryOrthogonalAxis( xAxis );
		yAxis.getTitle().setVisible( true );
		yAxis.getTitle().getCaption().setValue( "" );
		yAxis.getScale().setStep( 1.0 );
		TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
				"short", "medium", "tall"
		} );
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet( categoryValues );
		adjustFont( seCategory.getLabel().getCaption().getFont() );
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift( 1 );
		xAxis.getSeriesDefinitions().add( sdX );
		sdX.getSeries().add( seCategory );
		NumberDataSet orthoValuesDataSet1 = NumberDataSetImpl.create( new double[]{
				1, 2, 3
		} );
		BarSeries bs1 = ( BarSeries )BarSeriesImpl.create();
		bs1.setDataSet( orthoValuesDataSet1 );
		adjustFont( bs1.getLabel().getCaption().getFont() );
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxis.getSeriesDefinitions().add( sdY );
		sdY.getSeries().add( bs1 );
		return chart;
	}

	private void adjustFont( FontDefinition font ) {
		font.setSize( 8 );
		font.setName( "Verdana" );
	}

	public void setFocus() {
		tabFolder.setFocus();
	}

}