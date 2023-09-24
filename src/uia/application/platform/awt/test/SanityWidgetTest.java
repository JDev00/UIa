package uia.application.platform.awt.test;

import uia.application.platform.awt.ContextAWT;
import uia.application.platform.awt.test.artefacts.ComponentTracker;
import uia.application.*;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.core.Font;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.physical.wrapper.WrapperViewGroup;
import uia.utility.Figure;
import uia.utility.TrigTable;
import uia.utility.Utility;

public class SanityWidgetTest extends WrapperViewGroup {

    public SanityWidgetTest(Context context) {
        super(new ComponentGroup(new Component("TEST", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        getPaint().setColor(Theme.DARK_GREY);


        UIButtonFilled buttonFilled = new UIButtonFilled(
                new Component("BUTTON_0", 0.85f, 0.25f, 0.15f, 0.1f), true
        );
        buttonFilled.getPaint()
                .setColor(Theme.TRANSPARENT)
                .setStrokeColor(Theme.WHITE)
                .setStrokeWidth(4);
        buttonFilled.getViewText().getTextPaint()
                .setColor(Theme.WHITE);


        UIButtonSwitch buttonSwitch = new UIButtonSwitch(
                new Component("BUTTON_1", 0.85f, 0.5f, 0.15f, 0.1f)
        );


        UIButtonList buttonList = new UIButtonList(new Component("BUTTON_2", 0.85f, 0.75f, 0.15f, 0.1f));
        buttonList.setText("1\n1", "2", "3", "4", "5");


        /*UIBar bar = new UIBar(new Component("BAR", 0.25f, 0.15f, 0.15f, 0.01f));
        bar.setRange(0, 1000);
        bar.setValue(0f);
        bar.setText(s -> Utility.limitDecimals(s, 1) + " %");
        bar.rotate(TrigTable.PI);
        bar.getViewText().getFont().setSize(30f).setStyle(Font.STYLE.BOLD);
        bar.addCallback((OnClick) pointers -> bar.rotate(bar.geometryBounds()[2] + TrigTable.HALF_PI));*/


        UICalendar calendar = new UICalendar(new Component("CALENDAR", 0.15f, 0.7f, 0.25f, 0.5f));
        calendar.setRotation(0.05f);


        ComponentTracker tracker = new ComponentTracker(
                new Component("FPS", 0.05f, 0.05f, 0.1f, 0.1f), context
        );


        add(buttonFilled, buttonSwitch, buttonList, calendar, tracker);
    }

    //

    private static View createView() {
        ViewText out = new ComponentText(new Component("TEXT", 0f, 0f, 0.9f, 0.2f));
        out.setText("HELLO!");
        out.getPaint().setColor(ThemeDarcula.W_FOREGROUND);
        return out;
    }

    //

    public static void main(String[] args) {
        int[] dim = ContextAWT.getScreenSize();
        Context context = new ContextAWT(4 * dim[0] / 5, 4 * dim[1] / 5);
        context.start();
        context.setView(new SanityWidgetTest(context));
    }
}
