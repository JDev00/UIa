package uia.core.platform;

import uia.core.policy.Paint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PaintAWT implements Paint {
    private java.awt.Paint c0;

    private java.awt.Paint paintStroke;

    private TYPE type;

    private final List<float[]> colors;

    private boolean strokeColor = false;

    public PaintAWT() {
        colors = new ArrayList<>(1);
    }

    private float[] next() {
        float[] arr = new float[4];
        colors.add(arr);
        return arr;
    }

    @Override
    public void setNative(Object o) {
        if (o instanceof java.awt.Paint) {
            c0 = (java.awt.Paint) o;

            colors.clear();

            if (o instanceof Color) {
                type = TYPE.LINEAR;
                colors.add(((Color) o).getComponents(next()));
            } else if (o instanceof GradientPaint) {
                type = TYPE.GRADIENT;
                GradientPaint g = (GradientPaint) o;
                colors.add(g.getColor1().getComponents(next()));
                colors.add(g.getColor2().getComponents(next()));
            } else if (o instanceof LinearGradientPaint) {
                type = TYPE.GRADIENT_LINEAR;
                LinearGradientPaint g = (LinearGradientPaint) o;

                for (Color i : g.getColors()) {
                    colors.add(i.getComponents(next()));
                }

            } else if (o instanceof RadialGradientPaint) {
                type = TYPE.GRADIENT_RADIAL;
                RadialGradientPaint g = (RadialGradientPaint) o;

                for (Color i : g.getColors()) {
                    colors.add(i.getComponents(next()));
                }
            } else {
                type = TYPE.OTHER;
            }
        }
    }

    @Override
    public java.awt.Paint getNative() {
        return c0;
    }

    @Override
    public void enableStrokeColor(boolean strokeColor) {
        this.strokeColor = strokeColor;

        if (strokeColor && paintStroke == null)
            paintStroke = Color.BLACK;
    }

    @Override
    public boolean hasStrokeColor() {
        return strokeColor;
    }

    @Override
    public void setStrokeColor(float r, float g, float b) {
        paintStroke = new Color((int) r, (int) g, (int) b);
    }

    @Override
    public java.awt.Paint getStrokeNative() {
        return paintStroke;
    }

    @Override
    public TYPE getType() {
        return type;
    }

    @Override
    public List<float[]> getColors() {
        return colors;
    }
}
