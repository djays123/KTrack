$(function() {
	var commentsBottom = $('#comments').offset().top + $('#comments').innerHeight();
	$('#map-container').height(commentsBottom - $('#map-container').offset().top);
	
});