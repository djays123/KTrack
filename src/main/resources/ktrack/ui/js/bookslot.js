$(function() {
	
/*	var eventData = [
		  {"date":"2017-12-12","badge":true, modal: true, "title":"Example 1",},
		  {"date":"2017-12-13","badge":true,modal: true, "title":"Example 2"}
	];
	
	
	$("#slot-booking-calendar").zabuto_calendar({
		language : "en",
		disableNoEvents : true, 
		show_previous : false,
		show_next : 1,
		cell_border: true,
	    today: false,
	    data: eventData,
	    eventTrigger : "day:click",
	    eventTarget : "slot-booking-calendar-events"
	}); */
	
	$("#slot-booking-calendar-events").on("day:click", function(event, date) {
		$('#booking-preview').modal({keyboard:true, show:true});	
	})
});