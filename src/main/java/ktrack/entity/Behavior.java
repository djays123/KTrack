package ktrack.entity;

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

	};
}
