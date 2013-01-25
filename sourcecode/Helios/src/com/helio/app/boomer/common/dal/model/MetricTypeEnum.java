package com.helio.app.boomer.common.dal.model;


public enum MetricTypeEnum {
	UNKNOWN(""), 
	CASESHANDLED("CASES HANDLED"), 
	ACTUALTARIFF("ACTUAL TARIFF"),
	NORMALIZEDTARIFF("NORMALIZED TARIFF"), 
	COSTALLOCATION("COST ALLOCATION");
    
    private String value;

	MetricTypeEnum(String value) {
        this.value = value;
    }

    // the identifierMethod
    public String toString() {
      return value;
    }

     // the valueOfMethod
     public  static MetricTypeEnum fromString(String value) { 
    	 if (value.equals(CASESHANDLED.toString())) {
    		 return CASESHANDLED;
    	 }
    	 if (value.equals(ACTUALTARIFF.toString())) {
    		 return ACTUALTARIFF;
    	 }
    	 if (value.equals(NORMALIZEDTARIFF.toString())) {
    		 return NORMALIZEDTARIFF;
    	 }
    	 if (value.equals(COSTALLOCATION.toString())) {
    		 return COSTALLOCATION;
    	 }
         return UNKNOWN;
    }
    
    
    
}
