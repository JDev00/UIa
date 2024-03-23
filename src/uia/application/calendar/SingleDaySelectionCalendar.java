package uia.application.calendar;

import uia.core.basement.Drawable;
import uia.core.paint.Paint;
import uia.core.ui.View;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.utility.Geometries;

/**
 * Implementation of {@link CalendarView} for single day selection.
 */

public class SingleDaySelectionCalendar extends AbstractCalendarView {
    private final Paint[] paintCell = {
            new Paint().setColor(Theme.TRANSPARENT),
            new Paint().setColor(ThemeDarcula.BLUE)
    };

    public SingleDaySelectionCalendar(View view) {
        super(view);

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
                cellPaint.set(paintCell[1]);
            } else {
                cellPaint.set(paintCell[0]);
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
