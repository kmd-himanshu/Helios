package com.helio.boomer.rap.view;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.helio.boomer.rap.engine.DivisionListController;
import com.helio.boomer.rap.engine.provider.content.DivisionContentProvider;
import com.helio.boomer.rap.engine.provider.label.DivisionNavTreeLabelProvider;

public class DivisionNavView extends ViewPart {
	
	public static final String ID = "com.helio.boomer.rap.view.divisionnavview";

	private TreeViewer viewer;

	public DivisionNavView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		int style = SWT.H_SCROLL | SWT.V_SCROLL;
		viewer = new TreeViewer(parent, style);
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
		tvc.getColumn().setAlignment(SWT.FILL);
		tvc.getColumn().setWidth(300);
		viewer.setContentProvider(new DivisionContentProvider());
		viewer.setLabelProvider(new DivisionNavTreeLabelProvider());
		getSite().setSelectionProvider(viewer);
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
	
}