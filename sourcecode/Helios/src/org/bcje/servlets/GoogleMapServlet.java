package org.bcje.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bcje.model.ChartModel;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.viewer.internal.util.ImageHTMLEmitter;
import org.jfree.chart.JFreeChart;

import com.eclipsesource.widgets.gmaps.LatLng;
import com.helio.boomer.rap.engine.DivisionListController;
import com.helio.boomer.rap.engine.model.Building;
import com.helio.boomer.rap.engine.model.BuildingAllocation;
import com.helio.boomer.rap.engine.model.Division;
import com.helio.boomer.rap.engine.model.Geolocation;
import com.helio.boomer.rap.engine.model.Location;
import com.helio.boomer.rap.engine.modellist.DivisionModelList;

/**
 * 
 */

public class GoogleMapServlet extends HttpServlet {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -970069279731222139L;
	private static ILogger logger = Logger.getLogger("BirtChartJsExt"); //$NON-NLS-1$

	private ServletContext context;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.context = config.getServletContext();

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String html = null;

		ChartModel chartModel = null;
		String imageMap = null;
		JFreeChart chart = null;
		final int DEFAULT_WIDTH=250;
		final int DEFAULT_HEIGHT=250;
		try {
			int width = DEFAULT_WIDTH;
			if (request.getParameter("width") != null) {
				try {
					width = Integer.parseInt(request.getParameter("width"));
					if (width < DEFAULT_WIDTH)
						width = DEFAULT_WIDTH;
				} catch (Exception e) {
					width = DEFAULT_WIDTH;
				}
			}
			int height = DEFAULT_HEIGHT;
			if (request.getParameter("height") != null) {
				try {
					height = Integer.parseInt(request.getParameter("height"));
					if (height < DEFAULT_HEIGHT)
						height = DEFAULT_HEIGHT;
				} catch (Exception e) {
					height = DEFAULT_HEIGHT;
				}
			}
			if(request.getSession().getAttribute("chartModel")!=null)
			{
				chartModel=(ChartModel)request.getSession().getAttribute("chartModel");
				chartModel.setWidth(width);
				chartModel.setHeight(height);
			}else{
			chartModel = new ChartModel("bar", "2.5d", "png", width, height);
			}
			if(request.getParameter("divisions")!=null)
			{
				List<Long> list=getList(request.getParameter("divisions"));
				chartModel.setDevisionIds(list);
				
			}
			if(request.getParameter("locations")!=null)
			{
				List<Long> list=getList(request.getParameter("locations"));
				chartModel.setLocationIds(list);
				
			}
			if(request.getParameter("buildings")!=null)
			{
				List<Long> list=getList(request.getParameter("buildings"));
				chartModel.setBuildingIds(list);
				
			}
			if(request.getParameter("buildingAllocations")!=null)
			{
				List<Long> list=getList(request.getParameter("buildingAllocations"));
				chartModel.setBuildingAllocationIds(list);
				
			}
			
			if(request.getParameter("startDate")!=null)
			{
				chartModel.setStartDate(request.getParameter("startDate"));
			}
			
			if(request.getParameter("endDate")!=null)
			{
				chartModel.setEndDate(request.getParameter("endDate"));
			}
			
			if(request.getParameter("startPeriod")!=null)
			{
				chartModel.setStartPeriod(request.getParameter("startPeriod"));
			}
			
			if(request.getParameter("endPeriod")!=null)
			{
				chartModel.setEndPeriod(request.getParameter("endPeriod"));
			}
			
			
			
			boolean isShowTitle =true;
			if (request.getParameter("displayTitle") != null
					&& !"true".equalsIgnoreCase(request
							.getParameter("displayTitle"))) {
				isShowTitle=false;
			}
			
			boolean isShowLabel = true;
			if (request.getParameter("displayLabels") != null
					&& !"true".equalsIgnoreCase(request
							.getParameter("displayLabels"))) {
				isShowLabel = false;
			}
			boolean isShowLegend = true;
			if (request.getParameter("displayLegends") != null
					&& !"true".equalsIgnoreCase(request
							.getParameter("displayLegends"))) {
				isShowLegend = false;
			}
			
			
			
			
			

			
			chartModel.setColorByCategory(true);
			chartModel.setShowLabel(isShowLabel);
			chartModel.setShowLegend(isShowLegend);
			chartModel.setShowTitle(isShowTitle);
			chartModel.setHeliosChartType(request.getParameter("chartType"));

			request.getSession().setAttribute("chartModel", chartModel ); 
			
			
			int zoomLevel = 14;
			String[] array=request.getParameter("googleMapParam").split("\\|");
			String divisionId="NULL".equalsIgnoreCase(array[0])?null:array[0];
			String locationId="NULL".equalsIgnoreCase(array[1])?null:array[1];
			String buildingId="NULL".equalsIgnoreCase(array[2])?null:array[2];
			String buildingAllocationId="NULL".equalsIgnoreCase(array[3])?null:array[3];
			String lastSelected=array[4];
			DivisionModelList list=DivisionListController.getInstance().getDivisionModelList();
			List<Division> divisionList=list.getDivisionList();
			Location location = null;
			Division division = null;
            Building building=null;
			for (int i = 0; divisionId!=null && divisionList != null && i < divisionList.size(); i++) {
				if (divisionId.equalsIgnoreCase(String.valueOf(divisionList.get(i)
						.getId()))) {
					division = divisionList.get(i);
					break;
				}

			}

			if("DIVISION".equals(lastSelected)) {
				
				if ((division.getLocations() != null)
						&& (division.getLocations().size() > 0)) {
					location = division.getLocations().get(0);
					zoomLevel = 12;
				}
				
				
			}
			if("LOCATION".equals(lastSelected)) {
				for(int i=0;division!=null && division.getLocations()!=null && i<division.getLocations().size();i++)
				{
					if (locationId.equalsIgnoreCase(String.valueOf(division.getLocations().get(i).getId()))) {
						location = division.getLocations().get(i);
						break;
					}

				}
				zoomLevel = 14;
			}
			if("BUILDING".equals(lastSelected)) {
				for(int i=0;location!=null && location.getBuildings()!=null && i<location.getBuildings().size();i++)
				{
					if (buildingId.equalsIgnoreCase(String.valueOf(location.getBuildings().get(i).getId()))) {
						building=location.getBuildings().get(i);
						location =building.getLocation();
						break;
					}

				}
				zoomLevel = 15;
			}
			if("BULDING_ALLOCATION".equals(lastSelected)) {
				BuildingAllocation buildingAllocation = null;
				for(int i=0;building!=null && building.getBuildingAllocations()!=null && i<building.getBuildingAllocations().size();i++)
				{
					if (buildingAllocationId.equalsIgnoreCase(String.valueOf(building.getBuildingAllocations().get(i).getId()))) {
						buildingAllocation=building.getBuildingAllocations().get(i);
						location = buildingAllocation.getBuilding().getLocation();
						break;
					}

				}					
					zoomLevel = 16;
				
			}
			LatLng latLng=null;
			
			if ((location != null) && (location.getGeolocation() != null)) {
				Geolocation geolocation = location.getGeolocation();
				if ((geolocation.getLatitude() != null)
						&& (geolocation.getLongitude() != null)) {
					try {
						latLng = new LatLng(geolocation.getLatitude(),
								geolocation.getLongitude());
						
					} catch (Exception e) {
						System.err.println("Problem re-centering map: "
								+ e.toString());
					}
				}
			}
			
			
			
				StringBuffer buffer=new StringBuffer();	               
				/*buffer.append("<!DOCTYPE html>");
				buffer.append("<html>");
				buffer.append("  <head>");
				buffer.append("    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />");*/
				buffer.append("    <style type=\"text/css\">");
				buffer.append("      html { height: 100% }");
				buffer.append("      body { height: 100%; margin: 0; padding: 0 }");
				buffer.append("      #map_canvas { height: 100% }");
				buffer.append("    </style>");
				buffer.append("    <script type=\"text/javascript\"");
				buffer.append("      src=\"http://maps.googleapis.com/maps/api/js?key=AIzaSyApuvViXRw1PjGmEF9kfL0GdRXVrENiyfo&amp;sensor=true\">");
				buffer.append("    </script>");
				buffer.append("    <script type=\"text/javascript\">");
				buffer.append("      function initialize() {");
				buffer.append("        var mapOptions = {");
				buffer.append("          center: new google.maps.LatLng("+latLng.latitude+","+latLng.longitude+"),");
				buffer.append("          zoom: "+zoomLevel+",");
				buffer.append("          mapTypeId: google.maps.MapTypeId.ROADMAP");
				buffer.append("        };");
				buffer.append("        var map = new google.maps.Map(document.getElementById(\"map_canvas\"),");
				buffer.append("            mapOptions);");
				buffer.append("      }");
				buffer.append("    </script>");
				buffer.append("  </head>");
				buffer.append("  <body onload=\"initialize()\">");
				buffer.append("    <div id=\"map_canvas\" style=\"width:100%; height:100%\"></div>");
				buffer.append("  </body>");
				/*buffer.append("</html>");*/

				response.getWriter().write(buffer.toString()); //$NON-NLS-1$ //$NON-NLS-2$
				response.setContentType("text/html"); //$NON-NLS-1$
				response.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
				response.getWriter().flush();
				
			
		} catch (Exception e) {
			e.printStackTrace();
			html = e.getMessage();
		}
		
  /*	    ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, chartModel.getWidth(),chartModel.getHeight());*/
		}

	public static ArrayList<Long> getList(String str)
	{
		ArrayList<Long> list=new ArrayList<Long>();
		StringTokenizer tokenizer=new StringTokenizer(str, "|");
		while(tokenizer.hasMoreTokens()){
			String id=tokenizer.nextToken();
			list.add(new Long(id));			
		}
		return list;
	}
	private ImageHTMLEmitter createEmitter( ChartModel chart, String id,
			String src, String imageMap )
	{
		ImageHTMLEmitter emitter = new ImageHTMLEmitter( );
		emitter.ext = chart.getFormat( );
		emitter.height = (int) chart.getHeight( );
		emitter.width = (int) chart.getWidth( );
		emitter.id = id;
		emitter.src = src;
		emitter.imageMap = imageMap;
		return emitter;
	}

	/*private ImageHTMLEmitter createEmitter(ChartModel chart, String id,
			String src, String imageMap) {
		ImageHTMLEmitter emitter = new ImageHTMLEmitter();
		emitter.ext = chart.getFormat();
		emitter.height = (int) chart.getHeight();
		emitter.width = (int) chart.getWidth();
		emitter.id = id;
		emitter.src = src;
		emitter.imageMap = imageMap;
		return emitter;
	}*/

	private String loadXML(String strUrl) {
		StringBuffer xml = new StringBuffer();
		try {
			InputStream is;
			if (strUrl.startsWith("http:")) //$NON-NLS-1$
			{
				URL url = new URL(strUrl);
				is = url.openStream();
			} else {
				File file = new File(strUrl);
				if (file.exists()) {
					is = new FileInputStream(file);
				} else {
					is = context.getResourceAsStream(strUrl);
				}
			}
			if (is != null) {
				byte[] buffer = new byte[1024];
				int readSize = 0;
				while ((readSize = is.read(buffer)) != -1) {
					xml.append(new String(buffer, 0, readSize));
				}
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xml.toString();
	}
	
	public static void main(String[] args) {
		
		List<Long> list=getList("1800|2100");
		System.out.println(list);
	}
}
