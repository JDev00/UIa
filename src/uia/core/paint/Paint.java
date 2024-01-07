package uia.core.paint;

import java.util.Arrays;
import java.util.Objects;

/**
 * Paint is used to set the Shape color, stroke color and stroke width.
 * <br>
 * The Paint class acts as a bridge between the UIa framework and the third-party graphic library or framework.
 */

public final class Paint {
    public static final int WITHOUT_STROKE = 0;

    private int strokeWidth = WITHOUT_STROKE;
    private Color color = Color.createColor(255);
    private Color strokeColor = Color.createColor(0);
    private final Object[] natives = new Object[3];

    @Override
    public String toString() {
        return "Paint{" +
                "color=" + color +
                ", strokeColor=" + strokeColor +
                ", strokeWidth=" + strokeWidth +
                ", natives=" + Arrays.toString(natives) +
                '}';
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(strokeWidth);
        result = 31 * result + color.hashCode();
        result = 31 * result + strokeColor.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paint paint = (Paint) o;
        return strokeWidth == paint.strokeWidth
                && color.equals(paint.color)
                && strokeColor.equals(paint.strokeColor);
    }

    /**
     * Invalidates this Paint and force UIa to rebuild its state
     */

    public void invalidate() {
        natives[0] = null;
    }

    /**
     * @return true if this Paint has the native color
     */

    public boolean isValid() {
        return natives[0] != null;
    }

    /**
     * Sets the objects required by the platform
     *
     * @param nativeColor       the platform color
     * @param nativeStrokeColor the platform stroke color
     * @param nativeStrokeWidth the platform stroke width
     */

    public void setNative(Object nativeColor, Object nativeStrokeColor, Object nativeStrokeWidth) {
        natives[0] = nativeColor;
        natives[1] = nativeStrokeColor;
        natives[2] = nativeStrokeWidth;
    }

    /**
     * @return the platform color object
     */

    public Object getNativeColor() {
        return natives[0];
    }

    /**
     * @return the platform stroke color object
     */

    public Object getNativeStrokeColor() {
        return natives[1];
    }

    /**
     * @return the platform stroke width object
     */

    public Object getNativeStrokeWidth() {
        return natives[2];
    }

    /**
     * Applies the specified Paint to this one
     *
     * @param paint a not null {@link Paint} to copy
     * @throws NullPointerException if {@code paint == null}
     * @implNote This operation doesn't modify the platform objects
     */

    public void set(Paint paint) {
        strokeWidth = paint.strokeWidth;
        setColor(paint.getColor());
        setStrokeColor(paint.getStrokeColor());
        invalidate();
    }

    /**
     * Sets the Paint color
     *
     * @param red   the red color channel between [0,255]
     * @param green the green color channel between [0,255]
     * @param blue  the blue color channel between [0,255]
     * @param alpha the alpha color channel between [0,255]
     * @return this Paint
     */

    private Paint setColor(int red, int green, int blue, int alpha) {
        if (!Color.equals(color, red, green, blue, alpha)) {
            color = Color.createColor(red, green, blue, alpha);
            invalidate();
        }
        return this;
    }

    /**
     * Sets the Paint color.
     * <br>
     * More formally, copies the given color and applies the copy to this Paint color.
     *
     * @param color a not null {@link Color}
     * @return this Paint
     * @throws NullPointerException if {@code color == null}
     */

    public Paint setColor(Color color) {
        return setColor(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha()
        );
    }

    /**
     * @return the Paint color
     */

    public Color getColor() {
        return color;
    }

    /**
     * Sets the Paint stroke color
     *
     * @param red   the red channel between [0,255]
     * @param green the green channel between [0,255]
     * @param blue  the blue channel between [0,255]
     * @return this Paint
     */

    private Paint setStrokeColor(int red, int green, int blue) {
        if (!Color.equals(strokeColor, red, green, blue, 255)) {
            strokeColor = Color.createColor(red, green, blue);
            invalidate();
        }
        return this;
    }

    /**
     * Sets the Paint stroke color
     *
     * @param color a not null {@link Color}
     * @throws NullPointerException if {@code color == null}
     */

    public Paint setStrokeColor(Color color) {
        return setStrokeColor(
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        );
    }

    /**
     * @return the Paint stroke color
     */

    public Color getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the Paint stroke width
     *
     * @param strokeWidth a value {@code >= 0}
     * @throws IllegalArgumentException if {@code strokeWidth < 0}
     */

    public Paint setStrokeWidth(int strokeWidth) {
        if (strokeWidth < 0) {
            throw new IllegalArgumentException("the stroke width value must be greater than or equal to 0");
        }
        if (this.strokeWidth != strokeWidth) {
            this.strokeWidth = strokeWidth;
            invalidate();
        }
        return this;
    }

    /**
     * @return the stroke width
     */

    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * @return true if {@link #getStrokeWidth()} > 0
     */

    public boolean hasStroke() {
        return strokeWidth != WITHOUT_STROKE;
    }

    /**
     * Checks if the given Paints have the same color
     *
     * @param paint1 a not null {@link Paint}
     * @param paint2 a not null {@link Paint}
     * @return true if the specified Paints have the same color
     * @throws NullPointerException if {@code paint1 == null or paint2 == null}
     */

    public static boolean haveTheSameColor(Paint paint1, Paint paint2) {
        Objects.requireNonNull(paint1);
        Objects.requireNonNull(paint2);
        return paint1.equals(paint2);
    }
}
