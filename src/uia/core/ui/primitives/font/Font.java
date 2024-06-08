package uia.core.ui.primitives.font;

import java.util.Objects;

/**
 * UIa text font abstraction.
 */

public class Font {
    private static final FontMeasure NO_MEASURE = (offset, length, text) -> 0;

    /**
     * Default desktop font size.
     */

    public static final float DESKTOP_SIZE = 21f;

    /**
     * Font style representation.
     */

    public enum FontStyle {PLAIN, BOLD, ITALIC}

    private FontMeasure measure = NO_MEASURE;

    private FontStyle fontStyle;
    private String name;
    private float leadingFactor = 1f;
    private float descent = 1f;
    private float leading = 1f;
    private float ascent = 1f;
    private float size;

    private boolean isValid = false;

    public Font(String name, FontStyle fontStyle, float size) {
        this.fontStyle = fontStyle;
        this.name = name;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Font font = (Font) o;
        return Float.compare(size, font.size) == 0
                && Float.compare(leadingFactor, font.leadingFactor) == 0
                && Objects.equals(name, font.name)
                && fontStyle == font.fontStyle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fontStyle, size, leadingFactor);
    }

    @Override
    public String toString() {
        return "Font{" +
                "name='" + name + '\'' +
                ", style=" + fontStyle +
                ", size=" + size +
                ", ascent=" + ascent +
                ", descent=" + descent +
                ", leading=" + leading +
                ", leadingFactor=" + leadingFactor +
                ", measure=" + measure +
                '}';
    }

    @Override
    protected Object clone() {
        return new Font(getName(), getStyle(), getSize());
    }

    /**
     * Invalidates this font and force UIa to rebuild it.
     */

    public void invalidate() {
        isValid = false;
    }

    /**
     * @return true if the native object has been set
     */

    public boolean isValid() {
        return isValid;
    }

    /**
     * Builds this Font with the given data.
     *
     * @param ascent  the font ascent
     * @param descent the font descent
     * @param leading the font leading
     * @param measure the {@link FontMeasure} object
     * @throws NullPointerException if {@code measure == null}
     */

    public void buildFont(float ascent, float descent, float leading, FontMeasure measure) {
        Objects.requireNonNull(measure);

        this.isValid = true;
        this.measure = measure;
        this.ascent = ascent;
        this.descent = descent;
        this.leading = leading;
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
        fontStyle = font.fontStyle;
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
     * @param fontStyle the new font {@link FontStyle}; if null, nothing is done
     * @return this font
     */

    public Font setStyle(FontStyle fontStyle) {
        if (fontStyle != null) {
            this.fontStyle = fontStyle;
            invalidate();
        }
        return this;
    }

    /**
     * @return the font {@link FontStyle}
     */

    public FontStyle getStyle() {
        return fontStyle;
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

    public static Font createDesktopFont(FontStyle fontStyle) {
        Objects.requireNonNull(fontStyle);
        return new Font("Arial", fontStyle, Font.DESKTOP_SIZE);
    }
}
