package test.__tests__.sanity;

import uia.application.UIProgressbar;
import uia.application.calendar.SingleDaySelectionCalendar;
import uia.application.calendar.CalendarView;
import uia.application.calendar.Calendars;
import uia.physical.ui.component.Component;
import uia.application.UIToggleButton;
import uia.application.UIButtonFilled;
import uia.application.UIButtonList;
import uia.core.context.Context;
import uia.physical.ui.Theme;
import uia.core.ui.ViewGroup;

import static test.__tests__.utility.TestUtility.*;

/**
 * Sanity test.
 */

public final class widgets {

    private widgets() {
    }

    /**
     * Builds some widgets component.
     */

    public static ViewGroup buildWidgets() {
        UIButtonFilled buttonFilled = new UIButtonFilled(
                new Component("BUTTON_0", 0.85f, 0.25f, 0.15f, 0.1f), true
        );
        buttonFilled.setText("Hello");
        buttonFilled.getStyle()
                .setBackgroundColor(Theme.TRANSPARENT)
                .setTextColor(Theme.LIGHT_CORAL)
                .setBorderColor(Theme.ROYAL_BLUE)
                .setBorderWidth(4)
                .setFontSize(40);

        UIToggleButton toggleButton = new UIToggleButton(
                new Component("BUTTON_1", 0.85f, 0.5f, 0.15f, 0.1f)
        );
        toggleButton.getStyle().setBackgroundColor(Theme.LIME);
        toggleButton.setEnabledStateStyleFunction(style -> style
                .setBackgroundColor(Theme.WHITE)
                .setTextColor(Theme.PINK)
        );

        UIButtonList buttonList = new UIButtonList(
                new Component("BUTTON_2", 0.85f, 0.75f, 0.15f, 0.1f)
        );
        buttonList.setValues("1\n1", "2", "3", "4", "5");
        buttonList.getStyle().setTextColor(Theme.BLUE);

        CalendarView calendar = Calendars.create(
                SingleDaySelectionCalendar.class,
                new Component("CALENDAR", 0.25f, 0.5f, 0.25f, 0.5f)
        );
        calendar.setDate(31, 3, 2024);
        calendar.getStyle().setRotation(0.05f);

        UIProgressbar progressbar = new UIProgressbar(
                new Component("PROGRESS_BAR", 0.5f, 0.5f, 0.25f, 0.1f)
        );
        progressbar.getStyle().setBackgroundColor(Theme.LIGHT_CORAL);
        progressbar.setValue(0.25f);

        ViewGroup result = createRoot();
        ViewGroup.insert(result, buttonFilled, toggleButton, buttonList, calendar, progressbar);
        return result;
    }

    public static void main(String[] args) {
        ViewGroup root = buildWidgets();

        Context context = createMockContext();
        context.setView(root);
    }
}
