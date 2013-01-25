package com.helio.boomer.rap.engine.model.birt;

import java.util.Date;

public class KWHData {

	public Date day;
	public double kWh;
	public double periodMin;
	public double periodMax;
	public double mean;
	public double stDev;
	public double meanUnderSD;
	public double meanOverSD;
	
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	public double getkWh() {
		return kWh;
	}
	public void setkWh(double kWh) {
		this.kWh = kWh;
	}
	public double getPeriodMin() {
		return periodMin;
	}
	public void setPeriodMin(double periodMin) {
		this.periodMin = periodMin;
	}
	public double getPeriodMax() {
		return periodMax;
	}
	public void setPeriodMax(double periodMax) {
		this.periodMax = periodMax;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getStDev() {
		return stDev;
	}
	public void setStDev(double stDev) {
		this.stDev = stDev;
	}
	public double getMeanUnderSD() {
		return mean - stDev;
	}
	public double getMeanOverSD() {
		return mean + stDev;
	}

}
                                                                       