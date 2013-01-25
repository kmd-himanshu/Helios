package com.helio.app.boomer.common.dal.model;

import java.util.List;

public class Role {

	private long id;
	private String name;
	private String abbreviation;
	private long clientId;
	List<Access> accessList;
	private List<User> users;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public List<Access> getAccessList() {
		return accessList;
	}
	public void setAccessList(List<Access> accessList) {
		this.accessList = accessList;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

}
