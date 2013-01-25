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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="PE_USER")
public class PE_User extends AbstractModelObject implements Serializable {
	
	private static final long serialVersionUID = -6443931837768039861L;

	public PE_User() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_Division_Gen")
	@TableGenerator(name = "BoomerId_Division_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "Division_Gen", allocationSize = 10)
	private long id;
	
	@Column( name="CLIENT_id", length=127, nullable=false)
	private long clientId;
	
	@Column( name="F_NAME", length=127, nullable=false)
	private String firstName;
	
	@Column( name="L_NAME", length=127, nullable=false)
	private String lastName;
	
	@Column( name="USERNAME", length=127, nullable=false)
	private String userName;
	
	@Column( name="EMAIL_ADDR", length=127, nullable=false)
	private String emailAddr;
	
	@Column( name="PASSWORD", length=127, nullable=false)
	private String password;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="pe_user", fetch=FetchType.LAZY)
	private List<User_action> list_user_action;
//	private Client client;
//	private List<UserRole> userRoles;	

	
	
	public List<User_action> getList_user_action() {
		return list_user_action;
	}
	public void setList_user_action(List<User_action> list_user_action) {
		this.list_user_action = list_user_action;
	}
	
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

//	public Client getClient() {
//		return client;
//	}
//	public void setClient(Client client) {
//		this.client = client;
//	}
//	public List<UserRole> getUserRoles() {
//		return userRoles;
//	}
//	public void setUserRoles(List<UserRole> userRoles) {
//		this.userRoles = userRoles;
//	}
	
}
