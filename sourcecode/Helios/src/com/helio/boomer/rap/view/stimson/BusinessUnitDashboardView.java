package com.helio.boomer.rap.view.stimson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.eclipsesource.widgets.gmaps.GMap;
import com.eclipsesource.widgets.gmaps.LatLng;
import com.google.common.collect.ImmutableMap;
import com.helio.boomer.rap.birt.ChartCanvas;
import com.helio.boomer.rap.birt.chart.IBoomerChart;
import com.helio.boomer.rap.birt.chart.PlaceholderBarWLineChart;
import com.helio.boomer.rap.birt.chart.PlaceholderChart;
import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.BuildingAllocation;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.model.Geolocation;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.BusinessUnitReportDAO;
import com.helio.boomer.rap.engine.servicedata.EnergyAndCostPerBuildingKPIs;
import com.helio.boomer.rap.session.BusinessUnitDataStore;
import com.helio.boomer.rap.session.SessionDataStore;
import com.helio.boomer.rap.utility.DataSetUtilities;

public class BusinessUnitDashboardView extends ViewPart {

	public static final String ID = "com.helio.boomer.rap.view.stimson.businessunitdashboardview";

	private ChartCanvas energyCasesHandledChartCanvas;
	private PlaceholderChart energyCasesHandledChart;

	private ChartCanvas energyCostPerSqFtChartCanvas;
	private PlaceholderChart energyCostPerSqFtChart;

	private ChartCanvas energyPerBUChartCanvas;
	private PlaceholderBarWLineChart energyPerBUChart;

	private ChartCanvas demandPerLocationChartCanvas;
	private PlaceholderChart demandPerLocationChart;

	private ISelectionListener selectionListener;

	private List<IBoomerChart> boomerCharts;

	private boolean showLegends = false;
	private boolean showTitles = true;
	private boolean showAxesLabels = true;

	private static final double YAXISROTATION = -20.0;

	// Map details

	static final private String INIT_CENTER = "37.717517,-122.484362";
	static final private int INIT_ZOOM = 15;
	static final private int INIT_TYPE = GMap.TYPE_HYBRID;

	private GMap gmap = null;

	// End map details

	public BusinessUnitDashboardView() {
		// Initialize Preferences from Store
		System.out.println("Initializing Preferences");
		try {
			// RWT.getSettingStore().setAttribute( "myAttribute",
			// "changed value" );
			String checkVal = RWT.getSettingStore().getAttribute("myAttribute");
			System.out.println("Got stored attribute of: " + checkVal);
			// } catch (SettingStoreException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}

	// ////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES //
	// ////////////////////////////////////////////////////////////////////////////////

	private final class WorkbenchSelectionListener implements
			ISelectionListener {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection instanceof IStructuredSelection) {
				updateSelection((IStructuredSelection) selection);
			} else {
				return;
			}
		}
	}

	/**
	 * This listens for workbench selections -- if a Location has been selected,
	 * it will recenter the map to the associated GeoLocation found for the
	 * Location. If a Building or BuildingAllocation is selected, it will get
	 * the parent Location and center on it.
	 * 
	 * @return
	 */
	private void updateSelection(IStructuredSelection selection) {
		if (selection == null)
			return;
		int zoomLevel = 14;
		Object object = selection.getFirstElement();
		Location location = null;
		if (object instanceof Division) {
			Division division = (Division) object;
			if ((division.getLocations() != null)
					&& (division.getLocations().size() > 0)) {
				location = division.getLocations().get(0);
				zoomLevel = 12;
			}
		}
		if (object instanceof Location) {
			location = (Location) object;
			zoomLevel = 14;
		}
		if (object instanceof Building) {
			location = ((Building) object).getLocation();
			zoomLevel = 15;
		}
		if (object instanceof BuildingAllocation) {
			BuildingAllocation buildingAllocation = (BuildingAllocation) object;
			if (buildingAllocation.getBuilding() != null) {
				location = buildingAllocation.getBuilding().getLocation();
				zoomLevel = 16;
			}
		}
		if ((location != null) && (location.getGeolocation() != null)) {
			Geolocation geolocation = location.getGeolocation();
			if ((geolocation.getLatitude() != null)
					&& (geolocation.getLongitude() != null)) {
				try {
					LatLng latLng = new LatLng(geolocation.getLatitude(),
							geolocation.getLongitude());
					gmap.setZoom(zoomLevel);
					gmap.setCenter(latLng);					
					
				} catch (Exception e) {
					System.err.println("Problem re-centering map: "
							+ e.toString());
				}
			}
		}
	}

	// Map methods

	private void createMap(final Composite parent) {
		gmap = new GMap(parent, SWT.NONE);
		gmap.setCenter(stringToLatLng(INIT_CENTER));
		gmap.setZoom(INIT_ZOOM);
		gmap.setType(INIT_TYPE);
	}

	private LatLng stringToLatLng(final String input) {
		LatLng result = null;
		if (input != null) {
			String temp[] = input.split(",");
			if (temp.length == 2) {
				try {
					double lat = Double.parseDouble(temp[0]);
					double lon = Double.parseDouble(temp[1]);
					result = new LatLng(lat, lon);
				} catch (NumberFormatException ex) {
				}
			}
		}
		return result;
	}

	// End Map Methods

	public void createPartControl(Composite parent) {

		boomerCharts = new ArrayList<IBoomerChart>();

		SashForm mainSashForm = new SashForm(parent, SWT.HORIZONTAL);

		SashForm leftSashForm = new SashForm(mainSashForm, SWT.VERTICAL);
		Composite energyCasesHandledComposite = new Composite(leftSashForm,
				SWT.NONE);
		Composite energyCostPerSqFtComposite = new Composite(leftSashForm,
				SWT.NONE);

		SashForm rightSashForm = new SashForm(mainSashForm, SWT.NONE);
		rightSashForm.setOrientation(SWT.VERTICAL);
		Composite mapComposite = new Composite(rightSashForm, SWT.NONE);

		SashForm middleSashForm = new SashForm(rightSashForm, SWT.VERTICAL);
		middleSashForm.setOrientation(SWT.HORIZONTAL);
		Composite energyPerBUComposite = new Composite(middleSashForm, SWT.NONE);
		Composite demandPerLocationComposite = new Composite(middleSashForm,
				SWT.NONE);

		mainSashForm.setWeights(new int[] { 1, 2 });
		leftSashForm.setWeights(new int[] { 1, 1 });
		middleSashForm.setWeights(new int[] { 1, 1 });
		rightSashForm.setWeights(new int[] { 1, 1 });

		energyCasesHandledComposite.setLayout(new FillLayout());
		energyCasesHandledChartCanvas = new ChartCanvas(
				energyCasesHandledComposite, SWT.NONE);
//		energyCasesHandledChartCanvas.setChart(getEnergyCasesHandledChart());
//		initAndAddChart(energyCasesHandledChart);

		energyCostPerSqFtComposite.setLayout(new FillLayout());
		energyCostPerSqFtChartCanvas = new ChartCanvas(
				energyCostPerSqFtComposite, SWT.NONE);
//		energyCostPerSqFtChartCanvas.setChart(getEnergyCostPerSqFtChart());
//		initAndAddChart(energyCostPerSqFtChart);
//		energyCostPerSqFtChart.setYAxisUOM("0.00");
//
//		mapComposite.setLayout(new FillLayout());
//		createMap(mapComposite);

		energyPerBUComposite.setLayout(new FillLayout());
		energyPerBUChartCanvas = new ChartCanvas(energyPerBUComposite, SWT.NONE);
//		energyPerBUChartCanvas.setChart(getEnergyPerBUChart());
//		initAndAddChart(energyPerBUChart);
//		energyPerBUChart.setYAxisUOM("0");

		demandPerLocationComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		demandPerLocationChartCanvas = new ChartCanvas(
				demandPerLocationComposite, SWT.NONE);
//		demandPerLocationChartCanvas.setChart(getDemandPerLocationChart());
//		initAndAddChart(demandPerLocationChart);
		
		/*
		 * RMAP-74
		 * Sprint 1.0.2
		 */
		
		/*
		 * Retrieve values from Application
		 */
		synchronized(this){
				String sessionId = RWT.getSessionStore().getId();				
				SessionDataStore sessionDataStoreLocal = (SessionDataStore) RWT.getSessionStore().getAttribute(sessionId); 	
				SessionDataStore sessionDataStoreAppl = (SessionDataStore) RWT.getApplicationStore().getAttribute(sessionDataStoreLocal.getUsername());
				
				
				
				
					if(sessionDataStoreAppl != null && sessionDataStoreLocal.isFirstScreenBusiness())
					{					
						sessionDataStoreAppl.setFirstScreenBusiness(false);						
						BusinessUnitDataStore businessUnitDataStore = sessionDataStoreAppl.getBusinessDataStore();
						
						if( businessUnitDataStore != null )
						{							
							energyCasesHandledChartCanvas.setChart(getEnergyCasesHandledChart_ReturnView(businessUnitDataStore));
							energyCostPerSqFtChartCanvas.setChart(getEnergyCostPerSqFtChart_ReturnView(businessUnitDataStore));													
							getEnergyPerBUChart_ReturnView(businessUnitDataStore);
							demandPerLocationChartCanvas.setChart(getDemandPerLocationChart_ReturnView(businessUnitDataStore));							
						}
						else
						{
							energyCasesHandledChartCanvas.setChart(getEnergyCasesHandledChart());
							energyCostPerSqFtChartCanvas.setChart(getEnergyCostPerSqFtChart());
							energyPerBUChartCanvas.setChart(getEnergyPerBUChart());
							demandPerLocationChartCanvas.setChart(getDemandPerLocationChart());
							
						}
					}
				
				else{
					energyCasesHandledChartCanvas.setChart(getEnergyCasesHandledChart());
					energyCostPerSqFtChartCanvas.setChart(getEnergyCostPerSqFtChart());
					energyPerBUChartCanvas.setChart(getEnergyPerBUChart());
					demandPerLocationChartCanvas.setChart(getDemandPerLocationChart());
				}
		}
		
		/*
		 * END RMAP-74
		 */
				
		
		initAndAddChart(energyCasesHandledChart);		
		initAndAddChart(energyCostPerSqFtChart);
		energyCostPerSqFtChart.setYAxisUOM("0.00");
		mapComposite.setLayout(new FillLayout());
		createMap(mapComposite);
		
		initAndAddChart(energyPerBUChart);
		energyPerBUChart.setYAxisUOM("0");
		
		initAndAddChart(demandPerLocationChart);
		
		

		
		
		selectionListener = new WorkbenchSelectionListener();
		ISelectionService selectionService = getViewSite().getWorkbenchWindow()
				.getSelectionService();
		selectionService.addSelectionListener(selectionListener);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}
	/*
	 * RMAP-74
	 * Sprint 1.0.2 Start
	 */

	private Chart getEnergyCasesHandledChart_ReturnView(BusinessUnitDataStore buDataStore) {
		energyCasesHandledChart = new PlaceholderChart(
				"Cases Handled per Period");
		energyCasesHandledChart.setShowChartTitle(true);
		energyCasesHandledChart.setXAxisLabel("");
		energyCasesHandledChart.setShowXAxisLabel(false);
		energyCasesHandledChart.setYAxisLabel("Cases Handled");
		Map<String, Double> valueMap = buDataStore.getEnergyCasesHandledChart_valueMap1();
		Map<String, Double> valueMap2 = buDataStore.getEnergyCasesHandledChart_valueMap2();
		energyCasesHandledChart.reassignValueMap(valueMap);
		energyCasesHandledChart.reassignValueMap2(valueMap2);
		return energyCasesHandledChart.createChart();
	}
	
	private Chart getEnergyCostPerSqFtChart_ReturnView(BusinessUnitDataStore buDataStore) {
		energyCostPerSqFtChart = new PlaceholderChart("Energy per SqFt");
		energyCostPerSqFtChart.setXAxisLabel("");
		energyCostPerSqFtChart.setShowXAxisLabel(false);
		energyCostPerSqFtChart.setYAxisLabel("Energy");
		energyCostPerSqFtChart.setRotateYAxisLabel(YAXISROTATION);
		Map<String, Double> valueMap = buDataStore.getEnergyCostPerSqFtChart_valueMap1();
		Map<String, Double> valueMap2 = buDataStore.getEnergyCostPerSqFtChart_valueMap2();
		energyCostPerSqFtChart.reassignValueMap(valueMap);
		energyCostPerSqFtChart.reassignValueMap2(valueMap2);		
		return energyCostPerSqFtChart.createChart();
	}
	
	private void getEnergyPerBUChart_ReturnView(BusinessUnitDataStore buDataStore) {
		Chart chart = getEnergyPerBUChart();
		Map<String, Double> valueMap = buDataStore.getEnergyPerBUChart_valueMap1();
		Map<String, Double> valueMap2 = buDataStore.getEnergyPerBUChart_valueMap2();
		energyPerBUChart.reassignValueMap(valueMap);
		energyPerBUChart.reassignValueMap2(valueMap2);
		energyPerBUChart.setLineAxisUOM("$.000");
		energyPerBUChart.setYAxisUOM("0.0");
		energyPerBUChartCanvas.setChart(chart);

	}
	
	private Chart getDemandPerLocationChart_ReturnView(BusinessUnitDataStore buDataStore) {
		demandPerLocationChart = new PlaceholderChart("Energy per Location");
		demandPerLocationChart.setXAxisLabel("");
		demandPerLocationChart.setYAxisLabel("Energy (1000's of kWh)");
		Map<String, Double> valueMap = buDataStore.getDemandPerLocationChart_valueMap1();
		Map<String, Double> valueMap2 = buDataStore.getDemandPerLocationChart_valueMap2();
		demandPerLocationChart.reassignValueMap(valueMap);
		demandPerLocationChart.reassignValueMap2(valueMap2);
		return demandPerLocationChart.createChart();
	}
	
	/*
	 * END RMAP-74
	 */

	private Chart getEnergyCasesHandledChart() {
		energyCasesHandledChart = new PlaceholderChart(
				"Cases Handled per Period");
		energyCasesHandledChart.setShowChartTitle(true);
		energyCasesHandledChart.setXAxisLabel("");
		energyCasesHandledChart.setShowXAxisLabel(false);
		energyCasesHandledChart.setYAxisLabel("Cases Handled");
		Map<String, Double> valueMap = new ImmutableMap.Builder<String, Double>()
				.put("Santa Fe Springs", 0.0).put("El Monte", 0.0)
				.put("Tracy", 0.0).build();		
		Map<String, Double> valueMap2 = new ImmutableMap.Builder<String, Double>()
				.put("Santa Fe Springs", 0.0).put("El Monte", 0.0)
				.put("Tracy", 0.0).build();
		energyCasesHandledChart.reassignValueMap(valueMap);
		energyCasesHandledChart.reassignValueMap2(valueMap2);
		return energyCasesHandledChart.createChart();
	}	

	private Chart getEnergyCostPerSqFtChart() {
		energyCostPerSqFtChart = new PlaceholderChart("Energy per SqFt");		
		energyCostPerSqFtChart.setXAxisLabel("");
		energyCostPerSqFtChart.setShowXAxisLabel(false);
		energyCostPerSqFtChart.setYAxisLabel("Energy");
		energyCostPerSqFtChart.setRotateYAxisLabel(YAXISROTATION);
		return energyCostPerSqFtChart.createChart();
	}	

	private Chart getEnergyPerBUChart() {
		// energyPerBUChart = new
		// PlaceholderChart("Energy & Cost per Building");
		energyPerBUChart = new PlaceholderBarWLineChart(
				"Energy & Cost per Building");
		energyPerBUChart.setXAxisLabel("");
		energyPerBUChart.setYAxisLabel("Energy (1000's of kWh)");
		energyPerBUChart.setSecondaryYAxisLabel("Cost (K$)");
		/*
		 * @Date : 13-August-2012
		 * 
		 * @Author : RSystems International Ltd
		 * 
		 * @Purpose: RMAP - 17 , Sprint -1
		 */
		// energyPerBUChart.setRotateYAxisLabel(YAXISROTATION);
		// RSI changes END
		return energyPerBUChart.createChart();
	}
	
	private Chart getDemandPerLocationChart() {
		demandPerLocationChart = new PlaceholderChart("Energy per Location");
		demandPerLocationChart.setXAxisLabel("");
		demandPerLocationChart.setYAxisLabel("Energy (1000's of kWh)");
		return demandPerLocationChart.createChart();
	}
	

	public BusinessUnitDataStore updateEnergyCasesHandledChart(List<Long> locationIds,
			List<Period> linkedPeriodList,BusinessUnitDataStore businessUnitDataStore) {
		try {
			
			Map<String, Double> currentValueMap = BusinessUnitReportDAO
					.getPrimaryCasesHandledKWhForLocation(locationIds,
							linkedPeriodList);
			energyCasesHandledChart.reassignValueMap(currentValueMap);
			energyCasesHandledChart.reassignValueMap2(currentValueMap);
			energyCasesHandledChart.populateValues();	
			
			//RMAP-74			
			businessUnitDataStore.setEnergyCasesHandledChart_valueMap1(currentValueMap);
			businessUnitDataStore.setEnergyCasesHandledChart_valueMap2(currentValueMap);
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
			//END
			energyCasesHandledChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessUnitDataStore;
	}

	public BusinessUnitDataStore updateEnergyCostPerSqFtChart(List<Long> buildingIds,
			List<Period> linkedPeriodList, boolean includeLocationName,BusinessUnitDataStore businessUnitDataStore) {
		try {
			
			
			Map<String, Double> currentValueMap = BusinessUnitReportDAO
					.getEnergyPerBuildingPerSquareFoot(buildingIds,
							linkedPeriodList, includeLocationName);			
			energyCostPerSqFtChart.reassignValueMap(currentValueMap);
			energyCostPerSqFtChart.reassignValueMap2(currentValueMap);
			energyCostPerSqFtChart.populateValues();
			
			//RMAP-74
			businessUnitDataStore.setEnergyCostPerSqFtChart_valueMap1(currentValueMap);
			businessUnitDataStore.setEnergyCostPerSqFtChart_valueMap2(currentValueMap);
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
			//END
			energyCostPerSqFtChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessUnitDataStore;
	}

	public BusinessUnitDataStore updateEnergyPerBUChart(List<Long> buildingIds, Date startDt,
			Date endDt, boolean includeLocationName,BusinessUnitDataStore businessUnitDataStore) {
		try {
			
			
			
			Map<String, EnergyAndCostPerBuildingKPIs> kpiValueMap = BusinessUnitReportDAO
					.getEnergyAndCostPerBuilding(buildingIds, startDt, endDt,
							includeLocationName);
			Map<String, Double> currentValueMap = new HashMap<String, Double>();
			Map<String, Double> currentLineMap = new HashMap<String, Double>();
			for (Entry<String, EnergyAndCostPerBuildingKPIs> kpiEntry : kpiValueMap
					.entrySet()) {
				// Set the values for the bar in the graph for the data point.
				// It represents Energy in kWh per building
				// getEnergyPerBuildingPerSqFt()
				currentValueMap.put(kpiEntry.getKey(), kpiEntry.getValue()
						.getKWHUsageKPI().getKpiValue().doubleValue());
				// Set the values for the line point in the graph for the data
				// point. It represents energy cost in K$ per building
				currentLineMap.put(kpiEntry.getKey(), kpiEntry.getValue()
						.getEnergyCostAllocationKPI().getKpiValue()
						.doubleValue());
			}
			energyPerBUChart.reassignValueMap(DataSetUtilities
					.reduceByThousands(currentValueMap));
			energyPerBUChart.reassignValueMap2(DataSetUtilities
					.reduceByThousands(currentValueMap));	
			
			//RMAP-74
			businessUnitDataStore.setEnergyPerBUChart_valueMap1(DataSetUtilities
					.reduceByThousands(currentValueMap));
			businessUnitDataStore.setEnergyPerBUChart_valueMap2(DataSetUtilities
					.reduceByThousands(currentValueMap));
			//END
			
			/*
			 * @Date : 13-August-2012
			 * 
			 * @Author : RSystems International Ltd
			 * 
			 * @Purpose: RMAP - 17 , Sprint -1
			 */
			energyPerBUChart.setLineAxisUOM("$.000");
			energyPerBUChart.setYAxisUOM("0.0");
			// {
			// energyPerBUChart.addLineSeries(DataSetUtilities.reduceByThousands(currentLineMap));
			energyPerBUChart.addLineSeries(currentLineMap);
			// }
			// RSI Changes END
			energyPerBUChart.populateValues();
			
			/*
			 * @Date : 24-August-2012
			 * @Author : RSystems International Ltd
			 * @Purpose: RMAP - 6 , Sprint -1
			 */
			if(buildingIds != null)
			{
				energyPerBUChartCanvas.setToolTipText("Left bar is previous year");
			}
			//END
			energyPerBUChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessUnitDataStore;
	}

	public BusinessUnitDataStore updateDemandPerLocationChart(List<Long> locationIds,
			Date startDt, Date endDt,BusinessUnitDataStore businessUnitDataStore) {
		try {
			
			
			Map<String, Double> currentValueMap = BusinessUnitReportDAO
					.getEnergyPerLocation(locationIds, startDt, endDt);			
			demandPerLocationChart.reassignValueMap(DataSetUtilities
					.reduceByThousands(currentValueMap));
			demandPerLocationChart.reassignValueMap2(DataSetUtilities
					.reduceByThousands(currentValueMap));
			demandPerLocationChart.populateValues();
			
			//RMAP-74
			businessUnitDataStore.setDemandPerLocationChart_valueMap1(DataSetUtilities
					.reduceByThousands(currentValueMap));
			businessUnitDataStore.setDemandPerLocationChart_valueMap2(DataSetUtilities
					.reduceByThousands(currentValueMap));
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
			//END
			demandPerLocationChartCanvas.updateChart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessUnitDataStore;
	}

	@Override
	public void dispose() {
		super.dispose();
		ISelectionService selectionService = getViewSite().getWorkbenchWindow()
				.getSelectionService();
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

	private void initAndAddChart(IBoomerChart newChart) {
		boomerCharts.add(newChart);
		newChart.setShowLegend(showLegends);
		newChart.setShowChartTitle(showTitles);
		newChart.setShowChartNotes(showTitles);
		newChart.setShowXAxisLabel(showAxesLabels);
		newChart.setShowYAxisLabel(showAxesLabels);
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