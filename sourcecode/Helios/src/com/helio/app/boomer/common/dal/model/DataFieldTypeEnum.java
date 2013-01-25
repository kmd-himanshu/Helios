package com.helio.app.boomer.common.dal.model;

public enum DataFieldTypeEnum {

	UNKNOWN(-1), 
    F_STRING(0), 
    F_INT(1), 
    F_FLOAT(2), 
    F_BOOLEAN(3), 
    F_DATE(4), 
    F_DATETIME(5), 
    F_DOUBLE(6);
    
    private int value;

    DataFieldTypeEnum(int value) {
        this.value = value;
    }

    // the identifierMethod
    public int toInt() {
      return value;
    }

     // the valueOfMethod
     public  static DataFieldTypeEnum fromInt(int value) {    
         switch(value) {
             case 0: return F_STRING;
             case 1: return F_INT;
             case 2: return F_FLOAT;
             case 3: return F_BOOLEAN;
             case 4: return F_DATE;
             case 5: return F_DATETIME;
             case 6: return F_DOUBLE;
             default:
                     return UNKNOWN;
         }
    }
}
