package ktrack.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
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
import de.agilecoders.wicket.jquery.util.Json;
import ktrack.entity.Behavior;
import ktrack.entity.Dog;
import ktrack.entity.Sex;
import ktrack.entity.Sterilized;
import ktrack.repository.DogNamesRepository;
import ktrack.repository.DogRepository;

@MountPath("/dogs")
public class DogsList extends BaseAuthenticatedPage {

	@SpringBean
	private DogRepository dogRepository;

	@SpringBean
	private DogNamesRepository dogNamesRepository;

	/**
	 * The default constructor.
	 * 
	 * @param pageParams
	 */
	public DogsList(PageParameters pageParams) {
		super(pageParams);

		List<IColumn<Dog, ? extends Object>> columns = new ArrayList<>(DogsDataProvider.DOG_PROPERTIES.length);

		List<Column> datatableColumns = new ArrayList<>(DogsDataProvider.DOG_PROPERTIES.length);
		
		SnapshotResource snapshotResource = new SnapshotResource() {
			@Override
			protected DogNamesRepository getDogNamesRepository() {
				return dogNamesRepository;
			}

			@Override
			protected boolean isThumbnail() {
				return true;
			}

		};
		String snapshotUrl = urlFor(new ResourceReference("dogsListSnapshots") {

			@Override
			public IResource getResource() {
				return snapshotResource;
			}

		}, (PageParameters) null).toString();
		TextTemplate renderThumbNailJSCode = new PackageTextTemplate(getClass(), "js/doglist.js", "text/javascript", "UTF-8");
		Map<String,Object> vars = new HashMap<String,Object>();
        vars.put("SNAPSHOTURL", snapshotUrl);
        vars.put("PREVIEWKEY", ImagePreview.IMAGE_FILE_ID_PREFIX);
        String renderFunctionValue = renderThumbNailJSCode.asString(vars);
        
		for (String dogProperty : DogsDataProvider.DOG_PROPERTIES) {
			StringResourceModel displayModel = new StringResourceModel(dogProperty, this, null);
			columns.add(new SpanColumn<Dog, String>(displayModel, dogProperty));
			Column column = new Column(dogProperty,
					ArrayUtils.contains(DogsDataProvider.ORDERABLE_DOG_PROPERTIES, dogProperty));
			if (DogsDataProvider.IMAGE_PROPERTY.equals(dogProperty)) {
				Json.RawValue renderFunction = new Json.RawValue(renderFunctionValue);

				column.setRender(renderFunction);
			}
			datatableColumns.add(column);
		}

		DataTables<Dog, String> table = new DataTables("dogTable", columns);
		table.addTopToolbar(new SpanHeadersToolbar<>(table));
		CharSequence ajaxUrl = urlFor(new DogDataVirtualScrollResourceReference(), null);
		ScrollerOptions scrollerOptions = new ScrollerOptions();
		scrollerOptions.loadingIndicator(true).displayBuffer(100).serverWait(500);

		Options options = table.getOptions();

		AbstractConfig ajaxConfig = new AbstractConfig() {
		};
		ajaxConfig.put(new Key<>("url", null), ajaxUrl);
		ajaxConfig.put(new Key<>("type", null), "POST");

		IKey<AbstractConfig> ajaxConfigKey = new Key<>(Options.Ajax.key(), null);

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

		ImagePreview<Void> imagePreview = new ImagePreview<>("image-preview");
		imagePreview.header(Model.<String>of(getString("view-image")));
		add(imagePreview);

	}

	private class DogDataVirtualScrollResourceReference extends AbstractVirtualScrollResourceReference<Dog> {

		public DogDataVirtualScrollResourceReference() {
			super(DogDataVirtualScrollResourceReference.class, "dogsDataVitrualScrollResRef");
		}

		@Override
		protected void populateEntryJson(JSONObject entryJson, Dog dog) {
			entryJson.put("DT_RowId", "PK_" + dog.getId());
			entryJson.put("DT_RowClass", "custom");

			/** The map to look up localized values for enums. */
			final Map<Enum<?>, String> dogPropertyValues = new HashMap<>();
			dogPropertyValues.put(Sex.M, getString("male"));
			dogPropertyValues.put(Sex.F, getString("female"));
			dogPropertyValues.put(Sterilized.NOT_STERLIZED, getString("no"));
			dogPropertyValues.put(Sterilized.STERLIZED, getString("yes"));
			dogPropertyValues.put(Behavior.FRIENDLY, getString("friendly"));
			dogPropertyValues.put(Behavior.AGGRESSIVE, getString("aggressive"));

			CompoundPropertyModel<Dog> dogPropertyModel = CompoundPropertyModel.of(Model.of(dog));
			for (String dogProperty : DogsDataProvider.DOG_PROPERTIES) {
				Object value = dogPropertyModel.bind(dogProperty).getObject();
				if (value != null) {
					if (value instanceof Collection) {
						entryJson.put(dogProperty, new JSONArray((Collection<?>) value));
					} else {
						value = StringUtils.defaultString(dogPropertyValues.get(value) , value.toString());
						entryJson.put(dogProperty, value);
					}
				} else {
					entryJson.put(dogProperty, StringUtils.EMPTY);
				}
			}
		}

		@Override
		protected IDataProvider<Dog> getDataProvider(PageParameters parameters) {
			return new DogsDataProvider(dogRepository, parameters);
		}

		@Override
		protected String generateResponse(PageParameters params) {
			IRequestParameters parameters = getRequest().getRequestParameters();

			PageParameters pageParameters = new PageParameters();
			for (String paramName : parameters.getParameterNames()) {
				List<StringValue> values = parameters.getParameterValues(paramName);
				String[] valuesArray = new String[values.size()];
				int i = 0;
				for (StringValue val : values) {
					valuesArray[i++] = val.toString();
				}
				pageParameters.add(paramName, valuesArray);
			}
			return super.generateResponse(pageParameters);
		}

	}

}
