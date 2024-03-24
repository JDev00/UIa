package uia.core.paint;

import uia.physical.theme.Theme;

import java.util.Arrays;
import java.util.Objects;

/**
 * Paint is used to set the geometry colour, stroke colour, stroke width and text colour.
 * <br>
 * The Paint class acts as a bridge between the UIa framework and the third-party graphic library/framework.
 * <br><br>
 * The Paint object must contain at least the geometry color. Optional attributes include:
 * <ul>
 *     <li>geometry stroke color;</li>
 *     <li>geometry stroke width;</li>
 *     <li>text color.</li>
 * </ul>
 */

public final class Paint {
    public static final int WITHOUT_STROKE = 0;

    private final Object[] natives;
    private int strokeWidth;
    private Color color;
    private Color textColor;
    private Color strokeColor;

    public Paint() {
        natives = new Object[4];

        strokeWidth = WITHOUT_STROKE;
        textColor = Theme.BLACK;
        color = Theme.WHITE;
        strokeColor = null;
    }

    @Override
    public String toString() {
        return "Paint{" +
                "color=" + color +
                ", textColor=" + textColor +
                ", strokeColor=" + strokeColor +
                ", strokeWidth=" + strokeWidth +
                '}';
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(strokeWidth, color, textColor, strokeColor);
        result = 31 * result + Arrays.hashCode(natives);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paint paint = (Paint) o;
        return strokeWidth == paint.strokeWidth
                && Arrays.equals(natives, paint.natives)
                && Objects.equals(color, paint.color)
                && Objects.equals(textColor, paint.textColor)
                && Objects.equals(strokeColor, paint.strokeColor);
    }

    /**
     * Invalidates this Paint and force UIa to rebuild its state.
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
     * Sets the objects required by the platform.
     *
     * @param nativeColor       the platform color
     * @param nativeTextColor   the platform text color
     * @param nativeStrokeColor the platform stroke color
     * @param nativeStrokeWidth the platform stroke width
     */

    public void setNative(Object nativeColor,
                          Object nativeTextColor,
                          Object nativeStrokeColor,
                          Object nativeStrokeWidth) {
        natives[0] = nativeColor;
        natives[1] = nativeTextColor;
        natives[2] = nativeStrokeColor;
        natives[3] = nativeStrokeWidth;
    }

    /**
     * @return the platform color object
     */

    public Object getNativeColor() {
        return natives[0];
    }

    /**
     * @return the platform text color object
     */

    public Object getNativeTextColor() {
        return natives[1];
    }

    /**
     * @return the platform stroke color object
     */

    public Object getNativeStrokeColor() {
        return natives[2];
    }

    /**
     * @return the platform stroke width object
     */

    public Object getNativeStrokeWidth() {
        return natives[3];
    }

    /**
     * Applies the specified Paint to this one.
     *
     * @param paint a not null {@link Paint} to copy
     * @return this Paint
     * @throws NullPointerException if {@code paint == null}
     * @implNote This method doesn't modify the platform objects
     */

    public Paint set(Paint paint) {
        Objects.requireNonNull(paint);

        setStrokeWidth(paint.getStrokeWidth());
        setStrokeColor(paint.getStrokeColor());
        setTextColor(paint.getTextColor());
        setColor(paint.getColor());
        return this;
    }

    /**
     * Sets the geometry color.
     *
     * @param color a not null {@link Color}
     * @return this Paint
     * @throws NullPointerException if {@code color == null}
     */

    public Paint setColor(Color color) {
        Objects.requireNonNull(color);
        if (!this.color.equals(color)) {
            this.color = Color.copy(color);
            invalidate();
        }
        return this;
    }

    /**
     * @return the geometry color
     */

    public Color getColor() {
        return color;
    }

    /**
     * Sets the text color.
     *
     * @param color the text {@link Color}; it could be null
     * @return this Paint
     */

    public Paint setTextColor(Color color) {
        if (!Objects.equals(this.textColor, color)) {
            this.textColor = null;
            if (color != null) {
                this.textColor = Color.copy(color);
            }
            invalidate();
        }
        return this;
    }

    /**
     * @return the text color or null if the text color has not been set
     */

    public Color getTextColor() {
        return textColor;
    }

    /**
     * Sets the geometry stroke color.
     *
     * @param color the stroke {@link Color}; it could be null
     * @return this Paint
     */

    public Paint setStrokeColor(Color color) {
        if (!Objects.equals(this.strokeColor, color)) {
            this.strokeColor = null;
            if (color != null) {
                this.strokeColor = Color.copy(color);
            }
            invalidate();
        }
        return this;
    }

    /**
     * @return the stroke colour of the geometry or null if the stroke colour is not set
     */

    public Color getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the geometry stroke width.
     *
     * @param strokeWidth a value {@code >= 0}
     * @return this Paint
     * @throws IllegalArgumentException if {@code strokeWidth < 0}
     */

    public Paint setStrokeWidth(int strokeWidth) {
        if (strokeWidth < 0) {
            throw new IllegalArgumentException("stroke width must be >= 0");
        }
        if (this.strokeWidth != strokeWidth) {
            this.strokeWidth = strokeWidth;
            invalidate();
        }
        return this;
    }

    /**
     * @return the geometry stroke width
     */

    public int getStrokeWidth() {
        return strokeWidth;
    }
}
