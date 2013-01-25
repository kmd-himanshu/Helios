package com.helio.app.boomer.common.dal.model;


public enum DeviceUseEnum {
	UNKNOWN(-1), 
    electrical(0), 
    water(1), 
    gas(2);
    
    private int value;

	DeviceUseEnum(int value) {
        this.value = value;
    }

    // the identifierMethod
    public int toInt() {
      return value;
    }

     // the valueOfMethod
     public  static DeviceUseEnum fromInt(int value) {    
         switch(value) {
             case 0: return electrical;
             case 1: return water;
             case 2: return gas;
             default:
                     return UNKNOWN;
         }
    }
    
    
    
}
