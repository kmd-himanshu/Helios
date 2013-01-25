package com.helio.boomer.rap.utility;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class BorderLayoutCustomize extends Layout
{
	  public static final int NORTH = 0;
	  public static final int SOUTH = 1;
	  public static final int CENTER = 2;
	  public static final int EAST = 3;
	  public static final int WEST = 4;

	  /**
	   * Indicates the region that a control belongs to.
	   *  
	   */
	  public static class BorderData {
	    public int region = CENTER; // default.

	    public BorderData() {
	    }

	    public BorderData(int region) {
	      this.region = region;
	    }
	  }

	  // Controls in all the regions.
	  public Control[] controls = new Control[5];

	  // Cached sizes.
	  Point[] sizes;

	  // Preferred width and height
	  int width;
	  int height;

	@Override
	protected Point computeSize(Composite arg0, int arg1, int arg2, boolean arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		if (flushCache || sizes == null)
		      refreshSizes(composite.getChildren());

		    Rectangle clientArea = composite.getClientArea();

		    // Enough space for all.
		    if (controls[NORTH] != null) {
		      controls[NORTH].setBounds(
		        clientArea.x,
		        clientArea.y,
		        clientArea.width,
		        550);
//		        sizes[NORTH].y);
		    }
		    if (controls[SOUTH] != null) {
		      controls[SOUTH].setBounds(
		        clientArea.x,
//		        clientArea.y + clientArea.height - sizes[SOUTH].y,
		        clientArea.y + clientArea.height - 40,
		        clientArea.width,
		        40);
//		        sizes[SOUTH].y);
		    }
		    if (controls[WEST] != null) {
		      controls[WEST].setBounds(
		        clientArea.x,
		        clientArea.y + sizes[NORTH].y,
		        sizes[WEST].x,
		        clientArea.height - sizes[NORTH].y - sizes[SOUTH].y);
		    }
		    if (controls[EAST] != null) {
		      controls[EAST].setBounds(
		        clientArea.x + clientArea.width - sizes[EAST].x,
		        clientArea.y + sizes[NORTH].y,
		        sizes[EAST].x,
		        clientArea.height - sizes[NORTH].y - sizes[SOUTH].y);
		    }
		    if (controls[CENTER] != null) {
		      controls[CENTER].setBounds(
		        clientArea.x + sizes[WEST].x,
		        clientArea.y + sizes[NORTH].y,
		        clientArea.width - sizes[WEST].x - sizes[EAST].x,
		        clientArea.height - sizes[NORTH].y - sizes[SOUTH].y);
		    }
		
	}
	
	 private void refreshSizes(Control[] children) {
		    for (int i = 0; i < children.length; i++) {
		      Object layoutData = children[i].getLayoutData();
		      if (layoutData == null || (!(layoutData instanceof BorderData)))
		      {controls[0] = children[i];
		        continue;
		      }
		      BorderData borderData = (BorderData) layoutData;
		      if (borderData.region < 0 || borderData.region > 4) // Invalid.
		        continue;
		      controls[borderData.region] = children[i];
		    }

		    width = 0;
		    height = 0;

		    if (sizes == null)
		      sizes = new Point[5];

		    for (int i = 0; i < controls.length; i++) {
		      Control control = controls[i];
		      if (control == null) {
		        sizes[i] = new Point(0, 0);
		      } else {
		        sizes[i] = control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		      }
		    }

		    width = Math.max(width, sizes[NORTH].x);
		    width =
		      Math.max(width, sizes[WEST].x + sizes[CENTER].x + sizes[EAST].x);
		    width = Math.max(width, sizes[SOUTH].x);

		    height =
		      Math.max(Math.max(sizes[WEST].y, sizes[EAST].y), sizes[CENTER].y)
		        + sizes[NORTH].y
		        + sizes[SOUTH].y;

		  }

}
