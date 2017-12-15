package ktrack.ui;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import ktrack.ui.panels.CalendarPanel;

public class AdminPage extends BaseAuthenticatedPage {

    public AdminPage(PageParameters pageParams) {
        super(pageParams);
        add(new CalendarPanel("calendar-panel"));
    }

}
