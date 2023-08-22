package uia.application;

import uia.core.ui.View;
import uia.core.Paint;
import uia.core.Geom;
import uia.core.basement.Graphic;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.utils.Figure;
import uia.application.theme.ThemeDarcula;
import uia.physical.ComponentGroup;
import uia.physical.wrapper.WrapperView;
import uia.physical.Component;
import uia.physical.ComponentText;
import uia.utils.Utils;

import java.util.function.Function;

import static java.lang.Math.*;
import static uia.utils.TrigTable.*;

/**
 * Progressbar
 */

public class UIBar extends WrapperView {
    private final Paint paintLine;

    private final Geom line;

    private final ViewText viewText;

    private Function<String, String> fun;

    private float val;
    private float min;
    private float max;

    public UIBar(View view) {
        super(new ComponentGroup(view));

        buildGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, 1f, v.desc()[0] / v.desc()[1]), true);
        getPaint().setColor(ThemeDarcula.W_BACKGROUND);

        line = new Geom();

        paintLine = new Paint().setColor(ThemeDarcula.W_FOREGROUND);

        viewText = new ComponentText(new Component("TEXT", 0.5f, 1.25f, 0.225f, 0.5f));
        viewText.buildGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, 0.5f, v.desc()[0] / v.desc()[1]), true);
        viewText.setConsumer(CONSUMER.POINTER, false);
        viewText.setAlign(ComponentText.AlignY.CENTER);
        viewText.getPaint().setColor(ThemeDarcula.BACKGROUND);
        viewText.getTextPaint().setColor(ThemeDarcula.TEXT);
        viewText.getFont().setSize(17f);

        ViewGroup group = (ViewGroup) getView();
        group.setClip(false);
        group.add(viewText);


        fun = s -> Utils.limitDecimals(s, 1);

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
        val = Utils.constrain(value, min, max);
        viewText.setText(fun.apply(String.valueOf(val)));
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        float mp = abs(val - min) / abs(max - min);

        // update geometry
        float[] bounds = bounds();
        float[] desc = desc();
        float cos = cos(bounds[4]);
        float sin = sin(bounds[4]);

        line.clear();
        line.addVertex(0, 0);
        line.addVertex(mp, 0);
        line.addVertex(mp, 1);
        line.addVertex(0, 1);
        line.setBounds(
                bounds[0] + bounds[2] / 2f - rotX(desc[0], desc[1], cos, sin) / 2f,
                bounds[1] + bounds[3] / 2f - rotY(desc[0], desc[1], cos, sin) / 2f,
                2 * desc[0], 2 * desc[1], bounds[4]);

        // update text bounds
        float w = 1.33f * viewText.getTextWidth() / desc[0];
        float h = 1.33f * viewText.getTextHeight() / desc[1];
        float yh = 0.75f * h + abs(rotX(0, 0.5f * viewText.getTextWidth() / desc[1], cos(-desc[2]), sin(-desc[2])));

        viewText.setDimension(w, h);
        viewText.setPosition(mp, 1f + yh);
        viewText.setRotation(-desc[2]);
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        graphic.setPaint(paintLine);
        graphic.setClip(getGeom());
        graphic.drawGeometry(line);
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

    public ViewText getViewText() {
        return viewText;
    }
}
