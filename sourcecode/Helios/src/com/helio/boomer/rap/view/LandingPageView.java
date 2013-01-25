package com.helio.boomer.rap.view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
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
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.birt.ChartCanvas;
import com.helio.boomer.rap.birt.chart.CumulativeViewBirtChart;
import com.helio.boomer.rap.engine.model.DeviceMonitor;
import com.helio.boomer.rap.graph.GraphSeriesSpec;
import com.helio.boomer.rap.service.HelioServiceDelegate;
import com.helio.boomer.rap.utility.DateUtilities;

public class LandingPageView extends ViewPart {

	public static final String ID = "com.helio.boomer.rap.view.landingpageview";

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	
	public enum DayType {
		MAINDAY,
		PREVIOUSDAY,
		NEXTDAY,
		AVERAGEDAY,
		OTHERDAY
	}

	private int setSize = 0;
	
	private Long trackingMonitor = 8L;
	private Date startDt = Date.valueOf("2011-07-25");
	private Date endDt = Date.valueOf("2011-07-26");

	// Workbench listener for changes in selected device
	private ISelectionListener selectionListener;

	private ChartWithAxes dayLineChart;

	private ChartCanvas fullDayChartCanvas;
	
	private Map<DayType, GraphSeriesSpec> seriesMap = Maps.newHashMap();
	private Axis yAxisPrimary;

	//////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR METHODS					 //
	//////////////////////////////////////////////////////////////////////////////////
	
	public LandingPageView() {
	}

	//////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES //
	//////////////////////////////////////////////////////////////////////////////////
	
	private final class WorkbenchSelectionListener implements ISelectionListener {
		public void selectionChanged( IWorkbenchPart part,
				ISelection selection ) {
			if( selection instanceof IStructuredSelection ){
				updateSelection( (IStructuredSelection) selection );
			}
			else {
				return;
			}
		}
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl( Composite parent ) {
		parent.setLayout( new FillLayout() );
		createChartFolder( parent );
		//
		// LISTEN TO THE WORKBENCH
		selectionListener = new WorkbenchSelectionListener();
		ISelectionService selectionService =
			getViewSite().getWorkbenchWindow().getSelectionService();
		selectionService.addSelectionListener(selectionListener);
	}

	private void createChartFolder( Composite parent ) {
		
		SashForm mainSashForm = new SashForm(parent, SWT.VERTICAL);
		
//		SashForm graphSashForm = new SashForm(mainSashForm, SWT.VERTICAL);
		
		Composite distributionGraphComposite = new Composite(mainSashForm, SWT.NONE);
		distributionGraphComposite.setLayout(new FillLayout());
		ChartCanvas fullDayChartCanvas = new ChartCanvas( distributionGraphComposite, SWT.NONE );
		fullDayChartCanvas.setChart( createFullDayChart() );
		

//		graphSashForm.setWeights(new int[] {1, 1});
		
		SashForm mapSashForm = new SashForm(mainSashForm, SWT.HORIZONTAL);
		
		Composite mapComposite = new Composite(mapSashForm, SWT.NONE);
		mapComposite.setLayout(new FillLayout());
		ChartCanvas cumulativeChartCanvas = new ChartCanvas( mapComposite, SWT.NONE );
		cumulativeChartCanvas.setChart(new CumulativeViewBirtChart("New Chart").createChart());
		
		Composite distributionComposite = new Composite(mapSashForm, SWT.NONE);
		
		mapSashForm.setWeights(new int[] {1, 1});

		mainSashForm.setWeights(new int[] {1, 1});

	}

	private Chart createFullDayChart() {
		String[] dayTimes = {
				"12am", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am",
				"noon", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm" 
				};
		
		dayLineChart = ChartWithAxesImpl.create( );

		// Legend
		dayLineChart.getLegend( ).setVisible( true );
		Text caption = dayLineChart.getTitle().getLabel().getCaption();
		caption.setValue( "Energy (kWh) Consumption" );
		adjustFont( caption.getFont(), 12 );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) dayLineChart ).getPrimaryBaseAxes( )[0];

		yAxisPrimary = ( (ChartWithAxesImpl) dayLineChart ).getPrimaryOrthogonalAxis( xAxisPrimary );

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
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries().add( setDayLineSeries(DayType.MAINDAY).lineSeries );
		
		adjustFont(yAxisPrimary.getLabel().getCaption().getFont(), 8);

		return dayLineChart;
	}

	private void adjustFont( FontDefinition font, int newSize ) {
		font.setSize( newSize );
		font.setName( "Verdana" );
	}

	public void setFocus() {

	}

	/**
	 * This listens for workbench selections -- if a Device has been selected, it will
	 * update the displayed graph with that device's information for the previous day.
	 * @return 
	 */
	private void updateSelection(IStructuredSelection selection) {
		Object object = selection.getFirstElement();
		if (object instanceof DeviceMonitor) {
			DeviceMonitor deviceMonitor = (DeviceMonitor) object;
			if (deviceMonitor != null) {
				this.trackingMonitor = new Long(deviceMonitor.getId());
				resetLineSeries();
			}
		}
	}
	
	private void resetLineSeries() {
		yAxisPrimary.getSeriesDefinitions().clear();
		for (Entry<DayType, GraphSeriesSpec> seriesEntry : seriesMap.entrySet()) {
			if (seriesEntry.getValue().enabled) {
				SeriesDefinition sdY = SeriesDefinitionImpl.create( );
				yAxisPrimary.getSeriesDefinitions( ).add( sdY );
				sdY.getSeries().add( seriesEntry.getValue().lineSeries );
			}
		}
		fullDayChartCanvas.updateChart();
	}
	
	public void setDateRange(Date begDate, Date endDate) {
		this.startDt = begDate;
		this.endDt = endDate;
		NumberDataSet dsLineValues = NumberDataSetImpl.create(
				HelioServiceDelegate.getDeviceValuesForDate(
						trackingMonitor,
						startDt,
						endDt));
		if (seriesMap.get(DayType.MAINDAY) != null) {
			LineSeries fullDaySeries = seriesMap.get(DayType.MAINDAY).lineSeries;
			fullDaySeries.setDataSet(dsLineValues);
		}
		resetLineSeries();
	}
	
	private GraphSeriesSpec setDayLineSeries(DayType day) {
		Date dayStartDt = Date.valueOf("2011-01-01");
		Date dayEndDt = Date.valueOf("2011-01-01");
		ColorDefinition colorDefinition = ColorDefinitionImpl.BLUE().brighter();
		switch (day) {
		case AVERAGEDAY:
			return averageLine();
		case MAINDAY:
			dayStartDt = startDt;
			dayEndDt = endDt;
			break;
		case NEXTDAY:
			dayStartDt = DateUtilities.incrementDateByDay(startDt, 1);
			dayEndDt = DateUtilities.incrementDateByDay(startDt, 2);
			colorDefinition = ColorDefinitionImpl.GREEN();
			break;
		case PREVIOUSDAY:
			dayStartDt = DateUtilities.incrementDateByDay(startDt, -1);
			dayEndDt = startDt;
			colorDefinition = ColorDefinitionImpl.ORANGE();
			break;
		case OTHERDAY: // Nothing yet--to be filled in with date specification later.
			return null;
		}
		LineSeries fullDaySeries = (LineSeries) LineSeriesImpl.create();
		fullDaySeries.getLineAttributes().setColor(colorDefinition);
		fullDaySeries.setCurve(true);
		for (Marker marker : fullDaySeries.getMarkers()) {
			marker.setType(MarkerType.CROSSHAIR_LITERAL);
		}
		List<Double> deviceValues = HelioServiceDelegate.getDeviceValuesForDate(
				trackingMonitor,
				dayStartDt,
				dayEndDt);
 		NumberDataSet dsLineValues = NumberDataSetImpl.create(deviceValues);
 		setSize = deviceValues.size();
		fullDaySeries.setDataSet(dsLineValues);
		//
		GraphSeriesSpec returnSeriesSpec = seriesMap.get(day);
		if (returnSeriesSpec == null) {
			returnSeriesSpec = new GraphSeriesSpec(fullDaySeries, dayStartDt, dayEndDt, colorDefinition, true);
			seriesMap.put(day, returnSeriesSpec);
		} else {
			returnSeriesSpec.startDate = dayStartDt;
			returnSeriesSpec.endDate = dayEndDt;
			returnSeriesSpec.lineSeries = fullDaySeries;
			returnSeriesSpec.colorDefinition = colorDefinition;
		}
		return returnSeriesSpec; 
	}

	public void enableDay(DayType day, boolean enableDay) {
		if (enableDay) {
			setDayLineSeries(day).enabled = enableDay;
		} else {
			if (seriesMap.get(day) != null) {
				seriesMap.get(day).enabled = enableDay;
			}
		}
		resetLineSeries();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GraphSeriesSpec averageLine() {
		ColorDefinition colorDefinition = ColorDefinitionImpl.RED().brighter();
		
		LineSeries fullDaySeries = (LineSeries) LineSeriesImpl.create();
		fullDaySeries.getLineAttributes().setColor(colorDefinition);
		fullDaySeries.setCurve(false);
		for (Marker marker : fullDaySeries.getMarkers()) {
			marker.setType(MarkerType.NABLA_LITERAL);
		}
		double[] dsValues = new double[setSize];
		if (seriesMap.containsKey(DayType.AVERAGEDAY)) {
			seriesMap.remove(DayType.AVERAGEDAY);
		}
		int avgDivisor = 0;
		for (Entry<DayType, GraphSeriesSpec> seriesEntry : seriesMap.entrySet()) {
			if (!(seriesEntry.getValue().enabled)) continue;
			avgDivisor++;
			List<Double> seriesList = new ArrayList<Double>();
			Object obj = seriesEntry.getValue().lineSeries.getDataSet().getValues();
			if (obj instanceof Collection) {
				seriesList.addAll((Collection<? extends Double>) seriesEntry.getValue().lineSeries.getDataSet().getValues());
			} else {
				List doubList = Arrays.asList(((double[]) obj));
				seriesList.addAll(doubList);
			}
			for (int i=0; i<seriesList.size(); i++) {
				dsValues[i] += seriesList.get(i);
			}
		}
		for (int i=0; i<dsValues.length; i++) {
			dsValues[i] /= avgDivisor;
		}
		
		NumberDataSet dsLineValues = NumberDataSetImpl.create(dsValues);
		fullDaySeries.setDataSet(dsLineValues);

		GraphSeriesSpec returnSeriesSpec = seriesMap.get(DayType.AVERAGEDAY);
		if (returnSeriesSpec == null) {
			returnSeriesSpec = new GraphSeriesSpec(fullDaySeries, startDt, endDt, colorDefinition, true);
			seriesMap.put(DayType.AVERAGEDAY, returnSeriesSpec);
		} else {
			returnSeriesSpec.startDate = startDt;
			returnSeriesSpec.endDate = endDt;
			returnSeriesSpec.lineSeries = fullDaySeries;
			returnSeriesSpec.colorDefinition = colorDefinition;
		}
		return returnSeriesSpec;
	}
	
}