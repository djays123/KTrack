package ktrack.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.template.PackageTextTemplate;

import ktrack.ui.panels.CalendarOptions;
import ktrack.ui.panels.CalendarPanel;
import ktrack.ui.panels.StatusPanel;
import ktrack.ui.panels.CalendarOptions.Event;

public class AdminPage extends BaseAuthenticatedPage {

    public AdminPage(PageParameters pageParams) {
        super(pageParams);
        FeedbackPanel feedback = new StatusPanel("feedback");
        add(feedback);
        Form<Void> form = new Form<Void>("save-admin-form");
        add(form);
        
        PackageTextTemplate dayTemplate = new PackageTextTemplate(getClass(), "AdminDaySlot.html");
        
		CalendarPanel calendarPanel = new CalendarPanel("calendar-panel");
		CalendarOptions calendarOptions = calendarPanel.getOptions();

		
		calendarOptions.showNext(1);
		calendarOptions.showPrevious(0);
		calendarOptions.cellBorder(true);
		calendarOptions.today(true);
		calendarOptions.dayTemplate(dayTemplate.asString());
		calendarOptions.disableBeforeToday(true);
		
		List<Event> events = new ArrayList<>();
		Event event = new Event();
		event.applyTemplate(true);
		event.calDate("2017-12-17");
		event.title("Slot info");
		event.Classname("evt");
		events.add(event);
		
		calendarOptions.events(events);
		
        form.add(calendarPanel);
    }

}
