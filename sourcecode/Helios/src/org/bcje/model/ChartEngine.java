package org.bcje.model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.helios.chart.BusinessEnergyCaseHandledPerPeriodChart;
import org.helios.chart.BusinessEnergyCostPerBuildingChart;
import org.helios.chart.BusinessEnergyPerLocationChart;
import org.helios.chart.EnergyCostPerSqFtChart;
import org.helios.chart.EnergyPerBuildingChart;
import org.helios.chart.EnergyPerCasesHandledPerBuildingChart;
import org.helios.chart.EnergyPerSquareFitChart;
import org.helios.chart.HelioChart;
import org.helios.chart.PlantEnergyCaseHandledPerPeriodChart;
import org.helios.chart.PlantEnergyCostPerBuildingChart;
import org.helios.chart.PlantEnergyPerLocationChart;
import org.jfree.chart.JFreeChart;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;

public class ChartEngine {
	final String series1 = "Previous Year";
    final String series2 = "Current Year";
    SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy");
	public  JFreeChart getChart(ChartModel chartModel) {
		
		List<Period> periods=new ArrayList<Period>();
		Period period=null;
		
		if(chartModel.getStartPeriod()!=null && chartModel.getEndPeriod()!=null)
		{
						
		Period onPeriod = PeriodListController.getInstance().getPeriod(
				new Long(chartModel.getStartPeriod()));

		Period lastPeriod = PeriodListController.getInstance().getPeriod(
				new Long(chartModel.getEndPeriod()));

		periods.add(onPeriod);
		
		while ((onPeriod != lastPeriod) && (onPeriod != null)) {
			onPeriod = onPeriod.nextPeriod;
			if (onPeriod != null)
				periods.add(onPeriod);
		}
		
		}
		/*if(chartModel.getEndDate()!=null)
		{
			period=PeriodListController.getInstance().getPeriod(
				new Long(chartModel.getEndPeriod()));
			periods.add(period);
			chartModel.setEndDate(format.format(period.getEndDt()));
			
		}*/
		
		
		chartModel.setPeriods(periods);
		
		
		JFreeChart chart = null;
		
		try{
			
			java.util.Date startDt=format.parse(chartModel.getStartDate());
			java.util.Date endDt=format.parse(chartModel.getEndDate());
			Calendar begCalendar = new GregorianCalendar(startDt
					.getYear()+1900, startDt.getMonth(), startDt
					.getDate());
			Date begDate = new Date(begCalendar.getTimeInMillis());
			Calendar endCalendar = new GregorianCalendar(endDt
					.getYear()+1900, endDt.getMonth(), endDt
					.getDate());
			Date endDate = new Date(endCalendar.getTimeInMillis());
			chartModel.setStartSqlDate(begDate);
			chartModel.setEndSqlDate(endDate);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.CASES_HANDLED_PER_PERIOD.toString())) {
			chartModel.setType("BAR");
			chart = new BusinessEnergyCaseHandledPerPeriodChart(chartModel).createChart(chartModel);
			
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_SQFT.toString())) {
			chartModel.setType("BAR");
			chart = new EnergyPerSquareFitChart(chartModel).createChart(chartModel);
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_AND_COST_PER_BUILDING.toString())) {
			chartModel.setType("MULTI_AXIS_BAR");
			chart = new BusinessEnergyCostPerBuildingChart(chartModel).createChart(chartModel);
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_LOCATION.toString())) {
			chartModel.setType("BAR");
			chart = new BusinessEnergyPerLocationChart(chartModel).createChart(chartModel);
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.CASES_HANDLED_PER_PERIOD_PLANT.toString())) {
			chartModel.setType("BAR");
			chart = new PlantEnergyCaseHandledPerPeriodChart(chartModel).createChart(chartModel);
			
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_CASES_HANDLED_PER_BUILDING.toString())) {
			chartModel.setType("PIE");
			chart = new EnergyPerCasesHandledPerBuildingChart(chartModel).createChart(chartModel);
		}else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_BUILDING.toString())) {
			chartModel.setType("PIE");
			chart = new EnergyPerBuildingChart(chartModel).createChart(chartModel);
		}else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_AND_COST_PER_BUILDING_PLANT.toString())) {
			chartModel.setType("MULTI_AXIS_BAR");
			chart = new PlantEnergyCostPerBuildingChart(chartModel).createChart(chartModel);
		}  else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_AND_COST_PER_SQFT.toString())) {
			chartModel.setType("MULTI_AXIS_BAR");
			chart = new EnergyCostPerSqFtChart(chartModel).createChart(chartModel);
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_LOCATION_PLANT.toString())) {
			chartModel.setType("BAR");
			chart = new PlantEnergyPerLocationChart(chartModel).createChart(chartModel);
			
		}
		
		return chart;

	}
	
	
	
public ArrayList<ArrayList<String>> getDataList(ChartModel chartModel) {
		
		List<Period> periods=new ArrayList<Period>();
		Period period=null;
		
		if(chartModel.getStartPeriod()!=null && chartModel.getEndPeriod()!=null)
		{
						
		Period onPeriod = PeriodListController.getInstance().getPeriod(
				new Long(chartModel.getStartPeriod()));

		Period lastPeriod = PeriodListController.getInstance().getPeriod(
				new Long(chartModel.getEndPeriod()));

		periods.add(onPeriod);
		
		while ((onPeriod != lastPeriod) && (onPeriod != null)) {
			onPeriod = onPeriod.nextPeriod;
			if (onPeriod != null)
				periods.add(onPeriod);
		}
		
		}
		/*if(chartModel.getEndDate()!=null)
		{
			period=PeriodListController.getInstance().getPeriod(
				new Long(chartModel.getEndPeriod()));
			periods.add(period);
			chartModel.setEndDate(format.format(period.getEndDt()));
			
		}*/
		
		
		chartModel.setPeriods(periods);
		
		
		 Map<String, Double> valueMap =null; 
		 Map<String, Double> valueMap2 =null; 
		 Map<String, Double> valueLineMap =null; 
		try{
			
			java.util.Date startDt=format.parse(chartModel.getStartDate());
			java.util.Date endDt=format.parse(chartModel.getEndDate());
			Calendar begCalendar = new GregorianCalendar(startDt
					.getYear()+1900, startDt.getMonth(), startDt
					.getDate());
			Date begDate = new Date(begCalendar.getTimeInMillis());
			Calendar endCalendar = new GregorianCalendar(endDt
					.getYear()+1900, endDt.getMonth(), endDt
					.getDate());
			Date endDate = new Date(endCalendar.getTimeInMillis());
			chartModel.setStartSqlDate(begDate);
			chartModel.setEndSqlDate(endDate);
		}catch (Exception e) {
			// TODO: handle exception
		}
		HelioChart helioChart=null;  
	
		if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.CASES_HANDLED_PER_PERIOD.toString())) {
			chartModel.setType("BAR");
			helioChart = new BusinessEnergyCaseHandledPerPeriodChart(chartModel);
			
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_SQFT.toString())) {
			chartModel.setType("BAR");
			helioChart = new EnergyPerSquareFitChart(chartModel);
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_AND_COST_PER_BUILDING.toString())) {
			chartModel.setType("MULTI_AXIS_BAR");
			helioChart = new BusinessEnergyCostPerBuildingChart(chartModel);
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_LOCATION.toString())) {
			chartModel.setType("BAR");
			helioChart = new BusinessEnergyPerLocationChart(chartModel);
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.CASES_HANDLED_PER_PERIOD_PLANT.toString())) {
			chartModel.setType("BAR");
			helioChart = new PlantEnergyCaseHandledPerPeriodChart(chartModel);
			
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_CASES_HANDLED_PER_BUILDING.toString())) {
			chartModel.setType("PIE");
			helioChart = new EnergyPerCasesHandledPerBuildingChart(chartModel);
		}else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_BUILDING.toString())) {
			chartModel.setType("PIE");
			helioChart = new EnergyPerBuildingChart(chartModel);
		}else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_AND_COST_PER_BUILDING_PLANT.toString())) {
			chartModel.setType("MULTI_AXIS_BAR");
			helioChart = new PlantEnergyCostPerBuildingChart(chartModel);
		}  else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_AND_COST_PER_SQFT.toString())) {
			chartModel.setType("MULTI_AXIS_BAR");
			helioChart = new EnergyCostPerSqFtChart(chartModel);
		} else if (chartModel.getHeliosChartType().equalsIgnoreCase(
				CHART_TYPE.ENERGY_PER_LOCATION_PLANT.toString())) {
			chartModel.setType("BAR");
			helioChart = new PlantEnergyPerLocationChart(chartModel);
			
		}
		valueMap=helioChart.getValueMap();
		valueMap2=helioChart.getValueMap2();
		valueLineMap=helioChart.getValueLineMap();
		ArrayList<String>  inner=new ArrayList<String>();
		
		
		
		ArrayList<ArrayList<String>>  outer=new ArrayList<ArrayList<String>>();
		if(valueMap2!=null){
			inner.add("'Year'");
			inner.add("'Previous Year'");
			inner.add("'Current Year'");	
			
		}
		if(valueLineMap!=null)
		{
			inner.add("'Cost'");	
		}
		
		if(inner!=null && inner.size()>0)
		outer.add(inner);
		
		
		 Iterator<String> iterator=valueMap.keySet().iterator();
		 while (iterator.hasNext()) {
			String key = (String) iterator.next();
			inner=new ArrayList<String>();
			inner.add("'"+key+"'");
			if(valueMap2!=null){
			inner.add(String.valueOf(valueMap2.get(key)));
			}
			inner.add(String.valueOf(valueMap.get(key)));
			if(valueLineMap!=null)
			{
				inner.add(String.valueOf(valueLineMap.get(key)));
			}
			outer.add(inner);
		}
		
		
		return outer;

	}
	
	
	

	
}
