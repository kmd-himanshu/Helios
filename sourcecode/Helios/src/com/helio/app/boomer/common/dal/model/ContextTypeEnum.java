package com.helio.app.boomer.common.dal.model;


public enum ContextTypeEnum {
	UNKNOWN(-1), 
    MONITOR(1), 
    ALLOCATION(2), 
    BUILDING(3),
    LOCATION(4),
    DIVISION(5);
    
    private int value;

	ContextTypeEnum(int value) {
        this.value = value;
    }

    // the identifierMethod
    public int toInt() {
      return value;
    }

     // the valueOfMethod
     public  static ContextTypeEnum fromInt(int value) {    
         switch(value) {
             case 1: return MONITOR;
             case 2: return ALLOCATION;
             case 3: return BUILDING;
             case 4: return LOCATION;
             case 5: return DIVISION;
             default:
                     return UNKNOWN;
         }
    }
    
    
    
}
