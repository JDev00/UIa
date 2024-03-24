package uia.application.calendar;

import uia.core.paint.Paint;
import uia.core.ui.View;
import uia.physical.theme.Theme;
import uia.utility.Geometries;

/**
 * Implementation of {@link CalendarView} for range day selection.
 */

// TODO: to complete
public class RangeDaySelectionCalendar extends AbstractCalendarView {
    private final Paint deselectedCellPaint = new Paint()
            .setColor(Theme.TRANSPARENT)
            .setTextColor(Theme.WHITE);
    private final Paint selectedCellPaint = new Paint()
            .setColor(Theme.ROYAL_BLUE)
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
        notifyCallbacks(OnDaySelect.class, day);
    }

    @Override
    public void deselectDay(int day) {
        notifyCallbacks(OnDaySelect.class, day);
    }

    @Override
    public void clearDaySelection() {

    }

    /**
     * Helper function. Updates the day cells selection color.
     */

    private void updateDayStyle() {
        for (int i = 1; i <= 31; i++) {
            Paint cellPaint = getDayCellPaint(i);
            if (isDayMarkedAsSelected(i)) {
                cellPaint.set(selectedCellPaint);
            } else {
                cellPaint.set(deselectedCellPaint);
            }
        }
    }

    /**
     * Selects a range of days.
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
