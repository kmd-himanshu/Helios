package org.bcje.model;

public enum CHART_TYPE {

	CASES_HANDLED_PER_PERIOD,ENERGY_PER_SQFT,ENERGY_AND_COST_PER_BUILDING,ENERGY_PER_LOCATION,
	ENERGY_PER_CASES_HANDLED_PER_BUILDING,ENERGY_PER_BUILDING,ENERGY_AND_COST_PER_SQFT,CASES_HANDLED_PER_PERIOD_PLANT,ENERGY_PER_LOCATION_PLANT,ENERGY_AND_COST_PER_BUILDING_PLANT;
	@Override public String toString() {   
		//only capitalize the first letter   
		String s = super.toString();   
		return s.substring(0, 1) + s.substring(1).toLowerCase(); 
	}
	
}
