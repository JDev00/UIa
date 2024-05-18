package uia.application.calendar;

import uia.core.basement.Callback;

/**
 * OnDateSet is triggered when the calendar date is set.
 * <br>
 * It provides the set date as an array consisting of: day, month and year.
 */

public interface OnDateSet extends Callback<int[]> {
}
