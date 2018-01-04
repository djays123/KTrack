package ktrack.entity;

import java.io.Serializable;
import java.util.Set;

import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.querydsl.core.annotations.QueryEntity;

/**
 * The single booking object in the system.
 * 
 * @author dsharma
 *
 */
@QueryEntity
@Entity
@Document(collection = "booking")
public class Booking implements Serializable {

	/**
	 * The default serial version id.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The default maximum number of bookings allowed per day.
	 */
	public static final int MAX_BOOKINGS_PER_DAY = 40;

	/**
	 * The default maximum number of bookings allowed to be booked in a
	 * transaction.
	 */
	public static final int MAX_BOOKINGS_PER_TXN = 6;

	/** The id. */
	@Id
	private String id;

	/**
	 * The maximum number of bookings allowed per day.
	 */
	private int maximumBookingsPerDay;

	/**
	 * The maximum number of bookings allowed to be booked in a transaction.
	 */
	private int maximumBookingsPerTxn;

	
	/** The number of days after today when the bookings will start. */
	private int startBookingsAfterDays;

	/**
	 * The slots booked each day.
	 */
	private Set<DailySlotBooking> dailySlotBookings;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMaximumBookingsPerDay() {
		return maximumBookingsPerDay;
	}

	public void setMaximumBookingsPerDay(int maximumBookingsPerDay) {
		this.maximumBookingsPerDay = maximumBookingsPerDay;
	}

	public int getMaximumBookingsPerTxn() {
		return maximumBookingsPerTxn;
	}

	public void setMaximumBookingsPerTxn(int maximumBookingsPerTxn) {
		this.maximumBookingsPerTxn = maximumBookingsPerTxn;
	}

	public Set<DailySlotBooking> getDailySlotBookings() {
		return dailySlotBookings;
	}

	public void setDailySlotBookings(Set<DailySlotBooking> dailySlotBookings) {
		this.dailySlotBookings = dailySlotBookings;
	}

	public int getStartBookingsAfterDays() {
		return startBookingsAfterDays;
	}

	public void setStartBookingsAfterDays(int startBookingsAfterDays) {
		this.startBookingsAfterDays = startBookingsAfterDays;
	}

}
