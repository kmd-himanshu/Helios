package com.helio.app.serviceapi.rules;

public class RuleConstants {
	
	/**
	 * Abstraction of model names of the pulse meters
	 */
	public static String[] pulseMeterModels = new String[] { "A8812","R9120" };
	
	/**
	 * Array of field names of the pulse constants by meter 
	 */
	public static String[][] pulseMeterFields = new String[][] { {"A8812","input1A"}, {"R9120","Pulse1Consumption"}};
	
	public static String[][] pulseMeterProxyFields = new String[][] { {"A8812","input2A"},{"R9120","Pulse2Consumption"} };
	

}
