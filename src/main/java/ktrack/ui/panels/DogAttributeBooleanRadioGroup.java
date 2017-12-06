package ktrack.ui.panels;

import java.io.Serializable;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Classes;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioChoiceRenderer;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.EnumRadioChoiceRenderer;
import ktrack.entity.Behavior;

/**
 * A boolean radio group choice adapted for the dog form.
 * 
 * @author dsharma
 *
 */
public class DogAttributeBooleanRadioGroup extends BooleanRadioGroup {
	/** The enum holding the boolean options. */
	private Class<? extends Enum> choiceClazz;

	public DogAttributeBooleanRadioGroup(String id,  CompoundPropertyModel compoundModel, String property,
			Class<? extends Enum> choiceClazz) {
		super(id, new Model<Boolean>() {
			/**
			 * @see org.apache.wicket.model.IModel#getObject()
			 */
			@Override
			public Boolean getObject() {
				Enum<?> model = (Enum<?>) compoundModel.bind(property).getObject();
				return model.equals(choiceClazz.getEnumConstants()[0]) ? true : false;
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
			public void setObject(final Boolean object) {
				Enum<?> model = object ? choiceClazz.getEnumConstants()[0] : choiceClazz.getEnumConstants()[1];
				compoundModel.<Enum<?>>bind(property).setObject(model);
			}

		});
		this.choiceClazz = choiceClazz;

		setChoiceRenderer(
				new BooleanRadioChoiceRenderer(Type.Primary, this) {
					@Override
					protected String resourceKey(Boolean choice) {
						return choice ? choiceClazz.getEnumConstants()[0].toString()
								: choiceClazz.getEnumConstants()[1].toString();
					}

					@Override
					public String getButtonClass(Boolean option) {
						return Type.Primary.cssClassName() + " btn-sm small ";
					}
				});
	}
}