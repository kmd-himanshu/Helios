package com.helio.app.boomer.common.dal.model;


public class Access {

	private long id;
	private long accessTypeId;
	private String name;
	private String abbreviation;
	private boolean create;
	private boolean read;
	private boolean update;
	private boolean delete;

	
	@SuppressWarnings("unused")
	private Access() {}
	
	public Access(AccessType accessType) {
		setAccessTypeId(accessType.getId());
		setName(accessType.getName());
		setAbbreviation(accessType.getAbbreviation());
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAccessTypeId() {
		return accessTypeId;
	}
	private void setAccessTypeId(long accessTypeId) {
		this.accessTypeId = accessTypeId;
	}
	public String getName() {
		return name;
	}
	private void setName(String name) {
		this.name = name;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	private void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public boolean isCreate() {
		return create;
	}
	public void setCreate(boolean create) {
		this.create = create;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

}
