package test.__tests__.sanity;

import uia.application.UIButtonFilled;
import uia.application.UIButtonList;
import uia.application.UIToggleButton;
import uia.application.calendar.UICalendar;
import uia.core.ui.context.Context;
import uia.core.ui.ViewGroup;
import uia.physical.component.Component;
import uia.physical.theme.Theme;

import static test.__tests__.utility.TestUtility.*;

/**
 * Sanity test.
 */

public class Widgets {

    /**
     * Builds some widgets component.
     */

    public static ViewGroup buildWidgets() {
        UIButtonFilled buttonFilled = new UIButtonFilled(
                new Component("BUTTON_0", 0.85f, 0.25f, 0.15f, 0.1f), true
        );
        buttonFilled.getPaint()
                .setColor(Theme.TRANSPARENT)
                .setStrokeColor(Theme.WHITE)
                .setStrokeWidth(4);
        buttonFilled.getTextPaint()
                .setColor(Theme.WHITE);

        UIToggleButton buttonSwitch = new UIToggleButton(
                new Component("BUTTON_1", 0.85f, 0.5f, 0.15f, 0.1f)
        );

        UIButtonList buttonList = new UIButtonList(
                new Component("BUTTON_2", 0.85f, 0.75f, 0.15f, 0.1f)
        );
        buttonList.setText("1\n1", "2", "3", "4", "5");

        UICalendar calendar = new UICalendar(
                new Component("CALENDAR", 0.15f, 0.7f, 0.25f, 0.5f)
        );
        calendar.setRotation(0.05f);

        ViewGroup result = createRoot();
        ViewGroup.insert(result, buttonFilled, buttonSwitch, buttonList, calendar);
        return result;
    }

    public static void main(String[] args) {
        ViewGroup root = buildWidgets();

        Context context = createMockContext();
        context.setView(root);
    }
}
