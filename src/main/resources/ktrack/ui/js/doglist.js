function renderThumbNail( data, type, row, meta ) {
	
	if(! window.dogList) {		
		window.dogList = {};
		window.dogList.snapshotUrl = '${SNAPSHOTURL}';
		window.dogList.previewfileKey = '${PREVIEWKEY}';
		window.dogList.renderSnapshotThumbnail = function(data, type, row, meta) {
			var html = '';
			var colLength = 12 / $(data).length; 
			$.each(data, function(index, value) {
				var cls = "thumbnail col-xs-"  + colLength;
				html += '<div style="margin-bottom: 0" class="' + cls + '" ><a  class="thumbNailLink" href="#" id="' + value + '" >';			
				html += '<img src="' + dogList.snapshotUrl +'?imageId=' + value + '" /></a></div>';
			});
		
			return html;
		};
		
		$('#dogTable').on('click', '.thumbNailLink', function() {		
			 $('#image-file-key').val(window.dogList.previewfileKey + this.id);
	         $('#image-preview-form').trigger('submit');
	         $('#image-preview').modal({keyboard:true, show:true});
	    });
		
		$('#dogTable').on('select.dt', function ( e, dt, type, indexes ) {
			
			if('row' == type) {
				var dogId = dt.rows( { selected: true }).data()[0].id;
				$('#edit-dog-key').val(dogId);
				$('#edit-dog').trigger('submit');
			}
		});
				
	}
	
	
	
	return window.dogList.renderSnapshotThumbnail(data, type, row, meta);
}
