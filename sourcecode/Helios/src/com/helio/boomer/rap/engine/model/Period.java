package com.helio.boomer.rap.engine.model;

import java.io.Serializable;
import java.sql.Date;

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
import javax.persistence.Transient;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="PERIOD")
public class Period extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = -2817134547972324704L;

	public Period() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_Period_Gen")
	@TableGenerator(name = "BoomerId_Period_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "Period_Gen", allocationSize = 1)
	private int id;

	@Column( name="PERIODNAME", length=127, nullable=false)
	private String periodName;
	
	@Column( name="STARTDT", nullable=false)
	private Date startDt;

	@Column( name="ENDDT", nullable=false)
	private Date endDt;

	@ManyToOne(cascade=CascadeType.ALL, targetEntity=BoomerType.class, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="PERIODTYPE_id")
	private BoomerType periodType;
	
	@Column( name="NOTES", length=127, nullable=false)
	private String notes;
	
	@Transient
	public Period prevPeriod = null;
	
	@Transient
	public Period nextPeriod = null;
	
	public int getId() {
		return id;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

	public String getPeriodName() {
		return Strings.nullToEmpty(periodName);
	}

	public Date getStartDt() {
		return startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public BoomerType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(BoomerType periodType) {
		this.periodType = periodType;
	}

	@Override
	public String toString() {
		return periodName;
//				+ " < ID=" + id + "; " 
//				+ " {From " + startDt.toString() + " to " + endDt.toString() + "}"
//				+ " >";
	}
	
}
