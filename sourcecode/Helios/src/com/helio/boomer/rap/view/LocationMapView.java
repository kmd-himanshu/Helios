package com.helio.boomer.rap.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.eclipsesource.widgets.gmaps.GMap;
import com.eclipsesource.widgets.gmaps.LatLng;
import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.BuildingAllocation;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.model.Geolocation;
import com.helio.boomer.rap.engine.model.Location;

public class LocationMapView extends ViewPart {

	static final private String INIT_CENTER = "37.717517,-122.484362";
	static final private int INIT_ZOOM = 15;
	static final private int INIT_TYPE = GMap.TYPE_HYBRID;
	
	private GMap gmap = null;

	public static final String ID = "com.helio.boomer.rap.view.locationmapview";

	private ISelectionListener selectionListener;

	//////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS //
	//////////////////////////////////////////////////////////////////////////////////
	
	public LocationMapView() {
		super();
	}

	//////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES //
	//////////////////////////////////////////////////////////////////////////////////
	
	private final class WorkbenchSelectionListener implements ISelectionListener {
		public void selectionChanged( IWorkbenchPart part,
				ISelection selection ) {
			if( selection instanceof IStructuredSelection ){
				updateSelection( (IStructuredSelection) selection );
			}
			else {
				return;
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	// MAIN BODY //
	//////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This listens for workbench selections -- if a Location has been selected, it will
	 * recenter the map to the associated GeoLocation found for the Location. If a
	 * Building or BuildingAllocation is selected, it will get the parent Location and
	 * center on it. 
	 * @return 
	 */
	private void updateSelection(IStructuredSelection selection) {
		int zoomLevel = 14;
		Object object = selection.getFirstElement();
		Location location = null;
		if (object instanceof Division) {
			Division division = (Division) object;
			if ((division.getLocations() != null) && (division.getLocations().size() > 0)) {
				location = division.getLocations().get(0);
				zoomLevel = 12;
			}
		}
		if (object instanceof Location) {
			location = (Location) object;
			zoomLevel = 14;
		}
		if (object instanceof Building) {
			location = ((Building) object).getLocation();
			zoomLevel = 15;
		}
		if (object instanceof BuildingAllocation) {
			BuildingAllocation buildingAllocation = (BuildingAllocation) object;
			if (buildingAllocation.getBuilding() != null) {
				location = buildingAllocation.getBuilding().getLocation();
				zoomLevel = 16;
			}
		}
		if ((location != null) && (location.getGeolocation() != null)) {
			Geolocation geolocation = location.getGeolocation();
			if ((geolocation.getLatitude() != null) && (geolocation.getLongitude() != null)) {
				try {
					LatLng latLng = new LatLng(geolocation.getLatitude(), geolocation.getLongitude());
					gmap.setZoom(zoomLevel);
					gmap.setCenter(latLng);
				} catch (Exception e) {
					System.err.println("Problem re-centering map: " + e.toString());
				}
			}
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl( Composite parent ) {
		parent.setLayout( new FillLayout() );
		createMap(parent);
		// LISTEN TO THE WORKBENCH
		selectionListener = new WorkbenchSelectionListener();
		ISelectionService selectionService =
			getViewSite().getWorkbenchWindow().getSelectionService();
		selectionService.addSelectionListener(selectionListener);
	}

	private void createMap( final Composite parent ) {
		gmap = new GMap( parent, SWT.NONE );
		gmap.setCenter( stringToLatLng( INIT_CENTER ) );
		gmap.setZoom( INIT_ZOOM );
		gmap.setType( INIT_TYPE );
	}

	private LatLng stringToLatLng( final String input ) {
		LatLng result = null;
		if( input != null ) {
			String temp[] = input.split( "," );
			if( temp.length == 2 ) {
				try {
					double lat = Double.parseDouble( temp[ 0 ] );
					double lon = Double.parseDouble( temp[ 1 ] );
					result = new LatLng( lat, lon );
				} catch ( NumberFormatException ex ) {
				}
			}
		}
		return result;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		super.dispose();
		ISelectionService selectionService =
			getViewSite().getWorkbenchWindow().getSelectionService();
		selectionService.removeSelectionListener(selectionListener);
	}

}