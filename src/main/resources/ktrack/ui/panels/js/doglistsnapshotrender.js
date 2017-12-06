function renderThumbNail( data, type, row, meta ) {
	if(! window.dogList) {
		window.dogList = {};
	}
	if(! window.dogList.snapshotUrl) {		
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
	}
	
	
	return window.dogList.renderSnapshotThumbnail(data, type, row, meta);
}

