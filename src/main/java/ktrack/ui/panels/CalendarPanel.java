package ktrack.ui.panels;

import static de.agilecoders.wicket.jquery.JQuery.$;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import de.agilecoders.wicket.jquery.JQuery;
import de.agilecoders.wicket.jquery.util.Strings2;

public class CalendarPanel extends Panel {
    /** The id of the div that will hold the calendar. */
    private static final String CALENDAR_DIV_ID = "slot-booking-calendar";
    
    private final CalendarOptions options;

    public CalendarPanel(String id) {
        super(id);
        options = new CalendarOptions();
    }
    
    public CalendarOptions getOptions() {
        return options;
    }
    
    /**
     * @return An identifier that could be used to lookup the zabuto calendar after initialization
     */
    public String getJsHandle() {
        return "WS_ZC_" + Strings2.escapeMarkupId(CALENDAR_DIV_ID);
    }
    
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        if (isEnabledInHierarchy()) {
            String zabutoCalendarFunction = $(JQuery.markupId(CALENDAR_DIV_ID)).chain("zabuto_calendar", getOptions()).get();
            String setup = String.format("window['%s'] = %s", getJsHandle(), zabutoCalendarFunction);
            response.render(OnDomReadyHeaderItem.forScript(setup));
        }
        
        response.render(new FilteredHeaderItem(CssHeaderItem.forReference(FontAwesomeCssReference.instance()),
                "footer-container"));

        response.render(new FilteredHeaderItem(
                JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(),
                        "js/zabuto/zabuto_calendar.js", getLocale(), getStyle(), getVariation())),
                "footer-container"));
        response.render(new FilteredHeaderItem(
                CssHeaderItem.forReference(
                        new CssResourceReference(getClass(), "css/zabuto/zabuto_calendar.min.css")),
                "footer-container"));
    }

}
