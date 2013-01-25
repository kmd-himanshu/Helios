package com.helio.boomer.rap.view.stimson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

public class BusinessUnitNavView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.stimson.businessunitnavview";

	private TreeViewer viewer;

	public BusinessUnitNavView() {
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
		viewer.setInput(DivisionListController.getInstance().getDivisionModelList());			
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