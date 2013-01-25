package com.helio.boomer.rap.utility;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class DataSetUtilities {
	
	public static Map<String, Double> reduceByX(Map<String, Double> sourceMap, Double reductionFactor) {
		Map<String, Double> returnMap = Maps.newHashMap();
		for (Entry<String, Double> entry : sourceMap.entrySet()) {
			returnMap.put(entry.getKey(), entry.getValue() / reductionFactor);
		}
		return returnMap;
	}

	public static Map<String, Double> reduceByHundreds(Map<String, Double> sourceMap) {
		return DataSetUtilities.reduceByX(sourceMap, 100.0);
	}
	
	public static Map<String, Double> reduceByThousands(Map<String, Double> sourceMap) {
		return DataSetUtilities.reduceByX(sourceMap, 1000.0);
	}
	
}
