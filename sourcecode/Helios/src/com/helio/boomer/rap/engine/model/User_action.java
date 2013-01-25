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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;

@Entity
@Table(name="USER_ACTION")
public class User_action extends AbstractModelObject implements Serializable {

	private static final long serialVersionUID = -4535627592969320512L;	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BoomerId_Division_Gen")
	@TableGenerator(name = "BoomerId_Division_Gen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "Division_Gen", allocationSize = 10)
	private long user_action_id;
	

	@Column( name="ACTIONNAME", length=127, nullable=false)
	private String actionName;
	
	
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=PE_User.class, fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="id")
	private PE_User pe_user;
	
	public PE_User getPe_user() {
		return pe_user;
	}

	public void setPe_user(PE_User pe_user) {
		this.pe_user = pe_user;
	}

	public User_action() {
		super();
	}	
	
	public long getUser_action_id() {
		return user_action_id;
	}

	public void setUser_action_id(long user_action_id) {
		this.user_action_id = user_action_id;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Override
	public String toString() {
		return actionName;
	}
	
}
