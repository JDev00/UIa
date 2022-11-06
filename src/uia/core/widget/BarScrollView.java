package uia.core.widget;

import uia.core.View;
import uia.core.event.Event;
import uia.core.gesture.Scroller;
import uia.core.gesture.WheelScroller;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
import uia.core.platform.policy.Pointer;
import uia.utils.TrigTable;
import uia.utils.Utils;

import static java.lang.Math.abs;

/**
 * UI scrollbar
 */

public class BarScrollView extends View {
    private View cursor;

    private Scroller scroller;

    private float val;
    private float min;
    private float max;

    private boolean vertical = true;
    private boolean locked = false;

    public BarScrollView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.buildShape(s -> Figure.rect(s, Figure.STD_VERT, 1f), true);
        getPaint().setColor(190);
        getEventQueue()
                .addEvent((v, s) -> {
                    PointerEvent e = v.getPointerEvent();

                    if (s == Event.POINTER_HOVER) {
                        if (e.isMousePressed()) {
                            locked = true;
                        } else if (scroller.update(e)) {
                            setValue(min + scroller.getValue());
                        }
                    }
                });

        cursor = new View(context, 0f, 0f, 1f, 1f);
        cursor.setExpansion(0f, 0f);
        cursor.setPointerConsumer(false);
        cursor.setPositionAdjustment(false);
        cursor.setDimensionAdjustment(false);
        cursor.buildShape(s -> Figure.rect(s, Figure.STD_VERT, 1f), true);
        cursor.getPaint().setColor(255, 0, 0);

        scroller = new WheelScroller();

        setRange(0f, 1f);
        setValue(val);
    }

    private void updateValue(float x, float y) {
        float off = min + abs(max - min);
        float py = Utils.map(y - y() + height() / 2f, 0f, height(), -.05f, 1.05f);
        float px = Utils.map(x - x() + width() / 2f, 0f, width(), -.05f, 1.05f);
        setValue(off * (vertical ? py : px));
    }

    /**
     * Decide if this barView must be vertical or horizontal
     *
     * @param vertical true to set the progressBar vertical
     */

    public void setVertical(boolean vertical) {
        if (this.vertical != vertical) {
            this.vertical = vertical;
            setRotation(vertical ? -TrigTable.HALF_PI : TrigTable.HALF_PI);
        }
    }

    /**
     * Set the range [min, max] for this bar
     *
     * @param min the minimum value
     * @param max the maximum value
     */

    public void setRange(float min, float max) {
        if (min < max) {
            this.min = min;
            this.max = max;
            scroller.setMax(abs(max - min));
            setValue(val);
        }
    }

    /**
     * Set the current value for this bar
     *
     * @param value the value to assign to this bar
     */

    public void setValue(float value) {
        val = Utils.constrain(value, min, max);
        scroller.setValue(val - min);
    }

    /**
     * Set a new {@link Scroller}
     *
     * @param scroller a not null Scroller
     */

    public void setScroller(Scroller scroller) {
        if (scroller != null) this.scroller = scroller;
    }

    /**
     * Set the scroll factor.
     * <br>
     * A scroll factor is used to control the amount of scrolled pixels
     *
     * @param scrollFactor a float value
     */

    public void setScrollFactor(float scrollFactor) {
        scroller.setFactor(scrollFactor);
    }

    /**
     * Test!
     * <br>
     * Scroll this scrollbar of one scrollerFactor unit
     *
     * @param up true to scroll it up
     * @see #getScrollerFactor()
     */

    public void scroll(boolean up) {
        if (up) {
            setValue(getValue() - scroller.getFactor());
        } else {
            setValue(getValue() + scroller.getFactor());
        }
    }

    /**
     * Set a new View to use as a cursor.
     * <br>
     * Note that the following methods will be set to false:
     * <br>
     * 1) {@link #setPointerConsumer(boolean)}
     * <br>
     * 2) {@link #setPositionAdjustment(boolean)}
     * <br>
     * 3) {@link #setDimensionAdjustment(boolean)}
     *
     * @param view a not null {@link View}
     */

    public void setCursor(View view) {
        if (view != null) {
            cursor = view;
            cursor.setPointerConsumer(false);
            cursor.setPositionAdjustment(false);
            cursor.setDimensionAdjustment(false);
        }
    }

    /**
     * Set the cursor's Paint to use when this bar is locked
     *
     * @param paint a not null {@link Paint}
     */

    @Deprecated
    public void setPaintLocked(Paint paint) {
        //if (paint != null) lockedPaint = paint;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        cursor.setVisible(isVisible);
        // reset lock state
        if (!isVisible) locked = false;
    }

    @Override
    protected void updateKeys(boolean update) {
        View.updateKeys(cursor, update);
        super.updateKeys(update);
    }

    @Override
    protected void updatePointers(boolean update) {
        Context context = getContext();
        Pointer p = getContext().pointers()[0];

        if (locked && !p.isConsumed() && context.isMousePressed()) {
            updateValue(p.getX(), p.getY());
            p.consume();
        }

        View.updatePointers(cursor, update);
        super.updatePointers(update);
    }

    @Override
    protected void update() {
        super.update();

        locked = locked && getContext().isMousePressed();

        // adjust scrolling factor
        if (scroller.getFactor() < 0) scroller.setFactor(-1 * scroller.getFactor());

        float top = 2 * abs(val - min) / abs(max - min);
        float k = 0.25f;
        float t = Utils.map(top, 0f, 2f, k, 2 - k) - 1;

        if (vertical) {
            cursor.setDimension(width(), k * height());
            cursor.setPosition(x(), y() + t * height() / 2f);
        } else {
            cursor.setDimension(k * width(), height());
            cursor.setPosition(x() + t * width() / 2f, y());
        }

        View.update(cursor);
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        graphic.setClip(getShape());
        View.draw(cursor, graphic);
        graphic.restoreClip();
    }

    /**
     * @return the minimum value
     */

    public float getMin() {
        return min;
    }

    /**
     * @return the maximum value
     */

    public float getMax() {
        return max;
    }

    /**
     * @return the current value
     */

    public float getValue() {
        return val;
    }

    /**
     * @return the current scroller factor
     */

    public float getScrollerFactor() {
        return scroller.getFactor();
    }

    /**
     * @return true if this bar is vertical
     */

    public boolean isVertical() {
        return vertical;
    }

    /**
     * @return true if this bar is currently locked
     */

    public boolean isLocked() {
        return locked;
    }

    /**
     * @return the View used as cursor
     */

    public View getCursor() {
        return cursor;
    }

    /**
     * @return the {@link Paint} used when this bra is locked
     */

    @Deprecated
    public Paint getLockedPaint() {
        return null;//lockedPaint;
    }

    /**
     * @return the associated {@link Scroller}
     */

    public Scroller getScroller() {
        return scroller;
    }
}
