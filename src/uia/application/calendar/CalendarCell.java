package uia.application.calendar;

import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnMouseEnter;
import uia.core.ui.callbacks.OnMouseExit;
import uia.physical.component.Component;
import uia.physical.component.ComponentText;
import uia.physical.component.WrapperViewText;

/**
 * CalendarCell is a generic view used to represent a calendar day or a week day.
 */

public class CalendarCell extends WrapperViewText {
    public boolean selected = false;
    public boolean current = false;
    public boolean active = false;

    public CalendarCell(String id) {
        super(new ComponentText(
                new Component(id, 0f, 0f, 0f, 0f))
        );
        registerCallback((OnMouseEnter) touches -> active = true);
        registerCallback((OnMouseExit) touches -> active = false);
        setConsumer(Consumer.SCREEN_TOUCH, false);
        setAlign(ViewText.AlignY.CENTER);
    }

    /**
     * Creates a new CalendarCell suitable for representing a week day.
     *
     * @param weekDay the day of the week
     * @return a new {@link CalendarCell}
     */

    public static CalendarCell createWeekDay(String weekDay) {
        CalendarCell result = new CalendarCell(weekDay);
        result.setText(weekDay);
        return result;
    }

    /**
     * Creates a new CalendarCell suitable for representing a day.
     *
     * @param day the calendar day
     * @return a new {@link CalendarCell}
     */

    public static CalendarCell createDay(String day) {
        CalendarCell result = new CalendarCell(day);
        result.setText(day);
        return result;
    }
}
