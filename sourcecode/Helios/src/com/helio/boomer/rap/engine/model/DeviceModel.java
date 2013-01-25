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

import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="DEVICEMODEL")
public class DeviceModel extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = 4819929528366419577L;

	public DeviceModel() {
		super();
	}

	public DeviceModel(String modelName) {
		this();
		this.setModelName(modelName);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_DeviceModel_Gen")
	@TableGenerator(name = "BoomerId_DeviceModel_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DeviceModel_Gen", initialValue = 100, allocationSize = 10)
	private int id;

	@Column( name="MODELNAME", length=255, nullable=false)
	private String modelName;

	@Column( name="UTILITYGRADE")
	private Boolean utilityGrade;

	@Column( name="DEVICEUSE", length=25)
	private String deviceUse;

	@ManyToOne()
	@JoinColumn(name="MANUFACTURER_id")
	private Manufacturer manufacturer;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="deviceModel", fetch=FetchType.LAZY)
	private List<DeviceMonitor> deviceMonitors;
	
	public int getId() {
		return id;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public Boolean isUtilityGrade() {
		return utilityGrade;
	}
	
	public Boolean getUtilityGrade() {
		return isUtilityGrade();
	}
	
	public void setUtilityGrade(Boolean utilityGrade) {
		this.utilityGrade = utilityGrade;
	}
	
	public String getDeviceUse() {
		return deviceUse;
	}
	
	public void setDeviceUse(String deviceUse) {
		this.deviceUse = deviceUse;
	}
	
	public Manufacturer getManufacturer() {
		return manufacturer;
	}
	
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<DeviceMonitor> getDeviceMonitors() {
		return deviceMonitors;
	}
	
	public void addDeviceMonitor(DeviceMonitor deviceMonitor) {
		this.deviceMonitors.add(deviceMonitor);
	}
	
	public void removeDeviceModel(DeviceMonitor deviceMonitor) {
		this.deviceMonitors.remove(deviceMonitor);
	}
	
}
