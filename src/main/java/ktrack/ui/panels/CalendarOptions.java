package ktrack.ui.panels;

import java.util.List;

import de.agilecoders.wicket.jquery.AbstractConfig;
import de.agilecoders.wicket.jquery.IKey;
import de.agilecoders.wicket.jquery.Key;

public class CalendarOptions extends AbstractConfig {
    /**
     * https://github.com/zabuto/calendar
     */
    public static final IKey<String> Language = new Key<>("language", "en");

    public static final IKey<Boolean> DisableNoEvents = new Key<>("disableNoEvents", false);

    public static final IKey<Boolean> ShowPrevious = new Key<>("show_previous", false);

    public static final IKey<Boolean> ShowNext = new Key<>("show_next", false);

    public static final IKey<Boolean> CellBorder = new Key<>("cell_border", false);

    public static final IKey<Boolean> Today = new Key<>("today", false);
    
    public static final IKey<List<Event>> Events = new Key<>("data", null);
    
    
    
    /**
     * Encapsulates a day event.
     */
    public static class Event extends AbstractConfig {
        public static final IKey<String> CalDate = new Key<>("date", "en");
        
        public static final IKey<Boolean> Badge = new Key<>("badge", false);

        public static final IKey<Boolean> Today = new Key<>("modal", false);
        
        public static final IKey<String> Title = new Key<>("title", "");
        
        public Event calDate(CharSequence calDate) {
            put(CalDate, String.valueOf(calDate));
            return this;
        }
        
        public Event badge(boolean badge) {
            put(Badge, badge);
            return this;
        }
        
        public Event today(boolean today) {
            put(Today, today);
            return this;
        }
        
        public Event title(CharSequence title) {
            put(CalDate, String.valueOf(title));
            return this;
        }

    }

}
