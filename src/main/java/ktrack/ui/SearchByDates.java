package ktrack.ui;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BootstrapRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.EnumRadioChoiceRenderer;

public class SearchByDates extends BaseAuthenticatedPage {
	/**
	 * The search by options.
	 */
	private static enum SearchOptions {
		DATE_KENNEL, CAREGIVER, VET,

	};

	/**
	 * The constructor.
	 * 
	 * @param pageParams
	 *            The page params.
	 */

	public SearchByDates(PageParameters pageParams) {
		super(pageParams);

		Form<Void> searchDogForm = new Form<Void>("search-dog-form");

	
		searchDogForm.add(new BootstrapRadioGroup<SearchOptions>("search-by", Model.of(SearchOptions.DATE_KENNEL),
				Lists.newArrayList(SearchOptions.DATE_KENNEL, SearchOptions.CAREGIVER, SearchOptions.VET),
				new EnumRadioChoiceRenderer<SearchOptions>(Buttons.Type.Default, this)));

		add(searchDogForm);

	}

}
