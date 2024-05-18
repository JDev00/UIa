package uia.application.calendar;

import uia.core.basement.Callback;

/**
 * OnDateChange is triggered when the calendar month or year is changed.
 * <br>
 * It provides the current date as an array consisting of: day, month and year.
 */

public interface OnDateChange extends Callback<int[]> {
}
