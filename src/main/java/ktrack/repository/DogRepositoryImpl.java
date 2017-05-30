package ktrack.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import ktrack.entity.Dog;

public class DogRepositoryImpl implements CustomDogRepository {
	private final MongoOperations operations;

	@Autowired
	public DogRepositoryImpl(MongoOperations operations) {

		Assert.notNull(operations, "MongoOperations must not be null!");
		this.operations = operations;
	}

	@Override
	public List<Dog> findBy(Query criteria) {
		return operations.find(criteria, Dog.class);
	}

}
