package com.helio.app.serviceapi.kpi;

import java.sql.Timestamp;

import com.helio.app.boomer.common.dal.model.ContextHolder;
import com.helio.app.serviceapi.HelioInvalidPacketException;

/**
 * Represents a Key Performance Indicator for the Helio Boomer Application.
 * 
 * This class represents a single KPI measurement, which may be one of many used to
 * comprise an overall KPI.
 * 
 */
public class HelioKPI {

	/**
	 * The type of KPI represented by the instance.
	 */
	KPIType kpiType;
	
	/**
	 * Base KPI if this is derived 
	 */
	HelioKPI baseKPI;
	
	/**
	 * The float value of the observed KPI. 
	 */
	Float kpiValue;
	
	/**
	 * The begin date when the KPI represents a range. 
	 */
	Timestamp begTime;
	
	/**
	 * The date for which the KPI applies to.
	 * Also the end date when the KPI represents a range. 
	 */
	Timestamp observationTime;	
	
	/**
	 * Indicates the context of this KPI
	 */
	ContextHolder context;
	
	/**
	 * Creates an empty KPI, designating only the KPIType.
	 * @param {@link KpiType} indicating the KPI type.
	 */
	HelioKPI(KPIType pKPIType, ContextHolder pContextHolder, Float pKPIValue ) throws HelioInvalidKPIException 
	{
		this.kpiType = pKPIType;
		this.context = pContextHolder;
		this.kpiValue = pKPIValue;
		if (pKPIType == null) {
			throw new HelioInvalidKPIException("KPI Type cannot be null");
		}
		if (pContextHolder == null) {
			throw new HelioInvalidKPIException("Context for KPI cannot be null");
		}
		if (pKPIValue < 0f) {
			throw new HelioInvalidKPIException("Value of KPI cannot be less than or equal to zero");
		}
	}
	
	public Float getKpiValue() {
		return kpiValue;
	}

	/**
	 * Return the Key Performance Indicator's instance {@link KPIType}.
	 * @return KPI's type.
	 */
	public KPIType getKpiType() {
		return kpiType;
	}

	public ContextHolder getContext() {
		return context;
	}

	public Timestamp getBegTime() {
		return begTime;
	}

	public Timestamp getObservationTime() {
		return observationTime;
	}

	/**
	 * This method contains the knowledge of how to calculate KW (Demand) for 
	 * pulse and induction meters.
	 * 
	 *  If the AcquiSuite reports 30 pulses during its most recent 1 minute sample and the 
	 *  pulse constant is 0.762 kWh/pulse the following can be calculated:
	 *   	30 pulses X 0.762 kWh/pulse = 22.86 kWh accumulated during the 1 minute period
	 *   	22.86 kWh Ö 1/60 hrs (1 minute) = 1,372 kW average load during the 1 minute period
	 *   	Note: 	kW values calculated on 1 minute intervals are somewhat course.  
	 *   			kW values can be calculated on any time interval to obtain a smoother data 
	 *   			set if desired.
	 *   
	 * @param pRequest contains all objects necessary to calculate KPI for the 
	 * requested meter. 
	 * @return float representing KH demand for the passed request. 
	 */
	public Float getDemand() throws HelioInvalidKPIException, HelioInvalidPacketException
	{		
		if (kpiType != KPIType.KWH) {
			if (baseKPI != null) {
				return baseKPI.getDemand();
			}
			throw new HelioInvalidKPIException("Denmand KPI cannot be determined from this KPI");
		}
		if (kpiValue <= 0) {
			return 0f;
		}
		long elapseTimeMilliseconds 	= observationTime.getTime() - begTime.getTime();
		long elapseTimeMinutes 			= Math.round((elapseTimeMilliseconds / 1000) / 60); 	// rounding to minute

		/*
		 * Demand = energy / ( minutes / minutes in hour) 
		 */
		float KW = kpiValue / (elapseTimeMinutes/60);  
		return KW;
	}
	
}
