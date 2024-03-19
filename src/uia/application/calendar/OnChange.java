package uia.application.calendar;

import uia.core.basement.Callback;

/**
 * OnChange is a callback that is invoked when the calendar month or year is changed.
 * <br>
 * It provides the date as an array made of: day, month and year.
 */

public interface OnChange extends Callback<Integer[]> {
}
