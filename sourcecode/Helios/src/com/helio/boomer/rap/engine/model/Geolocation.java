package com.helio.boomer.rap.engine.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="GEOLOCATION")
public class Geolocation extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = -1864932079018375383L;

	public Geolocation() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_Geolocation_Gen")
	@TableGenerator(name = "BoomerId_Geolocation_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "Geolocation_Gen", allocationSize = 10)
	private int id;

	@Column( name="LOCATIONNAME", length=255, nullable=false)
	private String locationName;

	@Column( name="ABBREVIATION", length=30, nullable=true)
	private String abbreviation;

	@Column( name="STREETADD1", length=127)
	private String streetAdd1;

	@Column( name="STREETADD2", length=127)
	private String streetAdd2;

	@Column( name="CITY", length=127)
	private String city;

	@Column( name="STATE", length=10)
	private String state;
	
	@Column( name="PROVINCE", length=50)
	private String province;

	@Column( name="COUNTRY", length=127)
	private String country;

	@Column( name="MAILCODE", length=45)
	private String mailcode;

	@Column( name="LATITUDE", precision=8)
	private Double latitude;

	@Column( name="LONGITUDE", precision=8)
	private Double longitude;

//	@OneToMany(cascade=CascadeType.ALL, mappedBy="manufacturer", fetch=FetchType.LAZY)
//	private List<DeviceModel> deviceModels;

	public int getId() {
		return id;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationName() {
		return Strings.nullToEmpty(locationName);
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

//	public List<DeviceModel> getDeviceModels() {
//		return deviceModels;
//	}
//	
//	public void addDeviceModel(DeviceModel deviceModel) {
//		this.deviceModels.add(deviceModel);
//	}
//	
//	public void removeDeviceModel(DeviceModel deviceModel) {
//		this.deviceModels.remove(deviceModel);
//	}

	public String getStreetAdd1() {
		return streetAdd1;
	}

	public void setStreetAdd1(String streetAdd1) {
		this.streetAdd1 = streetAdd1;
	}

	public String getStreetAdd2() {
		return streetAdd2;
	}

	public void setStreetAdd2(String streetAdd2) {
		this.streetAdd2 = streetAdd2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMailcode() {
		return mailcode;
	}

	public void setMailcode(String mailcode) {
		this.mailcode = mailcode;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return locationName + " <ID="
			+ id
			+ ">";
	}
	
}
