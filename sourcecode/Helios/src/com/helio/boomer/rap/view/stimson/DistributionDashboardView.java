package com.helio.boomer.rap.view.stimson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.part.ViewPart;

import com.helio.app.serviceapi.kpi.EnergyCostAllocationKPI;
import com.helio.boomer.rap.birt.ChartCanvas;
import com.helio.boomer.rap.birt.chart.IBoomerChart;
import com.helio.boomer.rap.birt.chart.PlaceholderBarWLineChart;
import com.helio.boomer.rap.birt.chart.PlaceholderChart;
import com.helio.boomer.rap.birt.chart.PlaceholderPieChart;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.DistributionReportDAO;
import com.helio.boomer.rap.engine.servicedata.EnergyAndCostPerSqFtKPIs;
import com.helio.boomer.rap.session.BusinessUnitDataStore;
import com.helio.boomer.rap.session.DistributionUnitDataStore;
import com.helio.boomer.rap.session.SessionDataStore;
import com.helio.boomer.rap.utility.DataSetUtilities;

public class DistributionDashboardView extends ViewPart {

	public static final String ID = "com.helio.boomer.rap.view.stimson.distributiondashboardview";
//	private Text apiResultText;

	private ChartCanvas energyCasesHandledChartCanvas;
	private PlaceholderChart energyCasesHandledChart;

	private ChartCanvas energyCostPerBuildingChartCanvas;
	private PlaceholderBarWLineChart energyCostPerBuildingChart;

	private ChartCanvas energyCHPerBuildingPieChartCanvas;
	private PlaceholderPieChart energyCHPerBuildingPieChart;

	private ChartCanvas energyCostPerSqFtChartCanvas;
	private PlaceholderBarWLineChart energyCostPerSqFtChart;

	private ChartCanvas energyKWHBuildingPieChartCanvas;
	private PlaceholderPieChart energyKWHBuildingPieChart;

	private ChartCanvas demandPerLocationChartCanvas;
	private PlaceholderChart demandPerLocationChart;
	
	private List<IBoomerChart> boomerCharts;

	private ISelectionListener selectionListener;

	private boolean showLegends = false;
	private boolean showTitles = true;
	private boolean showAxesLabels = true;

	public DistributionDashboardView() {

	}

	//////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES //
	//////////////////////////////////////////////////////////////////////////////////

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		SashForm mainSashForm = new SashForm(parent, SWT.HORIZONTAL);
		SashForm leftSashForm = new SashForm(mainSashForm, SWT.VERTICAL);
		SashForm rightRemainderSashForm = new SashForm(mainSashForm, SWT.HORIZONTAL);
		SashForm middleSashForm = new SashForm(rightRemainderSashForm, SWT.VERTICAL);
		SashForm rightSashForm = new SashForm(rightRemainderSashForm, SWT.VERTICAL);

		Composite energyCasesHandledComposite		= new Composite(leftSashForm, SWT.NONE);
		Composite energyCostPerBuildingComposite	= new Composite(leftSashForm, SWT.NONE);
		Composite energyCHBuildingComposite			= new Composite(middleSashForm, SWT.NONE);
		Composite energyCostPerSqFtComposite		= new Composite(middleSashForm, SWT.NONE);
		Composite energyKWHBuildingComposite		= new Composite(rightSashForm, SWT.NONE);
		Composite demandPerLocationComposite		= new Composite(rightSashForm, SWT.NONE);

		energyCasesHandledComposite.setLayout(new FillLayout());
		energyCostPerBuildingComposite.setLayout(new FillLayout());
		energyCHBuildingComposite.setLayout(new FillLayout());
		energyCostPerSqFtComposite.setLayout(new FillLayout());
		energyKWHBuildingComposite.setLayout(new FillLayout());
		demandPerLocationComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		boomerCharts = new ArrayList<IBoomerChart>();
		
		energyCasesHandledChartCanvas = new ChartCanvas( energyCasesHandledComposite, SWT.NONE);

		energyCostPerBuildingChartCanvas = new ChartCanvas( energyCostPerBuildingComposite, SWT.NONE);

		energyCHPerBuildingPieChartCanvas = new ChartCanvas( energyCHBuildingComposite, SWT.NONE);

		energyCostPerSqFtChartCanvas = new ChartCanvas( energyCostPerSqFtComposite, SWT.NONE );
		
		energyKWHBuildingPieChartCanvas = new ChartCanvas( energyKWHBuildingComposite, SWT.NONE);

		demandPerLocationChartCanvas = new ChartCanvas( demandPerLocationComposite, SWT.NONE );
		
		/*
		 * RMAP-74
		 * Sprint 1.0.2
		 */
		
				/*
				 * Retrieve values from Application store
				 */
				synchronized(this){
						String sessionId = RWT.getSessionStore().getId();				
						SessionDataStore sessionDataStoreLocal = (SessionDataStore) RWT.getSessionStore().getAttribute(sessionId); 	
						SessionDataStore sessionDataStoreAppl = (SessionDataStore) RWT.getApplicationStore().getAttribute(sessionDataStoreLocal.getUsername());
						
							if(sessionDataStoreAppl != null && sessionDataStoreLocal.isFirstScreenDistribution())
							{					
								sessionDataStoreAppl.setFirstScreenDistribution(false);						
								DistributionUnitDataStore distributionUnitDataStore = sessionDataStoreAppl.getDistributionDataStore();
								
								if( distributionUnitDataStore != null )
								{									
									energyCasesHandledChartCanvas.setChart(getEnergyCasesHandledChart_ReturnView(distributionUnitDataStore));
									getEnergyCostPerBuildingChart_ReturnView(distributionUnitDataStore);
									energyCHPerBuildingPieChartCanvas.setChart(getEnergyCHPerBuildingPieChart_ReturnView(distributionUnitDataStore));
									getEnergyCostPerSqFtChart_ReturnView(distributionUnitDataStore) ;
									energyKWHBuildingPieChartCanvas.setChart(getEnergyKWHBuildingPieChart_ReturnView(distributionUnitDataStore));
									demandPerLocationChartCanvas.setChart( getDemandPerLocationChart_ReturnView(distributionUnitDataStore));									
								}
								else
								{
									energyCasesHandledChartCanvas.setChart(getEnergyCasesHandledChart());
									energyCostPerBuildingChartCanvas.setChart(getEnergyCostPerBuildingChart());
									energyCHPerBuildingPieChartCanvas.setChart(getEnergyCHPerBuildingPieChart());
									energyCostPerSqFtChartCanvas.setChart( getEnergyCostPerSqFtChart() );
									energyKWHBuildingPieChartCanvas.setChart(getEnergyKWHBuildingPieChart());
									demandPerLocationChartCanvas.setChart( getDemandPerLocationChart() );
								}
							}						
						
						else{
							energyCasesHandledChartCanvas.setChart(getEnergyCasesHandledChart());
							energyCostPerBuildingChartCanvas.setChart(getEnergyCostPerBuildingChart());
							energyCHPerBuildingPieChartCanvas.setChart(getEnergyCHPerBuildingPieChart());
							energyCostPerSqFtChartCanvas.setChart( getEnergyCostPerSqFtChart() );
							energyKWHBuildingPieChartCanvas.setChart(getEnergyKWHBuildingPieChart());
							demandPerLocationChartCanvas.setChart( getDemandPerLocationChart() );
						}
				}
				
				initAndAddChart(energyCasesHandledChart);				
				initAndAddChart(energyCostPerBuildingChart);
				initAndAddChart(energyCostPerSqFtChart);
				initAndAddChart(demandPerLocationChart);				
				
		/*
		 * END RMAP-74
		 */
				
				
				

		mainSashForm.setWeights(new int[] {1, 2});
		leftSashForm.setWeights(new int[] {1, 1});
		rightRemainderSashForm.setWeights(new int[] {1, 1});
		middleSashForm.setWeights(new int[] {1, 1});
		rightSashForm.setWeights(new int[] {1, 1});
		
	}

	private void initAndAddChart(IBoomerChart newChart) {
		boomerCharts.add(newChart);
		newChart.setShowLegend(showLegends);
		newChart.setShowChartTitle(showTitles);
		newChart.setShowChartNotes(showTitles);
		newChart.setShowXAxisLabel(showAxesLabels);
		newChart.setShowYAxisLabel(showAxesLabels);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}

// Upper-left report
	private Chart getEnergyCasesHandledChart() {
		energyCasesHandledChart = new PlaceholderChart("Cases Handled per Period");
		energyCasesHandledChart.setXAxisLabel("");
		energyCasesHandledChart.setYAxisLabel("Cases Handled");
		return energyCasesHandledChart.createChart();
	}

// Upper-middle pie chart	
	private Chart getEnergyCHPerBuildingPieChart() {
		energyCHPerBuildingPieChart = new PlaceholderPieChart("Energy per Cases Handled per Building");
		return energyCHPerBuildingPieChart.createChart();
	}

// Upper-right pie chart
	private Chart getEnergyKWHBuildingPieChart() {
		energyKWHBuildingPieChart = new PlaceholderPieChart("Energy per Building");
		return energyKWHBuildingPieChart.createChart();
	}
	
// Lower-left report
	private Chart getEnergyCostPerBuildingChart() {
		energyCostPerBuildingChart = new PlaceholderBarWLineChart("Energy & Cost ($K) per Building");
		energyCostPerBuildingChart.setXAxisLabel("");
		energyCostPerBuildingChart.setYAxisLabel("Energy (1000's of kWh)");
		return energyCostPerBuildingChart.createChart();
	}

// Lower-middle report	
	private Chart getEnergyCostPerSqFtChart() {
		energyCostPerSqFtChart = new PlaceholderBarWLineChart("Energy & Cost per SqFt");
		energyCostPerSqFtChart.setXAxisLabel("");
		energyCostPerSqFtChart.setYAxisLabel("Energy / SqFt");
		energyCostPerSqFtChart.setSecondaryYAxisLabel("Cost ($) / SqFt");
		return energyCostPerSqFtChart.createChart();
	}

// Lower-right report	
	private Chart getDemandPerLocationChart() {
		demandPerLocationChart = new PlaceholderChart("Energy per Location");
		demandPerLocationChart.setXAxisLabel("");
		demandPerLocationChart.setYAxisLabel("Energy (1000's of kWh)");
		return demandPerLocationChart.createChart();
	}
	
	
	
	/*
	 * RMAP-74
	 * Sprint 1.0.2
	 */


	private Chart getEnergyCasesHandledChart_ReturnView(DistributionUnitDataStore duDataStore) {		
		energyCasesHandledChart = new PlaceholderChart(
				"Cases Handled per Period");
		energyCasesHandledChart.setShowChartTitle(true);
		energyCasesHandledChart.setXAxisLabel("");
		energyCasesHandledChart.setShowXAxisLabel(false);
		energyCasesHandledChart.setYAxisLabel("Cases Handled");
		Map<String, Double> valueMap = duDataStore.getEnergyCasesHandledChart_valueMap1();
		Map<String, Double> valueMap2 = duDataStore.getEnergyCasesHandledChart_valueMap2();
		energyCasesHandledChart.reassignValueMap(valueMap);
		energyCasesHandledChart.reassignValueMap2(valueMap2);
		return energyCasesHandledChart.createChart();
	}	
	
	private void getEnergyCostPerSqFtChart_ReturnView(DistributionUnitDataStore duDataStore) {
		Chart chart = getEnergyCostPerSqFtChart();
		Map<String, Double> valueMap = duDataStore.getEnergyCostPerSqFtChart_valueMap1();
		Map<String, Double> valueMap2 = duDataStore.getEnergyCostPerSqFtChart_valueMap2();
		energyCostPerSqFtChart.reassignValueMap(valueMap);
		energyCostPerSqFtChart.reassignValueMap2(valueMap2);	
		energyCostPerSqFtChart.setLineAxisUOM("$.000");
		energyCostPerSqFtChart.setYAxisUOM("0.0");
		energyCostPerSqFtChartCanvas.setChart(chart);
	}
	private Chart getEnergyCHPerBuildingPieChart_ReturnView(DistributionUnitDataStore duDataStore) {
		energyCHPerBuildingPieChart = new PlaceholderPieChart("Energy per Cases Handled per Building");
		Map<String, Double> valueMap = duDataStore.getEnergyCHPerBuildingPieChart_valueMap();
		energyCHPerBuildingPieChart.reassignValueMap(valueMap);
		return energyCHPerBuildingPieChart.createChart();
	}	
	
	private Chart getEnergyKWHBuildingPieChart_ReturnView(DistributionUnitDataStore duDataStore) {
		energyKWHBuildingPieChart = new PlaceholderPieChart("Energy per Building");
		Map<String, Double> valueMap = duDataStore.getEnergyKWHBuildingPieChart_valueMap();		
		energyKWHBuildingPieChart.reassignValueMap(valueMap);
		return energyKWHBuildingPieChart.createChart();
	}	
	
	private void getEnergyCostPerBuildingChart_ReturnView(DistributionUnitDataStore duDataStore) {
		Chart chart = getEnergyCostPerBuildingChart();	
		Map<String, Double> valueMap = duDataStore.getEnergyCostPerBuildingChart_valueMap1();
		Map<String, Double> valueMap2 = duDataStore.getEnergyCostPerBuildingChart_valueMap2();			
		energyCostPerBuildingChart.reassignValueMap(valueMap);
		energyCostPerBuildingChart.reassignValueMap2(valueMap);
		energyCostPerBuildingChart.setLineAxisUOM("$0");
		energyCostPerBuildingChart.setYAxisUOM("0");
		energyCostPerBuildingChart.addLineSeries(DataSetUtilities.reduceByThousands(valueMap2));
		energyCostPerBuildingChartCanvas.setChart(chart);
	}	
	
	private Chart getDemandPerLocationChart_ReturnView(DistributionUnitDataStore duDataStore) {		
		demandPerLocationChart = new PlaceholderChart("Energy per Location");
		demandPerLocationChart.setXAxisLabel("");
		demandPerLocationChart.setYAxisLabel("Energy (1000's of kWh)");
		Map<String, Double> valueMap = duDataStore.getDemandPerLocationChart_valueMap1();
		Map<String, Double> valueMap2 = duDataStore.getDemandPerLocationChart_valueMap2();	
		demandPerLocationChart.reassignValueMap(valueMap);
		demandPerLocationChart.reassignValueMap2(valueMap2);		
		return demandPerLocationChart.createChart();		
	}
		
	/*
	 * END RMAP-74
	 */
	

// UPDATE SECTION
	
	public DistributionUnitDataStore updateEnergyCasesHandledChart(
			List<Long> locationIds,
			List<Period> linkedPeriodList,DistributionUnitDataStore distributionUnitDataStore) {
		try {
			Map<String, Double> currentValueMap = DistributionReportDAO.getPrimaryCasesHandledKWhForLocation(
					locationIds,
					linkedPeriodList);
			energyCasesHandledChart.reassignValueMap(currentValueMap);
			energyCasesHandledChart.reassignValueMap2(currentValueMap);
			energyCasesHandledChart.populateValues();
			
			//RMAP-74
			
			distributionUnitDataStore.setEnergyCasesHandledChart_valueMap1(currentValueMap);
			distributionUnitDataStore.setEnergyCasesHandledChart_valueMap2(currentValueMap);
			//END
			
			/*
			 * @Date : 24-August-2012
			 * @Author : RSystems International Ltd
			 * @Purpose: RMAP - 6 , Sprint -1
			 */
			if(locationIds != null)
			{
				energyCasesHandledChartCanvas.setToolTipText("Left bar is previous year");
			}
			// END
			energyCasesHandledChartCanvas.updateChart();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distributionUnitDataStore;
	}

	public DistributionUnitDataStore updateEnergyCHPerBuildingPieChart(
			List<Long> buildingIds,
			Date startDt,
			Date endDt,DistributionUnitDataStore distributionUnitDataStore) {
		try {
			Map<String, Double> currentValueMap = DistributionReportDAO.getEnergyCHPerBuilding(
					buildingIds,
					startDt,
					endDt);
			energyCHPerBuildingPieChart.reassignValueMap(currentValueMap);
			energyCHPerBuildingPieChart.populateValues();	
			
			//RMAP-74
			
			distributionUnitDataStore.setEnergyCHPerBuildingPieChart_valueMap(currentValueMap);
			
			//END
			
			
			energyCHPerBuildingPieChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distributionUnitDataStore;
	}

	public DistributionUnitDataStore updateEnergyKWHBuildingPieChart(
			List<Long> buildingIds,
			Date startDt,
			Date endDt,DistributionUnitDataStore distributionUnitDataStore) {
		try {
			Map<String, Double> currentValueMap = DistributionReportDAO.getEnergyKWHPerBuilding(
					buildingIds,
					startDt,
					endDt);
			energyKWHBuildingPieChart.reassignValueMap(currentValueMap);
			energyKWHBuildingPieChart.populateValues();	
			
			//RMAP-74			
			distributionUnitDataStore.setEnergyKWHBuildingPieChart_valueMap(currentValueMap);			
			//END
			
			
			energyKWHBuildingPieChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distributionUnitDataStore;
	}	

	public DistributionUnitDataStore updateEnergyCostPerBuildingChart(
			List<Long> buildingIds,
			Date startDt,
			Date endDt,DistributionUnitDataStore distributionUnitDataStore) {
		try {
			Map<String, EnergyCostAllocationKPI> kpiValueMap = DistributionReportDAO.getEnergyCostPerBuilding(
					buildingIds,
					startDt,
					endDt);
			Map<String, Double> currentValueMap = new HashMap<String, Double>();
			Map<String, Double> currentLineMap = new HashMap<String, Double>();
			for (Entry<String, EnergyCostAllocationKPI> kpiEntry : kpiValueMap.entrySet()) {
				currentValueMap.put(kpiEntry.getKey(), kpiEntry.getValue().getBaseKPI().getKpiValue().doubleValue());
				currentLineMap.put(kpiEntry.getKey(), kpiEntry.getValue().getKpiValue().doubleValue() );
			}
			energyCostPerBuildingChart.setLineAxisUOM("$0");
			energyCostPerBuildingChart.setYAxisUOM("0");
			energyCostPerBuildingChart.reassignValueMap(DataSetUtilities.reduceByThousands(currentValueMap));
			energyCostPerBuildingChart.reassignValueMap2(DataSetUtilities.reduceByThousands(currentValueMap));
			energyCostPerBuildingChart.addLineSeries(DataSetUtilities.reduceByThousands(currentLineMap));
			energyCostPerBuildingChart.populateValues();	
			
			//RMAP-74
			
			distributionUnitDataStore.setEnergyCostPerBuildingChart_valueMap1(DataSetUtilities.reduceByThousands(currentValueMap));
			distributionUnitDataStore.setEnergyCostPerBuildingChart_valueMap2(DataSetUtilities.reduceByThousands(currentLineMap));			
			//END
			
			/*
			 * @Date : 24-August-2012
			 * @Author : RSystems International Ltd
			 * @Purpose: RMAP - 6 , Sprint -1
			 */
			if(buildingIds != null)
			{
				energyCostPerBuildingChartCanvas.setToolTipText("Left bar is previous year");
			}
			// END
			energyCostPerBuildingChartCanvas.updateChart();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distributionUnitDataStore;
	}
	
	public DistributionUnitDataStore updateEnergyCostPerSqFtChart(
			List<Long> buildingIds,
			Date startDt,
			Date endDt,DistributionUnitDataStore distributionUnitDataStore) {
		try {
			Map<String, EnergyAndCostPerSqFtKPIs> kpiValueMap = DistributionReportDAO.getEnergyAndCostPerSqFt(
					buildingIds,
					startDt,
					endDt);
			Map<String, Double> currentValueMap = new HashMap<String, Double>();
			Map<String, Double> currentLineMap = new HashMap<String, Double>();
			for (Entry<String, EnergyAndCostPerSqFtKPIs> kpiEntry : kpiValueMap.entrySet()) {
				// Set the values for the bar in the graph for the data point. It represents Energy in kWh/sqft
				// getEnergyPerBuildingPerSqFt()
				currentValueMap.put(kpiEntry.getKey(), kpiEntry.getValue().getEnergyPerSquareFootKPI().getKpiValue().doubleValue());
				// Set the values for the line point in the graph for the data point. It represents energy cost in $/sqft
				currentLineMap.put(kpiEntry.getKey(), kpiEntry.getValue().getEnergyCostPerSquareFootKPI().getKpiValue().doubleValue() );
			}
			energyCostPerSqFtChart.reassignValueMap(currentValueMap);
			energyCostPerSqFtChart.reassignValueMap2(currentValueMap);
			energyCostPerSqFtChart.setLineAxisUOM("$.000");
			energyCostPerSqFtChart.setYAxisUOM("0.0");
			energyCostPerSqFtChart.addLineSeries(currentLineMap);
			energyCostPerSqFtChart.populateValues();
			
			//RMAP-74
			
			distributionUnitDataStore.setEnergyCostPerSqFtChart_valueMap1(currentValueMap);
			distributionUnitDataStore.setEnergyCostPerSqFtChart_valueMap2(currentLineMap);			
			//END
			
			
			/*
			 * @Date : 24-August-2012
			 * @Author : RSystems International Ltd
			 * @Purpose: RMAP - 6 , Sprint -1
			 */
			if(buildingIds != null)
			{
				energyCostPerSqFtChartCanvas.setToolTipText("Left bar is previous year");
			}
			// END
			energyCostPerSqFtChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distributionUnitDataStore;
	}
	

	public DistributionUnitDataStore updateDemandPerLocationChart(
			List<Long> locationIds,
			Date startDt,
			Date endDt,DistributionUnitDataStore distributionUnitDataStore) {
		try {
			Map<String, Double> currentValueMap = DistributionReportDAO.getDemandPerLocation(
					locationIds,
					startDt,
					endDt);
			demandPerLocationChart.reassignValueMap(DataSetUtilities.reduceByThousands(currentValueMap));
			demandPerLocationChart.reassignValueMap2(DataSetUtilities.reduceByThousands(currentValueMap));
			demandPerLocationChart.populateValues();
			
//RMAP-74
			
			distributionUnitDataStore.setDemandPerLocationChart_valueMap1(DataSetUtilities.reduceByThousands(currentValueMap));
			distributionUnitDataStore.setDemandPerLocationChart_valueMap2(DataSetUtilities.reduceByThousands(currentValueMap));			
			//END
			
			
			
			/*
			 * @Date : 24-August-2012
			 * @Author : RSystems International Ltd
			 * @Purpose: RMAP - 6 , Sprint -1
			 */
			if(locationIds != null)
			{
				demandPerLocationChartCanvas.setToolTipText("Left bar is previous year");
			}
			// END
			demandPerLocationChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
			return distributionUnitDataStore;
	}

	@Override
	public void dispose() {
		super.dispose();
		ISelectionService selectionService =
			getViewSite().getWorkbenchWindow().getSelectionService();
		/*
		 * @Date   : 28-Aug-2012
		 * @Author : RSystems International Ltd
		 * @Purpose: when a component getting dispose,on logout click,throwing a exception "IllegalArgumentException" as some Listeners is not initialized.So below line of code is need to write to prevent of coming the exception.
		 * @Task   : RMAP-83,Sprint -1
		 * @CHANGE ROOT CAUSE:A IllegalArgumentException exception occurred in Peer Testing for RMAP-83 
		 */
		if(selectionListener!=null && selectionService!=null)
		// RSI changes RMAP-83 | END	
		selectionService.removeSelectionListener(selectionListener);
	}

	public boolean getShowLegends() {
		return showLegends;
	}
	
	public void setShowLegends(boolean showLegends) {
		this.showLegends = showLegends;
		for (IBoomerChart bc : boomerCharts) {
			bc.setShowLegend(showLegends);
		}
	}

	public boolean getShowAxesLabels() {
		return showAxesLabels;
	}

	public void setShowAxesLabels(boolean showAxesLabels) {
		this.showAxesLabels = showAxesLabels;
		for (IBoomerChart bc : boomerCharts) {
			bc.setShowXAxisLabel(showAxesLabels);
			bc.setShowYAxisLabel(showAxesLabels);
		}
	}
	
	public boolean getShowTitles() {
		return showTitles;
	}
	
	public void setShowTitles(boolean showTitles) {
		this.showTitles = showTitles;
		for (IBoomerChart bc : boomerCharts) {
			bc.setShowChartTitle(showTitles);
		}
	}

}