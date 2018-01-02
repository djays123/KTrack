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
    
    public static final IKey<Boolean> DisableBeforeToday = new Key<>("disableBeforeToday", false);

    public static final IKey<Integer> ShowPrevious = new Key<>("show_previous", -1);

    public static final IKey<Integer> ShowNext = new Key<>("show_next", -1);

    public static final IKey<Boolean> CellBorder = new Key<>("cell_border", false);

    public static final IKey<Boolean> Today = new Key<>("today", false);
    	
    public static final IKey<List<Event>> Events = new Key<>("data", null);
    
    public static final IKey<String> EventTrigger = new Key<>("eventTrigger", "");
    
    public static final IKey<String> EventTarget = new Key<>("eventTarget", "");
    
   
    
    public CalendarOptions eventTrigger(String eventTrigger) {
    	put(EventTrigger, eventTrigger);
    	return this;
    }
    
    public CalendarOptions eventTarget(String eventTarget) {
    	put(EventTarget, eventTarget);
    	return this;
    }
    
    public CalendarOptions language(String language) {
    	put(Language, language);
    	return this;
    }
    
    
    public CalendarOptions disableNoEvents(boolean disableNoEvents) {
        put(DisableNoEvents, disableNoEvents);
        return this;
    }
    
    public CalendarOptions disableBeforeToday(boolean disableBeforeToday) {
        put(DisableBeforeToday, disableBeforeToday);
        return this;
    }
    
    public CalendarOptions showPrevious(Integer showPrevious) {
        put(ShowPrevious, showPrevious);
        return this;
    }
    
    public CalendarOptions showNext(Integer showNext) {
        put(ShowNext, showNext);
        return this;
    }
    
    public CalendarOptions cellBorder(boolean cellBorder) {
        put(CellBorder, cellBorder);
        return this;
    }
    
    public CalendarOptions today(boolean today) {
        put(Today, today);
        return this;
    }
    
    public CalendarOptions events(List<Event> events) {
        put(Events, events);
        return this;
    }
    
    /**
     * Encapsulates a day event.
     */
    public static class Event extends AbstractConfig {
        public static final IKey<String> CalDate = new Key<>("date", "");
        
        public static final IKey<Boolean> Badge = new Key<>("badge", false);

        public static final IKey<Boolean> Modal = new Key<>("modal", false);
        
        public static final IKey<String> Title = new Key<>("title", "");
        
        public static final IKey<String>  Classname = new Key<>("classname", "event");
        
        public static final IKey<String> DayHTML = new Key<>("dayHTML", "");
        
        public Event dayHTML(String dayHTML) {
            put(DayHTML, dayHTML);
            return this;
        }
        
        public Event Classname(CharSequence classname) {
            put(Classname, String.valueOf(classname));
            return this;
        }
        
        public Event calDate(CharSequence calDate) {
            put(CalDate, String.valueOf(calDate));
            return this;
        }
        
        public Event badge(boolean badge) {
            put(Badge, badge);
            return this;
        }
        
        
        public Event modal(boolean modal) {
            put(Modal, modal);
            return this;
        }
        
        public Event title(CharSequence title) {
            put(Title, String.valueOf(title));
            return this;
        }

    }

}
