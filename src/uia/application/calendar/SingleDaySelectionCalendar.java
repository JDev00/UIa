package uia.application.calendar;

import uia.core.ui.style.StyleFunction;
import uia.core.basement.Drawable;
import uia.physical.theme.Theme;
import uia.core.ui.style.Style;
import uia.utility.Geometries;
import uia.core.ui.View;

/**
 * Standard UIa component.
 * <br>
 * Gregorian calendar with single day selection.
 */

public class SingleDaySelectionCalendar extends AbstractCalendarView {
    private final StyleFunction deselectedCellPaint = style -> style
            .setBackgroundColor(Theme.TRANSPARENT)
            .setTextColor(Theme.WHITE);
    private final StyleFunction selectedCellPaint = style -> style
            .setBackgroundColor(Theme.ROYAL_BLUE)
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
            Style cellStyle = getDayCellStyle(i);
            if (isDayMarkedAsSelected(i)) {
                cellStyle.applyStyleFunction(selectedCellPaint);
            } else {
                cellStyle.applyStyleFunction(deselectedCellPaint);
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
        notifyCallbacks(onSelectionClear.class, this);
    }
}
