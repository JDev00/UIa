package uia.core;

import java.util.Objects;

/**
 * Platform font abstraction.
 * <br>
 * The Font class acts as an adapter: the native font must be set with {@link #setNative(Object, Measure, float, float, float)}
 */

public class Font {

    private static final Measure NO_MEASURE = (off, len, in) -> 0;

    /**
     * Default Desktop Font size
     */
    public static final float FONT_SIZE_DESKTOP = 21f;

    /**
     * Font styles
     */
    public enum STYLE {PLAIN, BOLD, ITALIC}

    private String name;
    private STYLE style;
    private float size;

    private float ascent = 1f;
    private float descent = 1f;
    private float leading = 1f;
    private float leadingFactor = 1f;

    private Measure measure = NO_MEASURE;
    private Object nativeFont;

    public Font(String name, STYLE style, float size) {
        this.name = name;
        this.style = style;
        this.size = size;
    }

    /**
     * Measure ADT
     */

    public interface Measure {

        /**
         * @param in  an array of chars to measure
         * @param off the first position used to measure the array
         * @param len the number of chars to measure
         * @return the width in pixels of the given array
         */

        float width(int off, int len, char... in);
    }

    /**
     * Invalidate this Font and force the system to rebuild its state
     */

    public void invalidate() {
        nativeFont = null;
    }

    /**
     * @return true if the native object has been set
     */

    public boolean isValid() {
        return nativeFont != null;
    }

    /**
     * Set the native Font and FontMetrics objects
     *
     * @param o       the native Font object
     * @param measure a {@link Measure} object
     */

    public void setNative(Object o, Measure measure,
                          float ascent, float descent, float leading) {
        nativeFont = o;
        this.measure = (measure == null) ? NO_MEASURE : measure;
        this.ascent = ascent;
        this.descent = descent;
        this.leading = leading;
    }

    /**
     * @return the native Font object
     */

    public Object getNative() {
        return nativeFont;
    }

    /**
     * Copy and set the given Font
     *
     * @param font a not null {@link Font} to copy
     * @throws NullPointerException if {@code font == null}
     */

    public Font set(Font font) {
        name = font.name;
        size = font.size;
        style = font.style;
        leadingFactor = font.leadingFactor;
        measure = null;
        invalidate();
        return this;
    }

    /**
     * Change the Font's name
     *
     * @param name a not null String
     */

    public Font setName(String name) {
        if (name != null) {
            this.name = name;
            invalidate();
        }
        return this;
    }

    /**
     * @return the Font's name
     */

    public String getName() {
        return name;
    }

    /**
     * Change the Font's style
     *
     * @param style a not null {@link STYLE}
     */

    public Font setStyle(STYLE style) {
        if (style != null) {
            this.style = style;
            invalidate();
        }
        return this;
    }

    /**
     * @return the Font {@link STYLE}
     */

    public STYLE getStyle() {
        return style;
    }

    /**
     * Change the Font's size
     *
     * @param size a value greater than zero
     */

    public Font setSize(float size) {
        if (size > 0) {
            this.size = size;
            invalidate();
        }
        return this;
    }

    /**
     * @return the Font size
     */

    public float getSize() {
        return size;
    }

    /**
     * Set the leading multiple factor
     *
     * @param leadingFactor a value {@code > 0} used to multiply the current font leading value
     */

    public Font setLeadingFactor(float leadingFactor) {
        this.leadingFactor = leadingFactor;
        return this;
    }

    /**
     * @return the font's ascent
     */

    public float getAscent() {
        return ascent;
    }

    /**
     * @return the font's descent
     */

    public float getDescent() {
        return descent;
    }

    /**
     * @return the leading value, eventually multiplied by the leadingFactor
     */

    public float getLeading() {
        return leadingFactor * leading;
    }

    /**
     * @return the height of a line of text in pixel
     */

    public float getLineHeight() {
        return size + getLeading();
    }

    /**
     * @return the given char's width in pixel
     */

    public float getWidth(char c) {
        return measure.width(0, 1, c);
    }

    /**
     * @param off the initial position offset
     * @param len the amount of chars to measure
     * @param in  a not null array to measure
     * @return the chars cumulative width in pixel
     * @throws NullPointerException      if {@code in == null}
     * @throws IndexOutOfBoundsException if {@code (off < 0 or off >= in.length) or (len < 0 or len > in.length - offset)}
     */

    public float getWidth(int off, int len, char... in) {
        return measure.width(off, len, in);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Font font = (Font) o;
        return Float.compare(size, font.size) == 0
                && Float.compare(leadingFactor, font.leadingFactor) == 0
                && Objects.equals(name, font.name)
                && style == font.style;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, style, size, leadingFactor);
    }
}
