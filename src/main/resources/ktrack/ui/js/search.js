$(function() {
 
	var activePanelId = '';
	
	var toggleSearchPanels = function(activeLabel) {
		var currentActivePanelId = activePanelId;		
		activePanelId = $(activeLabel).children('input')
				.attr('data-target');
		
		if(currentActivePanelId !== activePanelId) {
			$('.collapse').collapse("hide");
			$('#' + activePanelId).collapse("toggle");
		}
		

	}

	$('#search-by').find('label').on('click', function() {
		toggleSearchPanels(this);
	});

	toggleSearchPanels($('#search-by').find('label.active'));
});