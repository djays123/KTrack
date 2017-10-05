package ktrack.ui;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.IMultipartWebRequest;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioChoiceRenderer;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;
import ktrack.WebApp;
import ktrack.entity.Behavior;
import ktrack.entity.Dog;
import ktrack.entity.Sex;
import ktrack.entity.Sterilized;
import ktrack.repository.DogNamesRepository;
import ktrack.repository.DogRepository;

@MountPath("/newdog")
public class NewDogPage extends BaseAuthenticatedPage {

	/** The initial latitude. */
	private static final Double LATITUDE = 18.52895184;

	/** The initial longitude. */
	private static final Double LONGITUDE = 73.87434160;

	/** The google maps API key. */
	private static String GOOGLE_MAPS_KEY = "AIzaSyCCBGibN4Tkk59VRZ2AtFnJdqTPK6PymNQ";

	@SpringBean
	private DogNamesRepository dogNamesRepository;

	@SpringBean
	private DogRepository dogRepository;

	/**
	 * 
	 * @param pageParams
	 */
	public NewDogPage(final PageParameters pageParams) {
		super(pageParams);

		Dog dog = new Dog();
		dog.setLatitude(LATITUDE);
		dog.setLongitude(LONGITUDE);
		dog.setArrivalDate(new Date());
		CompoundPropertyModel<Dog> dogModel = new CompoundPropertyModel<Dog>(Model.of(dog));
		Form<Dog> form = new Form<Dog>("save-dog-form", dogModel);
		TextField<String> dogName = new TextField<String>("name");
		dogName.setOutputMarkupId(true);
		FeedbackPanel feedback = new FeedbackPanel("feedback") {
			@Override
			protected String getCSSClass(FeedbackMessage message) {
				switch (message.getLevel()) {
				case FeedbackMessage.SUCCESS:
					return "active list-unstyled";
				}

				return super.getCSSClass(message);
			}
		};
		feedback.setOutputMarkupId(true);

		AjaxFormSubmitBehavior ajaxFormSubmitBehavior = new AjaxFormSubmitBehavior("submit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				if (StringUtils.isEmpty(dog.getName())) {
					dog.setName(dogNamesRepository.getRandomName(dog.getSex()).getName());
					target.add(dogName);
				}

				List<String> imageIds = new LinkedList();
				IRequestParameters postParams = getRequest().getPostParameters();
				postParams.getParameterNames().forEach(param -> {
					if (StringUtils.startsWith(param, ImagePreview.IMAGE_FILE_ID_PREFIX)) {
						imageIds.add(StringUtils.substringAfter(param, ImagePreview.IMAGE_FILE_ID_PREFIX));
					}
				});

				dog.getImageIds().forEach(imageId -> {
					if (!imageIds.contains(imageId)) {
						dogNamesRepository.removeImage(imageId);
					}
				});
				dog.setImageIds(imageIds);
				dog.setUserId(((WebApp) getApplication()).getLoggedInUsername());
				dogRepository.save(dog);
				success(getString("save-success"));
				feedback.add(new AttributeModifier("class", "add-check alert alert-success"));
				target.add(feedback);
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.setPreventDefault(true);
			}
		};

		form.add(dogName);
		form.add(new TextField<String>("vetrinarian"));
		form.add(new TextArea<String>("comments"));
		form.add(new RequiredTextField<String>("location"));
		form.add(new HiddenField<Double>("latitude", Double.class));
		form.add(new HiddenField<Double>("longitude", Double.class));
		form.add(new NumberTextField<Integer>("age", Integer.class).setMinimum(0).setMaximum(15).setStep(1));
		form.add(new NumberTextField<Integer>("kennel", Integer.class).setMinimum(1).setMaximum(999).setStep(1));
		form.add(new DateTextField("arrivalDate", new DateTextFieldConfig().autoClose(true).withFormat("dd/mm/yyyy")));
		form.add(new DateTextField("surgeryDate", new DateTextFieldConfig().autoClose(true).withFormat("dd/mm/yyyy")));
		form.add(new DateTextField("releaseDate", new DateTextFieldConfig().autoClose(true).withFormat("dd/mm/yyyy")));
		form.add(new TextField<String>("caregiver"));
		form.add(new TextField<String>("caregiverMobile"));
		
		form.add(ajaxFormSubmitBehavior);
		add(form);
		add(feedback);

		ImagePreview<Void> imagePreview = new ImagePreview<>("image-preview");
		imagePreview.header(Model.<String>of(getString("view-image")));
		add(imagePreview);
		Form uploadFileform = new Form<Void>("upload-file-form") {

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
						dog.getImageIds().add(fileId);
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
		add(uploadFileform);

		form.add(new Icon("paw-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.paw).build()));
		form.add(new Icon("home-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.home).build()));
		form.add(new Icon("location-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.location_arrow).build()));
		form.add(new Icon("comment-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.comment).build()));
		form.add(new Icon("photo-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.camera).build()));
		form.add(new Icon("age-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.calendar_check_o).build()));
		form.add(new Icon("doctor-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.stethoscope).build()));
		form.add(new Icon("arrival-calendar-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.calendar).build()));
		form.add(new Icon("surgery-calendar-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.calendar).build()));
		form.add(new Icon("release-calendar-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.calendar).build()));
		form.add(new Icon("caregiver-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.user).build()));
		form.add(new Icon("caregiverMobile-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.mobile).build()));
		form.add(new DogAttributeBooleanRadioGroup("sex", dogModel, "sex", Sex.class));
		form.add(new DogAttributeBooleanRadioGroup("sterilized", dogModel, "sterilized", Sterilized.class));
		form.add(new DogAttributeBooleanRadioGroup("behavior", dogModel, "behavior", Behavior.class));
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
				CssHeaderItem.forReference(new CssResourceReference(getClass(), "css/NewDogPage.css")),
				"footer-container"));

		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem
						.forReference(new JavaScriptResourceReference(getClass(), "js/locationpicker.jquery.js")),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/newdogpage.js")),
				"footer-container"));
	}

	/**
	 * A boolean radio group choice adapted for the dog form.
	 * 
	 * @author dsharma
	 *
	 */
	private class DogAttributeBooleanRadioGroup extends BooleanRadioGroup {
		/** The enum holding the boolean options. */
		private Class<? extends Enum> choiceClazz;

		public DogAttributeBooleanRadioGroup(String id, final CompoundPropertyModel compoundModel, String property,
				Class<? extends Enum> choiceClazz) {
			super(id, new Model<Boolean>() {
				/**
				 * @see org.apache.wicket.model.IModel#getObject()
				 */
				@Override
				public Boolean getObject() {
					Enum<?> model = (Enum<?>) compoundModel.bind(property).getObject();
					return model.equals(choiceClazz.getEnumConstants()[0]) ? true : false;
				}

				/**
				 * Set the model object; calls setObject(java.io.Serializable).
				 * The model object must be serializable, as it is stored in the
				 * session
				 * 
				 * @param object
				 *            the model object
				 * @see org.apache.wicket.model.IModel#setObject(Object)
				 */
				@Override
				public void setObject(final Boolean object) {
					Enum<?> model = object ? choiceClazz.getEnumConstants()[0] : choiceClazz.getEnumConstants()[1];
					compoundModel.<Enum<?>>bind(property).setObject(model);
				}

			});
			this.choiceClazz = choiceClazz;
			setChoiceRenderer(new BooleanRadioChoiceRenderer(Type.Primary, this) {
				@Override
				protected String resourceKey(Boolean choice) {
					return choice ? choiceClazz.getEnumConstants()[0].toString()
							: choiceClazz.getEnumConstants()[1].toString();
				}

				@Override
				public String getButtonClass(Boolean option) {
					return Type.Primary.cssClassName() + " btn-md small";
				}
			});
		}

	}

}
