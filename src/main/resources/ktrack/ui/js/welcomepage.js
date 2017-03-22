$(function() {
	$('#map-container').locationpicker({
		location : {
			latitude : 18.52895184,
			longitude : 73.87434160
		},
		enableAutocomplete : true,
		enableAutocompleteBlur : true,
		radius : 0,
		addressFormat : 'sublocality_level_2',
		inputBinding : {
			locationNameInput : $('#location')

		},
		autocompleteOptions : {
			componentRestrictions : {
				country : 'in'
			}
		}
	});

	var myDropzone = new Dropzone('div#images',{		
		url : '/',
		paramName : 'images[]',
		addRemoveLinks : true,
		acceptedFiles : 'image/*',
		dictDefaultMessage: 'Drop or click to upload pictures',	
		clickable: '#images',
		autoProcessQueue: false,
		uploadMultiple: true
	});
});