package ktrack.ui.panels;

import org.apache.wicket.model.CompoundPropertyModel;

import ktrack.entity.Sex;

public class SexPanel extends DogAttributeBooleanRadioGroup{

	public SexPanel(String id, CompoundPropertyModel<?> compoundModel, String property) {
		super(id, compoundModel, property, Sex.class);
	}
}
