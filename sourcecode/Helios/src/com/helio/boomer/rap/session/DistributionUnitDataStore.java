package com.helio.boomer.rap.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.DateTime;

import com.helio.boomer.rap.birt.ChartCanvas;
import com.helio.boomer.rap.birt.chart.PlaceholderBarWLineChart;
import com.helio.boomer.rap.birt.chart.PlaceholderChart;
import com.helio.boomer.rap.engine.model.Period;

public class DistributionUnitDataStore implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Long> division=null;
	private List<Long> listLocation=null;
	private List<Long> listBuilding=null;	
	private Period onPeriod = null;	
	private Period lastPeriod = null;	
	private Integer beginYear=null;	
	private Integer beginMonth=null;
	private Integer beginDay=null;
	private Integer endYear=null;
	private Integer endMonth=null;
	private Integer endDay=null;
	private boolean isShowLegend = false;
	private boolean isShowAxesLabels = false;
	private boolean isShowTitles = false;
	private Object[] expandedElements = null;
	private ISelection selectedElement = null;
	private ISelection selectedPeriodBegin = null;
	private ISelection selectedPeriodEnd = null;
	
	private Map<String, Double> EnergyCasesHandledChart_valueMap1 = null;	
	private Map<String, Double> EnergyCasesHandledChart_valueMap2 = null;
	private Map<String, Double> EnergyCostPerSqFtChart_valueMap1 = null;
	private Map<String, Double> EnergyCostPerSqFtChart_valueMap2 = null;
	private Map<String, Double> EnergyCHPerBuildingPieChart_valueMap = null;
	private Map<String, Double> EnergyKWHBuildingPieChart_valueMap = null;
	private Map<String, Double> EnergyCostPerBuildingChart_valueMap1 = null;
	private Map<String, Double> EnergyCostPerBuildingChart_valueMap2 = null;
	private Map<String, Double> DemandPerLocationChart_valueMap1 = null;
	private Map<String, Double> demandPerLocationChart_valueMap2 = null;
	
	public Map<String, Double> getEnergyCasesHandledChart_valueMap1() {
		return EnergyCasesHandledChart_valueMap1;
	}
	public void setEnergyCasesHandledChart_valueMap1(
			Map<String, Double> energyCasesHandledChart_valueMap1) {
		EnergyCasesHandledChart_valueMap1 = energyCasesHandledChart_valueMap1;
	}
	
	public List<Long> getDivision() {
		return division;
	}
	public void setDivision(List<Long> division) {
		this.division = division;
	}
	public List<Long> getListLocation() {
		return listLocation;
	}
	public void setListLocation(List<Long> listLocation) {
		this.listLocation = listLocation;
	}
	public List<Long> getListBuilding() {
		return listBuilding;
	}
	public void setListBuilding(List<Long> listBuilding) {
		this.listBuilding = listBuilding;
	}
	public Period getOnPeriod() {
		return onPeriod;
	}
	public void setOnPeriod(Period onPeriod) {
		this.onPeriod = onPeriod;
	}
	public Period getLastPeriod() {
		return lastPeriod;
	}
	public void setLastPeriod(Period lastPeriod) {
		this.lastPeriod = lastPeriod;
	}
	public Integer getBeginYear() {
		return beginYear;
	}
	public void setBeginYear(Integer beginYear) {
		this.beginYear = beginYear;
	}
	public Integer getBeginMonth() {
		return beginMonth;
	}
	public void setBeginMonth(Integer beginMonth) {
		this.beginMonth = beginMonth;
	}
	public Integer getBeginDay() {
		return beginDay;
	}
	public void setBeginDay(Integer beginDay) {
		this.beginDay = beginDay;
	}
	public Integer getEndYear() {
		return endYear;
	}
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	public Integer getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}
	public Integer getEndDay() {
		return endDay;
	}
	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}
	public boolean isShowLegend() {
		return isShowLegend;
	}
	public void setShowLegend(boolean isShowLegend) {
		this.isShowLegend = isShowLegend;
	}
	public boolean isShowAxesLabels() {
		return isShowAxesLabels;
	}
	public void setShowAxesLabels(boolean isShowAxesLabels) {
		this.isShowAxesLabels = isShowAxesLabels;
	}
	public boolean isShowTitles() {
		return isShowTitles;
	}
	public void setShowTitles(boolean isShowTitles) {
		this.isShowTitles = isShowTitles;
	}
	public Object[] getExpandedElements() {
		return expandedElements;
	}
	public void setExpandedElements(Object[] expandedElements) {
		this.expandedElements = expandedElements;
	}
	public ISelection getSelectedElement() {
		return selectedElement;
	}
	public void setSelectedElement(ISelection selectedElement) {
		this.selectedElement = selectedElement;
	}
	public ISelection getSelectedPeriodBegin() {
		return selectedPeriodBegin;
	}
	public void setSelectedPeriodBegin(ISelection selectedPeriodBegin) {
		this.selectedPeriodBegin = selectedPeriodBegin;
	}
	public ISelection getSelectedPeriodEnd() {
		return selectedPeriodEnd;
	}
	public void setSelectedPeriodEnd(ISelection selectedPeriodEnd) {
		this.selectedPeriodEnd = selectedPeriodEnd;
	}
	public Map<String, Double> getEnergyCasesHandledChart_valueMap2() {
		return EnergyCasesHandledChart_valueMap2;
	}
	public void setEnergyCasesHandledChart_valueMap2(
			Map<String, Double> energyCasesHandledChart_valueMap2) {
		EnergyCasesHandledChart_valueMap2 = energyCasesHandledChart_valueMap2;
	}
	public Map<String, Double> getEnergyCostPerSqFtChart_valueMap1() {
		return EnergyCostPerSqFtChart_valueMap1;
	}
	public void setEnergyCostPerSqFtChart_valueMap1(
			Map<String, Double> energyCostPerSqFtChart_valueMap1) {
		EnergyCostPerSqFtChart_valueMap1 = energyCostPerSqFtChart_valueMap1;
	}
	public Map<String, Double> getEnergyCostPerSqFtChart_valueMap2() {
		return EnergyCostPerSqFtChart_valueMap2;
	}
	public void setEnergyCostPerSqFtChart_valueMap2(
			Map<String, Double> energyCostPerSqFtChart_valueMap2) {
		EnergyCostPerSqFtChart_valueMap2 = energyCostPerSqFtChart_valueMap2;
	}
	public Map<String, Double> getEnergyCHPerBuildingPieChart_valueMap() {
		return EnergyCHPerBuildingPieChart_valueMap;
	}
	public void setEnergyCHPerBuildingPieChart_valueMap(
			Map<String, Double> energyCHPerBuildingPieChart_valueMap) {
		EnergyCHPerBuildingPieChart_valueMap = energyCHPerBuildingPieChart_valueMap;
	}
	public Map<String, Double> getEnergyKWHBuildingPieChart_valueMap() {
		return EnergyKWHBuildingPieChart_valueMap;
	}
	public void setEnergyKWHBuildingPieChart_valueMap(
			Map<String, Double> energyKWHBuildingPieChart_valueMap) {
		EnergyKWHBuildingPieChart_valueMap = energyKWHBuildingPieChart_valueMap;
	}
	public Map<String, Double> getEnergyCostPerBuildingChart_valueMap1() {
		return EnergyCostPerBuildingChart_valueMap1;
	}
	public void setEnergyCostPerBuildingChart_valueMap1(
			Map<String, Double> energyCostPerBuildingChart_valueMap1) {
		EnergyCostPerBuildingChart_valueMap1 = energyCostPerBuildingChart_valueMap1;
	}
	public Map<String, Double> getEnergyCostPerBuildingChart_valueMap2() {
		return EnergyCostPerBuildingChart_valueMap2;
	}
	public void setEnergyCostPerBuildingChart_valueMap2(
			Map<String, Double> energyCostPerBuildingChart_valueMap2) {
		EnergyCostPerBuildingChart_valueMap2 = energyCostPerBuildingChart_valueMap2;
	}
	public Map<String, Double> getDemandPerLocationChart_valueMap1() {
		return DemandPerLocationChart_valueMap1;
	}
	public void setDemandPerLocationChart_valueMap1(
			Map<String, Double> demandPerLocationChart_valueMap1) {
		DemandPerLocationChart_valueMap1 = demandPerLocationChart_valueMap1;
	}
	public Map<String, Double> getDemandPerLocationChart_valueMap2() {
		return demandPerLocationChart_valueMap2;
	}
	public void setDemandPerLocationChart_valueMap2(
			Map<String, Double> demandPerLocationChart_valueMap2) {
		this.demandPerLocationChart_valueMap2 = demandPerLocationChart_valueMap2;
	}
	
	
	
	
	
	
	
	
	


}
