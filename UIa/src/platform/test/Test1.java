package platform.test;

import uia.application.UIButtonFilled;
import uia.application.UIButtonList;
import uia.application.UIButtonSwitch;
import uia.application.theme.Theme;
import uia.core.basement.Context;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.physical.wrapper.WrapperViewGroup;
import uia.utils.Figure;
import uia.utils.Utils;

public class Test1 extends WrapperViewGroup {

    public Test1(Context context) {
        super(new ComponentGroup(new Component("TEST", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        getPaint().setColor(Theme.BACKGROUND);


        UIButtonFilled UIButtonFilled = new UIButtonFilled(new Component("Button0", 0.75f, 0.25f, 0.25f, 0.1f), true);
        UIButtonFilled.getPaint()
                .setColor(Theme.TRANSPARENT)
                .setStrokeColor(Theme.WHITE)
                .setStrokeWidth(4);
        UIButtonFilled.getViewText().getTextPaint()
                .setColor(Theme.WHITE);


        UIButtonSwitch UIButtonSwitch = new UIButtonSwitch(new Component("BUTTON1", 0.75f, 0.5f, 0.25f, 0.1f));


        UIButtonList UIButtonList = new UIButtonList(new Component("BUTTON2", 0.75f, 0.75f, 0.25f, 0.1f));
        UIButtonList.setText("1\n1", "2", "3", "4", "5");


        ViewText text = new ComponentText(new Component("TEXT", 0.25f, 0.5f, 0.5f, 0.75f));
        text.setAlign(ViewText.AlignX.RIGHT);
        text.setText(Utils.readAll("..\\uia\\src\\platform\\test\\Test1.java"));
        text.setRotation(0.3f);


        ComponentGroup group = new ComponentGroup(new Component("GROUP", 0.33f, 0.5f, 0.5f, 0.5f)
                .setExpanseLimit(1.005f, 1.005f));
        group.buildGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, 0.2f, v.desc()[0] / v.desc()[1]), true);
        group.getPaint().setColor(Theme.DARK_GREY);
        group.setClip(false);
        group.add(text, UIButtonSwitch, UIButtonList, UIButtonFilled);


        TrackerComponent trackerComponent = new TrackerComponent(
                new Component("FPS", 0.05f, 0.05f, 0.1f, 0.1f), context
        );


        add(group, trackerComponent);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        try {
            View view = ((ViewGroup) get("GROUP")).get("TEXT");
            //view.setRotation(view.desc()[2] + 0.0025f);
        } catch (Exception ignored) {
        }
    }
}
