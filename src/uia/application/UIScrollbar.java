package uia.application;

import uia.core.Paint;
import uia.core.ScreenTouch;
import uia.core.basement.Drawable;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.callbacks.OnMouseExit;
import uia.core.ui.callbacks.OnMouseHover;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.theme.Theme;
import uia.physical.WrapperView;
import uia.utility.Utility;

/**
 * Standard UIa component.
 * <br>
 * UIScrollbar represents a scrollbar component. It can be set vertical or horizontal at the creation time.
 * It consists of two graphical elements:
 * <ul>
 *     <li>an external bar that is accessed and modified using the standard interface;</li>
 *     <li>an internal bar that represents the current scroll value.</li>
 * </ul>
 * The internal bar can't be accessed directly.
 * <br>
 * By design, UIScrollbar supports dragging and moving the internal bar. The only operation that you have to
 * implement on your own is the scrolling caused by a mouse wheeling event.
 * <br>
 * The scroll value (accessed with {@link #getValue()}) is bounded between [0, max]. The maximum scroll value is set
 * with {@link #setMaxValue(float)}.
 *
 * @apiNote UIScrollbar doesn't fully support rotation, so rotate it carefully.
 */

public final class UIScrollbar extends WrapperView {
    private final View internalBar;
    private float val;
    private float max = 1f;
    private float barDragOffset;
    private boolean locked = false;
    private final boolean vertical;
    private boolean updateInternalBar = false;

    public UIScrollbar(View view, boolean vertical) {
        super(new ComponentGroup(view));

        this.vertical = vertical;

        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);
        getPaint().setColor(Theme.DARK_GRAY);
        registerCallback((OnClick) touches -> {
            ScreenTouch touch = touches.get(0);
            updateScroll(touch.getX(), touch.getY());
        });
        registerCallback((OnMouseHover) touches -> {
            ScreenTouch touch = touches.get(0);
            if (touch.getAction().equals(ScreenTouch.Action.DRAGGED)) {
                if (requestLock()) {
                    barDragOffset = getBarDragOffset(touch.getX(), touch.getY());
                }
                updateScroll(touch.getX() - barDragOffset, touch.getY() - barDragOffset);
            }
            if (touch.getAction().equals(ScreenTouch.Action.RELEASED)) {
                if (releaseLock()) {
                    barDragOffset = 0f;
                }
            }
        });
        registerCallback((OnMouseExit) o -> {
            if (releaseLock()) {
                barDragOffset = 0f;
            }
        });

        internalBar = vertical ? createVerticalBar() : createHorizontalBar();
        internalBar.setGeometry(
                g -> Drawable.buildRect(g, internalBar.getWidth(), internalBar.getHeight(), 1f),
                true
        );
        internalBar.setConsumer(Consumer.SCREEN_TOUCH, false);
        internalBar.getPaint().setColor(Theme.LIGHT_GRAY);

        ViewGroup group = getView();
        group.setClip(false);
        ViewGroup.insert(group, internalBar);
    }

    /**
     * Send a message to request the event messages lock
     */

    private boolean requestLock() {
        boolean result = false;
        if (!locked) {
            result = true;
            locked = true;
            sendMessage(EventTouchScreenMessage.requestLock(getID()));
        }
        return result;
    }

    /**
     * Send a message to release the event messages lock
     */

    private boolean releaseLock() {
        boolean result = false;
        if (locked) {
            result = true;
            locked = false;
            sendMessage(EventTouchScreenMessage.releaseLock(getID()));
        }
        return result;
    }

    /**
     * Helper function.
     *
     * @param x the screen touch position on the x-axis
     * @return the bar drag offset on the x-axis
     */

    private float getBarDragOffsetX(float x) {
        float[] bounds = internalBar.bounds();
        float xOff = bounds[0] - bounds()[0];
        return Utility.constrain(Math.max(0f, x - xOff), 0f, bounds[2]);
    }

    /**
     * Helper function.
     *
     * @param y the screen touch position on the y-axis
     * @return the bar drag offset on the y-axis
     */

    private float getBarDragOffsetY(float y) {
        float[] bounds = internalBar.bounds();
        float yOff = bounds[1] - bounds()[1];
        return Utility.constrain(Math.max(0f, y - yOff), 0f, bounds[3]);
    }

    /**
     * Helper function.
     * Return the bar dragging offset position on the x-axis or y-axis according to the bar alignment
     *
     * @param x the screen touch position on the x-axis
     * @param y the screen touch position on the y-axis
     * @return the bar dragging offset position on the x-axis or the y-axis
     */

    private float getBarDragOffset(float x, float y) {
        return vertical ? getBarDragOffsetY(y) : getBarDragOffsetX(x);
    }

    /**
     * Helper function. Updates the scroll value according to the given point
     */

    private void updateScroll(float x, float y) {
        float[] bounds = bounds();
        float scrollValue;
        if (vertical) {
            float factor = 1f - internalBar.bounds()[3] / bounds[3];
            scrollValue = factor > 0
                    ? max * (y / factor) / bounds[3]
                    : 0f;
        } else {
            float factor = 1f - internalBar.bounds()[2] / bounds[2];
            scrollValue = factor > 0
                    ? max * (x / factor) / bounds[2]
                    : 0f;
        }
        setValue(scrollValue);
    }

    /**
     * Helper method. Update the internal bar position.
     */

    private void updateInternalBarPosition() {
        if (vertical) {
            float off = 0.5f * internalBar.getHeight() / getHeight();
            internalBar.setPosition(0.5f, Utility.map(val / max, 0f, 1f, off, 1f - off));
        } else {
            float off = 0.5f * internalBar.getWidth() / getWidth();
            internalBar.setPosition(Utility.map(val / max, 0f, 1f, off, 1f - off), 0.5f);
        }
    }

    /**
     * Set the internal bar size. More specifically, set the internal bar height when it is vertical
     * and its width when it is horizontal.
     *
     * @param size the internal bar size between [0, 1]
     */

    public void setInternalBarSize(float size) {
        size = Utility.constrain(size, 0, 1f);
        if (vertical) {
            internalBar.setDimension(0.9f, size);
        } else {
            internalBar.setDimension(size, 0.9f);
        }
        updateInternalBar = true;
    }

    /**
     * @return the internal bar {@link Paint} object
     */

    public Paint getInternalBarPaint() {
        return internalBar.getPaint();
    }

    /**
     * Sets the scrollbar max value
     *
     * @param max the maximum value greater than or equal to 1
     */

    public void setMaxValue(float max) {
        this.max = Math.max(1f, max);
    }

    /**
     * Set the scrollbar value
     *
     * @param value the scrollbar value between [0, max]
     */

    public void setValue(float value) {
        val = Utility.constrain(value, 0f, max);
        updateInternalBarPosition();
    }

    /**
     * @return the current value between [0, max]
     */

    public float getValue() {
        return val;
    }

    /**
     * Scroll this scrollbar of the specified amount
     *
     * @param scrollAmount a value between [0, max]
     */

    public void scroll(float scrollAmount) {
        setValue(val + scrollAmount);
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (!isVisible && releaseLock()) {
            barDragOffset = 0f;
        }
    }

    @Override
    public void update(View parent) {
        if (updateInternalBar) {
            updateInternalBar = false;
            updateInternalBarPosition();
        }
        super.update(parent);
    }

    /**
     * Create a new vertical bar of standard dimension and geometry
     */

    private static View createHorizontalBar() {
        return new Component("SCROLLBAR_INTERNAL_BAR", 0.25f, 0.5f, 0.5f, 0.9f);
    }

    /**
     * Create a new vertical bar of standard dimension and geometry
     */

    private static View createVerticalBar() {
        return new Component("SCROLLBAR_INTERNAL_BAR", 0.5f, 0.25f, 0.9f, 0.5f);
    }
}
