package ktrack.ui;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
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
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.collect.Lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BootstrapRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.EnumRadioChoiceRenderer;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import ktrack.WebApp;
import ktrack.entity.Dog;
import ktrack.ui.panels.CaregiverPanel;
import ktrack.ui.panels.DatePanel;
import ktrack.ui.panels.DogListPanel;
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

		},
		CAREGIVER {
			@Override
			public String getTargetId() {
				return "search-dog-caregiver-form";
			}

		},
		VET {
			@Override
			public String getTargetId() {
				return "search-dog-vet-form";
			}

		};

		public abstract String getTargetId();
		// public AjaxFormSubmitBehavior getFormSubmitBehavior();

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
			protected Radio<SearchOptions> newRadio(String id, IModel<SearchOptions> model,
					RadioGroup<SearchOptions> radioGroup) {
				Radio<SearchOptions> radio = super.newRadio(id, model, radioGroup);
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
		searchDogForm.add(new SearchByKennelOrDates());
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

		add(new EmptyPanel("dog-list-panel").setOutputMarkupId(true));

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

	/**
	 * Base class that encapsulates the ajax behavior for all search submits.
	 * 
	 * @author dsharma
	 */
	private abstract class SearchBehavior extends AjaxFormSubmitBehavior {
		public SearchBehavior() {
			super("submit");
		}

		@Override
		protected final void onSubmit(AjaxRequestTarget target) {
			DogListPanel panel = new DogListPanel("dog-list-panel", getQuery());
			panel.setOutputMarkupId(true);
			Search.this.addOrReplace(panel);
			target.add(panel);
		}

		@Override
		protected final void updateAjaxAttributes(AjaxRequestAttributes attributes) {
			super.updateAjaxAttributes(attributes);
			attributes.setPreventDefault(true);
		}

		/**
		 * Returns the query that drives the search.
		 * 
		 * @return the query.
		 */
		protected abstract Query getQuery();
	}

	/**
	 * Class that implements the search by date and kennel behavior.
	 */
	private class SearchByKennelOrDates extends SearchBehavior {

		@Override
		protected Query getQuery() {
			boolean isQueryFeasible = dog.getArrivalDate() != null || dog.getSurgeryDate() != null
					|| dog.getReleaseDate() != null || dog.getKennel() != null;
			if (isQueryFeasible) {
				Query query = new Query();
				if(dog.getArrivalDate() != null) {
					query.addCriteria(Criteria.where("arrivalDate").is(dog.getArrivalDate()));
				}
				if(dog.getSurgeryDate() != null) {
					query.addCriteria(Criteria.where("surgeryDate").is(dog.getSurgeryDate()));
				}
				if(dog.getReleaseDate() != null) {
					query.addCriteria(Criteria.where("releaseDate").is(dog.getReleaseDate()));
				}
				if(dog.getKennel() != null) {
					query.addCriteria(Criteria.where("kennel").is(dog.getKennel()));
				}
				
				return query;
				
			}
			return null;
		}

	}

}
