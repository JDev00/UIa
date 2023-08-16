package uia.core;

import uia.utils.Utils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Paint is used to set geometry color, stroke color and stroke width.
 * <br>
 * The Paint class acts as an adapter, the native objects are stored inside it.
 */

public class Paint {
    public static final int NO_STROKE = 0;

    private final int[] color = {255, 255, 255, 255};
    private final int[] colorStroke = {0, 0, 0};
    private int strokeWidth = NO_STROKE;

    public final Object[] natives = new Object[3];

    /**
     * Invalidate this Paint and force the system to rebuild its state
     */

    public void invalidate() {
        natives[0] = null;
    }

    /**
     * @return true if this Paint has a native color set
     */

    public boolean isValid() {
        return natives[0] != null;
    }

    /**
     * Set the native platform objects
     *
     * @param nativeColor       the native platform color
     * @param nativeStrokeColor the native platform stroke color
     * @param nativeStrokeWidth the native platform stroke width
     */

    public void setNative(Object nativeColor, Object nativeStrokeColor, Object nativeStrokeWidth) {
        natives[0] = nativeColor;
        natives[1] = nativeStrokeColor;
        natives[2] = nativeStrokeWidth;
    }

    /**
     * Set color and stroke of the given Paint to this one.
     * <br>
     * <i>This operation doesn't modify native objects.</i>
     *
     * @param paint a not null {@link Paint} to copy
     */

    public void set(Paint paint) {
        if (paint != null) {
            System.arraycopy(paint.color, 0, color, 0, color.length);
            System.arraycopy(paint.colorStroke, 0, colorStroke, 0, colorStroke.length);
            strokeWidth = paint.strokeWidth;
            invalidate();
        }
    }

    /**
     * Set the stroke width
     *
     * @param strokeWidth a value {@code >= 0}
     */

    public Paint setStrokeWidth(int strokeWidth) {
        if (this.strokeWidth != strokeWidth) {
            this.strokeWidth = Math.max(strokeWidth, NO_STROKE);
            invalidate();
        }
        return this;
    }

    /**
     * Set color
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     * @param a the alpha channel between [0,255]
     */

    public Paint setColor(int r, int g, int b, int a) {
        if (color[0] != r || color[1] != g || color[2] != b || color[3] != a) {
            color[0] = Utils.constrain(r, 0, 255);
            color[1] = Utils.constrain(g, 0, 255);
            color[2] = Utils.constrain(b, 0, 255);
            color[3] = Utils.constrain(a, 0, 255);
            invalidate();
        }
        return this;
    }

    /**
     * Set color
     *
     * @param grey a value between [0, 255]
     */

    public Paint setColor(int grey) {
        return setColor(grey, grey, grey, 255);
    }

    /**
     * Set color
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     */

    public Paint setColor(int r, int g, int b) {
        return setColor(r, g, b, 255);
    }

    /**
     * Set stroke color
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     */

    public Paint setStrokeColor(int r, int g, int b) {
        if (colorStroke[0] != r || colorStroke[1] != g || colorStroke[2] != b) {
            colorStroke[0] = Utils.constrain(r, 0, 255);
            colorStroke[1] = Utils.constrain(g, 0, 255);
            colorStroke[2] = Utils.constrain(b, 0, 255);
            invalidate();
        }
        return this;
    }

    /**
     * Set stroke color
     *
     * @param grey a value between [0, 255]
     */

    public Paint setStrokeColor(int grey) {
        return setStrokeColor(grey, grey, grey);
    }

    /**
     * @return the native color
     */

    public Object getNativeColor() {
        return natives[0];
    }

    /**
     * @return the native stroke color
     */

    public Object getNativeStrokeColor() {
        return natives[1];
    }

    /**
     * @return the native stroke width
     */

    public Object getNativeStrokeWidth() {
        return natives[2];
    }

    /**
     * @return the stroke width
     */

    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * @return the red channel
     */

    public int getRed() {
        return color[0];
    }

    /**
     * @return the green channel
     */

    public int getGreen() {
        return color[1];
    }

    /**
     * @return the blue channel
     */

    public int getBlue() {
        return color[2];
    }

    /**
     * @return the alpha channel
     */

    public int getAlpha() {
        return color[3];
    }

    /**
     * @return the stroke red channel
     */

    public int getStrokeRed() {
        return colorStroke[0];
    }

    /**
     * @return the stroke green channel
     */

    public int getStrokeGreen() {
        return colorStroke[1];
    }

    /**
     * @return the stroke blue channel
     */

    public int getStrokeBlue() {
        return colorStroke[2];
    }

    /**
     * @return true if {@code strokeWidth() != NO_STROKE}
     */

    public boolean hasStroke() {
        return strokeWidth != NO_STROKE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paint paint = (Paint) o;
        return strokeWidth == paint.strokeWidth
                && Arrays.equals(color, paint.color)
                && Arrays.equals(colorStroke, paint.colorStroke)
                && Arrays.equals(natives, paint.natives);
    }

    /**
     * Check if the object is equals (in a less manner than standard equals) to this one.
     * <br>
     * Loose equals doesn't consider native object in equality check.
     *
     * @return true if the given object is (loosely) equals to this one
     */

    public boolean looseEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paint paint = (Paint) o;
        return strokeWidth == paint.strokeWidth
                && Arrays.equals(color, paint.color)
                && Arrays.equals(colorStroke, paint.colorStroke);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(strokeWidth);
        result = 31 * result + Arrays.hashCode(color);
        result = 31 * result + Arrays.hashCode(colorStroke);
        result = 31 * result + Arrays.hashCode(natives);
        return result;
    }
}
