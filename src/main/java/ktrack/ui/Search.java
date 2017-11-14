package ktrack.ui;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.google.common.collect.Lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BootstrapRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.EnumRadioChoiceRenderer;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import ktrack.entity.Dog;
import ktrack.ui.panels.CaregiverPanel;
import ktrack.ui.panels.DatePanel;
import ktrack.ui.panels.KennelPanel;
import ktrack.ui.panels.SaveButtonPanel;
import ktrack.ui.panels.SaveButtonPanel.SaveText;
import ktrack.ui.panels.VetPanel;

public class Search extends BaseAuthenticatedPage {
	/** The dog. */
	private transient Dog dog;

	/**
	 * The search by options.
	 */
	private static enum SearchOptions {
		DATE_KENNEL {
			@Override
			public String getTargetId() {
				return "search-dog-date-form";
			}
			
		}, CAREGIVER {
			@Override
			public String getTargetId() {
				return "search-dog-caregiver-form";
			}
			
		}, VET {
			@Override
			public String getTargetId() {
				return "search-dog-vet-form";
			}
			
		};
		
		public abstract String getTargetId();

	};

	/**
	 * The constructor.
	 * 
	 * @param pageParams
	 *            The page params.
	 */

	public Search(PageParameters pageParams) {
		super(pageParams);
		dog = new Dog();
		CompoundPropertyModel<Dog> dogModel = new CompoundPropertyModel<Dog>(Model.of(dog));

		add(new BootstrapRadioGroup<SearchOptions>("search-by", Model.of(SearchOptions.DATE_KENNEL),
				Lists.newArrayList(SearchOptions.DATE_KENNEL, SearchOptions.CAREGIVER, SearchOptions.VET),
				new EnumRadioChoiceRenderer<SearchOptions>(Buttons.Type.Default, this)) {
			
			@Override
			protected  Radio<SearchOptions> newRadio(String id, IModel<SearchOptions> model, RadioGroup<SearchOptions> radioGroup) {
				Radio<SearchOptions> radio =  super.newRadio(id, model, radioGroup);
				radio.add(new AttributeAppender("data-target", model.getObject().getTargetId()));
				return radio;
		    }
		});

		Form<Dog> searchDogForm = new Form<Dog>(SearchOptions.DATE_KENNEL.getTargetId(), dogModel);
		searchDogForm.setOutputMarkupId(true);
		searchDogForm.setMarkupId(SearchOptions.DATE_KENNEL.getTargetId());
		searchDogForm.add(new DatePanel("datePanel").setRenderBodyOnly(true));
		searchDogForm.add(new KennelPanel("kennelPanel").setRenderBodyOnly(true));
		searchDogForm.add(new SaveButtonPanel("saveButtonPanel", SaveText.SEARCH).setRenderBodyOnly(true));

		add(searchDogForm);

		Form<Dog> searchDogByVetForm = new Form<Dog>(SearchOptions.VET.getTargetId(), dogModel);
		searchDogByVetForm.setOutputMarkupId(true);
		searchDogByVetForm.setMarkupId(SearchOptions.VET.getTargetId());
		searchDogByVetForm.add(new VetPanel("vetpanel").setRenderBodyOnly(true));
		searchDogByVetForm.add(new SaveButtonPanel("saveButtonPanel", SaveText.SEARCH).setRenderBodyOnly(true));

		add(searchDogByVetForm);
		
		Form<Dog> searchDogByCaregiverForm = new Form<Dog>(SearchOptions.CAREGIVER.getTargetId(), dogModel);
		searchDogByCaregiverForm.setOutputMarkupId(true);
		searchDogByCaregiverForm.setMarkupId(SearchOptions.CAREGIVER.getTargetId());
		searchDogByCaregiverForm.add(new CaregiverPanel("caregiverpanel").setRenderBodyOnly(true));
		searchDogByCaregiverForm.add(new SaveButtonPanel("saveButtonPanel", SaveText.SEARCH).setRenderBodyOnly(true));

		add(searchDogByCaregiverForm);
		
		
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(
				new JavaScriptResourceReference(getClass(), "js/search.js", getLocale(), getStyle(), getVariation())),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(new CssResourceReference(getClass(), "css/NewDogPage.css")),
				"footer-container"));
		response.render(new FilteredHeaderItem(CssHeaderItem.forReference(FontAwesomeCssReference.instance()),
				"footer-container"));

	}

}
