package org.helios.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bcje.model.ChartModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.BusinessUnitReportDAO;


public class EnergyPerSquareFitChart extends HelioChart{

	public EnergyPerSquareFitChart(ChartModel chartModel)
	{
		if(chartModel.getBuildingIds()!=null && chartModel.getBuildingIds().size()>0)
        {
     	   
			try {
				currentValueMap = BusinessUnitReportDAO
						.getEnergyPerBuildingPerSquareFoot(
								chartModel.getBuildingIds(), chartModel.getPeriods(),true);
				reassignValueMap(currentValueMap);
		        reassignValueMap2(currentValueMap);
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
       
        chartModel.setChartTitleLabel("Energy per SqFt");
 		chartModel.setDomainAxisLabel("Building");
 		chartModel.setRangeAxisLabel("Energy");
 		
 		
 		setChartTitleLabel(chartModel.getChartTitleLabel());
 		setDomainAxisLabel(chartModel.getDomainAxisLabel());
 		setRangeAxisLabel(chartModel.getRangeAxisLabel());
 		
        
        
	}
	
	
		

}
