$(function() {
	$('#map-container').locationpicker({
		location : {
			latitude : $('#latitude').val(),
			longitude : $('#longitude').val()
		},
		enableAutocomplete : true,
		enableAutocompleteBlur : true,
		radius : 0,
		addressFormat : 'sublocality_level_2',
		inputBinding : {
			locationNameInput : $('#location'),
			latitudeInput : $('#latitude'),
			longitudeInput : $('#longitude')

		},
		autocompleteOptions : {
			componentRestrictions : {
				country : 'in'
			}
		}
	});
});