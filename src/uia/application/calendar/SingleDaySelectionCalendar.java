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
            deselectAllDays();
            if (!isDayAlreadySelected) {
                _selectDay(day);
            }
            updateDayStyle();
        });
    }

    /**
     * Helper function. Deselects all days.
     */

    private void deselectAllDays() {
        for (int i = 1; i <= 31; i++) {
            setDayCellGeometry(i, Geometries::rect, false);
            markDayAsSelected(i, false);
        }
    }

    /**
     * Helper function. Selects the specified day.
     */

    private void _selectDay(int day) {
        markDayAsSelected(day, true);
        setDayCellGeometry(day,
                g -> Drawable.buildRect(g, getDayCelWidth(), getDayCelHeight(), 1f),
                true);
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
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("the day must be between [1, 31]. " + day + " provided");
        }

        notifyCallbacks(OnDaySelect.class, day);
    }

    @Override
    public void deselectDay(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("the day must be between [1, 31]. " + day + " provided");
        }

        if (isDayMarkedAsSelected(day)) {
            notifyCallbacks(OnDaySelect.class, day);
        }
    }

    @Override
    public void clearDaySelection() {
        deselectAllDays();
        updateDayStyle();
        notifyCallbacks(OnSelectionCleared.class, this);
    }
}
