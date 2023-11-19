package uia.application;

import uia.core.ScreenTouch;
import uia.core.basement.Drawable;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.callbacks.OnMouseExit;
import uia.core.ui.callbacks.OnMouseHover;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.physical.WrapperView;
import uia.utility.Utility;

/**
 * UI scrollbar
 */

public class UIScrollBar extends WrapperView {
    //private final float SCROLL_FACTOR = 0.05f;
    private final View internalBar;
    private float val;
    private boolean locked = false;

    public UIScrollBar(View view) {
        super(new ComponentGroup(view));

        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);
        getPaint().setColor(ThemeDarcula.W_BACKGROUND);
        registerCallback((OnClick) touches -> {
            ScreenTouch touch = touches.get(0);
            updateScroll(touch.getX(), touch.getY());
        });
        registerCallback((OnMouseHover) touches -> {
            ScreenTouch touch = touches.get(0);
            if (touch.getAction().equals(ScreenTouch.Action.DRAGGED)) {
                if (!locked) {
                    locked = true;
                    sendMessage(EventTouchScreenMessage.requestLock(getID()));
                }
                updateScroll(touch.getX(), touch.getY());
            }
            if (touch.getAction().equals(ScreenTouch.Action.RELEASED)) {
                if (locked) {
                    locked = false;
                    sendMessage(EventTouchScreenMessage.releaseLock(getID()));
                }
            }
        });
        registerCallback((OnMouseExit) o -> {
            if (locked) {
                locked = false;
                sendMessage(EventTouchScreenMessage.releaseLock(getID()));
            }
        });

        internalBar = new Component("SCROLLBAR_INTERNAL_BAR", 0.5f, 0.25f, 0.85f, 0.5f);
        internalBar.setGeometry(
                g -> Drawable.buildRect(g, internalBar.getWidth(), internalBar.getHeight(), 1f),
                true
        );
        internalBar.setConsumer(Consumer.SCREEN_TOUCH, false);
        internalBar.getPaint().setColor(Theme.LIGHT_GREY);

        this.<ComponentGroup>getView().add(internalBar);
        this.<ComponentGroup>getView().setClip(false);
    }

    private void updateScroll(float x, float y) {
        float[] bounds = bounds();

        //System.out.println(x + ", " + y);

        float x_off = x / bounds[2];
        float y_off = y / bounds[3];

        setScrollValue(y_off);

        //setScrollValue(abs(rotY(x_off, y_off, cos(bounds[4]), sin(bounds[4]))));
    }

    /**
     * Set the internal bar relative height
     *
     * @param height the internal bar height between [0, 1]
     */

    public void setInternalBarHeight(float height) {
        internalBar.setDimension(0.9f, Utility.constrain(height, 0.1f, 0.9f));
    }

    /**
     * @return the View used as cursor
     */

    public View getInternalBar() {
        return internalBar;
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
        //scroller.setFactor(Utility.constrain(factor, 0f, 1f));
    }

    /**
     * Set the scrolling value
     *
     * @param value the scrolling value between [0, 1]
     */

    public void setScrollValue(float value) {
        val = Utility.constrain(value, 0f, 1f);

        float off = 0.5f * internalBar.getHeight() / getHeight();
        //float internalBarPositionY = (0.9f - 2 * off) * val + 0.05f + off;
        //internalBar.setPosition(0.5f, Utility.constrain(internalBarPositionY, 0.05f + off, 0.95f - off));
        internalBar.setPosition(0.5f, Utility.constrain(val, off, 1f - off));
    }

    /**
     * Scroll this bar of the specified amount
     *
     * @param amount a value between [0, 1]
     */

    public void scroll(float amount) {
        setScrollValue(val + amount);
    }

    /**
     * @return the current scroll amount between [0, 1]
     */

    public float getScrollValue() {
        return val;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (!isVisible) locked = false;
    }
}
