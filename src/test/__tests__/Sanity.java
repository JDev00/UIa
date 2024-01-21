package test.__tests__;

import uia.application.UIButtonFilled;
import uia.application.UIButtonList;
import uia.application.UIToggleButton;
import uia.application.UICalendar;
import uia.application.desktop.ContextSwing;
import uia.core.ui.View;
import uia.core.ui.context.Context;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.physical.Component;
import uia.physical.ComponentImage;
import uia.physical.theme.Theme;
import uia.utility.IOUtility;

import java.io.File;

import static test.__tests__.utility.TestUtility.*;

/**
 * Sanity tests
 */

public class Sanity {

    /**
     * Shows the based UIa components
     */

    public static void showBaseComponents() {
        ViewGroup root = createRoot();

        Context context = ContextSwing.createAndStart(1000, 600);
        context.setView(root);

        ViewText text = createViewText("TEXT", 0.33f, 0.45f, 0.5f, 0.75f);
        text.setAlign(ViewText.AlignX.RIGHT);
        text.setText(IOUtility.readAll(new File("src\\test\\Sanity.java")));
        text.setRotation(0.3f);
        text.setSingleLine(true);

        ComponentImage image = new ComponentImage(new Component("IMAGE", 0.7f, 0.5f, 0.33f, 0.5f));
        image.getImage().load("sample\\img0.png");
        image.registerCallback((OnClick) touches -> System.out.println("ComponentImage clicked!"));

        ViewGroup group = createViewGroup("GROUP", 0.4f, 0.5f, 0.5f, 0.5f);
        group.getPaint().setColor(Theme.DARK_GRAY);
        ViewGroup.insert(group, image, text);

        View tracker = createTracker();
        ViewGroup.insert(root, group, tracker);
    }

    /**
     * Shows the UIa widgets component
     */

    public static void showWidgets() {
        ViewGroup root = createRoot();

        Context context = createMockContext();
        context.setView(root);

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

        UIButtonList buttonList = new UIButtonList(new Component("BUTTON_2", 0.85f, 0.75f, 0.15f, 0.1f));
        buttonList.setText("1\n1", "2", "3", "4", "5");

        UICalendar calendar = new UICalendar(new Component("CALENDAR", 0.15f, 0.7f, 0.25f, 0.5f));
        calendar.setRotation(0.05f);

        View tracker = createTracker();
        ViewGroup.insert(root, buttonFilled, buttonSwitch, buttonList, calendar, tracker);
    }

    public static void main(String[] args) {
        //showBaseComponents();
        showWidgets();
    }
}
