package uia.core.rendering.font;

/**
 * FontMeasure ADT.
 * <br>
 * FontMeasure is responsible for measuring text.
 */

public interface FontMeasure {

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
