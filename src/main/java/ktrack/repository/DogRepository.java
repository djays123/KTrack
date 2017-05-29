package ktrack.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ktrack.entity.Dog;

@Repository
public interface DogRepository extends MongoRepository<Dog, String> {
	

}