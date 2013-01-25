package com.helio.boomer.rap.engine.model.birt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class KWHDataMock {
	
	ImmutableList<Double> kWhList = new ImmutableList.Builder<Double>()
			.add(20000.0).add(25000.0).add(24000.0).add(19000.0).add(18000.0)
			.add(20000.0).add(21000.0).add(23000.0).add(24000.0).add(26000.0)
			.add(28000.0).add(33000.0).add(31000.0).add(32000.0).add(40000.0)
			.add(38000.0).add(37000.0).add(35000.0).add(43000.0).add(38000.0)
			.add(35000.0).add(33000.0).add(38000.0).add(31000.0).add(33000.0)
			.add(30000.0).add(31000.0).add(28000.0).add(26000.0).add(27000.0)
			.add(26000.0)
			.build();

	ImmutableList<Double> meanKWhList;
	ImmutableList<Double> maxKWhList;
	ImmutableList<Double> minKWhList;
	ImmutableList<Double> stDevKWhList;
	
	public KWHDataMock() {
		
		kWhList = new ImmutableList.Builder<Double>()
			.add(20000.0).add(25000.0).add(24000.0).add(19000.0).add(18000.0)
			.add(20000.0).add(21000.0).add(23000.0).add(24000.0).add(26000.0)
			.add(28000.0).add(33000.0).add(31000.0).add(32000.0).add(40000.0)
			.add(38000.0).add(37000.0).add(35000.0).add(43000.0).add(38000.0)
			.add(35000.0).add(33000.0).add(38000.0).add(31000.0).add(33000.0)
			.add(30000.0).add(31000.0).add(28000.0).add(26000.0).add(27000.0)
			.add(26000.0)
			.build();

		maxKWhList = new ImmutableList.Builder<Double>()
			.add(24000.0).add(28000.0).add(26000.0).add(23000.0).add(21000.0)
			.add(28000.0).add(22000.0).add(27000.0).add(30000.0).add(31000.0)
			.add(35000.0).add(37000.0).add(33000.0).add(38000.0).add(45000.0)
			.add(40000.0).add(41000.0).add(39000.0).add(44000.0).add(39000.0)
			.add(38000.0).add(36000.0).add(39000.0).add(36000.0).add(38000.0)
			.add(32000.0).add(33000.0).add(30000.0).add(28000.0).add(29000.0)
			.add(27000.0)
			.build();

		meanKWhList = new ImmutableList.Builder<Double>()
			.add(22000.0).add(27000.0).add(22000.0).add(20000.0).add(16000.0)
			.add(22000.0).add(19500.0).add(22000.0).add(27000.0).add(19000.0)
			.add(30000.0).add(32000.0).add(32000.0).add(32000.0).add(30000.0)
			.add(32000.0).add(33000.0).add(31000.0).add(40000.0).add(35000.0)
			.add(34000.0).add(32500.0).add(36000.0).add(33000.0).add(34000.0)
			.add(29500.0).add(32000.0).add(25000.0).add(24000.0).add(27000.0)
			.add(24000.0)
			.build();

		minKWhList = new ImmutableList.Builder<Double>()
			.add(18000.0).add(20000.0).add(21000.0).add(17000.0).add(15000.0)
			.add(18000.0).add(19000.0).add(20000.0).add(21000.0).add(22000.0)
			.add(25000.0).add(31000.0).add(30000.0).add(31000.0).add(26000.0)
			.add(25000.0).add(27000.0).add(33000.0).add(38000.0).add(32000.0)
			.add(30000.0).add(29000.0).add(33000.0).add(29000.0).add(30000.0)
			.add(27000.0).add(29000.0).add(20000.0).add(21000.0).add(25000.0)
			.add(23000.0)
			.build();
		
		stDevKWhList = new ImmutableList.Builder<Double>()
			.add(1000.0).add(2000.0).add(1500.0).add(1600.0).add(2100.0)
			.add(1000.0).add(1900.0).add(1200.0).add(800.0).add(2200.0)
			.add(2500.0).add(3100.0).add(3000.0).add(3100.0).add(2600.0)
			.add(2000.0).add(2700.0).add(3300.0).add(3800.0).add(3200.0)
			.add(3000.0).add(2900.0).add(1300.0).add(2000.0).add(300.0)
			.add(1700.0).add(900.0).add(1000.0).add(2100.0).add(1500.0)
			.add(300.0)
			.build();
		
	}
	
	public List<KWHData> getKWHValues(String company) {
		// Ignore the company and always return the data
		// A real implementation would of course use the company string
		List<KWHData> history = new ArrayList<KWHData>();
		// We fake the values, we will return fake value for 01.01.2009 -
		// 31.01.2009
		for (int i = 0; i < 31; i++) {
			KWHData data = new KWHData();

			Calendar day = Calendar.getInstance();
			day.set(Calendar.HOUR, 0);
			day.set(Calendar.MINUTE, 0);
			day.set(Calendar.SECOND, 0);
			day.set(Calendar.MILLISECOND, 0);
			day.set(Calendar.YEAR, 2011);
			day.set(Calendar.MONTH, 0);
			day.set(Calendar.DAY_OF_MONTH, i);
			data.setDay(day.getTime());

			// Loading up data from lists set in the constructor.
			data.setkWh(kWhList.get(i));
			data.setMean(meanKWhList.get(i));
			data.setPeriodMax(maxKWhList.get(i));
			data.setPeriodMin(minKWhList.get(i));

			 history.add(data);
		}
		return history;
	}

	public static void main(String[] args) {
		KWHDataMock mock = new KWHDataMock();
		List<KWHData> data = mock.getKWHValues("Lawrence");
		System.out.println(data.get(0).getkWh());
		System.out.println(data.get(0).getMean());
		System.out.println(data.get(0).getMeanOverSD());
		System.out.println(data.get(0).getMeanUnderSD());
	}
	
}
                                                                       