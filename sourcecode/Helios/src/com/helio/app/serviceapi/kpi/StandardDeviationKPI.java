package com.helio.app.serviceapi.kpi;

import java.sql.Timestamp;

import com.helio.app.boomer.common.dal.model.ContextHolder;

public class StandardDeviationKPI extends HelioKPI {
	/**
	 * The float value of the minimum observed for the KPI. 
	 */
	Float minValue;
	
	/**
	 * The float value of the maximum observed for the KPI. 
	 */
	Float maxValue;
	
	/**
	 * The float value of the mean (average) observed for the KPI. 
	 */
	Float meanValue;
	
	/**
	 * The float value of the standard deviation observed for the KPI. 
	 */
	Float standardDeviation;
	
	/**
	 * Creates an KPI with value, and the date 
	 * @param {@link KpiType} indicating the KPI type.
	 * @param Float indicating the observed KPI value.
	 */
	public StandardDeviationKPI(ContextHolder pContextHolder, 
								Float pKPIValue, 
								Timestamp pBegTime, 
								Timestamp pObservationTime, 
								Float pMinValue,
								Float pMaxValue, 
								Float pMeanValue,
								Float pStandardDeviation) 
	throws HelioInvalidKPIException
	{
		super(KPIType.STANDARD_DEVIATION, pContextHolder, pKPIValue);
		this.begTime = pBegTime;
		this.observationTime = pObservationTime;
		this.minValue = pMinValue;
		this.maxValue = pMaxValue;
		this.meanValue = pMeanValue;
		this.standardDeviation = pStandardDeviation;
	}
	
	public Float getMinValue() {
		return minValue;
	}

	public Float getMaxValue() {
		return maxValue;
	}

	public Float getMeanValue() {
		return meanValue;
	}

	public Float getStandardDeviation() {
		return standardDeviation;
	}	

}
