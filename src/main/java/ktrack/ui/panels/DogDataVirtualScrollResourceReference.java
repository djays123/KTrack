package ktrack.ui.panels;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.datatables.virtualscroll.AbstractVirtualScrollResourceReference;

import ktrack.WebApp;
import ktrack.entity.Behavior;
import ktrack.entity.Dog;
import ktrack.entity.Sex;
import ktrack.entity.Sterilized;
import ktrack.ui.DogsDataProvider;

public class DogDataVirtualScrollResourceReference extends AbstractVirtualScrollResourceReference<Dog> {

	/** The dogs data provider. */

	public DogDataVirtualScrollResourceReference() {
		super(DogDataVirtualScrollResourceReference.class, "dogsDataVitrualScrollResRef");
	}

	@Override
	protected void populateEntryJson(JSONObject entryJson, Dog dog) {
		entryJson.put("DT_RowId", "PK_" + dog.getId());
		entryJson.put("DT_RowClass", "custom");

		/** The map to look up localized values for enums. */
		final Map<Enum<?>, String> dogPropertyValues = new HashMap<>();
		dogPropertyValues.put(Sex.M, Sex.M.getDisplayString());
		dogPropertyValues.put(Sex.F, Sex.F.getDisplayString());
		dogPropertyValues.put(Sterilized.NOT_STERLIZED, Sterilized.NOT_STERLIZED.getDisplayString());
		dogPropertyValues.put(Sterilized.STERLIZED, Sterilized.STERLIZED.getDisplayString());
		dogPropertyValues.put(Behavior.FRIENDLY, Behavior.FRIENDLY.getDisplayString());
		dogPropertyValues.put(Behavior.AGGRESSIVE, Behavior.AGGRESSIVE.getDisplayString());

		CompoundPropertyModel<Dog> dogPropertyModel = CompoundPropertyModel.of(Model.of(dog));
		for (String dogProperty : DogsDataProvider.DOG_PROPERTIES) {
			Object value = dogPropertyModel.bind(dogProperty).getObject();
			if (value != null) {
				if (value instanceof Collection) {
					entryJson.put(dogProperty, new JSONArray((Collection<?>) value));
				} else {
					value = StringUtils.defaultString(dogPropertyValues.get(value), value.toString());
					entryJson.put(dogProperty, value);
				}
			} else {
				entryJson.put(dogProperty, StringUtils.EMPTY);
			}
		}

		entryJson.put(DogsDataProvider.DOG_ID_PROPERTY,
				dogPropertyModel.bind(DogsDataProvider.DOG_ID_PROPERTY).getObject());
	}

	@Override
	protected IDataProvider<Dog> getDataProvider(PageParameters parameters) {
		return new DogsDataProvider(((WebApp) WebApp.get()).getDogRepository(), parameters);
	}

	@Override
	protected String generateResponse(PageParameters params) {
		IRequestParameters parameters = RequestCycle.get().getRequest().getRequestParameters();

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
