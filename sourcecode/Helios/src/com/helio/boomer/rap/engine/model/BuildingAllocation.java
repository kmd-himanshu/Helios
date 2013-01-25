package com.helio.boomer.rap.engine.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="BUILDINGALLOCATION")
public class BuildingAllocation extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = 5622594736297269935L;

	public BuildingAllocation() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_BuildingAllocation_Gen")
	@TableGenerator(name = "BoomerId_BuildingAllocation_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "BuildingAllocation_Gen", allocationSize = 10)
	private int id;

	@Column( name="BLDALLOCNAME", length=127, nullable=false)
	private String buildingAllocationName;
	
	@Column( name="ABBREVIATION", length=30, nullable=true)
	private String abbreviation;

	@Column( name="ESTSQFT")
	private Integer estimatedSquareFeet;
	
	@Column( name="PERCENTAGE")
	private Float percentage;
	
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=AllocationType.class, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="ALLOCATIONTYPE_id")
	private AllocationType allocationType;
	
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=BoomerType.class, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="STORAGETYPE_id")
	private BoomerType storageType;
	
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=Building.class, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="BUILDING_id")
	private Building building;
	
	public int getId() {
		return id;
	}

	public void setBuildingAllocationName(String buildingAllocationName) {
		this.buildingAllocationName = buildingAllocationName;
	}
	
	public String getBuildingAllocationName() {
		return buildingAllocationName;
	}
	
	public String getName() {
		return Strings.nullToEmpty(abbreviation).length() > 0 ? abbreviation : Strings.nullToEmpty(buildingAllocationName);
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation() {
		return Strings.nullToEmpty(abbreviation);
	}
	
	public void setEstimatedSquareFeet(Integer estimatedSquareFeet) {
		this.estimatedSquareFeet = estimatedSquareFeet;
	}
	
	public Integer getEstimatedSquareFeet() {
		return estimatedSquareFeet;
	}
	
	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}
	
	public Float getPercentage() {
		return percentage;
	}

	public void setAllocationType(AllocationType allocationType) {
		this.allocationType = allocationType;
	}
	
	public AllocationType getAllocationType() {
		return allocationType;
	}

	public void setStorageType(BoomerType storageType) {
		this.storageType = storageType;
	}
	
	public BoomerType getStorageType() {
		return storageType;
	}
	
	public void setBuilding(Building building) {
		this.building = building;
	}
	
	public Building getBuilding() {
		return building;
	}

	@Override
	public String toString() {
		return buildingAllocationName + " <ID="
			+ id
			+ "; "
			+ " >";
	}
	
}
