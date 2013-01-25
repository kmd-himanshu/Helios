package com.helio.boomer.rap.engine.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="BOOMERTYPE")
public class BoomerType extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = 974626757084593751L;

	public BoomerType() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_BoomerType_Gen")
	@TableGenerator(name = "BoomerId_BoomerType_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "BoomerType_Gen", allocationSize = 10)
	private int id;

	@Column( name="DOMAINTABLE", length=127, nullable=false)
	private String domainTable;

	@Column( name="DOMAINCOLUMN", length=127, nullable=false)
	private String domainColumn;

	@Column( name="FIELDNAME", length=127, nullable=false)
	private String fieldName;

	@Column( name="FIELDVALUE", length=127, nullable=false)
	private String fieldValue;
	
	@Column( name="TYPEORDER")
	private Integer typeOrder;

	public int getId() {
		return id;
	}

	public String getDomainTable() {
		return domainTable;
	}

	public void setDomainTable(String domainTable) {
		this.domainTable = domainTable;
	}

	public String getDomainColumn() {
		return domainColumn;
	}

	public void setDomainColumn(String domainColumn) {
		this.domainColumn = domainColumn;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Integer getTypeOrder() {
		return typeOrder;
	}
	
	public void setTypeOrder(Integer typeOrder) {
		this.typeOrder = typeOrder;
	}
	
	@Override
	public String toString() {
		return "<ID="
			+ id
			+ "; "
			+ "DomainTable="	+ domainTable
			+ "; DomainColumn="	+ domainColumn
			+ "; FieldName="	+ fieldName
			+ "; FieldValue="	+ fieldValue
			+ "; TypeOrder="	+ typeOrder
			+ ">";
	}
	
}
