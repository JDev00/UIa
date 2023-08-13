package platform.test;

import uia.application.CalendarUI;
import uia.core.architecture.Context;
import uia.core.architecture.ui.View;
import uia.core.architecture.ui.ViewText;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;
import uia.physical.ui.wrapper.WrapperViewGroup;

public class Test2 extends WrapperViewGroup {

    public Test2(Context context) {
        super(new ComponentGroup(new Component("Test2", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        getPaint().setColor(50);


        View calendar = new CalendarUI(new Component("Calendar", 0.5f, 0.5f, 0.45f, 0.95f));


        ViewText tracker = new TrackerComponent(new Component("Tracker", 0.1f, 0.1f, 0.2f, 0.2f), context);
        tracker.getPaint().setColor(255, 0, 0);
        tracker.setAlign(ViewText.AlignY.CENTER);


        add(tracker, calendar);
    }
}
