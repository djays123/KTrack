function renderThumbNail( data, type, row, meta ) {
	
	if(! window.dogList) {		
		window.dogList = {};
		window.dogList.snapshotUrl = '${SNAPSHOTURL}';
		window.dogList.previewfileKey = '${PREVIEWKEY}';
		window.dogList.renderSnapshotThumbnail = function(data, type, row, meta) {
			var html = '<div class=\"row\">';
			$.each(data, function(index, value) {
				html += '<div class="thumbnail col-xs-4"><a  class="thumbNailLink" href="#" id="' + value + '" >';			
				html += '<img src="' + dogList.snapshotUrl +'?imageId=' + value + '" /></a></div>';
			});
			html += '</div>';
			return html;
		};
		
		$('#dogTable').on('click', '.thumbNailLink', function() {		
			 $('#image-file-key').val(window.dogList.previewfileKey + this.id);
	         $('#image-preview-form').trigger('submit');
	         $('#image-preview').modal({keyboard:true, show:true});
	    });
	}
	
	
	
	return window.dogList.renderSnapshotThumbnail(data, type, row, meta);
}
