package com.helio.boomer.rap.view.stimson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;



import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.helio.boomer.rap.engine.DivisionListController;
import com.helio.boomer.rap.engine.UserListController;
import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.provider.content.DivisionContentProvider;
import com.helio.boomer.rap.engine.provider.content.UserContentProvider;
import com.helio.boomer.rap.engine.provider.label.DivisionNavTreeLabelProvider;
import com.helio.boomer.rap.engine.provider.label.UserMgmtNavTreeLabelProvider;
import com.helio.boomer.rap.session.BusinessUnitDataStore;
import com.helio.boomer.rap.session.SessionDataStore;
import com.helio.boomer.rap.utility.BorderLayoutCustomize;

public class UserManagementNavView extends ViewPart {
	
	private UserManagementDashboardView userManagementDashboardView = (UserManagementDashboardView) PlatformUI
			.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.findView(UserManagementDashboardView.ID);
	

	public static final String ID = "com.helio.boomer.rap.view.stimson.usermanagementnavview";									 

	private TreeViewer viewer;

	public UserManagementNavView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		
		
//		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
////		rowLayout.spacing = 500;
//		rowLayout.marginLeft = 20;
//		rowLayout.wrap = true;
//		rowLayout.justify = true;
//		
//		
//		RowData rowdata = new RowData();
//		rowdata.height=30;
//		rowdata.width=200;
		
		
		parent.setLayout(new BorderLayoutCustomize());		
//		parent.setLayout( new GridLayout() );

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
		tvc.getColumn().setAlignment(SWT.FILL);
		tvc.getColumn().setWidth(300);
		viewer.setContentProvider(new UserContentProvider());
		viewer.setLabelProvider(new UserMgmtNavTreeLabelProvider());
		getSite().setSelectionProvider(viewer);
		viewer.setUseHashlookup(true);
		viewer.setInput(UserListController.getInstance().getUserModelList());			
		getSite().setSelectionProvider(viewer);	
		
		
		
		// Button to call ADD user		
		Button btnAddUser = new Button(parent, SWT.PUSH);
		btnAddUser.setText("Add New User");
		
		Display display = Display.getCurrent();
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
//		Color listBackground = display.getSystemColor(SWT.  );

//		btnAddUser.setBackground(listBackground);
		btnAddUser.setForeground(blue);
//		btnAddUser.setSize(50, 30);
		
//		btnAddUser.setLayoutData(rowdata);
//		btnAddUser.setLocation(10, 300);
		
		/////////////
		    
//		    Button buttonWest = new Button(shell, SWT.PUSH);
//		    buttonWest.setText("West");
		btnAddUser.setLayoutData(new BorderLayoutCustomize.BorderData(BorderLayoutCustomize.SOUTH));
		
		/////////////
		
//		parent.setLayout( new GridLayout() );

//		Button upper = new Button( parent, SWT.PUSH );
//		GridData upperData = new GridData( SWT.NONE, SWT.BOTTOM, true, true );
//		upperData.verticalAlignment = SWT.END;
//		upperData.verticalSpan = 1;
//		upperData.heightHint = 30;
//		btnAddUser.setLayoutData( upperData );

//		Button lower = new Button( parent, SWT.PUSH );
//		GridData lowerData = new GridData( SWT.FILL, SWT.FILL, true, true );
//		lower.setLayoutData( lowerData );

		
		///////////////////////
		
		btnAddUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				userManagementDashboardView.createForm("create", null, viewer);
				
			}
		});		

	}
	
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public void refresh() {
		viewer.refresh();
	}

	public List<Long> getSelectedBuildingIds() {
		List<Long> buildingIdList = Lists.newArrayList();
		for (Object selection : ((IStructuredSelection) viewer.getSelection()).toList()) {
			if (selection instanceof Building) {
				buildingIdList.add((long) ((Building) selection).getId());
			}
			if (selection instanceof Location) {
				for (Building building : ((Location) selection).getBuildings()) {
					buildingIdList.add((long) building.getId());
				}
			}
		}
		return buildingIdList;
	}
	
	public Long getSelectedLocationId() {
		for (Object selection : ((IStructuredSelection) viewer.getSelection()).toList()) {
			if (selection instanceof Location) {
				return new Long(((Location) selection).getId());
			}
			if (selection instanceof Building) {
				return new Long(((Building) selection).getLocation().getId());
			}
		}
		return null;
	}
	
	public List<Long> getSelectedLocationIds() {
		Set<Long> locationSet = Sets.newHashSet();
		for (Object selection : ((IStructuredSelection) viewer.getSelection()).toList()) {
			if (selection instanceof Location) {
				locationSet.add(new Long(((Location) selection).getId()));
			}
			if (selection instanceof Building) {
				locationSet.add(new Long(((Building) selection).getLocation().getId()));
			}
		}
		return new ArrayList<Long>(locationSet);
	}
	
	/*
	 * RMAP-74
	 * Sprint 1.0.2
	 */

	
	public Object[] getExpandedList()
	{		
		return viewer.getExpandedElements();			
	}
	
	public void expandTree(Object[] arrExpanded)
	{
		viewer.setExpandedElements(arrExpanded);		
	}
	
	public ISelection getSelectedElements()
	{
		 ISelection iSelection = viewer.getSelection();
		 return iSelection ;
	}
	
	public void setSelectedElements(ISelection iSelection)
	{
		viewer.setSelection(iSelection); 
	}
	
	/*
	 * END RMAP-74
	 */	
	
}