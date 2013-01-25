package com.helio.app.boomer.common.dal.model;

public class PacketValue {
	
	private String name;
	private String value;
	private DataFieldTypeEnum fieldType;
	private int version;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStrValue() {
		return value;
	}
	public void setStrValue(String value) {
		this.value = value;
	}
	public DataFieldTypeEnum getFieldType() {
		return fieldType;
	}
	public void setFieldType(int fieldType) {
		this.fieldType = DataFieldTypeEnum.fromInt(fieldType);
	}	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
}
