package ktrack.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import ktrack.entity.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String>, QueryDslPredicateExecutor<Booking> {

	@Query("{'dailySlotBookings.slotDate': {$gte: ?0}}")
	Booking getWithSlotStartDate(final String startDate);
}
