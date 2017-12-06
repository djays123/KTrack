package ktrack.ui.panels;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.wicket.model.IModel;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BootstrapRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.EnumRadioChoiceRenderer;

public class DogAttributeEnumRadioGroup extends BootstrapRadioGroup<Enum<?>> {

	public DogAttributeEnumRadioGroup(String id, IModel<Enum<?>> model, Class<? extends Enum> enumClass) {
		super(id, model,  Arrays.asList(enumClass.getEnumConstants()));
        setChoiceRenderer(new EnumRadioChoiceRenderer(Type.Primary, this) {
			
			@Override
			protected String resourceKey(Enum object) {
				return object.toString();
			}
			
			@Override
			public String getButtonClass(Serializable option) {
				return Type.Primary.cssClassName() + " btn-sm small ";
			}
			
			
		});
	    setOutputMarkupId(true);
	}
}
