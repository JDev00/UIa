package uia.application.calendar;

import uia.core.ui.View;

import java.util.Objects;

import static uia.application.calendar.CalendarView.*;

/**
 * Calendars class is designed to create the desired calendar on the fly.
 */

public class Calendars {

    private Calendars() {
    }

    /**
     * Creates a new {@link CalendarView} instance with {@link CalendarView#WEEK} and {@link CalendarView#MONTHS}.
     *
     * @param calendarClass the calendar to be created
     * @param view          the backbone view of the calendar
     * @return a new {@link CalendarView} instance
     * @throws NullPointerException if {@code calendarClass == null || view == null}
     */

    public static CalendarView create(Class<? extends CalendarView> calendarClass, View view) {
        Objects.requireNonNull(calendarClass);
        Objects.requireNonNull(view);

        CalendarView result = null;
        if (calendarClass.equals(SingleDaySelectionCalendar.class)) {
            result = new SingleDaySelectionCalendar(view, WEEK, MONTHS);
        } else if (calendarClass.equals(RangeDaySelectionCalendar.class)) {
            result = new RangeDaySelectionCalendar(view, WEEK, MONTHS);
        }

        return result;
    }
}
