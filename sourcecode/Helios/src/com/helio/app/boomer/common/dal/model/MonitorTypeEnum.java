package com.helio.app.boomer.common.dal.model;


public enum MonitorTypeEnum {
	UNKNOWN(-1), 
    PULSEMETER(1), 
    PULSEMETERPROXY(2),
    INDUCTION(3); 
    
    private int value;

	MonitorTypeEnum(int value) {
        this.value = value;
    }

    // the identifierMethod
    public int toInt() {
      return value;
    }

     // the valueOfMethod
     public  static MonitorTypeEnum fromInt(int value) {    
         switch(value) {
             case 1: return PULSEMETER;
             case 2: return PULSEMETERPROXY;
             case 3: return INDUCTION;
             default:
                     return UNKNOWN;
         }
    }
    
    
    
}
