package com.helio.boomer.rap.engine;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rwt.RWT;
import org.eclipse.ui.PlatformUI;

import com.helio.boomer.rap.engine.model.PE_User;
import com.helio.boomer.rap.engine.modellist.UserModelList;
import com.helio.boomer.rap.persistence.PersistenceManager;
import com.helio.boomer.rap.session.SessionDataStore;
import com.helio.boomer.rap.view.stimson.UserManagementDashboardView;
import com.helio.boomer.rap.view.stimson.UserManagementNavView;

public class UserListController {
	
	public static class UserListControllerHolder {
		private final static UserListController INSTANCE = new UserListController();
	}

	private UserModelList userModelList;	

	private UserListController() {
		userModelList = new UserModelList();		
		loadAllUser();
	}

	public static UserListController getInstance() {
		return UserListControllerHolder.INSTANCE;
	}

	public UserModelList getUserModelList() {
		return userModelList;
	}

	public void loadAllUser() {
		try {
			EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
			EntityManager em = emf.createEntityManager();		
			
			try {
				
				TypedQuery<PE_User> query = em.createQuery("select u from PE_User u", PE_User.class);
				for (PE_User user : query.getResultList()) {					
					userModelList.addUser(user);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				em.close();
			}
		} catch (Exception e) {
			MessageDialog.openInformation(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Problem!",
					e.toString());
			e.printStackTrace();
		}
	}

	public void addUser(PE_User user) {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.persist(user); 
				transaction.commit();
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} finally {
			em.close();
		}
		userModelList.addUser(user);
	}
	public void replaceUser(PE_User userOld,PE_User userNew) {
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
//				em.remove(userOld);
				em.persist(userNew); 
//				em.refresh(userOld);
				transaction.commit();
				
//				userModelList.removeUser(userOld);
				userModelList.addUser(userNew);					
				
			} finally {
				if (transaction.isActive()) transaction.rollback();
			}
		} finally {
			em.close();
		}
		
	}

}
