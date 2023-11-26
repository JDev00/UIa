package uia.develop;

import uia.core.Shape;
import uia.core.basement.Drawable;
import uia.core.ui.View;
import uia.core.Paint;
import uia.core.Geometry;
import uia.core.ui.Graphic;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.physical.theme.ThemeDarcula;
import uia.physical.ComponentGroup;
import uia.physical.WrapperView;
import uia.physical.Component;
import uia.physical.ComponentText;
import uia.utility.Utility;

import java.util.function.Function;

import static java.lang.Math.*;
import static uia.utility.TrigTable.*;

/**
 * Progressbar
 */

public class UIBar extends WrapperView {
    private final Paint paintLine;
    private final Shape shapeLine;
    private final ViewText viewText;

    private Function<String, String> fun;

    private float val;
    private float min;
    private float max;

    public UIBar(View view) {
        super(new ComponentGroup(view));

        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);
        getPaint().setColor(ThemeDarcula.W_BACKGROUND);

        shapeLine = new Shape();

        paintLine = new Paint().setColor(ThemeDarcula.W_FOREGROUND);

        viewText = new ComponentText(new Component("TEXT", 0.5f, 1.25f, 0.225f, 0.5f));
        viewText.setGeometry(g -> Drawable.buildRect(g, viewText.getWidth(), viewText.getHeight(), 0.5f), true);
        viewText.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewText.setAlign(ComponentText.AlignY.CENTER);
        viewText.getPaint().setColor(ThemeDarcula.BACKGROUND);
        viewText.getTextPaint().setColor(ThemeDarcula.TEXT);
        viewText.getFont().setSize(17f);

        ViewGroup group = getView();
        group.setClip(false);
        ViewGroup.insert(group, viewText);

        fun = s -> Utility.limitDecimals(s, 1);

        setRange(0f, 100f);
        setValue(val);
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
            setValue(val);
        }
    }

    /**
     * Set the current value for this bar
     *
     * @param value the value to assign to this bar
     */

    public void setValue(float value) {
        val = Utility.constrain(value, min, max);
        viewText.setText(fun.apply(String.valueOf(val)));
    }

    private void updateShape() {
        float[] bounds = bounds();
        float w = getWidth(), h = getHeight();
        float cos = cos(bounds[4]), sin = sin(bounds[4]);

        Geometry geometry = shapeLine.getGeometry();
        geometry.clear();
        geometry.addVertex(0, 0);
        //geometry.addVertex(mp, 0);
        //geometry.addVertex(mp, 1);
        geometry.addVertex(0, 1);

        shapeLine.setPosition(
                bounds[0] + bounds[2] / 2f - rotX(w, h, cos, sin) / 2f,
                bounds[1] + bounds[3] / 2f - rotY(w, h, cos, sin) / 2f
        );
        shapeLine.setDimension(2 * w, 2 * h);
        shapeLine.setRotation(bounds[4]);
    }

    private void updateTextBounds() {
        /*float h = 1.33f * viewText.getTextHeight() / desc[1];
        float yh = 0.75f * h + abs(rotX(0, 0.5f * viewText.getTextWidth() / desc[1], cos(-desc[2]), sin(-desc[2])));

        viewText.setDimension(
                1.33f * viewText.getTextWidth() / getWidth(),
                1.33f * viewText.getTextHeight() / desc[1]
        );
        viewText.setPosition(mp, 1f + yh);
        viewText.setRotation(-desc[2]);*/
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        float mp = abs(val - min) / abs(max - min);

        updateShape();
        updateTextBounds();
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        graphic.setPaint(paintLine);
        graphic.setClip(shapeLine);
        graphic.drawShape(shapeLine);
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
     * @return the {@link Paint} used to color the progress line
     */

    public Paint getPaintLine() {
        return paintLine;
    }

    /**
     * @return the {@link ViewText} object
     */

    public ViewText getText() {
        return viewText;
    }
}
