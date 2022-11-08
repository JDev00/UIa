package uia.core.platform.independent;

import uia.core.platform.policy.FontMetrics;

/**
 * Font bridge implementation
 */

public class Font extends NativeBuilder {
    /**
     * PUPPET_METRICS is an empty FontMetrics' implementation.
     * <br>
     * It is used when a null FontMetrics object is passed in with {@link #setNative(Object, FontMetrics)}
     */
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
        public float getHeight() {
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

        @Override
        public float getWidth(String in) {
            return 0;
        }

        @Override
        public Object getNative() {
            return null;
        }
    };

    /**
     * Font styles
     */
    public enum STYLE {PLAIN, BOLD, ITALIC}

    private String name;
    private STYLE style;
    private float size;

    private FontMetrics fontMetrics;
    private Object nativeFont;

    public Font(String name, STYLE style, float size) {
        this.name = name;
        this.style = style;
        this.size = size;
        fontMetrics = PUPPET_METRICS;
        request();
    }

    /**
     * Change the Font's name. This causes the Font rebuilding.
     *
     * @param name a not null String
     */

    public Font setName(String name) {
        if (name != null) {
            this.name = name;
            request();
        }
        return this;
    }

    /**
     * Change the Font's style. This causes the Font rebuilding.
     *
     * @param style a not null {@link STYLE}
     */

    public Font setStyle(STYLE style) {
        if (style != null) {
            this.style = style;
            request();
        }
        return this;
    }

    /**
     * Change the Font's size. This causes the Font rebuilding.
     *
     * @param size a value greater than zero
     */

    public Font setSize(float size) {
        if (size > 0) {
            this.size = size;
            request();
        }
        return this;
    }

    /**
     * Set the native Font and FontMetrics objects
     *
     * @param o           the native Font object
     * @param fontMetrics the native FontMetrics object
     */

    public void setNative(Object o, FontMetrics fontMetrics) {
        nativeFont = o;
        this.fontMetrics = fontMetrics != null ? fontMetrics : PUPPET_METRICS;

        if (o == null) {
            request();
        } else {
            deny();
        }
    }

    /**
     * @return the native Font object
     */

    public Object getNative() {
        return nativeFont;
    }

    /**
     * @return the Font's name
     */

    public String getName() {
        return name;
    }

    /**
     * @return the Font {@link STYLE}
     */

    public STYLE getStyle() {
        return style;
    }

    /**
     * @return the Font size
     */

    public float getSize() {
        return size;
    }

    /**
     * @see FontMetrics#getWidth(char)
     */

    public float getWidth(char c) {
        return fontMetrics.getWidth(c);
    }

    /**
     * @see FontMetrics#getWidth(char[], int, int)
     */

    public float getWidth(char[] in, int off, int len) {
        return fontMetrics.getWidth(in, off, len);
    }

    /**
     * @see FontMetrics#getWidth(String)
     */

    public float getWidth(String in) {
        return fontMetrics.getWidth(in);
    }

    /**
     * @return the associated {@link FontMetrics}
     */

    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }
}
