package ktrack.ui;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import ktrack.WebApp;
import ktrack.entity.Dog;
import ktrack.entity.Sterilized;
import ktrack.repository.DogNamesRepository;
import ktrack.repository.DogRepository;
import ktrack.ui.panels.BehaviorPanel;
import ktrack.ui.panels.CaregiverPanel;
import ktrack.ui.panels.CommentPanel;
import ktrack.ui.panels.DatePanel;
import ktrack.ui.panels.DogAttributeBooleanRadioGroup;
import ktrack.ui.panels.DogInfoPanel;
import ktrack.ui.panels.KennelPanel;
import ktrack.ui.panels.LocationPanel;
import ktrack.ui.panels.SaveButtonPanel;
import ktrack.ui.panels.SaveButtonPanel.SaveText;
import ktrack.ui.panels.SexPanel;
import ktrack.ui.panels.SnapshotPanel;
import ktrack.ui.panels.StatusPanel;
import ktrack.ui.panels.VetPanel;

@MountPath("/newdog")
public class NewDogPage extends BaseAuthenticatedPage {
	
	@SpringBean
	private DogNamesRepository dogNamesRepository;

	@SpringBean
	private DogRepository dogRepository;

	/** The dog. */
	private transient Dog dog;

	/**
	 * 
	 * @param pageParams
	 */
	public NewDogPage(final PageParameters pageParams) {
		super(pageParams);

		String dogId = pageParams.get(SnapshotPanel.DOG_PARAM).toString();
		boolean isExistingDog = StringUtils.isNotEmpty(dogId);
		if (isExistingDog) {
			dog = dogRepository.findOne(dogId);
		}

		if (dog == null) {
			dog = new Dog();
			dog.setArrivalDate(new Date());
		}

		CompoundPropertyModel<Dog> dogModel = new CompoundPropertyModel<Dog>(Model.of(dog));
		Form<Dog> form = new Form<Dog>("save-dog-form", dogModel);
		DogInfoPanel dogInfoPanel = new DogInfoPanel("dogInfoPanel");
		dogInfoPanel.setOutputMarkupId(true).setRenderBodyOnly(true);
		form.add(dogInfoPanel);
		
		
		FeedbackPanel feedback = new StatusPanel("feedback");
	
		AjaxFormSubmitBehavior ajaxFormSubmitBehavior = new AjaxFormSubmitBehavior("submit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				dogInfoPanel.update(target);

				Collection<String> imageIds = new HashSet<>();
				IRequestParameters postParams = getRequest().getPostParameters();
				postParams.getParameterNames().forEach(param -> {
					if (StringUtils.startsWith(param, ImagePreview.IMAGE_FILE_ID_PREFIX)) {
						imageIds.add(StringUtils.substringAfter(param, ImagePreview.IMAGE_FILE_ID_PREFIX));
					}
				});

				dog.setImageIds(imageIds);
				dog.setUserId(((WebApp) getApplication()).getLoggedInUsername());
				dogRepository.save(dog);
				dogNamesRepository.associateImages(dog.getId(), imageIds);
				success(getString("save-success"));
				feedback.add(new AttributeModifier("class", "add-check"));
				target.add(feedback);
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.setPreventDefault(true);
			}
		};

		
		form.add(new VetPanel("vetpanel").setRenderBodyOnly(true));
		form.add(new CommentPanel("comment-panel").setRenderBodyOnly(true));
		form.add(new LocationPanel("locationPanel", isExistingDog ? null : dog).setRenderBodyOnly(true));
		form.add(new SnapshotPanel("snapshot-panel", isExistingDog ? dog : null, "upload-file-form", "image-preview", this).setRenderBodyOnly(true));
		form.add(new KennelPanel("kennelPanel").setRenderBodyOnly(true));
		form.add(new CaregiverPanel("caregiverpanel", true).setRenderBodyOnly(true));
		form.add(new DatePanel("datePanel"));

		form.add(ajaxFormSubmitBehavior);
		add(form);
		add(feedback);



		form.add(new SexPanel("sex", dogModel, "sex"));
		form.add(new DogAttributeBooleanRadioGroup("sterilized", dogModel, "sterilized", Sterilized.class));
		form.add(new BehaviorPanel("behavior", dogModel, "behavior"));

		form.add(new SaveButtonPanel("savePanel", SaveText.SAVE).setRenderBodyOnly(true));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
			response.render(new FilteredHeaderItem(CssHeaderItem.forReference(FontAwesomeCssReference.instance()),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(new CssResourceReference(getClass(), "css/NewDogPage.css")),
				"footer-container"));

		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/newdogpage.js")),
				"footer-container"));

	
	}

	
	

	

}
