package test;

import test.artefacts.ComponentTracker;
import uia.application.UIButtonFilled;
import uia.application.UIButtonList;
import uia.application.UIButtonSwitch;
import uia.application.UICalendar;
import uia.application.awt.ContextAWT;
import uia.core.ui.View;
import uia.core.ui.context.Context;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentImage;
import uia.physical.ComponentText;
import uia.physical.theme.Theme;
import uia.utility.Utility;

public class Sanity {

    public static View createView(String id, float x, float y, float width, float height) {
        return new Component(id, x, y, width, height);
    }

    public static ViewText createViewText(String id, float x, float y, float width, float height) {
        return new ComponentText(new Component(id, x, y, width, height));
    }

    public static ViewGroup createViewGroup(String id, float x, float y, float width, float height) {
        return new ComponentGroup(new Component(id, x, y, width, height));
    }

    /**
     * Create a new ViewGroup that fills the entire screen.
     * Its dimension is the screen one and is positioned at the screen center.
     *
     * @return a new {@link ViewGroup} instance
     */

    public static ViewGroup createRoot() {
        ViewGroup result = createViewGroup("ROOT_GROUP", 0.5f, 0.5f, 1f, 1f);
        result.getPaint().setColor(Theme.BACKGROUND);
        return result;
    }

    public static ViewText createTracker(Context context) {
        return new ComponentTracker(context, 0.95f, 0.95f, 0.1f, 0.1f);
    }

    /**
     * Create a new ContextAWT with width: 720 and height: 540 and start it.
     *
     * @return a new {@link ContextAWT}
     */

    public static Context createMockContext() {
        Context context = new ContextAWT(720, 540);
        context.setLifecycleStage(Context.LifecycleStage.RUN);
        return context;
    }

    public static void showBaseComponents() {
        ViewGroup root = createRoot();

        Context context = createMockContext();
        context.getWindow().show();
        context.setView(root);

        ViewText text = createViewText("TEXT", 0.33f, 0.45f, 0.5f, 0.75f);
        text.setAlign(ViewText.AlignX.RIGHT);
        text.setText(Utility.readAll("src\\test\\Sanity.java"));
        text.setRotation(0.3f);
        text.setSingleLine(true);

        ComponentImage image = new ComponentImage(new Component("IMAGE", 0.7f, 0.5f, 0.33f, 0.5f));
        image.getImage().load("sample\\img0.png");
        image.registerCallback((OnClick) pointers -> System.out.println("ComponentImage clicked!"));

        ViewGroup group = createViewGroup("GROUP", 0.4f, 0.5f, 0.5f, 0.5f);
        group.getPaint().setColor(Theme.DARK_GREY);
        group.add(image, text);

        ViewText tracker = createTracker(context);

        root.add(group, tracker);
    }

    public static void showWidgets() {
        ViewGroup root = createRoot();

        Context context = createMockContext();
        context.getWindow().show();
        context.setView(root);

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

        ViewText tracker = createTracker(context);

        root.add(buttonFilled, buttonSwitch, buttonList, calendar, tracker);
    }

    public static void main(String[] args) {
        showBaseComponents();
    }
}
