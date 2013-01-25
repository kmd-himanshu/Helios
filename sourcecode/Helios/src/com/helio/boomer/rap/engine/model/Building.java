package com.helio.boomer.rap.engine.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="BUILDING")
public class Building extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = 8148519500960488906L;

	public Building() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_Building_Gen")
	@TableGenerator(name = "BoomerId_Building_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "Building_Gen", allocationSize = 10)
	private int id;

	@Column( name="BUILDINGNAME", length=127, nullable=false)
	private String buildingName;
	
	@Column( name="ABBREVIATION", length=30, nullable=true)
	private String abbreviation;

	@Column( name="SQFT")
	private Integer squareFeet;
	
	@Column( name="ESTIMATE")
	private Boolean estimate;
	
	@Column( name="PERCENTAGELOCSQFT", precision=3)
	private Float percentageLocationSquareFeet;

	@Column( name="ENERGY", precision=3)
	private Float energy;
	
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=BoomerType.class, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="BUILDINGTYPE_id")
	private BoomerType buildingType;
	
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=Location.class, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="LOCATION_id")
	private Location location;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="building")
	private List<BuildingAllocation> buildingAllocations;
	
	public int getId() {
		return id;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBuildingName() {
		return Strings.nullToEmpty(buildingName);
	}
	
	public String getName() {
		return Strings.nullToEmpty(abbreviation).length() > 0 ? abbreviation : Strings.nullToEmpty(buildingName);
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation() {
		return Strings.nullToEmpty(abbreviation);
	}

	public Integer getSquareFeet() {
		return squareFeet;
	}

	public void setSquareFeet(Integer squareFeet) {
		this.squareFeet = squareFeet;
	}

	public Boolean getEstimate() {
		return estimate;
	}

	public void setEstimate(Boolean estimate) {
		this.estimate = estimate;
	}

	public Float getPercentageLocationSquareFeet() {
		return percentageLocationSquareFeet;
	}

	public void setPercentageLocationSquareFeet(Float percentageLocationSquareFeet) {
		this.percentageLocationSquareFeet = percentageLocationSquareFeet;
	}

	public Float getEnergy() {
		return energy;
	}

	public void setEnergy(Float energy) {
		this.energy = energy;
	}

	public BoomerType getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(BoomerType buildingType) {
		this.buildingType = buildingType;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<BuildingAllocation> getBuildingAllocations() {
		return buildingAllocations;
	}
	
	public void addBuildingAllocation(BuildingAllocation buildingAllocation) {
		buildingAllocations.add(buildingAllocation);
	}
	
	public void removeBuildingAllocation(BuildingAllocation buildingAllocation) {
		buildingAllocations.remove(buildingAllocation);
	}
	
	@Override
	public String toString() {
		return buildingName + " <ID="
			+ id
			+ "; "
			+ " >";
	}
	
}
