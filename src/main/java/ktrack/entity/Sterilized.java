package ktrack.entity;

import org.apache.wicket.model.ResourceModel;

public enum Sterilized {
	STERLIZED {

		@Override
		public String toString() {
			return "sterilized";
		}

	},

	NOT_STERLIZED {

		@Override
		public String toString() {
			return "not-sterilized";
		}

	};
	
	public final String getDisplayString() {
		return new ResourceModel(toString()).getObject();
	}

}
