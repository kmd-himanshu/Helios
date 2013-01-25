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
@Table(name="ALLOCATIONTYPE")
public class AllocationType extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = 3111448941320822101L;

	public AllocationType() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_AllocationType_Gen")
	@TableGenerator(name = "BoomerId_AllocationType_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "AllocationType_Gen", allocationSize = 10)
	private int id;

	@Column( name="ALLOCATIONNAME", length=127, nullable=false)
	private String allocationTypeName;

	@Column( name="MEASURABLE")
	private Boolean measurable;

	public int getId() {
		return id;
	}

	public void setAllocationTypeName(String allocationTypeName) {
		this.allocationTypeName = allocationTypeName;
	}

	public String getAllocationTypeName() {
		return Strings.nullToEmpty(allocationTypeName);
	}
	
	public void setMeasurable(Boolean measurable) {
		this.measurable = measurable;
	}
	
	public Boolean getMeasurable() {
		return measurable;
	}

	@Override
	public String toString() {
		return allocationTypeName + " <ID="
			+ id
			+ "; "
			+ " >";
	}
	
}
