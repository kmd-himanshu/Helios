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

import com.google.common.base.Strings;
import com.helio.boomer.rap.engine.AbstractModelObject;


public class User extends AbstractModelObject implements Serializable {

	private String summary = "";
	  private String description = "";

	  public User(String summary) {
	    this.summary = summary;
	  }

	  public User(String summary, String description) {
	    this.summary = summary;
	    this.description = description;

	  }

	  public String getSummary() {
	    return summary;
	  }

	  public void setSummary(String summary) {
	    this.summary = summary;
	  }

	  public String getDescription() {
	    return description;
	  }

	  public void setDescription(String description) {
	    this.description = description;
	  }
	
}
