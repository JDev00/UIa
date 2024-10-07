package uia.core.ui;

/**
 * A ViewText is a specialised View responsible for drawing text on the screen.
 */

public interface ViewText extends View {

    /**
     * Sets a placeholder to display when no text is available.
     *
     * @param placeholder the placeholder to be displayed; it could be null
     */

    void setPlaceholder(String placeholder);

    /**
     * Sets a text to be displayed.
     * <br>
     * A null String will reset the text and all its attributes.
     *
     * @param text the text to be displayed; it could be null
     */

    void setText(String text);

    /**
     * @return the displayed text
     */

    String getText();

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

    static int countLines(String text) {
        int lines = 0;
        if (text != null) {
            char[] chars = text.toCharArray();
            for (char i : chars) {
                if (i == '\n') {
                    lines++;
                }
            }
        }
        return lines;
    }
}
