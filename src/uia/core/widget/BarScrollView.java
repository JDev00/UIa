package uia.core.widget;

import uia.core.View;
import uia.core.event.Event;
import uia.core.gesture.Scroller;
import uia.core.gesture.WheelScroller;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.shape.Shape;
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
    private Scroller scroller;

    private Paint lockedPaint;
    private Paint paintLine;

    private final Shape line;

    private float val;
    private float min;
    private float max;

    private boolean vertical = true;
    private boolean locked = false;

    public BarScrollView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        getPaint().setColor(170, 170, 170);
        getEventQueue().addEvent((v, s) -> {
            PointerEvent e = v.getPointerEvent();

            if (s == Event.POINTER_HOVER) {
                if (e.isMousePressed()) {
                    locked = true;
                } else if (scroller.update(e)) {
                    setValue(min + scroller.getValue());
                }
            }
        });

        scroller = new WheelScroller();

        line = new Shape(4);
        Figure.rect(line);

        lockedPaint = new Paint(180, 0, 0);

        paintLine = new Paint(255, 0, 0);

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
     * Set the progress line's Paint
     *
     * @param paint a not null {@link Paint}
     */

    public void setPaintLine(Paint paint) {
        if (paint != null) paintLine = paint;
    }

    /**
     * Set the Paint to use when this bar is locked
     *
     * @param paint a not null {@link Paint}
     */

    public void setPaintLocked(Paint paint) {
        if (paint != null) lockedPaint = paint;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        // reset lock state
        if (!isVisible) locked = false;
    }

    @Override
    protected void updatePointers(boolean update) {
        Context context = getContext();
        Pointer p = getContext().pointers()[0];

        if (locked && !p.isConsumed() && context.isMousePressed()) {
            updateValue(p.getX(), p.getY());
            p.consume();
        }

        super.updatePointers(update);
    }

    @Override
    protected void update() {
        super.update();

        locked = locked && getContext().isMousePressed();

        // adjust scrolling factor
        if (scroller.getFactor() < 0) scroller.setFactor(-1 * scroller.getFactor());

        float top = 2 * abs(val - min) / abs(max - min);
        float k = 0.1f;
        float t = Utils.map(top, 0f, 2f, k, 2 - k) - 1;

        if (vertical) {
            line.setDimension(width(), k * height());
            line.setPosition(x(), y() + t * height() / 2f);
        } else {
            line.setDimension(k * width(), height());
            line.setPosition(x() + t * width() / 2f, y());
        }
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        graphic.setPaint(locked ? lockedPaint : paintLine);
        graphic.setClip(getShape());
        line.draw(graphic);
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
     * @return the {@link Paint} used to color the progress line
     */

    public Paint getPaintLine() {
        return paintLine;
    }

    /**
     * @return the {@link Paint} used when this bra is locked
     */

    public Paint getLockedPaint() {
        return lockedPaint;
    }

    /**
     * @return the associated {@link Scroller}
     */

    public Scroller getScroller() {
        return scroller;
    }
}
