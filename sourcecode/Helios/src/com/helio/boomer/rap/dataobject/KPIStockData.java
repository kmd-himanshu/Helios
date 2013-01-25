package com.helio.boomer.rap.dataobject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.extension.datafeed.StockEntry;
import org.eclipse.birt.chart.util.CDateTime;

import com.helio.app.serviceapi.kpi.StandardDeviationKPI;

public class KPIStockData {

	private List<StandardDeviationKPI> kpis;
	
	public KPIStockData(List<StandardDeviationKPI> kpis) {
		this.kpis = kpis;
	}
	
	public int getSize() {
		return kpis.size();
	}
	
	public List<StockEntry> getStockEntryList() {
		List<StockEntry> stockEntryList = new ArrayList<StockEntry>();
		for (StandardDeviationKPI sdKPI : kpis) {
			StockEntry stockEntry = new StockEntry(
					(sdKPI.getMeanValue() - sdKPI.getStandardDeviation()),
					sdKPI.getMinValue(),
					sdKPI.getMaxValue(),
					(sdKPI.getMeanValue() + sdKPI.getStandardDeviation()));
			stockEntryList.add(stockEntry);
		}
		return stockEntryList;
	}
	
	public List<CDateTime> getStockEntryDateTimeList() {
		List<CDateTime> stockEntryDateTimeList = new ArrayList<CDateTime>();
		for (StandardDeviationKPI sdKPI : kpis) {
			stockEntryDateTimeList.add(new CDateTime(sdKPI.getBegTime()));
		}
		return stockEntryDateTimeList;
	}
	
	public Timestamp getFirstDateOfPeriod() {
		Timestamp earliestDate = null;
		for (StandardDeviationKPI sdKPI : kpis) {
			if ((earliestDate == null) || (sdKPI.getBegTime().before(earliestDate))) {
				earliestDate = sdKPI.getBegTime();
			}
		}
		return earliestDate;
	}
	
	public Timestamp getLatestDateOfPeriod() {
		Timestamp latestDate = null;
		for (StandardDeviationKPI sdKPI : kpis) {
			if ((latestDate == null) || (sdKPI.getBegTime().after(latestDate))) {
				latestDate = sdKPI.getBegTime();
			}
		}
		return latestDate;
	}
	
}
