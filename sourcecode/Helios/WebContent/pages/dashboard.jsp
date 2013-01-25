<%@page import="org.bcje.model.CHART_TYPE"%>

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/js/jqueryui1.8.23/css/ui-lightness/jquery-ui-1.8.23.custom.css" />
 
<script type='text/javascript'
	src='<%=request.getContextPath()%>/js/jqueryui1.8.23/js/jquery-1.8.0.min.js'></script>
<script type='text/javascript'
	src='<%=request.getContextPath()%>/js/jqueryui1.8.23/js/jquery-ui-1.8.23.custom.min.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/js/sliding.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/js/easing.js'></script>
<title>BIRT Chart Javascript extension API</title>
   
<style>
body{
padding: 0;
margin: 0;
font-family: sans-serif;
}
iframe{
border:0;
padding:0;
margin: 0;
}
</style> 
</head>
 

<body>
<%!
  
StringBuffer getQueryPart(HttpServletRequest request)
{
	String pageName=request.getParameter("pageName")!=null?request.getParameter("pageName"):"defaultViewer.jsp";
	 String displayTitle=request.getParameter("displayTitle")!=null?request.getParameter("displayTitle"):"true";
	 String displayLabels=request.getParameter("displayLabels")!=null?request.getParameter("displayLabels"):"true";
	 String displayLegends=request.getParameter("displayLegends")!=null?request.getParameter("displayLegends"):"true";
	 
	 
	 StringBuffer query=new StringBuffer();
	 	
	 
	 
	 if(request.getParameter("divisions")!=null)
	 {
	 query.append("&divisions="+request.getParameter("divisions"));
	 }
	 if(request.getParameter("locations")!=null)
	 {
	 query.append("&locations="+request.getParameter("locations"));
	 }
	 
	 if(request.getParameter("buildings")!=null)
	 {
	 query.append("&buildings="+request.getParameter("buildings"));
	 }
	 if(request.getParameter("buildingAllocations")!=null)
	 {
	 query.append("&buildingAllocations="+request.getParameter("buildingAllocations"));
	 } 
	 
	 if(request.getParameter("startDate")!=null){
		 query.append("&startDate="+request.getParameter("startDate"));
	}
		 if(request.getParameter("endDate")!=null){
		 query.append("&endDate="+request.getParameter("endDate"));
   }
	 
	 if(request.getParameter("startPeriod")!=null){
	 query.append("&startPeriod="+request.getParameter("startPeriod"));
	 }
	 if(request.getParameter("endPeriod")!=null){
	 query.append("&endPeriod="+request.getParameter("endPeriod"));
	 }
	 
	 
	 query.append("&displayTitle="+displayTitle);
	 query.append("&displayLabels="+displayLabels);
	 query.append("&displayLegends="+displayLegends);
	
	  
	 
	 //System.out.println("In DASHBOARD"+request.getParameter("chartWidth"));
	 if(request.getParameter("width")!=null)
	 query.append("&width="+request.getParameter("width"));
	 if(request.getParameter("resize")!=null)
		 query.append("&resize="+request.getParameter("resize"));
	 //System.out.println(request.getContextPath()+"/"+pageName+"?"+query.toString());
	 return query;
}

 String getPath(HttpServletRequest request,String chartType){
	 StringBuffer query=getQueryPart(request);
	 query.append("&chartType="+chartType);
	 return "chartNew?"+query.toString(); 
 }
 
String getMapPath(HttpServletRequest request){
	 StringBuffer query=getQueryPart(request);
	 if(request.getParameter("googleMapParam")!=null){
		 query.append("&googleMapParam="+request.getParameter("googleMapParam"));
	 }
	 System.out.println("@@@@@@@@@:"+query.toString());
	 return "googleMap?"+query.toString(); 
 }



 %>
<%
if(request.getParameter("MI")==null || "BUSINESS_UNIT".equalsIgnoreCase(request.getParameter("MI"))) {%>  

<table  height="650px" border="0" align="center"  width="97%" >
 <tr>
 <td align="left" valign="top"><iframe height="325px" width="100%"  src="<%=getPath(request,CHART_TYPE.CASES_HANDLED_PER_PERIOD.toString())%>"   frameborder=0></iframe></td>
 <td align="left" valign="top" colspan="2"><iframe id=googleMapFrame  height="325px" width="100%" src="<%=getMapPath(request)%>"></iframe></td>
 </tr> 
  <tr>
  <td align="left" valign="top"><iframe height="325px" width="100%" src="<%=getPath(request,CHART_TYPE.ENERGY_PER_SQFT.toString())%>" ></iframe></td> 
 <td align="left" valign="top"><iframe height="325px" width="100%" src="<%=getPath(request,CHART_TYPE.ENERGY_AND_COST_PER_BUILDING.toString())%>" ></iframe></td>
 <td align="left" valign="top"><iframe height="325px" width="100%" src="<%=getPath(request,CHART_TYPE.ENERGY_PER_LOCATION.toString())%>" ></iframe></td> 
 </tr> 
 </table>

 <%-- <table  height="600px"  align="center" width="94%">
 <tr>
 <td><div id=chart1 style="border: 1px solud blue"><script type="text/javascript">$("#chart1").load('<%=getPath(request,CHART_TYPE.CASES_HANDLED_PER_PERIOD.toString())%>');</script></div></td>
 <td colspan="2"><div id=googlemap style="border: 1px solud blue;width:100%;height:300px"><script type="text/javascript">$("#googlemap").load('<%=request.getContextPath()%>/map.jsp');</script></div></td>
 </tr>
  <tr>
 <td><div id=chart2 style="border: 1px solud blue"><script type="text/javascript">$("#chart2").load('<%=getPath(request,CHART_TYPE.CASES_HANDLED_PER_PERIOD.toString())%>');</script></div></td>
 <td><div id=chart3 style="border: 1px solud blue"><script type="text/javascript">$("#chart3").load('<%=getPath(request,CHART_TYPE.CASES_HANDLED_PER_PERIOD.toString())%>');</script></div></td>
 <td align="right"><div id=chart4 style="border: 1px solud blue"><script type="text/javascript">$("#chart4").load('<%=getPath(request,CHART_TYPE.CASES_HANDLED_PER_PERIOD.toString())%>');</script></div></td>
 </tr> 
 </table> --%>
 <%}else{%>
 <table  border="0" height="600px" align="center"  width="94%">
 <tr>
  <td align="left" valign="top"><iframe height="325px" width="100%"  src="<%=getPath(request,CHART_TYPE.CASES_HANDLED_PER_PERIOD_PLANT.toString())%>"   frameborder=0></iframe></td>
 <td align="left" valign="top"><iframe height="325px" width="100%"  src="<%=getPath(request,CHART_TYPE.ENERGY_PER_CASES_HANDLED_PER_BUILDING.toString())%>"   frameborder=0></iframe></td>
  <td align="left" valign="top"><iframe height="325px" width="100%"  src="<%=getPath(request,CHART_TYPE.ENERGY_PER_BUILDING.toString())%>"   frameborder=0></iframe></td>
 </tr>
  <tr>
 <td align="left" valign="top"><iframe height="325px" width="100%" src="<%=getPath(request,CHART_TYPE.ENERGY_AND_COST_PER_BUILDING_PLANT.toString())%>" ></iframe></td>
 <td align="left" valign="top"><iframe height="325px" width="100%" src="<%=getPath(request,CHART_TYPE.ENERGY_AND_COST_PER_SQFT.toString())%>" ></iframe></td>
 <td align="left" valign="top"><iframe height="325px" width="100%" src="<%=getPath(request,CHART_TYPE.ENERGY_PER_LOCATION_PLANT.toString())%>" ></iframe></td>
 </tr>  
 </table>

 <%}%>
</body>
</html>