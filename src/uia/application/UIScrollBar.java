package uia.application;

import uia.application.platform.awt.ContextAWT;
import uia.core.ScreenPointer;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnMouseHover;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.scroller.Scroller;
import uia.physical.scroller.WheelScroller;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.physical.wrapper.WrapperView;
import uia.utility.Utility;

import java.util.Arrays;

import static uia.utility.TrigTable.*;
import static java.lang.Math.abs;

/**
 * UI scrollbar
 */

public class UIScrollBar extends WrapperView {
    private final View cursor;

    private Scroller scroller;

    private float val;

    private boolean locked = false;

    public UIScrollBar(View view) {
        super(new ComponentGroup(view));

        buildGeometry(g -> Component.buildRect(g, bounds(), 1f), true);
        getPaint().setColor(ThemeDarcula.W_BACKGROUND);
        addCallback((OnMouseHover) pointers -> {
            ScreenPointer pointer = pointers.get(0);

            if (pointer.getAction().equals(ScreenPointer.ACTION.DRAGGED)) {
                locked = true;
                updateScroll(pointer.getX(), pointer.getY());
            } else if (scroller.update(pointers)) {
                setScroll(scroller.getValue());
            }
        });


        cursor = new Component("CURSOR", 0.5f, 0.075f, 1f, 0.15f);
        cursor.buildGeometry(g -> Component.buildRect(g, cursor.bounds(), 1f), true);
        cursor.setConsumer(CONSUMER.POINTER, false);
        cursor.getPaint().setColor(Theme.RED);


        scroller = new WheelScroller();
        scroller.setFactor(0.1f);
        scroller.setMax(1f);


        ((ComponentGroup) getView()).add(cursor);
    }

    private void updateScroll(float x, float y) {
        float[] bounds = bounds();

        float x_off = x / bounds[2];
        float y_off = y / bounds[3];

        setScroll(abs(rotY(x_off, y_off, cos(bounds[4]), sin(bounds[4]))));
    }

    /**
     * @return the View used as cursor
     */

    public View getCursor() {
        return cursor;
    }

    /**
     * Set the scroll factor.
     * <br>
     * Scroll factor is used to determine the number of scrolls to do before reach the maximum value.
     * By default, is set to 0.1.
     *
     * @param factor a value between [0,1]
     */

    public void setScrollFactor(float factor) {
        scroller.setFactor(Utility.constrain(factor, 0f, 1f));
    }

    /**
     * Set the scroll amount
     *
     * @param scroll the scroll amount between [0, 1]
     */

    public void setScroll(float scroll) {
        val = Utility.constrain(scroll, 0f, 1f);
        scroller.setValue(val);

        float off = 0.5f * Component.dimensionWithoutRotation(cursor.bounds())[1] / Component.dimensionWithoutRotation(bounds())[1];
        cursor.setPosition(0.5f, Utility.constrain(val, off, 1 - off));
    }

    /**
     * Scroll this bar of the given amount
     *
     * @param amount a value between [0, 1]
     */

    public void scroll(float amount) {
        setScroll(val + amount);
    }

    /**
     * @return the current scroll amount
     */

    public float getScroll() {
        return val;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);

        if (!isVisible) locked = false;
    }

    @Override
    public void update(View parent) {
        super.update(parent);
        System.out.println(Arrays.toString(Component.dimensionWithoutRotation(cursor.bounds())));
        //System.out.println(Arrays.toString(bounds()));
    }

    //

    public static void main(String[] args) {
        View view = new UIScrollBar(new Component("TEST", 0.5f, 0.5f, 0.1f, 0.5f));
        view.setRotation(HALF_PI / 2f);

        Context context = new ContextAWT(1000, 500);
        context.start();
        context.setView(view);
    }
}
