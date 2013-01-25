package com.helio.boomer.rap.service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.helio.app.boomer.common.dal.model.DevicePacket;
import com.helio.app.serviceapi.kpi.EnergyCostAllocationKPI;
import com.helio.app.serviceapi.kpi.EnergyCostPerCasesHandledKPI;
import com.helio.app.serviceapi.kpi.EnergyPerCasesHandledKPI;
import com.helio.app.serviceapi.kpi.EnergyPerSquareFootKPI;
import com.helio.app.serviceapi.kpi.HelioKPI;
import com.helio.app.serviceapi.kpi.KWHUsageKPI;

public class GsonHandler {
	
	public static Gson gson;
	
	static {
		gson = new GsonBuilder()
//	    	.registerTypeAdapter(
//	    			HelioKPI.class,
//	    			new HelioKPIDeserializer<HelioKPI>())
	    	.create();
	}
	
	public static KWHUsageKPI getKWHUsageKPIFromJson(String json) {
		KWHUsageKPI kpi = gson.fromJson(json, KWHUsageKPI.class);
		return kpi;
	}
	
	public static Collection<DevicePacket> getDevicePacketCollectionFromJson(String json) {
		Type collectionType = new TypeToken<Collection<DevicePacket>>(){}.getType();
		Collection<DevicePacket> packets = gson.fromJson(json, collectionType);
		return packets;
	}
	
	public static List<KWHUsageKPI> getKWHUsageKPIListFromJson(String json) {
		Type collectionType = new TypeToken<List<KWHUsageKPI>>(){}.getType();
		List<KWHUsageKPI> slices = gson.fromJson(json, collectionType);
		return slices;
	}
	
	@SuppressWarnings("unchecked")
	public static List<HelioKPI>[] getKWHUsageKPIListArrayFromJson(String json) {
		Type collectionType = new TypeToken<List<List<KWHUsageKPI>>>(){}.getType();
		List<List<HelioKPI>> listArray = gson.fromJson(json, collectionType);
		List<HelioKPI>[] returnListArray = new List[listArray.size()];
		for (int i=0; i<listArray.size(); i++) {
			returnListArray[i] = listArray.get(i);
		}
		return returnListArray;
	}
	
//	public static KwhDateRangeSliceIntervalsKPI getKwhDateRangeSliceIntervalsKPI(String json) {
//		KwhDateRangeSliceIntervalsKPI kpi = gson.fromJson(json, KwhDateRangeSliceIntervalsKPI.class);
//		return kpi;
//	}

	public static EnergyPerCasesHandledKPI getEnergyPerCasesHandledKPIFromJson(String json) {
		EnergyPerCasesHandledKPI kpi = gson.fromJson(json, EnergyPerCasesHandledKPI.class);
		return kpi;
	}
	
	public static EnergyCostPerCasesHandledKPI getEnergyCostPerCasesHandledKPIFromJson(String json) {
		EnergyCostPerCasesHandledKPI kpi = gson.fromJson(json, EnergyCostPerCasesHandledKPI.class);
		return kpi;
	}
	
	public static EnergyCostAllocationKPI getEnergyCostAllocationKPIFromJson(String json) {
		EnergyCostAllocationKPI kpi = gson.fromJson(json, EnergyCostAllocationKPI.class);
		return kpi;
	}
	
	public static EnergyPerSquareFootKPI getEnergyPerSquareFootKPIFromJson(String json) {
		EnergyPerSquareFootKPI kpi = gson.fromJson(json, EnergyPerSquareFootKPI.class);
		return kpi;
	}
	
}