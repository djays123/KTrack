package ktrack.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.datatables.DataTables;
import org.wicketstuff.datatables.columns.SpanColumn;
import org.wicketstuff.datatables.columns.SpanHeadersToolbar;
import org.wicketstuff.datatables.options.Column;
import org.wicketstuff.datatables.options.Options;
import org.wicketstuff.datatables.options.ScrollerOptions;
import org.wicketstuff.datatables.virtualscroll.AbstractVirtualScrollResourceReference;

import de.agilecoders.wicket.jquery.AbstractConfig;
import de.agilecoders.wicket.jquery.IKey;
import de.agilecoders.wicket.jquery.Key;
import ktrack.entity.Behavior;
import ktrack.entity.Dog;
import ktrack.entity.Sex;
import ktrack.entity.Sterilized;
import ktrack.repository.DogRepository;

@MountPath("/dogs")
public class DogsList extends BaseAuthenticatedPage {

	/** The maximum number of records displayed per page. */
	private int MAX_RECORDS_PER_PAGE = 2;

	/** The dog properties shown in the grid. */
	private static final String[] DOG_PROPERTIES = { "name", "age", "sex", "sterilized", "behavior", "location",
			"comments" };

	@SpringBean
	private DogRepository dogRepository;

	/**
	 * The default constructor.
	 * 
	 * @param pageParams
	 */
	public DogsList(PageParameters pageParams) {
		super(pageParams);
		List<IColumn<Dog, ? extends Object>> columns = new ArrayList<>(DOG_PROPERTIES.length);
		List<Column> datatableColumns = new ArrayList<>();

		for (String dogProperty : DOG_PROPERTIES) {
			StringResourceModel displayModel = new StringResourceModel(dogProperty, this, null);
			columns.add(new SpanColumn<Dog, String>(displayModel, dogProperty));
			datatableColumns.add(new Column(dogProperty));
		}

		DogsDataProvider dogsDataProvider = new DogsDataProvider(dogRepository);
		DataTables<Dog, String> table = new DataTables("dogTable", columns);
		table.addTopToolbar(new SpanHeadersToolbar<>(table));
		CharSequence ajaxUrl = urlFor(new DogDataVirtualScrollResourceReference(dogsDataProvider), null);
		ScrollerOptions scrollerOptions = new ScrollerOptions();
		scrollerOptions.loadingIndicator(true).displayBuffer(100).serverWait(500);

		Options options = table.getOptions();
		Map<String, CharSequence> ajaxOptions = new HashMap<>();
		ajaxOptions.put("url", ajaxUrl);
		ajaxOptions.put("type", "POST");
		
		AbstractConfig ajaxConfig = new AbstractConfig() {
		};
		ajaxConfig.put(new Key<>("url", null), ajaxUrl);
		ajaxConfig.put(new Key<>("type", null), "POST");
		
		IKey<AbstractConfig> ajaxConfigKey = new Key<>("ajax", null);

		options.columns(datatableColumns).serverSide(true).ordering(true).searching(true).scrollY("300")
				.deferRender(true).scroller(scrollerOptions).dom("frti") // "p"
																						// is
																						// removed
																						// because
																						// we
																						// use
																						// Scroller
																						// (virtual
																						// scrolling)
				// .scrollCollapse(true)
				.stateSave(true).info(true).processing(false).retrieve(true).put(ajaxConfigKey, ajaxConfig);

		add(table);

	}

	private class DogDataVirtualScrollResourceReference extends AbstractVirtualScrollResourceReference<Dog> {

		/** The Dog data provider. */
		private DogsDataProvider dogsDataProvider;

		public DogDataVirtualScrollResourceReference(DogsDataProvider dogsDataProvider) {
			super(DogDataVirtualScrollResourceReference.class, "dogsDataVitrualScrollResRef");
			this.dogsDataProvider = dogsDataProvider;
		}

		@Override
		protected void populateEntryJson(JSONObject entryJson, Dog dog) {
			entryJson.put("DT_RowId", "PK_" + dog.getId());
			entryJson.put("DT_RowClass", "custom");

			/** The map to look up localized values for enums. */
			final Map<Object, String> dogPropertyValues = new HashMap<>();
			dogPropertyValues.put(Sex.M, getString("male"));
			dogPropertyValues.put(Sex.F, getString("female"));
			dogPropertyValues.put(Sterilized.NOT_STERLIZED, getString("no"));
			dogPropertyValues.put(Sterilized.STERLIZED, getString("yes"));
			dogPropertyValues.put(Behavior.FRIENDLY, getString("friendly"));
			dogPropertyValues.put(Behavior.AGGRESSIVE, getString("aggressive"));

			CompoundPropertyModel<Dog> dogPropertyModel = CompoundPropertyModel.of(Model.of(dog));
			for (String dogProperty : DOG_PROPERTIES) {
				Object value = dogPropertyModel.bind(dogProperty).getObject();
				if (value != null) {
					value = value.getClass().isEnum() ? dogPropertyValues.get(value) : value;
					entryJson.put(dogProperty, value);
				} else {
					entryJson.put(dogProperty, StringUtils.EMPTY);
				}				
			}
		}

		@Override
		protected IDataProvider<Dog> getDataProvider(PageParameters parameters) {
			return dogsDataProvider;
		}

	}

}
