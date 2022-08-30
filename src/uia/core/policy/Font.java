package uia.core.policy;

/**
 * Font ADT.
 * <br>
 * Since every platform/implementation has its own Font object, this object is intended to create
 * an abstraction between native Font and framework Font.
 */

public interface Font extends Bundle {

    /**
     * Font styles
     */
    enum STYLE {PLAIN, BOLD, ITALIC}

    /**
     * Set the font size
     *
     * @param size a value {@code > 0}
     */

    void setSize(float size);

    /**
     * @return if exists, the font metrics otherwise null
     */

    Object getMetrics();

    /**
     * @return the Font size
     */

    float getSize();

    /**
     * @return the font ascent
     */

    float getAscent();

    /**
     * @return the font descent
     */

    float getDescent();

    /**
     * @return the font leading
     */

    float getLeading();

    /**
     * @return a line height
     */

    float getLineHeight();

    /**
     * @param c a char to measure
     * @return the width in pixels of the given char
     */

    float getWidth(char c);

    /**
     * @param in  a byte array to measure
     * @param off the first position used to measure the array
     * @param len the number of bytes to measure
     * @return the width in pixels of the given array of bytes
     */

    float getWidth(byte[] in, int off, int len);

    /**
     * @param in  a char array to measure
     * @param off the first position used to measure the array
     * @param len the number of chars to measure
     * @return the width in pixels of the given array of chars
     */

    float getWidth(char[] in, int off, int len);

    /**
     * @param in a String to measure
     * @return the width in pixels of the given String
     */

    float getWidth(String in);
}
