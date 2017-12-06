package ktrack.entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.google.gson.Gson;

/**
 * A class that dispenses search queries for Dogs.
 * 
 * @author dsharma
 *
 */
public class QueryFactory {

	/**
	 * A class that holds the query data.
	 */
	private static class QueryHolder {
		@SuppressWarnings("unused")
		private String queryProvider;

		@SuppressWarnings("unused")
		private Dog dog;
	};

	public static interface IDogQuery {
		/**
		 * Returns the query to run this search.
		 * 
		 * @param dogJson
		 *            The dog object.
		 */
		Query getQuery(Dog dog);

		/**
		 * Returns the query to run this search.
		 * 
		 * @param dog
		 *            The dog object.
		 */
		String getQueryString(Dog dog);

	};

	private static abstract class DogQueryProvider implements IDogQuery {
		@Override
		public final String getQueryString(Dog dog) {
			QueryHolder queryHolder = new QueryHolder();
			queryHolder.queryProvider = getQueryProvider().name();
			queryHolder.dog = dog;
			return new Gson().toJson(queryHolder);
		};

		protected abstract QUERYPROVIDERS getQueryProvider();

	};

	/**
	 * Class that implements the search by date and kennel behavior.
	 */
	private static class SearchByKennelOrDates extends DogQueryProvider {

		@Override
		public final Query getQuery(Dog dog) {
			Query query = new Query();
			if (dog.getArrivalDate() != null) {
				query.addCriteria(Criteria.where("arrivalDate").is(dog.getArrivalDate()));
			}
			if (dog.getSurgeryDate() != null) {
				query.addCriteria(Criteria.where("surgeryDate").is(dog.getSurgeryDate()));
			}
			if (dog.getReleaseDate() != null) {
				query.addCriteria(Criteria.where("releaseDate").is(dog.getReleaseDate()));
			}
			if (dog.getKennel() != null) {
				query.addCriteria(Criteria.where("kennel").is(dog.getKennel()));
			}

			return query;
		}

		@Override
		protected final QUERYPROVIDERS getQueryProvider() {
			return QUERYPROVIDERS.DATE_KENNEL;
		}

	}

	/**
	 * Class that implements the search by caregiver behavior.
	 */
	private static class SearchByCaregiver extends DogQueryProvider {

		@Override
		public final Query getQuery(Dog dog) {

			Query query = new Query();
			if (StringUtils.isNotEmpty(dog.getCaregiver())) {
				query.addCriteria(Criteria.where("caregiver").is(dog.getCaregiver()));
			}
			if (StringUtils.isNotEmpty(dog.getCaregiverMobile())) {
				query.addCriteria(Criteria.where("caregiverMobile").is(dog.getSurgeryDate()));
			}
			return query;
		}

		@Override
		protected final QUERYPROVIDERS getQueryProvider() {
			return QUERYPROVIDERS.CAREGIVER;
		}

	}

	/**
	 * Class that implements the search by vet behavior.
	 */
	private static class SearchByVet extends DogQueryProvider {

		@Override
		public final Query getQuery(Dog dog) {

			Query query = new Query();
			if (StringUtils.isNotEmpty(dog.getVetrinarian())) {
				query.addCriteria(Criteria.where("vetrinarian").is(dog.getVetrinarian()));
			}
			
			return query;
		}

		@Override
		protected final QUERYPROVIDERS getQueryProvider() {
			return QUERYPROVIDERS.VET;
		}

	}

	/**
	 * The available query providers.
	 * 
	 * @author dsharma
	 *
	 */
	public enum QUERYPROVIDERS {
		DATE_KENNEL {

			@Override
			public IDogQuery getQueryProvider() {
				return new SearchByKennelOrDates();
			}

		},

		CAREGIVER {
			@Override
			public IDogQuery getQueryProvider() {
				return new SearchByCaregiver();
			}

		},

		VET {
			@Override
			public IDogQuery getQueryProvider() {
				return new SearchByVet();
			}

		};

		public abstract IDogQuery getQueryProvider();

	};

}
