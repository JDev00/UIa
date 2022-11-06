package uia.core.platform.policy;

/**
 * FontMetrics ADT
 */

public interface FontMetrics {

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

    float getHeight();

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

    /**
     * @return if exists, the native font metrics otherwise null
     */

    Object getNative();
}