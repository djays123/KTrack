package ktrack.entity;

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

}
