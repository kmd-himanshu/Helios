package com.helio.boomer.rap.view.stimson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.helio.boomer.rap.engine.DivisionListController;
import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.provider.content.DivisionContentProvider;
import com.helio.boomer.rap.engine.provider.label.DivisionNavTreeLabelProvider;

public class DistributionNavView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.stimson.distributionnavview";

	private TreeViewer viewer;

	public DistributionNavView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
		tvc.getColumn().setAlignment(SWT.FILL);
		tvc.getColumn().setWidth(300);
		viewer.setContentProvider(new DivisionContentProvider());
		viewer.setLabelProvider(new DivisionNavTreeLabelProvider());
		getSite().setSelectionProvider(viewer);
		setSelectionLogic(viewer);
		viewer.setInput(DivisionListController.getInstance().getDivisionModelList());
		//
		getSite().setSelectionProvider(viewer);
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
	
	private void setSelectionLogic(final TreeViewer myViewer) {
		myViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.size() <= 1) return;
                Object onObject = selection.getFirstElement();
                List goodObjects = new ArrayList();
                goodObjects.add(onObject);
                for (Object obj : selection.toList()) {
                	if (obj == onObject) continue;
                	if (checkParentage(onObject, obj)) {
                		goodObjects.add(obj);
                	} else {
                		myViewer.setSelection(new StructuredSelection(goodObjects));
                		return;
                	}
                }
            }
		});
	}
	
	public static boolean checkParentage(Object obj1, Object obj2) {
		if (!obj1.getClass().equals(obj2.getClass())) return false;
		if (obj1 instanceof Division) {
			Division division1 = (Division) obj1;
			Division division2 = (Division) obj2;
			return division1.equals(division2);
		}
		if ((obj1 instanceof Location) && (obj2 instanceof Location)) {
			Location location1 = (Location) obj1;
			Location location2 = (Location) obj2;
			return location1.getDivision().equals(location2.getDivision());
		}
		if ((obj1 instanceof Building) && (obj2 instanceof Building)) {
			Building building1 = (Building) obj1;
			Building building2 = (Building) obj2;
			return building1.getLocation().equals(building2.getLocation());
		}
		return false;
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