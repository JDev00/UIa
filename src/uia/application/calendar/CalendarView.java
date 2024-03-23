package uia.application.calendar;

import java.util.List;

/**
 * CalendarView ADT.
 * <br>
 * CalendarView is responsible for drawing a calendar.
 */

public interface CalendarView {
    String[] WEEK = {
            "M", "T", "W", "T", "F", "S", "S"
    };
    String[] MONTHS = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    /**
     * Sets the current calendar date.
     *
     * @param day   the current calendar day between [1, 31]
     * @param month the current calendar month between [1, 12]
     * @param year  the current calendar year
     */

    void setDate(int day, int month, int year);

    /**
     * @return the current calendar date
     */

    int[] getDate();

    /**
     * Selects the specified day.
     *
     * @param day the day to be selected
     */

    void selectDay(int day);

    /**
     * Deselects the specified day.
     *
     * @param day the day to be deselected; if -1 is given, all the selected days will be deselected.
     */

    void deselectDay(int day);

    /**
     * @return all the selected days
     */

    List<Integer> getSelectedDays();
}
