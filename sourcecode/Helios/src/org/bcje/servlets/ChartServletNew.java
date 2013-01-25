package org.bcje.servlets;

import java.awt.Color;
import java.awt.image.BufferedImage;
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

import org.bcje.model.CHART_TYPE;
import org.bcje.model.ChartCategory;
import org.bcje.model.ChartDataSet;
import org.bcje.model.ChartEngine;
import org.bcje.model.ChartModel;
import org.bcje.utils.ChartConvertor;
import org.bcje.utils.ChartXMLParser;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.viewer.internal.util.ChartImageManager;
import org.eclipse.birt.chart.viewer.internal.util.ImageHTMLEmitter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;

/**
 * 
 */

public class ChartServletNew extends HttpServlet {

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
		final int DEFAULT_WIDTH=280;
		final int DEFAULT_HEIGHT=300;
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
			if(request.getSession().getAttribute(request.getParameter("chartType")+ "chartModel")!=null)
			{
				chartModel=(ChartModel)request.getSession().getAttribute(request.getParameter("chartType")+ "chartModel");
				chartModel.setWidth(width);
				chartModel.setHeight(height);
			}else{
			//chartModel = new ChartModel("bar", "2.5d", "png", width, height);
			chartModel = new ChartModel("bar", "2.5d", "gif", width, height);
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

			if(request.getParameter("resize")!=null && Boolean.parseBoolean(request.getParameter("resize")))
			{
				chart=(JFreeChart) request.getSession().getAttribute(chartModel.getHeliosChartType() + "chart");
			}
			else{
				chart = (new ChartEngine()).getChart(chartModel);
			}

			

			if (chart != null) {
			
				chart.getTitle().setVisible(chartModel.isShowTitle());
				chart.getLegend().setVisible(chartModel.isShowLegend());
				if(chart.getPlot() instanceof CategoryPlot)
				{
					CategoryAxis axis=chart.getCategoryPlot().getDomainAxis();
					axis.setVisible(chartModel.isShowLabel());
				}
				
				
				response.setContentType("text/html"); 

				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection()); 

				BufferedImage chartImage = chart.createBufferedImage(chartModel.getWidth(),
						chartModel.getHeight(), info);

				
				
				// populate the info
				
				chart.createBufferedImage(chartModel.getWidth(), chartModel.getHeight( ), info); 
				imageMap = ChartUtilities.getImageMap( "map", info ); 
				request.getSession().setAttribute( chartModel.getHeliosChartType()+"chart", chart ); 
				request.getSession().setAttribute( chartModel.getHeliosChartType()+"chartModel", chartModel ); 
				request.getSession().setAttribute( chartModel.getHeliosChartType()+"chartImage",chartImage);
				
				/*StringBuffer buffer = new StringBuffer();
				//buffer.append(imageMap);
				// buffer.append("<IMG src=\"chartViewer?page="+
				// chartModel.getHeliosChartType()+"\" style=\"border:1px solid white\" usemap=\"#map\">");
				buffer.append("<IMG src=\"chartViewer?page="
						+ chartModel.getHeliosChartType()
						+ "&width="
						+ chartModel.getWidth()
						+ "&height="
						+ chartModel.getHeight()
						+ "\" style=\"border:1px solid white;padding:0px;margin:0px\" usemap=\"#map\">");
				html = buffer.toString();*/
				

				StringBuffer buffer=new StringBuffer();
	               buffer.append(imageMap);
	               //buffer.append("<IMG src=\"chartViewer?page="+ chartModel.getHeliosChartType()+"\" style=\"border:1px solid white\" usemap=\"#map\">");
	               buffer.append("<IMG src=\"chartViewer?page="+ chartModel.getHeliosChartType()+"&width="+chartModel.getWidth()+"&height="+chartModel.getHeight()+"\"  usemap=\"#map\">");
					html= buffer.toString();
					response.setContentType("text/html"); //$NON-NLS-1$
					response.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
					//response.getWriter( ).write( "<chart><![CDATA[" + html + "]]></chart>" ); //$NON-NLS-1$ //$NON-NLS-2$
				response.getWriter().write(html); //$NON-NLS-1$ //$NON-NLS-2$
				response.getWriter().flush();
				
			}
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
