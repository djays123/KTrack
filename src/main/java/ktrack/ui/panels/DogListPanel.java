package ktrack.ui.panels;

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
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.wicketstuff.datatables.DataTables;
import org.wicketstuff.datatables.columns.SpanColumn;
import org.wicketstuff.datatables.columns.SpanHeadersToolbar;
import org.wicketstuff.datatables.options.Column;
import org.wicketstuff.datatables.options.Options;
import org.wicketstuff.datatables.options.ScrollerOptions;
import org.wicketstuff.datatables.options.SelectOptions;
import org.wicketstuff.datatables.options.SelectOptions.Style;
import org.wicketstuff.datatables.virtualscroll.AbstractVirtualScrollResourceReference;

import de.agilecoders.wicket.jquery.AbstractConfig;
import de.agilecoders.wicket.jquery.IKey;
import de.agilecoders.wicket.jquery.Key;
import de.agilecoders.wicket.jquery.util.Json;
import ktrack.WebApp;
import ktrack.entity.Behavior;
import ktrack.entity.Dog;
import ktrack.entity.Sex;
import ktrack.entity.Sterilized;
import ktrack.repository.DogNamesRepository;
import ktrack.repository.DogRepository;
import ktrack.security.wicket.AuthenticatedSession;
import ktrack.security.wicket.AuthenticatedSession.AuthenticatedWebSession;
import ktrack.ui.DogsDataProvider;
import ktrack.ui.ImagePreview;
import ktrack.ui.NewDogPage;
import ktrack.ui.SnapshotResource;

/**
 * The panel that displays a list of dogs.
 * 
 * @author dsharma
 *
 */
public class DogListPanel extends Panel {
	@SpringBean
	private DogRepository dogRepository;

	@SpringBean
	private DogNamesRepository dogNamesRepository;


	/**
	 * The constructor.
	 * 
	 * @param id
	 *            The panel id.
	 * @param query
	 *            The query used to populate the dog list, can be null to
	 *            indicate all dogs in the system should be displayed.
	 */
	public DogListPanel(String id, Query query) {
		super(id);
		((AuthenticatedWebSession)getWebSession()).setSearchQuery(query);

		List<IColumn<Dog, ? extends Object>> columns = new ArrayList<>(DogsDataProvider.DOG_PROPERTIES.length);

		List<Column> datatableColumns = new ArrayList<>(DogsDataProvider.DOG_PROPERTIES.length);

		String snapshotUrl = urlFor(((WebApp) getWebApplication()).getSnapshotResourceReference(),
				(PageParameters) null).toString();
		TextTemplate renderThumbNailJSCode = new PackageTextTemplate(getClass(), "js/doglist.js", "text/javascript",
				"UTF-8");
		Map<String, Object> vars = new HashMap<String, Object>();
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

		SelectOptions selectOptions = new SelectOptions().style(Style.Single).selector("td:not(:last-child)");

		IKey<AbstractConfig> ajaxConfigKey = new Key<>(Options.Ajax.key(), null);

		options.columns(datatableColumns).serverSide(true).ordering(true).searching(true).scrollY("300")
				.deferRender(true).scroller(scrollerOptions).dom("Bfrti") // "p"
																			// is
																			// removed
																			// because
																			// we
																			// use
																			// Scroller
																			// (virtual
																			// scrolling)
				// .scrollCollapse(true)
				.stateSave(true).info(true).processing(false).retrieve(true).select(selectOptions)
				.put(ajaxConfigKey, ajaxConfig);

		String[] buttonsArray = new String[] { "copy", "csv", "excel", "pdf", "print" };
		options.put(new Key<String[]>("buttons", null), buttonsArray);

		add(table);

		ImagePreview<Void> imagePreview = new ImagePreview<>("image-preview");
		imagePreview.header(Model.<String>of(getString("view-image")));
		add(imagePreview);

		HiddenField<String> editDogKey = new HiddenField<String>("edit-dog-key", Model.of(new String()), String.class);
		Form<Void> editDog = new Form<Void>("edit-dog") {

			@Override
			protected void onSubmit() {
				String dogId = editDogKey.getValue();
				Dog dogToEdit = dogRepository.findOne(dogId);
				if (dogToEdit != null) {
					final PageParameters dogPageParameters = new PageParameters();
					dogPageParameters.add(NewDogPage.DOG_PARAM, dogId);
					setResponsePage(NewDogPage.class, dogPageParameters);
				}
			}

		};

		editDog.add(editDogKey);

		add(editDog);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
						"js/datatables/dataTables.buttons.min.js", getLocale(), getStyle(), getVariation())),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
						"js/datatables/buttons.flash.min.js", getLocale(), getStyle(), getVariation())),
				"footer-container"));

		response.render(
				new FilteredHeaderItem(
						JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
								"js/datatables/jszip.min.js", getLocale(), getStyle(), getVariation())),
						"footer-container"));
		response.render(
				new FilteredHeaderItem(
						JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
								"js/datatables/pdfmake.min.js", getLocale(), getStyle(), getVariation())),
						"footer-container"));
		response.render(
				new FilteredHeaderItem(
						JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
								"js/datatables/vfs_fonts.js", getLocale(), getStyle(), getVariation())),
						"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
						"js/datatables/buttons.html5.min.js", getLocale(), getStyle(), getVariation())),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
						"js/datatables/buttons.print.min.js", getLocale(), getStyle(), getVariation())),
				"footer-container"));

		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(
						new CssResourceReference(getPage().getClass(), "css/datatables/buttons.dataTables.min.css")),
				"footer-container"));
	}

}
