package ktrack.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.IMultipartWebRequest;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
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
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;
import ktrack.entity.Behavior;
import ktrack.entity.Dog;
import ktrack.entity.Sex;
import ktrack.entity.Sterilized;
import ktrack.repository.DogNamesRepository;

@MountPath("/welcome")
public class WelcomePage extends BaseAuthenticatedPage {
	/** The google maps API key. */
	private static String GOOGLE_MAPS_KEY = "AIzaSyCCBGibN4Tkk59VRZ2AtFnJdqTPK6PymNQ";

	@SpringBean
	private DogNamesRepository dogNamesRepository;

	/**
	 * 
	 * @param pageParams
	 */
	public WelcomePage(final PageParameters pageParams) {
		super(pageParams);

		Dog dog = new Dog();

		CompoundPropertyModel<Dog> dogModel = new CompoundPropertyModel<Dog>(Model.of(dog));
		Form<Dog> form = new Form<Dog>("save-dog-form", dogModel);
		// form.setMultiPart(true);
		AjaxFormSubmitBehavior ajaxFormSubmitBehavior = new AjaxFormSubmitBehavior("submit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				DogNamesRepository dg = dogNamesRepository;
				System.out.println("Dog Name:" + dg.getRandomName(Sex.M));
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.setPreventDefault(true);
			}
		};

		form.add(new TextField<String>("name"));
		form.add(new TextArea<String>("comments"));
		form.add(ajaxFormSubmitBehavior);
		add(form);

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
						fileKeys.add(dogNamesRepository.saveImage(uploadedFile.getInputStream(), fileName,
								new MimetypesFileTypeMap().getContentType(fileName)));
					} catch (IOException ioException) {
						throw new IllegalArgumentException("Failed to create uploaded file: " + uploadedFile.getName());
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
		form.add(new Icon("location-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.location_arrow).build()));
		form.add(new Icon("comment-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.comment).build()));
		form.add(new Icon("photo-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.camera).build()));

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
