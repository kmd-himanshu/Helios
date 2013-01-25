package org.helios.chart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bcje.model.ChartModel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.utility.NumericUtility;

public abstract class HelioChart {
	final String series1 = "Previous Year";
	final String series2 = "Current Year";
	Map<String, Double> valueMap = null;
	Map<String, Double> valueMap2 = null;
	Map<String, Double> valueLineMap = null;
	public Map<String, Double> currentValueMap=null;
	
	private String chartTitleLabel = null;
	private String domainAxisLabel = null;
	private String rangeAxisLabel = null;
	private String rangeAxisLabel2 = null;

	public String getRangeAxisLabel2() {
		return rangeAxisLabel2;
	}

	public void setRangeAxisLabel2(String rangeAxisLabel2) {
		this.rangeAxisLabel2 = rangeAxisLabel2;
	}

	public String getChartTitleLabel() {
		return chartTitleLabel;
	}

	public void setChartTitleLabel(String chartTitleLabel) {
		this.chartTitleLabel = chartTitleLabel;
	}

	public String getDomainAxisLabel() {
		return domainAxisLabel;
	}

	public void setDomainAxisLabel(String domainAxisLabel) {
		this.domainAxisLabel = domainAxisLabel;
	}

	public String getRangeAxisLabel() {
		return rangeAxisLabel;
	}

	public void setRangeAxisLabel(String rangeAxisLabel) {
		this.rangeAxisLabel = rangeAxisLabel;
	}

	/*public void reassignValueMap(Map<String, Double> value) {
		valueMap = new HashMap<String, Double>(value);
		for (Entry<String, Double> valueEntry : valueMap.entrySet()) {
			Double newValue = NumericUtility.getPrecDouble(
					valueEntry.getValue(), 2);
			valueEntry.setValue(newValue);
		}
	}

	public void reassignValueMap2(Map<String, Double> value) {
		valueMap2 = new HashMap<String, Double>(value);
		for (Entry<String, Double> valueEntry : valueMap2.entrySet()) {
			Double newValue = NumericUtility.modifyByRandom(valueEntry
					.getValue());
			if (newValue < 0.0)
				newValue = 0.0;
			valueEntry.setValue(NumericUtility.getPrecDouble(newValue, 2));
		}
	}
	*/
	
	public void reassignValueMap(Map<String, Double> newValueMap) {
		this.valueMap=new HashMap<String, Double>();
		for (Entry<String, Double> valueEntry : newValueMap.entrySet()) {
			this.valueMap.put(valueEntry.getKey(), NumericUtility.getPrecDouble(valueEntry.getValue(), 2));
		}
	}
	
	public void reassignValueMap2(Map<String, Double> newValueMap) {
		this.valueMap2=new HashMap<String, Double>();
		for (Entry<String, Double> valueEntry : newValueMap.entrySet()) {
			Double newValue = NumericUtility.modifyByRandom(valueEntry.getValue());
			if (newValue < 0.0) newValue = 0.0;
			this.valueMap2.put(valueEntry.getKey(), NumericUtility.getPrecDouble(newValue, 2));
		}
	}
	

	public DefaultCategoryDataset getDataSet(Map<String, Double> valueMap,
			Map<String, Double> valueMap2) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Iterator<String> iterator = valueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			dataset.setValue(valueMap2.get(key).doubleValue(), series1, key);
			dataset.setValue(valueMap.get(key).doubleValue(), series2, key);
		}

		return dataset;
	}

	public DefaultCategoryDataset getDataSet(Map<String, Double> valueMap) {

		if (valueMap == null)
			return null;

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Iterator<String> iterator = valueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			dataset.setValue(valueMap.get(key).doubleValue(), series2, key);
		}

		return dataset;
	}

	public Map<String, Double> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Double> valueMap) {
		this.valueMap = valueMap;
	}

	public Map<String, Double> getValueMap2() {
		return valueMap2;
	}

	public void setValueMap2(Map<String, Double> valueMap2) {
		this.valueMap2 = valueMap2;
	}

	public Map<String, Double> getValueLineMap() {
		return valueLineMap;
	}

	public void setValueLineMap(Map<String, Double> valueLineMap) {
		this.valueLineMap = valueLineMap;
	}

	public JFreeChart createChart(ChartModel chartModel) {

		JFreeChart chart = null;

		if ("BAR".equalsIgnoreCase(chartModel.getType())) {
			BarChart barChart = new BarChart();

			chart = barChart.createChart(getChartTitleLabel(),
					getDomainAxisLabel(), getRangeAxisLabel(),
					getDataSet(valueMap, valueMap2));
		} else if ("MULTI_AXIS_BAR".equalsIgnoreCase(chartModel.getType())) {
			MultiAxisBarChart axisBarChart = new MultiAxisBarChart();

			chart = axisBarChart.createChart(getChartTitleLabel(),
					getDomainAxisLabel(), getRangeAxisLabel(),
					getRangeAxisLabel2(), getDataSet(valueMap, valueMap2),
					getDataSet(valueLineMap));
		} else if ("PIE".equalsIgnoreCase(chartModel.getType())) {
			PieChart pieChart = new PieChart();

			chart = pieChart.createChart(getChartTitleLabel(),
					getPieDataSet(valueMap));
		}
		return chart;

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
