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
	
	$("div#images").css({
	    'top': $('#images_shim').position().top,
	    'left': $('#images_shim').position().left
	});

	Dropzone.autoDiscover = false;
	var url = "/";
	var pictureDropzone = new Dropzone('div#images',{		
		url : function() { return $('#upload-file-form').prop('action'); },
		paramName : 'images[]',
		addRemoveLinks : true,
		acceptedFiles : 'image/*',
		dictDefaultMessage: '',			
		uploadMultiple: true,
		parallelUploads : 3,
		maxFiles: 3
	});
	
	var updateImageShim = function() {
		setTimeout(function() {
			var filesExist = pictureDropzone.getAcceptedFiles().length > 0;
			$('#images_shim').val(filesExist ? ' ' : '');	
        }, 0);
		
	};
	
	var filePreviewKey = "";
	pictureDropzone.on("addedfile", function(file) {
		updateImageShim();
		file.previewElement.addEventListener("click", function() {
		    if(file.fileKey) {		  
		    	if(file.fileKey !== filePreviewKey) {
		    		$('#image-file-key').val(file.fileKey);
		    		$('#image-preview-form').trigger('submit');
		    	}
		    	$('#image-preview').modal({keyboard:true, show:true});	
		    	filePreviewKey = file.fileKey;
		    }
		});
	 });
	
	pictureDropzone.on("removedfile", function(file) {
		updateImageShim();
		if(file.fileKey) {
			$('#deleted-file-key').val(file.fileKey);
			$('#delete-uploaded-file-form').trigger('submit');
		}
	 });
	
	pictureDropzone.on("error", function(file) {
		if (!file.accepted)
			this.removeFile(file);
	});
	
	pictureDropzone.on("successmultiple", function(files, responseText, e) {
		var fileKeys = responseText.files;
		$.each(fileKeys, function( index, fileKey ) {
			var fileKeyId = 'file_key_' + fileKey;
			$('<input>').attr({
			    type: 'hidden',
			    id: fileKeyId,
			    name: fileKeyId,
			    value : fileKey
			}).appendTo('#save-dog-form');
			files[index].fileKey = fileKeyId;		
			
		});
	});
	
	pictureDropzone.on("removedfile", function(file) {
		if(file.fileKey) {
			$('#' + file.fileKey).remove();
		}		
	});
	
	var commentsBottom = $('#comments').offset().top + $('#comments').innerHeight();
	$('#map-container').height(commentsBottom - $('#map-container').offset().top);
	
});