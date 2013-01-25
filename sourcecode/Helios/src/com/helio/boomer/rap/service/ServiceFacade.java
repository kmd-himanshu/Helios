package com.helio.boomer.rap.service;

import java.sql.Date;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;
import com.helio.app.serviceapi.CoreServicesImpl;
import com.helio.app.serviceapi.kpi.HelioKPI;
import com.helio.app.serviceapi.kpi.KWHUsageKPI;

public class ServiceFacade {

	public static String testKwhDateRangeSlices() {
		//
		Date begDate = Date.valueOf("2011-08-25");
		Date endDate = Date.valueOf("2011-08-28");	// 3 days
		float interval = 3600f;						// 1 hour
		long monitorId = 8l;						// Monitor #8

		List<KWHUsageKPI> kpiList = null;
		try {
			kpiList = CoreServicesImpl.getInstance().getKwhDateRangeSlices(monitorId, interval, begDate, endDate);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return "Size of KPI List =" + kpiList.size();
	}

	public static List<Double> getSampleReportValues() {
		Date begDate = Date.valueOf("2011-08-25");
		Date endDate = Date.valueOf("2011-08-26");
		List<KWHUsageKPI> kpiList = null;
		float interval = 3600f;
		long monitorId = 14l;
		try {
			MessageDialog.openInformation(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"API Info",
					"Trying to get kpi for: " +
					"Monitor:" + monitorId +
					"; Interval:" + interval +
					"; Start:" + begDate +
					"; End:" + endDate);
			kpiList = CoreServicesImpl.getInstance().getKwhDateRangeSlices(monitorId, interval, begDate, endDate);
			if (kpiList == null) {
				MessageDialog.openInformation(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"API Failure!",
				"Null result");
			} else {
				MessageDialog.openInformation(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"API Result!",
						"Good result-->" + kpiList.toString());
			}
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			MessageDialog.openInformation(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"API Error",
					"Error during sample call: " + ex.toString());
		}
		List<Double> kpiFloatArrayList = Lists.newArrayList();
		for (HelioKPI kpi : kpiList) {
			kpiFloatArrayList.add((double) kpi.getKpiValue());
		}
		return kpiFloatArrayList;
	}

	public static HelioKPI testEnergyPerCasesHandledPerLocation() 
	{
		Date begDate = Date.valueOf("2011-10-15");
		Date endDate = Date.valueOf("2011-10-19"); // 3 days
		long id = 1801l;

		HelioKPI kpi = null;
		try {
			MessageDialog.openInformation(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"API Info",
					"Trying to get kpi for: " +
					"ID:" + id +
					"; Start:" + begDate +
					"; End:" + endDate);
			kpi = CoreServicesImpl.getInstance().getEnergyPerCasesHandledPerLocation(id, begDate, endDate);
			if (kpi == null) {
				MessageDialog.openInformation(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"API Failure!",
				"Null result");
			} else {
				MessageDialog.openInformation(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"API Result!",
						"Good result-->" + kpi.toString());
			}
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			MessageDialog.openInformation(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"API Error",
					"Error during sample call: " + ex.toString());
		}
		return kpi;
	}

}
