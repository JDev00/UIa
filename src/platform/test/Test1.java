package platform.test;

import uia.application.ButtonFilledUI;
import uia.application.ButtonListUI;
import uia.application.ButtonSwitchUI;
import uia.core.architecture.Context;
import uia.core.architecture.ui.View;
import uia.core.architecture.ui.ViewText;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;
import uia.physical.ui.ComponentText;
import uia.physical.ui.wrapper.WrapperViewGroup;
import uia.utils.Utils;

public class Test1 extends WrapperViewGroup {
    private Context context;

    public Test1(Context context) {
        super(new ComponentGroup(new Component("Test1", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        this.context = context;

        getPaint().setColor(50);

        ButtonFilledUI buttonFilledUI = new ButtonFilledUI(new Component("Button0", 0.75f, 0.25f, 0.25f, 0.1f), true);
        buttonFilledUI.getPaint()
                .setColor(0, 0, 0, 1)
                .setStrokeWidth(3)
                .setStrokeColor(255);

        ButtonSwitchUI buttonSwitchUI = new ButtonSwitchUI(new Component("Button1", 0.75f, 0.5f, 0.25f, 0.1f));

        ButtonListUI buttonListUI = new ButtonListUI(new Component("Button2", 0.75f, 0.75f, 0.25f, 0.1f));
        buttonListUI.setText("1", "2", "3", "4", "5");


        ViewText text = new ComponentText(new Component("Text", 0.25f, 0.5f, 0.45f, 0.5f));
        text.setAlign(ViewText.AlignX.LEFT);
        text.setText(Utils.readAll("..\\uia\\src\\uia\\physical\\ui\\Component.java"));
        text.setSingleLine(true);


        ComponentGroup group = new ComponentGroup(new Component("ViewGroup", 0.25f, 0.5f, 0.5f, 0.5f)
                .setExpanseLimit(1.005f, 1.005f));
        group.getPaint().setColor(0, 255, 0);
        group.add(text, buttonFilledUI, buttonSwitchUI, buttonListUI);
        group.setClip(false);


        TrackerComponent trackerComponent = new TrackerComponent(new Component("ViewFPS", 0.05f, 0.05f, 0.1f, 0.1f), context);


        add(group, trackerComponent);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        try {
            View view = get("ViewGroup");
            view.setRotation(view.boundsShape()[4] + 0.001f);
        } catch (NullPointerException ignored) {
        }
    }
}
