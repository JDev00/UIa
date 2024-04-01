package uia.application.calendar;

import uia.core.basement.Callback;

/**
 * OnDaySelect is triggered when a day is selected/deselected on the calendar.
 * <br>
 * It provides the selected/deselected day.
 */

public interface OnDaySelect extends Callback<Integer> {
}
