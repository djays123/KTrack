package ktrack.repository;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import ktrack.entity.Dog;

public interface CustomDogRepository {
	 List<Dog> findBy(Query criteria);
}
