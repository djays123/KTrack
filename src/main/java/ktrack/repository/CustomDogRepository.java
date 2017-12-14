package ktrack.repository;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import ktrack.entity.Dog;

public interface CustomDogRepository {
	 List<Dog> findBy(Query criteria);
	 
	
	 List<String> findDistinctEmail(String email, int maxRecords);
	 
	 List<String> findDistinctMobile(String email, int maxRecords);
	 
	
	 Dog findByEmail(String email);
	 
	 Dog findByMobile(String mobile);
	
}
