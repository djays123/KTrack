package ktrack.entity;

import org.apache.wicket.model.ResourceModel;

public enum Behavior {
	FRIENDLY {

		@Override
		public String toString() {
			return "friendly";
		}

	},

	AGGRESSIVE {

		@Override
		public String toString() {
			return "aggressive";
		}

	},
	
	TIMID {

		@Override
		public String toString() {
			return "timid";
		}

	};
	
	public final String getDisplayString() {
		return new ResourceModel(toString()).getObject();
	}

}
