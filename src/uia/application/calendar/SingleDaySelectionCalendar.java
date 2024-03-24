package uia.application.calendar;

import uia.core.basement.Drawable;
import uia.core.paint.Paint;
import uia.core.ui.View;
import uia.physical.theme.Theme;
import uia.utility.Geometries;

/**
 * Implementation of {@link CalendarView} for single day selection.
 */

public class SingleDaySelectionCalendar extends AbstractCalendarView {
    private final Paint deselectedCellPaint = new Paint()
            .setColor(Theme.TRANSPARENT)
            .setTextColor(Theme.WHITE);
    private final Paint selectedCellPaint = new Paint()
            .setColor(Theme.ROYAL_BLUE)
            .setTextColor(Theme.WHITE);

    public SingleDaySelectionCalendar(View view, String[] weekdays, String[] months) {
        super(view, weekdays, months);

        // updates day selection
        registerCallback((OnDaySelect) day -> {
            boolean isDayAlreadySelected = isDayMarkedAsSelected(day);

            for (int i = 1; i <= 31; i++) {
                setDayCellGeometry(i, Geometries::rect, false);
                markDayAsSelected(i, false);
            }

            if (!isDayAlreadySelected) {
                markDayAsSelected(day, true);
                setDayCellGeometry(day,
                        g -> Drawable.buildRect(g, getDayCelWidth(), getDayCelHeight(), 1f),
                        true);
            }

            updateDayStyle();
        });
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

    @Override
    public void selectDay(int day) {
        notifyCallbacks(OnDaySelect.class, day);
    }

    @Override
    public void deselectDay(int day) {
        notifyCallbacks(OnDaySelect.class, day);
    }
}
