package com.helio.app.boomer.common.dal.model;

import java.sql.Timestamp;

public class BoomerMetric {

	private MetricTypeEnum metricType;
	private String metricStr;
	private Timestamp begDate;
	private Timestamp endDate;
	private String periodName;
	private long periodId;
	
	public MetricTypeEnum getMetricType() {
		return metricType;
	}
	public void setMetricType(MetricTypeEnum metricType) {
		this.metricType = metricType;
	}
	public String getMetricStr() {
		return metricStr;
	}
	public void setMetricStr(String metricStr) {
		this.metricStr = metricStr;
	}
	public Timestamp getBegDate() {
		return begDate;
	}
	public void setBegDate(Timestamp begDate) {
		this.begDate = begDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	public long getPeriodId() {
		return periodId;
	}
	public void setPeriodId(long periodId) {
		this.periodId = periodId;
	}
		
}
