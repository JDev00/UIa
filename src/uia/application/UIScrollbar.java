package uia.application;

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
 * Standard UI library component.
 * <br>
 * UIScrollbar represents a default scrollbar. It can be set vertical or horizontal at the creation time.
 * It consists of two graphical elements:
 * <ul>
 *     <li>an external bar that is accessed and modified using the standard interface;</li>
 *     <li>an internal bar that represents the current scroll value.</li>
 * </ul>
 * The internal bar can be accessed and modified with {@link #getInternalBar()}.
 * <br>
 * By design, UIScrollbar natively supports dragging and moving the internal bar. The only operation that you have to
 * implement on your own is the scrolling caused by a mouse wheeling event.
 * <br>
 * To keep this component as simple as possible, the UIScrollbar value (accessed with {@link #getValue()})
 * is bounded between [0, 1].
 *
 * @apiNote UIScrollbar doesn't fully support rotation, so rotate it carefully.
 */

public class UIScrollbar extends WrapperView {
    private final View internalBar;
    private float val;
    private float barDragOffset;
    private boolean locked = false;
    private final boolean vertical;

    public UIScrollbar(View view, boolean vertical) {
        super(new ComponentGroup(view));

        this.vertical = vertical;

        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);
        getPaint().setColor(Theme.DARK_GREY);
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
                    //
                    barDragOffset = getBarDragOffset(touch.getX(), touch.getY());
                }
                updateScroll(touch.getX(), touch.getY() - barDragOffset);
            }
            if (touch.getAction().equals(ScreenTouch.Action.RELEASED)) {
                if (locked) {
                    locked = false;
                    barDragOffset = 0f;
                    sendMessage(EventTouchScreenMessage.releaseLock(getID()));
                }
            }
        });
        registerCallback((OnMouseExit) o -> {
            if (locked) {
                locked = false;
                barDragOffset = 0f;
                sendMessage(EventTouchScreenMessage.releaseLock(getID()));
            }
        });

        internalBar = vertical
                ? createVerticalBar()
                : createHorizontalBar();
        internalBar.setConsumer(Consumer.SCREEN_TOUCH, false);
        internalBar.getPaint().setColor(Theme.LIGHT_GREY);

        ViewGroup group = getView();
        group.setClip(false);
        group.add(internalBar);
    }

    private float getBarDragOffsetX(float x) {
        float[] bounds = internalBar.bounds();
        float xOff = bounds[0] - bounds()[0];
        return Utility.constrain(Math.max(0f, x - xOff), 0f, bounds[2]);
    }

    private float getBarDragOffsetY(float y) {
        float[] bounds = internalBar.bounds();
        float yOff = bounds[1] - bounds()[1];
        return Utility.constrain(Math.max(0f, y - yOff), 0f, bounds[3]);
    }

    private float getBarDragOffset(float x, float y) {
        return vertical ? getBarDragOffsetY(y) : getBarDragOffsetX(x);
    }

    private void updateScroll(float x, float y) {
        float[] bounds = bounds();
        float scrollValue;
        if (vertical) {
            float yFactor = 1f - internalBar.bounds()[3] / bounds[3];
            scrollValue = (y / yFactor) / bounds[3];
        } else {
            float xFactor = 1f - internalBar.bounds()[2] / bounds[2];
            scrollValue = (x / xFactor) / bounds[2];
        }
        setValue(scrollValue);
    }

    /**
     * @return the internal bar
     */

    public View getInternalBar() {
        return internalBar;
    }

    /**
     * Set the scrollbar value
     *
     * @param value the scrollbar value between [0, 1]
     */

    public void setValue(float value) {
        val = Utility.constrain(value, 0f, 1f);

        if (vertical) {
            float off = 0.5f * internalBar.getHeight() / getHeight();
            internalBar.setPosition(0.5f, Utility.map(val, 0f, 1f, off, 1f - off));
        } else {
            float off = 0.5f * internalBar.getWidth() / getWidth();
            internalBar.setPosition(Utility.map(val, 0f, 1f, off, 1f - off), 0.5f);
        }
    }

    /**
     * Scroll this scrollbar of the specified amount
     *
     * @param scrollAmount a value between [0, 1]
     */

    public void scroll(float scrollAmount) {
        setValue(val + scrollAmount);
    }

    /**
     * @return the current value between [0, 1]
     */

    public float getValue() {
        return val;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (!isVisible) locked = false;
    }

    /**
     * Create a new vertical bar of standard dimension and geometry
     */

    private static View createHorizontalBar() {
        View out = new Component("SCROLLBAR_INTERNAL_BAR", 0.25f, 0.5f, 0.5f, 0.98f);
        out.setGeometry(
                g -> Drawable.buildRect(g, out.getWidth(), out.getHeight(), 1f),
                true
        );
        return out;
    }

    /**
     * Create a new vertical bar of standard dimension and geometry
     */

    private static View createVerticalBar() {
        View out = new Component("SCROLLBAR_INTERNAL_BAR", 0.5f, 0.25f, 0.85f, 0.5f);
        out.setGeometry(
                g -> Drawable.buildRect(g, out.getWidth(), out.getHeight(), 1f),
                true
        );
        return out;
    }
}
