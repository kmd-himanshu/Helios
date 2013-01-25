    $(document).ready(function() {

     $("#controlbtn").click(function(e) {
      
        e.preventDefault();
        
        var slidepx=$("div#linkblock").width() + 0;
    	var width=280;
    	var googleMapParam=$("#googleMapParam").val();
    	if ( !$("div#maincontent").is(':animated') ) { 
        
			if (parseInt($("div#maincontent").css('marginLeft'), 10) < slidepx) {

				$("#controlbtn").attr('src','./images/BtnHide.png');  				
      			margin = "+=" + slidepx;
      			
    		} else {
    			$("#controlbtn").attr('src','./images/BtnShow.png');    			
    			
    			
      			margin = "-=" + slidepx;
      			width=280+100;

    		}
			
			
        	$("div#maincontent").animate({ 
        		marginLeft: margin
      		}, {
                    duration: 'slow',
                    easing: 'easeOutQuint'
                });
        	
        	var displayTitle=true;
        	var displayLabels=true;
        	var displayLegends=true;
        	
        	if($("#isUpdate").val()=='true'){
        	
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
	     	    
	     	   var startDate=$("#startDate").val();
	           var endDate=$("#endDate").val();    
	           var startPeriod=$("#startPeriod").val();
	           var endPeriod=$("#endPeriod").val();
	           
	            var divisionIds= $("#divisions").val(); 
	         	var locationIds=$("#locations").val(); 
	         	var buildingIds=$("#buildings").val(); 
	         	var buildingAllocationIds=$("#buildingAllocations").val(); 
	           
	     	   
     	        	  
	     	   $("#right").load("./pages/dashboard.jsp?"+
	        			$.param(
	        					{
	        						width:width,        						
	        						resize:true,
	        						MI:$("#MI").val(),
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
        	}else{
        		
        		$("#right").load("./pages/dashboard.jsp?"+
            			$.param(
            					{
            						width:width,        						
            						resize:true,
            						MI:$("#MI").val(),
            						displayTitle:displayTitle,
            						displayLabels:displayLabels,
            						displayLegends:displayLegends,
            						googleMapParam:googleMapParam
            					}
            	
            	) );  
        	}
                	    	
        	
        	
        	
    	
        	
    	} 


      }); 
    	
    	

});