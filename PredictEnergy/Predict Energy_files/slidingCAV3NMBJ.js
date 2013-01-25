    $(document).ready(function() {

      $("#controlbtn").click(function(e) {
      
        e.preventDefault();
        
        var slidepx=$("div#linkblock").width() + 10;
    	var width=200;
    	if ( !$("div#maincontent").is(':animated') ) { 
        
			if (parseInt($("div#maincontent").css('marginLeft'), 10) < slidepx) {

				$("#controlbtn").attr('src','./images/toggle_minus.png');  
				$("#show_hide").html('Hide Controls');
      			margin = "+=" + slidepx;
      			
    		} else {
    			$("#controlbtn").attr('src','./images/toggle_plus.png');    			
    			
    			$("#show_hide").html('Show Controls');
      			margin = "-=" + slidepx;
      			width=300+50;

    		}
			
			alert("width"+width);
        	$("div#maincontent").animate({ 
        		marginLeft: margin
      		}, {
                    duration: 'slow',
                    easing: 'easeOutQuint'
                });
        	        	
        	$("#right").load("./pages/dashboard.jsp?"+
        			$.param(
        					{
        						width:width        						
        						
        					}
        	
        	) );  
        	
        	/*$("#right").load('./pages/dashboard.jsp?width='+$("#iframe1").width()+50);*/
    	
        	
    	} 


      }); 
   
    	
    	
      
      
      $(function() {
    	  $("#right").resizable({ minWidth: 100 },{ minHeight: 150 },{ maxWidth: 750 },{ maxHeight: 750 });
  	});
      
    
      
      $("#cssId").click(function(){
    	  
    	  $("#rightP").html("This is CSS");
    	  
      	});
      
      $("#phpId").click(function(){
	  		
    	  $("#rightP").html("This is PHP Paragraph");
    	  
      	});
      $("#resourcesId").click(function(){
	  		
    	  $("#rightP").html("This is RESOURCE Paragraph");
    	  
      	});
      $("#themesId").click(function(){
	  		
    	  $("#rightP").html("This is THEMES Paragraph");
    	  
      	});

});