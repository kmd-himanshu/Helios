package com.helio.app.serviceapi.kpi;

/**
 * This enumerates the various KPI types (Key Performance Indicators) for the
 * Boomer system. Note that each enumeration has a separate name and description
 * for that name. 
 *
 */
public enum KPIType {

	/**
	 * KWH indicates Kilowatt Hours consumed.
	 */
	KWH	("kwh", "Kilowatt Hours"),
	/**
	 * SDV indicates Standard deviation.
	 */
	STANDARD_DEVIATION	("sdv", "Standard Deviation"),
	/**
	 * SDH holds Standard deviation KPIs.
	 */
	STANDARD_DEVIATION_HOLDER	("sdh", "Standard Deviation Holder"),
	/**
	 * ECH indicates Kilowatt per Cases Handled.
	 */
	ENERGY_PER_CASESHANDLED	("ech", "Energy per Cases Handled"),
	/**
	 * ESF indicates Kilowatt per Square Foot.
	 */
	ENERGY_PER_SQFT	("esf", "Energy per Square Foot"),
	/**
	 * CCH indicates Cost per Cases Handled.
	 */
	COST_PER_CASESHANDLED	("cch", "Cost per Cases Handled"),
	/**
	 * EPB indicates energy per building
	 */
	COST_OF_ENERGY	("coe", "Cost of Energy"),
	/**
	 * EPB indicates cost of energy per building
	 */
	COST_OF_ENERGY_PER_SQFT ("ces", "Cost of Energy per Square Foot"),
;
	
	private String kpiName;
	private String kpiDesc;
	
	KPIType(String kpiName, String kpiDesc) {
		this.kpiName = kpiName;
		this.kpiDesc = kpiDesc;
	}
	
	/**
	 * Returns the formal name for the KPI enumeration.
	 * 
	 * @return String indicating KPI name.
	 */
	String kpiName() {
		return kpiName;
	}
	
	/**
	 * Returns the human-readable description string for the KPI enumeration.
	 * 
	 * @return String indicating human-readable KPI description.
	 */
	String kpiDesc() {
		return kpiDesc;
	}
	
}
