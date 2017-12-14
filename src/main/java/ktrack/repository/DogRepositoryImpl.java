package ktrack.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
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

    @Override
    public List<String> findDistinctEmail(String email, int maxRecords) {

        Query findByEmail = new Query();
        findByEmail.addCriteria(Criteria.where("caregiverEmail").regex(email)).fields()
                .include("caregiverEmail");
        if(maxRecords > 0) {
            findByEmail.limit(maxRecords);
        }
        
        return (List<String>) operations.getCollection("dogs").distinct("caregiverEmail", findByEmail.getQueryObject());

    }
    

    @Override
    public List<String> findDistinctMobile(String mobile, int maxRecords) {

        Query findByMobile = new Query();
        findByMobile.addCriteria(Criteria.where("caregiverMobile").regex(mobile)).fields()
                .include("caregiverMobile");
        if(maxRecords > 0) {
            findByMobile.limit(maxRecords);
        }

        return (List<String>) operations.getCollection("dogs").distinct("caregiverMobile", findByMobile.getQueryObject());

    }
    
    @Override
    public Dog findByEmail(String email) {
        Query findByEmail = new Query();
        findByEmail.addCriteria(Criteria.where("caregiverEmail").is(email));
       
        return operations.findOne(findByEmail, Dog.class, "dogs");
    }
    
    @Override
    public Dog findByMobile(String mobile) {
        Query findByEmail = new Query();
        findByEmail.addCriteria(Criteria.where("caregiverMobile").is(mobile));
        
        return operations.findOne(findByEmail, Dog.class, "dogs");
    }

}
