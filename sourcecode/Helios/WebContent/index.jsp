<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<TITLE>:: Predict Energy ::</TITLE>
		<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
		
			<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		
			
<!-- sliding featers -->
			
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/sliding.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/js/jqueryui1.8.23/css/ui-lightness/jquery-ui-1.8.23.custom.css" />
<script type='text/javascript'
	src='<%=request.getContextPath()%>/js/jqueryui1.8.23/js/jquery-1.8.0.min.js'></script>

<script type='text/javascript' src='<%=request.getContextPath()%>/js/sliding.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/js/easing.js'></script>
<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap.css" /> --%>
<!-- end sliding featers -->

<!-- accordion plugin files -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/chili-1.7.pack.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.easing.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dimensions.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.accordion.js"></script>
	
	


	</head>
<BODY class="AlignBody"  >
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="MainTable" ID="Table1">
		<tr>
		<td align="center" valign="top">
			<table height="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="BodyTable" ID="Table2">
		<!-- ***************************** HEADER STARTS HERE ******************************************** -->
		<tr> 
          <td class="HeaderSection"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td align="left">&nbsp;</td>
                <td align="right"><img src="images/Logo.jpg"></td>
            </tr>
          </table></td>
        </tr>
		<!-- ***************************** HEADER ENDS HERE ******************************************** -->
		
		
		<!-- ***************************** MENU STARTS HERE ******************************************** -->
        <tr> 
          <td class="BgMainMenu"><table width="100%" height="40" border="0" cellpadding="0" cellspacing="0">
              <tr><td width="425" align="left"><div id='MenuPos' style='position:relative;  height:40px; margin:0; padding:0;'></div></td>
                <td align="center" class="MenuEnd"></td>
                <td align="right">&nbsp;</td>
              </tr>
            </table></td>
		</tr>
		<!-- ***************************** MENU ENDS HERE ******************************************** -->
			<tr><td class="BgTitleBar"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td align="left" class="PageHeading" width="200px">
                <%if(request.getParameter("MI")==null || "BUSINESS_UNIT".equalsIgnoreCase(request.getParameter("MI"))) {%>  
                Business Unit Dashboard
                <%}else{%>
                Plant Unit Dashboard
                <%}%>
                
                </td>
                <td align="left">
                               <img alt="Preview selected inputs" id="filterPopup" src="images/filter.jpeg" width="18" height="18">
                </td>
                <td align="right"><table border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>&nbsp;</td>
                    <td><a href="#"><img src="images/IconEnterprise.png" alt="Enterprise" border="0"></a></td>
                    <td width="10">&nbsp;</td>
                    <td><a href="#"><img src="images/IconBusinessUnit.png" alt="Business Unit" border="0"></a></td>
					  <td width="10">&nbsp;</td>
                    <td><a href="#"><img src="images/IconPlant.png" alt="Plant" border="0"></a></td>
					 <td width="10">&nbsp;</td>
                    <td><a href="#"><img src="images/IconOperations.png" alt="Operations" border="0"></a></td>
                  </tr>
                </table></td>
              </tr>
            </table></td>
			</tr>
			<tr>
			  
              <td valign="top" align="center" class="PageBody"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  
                  <td align="left" width="100%"><!-- <button id=hide>Hide Animation</button>
<button id=show>Show Animation</button> -->

                  </td>
                </tr>
                <tr>
                  <td align="left"><div id="maincontent" >
 <div class="sidebar1"  id="linkblock"><%String viewPage=request.getParameter("MI")!=null?request.getParameter("MI"):"BUSINESS_UNIT"; %>
                  <input type="hidden" value="<%=viewPage%>" id="MI">
                 <jsp:include  page="./pages/controls/unitNavigationControl.jsp" >
                 <jsp:param value="<%=viewPage%>" name="MI"/>
                 </jsp:include> </div>
    <div class="sidebar2"><img  src="./images/BtnHide.png" id="controlbtn" /></div>
        <div class="RightContent" id='right'><script type="text/javascript">var gMap=$("#googleMapParam").val();
        var unit= '<%=request.getParameter("MI")!=null?request.getParameter("MI"):"BUSINESS_UNIT"%>';
    	$("#right").load("<%=request.getContextPath()%>/pages/dashboard.jsp?"+
    			$.param(
    					{        						
    						MI:unit,
    						googleMapParam:gMap
    					}
    	
    	) );  </script></div> 

 </div></td>
                </tr>
                
              </table></td>
			</tr>
			
			<!-- ***************************** FOOTER STARTS HERE ******************************************** -->
       		<tr> 
          	<td class="footer">Copyright © 2012. All Rights Reserved. </td>
        	</tr>
		<!-- ***************************** FOOTER ENDS HERE ******************************************** -->
			</table>
		</td>
		</tr>
		</table>
<script type='text/javascript'>function Go(){return}</script>
<script type='text/javascript' src='<%=request.getContextPath()%>/js/RelativePositioned_var.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/js/menu132_compact.js'></script>
<script type="text/javascript">

$('#filterPopup').click(function(){ 
	
	$('#selectedIds').val("");
	var index=0;
	var x = $('.jstree-clicked');
	var levelArray = new Array();
    $.each(x,function(i,v){
    	
    	var allIds = new Array();
    	var count = 0;
    	
    	var allParents = $(v).parents('li');
    	
    	$.each(allParents,function(j,item){
    		var itemValue = $(item).children('a').text();
    		allIds[count]= itemValue;
    		count++;
    	}) 
    	var levelStatus = new Object();
    	var allItemsLen = allIds.length;
    	
    	if(allItemsLen == 1){
    		levelStatus.first = allIds[0];
    	}else if(allItemsLen == 2){
    		levelStatus.first = allIds[1];
           	levelStatus.second = allIds[0];
    	}else if(allItemsLen == 3){
    		levelStatus.first = allIds[2];
           	levelStatus.second = allIds[1];
           	levelStatus.third = allIds[0];
    	}else if(allItemsLen == 4){
    		levelStatus.first = allIds[3];
           	levelStatus.second = allIds[2];
           	levelStatus.third = allIds[1];
           	levelStatus.fourth = allIds[0];
    	}
       	levelArray[index] = levelStatus;
       	index++;
    })
    createLevelTree(levelArray);
    
}); 

function getRectifiedArray(array){
	array = array.unique();
    array = array.clean(undefined);
	return array;
}

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}


function isValueExistinArray(array,itemValue){
	var isValueExist = false;
	for(var i=0;i<array.length;i++){
		if(array[i].node.value == itemValue){
			isValueExist = true;
			break;
		}
	}
	return isValueExist;
}

function isExistInArray(array,item){
	for(var i=0;i<array.length;i++){
		var node = array[i];
		if(item == node.value){
			return true;
		}
	}
}

function createLevelTree(levelArray){
	
	var firstLevelTree = new Array();
	var firstIndex = 0;
	for(var i=0;i<levelArray.length;i++){
		var first = levelArray[i].first;
		if(first != undefined && !isExistInArray(firstLevelTree,first)){
			var node = new Object();
			node.value = first;
			firstLevelTree[firstIndex] = node;
			firstIndex++;
		}
	}
	
	for(var i=0;i<firstLevelTree.length;i++){
		var node = firstLevelTree[i];
		var secondLevelTree = getAllSecondsFor(node.value,levelArray);
		node.pointer = secondLevelTree;
		
		for(var j=0;j<secondLevelTree.length;j++){
			var node2 = secondLevelTree[j];
			var thirdLevelTree = getAllThirdsFor(node2.value,levelArray);
			node2.pointer = thirdLevelTree;
			
			for(var k=0;k<thirdLevelTree.length;k++){
				var node3 = thirdLevelTree[k];
				var fourthLevelTree = getAllFourthsFor(node3.value,levelArray);
				node3.pointer = fourthLevelTree;
				
			}
		}
		
	}
	
	writeToDialogTree(firstLevelTree);
}

function writeToDialogTree(firstLevelTree){
	var levelString = "<div class=\"LeftSection\">";
	levelString=levelString.concat("<table cellpadding=\"0\" cellspacing=\"0\" width=\"280\" align=\"center\">");
	levelString=levelString.concat("<tr><td class=\"LeftPanHead\">Unit Navigation</td></tr>");
	levelString=levelString.concat("<tr><td class=\"LeftPanBody\" >");
	
	levelString = levelString.concat("<ul class='filterPopup'>");
	for(var i=0;i<firstLevelTree.length;i++){
		var node = firstLevelTree[i];
		levelString = levelString.concat("<li>"+node.value+"</li>");
		var secondLevelTree = node.pointer;
		
		for(var j=0;j<secondLevelTree.length;j++){
			levelString = levelString.concat("<ul>");
			var node2 = secondLevelTree[j];
			
    		levelString = levelString.concat("<li>"+node2.value+"</li>");
    		var thirdLevelTree = node2.pointer;
			
    		
    		for(var k=0;k<thirdLevelTree.length;k++){
    			levelString = levelString.concat("<ul>");
    			var node3 = thirdLevelTree[k];
    			
        		levelString = levelString.concat("<li>"+node3.value+"</li>");
        		var fourthLevelTree = node3.pointer;
        		for(var l=0;l<fourthLevelTree.length;l++){
        			levelString = levelString.concat("<ul>");
        			var node4 = fourthLevelTree[l];
        			
            		levelString = levelString.concat("<li>"+node4.value+"</li>");
            		levelString = levelString.concat("</ul>");
        		}
        		levelString = levelString.concat("</ul>");
    		}
    		levelString = levelString.concat("</ul>");
		}
	}
	levelString = levelString.concat("</ul></td></tr>");
	levelString = levelString.concat("<tr><td>&nbsp;</td></tr>");
	
	levelString = levelString.concat(getDateString()); 
	
	levelString = levelString.concat(getGraphControlString());
	levelString = levelString.concat("</table></div>");
	
	$('#selectedIds').html(levelString);
    $('#dialogHelios').dialog({ modal: true },{ minWidth: 320 },{title : "Dashboard Filters"},{ show: { effect: 'drop', direction: "up" }},{resizable: false});
	
}

function getGraphControlString(){
	var graphString = "";
	graphString=graphString.concat("<tr><td class=\"LeftPanHead\">Graphs Control</td></tr>");
	graphString=graphString.concat("<tr><td class=\"LeftPanBody\" >");
	graphString=graphString.concat("<table>");
	graphString = graphString.concat("<tr><td>Cost Allocation:</td><td>$0.15</td></tr>");
	graphString = graphString.concat("<tr><td>Display Axes Labels:</td><td>"+$("#displaLabelsCheckbox").is(':checked')+"</td></tr>");
	graphString = graphString.concat("<tr><td>Display Title:</td><td>"+$("#displayTitleCheckbox").is(':checked')+"</td></tr>");
	graphString = graphString.concat("<tr><td>Display Legends:</td><td>"+$("#displayLegendsCheckbox").is(':checked')+"</td></tr>");
	graphString=graphString.concat("</table>");
	graphString = graphString.concat("</td></tr>");
	graphString = graphString.concat("<tr><td>&nbsp;</td></tr>");
	return graphString;
}

function getDateString(){
	var dateStr = "";
	dateStr=dateStr.concat("<tr><td class=\"LeftPanHead\">Distribution (Loc)</td></tr>");
	dateStr=dateStr.concat("<tr><td class=\"LeftPanBody\" >");
	dateStr=dateStr.concat("<table>");
	dateStr = dateStr.concat("<tr><td>Start Date:</td><td>"+$("#startDate").val()+"</td></tr>");
	dateStr = dateStr.concat("<tr><td>End Date:</td><td>"+$("#endDate").val()+"</td></tr>");
	dateStr = dateStr.concat("<tr><td>Start Period:</td><td>"+$("#startPeriod option:selected").text()+"</td></tr>");
	dateStr = dateStr.concat("<tr><td>End Period:</td><td>"+$("#endPeriod option:selected").text()+"</td></tr>");
	dateStr=dateStr.concat("</table>");
	dateStr = dateStr.concat("</td></tr>");
	dateStr = dateStr.concat("<tr><td>&nbsp;</td></tr>");
	return dateStr; 
}

function getAllThirdsFor(second,levelArray){
	var thirdLevelTree = new Array();
	var thirdIndex = 0;
	for(var i=0;i<levelArray.length;i++){
		if(second == levelArray[i].second){
			var third = levelArray[i].third;
			if(third != undefined && !isExistInArray(thirdLevelTree,third)){
				var node = new Object();
				node.value = third;
				thirdLevelTree[thirdIndex] = node;
				thirdIndex++;
			}
		}
	}
	
	return thirdLevelTree;
}

function getAllFourthsFor(third,levelArray){
	var fourthLevelTree = new Array();
	var fourthIndex = 0;
	for(var i=0;i<levelArray.length;i++){
		if(third == levelArray[i].third){
			var fourth = levelArray[i].fourth;
			if(fourth != undefined && !isExistInArray(fourthLevelTree,fourth)){
				var node = new Object();
				node.value = fourth;
				fourthLevelTree[fourthIndex] = node;
				fourthIndex++;
			}
		}
	}
	
	return fourthLevelTree;
}

function getAllSecondsFor(first,levelArray){
	var secondLevelTree = new Array();
	var secondIndex = 0;
	for(var i=0;i<levelArray.length;i++){
		if(first == levelArray[i].first){
			var second = levelArray[i].second;
			if(second != undefined && !isExistInArray(secondLevelTree,second)){
				var node = new Object();
				node.value = second;
				secondLevelTree[secondIndex] = node;
				secondIndex++;
			}
		}
	}
	
	return secondLevelTree;
}

function loadDashboard()
{
	var gMap=$("#googleMapParam").val();
	$("#right").load("<%=request.getContextPath()%>/pages/dashboard.jsp?"+
			$.param(
					{        						
						MI:<%=request.getParameter("MI")!=null?request.getParameter("MI"):"BUSINESS_UNIT"%>,
						googleMapParam:gMap
					}
	
	) );  
	
	
}

</script>
<div id="dialogHelios" title="Dialog Title" class="look" style="display:none"><p id="selectedIds"></p></div>
	</BODY>
	
</html>
