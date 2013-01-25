<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.helio.boomer.rap.engine.PeriodListController"%>
<%@page import="com.helio.boomer.rap.engine.model.Period"%>
<%@page import="com.helio.boomer.rap.engine.model.BuildingAllocation"%>
<%@page import="com.helio.boomer.rap.engine.model.Building"%>
<%@page import="com.helio.boomer.rap.engine.model.Location"%>
<%@page import="com.helio.boomer.rap.engine.model.Division"%>
<%@page import="java.util.List"%>
<%@page import="com.helio.boomer.rap.engine.modellist.DivisionModelList"%>
<%@page import="com.helio.boomer.rap.engine.DivisionListController"%>
<html>
<head>
	<title>Predict Energy</title>
	<style type="text/css">


	
	</style>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/stdlib.js"></script>
	<!-- JSTree plugin files -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.cookie.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.hotkeys.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.jstree.js"></script> 
	
	<script type="text/javascript">
    $(function() {
        $('#sampleTree1').jstree();
        $('#sampleTree2').jstree({plugins : ["themes", "html_data", "ui", "hotkeys"]});
        
        $(".jstree-closed > a")
            .attr("aria-expanded", "false");
        $(".jstree-open > a")
            .attr("aria-expanded", "true");
    });
    </script>
	
	<script type="text/javascript">
	
	
	
	jQuery().ready(function(){
		// highly customized accordion
		jQuery('#listnavigation').accordion({
			event: 'mouseover',
			active: '.selected',
			selectedClass: 'active',
			animated: "bounceslide",
			header: "dt"
		});
	});

	
	</script>
</head>
<body>
<form onsubmit="callViewer()" action="./Viewer.jsp" name="menu" target="right" style="padding:0; margin:0;">
<!-- <img src="./images/BgLeftPan.png" /> -->

<input type="hidden" id=isUpdate>
<input type="hidden" id=divisions>
<input type="hidden" id=locations>
<input type="hidden" id=buildings>
<input type="hidden" id=buildingAllocations>
<input type="hidden" id=googleMapParam value="">

<div class="LeftSection">

<table cellpadding="0" cellspacing="0" width="280" align="center">
<tr><td class="LeftPanHead">Business Unit Navigation</td></tr>
<tr><td class="LeftPanBody" >
<%
String googleMapParam=null;
%>
<div id="sampleTree2"  style="width:260px; height:340px; overflow:scroll;">
  
					<UL>
					<%    DivisionModelList list=DivisionListController.getInstance().getDivisionModelList();
						  List<Division> divisionList=list.getDivisionList();
						  for(int i=0;divisionList!=null && i<divisionList.size();i++){
						  Division division=divisionList.get(i);
						  List<Location> locations=division.getLocations();
						  if(i==0){
								googleMapParam="'"+division.getId()+"|NULL|NULL|NULL|DIVISION'";
						  }
					%>
					
					<LI onclick="lastSelected('<%=division.getId()%>|NULL|NULL|NULL|DIVISION')" id='<%=division.getId()%>'><a href="#"><%=division.getDivisionName()%></a>
					               <UL>
					                    <%for(int j=0;j<locations.size();j++){
					                    	Location location=locations.get(j);
					                    	List<Building> buildings=location.getBuildings();
					                    %>  
					                    <LI onclick="lastSelected('<%=division.getId()%>|<%=location.getId()%>|NULL|NULL|LOCATION')" id='<%=location.getId()%>'><a href="#"><%=location.getLocationName()%></a>
					               
					                     <UL>
					                     <%for(int k=0;k<buildings.size();k++){
					                    	Building building=buildings.get(k);      
					                    	List<BuildingAllocation> buildingAllocations=building.getBuildingAllocations();
					                      %> 
					                      <LI onclick="lastSelected('<%=division.getId()%>|<%=location.getId()%>|<%=building.getId()%>|NULL|BUILDING')" id='<%=building.getId()%>'><a href="#"><%=building.getBuildingName()+" < "+ building.getPercentageLocationSquareFeet()+" > "%></a>
					                      <UL><%for(int l=0;l<buildingAllocations.size();l++){
					                    	BuildingAllocation buildingAllocation=buildingAllocations.get(l);%>
					                    	<LI onclick="lastSelected('<%=division.getId()%>|<%=location.getId()%>|<%=building.getId()%>|<%=buildingAllocation.getId()%>|BULDING_ALLOCATION')" id='<%=buildingAllocation.getId()%>'><a href="#"><%=buildingAllocation.getName()%></a></LI>      
					                      <%}%>
					                      </UL>
					                      </LI>
					                      <%}%>
					                      </UL>
					                    </LI>
					                    <%}%>
					               </UL>
					            </LI>
					         
					  
					 <%}%>
					 </UL>
 				</div></td></tr>
 			
<tr><td>&nbsp;</td></tr>
<tr><td class="LeftPanHead">Graphs Control</td></tr>
<tr><td class="LeftPanBody"> <div style="border: 1px solid white;padding:0px">
				<table class="helios_controls">
				<tr><td><input type="checkbox" name="displaLabelsCheckbox" id="displaLabelsCheckbox" checked="checked" ><label>Display Axes Labels</label></td><td><input type="checkbox" name="displayTitleCheckbox" id="displayTitleCheckbox" checked="checked" > <label>Display Title</label></td></tr>
					<tr><td><input type="checkbox" name="displayLegendsCheckbox" id="displayLegendsCheckbox" checked="checked" > <label>Display Legends</label></td><td>Cost Allocation: $0.15</td></tr></table>

<input type="hidden" name="displayLabels" id="displayLabels">
<input type="hidden" name="displayTitle" id="displayTitle">
<input type="hidden" name="displayLegends" id="displayLegends">
<input type="hidden" name="buildingAllocationId" id="buildingAllocationId">
<input type="hidden" name="pageName" id="pageName" value="defaultViewer.jsp">

				
			</div> </td></tr>
			
<tr><td>&nbsp;</td></tr>
<tr><td class="LeftPanHead">Distribution (Loc)</td></tr>
<tr><td class="LeftPanBody">

<%
List<Period> periods=Arrays.asList(PeriodListController.getInstance().getPeriodModelListAsArray());
Period period=null;
SimpleDateFormat df=new SimpleDateFormat("MM/dd/yyyy");

	
%>

 <div>
				
					<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="helios_controls">
						<tr>
						
						<td style="border-right: 1px solid lightgray">Date</td>
						<td><label>Start</label></td>
						<td><select id="startDate">
						<%for(int i=0;i<periods.size();i++)
						{
							
							period=periods.get(i);
							String date=df.format(period.getStartDt());
						%>
						<option value="<%=date%>"><%=date%></option>
						<%}%>	
						
						</select> 
						</td>
						<td><label>End:</label></td>
						<td>
						 <select id="endDate">
						<%for(int i=0;i<periods.size();i++)
						{
							
							period=periods.get(i);
						    String date=df.format(period.getEndDt());
						%>
						<option value="<%=date%>"><%=date%></option>
						<%}%>	
						</select> </td>
						
						</tr>
						
						<tr>
						<td style="border-right: 1px solid lightgray">Period</td>
						<td><label>Start:</label></td>
						<td>
						<select id="startPeriod"	>
							<%for(int i=0;i<periods.size();i++)
						{
							
							period=periods.get(i);
						%>
						 <option value="<%=period.getId()%>"><%=period.getPeriodName()%></option>
						<%}%>
							
						</select> 
						</td>
						<td><label>End:</label></td>
						<td>
						<select id="endPeriod"	>
							<%for(int i=0;i<periods.size();i++)
						{
							
							period=periods.get(i);
						%>
						 <option value="<%=period.getId()%>"><%=period.getPeriodName()%></option>
						<%}%>
							
						</select> 
						</td>
						
						</tr>
						
						<tr><td colspan="3">
						<input type="button" id="updateId" value="Update" >
						</td></tr>
						</table>
				
				
			</div> </td></tr>
			
			<tr><td>&nbsp;</td></tr>

</table>

</div>







<script type="text/javascript">

	    
	    $(document).ready(function() {

	    	  $("#startPeriod").change(function() {
	    	    
	    		 $("#endPeriod").val($(this).val());	    		 
	    		 $("#startDate")[0].selectedIndex=$("#startPeriod option:selected").index();
	    		 $("#endDate")[0].selectedIndex=$("#endPeriod option:selected").index();
	    		 
	    	   });
	    	  	$("#endPeriod").change(function() {	    	    
		    		    		 
		    		 $("#endDate")[0].selectedIndex=$("#endPeriod option:selected").index();
		    		 
		    	   });
	    	
	    	
         $("#updateId").click(function(){
        	 
        	 var displayTitle=true;
         	var displayLabels=true;
         	var displayLegends=true;
         	
         	if($("#displayTitleCheckbox").is(':checked'))
      		{
         		displayTitle=true;
      		}else{
      			displayTitle=false;
      		}
      	    if($("#displaLabelsCheckbox").is(':checked'))
      		{
      	    	displayLabels=true;
      		}else{
      			displayLabels=false;
      		}
      	    
      	    if($("#displayLegendsCheckbox").is(':checked'))
      		{
      	    	displayLegends=true;
      		}else{
      			displayLegends=false;
      		}  
        	 
     	   
      	    
      	  
      	  var divisionIds = new Array();
      	var locationIds = new Array();
      	var buildingIds = new Array();
      	var buildingAllocationIds = new Array();
      	var index=0;
      	var x = $('.jstree-clicked');
      	
          $.each(x,function(i,v){
          	var allIds = new Array();
          	var count = 0;
          	var allParents = $(v).parents('li');
          	
          	$.each(allParents,function(j,item){
          		allIds[count]= $(item).attr('id');
          		count++;
          	}) 
          	
          	if(allIds.length == 1){
          		divisionIds[index]= allIds[0];
          	}else if(allIds.length == 2){
          		locationIds[index]= allIds[0];
              	divisionIds[index]= allIds[1];
          	}else if(allIds.length == 3){
          		buildingIds[index] = allIds[0];
              	locationIds[index]= allIds[1];
              	divisionIds[index]= allIds[2];
          	}else if(allIds.length == 4){
          		buildingAllocationIds[index] = allIds[0];
          		buildingIds[index] = allIds[1];
              	locationIds[index]= allIds[2];
              	divisionIds[index]= allIds[3];
          	}
          	index++;
          })
          
          /* buildingAllocationIds = buildingAllocationIds.unique();
          buildingIds = buildingIds.unique();
          locationIds = locationIds.unique();
          divisionIds = divisionIds.unique(); */
          
          
          buildingAllocationIds = getRectifiedString(buildingAllocationIds);
          buildingIds = getRectifiedString(buildingIds);
          locationIds = getRectifiedString(locationIds);
          divisionIds = getRectifiedString(divisionIds);
          
                   
          
          
        var startDate=$("#startDate").val();
        var endDate=$("#endDate").val();    
      	var startPeriod=$("#startPeriod").val();
      	var endPeriod=$("#endPeriod").val();
      	 var foo=false;	
      	
      	if($("#divisions").val()==divisionIds 
      	&& $("#locations").val()==locationIds 
      	&& $("#buildings").val()==buildingIds 
      	&& $("#buildingAllocations").val()==buildingAllocationIds)
      	{      		
                foo=true;
      	}
      	
      	$("#isUpdate").val('true'); 	
      	$("#divisions").val(divisionIds); 
      	$("#locations").val(locationIds); 
      	$("#buildings").val(buildingIds); 
      	$("#buildingAllocations").val(buildingAllocationIds); 
      	 
      	 
      	var googleMapParam=$("#googleMapParam").val();
     	    
     	    
        	 
        	$("#right").load("<%=request.getContextPath()%>/pages/dashboard.jsp?"+
        			$.param(
        					{        						
        						resize:foo,
        						width:250,
        						MI:<%="'"+request.getParameter("MI")+"'"%>,
        						displayTitle:displayTitle,
        						displayLabels:displayLabels,
        						displayLegends:displayLegends,        						
        						divisions:divisionIds,
        						locations:locationIds,
        						buildings:buildingIds,
        						buildingAllocations:buildingAllocationIds,        						
        						startDate:startDate,
        						endDate:endDate,
        						startPeriod:startPeriod,
        						endPeriod:endPeriod,
        						googleMapParam:googleMapParam
        					}
        	
        	) );  

         });
         
         
         function getRectifiedString(array){
         	array = array.unique();
             array = array.clean(undefined);
             var rectifiedString = array.join("|");
         	return rectifiedString;
         }
            
 		
     	function print(allIds){
     		for (var j=0;j<allIds.length;j++){
 	    		//var value = $(allIds[j]).attr('id');
 	    		var value = allIds[j];
 	    		console.log("j= "+j+" value = "+ value);
 	    	}
     	}
         
         
      });    

	    
	    Array.prototype.unique =
	    	  function() {
	    	    var a = [];
	    	    var l = this.length;
	    	    for(var i=0; i<l; i++) {
	    	      for(var j=i+1; j<l; j++) {
	    	        // If this[i] is found later in the array
	    	        if (this[i] === this[j])
	    	          j = ++i;
	    	      }
	    	      a.push(this[i]);
	    	    }
	    	    return a;
	    	  };




 function valuate(){
	var txt='';
	var buildingCheckboxes=document.getElementsByName('buildingCheckbox')
	var foo=false;
	for (var i_tem = 0; i_tem < buildingCheckboxes.length; i_tem++)
	if (buildingCheckboxes[i_tem].checked){
		foo=true;
	txt+=','+buildingCheckboxes[i_tem].value;
	}
	txt=txt.replace(/^,/,'');
	document.getElementById('buildingAllocationId').value=txt;
	return foo;
	}
	 function lastSelected(x)
	 {
		 $("#googleMapParam").val(x); 
		 
		 var gMap=$("#googleMapParam").val();
		 var unit= '<%=request.getParameter("MI")!=null?request.getParameter("MI"):"BUSINESS_UNIT"%>';
	    <%-- if(unit=='BUSINESS_UNIT'){
		    $("#googleMapFrame").load("<%=request.getContextPath()%>/googleMap?"+
	    			$.param(
	    					{        						
	    						MI:unit,
	    						googleMapParam:gMap
	    					}
	    	
	    	) );  
	    } --%>
		    
		 
		 
	    	
	 }
	 
	 $("#googleMapParam").val(<%=googleMapParam%>);
	 
</script>
</form>



</body>

</html>