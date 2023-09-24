package uia.core.ui;

import uia.core.Font;
import uia.core.Paint;

/**
 * ViewText ADT.
 * <br>
 * A ViewText is a specialised View responsible for drawing a text on the screen.
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
     * Set a new Font to this View
     *
     * @param font a not null {@link Font} object
     */

    void setFont(Font font);

    /**
     * @return the {@link Font} object used to display text
     */

    Font getFont();

    /**
     * Single line functionality ensures that the entire text will be drawn on a single line.
     *
     * @param singleLine true to draw text on a single line
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
     * Set a String to display.
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
     * Scroll text along x-axis and y-axis.
     *
     * @param x the scroll value along x-axis
     * @param y the scroll value along y-axis
     */

    void scrollText(float x, float y);

    /**
     * @return the text with (the longest line of text) in pixel
     */

    float getTextWidth();

    /**
     * @return the text height in pixel
     */

    float getTextHeight();

    /**
     * Count the number of break lines contained inside the given String.
     * If the given String is null, then return 0.
     *
     * @return the number of break lines inside the given String
     */

    static int countLines(String string) {
        int lines = 0;
        if (string != null) {
            char[] chars = string.toCharArray();
            for (char i : chars) {
                if (i == '\n') lines++;
            }
        }
        return lines;
    }
}
