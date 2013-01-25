<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Strict//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page import="java.util.Arrays"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BIRT Chart Javascript extension API viewer</title>
<script type="text/javascript" src="js/chartAjaxAPI.js"></script>
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


%>
function generateChart()
{
	// Construct data
	var categories = new Array("Tracy","El Monte","Santa Fe Springs");
	
	var cm = new ChartModel(350, 300);
	cm.setType("bar");
	cm.setFormat("png");
	cm.setDimension("2.5d");
	cm.setColorByCategory(true);
	cm.setShowLabel(<%=isShowLabel%>);
	cm.setShowLegend(<%=isShowLegend%>);
	cm.setTitle(<%=title%>);	
	cm.setCategories(categories);
	cm.setValues("0.0,0.0,0.0");
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