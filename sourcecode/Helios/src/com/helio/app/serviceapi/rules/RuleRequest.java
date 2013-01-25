package com.helio.app.serviceapi.rules;

import java.sql.Timestamp;
import java.util.List;

import com.helio.app.boomer.common.dal.model.ContextHolder;
import com.helio.app.boomer.common.dal.model.DevicePacket;


public class RuleRequest {
	private Timestamp begTime;
	private Timestamp endTime;
	private DevicePacket begPacket;
	private DevicePacket endPacket;
	private List<List<DevicePacket>> packetLists;
	private ContextHolder context;
	
	
	public Timestamp getBegTime() {
		return begTime;
	}
	public void setBegTime(Timestamp begTime) {
		this.begTime = begTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public DevicePacket getBegPacket() {
		return begPacket;
	}
	public void setBegPacket(DevicePacket begPacket) {
		this.begPacket = begPacket;
	}
	public DevicePacket getEndPacket() {
		return endPacket;
	}
	public void setEndPacket(DevicePacket endPacket) {
		this.endPacket = endPacket;
	}
	public List<List<DevicePacket>> getPacketLists() {
		return packetLists;
	}
	public void setPacketLists(List<List<DevicePacket>> packets) {
		this.packetLists = packets;
	}
	public ContextHolder getContext() {
		return context;
	}
	public void setContext(ContextHolder context) {
		this.context = context;
	}
	
}
