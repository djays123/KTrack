package ktrack.entity;

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
}
