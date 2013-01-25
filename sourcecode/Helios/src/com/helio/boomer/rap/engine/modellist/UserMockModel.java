package com.helio.boomer.rap.engine.modellist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rwt.RWT;

import com.helio.boomer.rap.engine.model.User;
import com.helio.boomer.rap.session.SessionDataStore;

public class UserMockModel {
	
	 public List<UserModelList> getCategories() {
		    List<UserModelList> categories = new ArrayList<UserModelList>();
		    UserModelList category = new UserModelList();
		    
		    String sessionId = RWT.getSessionStore().getId();
		    SessionDataStore sessionDataStore = (SessionDataStore)RWT.getSessionStore().getAttribute(sessionId);
		    String user = sessionDataStore.getUsername();
		    
		    category.setName(user);
		    categories.add(category);
		    User todo = new User("-- View");
		    category.getTodos().add(todo);
		    todo = new User("-- Update");
		    category.getTodos().add(todo);
		    todo = new User("-- Delete");
		    category.getTodos().add(todo);
		    
//		    category = new UserModelList();
//		    category.setName("Leasure");
//		    categories.add(category);
//		    todo = new User("Skiing");
//		    category.getTodos().add(todo);
		    
		    return categories;
		  }

}
