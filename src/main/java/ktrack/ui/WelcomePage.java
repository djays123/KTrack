package ktrack.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.IMultipartWebRequest;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.file.Folder;
import org.wicketstuff.annotation.mount.MountPath;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioChoiceRenderer;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;
import ktrack.WebApp;

@MountPath("/welcome")
public class WelcomePage extends BaseAuthenticatedPage {
	/** The google maps API key. */
	private static String GOOGLE_MAPS_KEY = "AIzaSyCCBGibN4Tkk59VRZ2AtFnJdqTPK6PymNQ";

	/**
	 * 
	 * @param pageParams
	 */
	public WelcomePage(final PageParameters pageParams) {
		super(pageParams);

		Form form = new Form<Void>("save-dog-form");
		// form.setMultiPart(true);
		AjaxFormSubmitBehavior ajaxFormSubmitBehavior = new AjaxFormSubmitBehavior("submit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				System.out.println("Ole!");
			}

			

		};

		form.add(ajaxFormSubmitBehavior);
		add(form);

		Form uploadFileform = new Form<Void>("upload-file-form") {

			@Override
			protected void onSubmit() {

				IMultipartWebRequest webRequest = (IMultipartWebRequest) getRequest();
				Map<String, List<org.apache.commons.fileupload.FileItem>> files = webRequest.getFiles();
				List<org.apache.commons.fileupload.FileItem> fileUploads = new ArrayList<>();
				files.values().forEach(listFileItem -> listFileItem.forEach(fileItem -> fileUploads.add(fileItem)));
				Folder tempFolder = ((WebApp) getApplication()).getUploadFolder();
				final JsonArray fileKeys = new JsonArray();
				for (org.apache.commons.fileupload.FileItem uploadedFile : fileUploads) {
					String fileName = uploadedFile.getName();
					String baseName = FilenameUtils.getBaseName(fileName);
					String suffix = "." + FilenameUtils.getExtension(fileName);

					try {
						File tempFile = File.createTempFile(baseName, suffix, tempFolder.getAbsoluteFile());
						FileUtils.writeByteArrayToFile(tempFile, uploadedFile.get());	
						fileKeys.add(Base64.getEncoder().encodeToString(tempFile.getAbsolutePath().getBytes()));
						System.out.println("Saved file: " + tempFile.getAbsolutePath() + " of size " + uploadedFile.getSize());
					} catch (Exception ioException) {
						throw new IllegalArgumentException("Failed to create uploaded file: " + uploadedFile.getName());
					}
				}

				getRequestCycle().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {
					@Override
					public void detach(IRequestCycle reqCycle) {
					}

					@Override
					public void respond(IRequestCycle requestCycle) {
						WebResponse webResponse = (WebResponse)requestCycle.getResponse();
						webResponse.setContentType("application/json");
						JsonObject filesData = new JsonObject();
						filesData.add("files", fileKeys);
						webResponse.write(filesData.toString());

					}
				});

			}

		};
		uploadFileform.setMultiPart(true);
		add(uploadFileform);

		form.add(new Icon("paw-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.paw).build()));
		form.add(new Icon("location-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.location_arrow).build()));
		form.add(new Icon("comment-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.comment).build()));
		form.add(new Icon("photo-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.camera).build()));
		BooleanRadioGroup sex = new BooleanRadioGroup("sex", new Model<Boolean>(Boolean.TRUE));
		sex.setChoiceRenderer(new BooleanRadioChoiceRenderer(Type.Primary, this) {
			@Override
			protected String resourceKey(Boolean choice) {
				return choice ? "male" : "female";
			}

			@Override
			public String getButtonClass(Boolean option) {
				return Type.Primary.cssClassName() + " btn-md small";
			}
		});
		form.add(sex);

		BooleanRadioGroup sterlized = new BooleanRadioGroup("sterilized", new Model<Boolean>(Boolean.FALSE));
		sterlized.setChoiceRenderer(new BooleanRadioChoiceRenderer(Type.Primary, this) {
			@Override
			protected String resourceKey(Boolean choice) {
				return choice ? "sterilized" : "not-sterilized";
			}

			@Override
			public String getButtonClass(Boolean option) {
				return Type.Primary.cssClassName() + " btn-md";
			}
		});
		form.add(sterlized);

		BooleanRadioGroup behavior = new BooleanRadioGroup("behavior", new Model<Boolean>(Boolean.TRUE));
		behavior.setChoiceRenderer(new BooleanRadioChoiceRenderer(Type.Primary, this) {
			@Override
			protected String resourceKey(Boolean choice) {
				return choice ? "friendly" : "not-friendly";
			}

			@Override
			public String getButtonClass(Boolean option) {
				return Type.Primary.cssClassName() + " btn-md";
			}
		});
		form.add(behavior);

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
		response.render(new FilteredHeaderItem(CssHeaderItem.forReference(FontAwesomeCssReference.instance()),
				"footer-container"));

		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem
						.forUrl("http://maps.google.com/maps/api/js?key=" + GOOGLE_MAPS_KEY + "&libraries=places"),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(new CssResourceReference(getClass(), "css/welcomepage.css")),
				"footer-container"));

		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem
						.forReference(new JavaScriptResourceReference(getClass(), "js/locationpicker.jquery.js")),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/welcomepage.js")),
				"footer-container"));
	}

}
