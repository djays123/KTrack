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
	 });
	
	pictureDropzone.on("error", function(file) {
		if (!file.accepted)
			this.removeFile(file);
	});
	
	var fileAdded = function(file, fileKey) {
		var fileKeyId = 'file_key_' + fileKey;
		$('<input>').attr({
		    type: 'hidden',
		    id: fileKeyId,
		    name: fileKeyId,
		    value : fileKey
		}).appendTo('#save-dog-form');
		file.fileKey = fileKeyId;		
	}
	
	pictureDropzone.on("successmultiple", function(files, responseText, e) {
		var fileKeys = responseText.files;
		$.each(fileKeys, function( index, fileKey ) {
			fileAdded(files[index], fileKey);
			
		});
	});
	
	pictureDropzone.on("removedfile", function(file) {
		if(file.fileKey) {
			$('#' + file.fileKey).remove();
		}		
	});
	
	var commentsBottom = $('#comments').offset().top + $('#comments').innerHeight();
	$('#map-container').height(commentsBottom - $('#map-container').offset().top);
	
	// For an existing dog show the thumbnails in dropzone
	if(window.dogData && window.dogData.imageData) {
		var existingFileCount = 0; // The number of files already uploaded
		
		$.each(window.dogData.imageData, function(index, imageData) {
			// Create the mock file:
			var mockFile = { name: imageData.fileName, size: imageData.fileSize, accepted: true, existing: true};

		
			// Call the default addedfile event handler
			pictureDropzone.emit("addedfile", mockFile);
			
			pictureDropzone.createThumbnailFromUrl(mockFile, imageData.snapshotURL, null);

			
			// Make sure that there is no progress bar, etc...
			pictureDropzone.emit("complete", mockFile);
			
			fileAdded(mockFile, imageData.imageId);
			
			pictureDropzone.files.push(mockFile);
			
		});
			
	}
	
});