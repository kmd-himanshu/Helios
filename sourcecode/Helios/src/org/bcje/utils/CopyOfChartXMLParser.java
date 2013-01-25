package org.bcje.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bcje.model.ChartCategory;
import org.bcje.model.ChartDataSet;
import org.bcje.model.ChartModel;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Maps;
import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.BusinessUnitReportDAO;
import com.helio.boomer.rap.utility.NumericUtility;

public class CopyOfChartXMLParser {

	private final String xml;

	public CopyOfChartXMLParser(String xml) {
		this.xml = xml;
	}

	public ChartModel createChart() {
		ChartModel chart = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder parser = factory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			Document doc = parser.parse(is);

			Node root = doc.getFirstChild();
			NamedNodeMap chartAttr = root.getAttributes();
			String type = chartAttr.getNamedItem("type").getNodeValue();
			String dimension = chartAttr.getNamedItem("dimension")
					.getNodeValue();
			String format = chartAttr.getNamedItem("format").getNodeValue();
			String width = chartAttr.getNamedItem("width").getNodeValue();
			String height = chartAttr.getNamedItem("height").getNodeValue();
			chart = new ChartModel(type, dimension, format, Double
					.parseDouble(width), Double.parseDouble(height));

			String title = chartAttr.getNamedItem("title").getNodeValue();
			chart.setTitle(title);

			chart.setStacked(getBooleanProperty(chartAttr, "stacked"));
			chart.setColorByCategory(getBooleanProperty(chartAttr,
					"colorByCategory"));
			chart.setShowLabel(getBooleanProperty(chartAttr, "showLabel"));
			chart.setShowLegend(getBooleanProperty(chartAttr, "showLegend"));					
			String period=chartAttr.getNamedItem("period").getNodeValue();
			
			chart.setPeriod(period);
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().equals("categories")) {
					handleCategories(chart, node);
				} else if (node.getNodeName().equals("dataset")) {
					handleValues(chart, node);
				}else if (node.getNodeName().equals("buildings")) {
						
						NodeList buildingList = node.getChildNodes();
						List<Long> buildingIds=new ArrayList<Long>();
						for (int j = 0; j< buildingList.getLength(); j++) {
							NamedNodeMap namedNode = buildingList.item(j).getAttributes();
							if (namedNode != null) {
								String label = namedNode.getNamedItem("label").getNodeValue();
								buildingIds.add(new Long(label));
							}
						}
						chart.setBuildingIds(buildingIds);
						
					}  else  if (node.getNodeName().equals("script")) {
					if (node.getFirstChild() != null) {
						chart.setScript(node.getFirstChild().getNodeValue());
					}
				}
			}
			if(chart.getCategories()==null || chart.getCategories().size()==0){
			List<Period> periods=new ArrayList<Period>();
			Period period1=PeriodListController.getInstance().getPeriod(new Long(chart.getPeriod()));			
			periods.add(period1);			
			Map<String, Double> valueMap = BusinessUnitReportDAO.getEnergyPerBuildingPerSquareFoot(chart.getBuildingIds(),periods, true);
			/*Map<String, String> currentValueMap=new HashMap<String, String>();
			Map<String, String> currentValueMap2=new HashMap<String, String>();
			currentValueMap.put("GLOCERY-SFS","1.127567890344");
			currentValueMap.put("GLOCERY-CPW","5.124545656");
			currentValueMap2.put("GLOCERY-SFS","1.0");
			currentValueMap2.put("GLOCERY-CPW","5.0");*/
			Map<String, Double> currentValueMap=reassignValueMap(valueMap);
			Map<String, Double> currentValueMap2=reassignValueMap2(valueMap);
			handleCategories(chart,currentValueMap,currentValueMap2);
			handleValues(chart,currentValueMap,currentValueMap2);
			}
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chart;
	}
	
	public Map<String, Double> reassignValueMap(Map<String, Double> newValueMap) {
		Map<String, Double> valueMap = Maps.newHashMap(newValueMap);
		for (Entry<String, Double> valueEntry : valueMap.entrySet()) {
			Double newValue = NumericUtility.getPrecDouble(valueEntry.getValue(), 2);
			valueEntry.setValue(newValue);
		}
		return valueMap;
	}
	
	public Map<String, Double> reassignValueMap2(Map<String, Double> newValueMap) {
		Map<String, Double> valueMap= Maps.newHashMap(newValueMap);
		for (Entry<String, Double> valueEntry : valueMap.entrySet()) {
			Double newValue = NumericUtility.modifyByRandom(valueEntry.getValue());
			if (newValue < 0.0) newValue = 0.0;
			valueEntry.setValue(NumericUtility.getPrecDouble(newValue, 2));
		}
		return valueMap;
	}


	private void handleCategories(ChartModel cm, Map<String, Double> currentValueMap, Map<String, Double> currentValueMap2) {
		Iterator<String> iterator=currentValueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			cm.getCategories().add(new ChartCategory(key));	
		}
		iterator=currentValueMap2.keySet().iterator();
		/*while (iterator.hasNext()) {
			String key = (String) iterator.next();
		
			cm.getCategories().add(new ChartCategory(key));	
		}*/
		
		
	}

	private void handleValues(ChartModel cm,Map<String, Double> currentValueMap, Map<String, Double> currentValueMap2) {
		
		Iterator<String> iterator=currentValueMap2.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = currentValueMap2.get(key).doubleValue()+"";
			ChartDataSet dataset = new ChartDataSet(Double
					.parseDouble(value));
			dataset.setTooltip(key+":"+value);
			//dataset.setUrl(getNodeValue(namedNode, "url"));
			cm.getDatasets().add(dataset);	
		}
		//cm.getSeriesNames().add(getNodeValue(values.getAttributes(), "name"));
		iterator=currentValueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = currentValueMap.get(key).doubleValue()+"";
			ChartDataSet dataset = new ChartDataSet(Double
					.parseDouble(value));
			dataset.setTooltip(key+":"+value);
			//dataset.setUrl(getNodeValue(namedNode, "url"));
			cm.getDatasets().add(dataset);	
		}
		
		
	}

	private void handleCategories(ChartModel cm, Node categories) {
		NodeList categoryList = categories.getChildNodes();
		for (int i = 0; i < categoryList.getLength(); i++) {
			NamedNodeMap namedNode = categoryList.item(i).getAttributes();
			if (namedNode != null) {
				String label = namedNode.getNamedItem("label").getNodeValue();
				cm.getCategories().add(new ChartCategory(label));
			}
		}
	}

	private void handleValues(ChartModel cm, Node values) {
		cm.getSeriesNames().add(getNodeValue(values.getAttributes(), "name"));

		NodeList valueList = values.getChildNodes();
		for (int i = 0; i < valueList.getLength(); i++) {
			NamedNodeMap namedNode = valueList.item(i).getAttributes();
			if (namedNode != null) {
				String label = getNodeValue(namedNode, "value");
				if (label != null) {
					ChartDataSet dataset = new ChartDataSet(Double
							.parseDouble(label));
					dataset.setTooltip(getNodeValue(namedNode, "tooltip"));
					dataset.setUrl(getNodeValue(namedNode, "url"));
					cm.getDatasets().add(dataset);
				}
			}
		}

	}

	private String getNodeValue(NamedNodeMap namedNode, String nodeName) {
		Node node = namedNode.getNamedItem(nodeName);
		if (node != null) {
			return node.getNodeValue();
		}
		return null;
	}

	private boolean getBooleanProperty(NamedNodeMap chartAttr,
			String propertyName) {
		Node node = chartAttr.getNamedItem(propertyName);
		return node != null && node.getNodeValue() != null
				&& Boolean.parseBoolean(node.getNodeValue());
	}
}
