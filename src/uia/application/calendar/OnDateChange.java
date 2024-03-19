package uia.application.calendar;

import uia.core.basement.Callback;

/**
 * OnDateChange is a callback that is invoked when the calendar month or year is changed.
 * <br>
 * It provides the date as an array made of: day, month and year.
 */

public interface OnDateChange extends Callback<Integer[]> {
}
