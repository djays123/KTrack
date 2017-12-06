if(! window.dogList) {
		window.dogList = {};
}
window.dogList.addBehavior = function() {
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
};

window.dogList.addBehavior();
				