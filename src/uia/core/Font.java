package uia.core;

import java.util.Objects;

/**
 * Font abstraction.
 * <br>
 * The Font class acts as an adapter: the native font must be set with
 * {@link #setNative(Object, float, float, float, Measure)}.
 */

public class Font {
    private static final Measure NO_MEASURE = (offset, length, text) -> 0;

    /**
     * Default desktop font size.
     */

    public static final float DESKTOP_SIZE = 21f;

    /**
     * Font style representation.
     */

    public enum Style {PLAIN, BOLD, ITALIC}

    private Measure measure = NO_MEASURE;
    private Object nativeFont;

    private Style style;
    private String name;
    private float size;
    private float ascent = 1f;
    private float descent = 1f;
    private float leading = 1f;
    private float leadingFactor = 1f;

    public Font(String name, Style style, float size) {
        this.name = name;
        this.style = style;
        this.size = size;
    }

    /**
     * Measure ADT.
     * <br>
     * Measure is responsible for measuring text based on a given Font.
     */

    public interface Measure {

        /**
         * Measures the given text and returns its length in pixels.
         *
         * @param text   the text to be measured
         * @param offset the position (index) of the first character to be taken into account when measuring text
         * @param length the number of text characters to be measured
         * @return the width in pixels of the given array
         */

        float width(int offset, int length, char... text);
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

    @Override
    public String toString() {
        return "Font{" +
                "name='" + name + '\'' +
                ", style=" + style +
                ", size=" + size +
                ", ascent=" + ascent +
                ", descent=" + descent +
                ", leading=" + leading +
                ", leadingFactor=" + leadingFactor +
                ", measure=" + measure +
                '}';
    }

    /**
     * Invalidates this font and force UIa to rebuild its state.
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
     * Sets the native Font and the {@link Measure} object.
     *
     * @param nativeFont the native Font object
     * @param ascent     the font ascent
     * @param descent    the font descent
     * @param leading    the font leading
     * @param measure    the font {@link Measure} object
     */

    public void setNative(Object nativeFont, float ascent, float descent, float leading, Measure measure) {
        this.nativeFont = nativeFont;
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
     * Copies and sets the specified font.
     *
     * @param font a {@link Font} to be copied
     * @return this font
     * @throws NullPointerException if {@code font == null}
     */

    public Font set(Font font) {
        Objects.requireNonNull(font);

        name = font.name;
        size = font.size;
        style = font.style;
        leadingFactor = font.leadingFactor;
        measure = null;
        invalidate();
        return this;
    }

    /**
     * Changes font name.
     *
     * @param name the new font name; if null, nothing is done
     * @return this font
     */

    public Font setName(String name) {
        if (name != null) {
            this.name = name;
            invalidate();
        }
        return this;
    }

    /**
     * @return the font name
     */

    public String getName() {
        return name;
    }

    /**
     * Changes font style.
     *
     * @param style the new font {@link Style}; if null, nothing is done
     * @return this font
     */

    public Font setStyle(Style style) {
        if (style != null) {
            this.style = style;
            invalidate();
        }
        return this;
    }

    /**
     * @return the font {@link Style}
     */

    public Style getStyle() {
        return style;
    }

    /**
     * Changes font size.
     *
     * @param size the new font size; if it is less than 1, nothing is done
     * @return this font
     */

    public Font setSize(float size) {
        if (size > 0) {
            this.size = size;
            invalidate();
        }
        return this;
    }

    /**
     * @return the font size
     */

    public float getSize() {
        return size;
    }

    /**
     * Sets the leading multiplier factor.
     *
     * @param leadingFactor a value {@code > 0} used to multiply the current font leading value
     * @return this font
     */

    public Font setLeadingFactor(float leadingFactor) {
        this.leadingFactor = leadingFactor;
        return this;
    }

    /**
     * @return the font ascent value
     */

    public float getAscent() {
        return ascent;
    }

    /**
     * @return the font descent value
     */

    public float getDescent() {
        return descent;
    }

    /**
     * @return the leading value multiplied by the leadingFactor
     */

    public float getLeading() {
        return leadingFactor * leading;
    }

    /**
     * @return the height of a line of text in pixels
     */

    public float getLineHeight() {
        return size + getLeading();
    }

    /**
     * @return the width in pixels of the provided character
     */

    public float getWidth(char character) {
        return measure.width(0, 1, character);
    }

    /**
     * Returns the width in pixels of the given text.
     *
     * @param offset the initial position offset
     * @param length the amount of chars to measure
     * @param text   the text to be measured
     * @return the width in pixels of the provided text
     * @throws NullPointerException      if {@code text == null}
     * @throws IndexOutOfBoundsException when:
     *                                   <ul>
     *                                       <li>offset < 0</li>
     *                                       <li>offset >= text.length</li>
     *                                       <li>length < 0</li>
     *                                       <li>length > text.length - offset</li>
     *                                   </ul>
     */

    public float getWidth(int offset, int length, char... text) {
        return measure.width(offset, length, text);
    }

    /**
     * Creates a new desktop Font of "Arial" type.
     *
     * @return a new {@link Font} object
     * @throws NullPointerException if {@code style == null}
     */

    public static Font createDesktopFont(Style style) {
        Objects.requireNonNull(style);
        return new Font("Arial", style, Font.DESKTOP_SIZE);
    }
}
