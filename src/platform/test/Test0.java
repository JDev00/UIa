package platform.test;

import uia.application.BarUI;
import uia.application.ButtonFilledUI;
import uia.application.ButtonListUI;
import uia.application.ButtonSwitchUI;
import uia.core.architecture.Context;
import uia.core.architecture.ui.View;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;
import uia.physical.ui.ComponentText;
import uia.core.architecture.ui.ViewText;
import uia.physical.ui.wrapper.WrapperViewGroup;
import uia.utils.Utils;

public class Test0 extends WrapperViewGroup {

    public Test0(Context context) {
        super(new ComponentGroup(new Component("Test0", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        getPaint().setColor(120, 120, 50);


        ButtonFilledUI buttonFilledUI = new ButtonFilledUI(new Component("Button0", 0.75f, 0.25f, 0.25f, 0.1f), true);
        buttonFilledUI.getPaint()
                .setColor(0, 0, 0, 1)
                .setStrokeWidth(3)
                .setStrokeColor(255);

        ButtonSwitchUI buttonSwitchUI = new ButtonSwitchUI(new Component("Button1", 0.75f, 0.5f, 0.25f, 0.1f));

        ButtonListUI buttonListUI = new ButtonListUI(new Component("Button2", 0.75f, 0.75f, 0.25f, 0.1f));
        buttonListUI.setText("1", "2", "3", "4", "5");


        ViewText text = new ComponentText(new Component("Text", 0.25f, 0.5f, 0.45f, 0.95f));
        text.setAlign(ViewText.AlignX.LEFT);
        text.setText(Utils.readAll("..\\uia\\src\\uia\\physical\\ui\\Component.java"));


        ComponentGroup group = new ComponentGroup(new Component("Group", 0.5f, 0.5f, 0.5f, 0.5f)
                .setExpanseLimit(1.005f, 1.005f));
        group.getPaint().setColor(0, 255, 0);
        group.add(text, buttonFilledUI, buttonSwitchUI, buttonListUI);
        //group.setRotation(0.85f);


        BarUI barUI = new BarUI(new Component("Bar", 0.5f, 0.5f, 0.25f, 0.1f)) {
            @Override
            public void update(View parent) {
                super.update(parent);
                setRotation(bounds()[4] + 0.01f);
                //System.out.println(bounds()[4]);
            }
        };
        barUI.setRange(0, 100);
        barUI.setValue(50);


        ViewText tracker = new TrackerComponent(new Component("Tracker", 0.1f, 0.1f, 0.2f, 0.2f), context);
        tracker.getPaint().setColor(255, 0, 0);
        tracker.setAlign(ViewText.AlignY.CENTER);


        add(tracker, group);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        setRotation(boundsShape()[4] + 0.005f);
    }
}
