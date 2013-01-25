package com.helio.app.boomer.common.dal.model;

import java.util.List;

public class Building {

	private long id;
	private String name;
	private String abbreviation;
	private int squareFeet;
	private int estimate; 
	private float percentSquareFeet;
	private float energy;
	private long buildingType;
	List<BoomerMetric> metrics;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public int getSquareFeet() {
		return squareFeet;
	}
	public void setSquareFeet(int squareFeet) {
		this.squareFeet = squareFeet;
	}
	public int getEstimate() {
		return estimate;
	}
	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}
	public float getPercentSquareFeet() {
		return percentSquareFeet;
	}
	public void setPercentSquareFeet(float percentSquareFeet) {
		this.percentSquareFeet = percentSquareFeet;
	}
	public float getEnergy() {
		return energy;
	}
	public void setEnergy(float energy) {
		this.energy = energy;
	}
	public long getBuildingType() {
		return buildingType;
	}
	public void setBuildingType(long buildingType) {
		this.buildingType = buildingType;
	}
	public List<BoomerMetric> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<BoomerMetric> metrics) {
		this.metrics = metrics;
	}
	
}
