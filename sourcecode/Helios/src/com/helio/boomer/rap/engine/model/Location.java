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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="LOCATION")
public class Location extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = -4535627592969320512L;

	public Location() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_Location_Gen")
	@TableGenerator(name = "BoomerId_Location_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "Location_Gen", allocationSize = 10)
	private int id;

	@Column( name="LOCATIONNAME", length=127, nullable=false)
	private String locationName;
	
	@Column( name="ABBREVIATION", length=30, nullable=true)
	private String abbreviation;

	@Column( name="SQFT")
	private Integer squareFeet;
	
	@Column( name="ESTIMATE")
	private Boolean estimate;
	
	@Column( name="ENERGY", precision=3)
	private Float energy;

	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="GEOLOCATION_id")
	private Geolocation geolocation;
	
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=Division.class, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="DIVISION_id")
	private Division division;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="location", fetch=FetchType.LAZY)
	private List<Building> buildings;
	
	public int getId() {
		return id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getName() {
		return Strings.nullToEmpty(abbreviation).length() > 0 ? abbreviation : Strings.nullToEmpty(locationName);
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

	public Float getEnergy() {
		return energy;
	}

	public void setEnergy(Float energy) {
		this.energy = energy;
	}

	public Geolocation getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(Geolocation geolocation) {
		this.geolocation = geolocation;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public List<Building> getBuildings() {
		return buildings;
	}
	
	public void addBuilding(Building building) {
		this.buildings.add(building);
	}
	
	public void removeBuilding(Building building) {
		this.buildings.remove(building);
	}


	@Override
	public String toString() {
		return locationName + " <ID="
			+ id
			+ ">";
	}
	
}
