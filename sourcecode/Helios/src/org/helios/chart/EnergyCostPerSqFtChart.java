package org.helios.chart;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bcje.model.ChartModel;

import com.helio.boomer.rap.engine.servicedata.DistributionReportDAO;
import com.helio.boomer.rap.engine.servicedata.EnergyAndCostPerSqFtKPIs;
import com.helio.boomer.rap.utility.DataSetUtilities;

public class EnergyCostPerSqFtChart extends HelioChart {
	

	public EnergyCostPerSqFtChart(ChartModel chartModel) {
		if (chartModel.getBuildingIds() != null
				&& chartModel.getBuildingIds().size() > 0) {

			try {

				Map<String, EnergyAndCostPerSqFtKPIs> kpiValueMap = DistributionReportDAO
						.getEnergyAndCostPerSqFt(chartModel.getBuildingIds(),
								chartModel.getStartSqlDate(),
								chartModel.getEndSqlDate());

				if (kpiValueMap != null && kpiValueMap.keySet().size() > 0) {
					currentValueMap = new HashMap<String, Double>();
					valueLineMap = new HashMap<String, Double>();
				}
				for (Entry<String, EnergyAndCostPerSqFtKPIs> kpiEntry : kpiValueMap
						.entrySet()) {
					// Set the values for the bar in the graph for the data
					// point. It represents Energy in kWh/sqft
					// getEnergyPerBuildingPerSqFt()
					currentValueMap.put(kpiEntry.getKey(), kpiEntry.getValue()
							.getEnergyPerSquareFootKPI().getKpiValue()
							.doubleValue());
					// Set the values for the line point in the graph for the
					// data point. It represents energy cost in $/sqft
					valueLineMap.put(kpiEntry.getKey(), kpiEntry.getValue()
							.getEnergyCostPerSquareFootKPI().getKpiValue()
							.doubleValue());
				}
				reassignValueMap(currentValueMap);
				reassignValueMap2(currentValueMap);
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
		    chartModel.setChartTitleLabel("Energy & Cost per SqFt");
	 		chartModel.setDomainAxisLabel("Building");
	 		chartModel.setRangeAxisLabel("Energy (1000's of kWh)");
	 		chartModel.setRangeAxisLabel2("Cost ($)/SqFt");
	 		
	 		setChartTitleLabel(chartModel.getChartTitleLabel());
	 		setDomainAxisLabel(chartModel.getDomainAxisLabel());
	 		setRangeAxisLabel(chartModel.getRangeAxisLabel());
	 		setRangeAxisLabel2(chartModel.getRangeAxisLabel2());
	 		
		

	}

	
}
