package com.helio.boomer.rap.engine.modellist;

import java.util.List;

import com.google.common.collect.Lists;
import com.helio.boomer.rap.engine.AbstractModelObject;
import com.helio.boomer.rap.engine.model.PE_User;

public class UserModelList extends AbstractModelObject {
	
	private List<PE_User> userList;
	
	public UserModelList() {
		this.userList = Lists.newArrayList();
	}
	
	public UserModelList(List<PE_User> userList) {
		this.userList = userList;
	}
	
	public List<PE_User> getUserList() {
		return userList;
	}
	
	public void addUser(PE_User user) {
		userList.add(user);
		this.firePropertyChange("userList", null, null);
	}
	
	
	public void removeUser(PE_User user) {
		userList.remove(user);
		this.firePropertyChange("userList", null, null);
	}

}