package ktrack.ui;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ktrack.ui.panels.BookingPreview;
import ktrack.ui.panels.CalendarPanel;

@MountPath("/book")
public class BookSlot extends BaseAuthenticatedPage {

	/**
	 * The serial version id.
	 */
	private static final long serialVersionUID = 3599765021503968315L;

	public BookSlot(final PageParameters pageParams) {
		super(pageParams);	
		add(new BookingPreview<>("booking-preview"));
		add(new CalendarPanel("calendar-panel"));
		
	}


	
}
