package ktrack.ui;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import ktrack.repository.DogNamesRepository;

public class ImagePreview<T> extends Modal<T> {
	/**
	 * The prefix added to names of parameters to identify them as image files
	 * ids.
	 */
	public static final String IMAGE_FILE_ID_PREFIX = "file_key_";

	@SpringBean
	private DogNamesRepository dogNamesRepository;

	public ImagePreview(String markupId) {
		super(markupId);
		final Model<String> imageKey = new Model<String>("");

		Image image = new Image("imgid", "preview");
		image.setOutputMarkupId(true);
		image.setMarkupId("imgid");
		image.setImageResource(new DynamicImageResource() {
			protected byte[] getImageData(Attributes attributes) {
				String imageId = StringUtils.substringAfter(imageKey.getObject(), ImagePreview.IMAGE_FILE_ID_PREFIX);
				if (StringUtils.isNotEmpty(imageId)) {
					return dogNamesRepository.getImage(imageId);
				}
				
				return new byte[0];
			}
		});
		add(image);

		Form imagePreviewForm = new Form<Void>("image-preview-form");

		HiddenField<String> imageKeyHolder = new HiddenField<String>("image-file-key", imageKey, String.class);
		imagePreviewForm.add(imageKeyHolder);

		AjaxFormSubmitBehavior ajaxFormSubmitBehavior = new AjaxFormSubmitBehavior("submit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
					target.add(image);				
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.setPreventDefault(true);
			}
		};

		imagePreviewForm.add(ajaxFormSubmitBehavior);
		add(imagePreviewForm);
		
		size(Size.Large);

	}

}
