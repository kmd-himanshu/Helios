package com.helio.boomer.rap.view;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import com.helio.boomer.rap.engine.ManufacturerListController;
import com.helio.boomer.rap.engine.provider.content.ManufacturerContentProvider;
import com.helio.boomer.rap.engine.provider.label.DeviceNavTreeLabelProvider;

public class DeviceNavView extends ViewPart {
	public DeviceNavView() {
	}
	public static final String ID = "com.helio.boomer.rap.view.devicenavview";

	private TreeViewer viewer;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		int style = SWT.H_SCROLL | SWT.V_SCROLL;
		parent.setLayout(new GridLayout(2, false));
		//
		viewer = new TreeViewer(parent, style);
		Tree tree = viewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
		tvc.getColumn().setAlignment(SWT.FILL);
		tvc.getColumn().setWidth(300);
		viewer.setContentProvider(new ManufacturerContentProvider());
		viewer.setLabelProvider(new DeviceNavTreeLabelProvider());
		//
		getSite().setSelectionProvider(viewer);
		viewer.setInput(ManufacturerListController.getInstance().getManufacturerList());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}