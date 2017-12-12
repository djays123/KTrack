package ktrack.ui.panels;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.IMultipartWebRequest;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;
import ktrack.WebApp;
import ktrack.entity.Dog;
import ktrack.repository.DogNamesRepository;

public class SnapshotPanel extends Panel {
	/** The key of the page parameter that indicates an existing dog to edit. */
	public static final String DOG_PARAM = "dogId";
	/**
	 * The internally used param to store the json representation of the list of the
	 * dogs image data.
	 */
	private static final String DOG_IMAGE_PARAM = "dogImages";

	/** The JS snippet that will hold the image ids of the existing dog. */
	private static final String DOG_IMAGE_PARAM_JS = "window.dogData = window.dogData || {}; window.dogData.imageData = %s; ";

	/**
	 * The JS snippet that will hold the thumbnail url of a snapshot of the
	 * existing dog.
	 */
	private static final String DOG_IMAGE_THUMBNAIL_PARAM_JS = "%s?imageId=%s";
	
	
	@SpringBean
	private DogNamesRepository dogNamesRepository;

	/** The dog. */
	private transient Dog dog;

	public SnapshotPanel(String id, Dog dog, String uploadFormId, String imagePreviewId, MarkupContainer parent) {
		super(id);
		this.dog = dog;

		ImagePreview<Void> imagePreview = new ImagePreview<>(imagePreviewId);
		imagePreview.header(Model.<String>of(getString("view-image")));
		add(new Icon("photo-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.camera).build()));

		parent.add(imagePreview);

		Form<Void> uploadFileform = new Form<Void>(uploadFormId) {

			@Override
			protected void onSubmit() {

				IMultipartWebRequest webRequest = (IMultipartWebRequest) getRequest();
				Map<String, List<org.apache.commons.fileupload.FileItem>> files = webRequest.getFiles();
				List<org.apache.commons.fileupload.FileItem> fileUploads = new ArrayList<>();
				files.values().forEach(listFileItem -> listFileItem.forEach(fileItem -> fileUploads.add(fileItem)));
				final JsonArray fileKeys = new JsonArray();
				for (org.apache.commons.fileupload.FileItem uploadedFile : fileUploads) {
					String fileName = uploadedFile.getName();
					try {
						String fileId = dogNamesRepository.saveImage(uploadedFile.getInputStream(), fileName,
								URLConnection.guessContentTypeFromName(fileName));
						fileKeys.add(fileId);
					} catch (IOException ioException) {
						throw new IllegalArgumentException("Failed to create uploaded file: " + fileName);
					}
				}

				getRequestCycle().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {
					@Override
					public void detach(IRequestCycle reqCycle) {
					}

					@Override
					public void respond(IRequestCycle requestCycle) {
						WebResponse webResponse = (WebResponse) requestCycle.getResponse();
						webResponse.setContentType("application/json");
						JsonObject filesData = new JsonObject();
						filesData.add("files", fileKeys);
						webResponse.write(filesData.toString());

					}
				});

			}

		};
		uploadFileform.setMultiPart(true);
		parent.add(uploadFileform);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(
				new JavaScriptResourceReference(getClass(), "js/dropzone.js", getLocale(), getStyle(), getVariation())),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(new CssResourceReference(getClass(), "css/dropzone.css")),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/snapshotpanel.js")),
				"footer-container"));


		if (dog != null) {
			String dogImageDataJSON = getImageJSON(dog);
			response.render(JavaScriptHeaderItem.forScript(dogImageDataJSON, DOG_IMAGE_PARAM));
		}

	}

	/**
	 * Returns the json data for existing image ids.
	 */
	private String getImageJSON(Dog dog) {

		final String snapshotUrl = urlFor(((WebApp) getApplication()).getSnapshotResourceReference(),
				(PageParameters) null).toString();

		List<DogImage> dogImages = new ArrayList<>();

		for (String imageFileId : dog.getImageIds()) {
			Object[] fileInfo = dogNamesRepository.getImageNameAndLength(imageFileId);

			dogImages.add(new DogImage(snapshotUrl, imageFileId, fileInfo[0].toString(), (Long) fileInfo[1]));
		}

		String json = new Gson().toJson(dogImages);
		return String.format(DOG_IMAGE_PARAM_JS, json);
	}

	/**
	 * Holds the dog's image data.
	 * 
	 * @author dsharma
	 * 
	 */
	private static class DogImage {
		/** The image id. */
		@SuppressWarnings("unused")
		private String imageId;

		/** The snapshot url. */
		@SuppressWarnings("unused")
		private String snapshotURL;

		/** The file name. */
		@SuppressWarnings("unused")
		private String fileName;

		/** The file size. */
		@SuppressWarnings("unused")
		private long fileSize;

		/**
		 * The constructor.
		 * 
		 * @param snapshotURL
		 *            The snapshot URL.
		 * @param imageId
		 *            The imageId.
		 */
		DogImage(String snapshotURL, String imageId, String fileName, long fileSize) {
			this.fileName = fileName;
			this.fileSize = fileSize;
			this.snapshotURL = String.format(DOG_IMAGE_THUMBNAIL_PARAM_JS, snapshotURL, imageId);
			this.imageId = imageId;
		}

	};

}
