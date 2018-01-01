package ktrack.entity;

import java.util.Date;

/**
 * Tracks the available and the booked slots in a day.
 * @author dsharma
 *
 */
public class DailySlotBooking {

	/** The total slots available in a day. */
	private int totalSlots;
	
	/** The booked slots in a day. */
	private int bookedSlots;
	
	/** The date represented by this record. */
	private Date slotDate;

	public int getTotalSlots() {
		return totalSlots;
	}

	public void setTotalSlots(int totalSlots) {
		this.totalSlots = totalSlots;
	}

	public int getBookedSlots() {
		return bookedSlots;
	}

	public void setBookedSlots(int bookedSlots) {
		this.bookedSlots = bookedSlots;
	}

	public Date getSlotDate() {
		return slotDate;
	}

	public void setSlotDate(Date slotDate) {
		this.slotDate = slotDate;
	}
	
	
	
}
