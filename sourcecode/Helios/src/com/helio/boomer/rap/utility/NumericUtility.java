package com.helio.boomer.rap.utility;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

public class NumericUtility {
	
	public static Random rndNumber = new Random((new Date()).getTime());
	public static int modifyPerc = 30;

	public static BigDecimal getPrecDecimal(Double doub, int precision) {
		BigDecimal bd = new BigDecimal(Double.toString(doub));
		bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
		return bd;

	}
	
	public static Double getPrecDouble(Double doub, int precision) {
		BigDecimal bd = new BigDecimal(Double.toString(doub));
		bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	public static Double modifyByRandom(Double sourceDouble) {
		return (sourceDouble - (sourceDouble * (rndNumber.nextInt(modifyPerc)  * 0.01))); 
	}
	
}
