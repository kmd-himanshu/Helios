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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="DIVISION")
public class Division extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = -6443931837768039861L;

	public Division() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_Division_Gen")
	@TableGenerator(name = "BoomerId_Division_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "Division_Gen", allocationSize = 10)
	private int id;

	@Column( name="DIVISIONNAME", length=127, nullable=false)
	private String divisionName;

	@Column( name="ABBREVIATION", length=30, nullable=true)
	private String abbreviation;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="division", fetch=FetchType.LAZY)
	private List<Location> locations;

	public int getId() {
		return id;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getDivisionName() {
		return Strings.nullToEmpty(divisionName);
	}

	public String getName() {
		return Strings.nullToEmpty(abbreviation).length() > 0 ? abbreviation : Strings.nullToEmpty(divisionName);
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation() {
		return Strings.nullToEmpty(abbreviation);
	}

	public List<Location> getLocations() {
		return locations;
	}
	
	public void addLocation(Location location) {
		this.locations.add(location);
	}
	
	public void removeLocation(Location location) {
		this.locations.remove(location);
	}

	@Override
	public String toString() {
		return divisionName + " <ID="
			+ id
			+ "; "
			+ locations.size()
			+ " locations>";
	}
	
}
