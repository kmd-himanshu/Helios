package com.helio.boomer.rap.service;

import java.sql.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.helio.app.serviceapi.CoreServicesImpl;
import com.helio.app.serviceapi.kpi.HelioKPI;
import com.helio.app.serviceapi.kpi.KWHUsageKPI;

public class HelioServiceDelegate {

	public static List<Double> getDeviceValuesForDate(
			Long monitorId, Date begDate, Date endDate) {
		List<KWHUsageKPI> kpiList = null;
		float interval = 3600f;
		try {
			kpiList = CoreServicesImpl.getInstance().getKwhDateRangeSlices(monitorId, interval, begDate, endDate);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		List<Double> kpiFloatArrayList = Lists.newArrayList();
		for (HelioKPI kpi : kpiList) {
			kpiFloatArrayList.add((double) kpi.getKpiValue());
		}
		return kpiFloatArrayList;
	}

}
