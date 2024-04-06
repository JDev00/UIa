package uia.application.calendar;

import uia.core.ui.View;

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
     * Sets the calendar date.
     *
     * @param day   the calendar day between [1, 31]
     * @param month the calendar month between [1, 12]
     * @param year  the calendar year
     * @throws IllegalArgumentException if:
     *                                  <ul>
     *                                      <li>{@code day < 1 || day > 31};</li>
     *                                      <li>{@code month < 1 || month > 12};</li>
     *                                  </ul>
     */

    void setDate(int day, int month, int year);

    /**
     * @return the calendar set date as an array of three elements:
     * <ul>
     *     <li>the day between [1, 31];</li>
     *     <li>the month between [1, 12];</li>
     *     <li>the year.</li>
     * </ul>
     */

    int[] getSetDate();

    /**
     * Changes the calendar date but keeps the set date.
     *
     * @param month the new calendar month between [1, 12]
     * @param year  the new calendar year
     * @throws IllegalArgumentException if {@code month < 1 || month > 12}
     */

    void changeDate(int month, int year);

    /**
     * @return the current calendar date. It could be different from {@link #getSetDate()}.
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

    int[] getSelectedDays();
}
