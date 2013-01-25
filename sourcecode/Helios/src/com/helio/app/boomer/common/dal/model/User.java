package com.helio.app.boomer.common.dal.model;

import java.sql.Date;
import java.util.List;


public class User {

	private long id;
	private String firstName;
	private String lastName;
	private String userName;
	private String emailAddr;
	private String password;
	private List<Location> locations;
	private Client client;
	private boolean globalAdmin;
	private boolean clientAdmin;
	private Date expire;
	private List<Role> roles;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isGlobalAdmin() {
		return globalAdmin;
	}
	public void setGlobalAdmin(boolean globalAdmin) {
		this.globalAdmin = globalAdmin;
	}
	public boolean isClientAdmin() {
		return clientAdmin;
	}
	public void setClientAdmin(boolean clientAdmin) {
		this.clientAdmin = clientAdmin;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public Date getExpire() {
		return expire;
	}
	public void setExpire(Date expire) {
		this.expire = expire;
	}
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	
}
