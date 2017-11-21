package ktrack.entity;

import org.apache.wicket.model.ResourceModel;

public enum Sex {
	M {

		@Override
		public String toString() {
			return "male";
		}

	},

	F {

		@Override
		public String toString() {
			return "female";
		}

	};
	
	
	public final String getDisplayString() {
		return new ResourceModel(toString()).getObject();
	}

}
