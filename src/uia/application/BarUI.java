package uia.application;

import uia.core.architecture.ui.View;
import uia.core.Paint;
import uia.core.Geom;
import uia.core.architecture.Graphic;
import uia.physical.ui.wrapper.WrapperView;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentText;
import uia.utils.TrigTable;
import uia.utils.Utils;

import java.util.function.Function;

import static java.lang.Math.*;

/**
 * UI progressbar
 */

@Deprecated
public class BarUI extends WrapperView {
    private Paint paintLine;

    private final Geom line;

    private final ComponentText textView;

    private Function<String, String> fun;

    private float val;
    private float min;
    private float max;

    private boolean vertical = true;
    private boolean userMovable = true;

    public BarUI(View view) {
        super(view);
        getPaint().setColor(170, 170, 170);
        /*addEvent((EventHover) (v, pointers) -> {
            if (userMovable) {

                for (Pointer i : pointers) {

                    if (i.getAction().equals(Pointer.ACTION.PRESSED)) {
                        float off = min + abs(max - min);
                        //float py = Utils.map(v.height() - i.getY(), 0f, v.height(), -.02f, 1.02f);
                        //float px = Utils.map(i.getX(), 0f, v.width(), -.02f, 1.02f);
                        //setValue(off * (vertical ? py : px));
                        break;
                    } else if (scroller.update(i)) {
                        setValue(min + scroller.getValue());
                        break;
                    }
                }
            }
        });*/

        line = new Geom();

        paintLine = new Paint().setColor(255, 0, 0);

        textView = new ComponentText(new Component("TextBar", 0f, 0f, 0f, 0f));
        textView.setConsumer(CONSUMER.POINTER, false);
        textView.setAlign(ComponentText.AlignY.CENTER);
        textView.getPaint().setColor(0, 0, 0, 1);
        textView.getFont().setSize(18f);


        fun = s -> Utils.limitDecimals(s, 1);

        setRange(0f, 1f);
        setValue(val);
    }

    /*
     * Decide if this barView must be vertical or horizontal
     *
     * @param vertical true to set the progressBar vertical
     *

    public void setVertical(boolean vertical) {
        if (this.vertical != vertical) {
            this.vertical = vertical;
            setRotation(vertical ? -TrigTable.HALF_PI : TrigTable.HALF_PI);
        }
    }

    /*
     * Decide if this bar can be moved by user
     *
     * @param userMovable true to let user move this bar
     *

    public void setUserMovable(boolean userMovable) {
        this.userMovable = userMovable;
    }*/

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
            //scroller.setMax(abs(max - min));
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
        //scroller.setValue(val - min);
        textView.setText(fun.apply(String.valueOf(val)));
    }

    /*
     * Set a new {@link Scroller}
     *
     * @param scroller a not null Scroller
     *

    public void setScroller(Scroller scroller) {
        if (scroller != null) this.scroller = scroller;
    }

    /*
     * Set the scroll factor.
     * <br>
     * A scroll factor is used to control the amount of scrolled pixels
     *
     * @param scrollFactor a float value
     *

    public void setScrollFactor(float scrollFactor) {
        scroller.setFactor(scrollFactor);
    }*/

    /**
     * Set the progress line's Paint
     *
     * @param paint a not null {@link Paint}
     */

    public void setPaintLine(Paint paint) {
        paintLine = paint;
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        // adjust scrolling factor
        //if (scroller.getFactor() > 0) scroller.setFactor(-1 * scroller.getFactor());

        float[] bounds = bounds();
        //line.x = bounds[0];
        //line.y = bounds[1];
        float sin = TrigTable.sin(bounds[4]);
        float cos = TrigTable.cos(bounds[4]);
        line.x = bounds[0] + TrigTable.rotX(bounds[2], bounds[3], sin, cos) / 2f;
        line.y = bounds[1] + TrigTable.rotY(bounds[2], bounds[3], sin, cos) / 2f;

        line.scaleX = bounds[2] / 2f;
        line.scaleY = bounds[3] / 2f;
        line.rotation = bounds[4];

        //textView.setCenter(x(), y());
        //textView.update();
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        float top = 2 * abs(val - min) / abs(max - min);

        line.clear();
        line.addVertex(-1, -1);
        line.addVertex(1, -1);
        line.addVertex(1, 1);
        line.addVertex(-1, 1);

        //float oldRot = bounds()[4];
        //setRotation(0f);

        graphic.setPaint(paintLine);
        //graphic.setClip(getGeom());
        graphic.drawGeometry(line);
        //graphic.restoreClip();

        //setRotation(oldRot);

        textView.draw(graphic);
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

    /*
     * @return the associated {@link Scroller}
     *

    public Scroller getScroller() {
        return scroller;
    }*/
}
