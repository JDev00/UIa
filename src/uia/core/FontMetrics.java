package uia.core;

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
     * @param c a char to measure
     * @return the width in pixels of the given char
     */

    float getWidth(char c);

    /**
     * @param in  a char array to measure
     * @param off the first position used to measure the array
     * @param len the number of chars to measure
     * @return the width in pixels of the given array of chars
     */

    float getWidth(char[] in, int off, int len);
}