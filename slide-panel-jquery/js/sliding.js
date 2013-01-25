    $(document).ready(function() {

      $("a#controlbtn").click(function(e) {
      
        e.preventDefault();
        
        var slidepx=$("div#linkblock").width() + 10;
    	
    	if ( !$("div#maincontent").is(':animated') ) { 
        
			if (parseInt($("div#maincontent").css('marginLeft'), 10) < slidepx) {

     			$(this).removeClass('close').html('Hide your stuffs');

      			margin = "+=" + slidepx;

    		} else {

     			$(this).addClass('close').html('Show your stuffs');

      			margin = "-=" + slidepx;
      		

    		}
    	
        	$("div#maincontent").animate({ 
        		marginLeft: margin
      		}, {
                    duration: 'slow',
                    easing: 'easeOutQuint'
                });
    	
    	
    	} 


      }); 
      
      
      $(function() {
    	  $("#rightDiv").resizable({ minWidth: 100 },{ minHeight: 150 },{ maxWidth: 600 },{ maxHeight: 600 });
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