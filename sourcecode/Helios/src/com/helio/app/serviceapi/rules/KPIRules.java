package com.helio.app.serviceapi.rules;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.helio.app.boomer.common.dal.model.DeviceModel;
import com.helio.app.boomer.common.dal.model.DevicePacket;
import com.helio.app.boomer.common.dal.model.PacketValue;
import com.helio.app.serviceapi.kpi.HelioInvalidKPIException;
import com.helio.app.serviceapi.kpi.StandardDeviationKPI;

public class KPIRules {

	private static KPIRules instance = null;
	
	private KPIRules() {
		super();
	}
	
	public synchronized static KPIRules getInstance() 
	{
		if (instance == null) {
			instance = new KPIRules();
		}
		return instance;
	}
	
	/**
	 * This method contains the knowledge of how to calculate KwH KPI for 
	 * pulse and induction meters. 
	 * @param pRequest contains all objects necessary to calculate KPI for the 
	 * requested meter. 
	 * @return The KPI representing KwH for the passed request. 
	 */
	public Float calculateKWH(RuleRequest pRequest)  
		throws RulesServiceException
	{
		DevicePacket begPacket = pRequest.getBegPacket();
		DevicePacket endPacket = pRequest.getEndPacket();
		
		return getKWHFromPackets(begPacket, endPacket);
	}
	
	/**
	 * Gets the kWh from two packets.
	 */
	private float getKWHFromPackets(DevicePacket pBegPacket,DevicePacket pEndPacket) 
		throws RulesServiceException
	{
		float kWh = 0f;
		DeviceModel model = pBegPacket.getDeviceMonitor().getDeviceModel();
		if (isPulseMeter(model)) {
			float begPulse = getPulseReadingFloat(pBegPacket);
			float endPulse = getPulseReadingFloat(pEndPacket);
			float pulseConstant = pBegPacket.getDeviceMonitor().getPulseConstant();
			if (begPulse > -1 && endPulse > 0 && pulseConstant > 0) {
				kWh = (endPulse - begPulse) * pulseConstant;
			}
			else {
				if (begPulse < 0) {
					/*
					 *  TODO: This is a work-around 
					 */
//					throw new RulesServiceException("Begining Pulse is invalid for packet " + pBegPacket.getId());
				}
				else if (endPulse < 1) {
					/*
					 *  TODO: This is a work-around 
					 */
//					throw new RulesServiceException("Ending Pulse is invalid for packet " + pEndPacket.getId());
				}
				else if (pulseConstant < 1) {
					throw new RulesServiceException("Pulse constant is invalid: " + pulseConstant);
				}
			}
		}
		return (kWh < 0 ? 0 : kWh); // Don't return negative kwh
	}

	
	/**
	 * This is based on Obvious AcquiSuite which uses float.
	 * May need additional methods if other pulse meters use non-floats.
	 * @return -1f if value not found. 
	 */
	private float getPulseReadingFloat(DevicePacket pPacket) {
		float returnVal = -1.0f;
		DeviceModel model = pPacket.getDeviceMonitor().getDeviceModel();
		String pulseMeterField = getPulseMeterReadingField(model);
		/*
		 * If we have a proxy role, then get the field containing the other meter
		 */
		if (pPacket.getDeviceMonitor().isProxyRole()) {
			pulseMeterField = getPulseMeterProxyReadingField(model);
		}
		Iterator<PacketValue> itr = pPacket.getPacketValues().iterator();
		while (itr.hasNext()) {
			PacketValue packetValue = itr.next();
			if (packetValue.getName().equals(pulseMeterField)) {
				switch (packetValue.getFieldType()) {
				case F_FLOAT: 	returnVal = Float.valueOf(packetValue.getStrValue()); break;
				case F_INT:		returnVal = Float.valueOf(packetValue.getStrValue()); break;
				case F_DOUBLE:	returnVal = Float.valueOf(packetValue.getStrValue()); break;
				default: 
					return -1f;
				}
				break; // break from outer loop
			}
		}
		return returnVal;
	}
		
	private boolean isPulseMeter(DeviceModel pModel) {
		for (int i=0; i<RuleConstants.pulseMeterModels.length;i++) {
			if (pModel.getModelName().equals(RuleConstants.pulseMeterModels[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Iterates through two dimensional array. 
	 * First position of inner array is model name, second position is the field name
	 * @param pModel
	 * @return
	 */
	private String getPulseMeterReadingField(DeviceModel pModel) {
		for (int i=0; i<RuleConstants.pulseMeterFields.length;i++) {
			if (pModel.getModelName().equals(RuleConstants.pulseMeterFields[i][0])) {
				return RuleConstants.pulseMeterFields[i][1];
			}
		}
		return null;
	}
	
	/**
	 * Iterates through two dimensional array. 
	 * First position of inner array is model name, second position is the field name
	 * @param pModel
	 * @return
	 */
	private String getPulseMeterProxyReadingField(DeviceModel pModel) {
		for (int i=0; i<RuleConstants.pulseMeterProxyFields.length;i++) {
			if (pModel.getModelName().equals(RuleConstants.pulseMeterProxyFields[i][0])) {
				return RuleConstants.pulseMeterProxyFields[i][1];
			}
		}
		return null;
	}
	
	/**
	 * This method calculates a standard deviation KPI for a collection of packets
	 * @param pRequest contains a list of packets and a context necessary to calculate standard deviation KPI
	 * @return The KPI representing standard deviation for a collection of packets. 
	 */
	public StandardDeviationKPI calculateStandardDeviation(RuleRequest pRequest)  
		throws RulesServiceException
	{
		List<List<DevicePacket>> packetLists = pRequest.getPacketLists();
		
		Iterator <List<DevicePacket>> packetListsItr = packetLists.iterator();
		
		float priorKWh = 0f;
		float kwhSum = 0f;
		float kwhMean = 0f;
		float lowValue = 9999999999f;
		float highValue = 0f;
		Timestamp  firstTime = null;
		Timestamp  lastTime = null;
		List<Float> kwhList = new ArrayList<Float>();
		
		/*
		 * We need an array of pulse pair for aggregation
		 */
		class PulsePair {
			long monitorId=0;
			float pulseConstant=0f;
			float begPulse=0f;
			float endPulse=0f;
			Timestamp begPulseTime;
			Timestamp endPulseTime;
			void loadBegPulse (DevicePacket pBegPacket) throws RulesServiceException {
				monitorId = pBegPacket.getMonitorId();
				if (isPulseMeter(pBegPacket.getDeviceMonitor().getDeviceModel())) {
					begPulse = getPulseReadingFloat(pBegPacket);
					pulseConstant = pBegPacket.getDeviceMonitor().getPulseConstant();
					if (pulseConstant < 0) {
						throw new RulesServiceException("Pulse constant " + pulseConstant 
													+ " is invalid for monitor " + pBegPacket.getDeviceMonitor().getMonitorName() 
													+ "/" +  pBegPacket.getDeviceMonitor().getSerialNumber());
					}
				}
				begPulseTime = pBegPacket.getPacketDate();
				endPulse = 0f;
				endPulseTime = null;
			}
			void loadEndPulse (DevicePacket pEndPacket) {
				if (isPulseMeter(pEndPacket.getDeviceMonitor().getDeviceModel())) {
					endPulse = getPulseReadingFloat(pEndPacket);
				}
				endPulseTime = pEndPacket.getPacketDate();
			}
			float getKWH(){
				float kWh = 0f;
				if (endPulseTime==null) return kWh;
				if (begPulse > -1 && endPulse > 0 && pulseConstant > 0) {
					kWh = (endPulse - begPulse) * pulseConstant;
					return kWh;
				}
				else if (begPulse < 0) {
						/*
						 *  TODO: This is a work-around 
						 */
//						throw new RulesServiceException("Beginning Pulse is invalid for packet " + begPacket.getId());
				}
				else if (endPulse < 1) {
						/*
						 *  TODO: This is a work-around 
						 */
//						throw new RulesServiceException("Ending Pulse is invalid for packet " + endPacket.getId());
				}
				return 0f;
			}
			void init() {
				begPulse=0f;
				endPulse=0f;
				begPulseTime=null;
				endPulseTime=null;
			}
		}
		PulsePair[] pulsePairs = new PulsePair[100];
		
		
		while (packetListsItr.hasNext()) {
			List<DevicePacket> packetList = packetListsItr.next();
			/*
			 * First make sure there are enough packets to calculate a standard deviation
			 */
			if (packetList.size() < 1) {
				throw new RulesServiceException("Received empty packet list while determining standard deviation.");
			}
			if (packetList.size() < 3) {
				throw new RulesServiceException("Received less than three packets while determining standard deviation; at least three required.");
			}
			
			
			/*
			 * First and last packet times for current list
			 */
			DevicePacket begPacket = packetList.get(0);
			firstTime = packetList.get(0).getPacketDate();
			lastTime  = packetList.get(packetList.size()-1).getPacketDate();
			/*
			 * For each pair in the list, calculate kwh
			 */
			if (pulsePairs[0] == null) {
				pulsePairs[0] = new PulsePair();
			}
			pulsePairs[0].begPulseTime = packetList.get(0).getPacketDate(); 
			int nextBegIndex = 0;
			
			for (int i=0; i<packetList.size()-1; i++) {
				float kWh = 0f;
				if (isPulseMeter(begPacket.getDeviceMonitor().getDeviceModel())) { //TODO maybe change
					/*
					 * Build an array of pulse pairs for the same beg and end time so that we can sum the KWH
					 */
					for (;i<packetList.size(); i++) { 
						DevicePacket nextPacket = packetList.get(i);
						int nextPacketCompareResult = nextPacket.getPacketDate().compareTo(pulsePairs[0].begPulseTime);
						if (nextPacketCompareResult == 0) {
							for (int j=0; j<pulsePairs.length; j++) {
								if (pulsePairs[j] == null) {
									pulsePairs[j] = new PulsePair();
								}
								if (pulsePairs[j].monitorId==nextPacket.getMonitorId() || pulsePairs[j].monitorId==0) {
									pulsePairs[j].loadBegPulse(nextPacket);
									break; // get out after adding to or updating array
								}
							}
						}
						else { // must be higher date based on SQL order statement
							if (pulsePairs[0].endPulseTime==null ) {
								pulsePairs[0].endPulseTime=nextPacket.getPacketDate(); //It may not be for same monitor, but we just need the date
								nextBegIndex = i;
							}
							if (nextPacket.getPacketDate().compareTo(pulsePairs[0].endPulseTime) != 0) {
								break; // passed last end pulse so get out of loop
							}
							for (int j=0; j<pulsePairs.length; j++) {
								if (pulsePairs[j] == null) {
									break; // means we have an end pulse without a beg pulse for the monitor, just skip
								}
								if (pulsePairs[j].monitorId==nextPacket.getMonitorId() ) {
									pulsePairs[j].loadEndPulse(nextPacket);
								}
							}
						}
					}
					/*
					 * Now sum the kWh for all meters of the same beg and end date.
					 */
					for (int j=0; j<pulsePairs.length; j++) {
						if (pulsePairs[j] == null) break;
						kWh = kWh + pulsePairs[j].getKWH();
					}
					/*
					 * If we derived an energy amount then check
					 * for min and max, add to sum and array of values
					 */
					if (kWh > 0) {
						/*
						 * TODO: Fix data
						 * This is a work around because DAQ stores corrupted data 
						 * due to a thread bug. If the kWh is out of proportion to the 
						 * prior reading, just use the prior reading.
						 */
						if (priorKWh>0 && ((kWh/priorKWh) > 100)) {
							kWh = priorKWh;
						}
						priorKWh = kWh;
						/*
						 * End of work around
						 */
						
						if( kWh < lowValue) {
							lowValue = kWh;
						}
						if( kWh > highValue) {
							highValue = kWh;
						}
						kwhList.add(kWh);
						kwhSum = kwhSum + kWh;
					}
					
					/*
					 * Re-init the pulse pair array
					 */
					for (int j=0; j<pulsePairs.length;j++) {
						if (pulsePairs[j]==null) {
							break; // all done
						}
						pulsePairs[j].init();
					}
					/*
					 * If there are more packets to look at set the index
					 * and begin packet time to the last end packet time
					 */
					if (i<packetList.size()-1) {
						i = nextBegIndex;
						pulsePairs[0].begPulseTime = packetList.get(i).getPacketDate();
						i--; // backup one in the index since we will increment by one by the for loop
					}
				}
			}
			
		}
		
		
		/*
		 * Now calculate Standard deviation using the values from 
		 * the preceding loops.
		 */
		kwhMean = kwhSum / kwhList.size();
		float sumMeanDiffSquared = 0f;
		Iterator<Float> kwhItr = kwhList.iterator();
		while (kwhItr.hasNext()) {
			sumMeanDiffSquared = sumMeanDiffSquared + (float)Math.pow(kwhItr.next() - kwhMean,2); 
		}
		float kwhStdDev = (float) (Math.sqrt((double)sumMeanDiffSquared) / Math.sqrt((double)kwhList.size()-1));

		/*
		 * Now fill the KPI with fields needed for standard deviation KPI
		 */
		StandardDeviationKPI sdv = null;
		try {
			 sdv = new StandardDeviationKPI(pRequest.getContext(), 
					kwhSum, 
					firstTime,
					lastTime,
					lowValue,
					highValue,
					kwhMean,
					kwhStdDev);
			return sdv;
		}
		catch (HelioInvalidKPIException ex) {
			throw new RulesServiceException(ex);
		}
	}

	
}
