package com.helio.boomer.rap.view.stimson;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.helio.boomer.rap.birt.ChartCanvas;
import com.helio.boomer.rap.birt.chart.PlaceholderChart;
import com.helio.boomer.rap.birt.chart.PlaceholderHorizBarChart;
import com.helio.boomer.rap.birt.chart.PlaceholderStockTickerChart;
import com.helio.boomer.rap.engine.servicedata.EnterpriseReportDAO;

public class EnterpriseDashboardView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.stimson.enterprisedashboardview";

	private ChartCanvas casesHandledByLocationChartCanvas;
	private PlaceholderHorizBarChart casesHandledByLocationChart;
	
	private ChartCanvas enterprisePeakDemandChartCanvas;
	private PlaceholderHorizBarChart enterprisePeakDemandChart;
	
	
	public EnterpriseDashboardView() {
		
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		
		SashForm mainSashForm = new SashForm(parent, SWT.HORIZONTAL);
		
		SashForm leftSashForm = new SashForm(mainSashForm, SWT.VERTICAL);
		Composite rightComposite = new Composite(mainSashForm, SWT.FILL);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		Composite rightMilkComposite = new Composite(rightComposite, SWT.NONE);
		rightMilkComposite.setLayout(new FillLayout());
		ChartCanvas milkChartCanvas = new ChartCanvas( rightMilkComposite, SWT.NONE );
		milkChartCanvas.setChart( getMilkChart() );
		
		Composite rightBeverageComposite = new Composite(rightComposite, SWT.NONE);
		rightBeverageComposite.setLayout(new FillLayout());
		ChartCanvas beverageChartCanvas = new ChartCanvas( rightBeverageComposite, SWT.NONE );
		beverageChartCanvas.setChart( getBeverageChart() );
		
		Composite rightBreadComposite = new Composite(rightComposite, SWT.NONE);
		rightBreadComposite.setLayout(new FillLayout());
		ChartCanvas breadChartCanvas = new ChartCanvas( rightBreadComposite, SWT.NONE );
		breadChartCanvas.setChart( getBreadChart() );
		
		Composite stockTickerComposite = new Composite(leftSashForm, SWT.FILL);  // Left-top composite
		stockTickerComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		ChartCanvas stockTickerChartCanvas = new ChartCanvas( stockTickerComposite, SWT.NONE );
		stockTickerChartCanvas.setChart( new PlaceholderStockTickerChart( "Dummy" ).createChart() );
		
		Composite leftBottomComposite = new Composite(leftSashForm, SWT.FILL);
		leftBottomComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite bottomLeftComposite = new Composite(leftBottomComposite, SWT.NONE);
		bottomLeftComposite.setLayout(new FillLayout());
		casesHandledByLocationChartCanvas = new ChartCanvas( bottomLeftComposite, SWT.NONE );
		casesHandledByLocationChartCanvas.setChart( getBottomLeftChart() );
		
		Composite bottomRightComposite = new Composite(leftBottomComposite, SWT.NONE);
		bottomRightComposite.setLayout(new FillLayout());
		enterprisePeakDemandChartCanvas = new ChartCanvas( bottomRightComposite, SWT.NONE );
		enterprisePeakDemandChartCanvas.setChart( getBottomRightChart() );
		
		leftSashForm.setWeights(new int[] {1, 1});
		mainSashForm.setWeights(new int[] {3, 1});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
	
	private Chart getBottomLeftChart() {
		casesHandledByLocationChart = new PlaceholderHorizBarChart("Energy/Total Cases Handled", false);
		casesHandledByLocationChart.setXAxisLabel("Location");
		casesHandledByLocationChart.setYAxisLabel("Energy kWh / Cases");
		return casesHandledByLocationChart.createChart();
	}
	
	private Chart getBottomRightChart() {
		enterprisePeakDemandChart = new PlaceholderHorizBarChart("Enterprise Peak DEMAND", false);
		enterprisePeakDemandChart.setXAxisLabel("Location");
		enterprisePeakDemandChart.setYAxisLabel("Peak Demand");
		return enterprisePeakDemandChart.createChart();
	}
	
	private Chart getMilkChart() {
		PlaceholderChart milkChart = new PlaceholderChart("MILK Energy (kWh) / Equivalent Unit");
		milkChart.setXAxisLabel("");
		milkChart.setYAxisLabel("Target kWh/EqU");
		return milkChart.createChart();
	}

	private Chart getBeverageChart() {
		PlaceholderChart beverageChart = new PlaceholderChart("BEVERAGE Energy (kWh) / Equivalent Unit");
		beverageChart.setXAxisLabel("");
		beverageChart.setYAxisLabel("Target kWh/EqU");
		return beverageChart.createChart();
	}

	private Chart getBreadChart() {
		PlaceholderChart breadChart = new PlaceholderChart("BREAD Energy (kWh) / Equivalent Unit");
		breadChart.setXAxisLabel("");
		breadChart.setYAxisLabel("Target kWh/EqU");
		return breadChart.createChart();
	}
	
	public void updateCasesHandledByLocationChart(long locationId, Date begDate, Date endDate) {
		try {
			List<Long> locationIds = Collections.singletonList(locationId);
			Map<String, Double> currentValueMap = EnterpriseReportDAO.getPrimaryCasesHandledKWhForLocation(
					locationIds,
					begDate,
					endDate);
			long offset = endDate.getTime() - begDate.getTime();
			Map<String, Double> previousValueMap = EnterpriseReportDAO.getPrimaryCasesHandledKWhForLocation(
					locationIds,
					new Date(begDate.getTime() - offset),
					begDate);
			casesHandledByLocationChart.reassignValueMap(currentValueMap);
			casesHandledByLocationChart.reassignValueMap2(previousValueMap);
			casesHandledByLocationChart.populateValues();
			casesHandledByLocationChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}