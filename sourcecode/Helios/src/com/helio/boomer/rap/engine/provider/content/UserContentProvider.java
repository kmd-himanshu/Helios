package com.helio.boomer.rap.engine.provider.content;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;

import com.helio.boomer.rap.engine.UserListController;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.model.PE_User;
//import com.helio.boomer.rap.engine.model.User;
import com.helio.boomer.rap.engine.model.User_action;
import com.helio.boomer.rap.engine.modellist.UserModelList;
import com.helio.boomer.rap.view.stimson.BusinessUnitDashboardView;
import com.helio.boomer.rap.view.stimson.UserManagementDashboardView;
import com.helio.boomer.rap.view.stimson.UserManagementNavView;

public class UserContentProvider implements ITreeContentProvider,PropertyChangeListener {	
	
	private UserManagementDashboardView userManagementDashboardView = (UserManagementDashboardView) PlatformUI
			.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.findView(UserManagementDashboardView.ID);

	private Viewer viewer;
	

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		if ((oldInput != null) && (oldInput instanceof UserModelList)) {
			removeListenerFrom((UserModelList) oldInput);
		}
		if ((newInput != null) && (newInput instanceof UserModelList)) {
			addListenerTo((UserModelList) newInput);
			((UserModelList) newInput).addPropertyChangeListener(this);
		}
		
		// added for Users
		if ((oldInput != null) && (oldInput instanceof PE_User)) {			
			removeListenerFrom((PE_User) oldInput);
		}
		if ((newInput != null) && (newInput instanceof PE_User)) {
			addListenerTo((PE_User) newInput);
			((PE_User) newInput).addPropertyChangeListener(this);
		}

	}
	
	private void addListenerTo(PE_User newInput) {
		newInput.addPropertyChangeListener(this);		
	}

	private void removeListenerFrom(PE_User oldInput) {
		oldInput.removePropertyChangeListener(this);
	}
	
	private void addListenerTo(UserModelList newInput) {
		newInput.addPropertyChangeListener(this);
	}

	private void removeListenerFrom(UserModelList oldInput) {
		oldInput.removePropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equalsIgnoreCase("divisionList")) {
			viewer.refresh();
		}
	}
	

	@Override
	public void dispose() {
		// Do nothing
	}

	@Override
	public boolean hasChildren(Object element) {	
		
		
		if (element instanceof UserModelList) {
			return true;
		}
		if (element instanceof PE_User) {
			return true;
		}
		if (element instanceof User_action) {
			return true;
		}	
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {		
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		if (parentElement instanceof UserModelList) {
			UserModelList userModelList = (UserModelList) parentElement;
			return userModelList.getUserList().toArray();
		}
		if (parentElement instanceof PE_User) {
			PE_User user = (PE_User) parentElement;
			return user.getList_user_action().toArray();
		}
		if (parentElement instanceof User_action) {
				User_action user_action = (User_action) parentElement;
				PE_User user = user_action.getPe_user();					
				String mode = null;
				
				if(user_action.getActionName().equalsIgnoreCase("VIEW"))
				{
					mode = "view";
					userManagementDashboardView.createForm("view",user,viewer);					
				}
				if(user_action.getActionName().equalsIgnoreCase("EDIT"))
				{
					mode = "edit";
					userManagementDashboardView.createForm("edit",user,viewer);						
				}	
				if(user_action.getActionName().equalsIgnoreCase("REMOVE"))
				{
					mode = "remove";
					userManagementDashboardView.createForm("remove",user,viewer);					
				}
				if(user_action.getActionName().equalsIgnoreCase("SEND PASSWORD"))
				{
					mode = "sendpwd";
					userManagementDashboardView.createForm("sendpwd",user,viewer);						
				}			

			return null;
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
//		if (element instanceof Location) {
//			Location location = (Location) element;
//			return location.getDivision();
//		}
//		if (element instanceof Building) {
//			Building building = (Building) element;
//			return building.getLocation();
//		}
//		if (element instanceof BuildingAllocation) {
//			BuildingAllocation buildingAllocation = (BuildingAllocation) element;
//			return buildingAllocation.getBuilding();
//		}		
		return null;
	}

}