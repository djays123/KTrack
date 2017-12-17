package ktrack.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.wicketstuff.annotation.mount.MountPath;

import ktrack.ui.panels.BookingPreview;
import ktrack.ui.panels.CalendarOptions;
import ktrack.ui.panels.CalendarOptions.Event;
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
		CalendarPanel calendarPanel = new CalendarPanel("calendar-panel");
		CalendarOptions calendarOptions = calendarPanel.getOptions();

		List<Event> events = new ArrayList<>();
		Event event = new Event();
		event.badge(true);
		event.modal(true);
		event.calDate("2017-12-12");
		event.title("Example 1");
		events.add(event);

		event = new Event();
		event.badge(true);
		event.modal(true);
		event.calDate("2017-12-13");
		event.title("Example 2");
		events.add(event);

		calendarOptions.events(events);
		calendarOptions.showNext(1);
		calendarOptions.showPrevious(0);
		calendarOptions.disableNoEvents(true);
		calendarOptions.cellBorder(true);
		calendarOptions.today(false);
		calendarOptions.eventTrigger("day:click");
		calendarOptions.eventTarget("slot-booking-calendar-events");

		add(calendarPanel);

	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
				getPage().getClass(), "js/bookslot.js", getLocale(), getStyle(), getVariation())), "footer-container"));

	}

}
