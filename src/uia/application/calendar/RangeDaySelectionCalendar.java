package uia.application.calendar;

import uia.core.ui.style.StyleFunction;
import uia.physical.ui.Theme;
import uia.core.ui.style.Style;
import uia.utility.Geometries;
import uia.core.ui.View;

/**
 * Standard UIa component.
 * <br>
 * Gregorian calendar with day range selection.
 */

public class RangeDaySelectionCalendar extends AbstractCalendarView {
    private final StyleFunction deselectedCellPaint = style -> style
            .setBackgroundColor(Theme.TRANSPARENT)
            .setTextColor(Theme.WHITE);
    private final StyleFunction selectedCellPaint = style -> style
            .setBackgroundColor(Theme.ROYAL_BLUE)
            .setTextColor(Theme.WHITE);

    private final int[] range = {-1, -1};

    public RangeDaySelectionCalendar(View view, String[] weekdays, String[] months) {
        super(view, weekdays, months);

        // updates day selection
        registerCallback((OnDaySelect) day -> {
            dayRangeSelection(day);
            updateDayStyle();
        });
    }

    @Override
    public void selectDay(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("the day must be between [1, 31]. " + day + " provided");
        }
        notifyCallbacks(OnDaySelect.class, day);
    }

    @Override
    public void deselectDay(int day) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearDaySelection() {
        deselectsAllDays();
        updateDayStyle();
        notifyCallbacks(onSelectionClear.class, this);
    }

    /**
     * Helper function. Updates the day cells selection color.
     */

    private void updateDayStyle() {
        for (int i = 1; i <= 31; i++) {
            Style cellStyle = getDayCellStyle(i);
            if (isDayMarkedAsSelected(i)) {
                cellStyle.applyStyleFunction(selectedCellPaint);
            } else {
                cellStyle.applyStyleFunction(deselectedCellPaint);
            }
        }
    }

    /**
     * Helper function. Deselects all days.
     */

    private void deselectsAllDays() {
        range[0] = range[1] = -1;
        for (int i = 1; i <= 31; i++) {
            setDayCellGeometry(i, Geometries::rect, false);
            markDayAsSelected(i, false);
        }
    }

    /**
     * Helper function. Selects a range of days.
     *
     * @param day the selected day; with -1 the range is cleared
     */

    private void dayRangeSelection(int day) {
        // updates range selection
        if (range[0] == -1) {
            range[0] = day;
        } else if (range[1] == -1) {
            range[1] = day;
        } else {
            range[0] = day;
            range[1] = -1;
        }

        // clears the selection range
        for (int i = 1; i <= 31; i++) {
            markDayAsSelected(i, false);
        }

        // update cell selection
        int minValue = Math.min(range[0], range[1]);
        int maxValue = Math.max(range[0], range[1]);
        for (int i = 1; i <= 31; i++) {
            markDayAsSelected(i, i == day || range[1] >= 0 && i >= minValue && i <= maxValue);
            setDayCellGeometry(i, Geometries::rect, false);
        }

        // update cell geometry
        if (range[1] != -1) {
            setDayCellGeometry(
                    minValue,
                    g -> Geometries.rect(g, Geometries.STD_VERT, 1f, 0f, 0f, 1f, getDayCelWidth() / getDayCelHeight()),
                    true);

            setDayCellGeometry(
                    maxValue,
                    g -> Geometries.rect(g, Geometries.STD_VERT, 0f, 1f, 1f, 0f, getDayCelWidth() / getDayCelHeight()),
                    true);

            if (range[0] == range[1]) {
                setDayCellGeometry(
                        range[0],
                        g -> Geometries.rect(g, Geometries.STD_VERT, 1f, getDayCelWidth() / getDayCelHeight()),
                        true);
            }
        }
    }
}
