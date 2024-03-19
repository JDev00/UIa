package uia.application.calendar;

import uia.core.basement.Callback;

/**
 * OnSelect is a callback invoked when a day is selected on a calendar.
 * <br>
 * It provides an array filled with the first and last day of the selection range.
 */

public interface OnSelect extends Callback<Integer[]> {
}
