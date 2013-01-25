package com.helio.boomer.rap.engine.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="MANUFACTURER")
public class Manufacturer extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = 2477711439348208401L;

	public Manufacturer() {
		super();
		this.setAddress1("");
		this.setAddress2("");
		this.setCity("");
		this.setAddState("");
		this.setZip("");
		this.setPhone("(   )   -");
		this.setContactName("");
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_Manufacturer_Gen")
	@TableGenerator(name = "BoomerId_Manufacturer_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "Manufacturer_Gen", allocationSize = 10)
	private int id;

	@Column( name="MANUFACTURERNAME", length=255, nullable=false)
	private String manufacturerName;

	@Column( name="ADDRESS1", length=255 )
	private String address1;

	@Column( name="ADDRESS2", length=255 )
	private String address2;

	@Column( name="CITY", length=100)
	private String city;

	@Column( name="ADDSTATE", length=2)
	private String addState;

	@Column( name="ZIP", length=10 )
	private String zip;

	@Column( name="PHONE", length=14 )
	private String phone;

	@Column( name="CONTACTNAME", length=100 )
	private String contactName;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="manufacturer", fetch=FetchType.LAZY)
	private List<DeviceModel> deviceModels;

	public int getId() {
		return id;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getManufacturerName() {
		return Strings.nullToEmpty(manufacturerName);
	}

	public void setAddress1( String address1 ) {
		this.address1 = address1;
	}

	public String getAddress1() {
		return Strings.nullToEmpty(address1);
	}

	public void setAddress2( String address2 ) {
		this.address2 = address2;
	}

	public String getAddress2() {
		return Strings.nullToEmpty(address2);
	}

	public void setCity( String city ) {
		this.city = city;
	}

	public String getCity() {
		return Strings.nullToEmpty(city);
	}

	public void setAddState( String addState ) {
		this.addState = addState;
	}

	public String getAddState() {
		return Strings.nullToEmpty(addState);
	}

	public void setZip( String zip ) {
		this.zip = zip;
	}

	public String getZip() {
		return Strings.nullToEmpty(zip);
	}

	public void setPhone( String phone ) {
		this.phone = phone;
	}

	public String getPhone() {
		return Strings.nullToEmpty(phone);
	}

	public void setContactName(String contactName ) {
		this.contactName = contactName;
	}

	public String getContactName() {
		return Strings.nullToEmpty(contactName);
	}

	public List<DeviceModel> getDeviceModels() {
		return deviceModels;
	}
	
	public void addDeviceModel(DeviceModel deviceModel) {
		this.deviceModels.add(deviceModel);
	}
	
	public void removeDeviceModel(DeviceModel deviceModel) {
		this.deviceModels.remove(deviceModel);
	}

	@Override
	public String toString() {
		return manufacturerName + " <"
			+ address1 + ", "
			+ address2 + ", "
			+ city + ", "
			+ addState + ", "
			+ zip 
			+ "; Contact: " + contactName
			+ "; Phone: " + phone
			+ ">";
	}
	
}
