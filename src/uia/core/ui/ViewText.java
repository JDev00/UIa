package uia.core.ui;

import uia.core.Font;
import uia.core.Paint;

/**
 * ViewText ADT.
 * <br>
 * A ViewText is a specialised View responsible for drawing text on the screen.
 */

public interface ViewText extends View {

    /**
     * Align text along x-axis
     */
    enum AlignX {LEFT, CENTER, RIGHT}

    /**
     * Align text along y-axis
     */
    enum AlignY {TOP, CENTER}

    /**
     * Set the text alignment along x-axis
     *
     * @param alignX a not null {@link AlignX}
     */

    void setAlign(AlignX alignX);

    /**
     * @return the text alignment along x-axis
     */

    AlignX getAlignX();

    /**
     * Set the text alignment along y-axis
     *
     * @param alignY a not null {@link AlignY}
     */

    void setAlign(AlignY alignY);

    /**
     * @return the text alignment along y-axis
     */

    AlignY getAlignY();

    /**
     * @return the {@link Paint} object used to set the graphical text properties
     */

    Paint getTextPaint();

    /**
     * Sets a new Font for text
     *
     * @param font a not null {@link Font} object
     */

    void setFont(Font font);

    /**
     * @return the {@link Font} object used to display text
     */

    Font getFont();

    /**
     * Single line functionality displays all the text on a single line.
     *
     * @param singleLine true to display text on a single line
     */

    void setSingleLine(boolean singleLine);

    /**
     * @return true if single line functionality is enabled
     */

    boolean isSingleLine();

    /**
     * Set a description to display when no text is available.
     *
     * @param description a String; it could be null
     */

    void setDescription(String description);

    /**
     * Sets a String to display.
     * <br>
     * A null String will reset the text and all its attributes.
     *
     * @param text a String to display; it could be null
     */

    void setText(String text);

    /**
     * @return the displayed text
     */

    String getText();

    /**
     * Scrolls text on the x-axis and y-axis.
     *
     * @param x the scroll value on the x-axis
     * @param y the scroll value on the y-axis
     */

    void scrollText(float x, float y);

    /**
     * @return the scroll value on the x-axis and y-axis
     */

    float[] getScrollValue();

    /**
     * Returns the rectangle occupied by the text.
     * <br>
     * <b>Note that the top left corner is based on the window viewport and not on the component dimension.</b>
     *
     * @return the text bounds as an array of 5 elements:
     * <ul>
     *     <li>the top left corner on the x-axis;</li>
     *     <li>the top left corner on the y-axis;</li>
     *     <li>the text width (text longest line) in pixels;</li>
     *     <li>the text height in pixels;</li>
     *     <li>the text rotation in radians.</li>
     * </ul>
     */

    float[] getTextBounds();

    /**
     * Counts the number of break lines contained in the specified String.
     *
     * @return the number of break lines in the specified String or 0 if it is null
     */

    static int countLines(String string) {
        int lines = 0;
        if (string != null) {
            char[] chars = string.toCharArray();
            for (char i : chars) {
                if (i == '\n') {
                    lines++;
                }
            }
        }
        return lines;
    }
}
