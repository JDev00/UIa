package uia.application.calendar;

import uia.core.ui.View;

import java.util.List;

/**
 * CalendarView ADT.
 * <br>
 * CalendarView is responsible for drawing a calendar.
 */

public interface CalendarView extends View {
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
     * @throws IllegalArgumentException if {@code day < 1 || day > 31}
     */

    void selectDay(int day);

    /**
     * Deselects the specified day.
     *
     * @param day the day to be deselected; if -1 is given, all the selected days will be deselected
     * @throws IllegalArgumentException      if {@code day < 1 || day > 31}
     * @throws UnsupportedOperationException if this method is not supported on the specific implementation
     */

    void deselectDay(int day);

    /**
     * Deselects all the selected days.
     */

    void clearDaySelection();

    /**
     * @return all the selected days
     */

    List<Integer> getSelectedDays();
}
