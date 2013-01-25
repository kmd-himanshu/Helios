package org.helios.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bcje.model.ChartModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.BusinessUnitReportDAO;
import com.helio.boomer.rap.engine.servicedata.EnergyAndCostPerBuildingKPIs;
import com.helio.boomer.rap.utility.DataSetUtilities;


public class BusinessEnergyCostPerBuildingChart extends HelioChart{
	
	public BusinessEnergyCostPerBuildingChart(ChartModel chartModel){
		
	
     if(chartModel.getBuildingIds()!=null && chartModel.getBuildingIds().size()>0)
     {
    	 
		try {
			
			Map<String, EnergyAndCostPerBuildingKPIs> kpiValueMap = BusinessUnitReportDAO
					.getEnergyAndCostPerBuilding(chartModel.getBuildingIds(), chartModel.getStartSqlDate(), chartModel.getEndSqlDate(),
							true);
			if(kpiValueMap!=null && kpiValueMap.keySet().size()>0)
			{  
				currentValueMap=new HashMap<String, Double>();
				valueLineMap=new HashMap<String, Double>();
			}
			for (Entry<String, EnergyAndCostPerBuildingKPIs> kpiEntry : kpiValueMap
					.entrySet()) {
				// Set the values for the bar in the graph for the data point.
				// It represents Energy in kWh per building
				// getEnergyPerBuildingPerSqFt()
				currentValueMap.put(kpiEntry.getKey(), kpiEntry.getValue()
						.getKWHUsageKPI().getKpiValue().doubleValue());
				// Set the values for the line point in the graph for the data
				// point. It represents energy cost in K$ per building
				valueLineMap.put(kpiEntry.getKey(), kpiEntry.getValue()
						.getEnergyCostAllocationKPI().getKpiValue()
						.doubleValue());
			}
			 reassignValueMap(DataSetUtilities
						.reduceByThousands(currentValueMap));
		     reassignValueMap2(DataSetUtilities
						.reduceByThousands(currentValueMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     if(valueMap == null || valueMap.keySet().size()==0){
    	 valueMap=new HashMap<String, Double>();
    	 valueMap.put("Santa Fe Springs", new Double(0.0));
    	 valueMap.put("El Monte", new Double(0.0));
    	 valueMap.put("Tracy", new Double(0.0));
    	 reassignValueMap2(valueMap);
    	 
    	  
     }
     
     chartModel.setChartTitleLabel("Energy & Cost per Building");
	  chartModel.setDomainAxisLabel("Building");
		chartModel.setRangeAxisLabel("Energy (1000's of kWh)");
		chartModel.setRangeAxisLabel2("Cost (K$)");
		
		setChartTitleLabel(chartModel.getChartTitleLabel());
		setDomainAxisLabel(chartModel.getDomainAxisLabel());
		setRangeAxisLabel(chartModel.getRangeAxisLabel());
		setRangeAxisLabel2(chartModel.getRangeAxisLabel2());
     
    
	}
	
	
	
}
