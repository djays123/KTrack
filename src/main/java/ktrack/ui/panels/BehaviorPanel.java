package ktrack.ui.panels;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import ktrack.entity.Behavior;

public class BehaviorPanel extends DogAttributeEnumRadioGroup {

	public BehaviorPanel(String id, CompoundPropertyModel<?> compoundModel, String property) {
		super(id, new Model<Enum<?>>() {
			/**
			 * @see org.apache.wicket.model.IModel#getObject()
			 */
			@Override
			public Enum<?> getObject() {
				return (Enum<?>)compoundModel.bind(property).getObject();
			}

			/**
			 * Set the model object; calls setObject(java.io.Serializable). The model object
			 * must be serializable, as it is stored in the session
			 * 
			 * @param object
			 *            the model object
			 * @see org.apache.wicket.model.IModel#setObject(Object)
			 */
			@Override
			public void setObject(final Enum<?> object) {
				compoundModel.<Enum<?>>bind(property).setObject(object);
			}

		}, Behavior.class);
	}
}
