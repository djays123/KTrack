package ktrack.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Tracks the available and the booked slots in a day.
 * @author dsharma
 *
 */
public class DailySlotBooking implements Serializable {

    /**
     * The default serial version id.
     */
    private static final long serialVersionUID = 1L;

	/** The total slots available in a day. */
	private Integer totalSlots;
	
	/** The booked slots in a day. */
	private Integer bookedSlots;
	
	/** The date represented by this record. */
	private Date slotDate;

	public Integer getTotalSlots() {
		return totalSlots;
	}

	public void setTotalSlots(Integer totalSlots) {
		this.totalSlots = totalSlots;
	}

	public Integer getBookedSlots() {
		return bookedSlots;
	}

	public void setBookedSlots(Integer bookedSlots) {
		this.bookedSlots = bookedSlots;
	}

	public Date getSlotDate() {
		return slotDate;
	}

	public void setSlotDate(Date slotDate) {
		this.slotDate = slotDate;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((slotDate == null) ? 0 : slotDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DailySlotBooking other = (DailySlotBooking) obj;
        if (slotDate == null) {
            if (other.slotDate != null)
                return false;
        } else if (!slotDate.equals(other.slotDate))
            return false;
        return true;
    }
	
}
