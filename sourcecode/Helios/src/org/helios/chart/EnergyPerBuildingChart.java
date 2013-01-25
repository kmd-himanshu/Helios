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

import org.bcje.model.CHART_TYPE;
import org.bcje.model.ChartModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.BusinessUnitReportDAO;
import com.helio.boomer.rap.engine.servicedata.DistributionReportDAO;

public class EnergyPerBuildingChart extends HelioChart {

	public EnergyPerBuildingChart(ChartModel chartModel) {
		if (chartModel.getBuildingIds() != null
				&& chartModel.getBuildingIds().size() > 0) {

			try {

				currentValueMap = DistributionReportDAO.getEnergyKWHPerBuilding(
						chartModel.getBuildingIds(),
						chartModel.getStartSqlDate(),
						chartModel.getEndSqlDate());
				reassignValueMap(currentValueMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (valueMap == null || valueMap.keySet().size() == 0) {
			valueMap = new HashMap<String, Double>();
			valueMap.put("Santa Fe Springs", new Double(0.0));
			valueMap.put("El Monte", new Double(0.0));
			valueMap.put("Tracy", new Double(0.0));
			reassignValueMap2(valueMap);
		
		}
		
		     chartModel.setChartTitleLabel("Energy per Building");	 		
	 		
	 		setChartTitleLabel(chartModel.getChartTitleLabel());
	 		
		
	}

	

	public void reassignValueMap(Map<String, Double> newValueMap) {
		this.valueMap = newValueMap;
	}

	public DefaultPieDataset getPieDataSet(Map<String, Double> valueMap) {

		DefaultPieDataset dataset = new DefaultPieDataset();
		Iterator<String> iterator = valueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			dataset.setValue(key, valueMap.get(key).doubleValue());
		}

		return dataset;
	}

}
