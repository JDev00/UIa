package uia.core.widget;

import uia.core.View;
import uia.core.event.Event;
import uia.core.gesture.Scroller;
import uia.core.gesture.WheelScroller;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
import uia.core.widget.text.TextView;
import uia.utils.TrigTable;
import uia.utils.Utils;

import java.util.function.Function;

import static java.lang.Math.*;

/**
 * UI progressbar
 */

public class BarView extends View {
    private Scroller scroller;

    private Paint paintLine;

    private final Shape line;

    private final TextView textView;

    private Function<String, String> fun;

    private float val;
    private float min;
    private float max;

    private boolean vertical = true;
    private boolean userMovable = true;

    public BarView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.buildShape(s -> Figure.rect(s, Figure.STD_VERT, 0.25f), true);
        getPaint().setColor(170, 170, 170);
        getEventQueue().addEvent((v, s) -> {
            if (userMovable && s == Event.POINTER_HOVER) {
                PointerEvent e = v.getPointerEvent();

                if (e.isMousePressed()) {
                    float off = min + abs(max - min);
                    float py = Utils.map(v.height() - e.getY(), 0f, v.height(), -.02f, 1.02f);
                    float px = Utils.map(e.getX(), 0f, v.width(), -.02f, 1.02f);
                    setValue(off * (vertical ? py : px));
                } else if (scroller.update(e)) {
                    setValue(min + scroller.getValue());
                }
            }
        });

        scroller = new WheelScroller();

        line = new Shape(4);

        paintLine = new Paint(255, 0, 0);

        textView = new TextView(context, 0, 0, width, height);
        textView.getPaint().setColor(0, 0, 0, 1);
        textView.getFont().setSize(18f);
        textView.setAlignY(TextView.AlignY.CENTER);
        textView.setPointerConsumer(false);


        fun = s -> Utils.decLimit(s, 1);

        setRange(0f, 1f);
        setValue(val);
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
     * Decide if this bar can be moved by user
     *
     * @param userMovable true to let user move this bar
     */

    public void setUserMovable(boolean userMovable) {
        this.userMovable = userMovable;
    }

    /**
     * Set the text for this bar
     *
     * @param fun a not null {@link Function}
     */

    public void setText(Function<String, String> fun) {
        if (fun != null) {
            this.fun = fun;
            setValue(getValue());
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
        textView.setText(fun.apply(String.valueOf(val)));
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
     * Set the progress line's {@link Paint}
     *
     * @param paint a not null Paint
     */

    public void setPaintLine(Paint paint) {
        if (paint != null) paintLine = paint;
    }

    @Override
    protected void update() {
        super.update();

        // adjust scrolling factor
        if (scroller.getFactor() > 0) scroller.setFactor(-1 * scroller.getFactor());

        textView.setCenter(x(), y());
        View.update(textView);
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        float top = 2 * abs(val - min) / abs(max - min);

        line.clear();
        line.addVertex(-1, 1 - top);
        line.addVertex(1, 1 - top);
        line.addVertex(1, 1);
        line.addVertex(-1, 1);

        float oldRot = rotation();
        setRotation(0f);

        line.setPosition(x(), y());
        line.setDimension(width(), height());
        line.setRotation(oldRot);

        graphic.setPaint(paintLine);
        graphic.setClip(getShape());
        line.draw(graphic);
        graphic.restoreClip();

        setRotation(oldRot);

        View.draw(textView, graphic);
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
     * @return true if this bar can be moved by the user
     */

    public boolean isUserMovable() {
        return userMovable;
    }

    /**
     * @return the {@link Paint} used to color the progress line
     */

    public Paint getPaintLine() {
        return paintLine;
    }

    /**
     * @return the associated {@link Scroller}
     */

    public Scroller getScroller() {
        return scroller;
    }
}
