<%@page import="org.bcje.model.CHART_TYPE"%>
<%!
 
 String getPath(HttpServletRequest request,String chartType){
	 String pageName=request.getParameter("pageName")!=null?request.getParameter("pageName"):"defaultViewer.jsp";
	 String period=request.getParameter("period");
	 String displayTitle=request.getParameter("displayTitle");
	 String buildingAllocationId=request.getParameter("buildingAllocationId");
	 String displayLabels=request.getParameter("displayLabels");
	 String displayLegends=request.getParameter("displayLegends");
	 StringBuffer query=new StringBuffer();
	 if(request.getParameter("pageName")!=null)
	 {		
	 query.append("&period="+period);
	 query.append("&displayTitle="+displayTitle);
	 query.append("&buildingAllocationId="+buildingAllocationId);
	 query.append("&displayLabels="+displayLabels);
	 query.append("&displayLegends="+displayLegends);
	 
	
	 
	 }
	  
	 query.append("&chartType="+chartType);
	 //System.out.println("In DASHBOARD"+request.getParameter("chartWidth"));
	 if(request.getParameter("width")!=null)
	 query.append("&width="+request.getParameter("width"));
	 if(request.getParameter("resize")!=null)
		 query.append("&resize="+request.getParameter("resize"));
	 //System.out.println(request.getContextPath()+"/"+pageName+"?"+query.toString());
	 return "chartNew?"+query.toString(); 
 }

 %>
 <div id=chart2 style="border: 1px solud blue"><script type="text/javascript">$("#chart2").load('<%=getPath(request,CHART_TYPE.CASES_HANDLED_PER_PERIOD.toString())%>');</script></div>