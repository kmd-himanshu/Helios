package com.helio.boomer.rap.engine.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="DEVICEMONITOR")
public class DeviceMonitor extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = 4660651908882585956L;

	public DeviceMonitor() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_DeviceMonitor_Gen")
	@TableGenerator(name = "BoomerId_DeviceMonitor_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DeviceMonitor_Gen", initialValue = 100, allocationSize = 10)
	private int id;

	@Column( name="MONITORNAME", length=255, nullable=false)
	private String monitorName;

	@Column( name="MONITORNOTES", length=255 )
	private String monitorNotes;

	@Column( name="SERIALNUMBER", length=255)
	private String serialNumber;

	@Column( name="PERSISTMONITOR")
	private Boolean persistMonitor;
	
	@Column( name="PULSECONSTANT")
	private Float pulseConstant;

	@ManyToOne()
	@JoinColumn(name="DEVICEMODEL_id")
	private DeviceModel deviceModel;
	
	public int getId() {
		return id;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public String getMonitorName() {
		return Strings.nullToEmpty(monitorName);
	}

	public void setMonitorNotes( String monitorNotes ) {
		this.monitorNotes = monitorNotes;
	}

	public String getMonitorNotes() {
		return Strings.nullToEmpty(monitorNotes);
	}

	public void setSerialNumber( String serialNumber ) {
		this.serialNumber = serialNumber;
	}

	public String getSerialNumber() {
		return Strings.nullToEmpty(serialNumber);
	}

	public void setPersistMonitor( Boolean persistMonitor ) {
		this.persistMonitor = persistMonitor;
	}

	public Boolean isPersistMonitor() {
		return persistMonitor;
	}
	
	public Boolean getPersistMonitor() {
		return isPersistMonitor();
	}

	public void setPulseConstant(Float pulseConstant) {
		this.pulseConstant = pulseConstant;
	}
	
	public Float getPulseConstant() {
		return pulseConstant;
	}
	
	public DeviceModel getDeviceModel() {
		return deviceModel;
	}
	
	public void setDeviceModel(DeviceModel deviceModel) {
		this.deviceModel = deviceModel;
	}

}
