<%-- 
<%@page import="org.jfree.chart.renderer.category.CategoryItemRenderer"%>
<%@page import="org.jfree.chart.plot.CategoryPlot"%>
<%@page import="java.awt.Color"%>
<%@page import="org.jfree.chart.plot.PlotOrientation"%>
<%@page import="org.jfree.chart.ChartFactory"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="org.jfree.chart.urls.StandardCategoryURLGenerator"%>
<%@page import="org.jfree.chart.labels.StandardCategoryToolTipGenerator"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.chart.ChartRenderingInfo"%>
<%
// get ImageMap
 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
 

            dataset.setValue(30, "Girls", "SCIENCE CLASS");
            dataset.setValue(30, "Boys", "SCIENCE CLASS");
 

            dataset.setValue(10, "Girls", "ECONOMICS CLASS");
            dataset.setValue(50, "Boys", "ECONOMICS CLASS");
 

            dataset.setValue(5, "Girls", "LANGUAGE CLASS");
            dataset.setValue(55, "Boys", "LANGUAGE CLASS");
 

            JFreeChart chart =
            ChartFactory.createBarChart3D("Comparison between Girls and Boys in Science, "+ "Economics and Language classes",
            "Students Comparisons",
            "No of Students",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false);
      chart.setBackgroundPaint(Color.white);
     
      // Set the background colour of the chart
      chart.getTitle().setPaint(Color.white);
      
      // Adjust the colour of the title
      CategoryPlot plot = chart.getCategoryPlot();
     
      // Get the Plot object for a bar graph
     
      plot.setBackgroundPaint(Color.white);      
      plot.setRangeGridlinePaint(Color.white);
      CategoryItemRenderer renderer = plot.getRenderer();
      renderer.setSeriesPaint(0, Color.red);
      renderer.setSeriesPaint(1, Color.green);
      renderer.setItemURLGenerator(
 

      new StandardCategoryURLGenerator(
            "index1.html",
            "series",
            "section"));
      renderer.setToolTipGenerator(
            new StandardCategoryToolTipGenerator());
ChartRenderingInfo info = new ChartRenderingInfo(); 
// populate the info
chart.createBufferedImage(350, 300, info); 
String imageMap = ChartUtilities.getImageMap( "map", info ); 
request.getSession().setAttribute( "defaultViewer"+"chart", chart ); 



%>
<%= imageMap%>

<IMG src="chartViewer?page=defaultViewer" style="border:1px solid white" usemap="#map"> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Strict//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page import="java.util.Arrays"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BIRT Chart Javascript extension API viewer</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chartAjaxAPI.js"></script>
<script type="text/javascript">
<% String period=request.getParameter("period");

String title="'Energy per Squere'";
if(request.getParameter("displayTitle")!=null && !"true".equalsIgnoreCase(request.getParameter("displayTitle")))
{
	title="''";
}
String buildingId=request.getParameter("buildingAllocationId");

boolean isShowLabel=true;
if(request.getParameter("displayLabels")!=null && !"true".equalsIgnoreCase(request.getParameter("displayLabels")))
{
	isShowLabel=false;
}
boolean isShowLegend=true;
if(request.getParameter("displayLegends")!=null && !"true".equalsIgnoreCase(request.getParameter("displayLegends")))
{
	isShowLegend=false;
}

/* String[] array=request.getParameterValues("buldingAllocationId");
String period=request.getParameter("period");
String value="";
if(array!=null)
{
	value=Arrays.toString(array);	
	value=value.replaceAll("\\[","");
	value=value.replaceAll("\\]","");
	//System.out.println(value);
} */

int width=250;
if(request.getParameter("width")!=null)
{
	try{
		  width=Integer.parseInt(request.getParameter("width"));
	if(width<250)
		width=250;
	}catch(Exception e){
				width=250;
			}
}
int height=200;
if(request.getParameter("height")!=null)
{
	try{
		height=Integer.parseInt(request.getParameter("height"));
	if(height<200)
		height=200;
	}catch(Exception e){
		height=200;
			}
}

%>

function generateChart()
{
	
	
	// Construct data
	var categories = new Array("Tracy","El Monte","Santa Fe Springs");
	
	var cm = new ChartModel(<%=width%>, <%=height%>);
	cm.setType("bar");
	cm.setFormat("png");
	cm.setDimension("2.5d");
	cm.setColorByCategory(true);
	cm.setShowLabel(<%=isShowLabel%>);
	cm.setShowLegend(<%=isShowLegend%>);
	cm.setTitle(<%=title%>);	
	cm.setCategories(categories);
	cm.setValues("1.0,2.0,3.0");
	cm.setHeliosChartType(<%="\""+request.getParameter("chartType")+"\""%>);
	cm.setSeriesNames("Tracy","El Monte","Santa Fe Springs");
	cm.setTooltips(categories);	
	var chart = new BirtChart();
	chart.setDataXML(cm);		
	chart.render("chartDiv");
}
</script>
</head>
<body onload="generateChart()">
<div id="chartDiv"></div>
		
</body>
</html>