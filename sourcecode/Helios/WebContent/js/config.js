document.documentElement.className += ' js';

(function($){
	$('ul#checkboxes input[type="checkbox"]').ajaxComplete().each (
		function () {
			$(this).bind('click change', function (){
				if($(this).is(':checked')) {
					$(this).siblings('ul').find('input[type="checkbox"]').attr('checked', 'checked');
					$(this).parents('ul').siblings('input[type="checkbox"]').attr('checked', 'checked');
                } else {
                    $(this).siblings('ul').find('input[type="checkbox"]').removeAttr('checked', 'checked');
                }
			});
		}
	);
	
})(jQuery);