package uia.core;

/**
 * Platform font abstraction.
 * <br>
 * The Font class acts as an adapter: the native font must be set with {@link #setNative(Object, FontMetrics)}
 */

public class Font {

    private static final FontMetrics PUPPET_METRICS = new FontMetrics() {
        @Override
        public float getAscent() {
            return 0;
        }

        @Override
        public float getDescent() {
            return 0;
        }

        @Override
        public float getLeading() {
            return 0;
        }

        @Override
        public float getWidth(char c) {
            return 0;
        }

        @Override
        public float getWidth(char[] in, int off, int len) {
            return 0;
        }
    };

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
    private float leadingFactor = 1f;

    private FontMetrics fontMetrics = PUPPET_METRICS;
    private Object nativeFont;

    public Font(String name, STYLE style, float size) {
        this.name = name;
        this.style = style;
        this.size = size;
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
     * @param metrics a {@link FontMetrics} object
     */

    public void setNative(Object o, FontMetrics metrics) {
        nativeFont = o;
        fontMetrics = metrics == null ? PUPPET_METRICS : metrics;
    }

    /**
     * @return the native Font object
     */

    public Object getNative() {
        return nativeFont;
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
        return fontMetrics.getAscent();
    }

    /**
     * @return the font's descent
     */

    public float getDescent() {
        return fontMetrics.getDescent();
    }

    /**
     * @return the leading value, eventually multiplied by the leadingFactor
     */

    public float getLeading() {
        return leadingFactor * fontMetrics.getLeading();
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
        return fontMetrics.getWidth(c);
    }

    /**
     * @param in  a not null array to measure
     * @param off the initial position offset
     * @param len the amount of chars to measure
     * @return the chars cumulative width in pixel
     * @throws NullPointerException      if {@code in == null}
     * @throws IndexOutOfBoundsException if {@code (off < 0 or off >= in.length) or (len < 0 or len > in.length - offset)}
     */

    public float getWidth(char[] in, int off, int len) {
        return fontMetrics.getWidth(in, off, len);
    }
}
