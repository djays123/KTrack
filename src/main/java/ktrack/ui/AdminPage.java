package ktrack.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import com.querydsl.core.types.Predicate;

import ktrack.entity.Booking;
import ktrack.entity.DailySlotBooking;
import ktrack.entity.Dog;
import ktrack.repository.BookingRepository;
import ktrack.ui.panels.CalendarOptions;
import ktrack.ui.panels.CalendarOptions.Event;
import ktrack.ui.panels.CalendarPanel;
import ktrack.ui.panels.StatusPanel;

public class AdminPage extends BaseAuthenticatedPage {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AdminPage.class);
    
    /** The booking repository. */
    @SpringBean
    private BookingRepository bookingRepository;
    
    /** The maximum number of months to be shown in the future. */
    private static final int MAX_NEXT_MONTHS = 1;

    public AdminPage(PageParameters pageParams) {
        super(pageParams);
        FeedbackPanel feedback = new StatusPanel("feedback");
        add(feedback);
        
        final SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        DateTime date = new DateTime();
        
        Booking booking = bookingRepository.getWithSlotStartDate(dtf.format(date.toDate()));
        
        if(booking == null) {
            booking = bookingRepository.findOne((Predicate)null);
        }
        CompoundPropertyModel<Booking> bookingModel = new CompoundPropertyModel<Booking>(Model.of(booking));
        Form<Booking> form = new Form<Booking>("save-admin-form", bookingModel);
        add(form);

        
        CalendarPanel calendarPanel = new CalendarPanel("calendar-panel");
        CalendarOptions calendarOptions = calendarPanel.getOptions();

        calendarOptions.showNext(MAX_NEXT_MONTHS);
        calendarOptions.showPrevious(0);
        calendarOptions.cellBorder(true);
        calendarOptions.today(true);

        calendarOptions.disableBeforeToday(true);

        List<Event> events = new ArrayList<>();
       
        calendarOptions.events(events);

        form.add(calendarPanel);

       
        final Set<DailySlotBooking> dailyBookingSlots = booking.getDailySlotBookings();
         
        /*
         * Populate the slots from today till the end of MAX_NEXT_MONTHS.
         */
        
        int currentMonth = date.getMonthOfYear();
        while(date.getMonthOfYear() - currentMonth <= MAX_NEXT_MONTHS) {
            DailySlotBooking d = new DailySlotBooking();
            d.setSlotDate(date.toDate());
            if(! dailyBookingSlots.contains(d)) {
                dailyBookingSlots.add(d);
            }
             date = date.plusDays(1);            
        } 

        RefreshingView<DailySlotBooking> dailySlotBookings = new RefreshingView<DailySlotBooking>("slots") {

            @Override
            protected Iterator<IModel<DailySlotBooking>> getItemModels() {
                return new ModelIteratorAdapter<DailySlotBooking>(dailyBookingSlots.iterator()) {

                    @Override
                    protected IModel<DailySlotBooking> model(DailySlotBooking object) {
                        return new CompoundPropertyModel<DailySlotBooking>(object);
                    }

                };
            }

            @Override
            protected void populateItem(Item<DailySlotBooking> item) {
                item.setOutputMarkupId(true);
                // populate the row of the repeater
                DailySlotBooking dailySlotBooking = item.getModel().getObject();
                item.add(new Label("bookedSlots"));
                item.add(new NumberTextField<Integer>("totalSlots", Integer.class)
                        .setMinimum(dailySlotBooking.getBookedSlots()).setMaximum(Booking.MAX_BOOKINGS_PER_DAY)
                        .setStep(1));
                
                Event event = new Event();
                event.badge(true);
                event.calDate(dtf.format(dailySlotBooking.getSlotDate()));
                event.dayHTML(item.getMarkupId());
                events.add(event);

            }

        };
        
        form.add(dailySlotBookings);
    }

}
