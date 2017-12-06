package ktrack.ui;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.EmptyPanel;
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
import ktrack.entity.QueryFactory.IDogQuery;
import ktrack.entity.QueryFactory.QUERYPROVIDERS;
import ktrack.ui.panels.CaregiverPanel;
import ktrack.ui.panels.DatePanel;
import ktrack.ui.panels.DogListPanel;
import ktrack.ui.panels.KennelPanel;
import ktrack.ui.panels.SaveButtonPanel;
import ktrack.ui.panels.SaveButtonPanel.SaveText;
import ktrack.ui.panels.VetPanel;

public class Search extends BaseAuthenticatedPage {
	
	/**
	 * The constructor.
	 * 
	 * @param pageParams
	 *            The page params.
	 */

	public Search(PageParameters pageParams) {
		super(pageParams);
		Dog dog = new Dog();
		CompoundPropertyModel<Dog> dogModel = new CompoundPropertyModel<Dog>(Model.of(dog));

		add(new BootstrapRadioGroup<QUERYPROVIDERS>("search-by", Model.of(QUERYPROVIDERS.DATE_KENNEL),
				Lists.newArrayList(QUERYPROVIDERS.DATE_KENNEL, QUERYPROVIDERS.CAREGIVER, QUERYPROVIDERS.VET),
				new EnumRadioChoiceRenderer<QUERYPROVIDERS>(Buttons.Type.Default, this)) {

			@Override
			protected Radio<QUERYPROVIDERS> newRadio(String id, IModel<QUERYPROVIDERS> model,
					RadioGroup<QUERYPROVIDERS> radioGroup) {
				Radio<QUERYPROVIDERS> radio = super.newRadio(id, model, radioGroup);
				radio.add(new AttributeAppender("data-target", getTargetId(model.getObject())));
				return radio;
			}
		});

		Form<Dog> searchDogForm = new Form<Dog>(getTargetId(QUERYPROVIDERS.DATE_KENNEL), dogModel);
		searchDogForm.setOutputMarkupId(true);
		searchDogForm.setMarkupId(getTargetId(QUERYPROVIDERS.DATE_KENNEL));
		searchDogForm.add(new DatePanel("datePanel").setRenderBodyOnly(true));
		searchDogForm.add(new KennelPanel("kennelPanel").setRenderBodyOnly(true));
		searchDogForm.add(new SaveButtonPanel("saveButtonPanel", SaveText.SEARCH).setRenderBodyOnly(true));
		searchDogForm.add(new SearchBehavior(QUERYPROVIDERS.DATE_KENNEL.getQueryProvider()));
		add(searchDogForm);

		Form<Dog> searchDogByVetForm = new Form<Dog>(getTargetId(QUERYPROVIDERS.VET), dogModel);
		searchDogByVetForm.setOutputMarkupId(true);
		searchDogByVetForm.setMarkupId(getTargetId(QUERYPROVIDERS.VET));
		searchDogByVetForm.add(new VetPanel("vetpanel").setRenderBodyOnly(true));
		searchDogByVetForm.add(new SaveButtonPanel("saveButtonPanel", SaveText.SEARCH).setRenderBodyOnly(true));
		searchDogByVetForm.add(new SearchBehavior(QUERYPROVIDERS.VET.getQueryProvider()));
		add(searchDogByVetForm);

		Form<Dog> searchDogByCaregiverForm = new Form<Dog>(getTargetId(QUERYPROVIDERS.CAREGIVER), dogModel);
		searchDogByCaregiverForm.setOutputMarkupId(true);
		searchDogByCaregiverForm.setMarkupId(getTargetId(QUERYPROVIDERS.CAREGIVER));
		searchDogByCaregiverForm.add(new CaregiverPanel("caregiverpanel", false).setRenderBodyOnly(true));
		searchDogByCaregiverForm.add(new SaveButtonPanel("saveButtonPanel", SaveText.SEARCH).setRenderBodyOnly(true));
		searchDogByCaregiverForm.add(new SearchBehavior(QUERYPROVIDERS.CAREGIVER.getQueryProvider()));
		add(searchDogByCaregiverForm);

		add(new EmptyPanel("dog-list-panel").setOutputMarkupId(true).setMarkupId("dog-list-panel"));

	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(
				new JavaScriptResourceReference(getClass(), "js/search.js", getLocale(), getStyle(), getVariation())),
				"footer-container"));
		response.render(new FilteredHeaderItem(CssHeaderItem.forReference(FontAwesomeCssReference.instance()),
				"footer-container"));

	}

	/**
	 * Returns the target id for the query provider.
	 */
	private String getTargetId(QUERYPROVIDERS queryProvider) {
		switch (queryProvider) {
		case DATE_KENNEL:
			return "search-dog-date-form";
		case CAREGIVER:
			return "search-dog-caregiver-form";
		case VET:
			return "search-dog-vet-form";
		default:
			throw new IllegalArgumentException("Unknown queryprovider: " + queryProvider);
		}

	}

	/**
	 * Base class that encapsulates the ajax behavior for all search submits.
	 * 
	 * @author dsharma
	 */
	private class SearchBehavior extends AjaxFormSubmitBehavior {
		/** The query provider. */
		private IDogQuery queryProvider;
		
		/**
		 * The constructor.
		 * @param queryProvider
		 */
		public SearchBehavior(IDogQuery queryProvider) {
			super("submit");
			this.queryProvider = queryProvider;
		}

		@Override
		protected final void onSubmit(AjaxRequestTarget target) {		
			Dog dog = (Dog)getForm().getModel().getObject();
			DogListPanel panel = new DogListPanel("dog-list-panel", queryProvider.getQueryString(dog));
			panel.setOutputMarkupId(true);
			panel.setMarkupId("dog-list-panel");
			Search.this.addOrReplace(panel);
			target.add(panel);
		}

		@Override
		protected final void updateAjaxAttributes(AjaxRequestAttributes attributes) {
			super.updateAjaxAttributes(attributes);
			attributes.setPreventDefault(true);
		}
	}
}
